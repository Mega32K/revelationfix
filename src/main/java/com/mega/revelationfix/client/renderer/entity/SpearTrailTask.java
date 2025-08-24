package com.mega.revelationfix.client.renderer.entity;

import com.mega.revelationfix.client.renderer.trail.TrailRenderTask;
import com.mega.revelationfix.client.renderer.VFRBuilders;
import com.mega.revelationfix.common.entity.projectile.GungnirSpearEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;

public record SpearTrailTask(GungnirSpearEntity gungnirSpear) implements TrailRenderTask {
    @Override
    public void task(PoseStack matrix, VFRBuilders.WorldVFRTrailBuilder vfrTrailBuilder) {
        Minecraft minecraft = Minecraft.getInstance();
        if (!gungnirSpear.getWrappedTrailData().trailPoints.isEmpty()) {
            vfrTrailBuilder.r = gungnirSpear.color.x;
            vfrTrailBuilder.g = gungnirSpear.color.y;
            vfrTrailBuilder.b = gungnirSpear.color.z;
            vfrTrailBuilder.a = 0.44F;
            vfrTrailBuilder.renderTrail(matrix, gungnirSpear.getWrappedTrailData().trailPoints, f -> (1.0F - f) * 0.7F * (Math.abs(Mth.cos((minecraft.level.getTimeOfDay(minecraft.getPartialTick()) + f * 12F) * ((float) Math.PI * 2F))) / 3F + 0.5F));
        }
    }

    @Override
    public void tick() {
        if (Minecraft.getInstance().isPaused()) return;

        if (gungnirSpear.getTrailLifeTime() <= 0) {
            gungnirSpear.getWrappedTrailData().remove();
        }
        if (gungnirSpear.isInvisibleSpear()) {
            gungnirSpear.zOld = gungnirSpear.getZ();
            gungnirSpear.yOld = gungnirSpear.getY();
            gungnirSpear.xOld = gungnirSpear.getX();
        }
    }
}
