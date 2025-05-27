package com.mega.revelationfix.common.apollyon.client.render.trail;

import net.minecraft.world.phys.Vec3;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class TrailPointBuilder {

    public final Supplier<Integer> trailLength;
    private final List<TrailPoint> trailPoints = new ArrayList<>();

    public TrailPointBuilder(Supplier<Integer> trailLength) {
        this.trailLength = trailLength;
    }

    public static TrailPointBuilder create(int trailLength) {
        return create(() -> trailLength);
    }

    public static TrailPointBuilder create(Supplier<Integer> trailLength) {
        return new TrailPointBuilder(trailLength);
    }

    public List<TrailPoint> getTrailPoints() {
        return this.trailPoints;
    }

    public List<TrailPoint> getTrailPoints(float lerp) {
        List<TrailPoint> lerpedTrailPoints = new ArrayList<>();
        int size = this.trailPoints.size();
        if (size > 1) {
            for (int i = 0; i < size - 2; ++i) {
                lerpedTrailPoints.add(this.trailPoints.get(i).lerp(this.trailPoints.get(i + 1), lerp));
            }
        }

        return lerpedTrailPoints;
    }

    public TrailPointBuilder addTrailPoint(Vec3 point) {
        return this.addTrailPoint(new TrailPoint(point, 0));
    }

    public TrailPointBuilder addTrailPoint(TrailPoint point) {
        this.trailPoints.add(point);
        return this;
    }

    public void addTrailPoint0(TrailPoint point) {
        this.trailPoints.add(point);
    }

    public void addTrailPoint0(Vec3 point) {
        this.addTrailPoint0(new TrailPoint(point, 0));
    }

    public TrailPointBuilder tickTrailPoints() {
        int trailLength = this.trailLength.get();
        this.trailPoints.forEach(TrailPoint::tick);
        this.trailPoints.removeIf((p) -> p.getTimeActive() > trailLength);
        return this;
    }

    public List<Vector4f> build() {
        return this.trailPoints.stream().map(TrailPoint::getMatrixPosition).collect(Collectors.toList());
    }
}
