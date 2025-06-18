package com.mega.revelationfix.mixin.goety;

import com.Polarice3.Goety.client.render.DeathArrowRenderer;
import com.Polarice3.Goety.common.entities.projectiles.DeathArrow;
import com.mega.revelationfix.common.apollyon.client.WrappedTrailUpdate;
import com.mega.revelationfix.client.renderer.VFRBuilders;
import com.mega.revelationfix.client.renderer.trail.TrailPoint;
import com.mega.revelationfix.common.compat.SafeClass;
import com.mega.revelationfix.common.config.ClientConfig;
import com.mega.revelationfix.client.renderer.entity.DeathArrowTrailTask;
import com.mega.revelationfix.common.event.handler.ClientEventHandler;
import com.mega.revelationfix.safe.entity.DeathArrowEC;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(DeathArrowRenderer.class)
public abstract class DeathArrowRendererMixin extends ArrowRenderer<DeathArrow> {

    public DeathArrowRendererMixin(EntityRendererProvider.Context p_173917_) {
        super(p_173917_);
    }

    @Override
    public void render(@NotNull DeathArrow deathArrow, float yaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int packedLight) {

        if (ClientConfig.enableTrailRenderer) {
            DeathArrowEC ec = (DeathArrowEC) deathArrow;
            WrappedTrailUpdate wrappedTrailUpdate = ec.revelationfix$getTrailData();
            if (wrappedTrailUpdate.shouldRenderTrail()) {
                if (!deathArrow.onGround || ec.revelationfix$inGroundTime() < 20) {
                    double x = Mth.lerp(partialTicks, deathArrow.xOld, deathArrow.getX());
                    double y = Mth.lerp(partialTicks, deathArrow.yOld, deathArrow.getY());
                    double z = Mth.lerp(partialTicks, deathArrow.zOld, deathArrow.getZ());
                    final List<TrailPoint> trailPoints = wrappedTrailUpdate.trailPoints;
                    if (!trailPoints.isEmpty()) {
                        label0:
                        {
                            if (Minecraft.getInstance().player != null && SafeClass.isClientTimeStop()) break label0;
                            wrappedTrailUpdate.update(x, y, z, partialTicks);
                        }
                    }
                    VFRBuilders.WorldVFRTrailBuilder trailBuilder = ClientEventHandler.normalStarTrailsBuilder;
                    if (trailBuilder != null)
                        trailBuilder.addTrailListRenderTask(new DeathArrowTrailTask(trailPoints));


                } else wrappedTrailUpdate.remove();
            } else wrappedTrailUpdate.remove();
        }
        super.render(deathArrow, yaw, partialTicks, poseStack, multiBufferSource, packedLight);
    }
}
