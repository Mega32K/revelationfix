package com.mega.revelationfix.client.renderer.entity;

import com.mega.revelationfix.common.entity.binding.RevelationCageEntity;
import com.mojang.blaze3d.Blaze3D;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import z1gned.goetyrevelation.util.RendererUtils;

public class RevelationCageEntityRenderer extends EntityRenderer<RevelationCageEntity> {
    public RevelationCageEntityRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull RevelationCageEntity p_114482_) {
        return new ResourceLocation("");
    }

    @Override
    public void render(@NotNull RevelationCageEntity revelationCageEntity, float yaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int light) {
        float scale = 9F * 1.309017F * 0.5F;
        MultiBufferSource.BufferSource source = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(Minecraft.useShaderTransparency());
        poseStack.pushPose();
        float degrees = (float) Blaze3D.getTime() * 50.0F;
        poseStack.mulPose(Axis.XP.rotationDegrees(degrees));
        poseStack.mulPose(Axis.YP.rotationDegrees(degrees));
        poseStack.mulPose(Axis.ZP.rotationDegrees(degrees));
        RendererUtils.renderRegularIcosahedron(poseStack, source, scale, 15728880, 0.5411765F, 0.0F, 0.105882354F, revelationCageEntity.alpha(partialTicks), 1F);
        poseStack.popPose();
        RenderSystem.depthMask(true);
        RenderSystem.disableDepthTest();
    }
}
