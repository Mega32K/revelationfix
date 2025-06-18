package com.mega.revelationfix.client.renderer.entity;

import com.mega.revelationfix.common.apollyon.client.render.VFRBuilders;
import com.mega.revelationfix.common.apollyon.client.render.trail.TrailPoint;
import com.mojang.blaze3d.vertex.PoseStack;

import java.util.List;

public record DeathArrowTrailTask(
        List<TrailPoint> trailPoints) implements VFRBuilders.WorldVFRTrailBuilder.TrailRenderTask {
    @Override
    public void task(PoseStack matrix, VFRBuilders.WorldVFRTrailBuilder vfrTrailBuilder) {
        if (!trailPoints.isEmpty()) {
            vfrTrailBuilder.r = 87F / 255F;
            vfrTrailBuilder.g = 34F / 255F;
            vfrTrailBuilder.b = 37F / 255F;
            vfrTrailBuilder.a = 0.74F;
            vfrTrailBuilder.renderTrail(matrix, trailPoints, f -> (f < 0.3F ? 1.0F : 1.3F - f) * 0.1F);
        }
    }
}
