package com.mega.revelationfix.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

public class LifeStealPacketHandler {
    public LifeStealPacketHandler() {
    }

    public static void doLifestealParticles(double xPos, double yPos, double zPos) {
        Level world = Minecraft.getInstance().level;
        if (world != null) {
            double radius = 0.7;
            double angleIncrement = 2.0 * Math.toRadians(0.5 / radius);
            int speedFactor = 10;

            for (double angle = 0.0; angle < 6.283185307179586; angle += angleIncrement) {
                double offsetX = radius * Math.sin(angle);
                double offsetZ = radius * Math.cos(angle);
                double targetX = xPos + offsetX;
                double targetZ = zPos + offsetZ;
                world.addParticle(new DustColorTransitionOptions(new Vector3f(0.78F, 0.18F, 0.18F), new Vector3f(0.0F, 0.0F, 0.0F), 1.0F), targetX, yPos, targetZ, offsetX * (double) speedFactor, 0.15, offsetZ * (double) speedFactor);
            }
        }

    }
}
