package com.custom.magic.entity;

import com.custom.magic.entity.MagicVillager;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.VillagerEntityRenderer;
import net.minecraft.client.render.entity.feature.HeadFeatureRenderer;
import net.minecraft.client.render.entity.feature.VillagerClothingFeatureRenderer;
import net.minecraft.client.render.entity.feature.VillagerHeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

public class MagicVillagerRenderer extends MobEntityRenderer<MagicVillager, VillagerResemblingModel<MagicVillager>> {
    public static final Identifier TEXTURE = new Identifier("magic","textures/entity/magic_villager.png");
    public MagicVillagerRenderer(EntityRendererFactory.Context context) {
        super(context, new VillagerResemblingModel(context.getPart(EntityModelLayers.VILLAGER)), 0.5F);
    }


    @Override
    public Identifier getTexture(MagicVillager villagerEntity) {
        return TEXTURE;
    }

    protected void scale(VillagerEntity villagerEntity, MatrixStack matrixStack, float f) {
        float g = 0.9375F;
        if (villagerEntity.isBaby()) {
            g *= 0.5F;
            this.shadowRadius = 0.25F;
        } else {
            this.shadowRadius = 0.5F;
        }

        matrixStack.scale(g, g, g);
    }
}
