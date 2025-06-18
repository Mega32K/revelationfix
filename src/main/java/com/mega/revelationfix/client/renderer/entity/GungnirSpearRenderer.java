package com.mega.revelationfix.client.renderer.entity;

import com.mega.revelationfix.common.apollyon.client.render.VFRBuilders;
import com.mega.revelationfix.common.apollyon.client.render.trail.TrailPoint;
import com.mega.revelationfix.common.compat.SafeClass;
import com.mega.revelationfix.common.config.ClientConfig;
import com.mega.revelationfix.common.entity.projectile.GungnirSpearEntity;
import com.mega.revelationfix.common.entity.projectile.StarArrow;
import com.mega.revelationfix.common.event.handler.ClientEventHandler;
import com.mega.revelationfix.common.init.GRItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class GungnirSpearRenderer extends EntityRenderer<GungnirSpearEntity> {
    static GameRenderer gameRenderer = Minecraft.getInstance().gameRenderer;
    private final ItemRenderer itemRenderer;
    private ItemStack itemStack;

    public GungnirSpearRenderer(EntityRendererProvider.Context p_174420_) {
        super(p_174420_);
        this.itemRenderer = p_174420_.getItemRenderer();
    }

    public void render(@NotNull GungnirSpearEntity spearEntity, float nothing, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        try {
            if (spearEntity.getOwner() instanceof LivingEntity living) {
                if (itemStack == null)
                    itemStack = new ItemStack(GRItems.GUNGNIR.get());
                Minecraft mc = Minecraft.getInstance();
                int trialLife = spearEntity.getTrailLifeTime();
                MultiBufferSource.BufferSource source = Minecraft.getInstance().renderBuffers().bufferSource();
                Camera camera = gameRenderer.getMainCamera();
                if (ClientConfig.enableTrailRenderer && trialLife > 35) {
                    List<TrailPoint> trailPoints = spearEntity.trailPoints;
                    double x = Mth.lerp(partialTicks, spearEntity.xOld, spearEntity.getX());
                    double y = Mth.lerp(partialTicks, spearEntity.yOld, spearEntity.getY());
                    double z = Mth.lerp(partialTicks, spearEntity.zOld, spearEntity.getZ());
                    if (trailPoints.size() > 0) {
                        label0:
                        {
                            if (mc.player != null && SafeClass.isClientTimeStop() || mc.isPaused()) break label0;
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
                            trailPoints.add(new TrailPoint(spearEntity.position, 0));
                        }
                    }
                    VFRBuilders.WorldVFRTrailBuilder trailBuilder = ClientEventHandler.normalStarTrailsBuilder;
                    if (trailBuilder != null)
                        trailBuilder.addTrailListRenderTask(new GungnirSpearRenderer.SpearTrailTask(spearEntity));
                }
                poseStack.pushPose();
                packedLight = 0xFF00F0;
                poseStack.scale(2F, 2F, 2.3F);
                poseStack.translate(0F, 0F, -0.F);
                poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, spearEntity.yRotO, spearEntity.getYRot()) - 90.0F));
                poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, spearEntity.xRotO, spearEntity.getXRot()) + 90.0F));
                renderItem(spearEntity, itemStack, ItemDisplayContext.NONE, false, poseStack, bufferSource, packedLight);
                poseStack.popPose();
            }

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        super.render(spearEntity, nothing, partialTicks, poseStack, bufferSource, packedLight);
    }

    public void renderItem(Entity p_270072_, ItemStack p_270793_, ItemDisplayContext p_270837_, boolean p_270203_, PoseStack p_270974_, MultiBufferSource p_270686_, int p_270103_) {
        if (!p_270793_.isEmpty()) {
            itemRenderer.renderStatic(null, p_270793_, p_270837_, p_270203_, p_270974_, p_270686_, p_270072_.level(), p_270103_, OverlayTexture.NO_OVERLAY, p_270072_.getId() + p_270837_.ordinal());
        }
    }

    public @NotNull ResourceLocation getTextureLocation(@NotNull GungnirSpearEntity p_116109_) {
        return new ResourceLocation("");
    }

    record SpearTrailTask(GungnirSpearEntity gungnirSpear) implements VFRBuilders.WorldVFRTrailBuilder.TrailRenderTask {
        @Override
        public void task(PoseStack matrix, VFRBuilders.WorldVFRTrailBuilder vfrTrailBuilder) {
            Minecraft minecraft = Minecraft.getInstance();
            if (SafeClass.usingShaderPack()) return;
            if (!gungnirSpear.trailPoints.isEmpty()) {
                vfrTrailBuilder.r = gungnirSpear.color.x;
                vfrTrailBuilder.g = gungnirSpear.color.y;
                vfrTrailBuilder.b = gungnirSpear.color.z;
                vfrTrailBuilder.a = 0.44F;
                vfrTrailBuilder.renderTrail(matrix, gungnirSpear.trailPoints, f -> (1.0F - f) * 0.7F * (Math.abs(Mth.cos((minecraft.level.getTimeOfDay(minecraft.getPartialTick()) + f * 12F) * ((float) Math.PI * 2F))) / 3F + 0.5F));
            }
        }

        @Override
        public void tick() {
            if (Minecraft.getInstance().isPaused()) return;

            if (gungnirSpear.getTrailLifeTime() <= 0) {
                synchronized (gungnirSpear.trailPoints) {
                    gungnirSpear.trailPoints.clear();
                }
            }
            if (gungnirSpear.isInvisibleSpear()) {
                gungnirSpear.zOld = gungnirSpear.getZ();
                gungnirSpear.yOld = gungnirSpear.getY();
                gungnirSpear.xOld = gungnirSpear.getX();
            }
        }
    }
}