package com.mega.revelationfix.client.renderer.entity;

import com.mega.revelationfix.Revelationfix;
import com.mega.revelationfix.client.renderer.MegaRenderType;
import com.mega.revelationfix.client.renderer.RendererUtils;
import com.mega.revelationfix.client.renderer.VFRBuilders;
import com.mega.revelationfix.client.renderer.trail.TrailPoint;
import com.mega.revelationfix.api.client.Easing;
import com.mega.revelationfix.client.TimeContext;
import com.mega.revelationfix.client.citadel.GRRenderTypes;
import com.mega.revelationfix.common.compat.SafeClass;
import com.mega.revelationfix.common.config.ClientConfig;
import com.mega.revelationfix.common.entity.projectile.StarArrow;
import com.mega.revelationfix.common.event.handler.ClientEventHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.awt.*;
import java.util.List;

public class StarArrowRenderer extends EntityRenderer<StarArrow> {
    private static final ResourceLocation STAR_TEXTURE = new ResourceLocation(Revelationfix.MODID, "textures/particle/star.png");
    private static final ResourceLocation WAVE_TEXTURE = new ResourceLocation(Revelationfix.MODID, "textures/particle/shockwave.png");
    public static RenderType PRT_STAR = MegaRenderType.particle(STAR_TEXTURE);
    public static RenderType PRT_WAVE = MegaRenderType.particle(WAVE_TEXTURE);
    static GameRenderer gameRenderer = Minecraft.getInstance().gameRenderer;
    public final float growingSizeBox = 0.1F;
    public final float dyingSizeBox = 0.95F;
    public float finalScaleWave = 1F;
    public float finalScaleBox = 1F;

