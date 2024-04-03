package com.custom.magic;

import com.custom.MoreMagic;
import com.custom.magic.magichit.gold;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.logging.Logger;

public class MagicEntity extends ThrownItemEntity {

    private boolean isLumos = false;
    private BlockPos LastLumosBlockPos;
    private long LumosAge = 0;

    public MagicEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);

        this.setNoGravity(true);
        this.setItem(Items.NETHER_STAR.getDefaultStack());
        this.setSwimming(true);
    }

    public MagicEntity(World world, LivingEntity owner) {
        super(MoreMagic.MagicEntityType, owner, world);
        this.setNoGravity(true);
        this.setItem(Items.NETHER_STAR.getDefaultStack());
        this.setSwimming(true);
    }
    public MagicEntity(World world, LivingEntity owner,boolean isLumos) {
        super(MoreMagic.MagicEntityType, owner, world);
        this.setItem(Items.NETHER_STAR.getDefaultStack());
        this.isLumos = isLumos;
        LastLumosBlockPos = this.getBlockPos();// 设置初始BlockPos
        this.setNoGravity(true);
        this.LumosAge = world.getTime();
        this.setSwimming(true);
    }

    public MagicEntity(World world, double x, double y, double z) {
        super(MoreMagic.MagicEntityType, x, y, z, world);
        this.setItem(Items.NETHER_STAR.getDefaultStack());
        this.setNoGravity(true);
        this.setSwimming(true);
    }

    @Override
    protected Item getDefaultItem() {
        return null; // We will configure this later, once we have created the ProjectileItem.
    }

    @Override
    protected ItemStack getItem() {
        // 返回你想要的物品
        return new ItemStack(Items.NETHER_STAR);
    }

    @Override
    public void tick() {
        super.tick();
        // 记录发射体移动过的路径
        if (!this.getWorld().isClient()&& this.isLumos) {
            //
            World world = this.getWorld();
            BlockPos blockPos = this.getBlockPos();
            if (world.getBlockState(blockPos).isAir()) {
                if (blockPos != this.LastLumosBlockPos){
                    // 如果此刻的方块位置不是上次记录的位置，就放置光源方块，并且让上一次的方块消失
                    MinecraftClient minecraft = MinecraftClient.getInstance();
                    world.setBlockState(blockPos, MoreMagic.BRIGHT_LIGHT_BLOCK.getDefaultState());
                    world.setBlockState(this.LastLumosBlockPos,Blocks.AIR.getDefaultState());
                    this.LastLumosBlockPos = blockPos;
                    // 粒子效果
                    long currentTick = world.getTime();
                    // 将 tick 值映射到 0 到 20 的范围内
                    int tickInSecond = (int) (currentTick % 20);
                    if((int)currentTick%5 == 0){
                        Particle particle = minecraft.particleManager.addParticle(ParticleTypes.END_ROD,
                                getX(), getY(), getZ(),
                                0, 0, 0);
                        particle.setColor(0.8F,0.9F,1.0F);
                        particle.setMaxAge(50);
                    }
                    minecraft.particleManager.addParticle(ParticleTypes.FLASH,
                            getX(), getY(), getZ(),
                            0, 0, 0);
                    // 5秒后清除发射体
                    if(currentTick - this.LumosAge > 200){
                        this.kill();
                    }
                }
            }
        }
    }

    protected void onBlockHit(BlockHitResult blockHitResult){
        World world = this.getWorld();
        if (world.getBlockState(this.getBlockPos()).getBlock() == Blocks.WATER) {
            // 如果碰撞的是水，取消删除发射物并返回
            return;
        }
        if(this.isLumos){
            BlockPos centerPos = this.getBlockPos();
            world.setBlockState(centerPos, Blocks.AIR.getDefaultState());

        }
        super.onBlockHit(blockHitResult);
    }

    protected void onEntityHit(EntityHitResult entityHitResult) { // called on entity hit.
        super.onEntityHit(entityHitResult);
        Entity entity = entityHitResult.getEntity(); // sets a new Entity instance as the EntityHitResult (victim)
        int i = entity instanceof BlazeEntity ? 3 : 0; // sets i to 3 if the Entity instance is an instance of BlazeEntity

        if (entity instanceof LivingEntity livingEntity) { // checks if entity is an instance of LivingEntity (meaning it is not a boat or minecart)
            gold.MagicHit(entityHitResult, this);
        }
        if(this.isLumos){
            this.kill();
        }
    }

    protected void onCollision(HitResult hitResult) { // called on collision with a block
        super.onCollision(hitResult);
        if (!this.getWorld().isClient) { // checks if the world is client
            this.getWorld().sendEntityStatus(this, (byte)3); // particle?
            this.kill(); // kills the projectile
        }

    }
}
