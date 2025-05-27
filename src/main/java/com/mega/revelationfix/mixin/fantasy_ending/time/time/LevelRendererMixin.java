package com.mega.revelationfix.mixin.fantasy_ending.time.time;

import com.mega.revelationfix.common.client.RendererUtils;
import com.mega.revelationfix.safe.NoModDependsMixin;
import com.mega.revelationfix.util.time.TimeContext;
import com.mega.revelationfix.util.time.TimeStopUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
@NoModDependsMixin("fantasy_ending")
public class LevelRendererMixin {
    @Shadow
    public EntityRenderDispatcher entityRenderDispatcher;

    @Inject(method = "renderEntity", at = @At(value = "HEAD"), cancellable = true)
    private void renderEntity(Entity p_109518_, double p_109519_, double p_109520_, double p_109521_, float p_109522_, PoseStack p_109523_, MultiBufferSource p_109524_, CallbackInfo ci) {
        if (TimeStopUtils.isTimeStop && TimeStopUtils.andSameDimension(p_109518_.level())) {
            if (TimeStopUtils.canMove(p_109518_)) {
                p_109522_ = TimeContext.Client.timer.partialTick;
                double d0 = Mth.lerp(p_109522_, p_109518_.xOld, p_109518_.getX());
                double d1 = Mth.lerp(p_109522_, p_109518_.yOld, p_109518_.getY());
                double d2 = Mth.lerp(p_109522_, p_109518_.zOld, p_109518_.getZ());
                float f = Mth.lerp(p_109522_, p_109518_.yRotO, p_109518_.getYRot());
                this.entityRenderDispatcher.render(p_109518_, d0 - p_109519_, d1 - p_109520_, d2 - p_109521_, f, p_109522_, p_109523_, p_109524_, this.entityRenderDispatcher.getPackedLightCoords(p_109518_, p_109522_));
                ci.cancel();
            }
        }
    }

    @Inject(method = "tickRain", at = @At("HEAD"), cancellable = true)
    private void tickRain(Camera p_109694_, CallbackInfo ci) {
        if (TimeStopUtils.isTimeStop && RendererUtils.isTimeStop_andSameDimension) ci.cancel();
    }
}
