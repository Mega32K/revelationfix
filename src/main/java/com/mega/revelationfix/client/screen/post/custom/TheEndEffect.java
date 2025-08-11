package com.mega.revelationfix.client.screen.post.custom;

import com.mega.endinglib.api.client.Easing;
import com.mega.revelationfix.Revelationfix;
import com.mega.revelationfix.client.screen.CustomScreenEffect;
import com.mega.revelationfix.client.screen.post.PostEffectHandler;
import com.mega.revelationfix.safe.level.ClientLevelExpandedContext;
import com.mega.revelationfix.safe.level.ClientLevelInterface;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;
import org.joml.Vector4f;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class TheEndEffect implements CustomScreenEffect {
    public static Matrix4f MODEL_VIEW = new Matrix4f();
    public static Matrix4f PROJ_MAT = new Matrix4f();
    static Minecraft mc = Minecraft.getInstance();
    static int tickCount = 0;
    static int tickCountO = 0;
    static float rotationStar;
    static GuiGraphics guiGraphics;
    static TheEndEffect INSTANCE;
    float saturation = 1.0F;

    public TheEndEffect() {
        TheEndEffect.INSTANCE = this;
    }

    @SubscribeEvent
    public static void clientProgramTick(TickEvent.ClientTickEvent event) {
        LocalPlayer player = mc.player;
        if (player == null || event.phase == TickEvent.Phase.END)
            return;
        tickCountO = tickCount;
        if (AberrationDistortionPostEffect.INSTANCE.canUse()) {
            if (tickCount < 25)
                tickCount++;
        } else if (tickCount > 0) tickCount--;
    }

    @SubscribeEvent
    public static void modelViewGetter(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            MODEL_VIEW = new Matrix4f(event.getPoseStack().last().pose());
            PROJ_MAT = event.getProjectionMatrix();
        }
    }

    @Override
    public String getName() {
        return "the_end";
    }

    @Override
    public ResourceLocation getShaderLocation() {
        return new ResourceLocation(Revelationfix.MODID, "shaders/post/movie.json");
    }

    @Override
    public void onRenderTick(float partialTicks) {

        float percent = Mth.lerp(partialTicks, TheEndEffect.tickCountO, TheEndEffect.tickCount) / 25F;
        PostEffectHandler.updateUniform_post(this, "Percent", Easing.OUT_CUBIC.calculate(percent));
        /*
        float ticks = Mth.lerp(partialTicks, tickCountO, tickCount) - 1.0F;
        if (mc.player != null) {
            ClientLevelExpandedContext context = ((ClientLevelInterface) mc.level).revelationfix$ECData();
            BlockPos blockPos = context.teEndRitualBE;
            if (blockPos == null) return;
            float d0 = blockPos.getX();
            float d1 = blockPos.getY();
            float d2 = blockPos.getZ();
            Camera camera = mc.gameRenderer.getMainCamera();
            PoseStack posestack = RenderSystem.getModelViewStack();
            posestack.pushPose();
            posestack.mulPoseMatrix(MODEL_VIEW);
            RenderSystem.applyModelViewMatrix();
            Vector4f pos = new Vector4f((float) -camera.getPosition().x, (float) -camera.getPosition().y, (float) -camera.getPosition().z, 1.0F);

            //玩家坐标  model坐标
            //x -> y
            //y -> z
            //z -> x
            //pos model坐标
            float distance = (float) Math.sqrt(mc.player.distanceToSqr(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
            PostEffectHandler.updateUniform_post(this, "HaloPos", new float[]{pos.x, pos.y, pos.z, pos.w});
            PostEffectHandler.updateUniform_post(this, "Scale", 1F);
            posestack.popPose();
            RenderSystem.applyModelViewMatrix();
        }
        PostEffectHandler.updateUniform_post(this, "Saturation", Math.max(0.05F, saturation - ticks * 0.02F));
         */
    }

    @Override
    public boolean canUse() {
        return tickCount > 0 || tickCountO > 0;
    }
}
