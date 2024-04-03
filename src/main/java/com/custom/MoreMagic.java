package com.custom;

import com.custom.magic.*;
import com.custom.magic.entity.MagicVillager;
import com.custom.magic.entity.wizard;
import com.custom.MyCommand;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.particle.*;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.RegistryKeyArgumentType;
import net.minecraft.entity.damage.DamageSource;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.client.player.ClientPickBlockApplyCallback;
import net.fabricmc.fabric.api.event.client.player.ClientPickBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.mob.PillagerEntity;
import net.minecraft.server.command.*;
import net.minecraft.block.Blocks;
import net.minecraft.block.GrindstoneBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.vehicle.CommandBlockMinecartEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtIo;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.util.*;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.*;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkManager;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.light.LightingProvider;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.tick.QueryableTickScheduler;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.custom.occult.OccultItem;
import com.custom.witchcraft.WitchcraftItem;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import software.bernie.geckolib.GeckoLib;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Predicate;

public class MoreMagic implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Block BRIGHT_LIGHT_BLOCK = new BrightLightBlock(FabricBlockSettings.copyOf(Blocks.AIR).luminance(15)); // 创建自定义方块实例
	public static final Block BRIGHT_LIGHT_BLOCK_NO_SCHEDULE = new BrightLightBlockNoSchedule(FabricBlockSettings.copyOf(Blocks.AIR).luminance(9)); // 12亮度
	public static final Random random = new Random();
    public static final Logger LOGGER = LoggerFactory.getLogger("moremagic");
	public static final WandItem CustomWandItem = new WandItem();
	public static final OccultItem CustomOccultItem = new OccultItem();
	public static final WitchcraftItem CustomWitchcraftItem = new WitchcraftItem();
	public static ArrayList<Item> CanListenItemArray = new ArrayList<>();
	public static Map<String, String> AllMagic = new HashMap<>();
	public static ArrayList<String> AllMagicArray = new ArrayList<>();
	public static String Magic_Name = "Stupefy";
	// 材质图Item
	public static Item BackGroundImage_Item = new Item(new FabricItemSettings());
	public static final EntityType<MagicEntity> MagicEntityType = Registry.register(
			Registries.ENTITY_TYPE,
			new Identifier("magic", "magic_ball"),
			FabricEntityTypeBuilder.<MagicEntity>create(SpawnGroup.MISC, MagicEntity::new)
					.dimensions(EntityDimensions.fixed(0.25F, 0.25F)) // dimensions in Minecraft units of the projectile
					.trackRangeBlocks(6).trackedUpdateRate(6) // necessary for all thrown projectiles (as it prevents it from breaking, lol)
					.build() // VERY IMPORTANT DONT DELETE FOR THE LOVE OF GOD PSLSSSSSS
	);

	public static final EntityType<MagicVillager> MagicVillagerType = Registry.register(
			Registries.ENTITY_TYPE,
			new Identifier("magic", "magic_villager"),
			FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, MagicVillager::new)
					.dimensions(EntityDimensions.fixed(0.6F, 1.95F)) // 调整实体大小
					.build()
	);
	public static final EntityType<wizard> WizardType = Registry.register(
			Registries.ENTITY_TYPE,
			new Identifier("magic", "wizard"),
			FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, wizard::new)
					.dimensions(EntityDimensions.fixed(0.6F, 1.95F)) // 调整实体大小
					.build()
	);;

	public static final Item PackedSnowballItem = new MagicEntityItem(new Item.Settings().maxCount(16));
	public static Boolean ifPressed_Key_R = false;
	public static HashMap<BlockPos,String> locatePos = new HashMap<>();

	@Override
	public void onInitialize() {
		// Proceed with mild caution.
		LOGGER.info("main init");
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			MyCommand.register(dispatcher,registryAccess);
		});
		GeckoLib.initialize();
		// 所有魔咒
		AllMagic.put("Stupefy","Apprentice");//学徒级咒语:昏昏倒地
		AllMagicArray.add("Stupefy");
		AllMagic.put("Expelliarmus","Apprentice");//学徒级咒语:除你武器
		AllMagicArray.add("Expelliarmus");
		AllMagic.put("Lumos","Apprentice");//学徒级咒语:荧光闪烁
		AllMagicArray.add("Lumos");
		AllMagic.put("Nox","Apprentice");//学徒级咒语:诺克斯
		AllMagicArray.add("Nox");
		AllMagic.put("Protego","Apprentice");//学徒级咒语:诺克斯
		AllMagicArray.add("Protego");
		// 注册
		Registry.register(Registries.BLOCK, new Identifier("magic", "lumos"), BRIGHT_LIGHT_BLOCK);
		Registry.register(Registries.BLOCK,new Identifier("magic","lumos_no_schedule"),BRIGHT_LIGHT_BLOCK_NO_SCHEDULE);
		// 材质图Item注册
		Registry.register(Registries.ITEM,new Identifier("magic","texture_image_1"),BackGroundImage_Item);


		// 注册实体属性
		FabricDefaultAttributeRegistry.register(MagicVillagerType,MagicVillager.createVillageGuardAttributes());
		FabricDefaultAttributeRegistry.register(WizardType,wizard.createVillageGuardAttributes());
		// 注册Item
		CustomWandItem.register();
		CustomOccultItem.register();
		CustomWitchcraftItem.register();
		Registry.register(Registries.ITEM, new Identifier("magic", "magic_ball"), PackedSnowballItem);




		// 注册事件
		AttackEntityCallback.EVENT.register((player, world, hand, target, entityHitResult) -> {
			Item heldItem = player.getMainHandStack().getItem();
			if(CustomWandItem.GoldWand.contains(heldItem)){
				GoldWandNormal_attack(target,world);
			}
			return ActionResult.PASS;
		});
		// 放置结构

	}



	private void GoldWandNormal_attack(Entity target, World world) {
		// 生成一个介于 0 和 1 之间的随机数
		double randomNumber = random.nextDouble();
		// 如果随机数小于 0.15（15%），则满足 10% 的概率
		if (randomNumber < 0.15) {
			LightningEntity lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
			lightning.updatePosition(target.getX(), target.getY(), target.getZ());
			world.spawnEntity(lightning);
		}
	}
}