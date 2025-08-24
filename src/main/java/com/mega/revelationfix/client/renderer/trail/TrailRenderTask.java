package com.mega.revelationfix.client.renderer.trail;

import com.mega.revelationfix.client.renderer.VFRBuilders;
import com.mojang.blaze3d.vertex.PoseStack;

public interface TrailRenderTask {
    void task(PoseStack matrix, VFRBuilders.WorldVFRTrailBuilder vfrTrailBuilder);

    default void tick() {
    }
}
