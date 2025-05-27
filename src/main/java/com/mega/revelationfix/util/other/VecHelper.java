package com.mega.revelationfix.util.other;

import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;

public class VecHelper {

    public static final Vec3 CENTER_OF_ORIGIN = new Vec3(0.5, 0.5, 0.5);

    public VecHelper() {
    }

    public static Vec3 rotatingRadialOffset(Vec3 pos, float distanceX, float distanceZ, float current, float total, float gameTime, float time) {
        double angle = (double) (current / total) * 6.283185307179586;
        angle += (double) (gameTime % time / time) * 6.283185307179586;
        double dx2 = (double) distanceX * Math.cos(angle);
        double dz2 = (double) distanceZ * Math.sin(angle);
        Vec3 vector2f = new Vec3(dx2, 0.0, dz2);
        double x = vector2f.x * (double) distanceX;
        double z = vector2f.z * (double) distanceZ;
        return pos.add(x, 0.0, z);
    }

    public static Vec3 getCenterOf(Vec3i pos) {
        return pos.equals(Vec3i.ZERO) ? CENTER_OF_ORIGIN : Vec3.atLowerCornerOf(pos).add(0.5, 0.5, 0.5);
    }
}
