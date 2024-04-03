package com.custom;

import com.google.common.base.Stopwatch;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.RegistryEntryPredicateArgumentType;
import net.minecraft.command.argument.RegistryPredicateArgumentType;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureSetKeys;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.poi.PointOfInterestStorage;
import net.minecraft.world.poi.PointOfInterestType;
import org.slf4j.Logger;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.LocateCommand.sendCoordinates;

public class MyCommand {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final DynamicCommandExceptionType STRUCTURE_NOT_FOUND_EXCEPTION = new DynamicCommandExceptionType((id) -> {
        return Text.stringifiedTranslatable("commands.locate.structure.not_found", id);
    });
    private static final DynamicCommandExceptionType STRUCTURE_INVALID_EXCEPTION = new DynamicCommandExceptionType((id) -> {
        return Text.stringifiedTranslatable("commands.locate.structure.invalid", id);
    });
    private static final DynamicCommandExceptionType BIOME_NOT_FOUND_EXCEPTION = new DynamicCommandExceptionType((id) -> {
        return Text.stringifiedTranslatable("commands.locate.biome.not_found", id);
    });
    private static final DynamicCommandExceptionType POI_NOT_FOUND_EXCEPTION = new DynamicCommandExceptionType((id) -> {
        return Text.stringifiedTranslatable("commands.locate.poi.not_found", id);
    });
    private static final int LOCATE_STRUCTURE_RADIUS = 100;
    private static final int LOCATE_BIOME_RADIUS = 6400;
    private static final int LOCATE_BIOME_HORIZONTAL_BLOCK_CHECK_INTERVAL = 32;
    private static final int LOCATE_BIOME_VERTICAL_BLOCK_CHECK_INTERVAL = 64;
    private static final int LOCATE_POI_RADIUS = 256;

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder) CommandManager.literal("get_near_village").requires((source) -> {
            return source.hasPermissionLevel(2);
        })).then(CommandManager.literal("structure").then(CommandManager.argument("structure", RegistryPredicateArgumentType.registryPredicate(RegistryKeys.STRUCTURE)).executes((context) -> {
            return executeLocateStructure((ServerCommandSource)context.getSource(), RegistryPredicateArgumentType.getPredicate(context, "structure", RegistryKeys.STRUCTURE, STRUCTURE_INVALID_EXCEPTION));
        })))).then(CommandManager.literal("biome").then(CommandManager.argument("biome", RegistryEntryPredicateArgumentType.registryEntryPredicate(registryAccess, RegistryKeys.BIOME)).executes((context) -> {
            return executeLocateBiome((ServerCommandSource)context.getSource(), RegistryEntryPredicateArgumentType.getRegistryEntryPredicate(context, "biome", RegistryKeys.BIOME));
        })))).then(CommandManager.literal("poi").then(CommandManager.argument("poi", RegistryEntryPredicateArgumentType.registryEntryPredicate(registryAccess, RegistryKeys.POINT_OF_INTEREST_TYPE)).executes((context) -> {
            return executeLocatePoi((ServerCommandSource)context.getSource(), RegistryEntryPredicateArgumentType.getRegistryEntryPredicate(context, "poi", RegistryKeys.POINT_OF_INTEREST_TYPE));
        }))));
    }

    private static Optional<? extends RegistryEntryList.ListBacked<Structure>> getStructureListForPredicate(RegistryPredicateArgumentType.RegistryPredicate<Structure> predicate, Registry<Structure> structureRegistry) {
        Either var10000 = predicate.getKey();
        Function var10001 = (key) -> {
            return structureRegistry.getEntry((Integer) key).map((entry) -> {
                return RegistryEntryList.of(entry);
            });
        };
        Objects.requireNonNull(structureRegistry);
        return (Optional<? extends RegistryEntryList.ListBacked<Structure>>) var10000.map(var10001, key -> structureRegistry.getEntryList((TagKey<Structure>) key));

    }

    private static int executeLocateStructure(ServerCommandSource source, RegistryPredicateArgumentType.RegistryPredicate<Structure> predicate) throws CommandSyntaxException {
        Registry<Structure> registry = source.getWorld().getRegistryManager().get(RegistryKeys.STRUCTURE);
        RegistryEntryList<Structure> registryEntryList = (RegistryEntryList)getStructureListForPredicate(predicate, registry).orElseThrow(() -> {
            return STRUCTURE_INVALID_EXCEPTION.create(predicate.asString());
        });
        BlockPos blockPos = BlockPos.ofFloored(source.getPosition());
        ServerWorld serverWorld = source.getWorld();
        Stopwatch stopwatch = Stopwatch.createStarted(Util.TICKER);
        Pair<BlockPos, RegistryEntry<Structure>> pair = serverWorld.getChunkManager().getChunkGenerator().locateStructure(serverWorld, registryEntryList, blockPos, 100, false);
        stopwatch.stop();
        if (pair == null) {
            throw STRUCTURE_NOT_FOUND_EXCEPTION.create(predicate.asString());
        } else {
            return sendCoordinates(source, predicate, blockPos, pair, "commands.locate.structure.success", false, stopwatch.elapsed());
        }
    }

    private static int executeLocateBiome(ServerCommandSource source, RegistryEntryPredicateArgumentType.EntryPredicate<Biome> predicate) throws CommandSyntaxException {
        BlockPos blockPos = BlockPos.ofFloored(source.getPosition());
        Stopwatch stopwatch = Stopwatch.createStarted(Util.TICKER);
        Pair<BlockPos, RegistryEntry<Biome>> pair = source.getWorld().locateBiome(predicate, blockPos, 6400, 32, 64);
        stopwatch.stop();
        if (pair == null) {
            throw BIOME_NOT_FOUND_EXCEPTION.create(predicate.asString());
        } else {
            return sendCoordinates(source, predicate, blockPos, pair, "commands.locate.biome.success", true, stopwatch.elapsed());
        }
    }

    private static int executeLocatePoi(ServerCommandSource source, RegistryEntryPredicateArgumentType.EntryPredicate<PointOfInterestType> predicate) throws CommandSyntaxException {
        BlockPos blockPos = BlockPos.ofFloored(source.getPosition());
        ServerWorld serverWorld = source.getWorld();
        Stopwatch stopwatch = Stopwatch.createStarted(Util.TICKER);
        Optional<Pair<RegistryEntry<PointOfInterestType>, BlockPos>> optional = serverWorld.getPointOfInterestStorage().getNearestTypeAndPosition(predicate, blockPos, 256, PointOfInterestStorage.OccupationStatus.ANY);
        stopwatch.stop();
        if (optional.isEmpty()) {
            throw POI_NOT_FOUND_EXCEPTION.create(predicate.asString());
        } else {
            return sendCoordinates(source, predicate, blockPos, ((Pair)optional.get()).swap(), "commands.locate.poi.success", true, stopwatch.elapsed());
        }
    }

    private static String getKeyString(Pair<BlockPos, ? extends RegistryEntry<?>> result) {
        return (String)((RegistryEntry)result.getSecond()).getKey().map((key) -> {
            return key.toString();
        }).orElse("[unregistered]");
    }

    public static int sendCoordinates(ServerCommandSource source, RegistryEntryPredicateArgumentType.EntryPredicate<?> predicate, BlockPos currentPos, Pair<BlockPos, ? extends RegistryEntry<?>> result, String successMessage, boolean includeY, Duration timeTaken) {
        String string = (String)predicate.getEntry().map((entry) -> {
            return predicate.asString();
        }, (tag) -> {
            String var10000 = predicate.asString();
            return var10000 + " (" + getKeyString(result) + ")";
        });
        return sendCoordinates(source, currentPos, result, successMessage, includeY, string, timeTaken);
    }

    public static int sendCoordinates(ServerCommandSource source, RegistryPredicateArgumentType.RegistryPredicate<?> structure, BlockPos currentPos, Pair<BlockPos, ? extends RegistryEntry<?>> result, String successMessage, boolean includeY, Duration timeTaken) {
        String string = (String)structure.getKey().map((key) -> {
            return key.getValue().toString();
        }, (key) -> {
            Identifier var10000 = key.id();
            return "#" + var10000 + " (" + getKeyString(result) + ")";
        });
        return sendCoordinates(source, currentPos, result, successMessage, includeY, string, timeTaken);
    }

    private static int sendCoordinates(ServerCommandSource source, BlockPos currentPos, Pair<BlockPos, ? extends RegistryEntry<?>> result, String successMessage, boolean includeY, String entryString, Duration timeTaken) {
        BlockPos blockPos = (BlockPos)result.getFirst();
        int i = includeY ? MathHelper.floor(MathHelper.sqrt((float)currentPos.getSquaredDistance(blockPos))) : MathHelper.floor(getDistance(currentPos.getX(), currentPos.getZ(), blockPos.getX(), blockPos.getZ()));

        MoreMagic.locatePos.put(blockPos,successMessage+entryString);
        LOGGER.info("Locating element " + entryString + " took " + timeTaken.toMillis() + " ms");
        return i;
    }

    private static float getDistance(int x1, int y1, int x2, int y2) {
        int i = x2 - x1;
        int j = y2 - y1;
        return MathHelper.sqrt((float)(i * i + j * j));
    }
}
