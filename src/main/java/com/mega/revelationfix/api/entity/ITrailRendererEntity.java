package com.mega.revelationfix.api.entity;

import com.mega.revelationfix.client.renderer.trail.TrailRenderTask;
import com.mega.revelationfix.client.renderer.VFRBuilders;
import com.mega.revelationfix.client.renderer.trail.TrailPoint;
import com.mega.revelationfix.common.apollyon.client.WrappedTrailData;
import com.mega.revelationfix.common.compat.SafeClass;
import com.mega.revelationfix.common.entity.projectile.StarArrow;
import com.mega.revelationfix.common.event.handler.ClientEventHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ITrailRendererEntity {
    WrappedTrailData getWrappedTrailData();
    boolean shouldRenderWrappedTrail();
    default void updateWrappedTrail(Entity entity, float yaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int packedLight) {
        WrappedTrailData wrappedTrailData = this.getWrappedTrailData();
        if (wrappedTrailData.shouldRenderTrail()) {
            Vec3 position = entity.position();
            Vec3 delta = entity.position().subtract(entity.xOld, entity.yOld, entity.zOld);
            float deltaMultiplier = this.getTrailRenderPosDeltaMultiplier();
            double x = Mth.lerp(partialTicks, entity.xOld + delta.x * deltaMultiplier, position.x + delta.x * deltaMultiplier);
            double y = Mth.lerp(partialTicks, entity.yOld + delta.y * deltaMultiplier, position.y + delta.y * deltaMultiplier);
            double z = Mth.lerp(partialTicks, entity.zOld + delta.z * deltaMultiplier, position.z + delta.z * deltaMultiplier);
            final List<TrailPoint> trailPoints = wrappedTrailData.trailPoints;
            if (!trailPoints.isEmpty()) {
                label0:
                {
                    if (Minecraft.getInstance().player != null && SafeClass.isClientTimeStop()) break label0;
                    wrappedTrailData.update(x, y, z, partialTicks);
                }
            } else {
                wrappedTrailData.join(StarArrow.maxTrails);
            }
            VFRBuilders.WorldVFRTrailBuilder trailBuilder = ClientEventHandler.normalStarTrailsBuilder;
            if (trailBuilder != null)
                trailBuilder.addTrailListRenderTask(createTrailRenderTask(wrappedTrailData));

        } else wrappedTrailData.remove();
    }
    TrailRenderTask createTrailRenderTask(WrappedTrailData data);
    default float getTrailRenderPosDeltaMultiplier() {
        return 0F;
    }
}
