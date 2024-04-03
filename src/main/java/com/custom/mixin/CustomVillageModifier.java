package com.custom.mixin;

import com.custom.MoreMagic;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.JigsawBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.command.argument.RegistryPredicateArgumentType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.LocateCommand;
import net.minecraft.server.command.PlaceCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.*;
import net.minecraft.structure.pool.*;
import net.minecraft.structure.pool.alias.StructurePoolAliasLookup;
import net.minecraft.text.Text;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.collection.ArrayListDeque;
import net.minecraft.util.collection.PriorityIterator;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.ChunkRandom;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.noise.NoiseConfig;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.Structures;
import org.apache.commons.lang3.mutable.MutableObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static com.custom.MoreMagic.LOGGER;
import static com.custom.MoreMagicClient.client;
import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.LocateCommand.sendCoordinates;


/*
@Inject(method = "generate(Lnet/minecraft/world/gen/structure/Structure$Context;Lnet/minecraft/registry/entry/RegistryEntry;Ljava/util/Optional;ILnet/minecraft/util/math/BlockPos;ZLjava/util/Optional;ILnet/minecraft/structure/pool/alias/StructurePoolAliasLookup;)Ljava/util/Optional;",
            at = @At(value = "RETURN", ordinal = 2),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILSOFT)
*/


@Mixin(StructurePoolBasedGenerator.class)
public class CustomVillageModifier {

    /**
     * @author Xk
     */
    @Inject(method = "generate(Lnet/minecraft/world/gen/structure/Structure$Context;Lnet/minecraft/registry/entry/RegistryEntry;Ljava/util/Optional;ILnet/minecraft/util/math/BlockPos;ZLjava/util/Optional;ILnet/minecraft/structure/pool/alias/StructurePoolAliasLookup;)Ljava/util/Optional;",
            at = @At(value = "RETURN"),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private static void MixinGenerate(Structure.Context context, RegistryEntry<StructurePool> structurePool, Optional<Identifier> id, int size, BlockPos pos, boolean useExpansionHack, Optional<Heightmap.Type> projectStartToHeightmap, int maxDistanceFromCenter, StructurePoolAliasLookup aliasLookup, CallbackInfoReturnable<Optional<Structure.StructurePosition>> cir){
        if(! cir.getReturnValue().isEmpty()){
            /*
            System.out.println(pos);
            System.out.println(structurePool.getKey().get().getValue().toUnderscoreSeparatedString());
            System.out.println(cir.getReturnValue().toString());

             */
            //



            // 执行您想要执行的指令

        }
    }
}