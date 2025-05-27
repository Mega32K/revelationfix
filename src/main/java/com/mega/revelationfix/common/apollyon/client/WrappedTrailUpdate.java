package com.mega.revelationfix.common.apollyon.client;

import com.mega.revelationfix.common.apollyon.client.render.trail.TrailPoint;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class WrappedTrailUpdate {
    public static EntityDataAccessor<Boolean> SHOULD_RENDER_TRAIL;
    public final List<TrailPoint> trailPoints = new ArrayList<>();
    public Entity entity;

    public WrappedTrailUpdate(Entity entity) {
        this.entity = entity;
    }

    public void join(int size) {
        synchronized (trailPoints) {
            for (int i = 0; i < size; i++) {
                trailPoints.add(new TrailPoint(entity.position, 0));
            }
        }
    }

    public void remove() {
        if (!trailPoints.isEmpty())
            synchronized (trailPoints) {
                if (!trailPoints.isEmpty())
                    trailPoints.clear();
            }
    }

    public void update(double x, double y, double z, float partialTicks) {
        synchronized (trailPoints) {
            trailPoints.set(0, new TrailPoint(new Vec3(x, y, z), 0));
            for (int i = trailPoints.size() - 1; i >= 1; i--) {
                TrailPoint point = trailPoints.get(i);
                if (point.getPosition().distanceToSqr(trailPoints.get(i - 1).getPosition()) < 1)
                    trailPoints.set(i, point.lerp(trailPoints.get(i - 1), partialTicks));
                else trailPoints.set(i, new TrailPoint(trailPoints.get(i - 1).getPosition()));
            }
        }
    }

    public void setShouldRenderTrail(boolean z) {
        entity.entityData.set(SHOULD_RENDER_TRAIL, z);
    }

    public boolean shouldRenderTrail() {
        return entity.entityData.get(SHOULD_RENDER_TRAIL);
    }
}
