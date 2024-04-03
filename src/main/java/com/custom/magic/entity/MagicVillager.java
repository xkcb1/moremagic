package com.custom.magic.entity;

import com.custom.MoreMagic;
import com.custom.RandomNameGenerator;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.*;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.*;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MagicVillager extends
        HostileEntity implements RangedAttackMob {
    private int shotCooldown = 0; // 射击冷却计时器
    public final MinecraftClient minecraft = MinecraftClient.getInstance();
    private static final int SHOT_COOLDOWN_TIME = 20; // 射击冷却时间，单位为tick

    public MagicVillager(EntityType<? extends MagicVillager> entityType, World world) {
        super(entityType, world);
        // RandomNameGenerator.generateRandomName(ThreadLocalRandom.current().nextInt(2, 7))
        this.setCustomName(Text.of(RandomNameGenerator.generateRandomName(ThreadLocalRandom.current().nextInt(2, 5))));
        //SkeletonEntity
    }
    public static DefaultAttributeContainer.Builder createVillageGuardAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0).add(EntityAttributes.GENERIC_MAX_HEALTH,40);
        // 自定义攻击伤害
    }

    protected void initGoals() {
        this.goalSelector.add(3, new WanderNearTargetGoal(this, 1, 32.0F));
        this.goalSelector.add(3, new WanderAroundPointOfInterestGoal(this, 1.2, false));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(8, new LookAtEntityGoal(this, MobEntity.class, 16.0F, 1.0F));
        this.goalSelector.add(0, new BowAttackGoal(this, 1.2, 1,32.0F));
        this.targetSelector.add(1, new RevengeGoal(this, new Class[0]));
        this.targetSelector.add(1, new ActiveTargetGoal(this, MobEntity.class, 16, true, false, (entity) -> {
            return entity instanceof Monster && !(entity instanceof MagicVillager);
        }));
        this.targetSelector.add(1, new UniversalAngerGoal(this, true));
    }

    @Override
    protected void initEquipment(Random random, LocalDifficulty localDifficulty) {
        super.initEquipment(random, localDifficulty);
        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        /*
        if (this.shotCooldown > 0) {
            this.shotCooldown--;
        }
        if (this.shotCooldown == 0) {
            // 获取周围的敌对生物
        }*/
    }

    protected PersistentProjectileEntity createArrowProjectile(ItemStack arrow) {
        return ProjectileUtil.createArrowProjectile(this, arrow, 1.0F);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        // 重新初始化装备
        this.initEquipment(this.getWorld().random, this.getWorld().getLocalDifficulty(this.getBlockPos()));
    }

    protected PersistentProjectileEntity createArrowProjectile(ItemStack arrow, float damageModifier) {
        return ProjectileUtil.createArrowProjectile(this, arrow, damageModifier);
    }

    public void applyExplosionForce(double x, double y, double z, double strength) {
        Box box = this.getBoundingBox().expand(9.0D); // 5格范围内的立方体区域

        // 获取区域内的所有实体，排除自身
        List<Entity> entities = getWorld().getEntitiesByClass(Entity.class, box,entity -> entity != this);
        for (Entity entity : entities) {
            if (entity != this && entity instanceof LivingEntity) {
                // 计算实体和爆炸中心的相对位置
                double deltaX = entity.getX() - x;
                double deltaY = entity.getY() - y;
                double deltaZ = entity.getZ() - z;

                // 计算施加在实体上的力的大小
                double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
                double force = 1.0 - distance / 9.0; // 强度随距离减小而减小
                if (force > 0.0) {
                    entity.addVelocity(deltaX / distance * force * strength, deltaY / distance * force * strength, deltaZ / distance * force * strength);
                    entity.damage(new DamageSources(getWorld().getRegistryManager()).create(DamageTypes.GENERIC,this),4);
                    entity.setOnFireFor(20);
                    StatusEffectInstance stunEffect = new StatusEffectInstance(StatusEffects.SLOWNESS, 60,1);
                    ((LivingEntity) entity).addStatusEffect(stunEffect);
                }
            }
        }
    }
    @Override
    public void shootAt(LivingEntity target, float pullProgress) {
        double randomNumber = random.nextDouble();
        // 如果随机数小于 0.15（15%），则满足 10% 的概率
        if (randomNumber < 0.15) {
            FireballEntity fireball = new FireballEntity(getWorld(),this,0.0F,0.0F,0.0F,1); // 创建火焰弹实体
            Vec3d look = this.getRotationVec(1.0f);
            fireball.setPos(this.getX() + look.x, this.getEyeY() + look.y, this.getZ() + look.z);
            fireball.setVelocity(this, this.getPitch(), this.getYaw(), 0.0F, 3F, 0F);
            this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 1.0F, 1.0F);
            this.getWorld().spawnEntity(fireball);
        } else if (randomNumber >= 0.15 && randomNumber <= 0.45) {
            ItemStack itemStack = this.getProjectileType(this.getStackInHand(ProjectileUtil.getHandPossiblyHolding(this, Items.BOW)));
            PersistentProjectileEntity persistentProjectileEntity = this.createArrowProjectile(itemStack, pullProgress);
            double d = target.getX() - this.getX();
            double e = target.getBodyY(0.3333333333333333) - persistentProjectileEntity.getY();
            double f = target.getZ() - this.getZ();
            double g = Math.sqrt(d * d + f * f);
            persistentProjectileEntity.setDamage(8);
            persistentProjectileEntity.setOnFire(true);
            persistentProjectileEntity.setOnFireFor(100);
            StatusEffectInstance fireResistanceEffect = new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 40);
            // 给实体添加火焰抗性效果
            this.addStatusEffect(fireResistanceEffect);
            persistentProjectileEntity.setVelocity(d, e + g * 0.20000000298023224, f, 1.6F, (float) (14 - this.getWorld().getDifficulty().getId() * 4));
            this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.getWorld().spawnEntity(persistentProjectileEntity);
        }else if (randomNumber >= 0.45 && randomNumber <= 0.6) {
            StatusEffectInstance stunEffect = new StatusEffectInstance(StatusEffects.REGENERATION, 100,2);
            StatusEffectInstance stunEffect_2 = new StatusEffectInstance(StatusEffects.RESISTANCE, 60,2);
            StatusEffectInstance fireResistanceEffect = new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 40);
            // 给实体添加火焰抗性效果
            this.addStatusEffect(fireResistanceEffect);
            this.addStatusEffect(stunEffect);
            this.addStatusEffect(stunEffect_2);
            applyExplosionForce(this.getX(),this.getY(),this.getZ(),5);
            Particle particle = minecraft.particleManager.addParticle(ParticleTypes.EXPLOSION,
                    this.getX(),this.getY(),this.getZ(),
                    0, 0, 0);
            particle.scale(3F);
        } else {
            ItemStack itemStack = this.getProjectileType(this.getStackInHand(ProjectileUtil.getHandPossiblyHolding(this, Items.BOW)));
            PersistentProjectileEntity persistentProjectileEntity = this.createArrowProjectile(itemStack, pullProgress);
            double d = target.getX() - this.getX();
            double e = target.getBodyY(0.3333333333333333) - persistentProjectileEntity.getY();
            double f = target.getZ() - this.getZ();
            double g = Math.sqrt(d * d + f * f);
            persistentProjectileEntity.setDamage(6);
            persistentProjectileEntity.setVelocity(d, e + g * 0.20000000298023224, f, 1.6F, (float) (14 - this.getWorld().getDifficulty().getId() * 4));
            this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.getWorld().spawnEntity(persistentProjectileEntity);
        }
    }
}
