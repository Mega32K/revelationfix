package com.mega.revelationfix.common.apollyon.client;

import com.mega.revelationfix.api.entity.ITrailRendererEntity;
import com.mega.revelationfix.client.renderer.trail.TrailPoint;
import com.mega.revelationfix.common.network.PacketHandler;
import com.mega.revelationfix.common.network.s2c.EntityTagsSyncPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import z1gned.goetyrevelation.ModMain;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class WrappedTrailData {
    public final List<TrailPoint> trailPoints = new ArrayList<>();
    public Entity entity;
    @Nullable
    public ITrailRendererEntity trailRenderer;
    public WrappedTrailData(Entity entity) {
        this.entity = entity;
        if (entity instanceof ITrailRendererEntity i)
            this.trailRenderer = i;
    }

    public void join(int size) {
        synchronized (trailPoints) {
            for (int i = 0; i < size; i++) {
                trailPoints.add(new TrailPoint(entity.position(), 0));
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
        entity.addTag(ModMain.MODID + "_shouldRenderTrail");
        PacketHandler.sendToAll(new EntityTagsSyncPacket(this.entity.getId(), this.entity.getTags()));
    }

    public boolean shouldRenderTrail() {
        if (trailRenderer != null && !trailRenderer.shouldRenderWrappedTrail())
            return false;
        return entity.getTags().contains(ModMain.MODID + "_shouldRenderTrail");
    }
}
