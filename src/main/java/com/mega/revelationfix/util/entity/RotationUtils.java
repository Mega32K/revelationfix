package com.mega.revelationfix.util.entity;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class RotationUtils {
    public static float rotateTowards(float p_24957_, float p_24958_, float limit) {
        float f = Mth.degreesDifference(p_24957_, p_24958_);
        float f1 = Mth.clamp(f, -limit, limit);
        return p_24957_ + f1;
    }

    public static void rotationAtoB(Entity a, Vec3 targetPos) {
        a.setYRot(getYRotD(a, targetPos.x, targetPos.z));
        a.setYHeadRot(getYRotD(a, targetPos.x, targetPos.z));
        a.setXRot(getXRotD(a, targetPos.x, targetPos.y, targetPos.z));
        a.xRotO = a.getXRot();
        a.yRotO = a.getYRot();

    }

    public static float getXRotD(Entity e, double wantedX, double wantedY, double wantedZ) {
        double d0 = wantedX - e.getX();
        double d1 = wantedY - e.getEyeY();
        double d2 = wantedZ - e.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        return (float) (-(Mth.atan2(d1, d3) * (180F / Mth.PI)));
    }

    public static float getYRotD(Entity e, double wantedX, double wantedZ) {
        double d0 = wantedX - e.getX();
        double d1 = wantedZ - e.getZ();
        return (float) (Mth.atan2(d1, d0) * (180F / Mth.PI)) - 90.0F;
    }
}
