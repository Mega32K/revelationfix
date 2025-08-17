package com.mega.revelationfix.util.client;

import com.Polarice3.Goety.client.particles.WindParticleOption;
import com.Polarice3.Goety.utils.ColorUtil;
import com.mega.revelationfix.util.RevelationFixMixinPlugin;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ClientParticleUtil {
    public static void windParticle(Level cleintLevel, ColorUtil color, float width, float height, int id, Vec3 vec3) {
        windParticle(cleintLevel, color, width, height, 0, id, vec3);
    }

    public static void windParticle(Level cleintLevel, ColorUtil color, float width, float height, int life, int id, Vec3 vec3) {
        cleintLevel.addParticle(new WindParticleOption(color, width, height, life, id), vec3.x(), vec3.y(), vec3.z(), 0, 0, 0);
    }

    public static <T extends ParticleOptions> void handleSendParticlesOnClient(Level level, T particle, double x, double y, double z, int count, double xDist, double yDist, double zDist, double maxSpeed) {
        boolean overrideLimiter = false;
        if (count == 0) {
            double d0 = maxSpeed * xDist;
            double d2 = maxSpeed * yDist;
            double d4 = maxSpeed * zDist;

            try {
                level.addParticle(particle, overrideLimiter, x, y, z, d0, d2, d4);
            } catch (Throwable throwable1) {
                RevelationFixMixinPlugin.LOGGER.warn("Could not spawn particle effect {}", particle);
            }
        } else {
            RandomSource random = level.random;
            for (int i = 0; i < count; ++i) {
                double d1 = random.nextGaussian() * xDist;
                double d3 = random.nextGaussian() * yDist;
                double d5 = random.nextGaussian() * zDist;
                double d6 = random.nextGaussian() * maxSpeed;
                double d7 = random.nextGaussian() * maxSpeed;
                double d8 = random.nextGaussian() * maxSpeed;

                try {
                    level.addParticle(particle, overrideLimiter, x + d1, y + d3, z + d5, d6, d7, d8);
                } catch (Throwable throwable) {
                    RevelationFixMixinPlugin.LOGGER.warn("Could not spawn particle effect {}", particle);
                    return;
                }
            }
        }
    }
}
