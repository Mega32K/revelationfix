package com.mega.revelationfix.common.apollyon.client.render.trail;

import net.minecraft.world.phys.Vec3;
import org.joml.Vector4f;

public class TrailPoint {

    private final Vec3 position;
    private int timeActive;

    public TrailPoint(Vec3 position, int timeActive) {
        this.position = position;
        this.timeActive = timeActive;
    }

    public TrailPoint(Vec3 position) {
        this(position, 0);
    }

    public Vector4f getMatrixPosition() {
        Vec3 position = this.getPosition();
        return new Vector4f((float) position.x, (float) position.y, (float) position.z, 1.0F);
    }

    public Vec3 getPosition() {
        return this.position;
    }

    public int getTimeActive() {
        return this.timeActive;
    }

    public TrailPoint lerp(TrailPoint trailPoint, float delta) {
        Vec3 position = this.getPosition();
        return new TrailPoint(position.lerp(trailPoint.position, delta), this.timeActive);
    }

    public TrailPoint lerp(Vec3 pos, float delta) {
        Vec3 position = this.getPosition();
        return new TrailPoint(position.lerp(pos, delta), this.timeActive);
    }

    public TrailPoint lerp(double x, double y, double z, float delta) {
        Vec3 position = this.getPosition();
        return new TrailPoint(position.lerp(new Vec3(x, y, z), delta), this.timeActive);
    }

    public void tick() {
        ++this.timeActive;
    }
}
