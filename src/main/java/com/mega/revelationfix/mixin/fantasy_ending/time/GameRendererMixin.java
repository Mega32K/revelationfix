package com.mega.revelationfix.mixin.fantasy_ending.time;

import com.mega.revelationfix.client.RendererUtils;
import com.mega.revelationfix.safe.mixinpart.NoModDependsMixin;
import com.mega.revelationfix.util.time.TimeContext;
import com.mega.revelationfix.util.time.TimeStopRandom;
import com.mega.revelationfix.util.time.TimeStopUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.RandomSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(GameRenderer.class)
@NoModDependsMixin("fantasy_ending")
public abstract class GameRendererMixin {
    @Shadow
    @Final
    Minecraft minecraft;

    @Shadow
    @Nullable
    PostChain postEffect;

    @Shadow
    private boolean effectActive;

    @Shadow
    @Final
    private ResourceManager resourceManager;

    @Mutable
    @Shadow
    @Final
    private RandomSource random;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(Minecraft p_234219_, ItemInHandRenderer p_234220_, ResourceManager p_234221_, RenderBuffers p_234222_, CallbackInfo ci) {
        this.random = new TimeStopRandom(TimeContext.Client.generateUniqueSeed());
    }

    @ModifyVariable(method = "render", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private float render(float value) {
        if (TimeStopUtils.isTimeStop && RendererUtils.isTimeStop_andSameDimension)
            value = TimeContext.Client.timer.partialTick;
        return value;
    }

    @ModifyVariable(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V", ordinal = 0), ordinal = 0, argsOnly = true)
    private float render_beforeCamera(float value) {
        if (TimeStopUtils.isTimeStop && RendererUtils.isTimeStop_andSameDimension)
            value = TimeContext.Client.timer.partialTick;
        return value;
    }

    @ModifyVariable(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/client/ForgeHooksClient;onCameraSetup(Lnet/minecraft/client/renderer/GameRenderer;Lnet/minecraft/client/Camera;F)Lnet/minecraftforge/client/event/ViewportEvent$ComputeCameraAngles;"), remap = false, ordinal = 0, argsOnly = true)
    private float render_afterCamera(float value) {
        if (TimeStopUtils.isTimeStop && RendererUtils.isTimeStop_andSameDimension)
            value = Minecraft.getInstance().getFrameTime();
        return value;
    }

    @ModifyVariable(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;prepareCullFrustum(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/phys/Vec3;Lorg/joml/Matrix4f;)V"), ordinal = 0, argsOnly = true)
    private float render_beforeLevel(float value) {
        if (TimeStopUtils.isTimeStop && RendererUtils.isTimeStop_andSameDimension)
            value = Minecraft.getInstance().getFrameTime();
        return value;
    }

    @ModifyVariable(method = "bobView", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private float bobView(float value) {
        if (TimeStopUtils.isTimeStop && RendererUtils.isTimeStop_andSameDimension && Minecraft.getInstance().player != null) {
            value = TimeStopUtils.canMove(Minecraft.getInstance().player) ? TimeContext.Client.timer.partialTick : Minecraft.getInstance().getFrameTime();
        }
        return value;
    }

    @ModifyVariable(method = "bobHurt", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private float bobHurt(float value) {
        if (TimeStopUtils.isTimeStop && RendererUtils.isTimeStop_andSameDimension && Minecraft.getInstance().player != null) {
            value = TimeStopUtils.canMove(Minecraft.getInstance().player) ? TimeContext.Client.timer.partialTick : Minecraft.getInstance().getFrameTime();
        }
        return value;
    }
}
