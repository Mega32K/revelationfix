package com.mega.revelationfix.mixin.goety;

import com.Polarice3.Goety.client.render.DeathArrowRenderer;
import com.Polarice3.Goety.common.entities.projectiles.DeathArrow;
import com.mega.revelationfix.api.entity.ITrailRendererEntity;
import com.mega.revelationfix.common.config.ClientConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DeathArrowRenderer.class)
public abstract class DeathArrowRendererMixin extends ArrowRenderer<DeathArrow> {

    public DeathArrowRendererMixin(EntityRendererProvider.Context p_173917_) {
        super(p_173917_);
    }

    @Override
    public void render(@NotNull DeathArrow deathArrow, float yaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int packedLight) {
        if (ClientConfig.enableTrailRenderer) {
            ((ITrailRendererEntity) deathArrow).updateWrappedTrail(deathArrow, yaw, partialTicks, poseStack, multiBufferSource, packedLight);
        }
        super.render(deathArrow, yaw, partialTicks, poseStack, multiBufferSource, packedLight);
    }
}
