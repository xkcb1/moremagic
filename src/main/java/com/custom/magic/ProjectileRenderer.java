package com.custom.magic;

import com.custom.MoreMagic;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.SnowballItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;

import java.util.Objects;

class ProjectileRenderer {

    public static void renderProjectile(ProjectileEntity projectile, Entity player) {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        ClientWorld world = minecraft.world;

        Vec3d pos = projectile.getPos();
        double x = pos.x;
        double y = pos.y;
        double z = pos.z;

        if (world != null && world.isClient()) {
            if(Objects.equals(MoreMagic.Magic_Name, "Stupefy")){
                // 昏昏倒地
                minecraft.execute(() -> {
                    Particle particle = minecraft.particleManager.addParticle(ParticleTypes.SOUL_FIRE_FLAME,
                            x , y, z,
                            0, 0, 0);
                    // 设置粒子的速度，使其跟随发射物移动
                    if (particle != null) {
                        particle.setVelocity(projectile.getVelocity().x, projectile.getVelocity().y, projectile.getVelocity().z);
                    }

                });
            } else if (Objects.equals(MoreMagic.Magic_Name, "Expelliarmus")) {
                // 除你武器
                minecraft.execute(() -> {
                    Particle particle = minecraft.particleManager.addParticle(ParticleTypes.FLAME,
                            x , y , z ,
                            0, 0, 0);
                    // 设置粒子的速度，使其跟随发射物移动
                    if (particle != null) {
                        particle.setVelocity(projectile.getVelocity().x, projectile.getVelocity().y, projectile.getVelocity().z);
                    }
                });
            } else if (Objects.equals(MoreMagic.Magic_Name, "Lumos")) {
                // 荧光闪烁
            }
        }
    }
}