    public StarArrowRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
    }

    @Override
    public void render(StarArrow starArrow, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource p_114489_, int light) {
        int trialLife = starArrow.getTrailLifeTime();
        MultiBufferSource.BufferSource source = Minecraft.getInstance().renderBuffers().bufferSource();
        p_114489_ = source;
        Camera camera = gameRenderer.getMainCamera();
        if (ClientConfig.enableTrailRenderer && trialLife > 35) {
            List<TrailPoint> trailPoints = starArrow.trailPoints;
            double x = Mth.lerp(partialTicks, starArrow.xOld, starArrow.getX());
            double y = Mth.lerp(partialTicks, starArrow.yOld, starArrow.getY());
            double z = Mth.lerp(partialTicks, starArrow.zOld, starArrow.getZ());
            if (trailPoints.size() > 0) {
                label0:
                {
                    if (Minecraft.getInstance().player != null && SafeClass.isClientTimeStop()) break label0;
                    synchronized (trailPoints) {
                        trailPoints.set(0, new TrailPoint(new Vec3(x, y, z), 0));
                        for (int i = trailPoints.size() - 1; i >= 1; i--) {
                            TrailPoint point = trailPoints.get(i);
                            if (point.getPosition().distanceToSqr(trailPoints.get(i - 1).getPosition()) < 4)
                                trailPoints.set(i, point.lerp(trailPoints.get(i - 1), partialTicks));
                            else trailPoints.set(i, new TrailPoint(trailPoints.get(i - 1).getPosition()));
                        }
                    }
                }
            } else {
                for (int i = 0; i < StarArrow.maxTrails; i++) {
                    trailPoints.add(new TrailPoint(starArrow.position, 0));
                }
            }
            VFRBuilders.WorldVFRTrailBuilder trailBuilder = ClientEventHandler.normalStarTrailsBuilder;
            if (trailBuilder != null)
                trailBuilder.addTrailListRenderTask(new StarTrailTask(starArrow));
        }
        if (starArrow.isInvisibleStar()) {
            int tickCount = StarArrow.maxTrialLife - trialLife;
            wave(tickCount, poseStack, light, partialTicks, source);
            return;
        }
        poseStack.pushPose();
        poseStack.scale(5F, 5F, 5F);
        poseStack.mulPose(camera.rotation());
        poseStack.mulPose(Axis.ZP.rotationDegrees(starArrow.initialZRot + (float) starArrow.rotationFactor * (starArrow.tickCount + partialTicks)));
        {
            RenderType renderType = SafeClass.usingShaderPack() ? GRRenderTypes.energySwirl(STAR_TEXTURE, 1f, 1f) : PRT_STAR;
            VertexConsumer consumer = p_114489_.getBuffer(renderType);
            Matrix4f m = poseStack.last().pose();
            Matrix3f matrix3f = poseStack.last().normal();
            Vector4f rgba = starArrow.getColor();
            if (rgba.w <= 0)
                rgba = new Vector4f(1f, 1f, 1f, 1f);
            RendererUtils.vertex(consumer, m, matrix3f, 0xF000F0, 0.0F, 0, 0F, 1F, rgba);
            RendererUtils.vertex(consumer, m, matrix3f, 0xF000F0, 1.0F, 0, 1F, 1F, rgba);
            RendererUtils.vertex(consumer, m, matrix3f, 0xF000F0, 1.0F, 1, 1F, 0F, rgba);
            RendererUtils.vertex(consumer, m, matrix3f, 0xF000F0, 0.0F, 1, 0F, 0F, rgba);
        }
        poseStack.popPose();
    }

    public void wave(int tickCount, PoseStack stack, int packedLight, float partialTicks, MultiBufferSource.BufferSource source) {
        float hTime = (tickCount + partialTicks) / 20.0F;
        if (hTime > 1.0F) return;
        {
            finalScaleWave = Easing.IN_OUT_CIRC.interpolate(hTime, 0F, 1);
            stack.pushPose();
            stack.translate(0, 0.6D, 0D);
            stack.scale(finalScaleWave, finalScaleWave, finalScaleWave);
            stack.mulPose(Axis.XP.rotationDegrees(90));
            Vector4f color = new Vector4f(1f, 1f, 1f, 1 - hTime);
            {
                stack.scale(16, 16, 16);
                Matrix4f m = stack.last().pose();
                Matrix3f matrix3f = stack.last().normal();
                VertexConsumer consumer = source.getBuffer(PRT_WAVE);
                RendererUtils.vertex(consumer, m, matrix3f, packedLight, 0.0F, 0, 0, 1, color);
                RendererUtils.vertex(consumer, m, matrix3f, packedLight, 1.0F, 0, 1, 1, color);
                RendererUtils.vertex(consumer, m, matrix3f, packedLight, 1.0F, 1, 1, 0, color);
                RendererUtils.vertex(consumer, m, matrix3f, packedLight, 0.0F, 1, 0, 0, color);
                source.endLastBatch();
            }
            stack.popPose();
        }
        {
            VertexConsumer consumer = source.getBuffer(RenderType.lines());
            float degrees = com.mega.revelationfix.util.time.TimeContext.Client.getCommonDegrees();
            float degrees1 = degrees / 1.3F;
            float degrees2 = degrees1 / 2.0F;
            finalScaleBox = hTime > dyingSizeBox ? 1F - (hTime - dyingSizeBox) / (1F - dyingSizeBox) : (hTime < growingSizeBox ? hTime / growingSizeBox : 1F);
            Color color = TimeContext.rainbow(1400.0F, 0.15F, 1.0F);
            stack.pushPose();
            stack.translate(0, 0.4D, 0D);
            stack.scale(finalScaleBox, finalScaleBox, finalScaleBox);
            stack.pushPose();
            stack.mulPose(Axis.XP.rotationDegrees(degrees));
            stack.mulPose(Axis.YP.rotationDegrees(degrees));
            stack.mulPose(Axis.ZP.rotationDegrees(degrees));
            RendererUtils.renderLineBox(stack, consumer, new AABB(-0.6F, -0.6F, -0.6F, 0.6F, 0.6F, 0.6F), 0.4f, 0.4f, 0.4f, .6f);
            stack.popPose();
            stack.pushPose();
            stack.mulPose(Axis.XP.rotationDegrees(degrees1));
            stack.mulPose(Axis.YP.rotationDegrees(degrees1));
            stack.mulPose(Axis.ZP.rotationDegrees(degrees1));
            RendererUtils.renderLineBox(stack, consumer, new AABB(-1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F), 0.4f, 0.4f, 0.4f, .7f);
            stack.popPose();
            stack.pushPose();
            stack.mulPose(Axis.XP.rotationDegrees(degrees2));
            stack.mulPose(Axis.YP.rotationDegrees(degrees2));
            stack.mulPose(Axis.ZP.rotationDegrees(degrees2));
            RendererUtils.renderLineBox(stack, consumer, new AABB(-1.24F, -1.24F, -1.24F, 1.24F, 1.24F, 1.24F), color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, .81f);
            stack.popPose();
            stack.popPose();
            source.endLastBatch();
        }
    }

    @Override
    public ResourceLocation getTextureLocation(StarArrow p_114482_) {
        return STAR_TEXTURE;
    }

    record StarTrailTask(StarArrow starArrow) implements VFRBuilders.WorldVFRTrailBuilder.TrailRenderTask {
        @Override
        public void task(PoseStack matrix, VFRBuilders.WorldVFRTrailBuilder vfrTrailBuilder) {
            if (SafeClass.usingShaderPack()) return;
            if (!starArrow.trailPoints.isEmpty()) {
                vfrTrailBuilder.r = starArrow.getRed() * 0.77F;
                vfrTrailBuilder.g = starArrow.getGreen() * 0.77F;
                vfrTrailBuilder.b = starArrow.getBlue() * 0.77F;
                vfrTrailBuilder.a = 0.44F;
                vfrTrailBuilder.renderTrail(matrix, starArrow.trailPoints, f -> (1.0F - f) * 0.7F);
            }
        }

        @Override
        public void tick() {
            if (Minecraft.getInstance().isPaused()) return;
            if (starArrow.getTrailLifeTime() <= 0) {
                synchronized (starArrow.trailPoints) {
                    starArrow.trailPoints.clear();
                }
            }
            if (starArrow.isInvisibleStar()) {
                starArrow.zOld = starArrow.getZ();
                starArrow.yOld = starArrow.getY();
                starArrow.xOld = starArrow.getX();
            }
        }
    }

}
