package com.mega.revelationfix.client.renderer.entity.mob;

import com.mega.revelationfix.client.model.entity.IceHermitModel;
import com.mega.revelationfix.common.entity.boss.IceHermitEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import z1gned.goetyrevelation.ModMain;

public class IceHermitEntityRenderer extends MobRenderer<IceHermitEntity, IceHermitModel> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(ModMain.MODID, "textures/entity/ice_hermit/ice_hermit.png");

    public IceHermitEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new IceHermitModel(context.bakeLayer(ModelLayers.PILLAGER)), 0.5F);
        this.addLayer(new CustomHeadLayer<>(this, context.getModelSet(), context.getItemInHandRenderer()));
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull IceHermitEntity p_114482_) {
        return TEXTURE;
    }
    @Override
    protected void scale(@NotNull IceHermitEntity p_114919_, PoseStack p_114920_, float p_114921_) {
        float f = 0.9375F;
        p_114920_.scale(f, f, f);
    }
}
