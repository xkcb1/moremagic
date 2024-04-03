package com.custom.magic.magichit;

import com.custom.MoreMagic;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

import java.util.Objects;
public class WizardMagicHit {
    public static void MagicHit(EntityHitResult entityHitResult, ProjectileEntity projectile){
        Entity targetEntity = entityHitResult.getEntity();
        // 确保被击中的是生物实体
        if (targetEntity instanceof LivingEntity livingTarget) {
            // 施加伤害
            // 使用弹射物造成的伤害源
            MinecraftClient minecraftClient = MinecraftClient.getInstance();
            if(Objects.equals(MoreMagic.Magic_Name, "Stupefy")){
                // 昏昏倒地
                if (minecraftClient.world != null) {
                    livingTarget.damage(new DamageSources(minecraftClient.world.getRegistryManager()).create(DamageTypes.GENERIC,projectile.getOwner()), (float) 3F);
                    double knockbackStrength = 1; // 设置击退强度，可以根据需要进行调整
                    double knockbackDirectionX = -(livingTarget.getX() - projectile.getX());
                    double knockbackDirectionZ = -(livingTarget.getZ() - projectile.getZ());
                    livingTarget.takeKnockback(knockbackStrength, knockbackDirectionX, knockbackDirectionZ);
                    StatusEffectInstance stunEffect = new StatusEffectInstance(StatusEffects.SLOWNESS, 10,2,false,false,false);
                    StatusEffectInstance stunEffect_2 = new StatusEffectInstance(StatusEffects.BLINDNESS, 5,0,false,false,false);
                    livingTarget.addStatusEffect(stunEffect);
                    livingTarget.addStatusEffect(stunEffect_2);
                }
            }else if(Objects.equals(MoreMagic.Magic_Name, "Expelliarmus")){
                // 除你武器
                ItemStack mainHandStack = livingTarget.getMainHandStack();
                if (!mainHandStack.isEmpty()) {
                    // 从实体手上移除武器
                    ItemStack dropStack = mainHandStack.copy();
                    livingTarget.setStackInHand(livingTarget.getActiveHand(), ItemStack.EMPTY);
                    World world = livingTarget.getEntityWorld();
                    world.spawnEntity(new ItemEntity(world, livingTarget.getX(), livingTarget.getY(), livingTarget.getZ(), dropStack));
                }
                livingTarget.damage(new DamageSources(minecraftClient.world.getRegistryManager()).create(DamageTypes.GENERIC,projectile.getOwner()), (float) 1.5F);
                double knockbackStrength = 0.3; // 设置击退强度，可以根据需要进行调整
                double knockbackDirectionX = -(livingTarget.getX() - projectile.getX());
                double knockbackDirectionZ = -(livingTarget.getZ() - projectile.getZ());
                livingTarget.takeKnockback(knockbackStrength, knockbackDirectionX, knockbackDirectionZ);
            } else if (Objects.equals(MoreMagic.Magic_Name, "Lumos")) {
                //Lumos 荧光闪烁
                double knockbackStrength = 0.2; // 设置击退强度，可以根据需要进行调整
                double knockbackDirectionX = -(livingTarget.getX() - projectile.getX());
                double knockbackDirectionZ = -(livingTarget.getZ() - projectile.getZ());
                livingTarget.takeKnockback(knockbackStrength, knockbackDirectionX, knockbackDirectionZ);
                livingTarget.damage(new DamageSources(minecraftClient.world.getRegistryManager()).create(DamageTypes.GENERIC,projectile.getOwner()), (float) 0.5F);
                StatusEffectInstance stunEffect = new StatusEffectInstance(StatusEffects.GLOWING, 40,0,false,false,false);
                livingTarget.addStatusEffect(stunEffect);
            }
        }
    }
}
