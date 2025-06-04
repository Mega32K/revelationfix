package com.mega.revelationfix.common.network;

import com.mega.revelationfix.common.init.ModParticleTypes;
import com.mega.revelationfix.common.network.s2c.IceSpellParticlePacket;
import com.mega.revelationfix.util.entity.EntityFinder;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.network.NetworkEvent;
import org.joml.Vector3f;

import java.util.function.Supplier;

public class PacketClientProxy {
    public PacketClientProxy() {
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
    public static void doFrostParticles(double xPos, double yPos, double zPos) {
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
                if (Math.random() < 0.5F)
                    world.addParticle(new DustColorTransitionOptions(new Vector3f(0.69F, 0.87F, 0.90F), new Vector3f(0.0F, 0.0F, 0.0F), 1.0F), targetX, yPos, targetZ, offsetX * (double) speedFactor, 0.15, offsetZ * (double) speedFactor);
                else world.addParticle(new DustColorTransitionOptions(new Vector3f(0.43F, 0.51F, 0.95F), new Vector3f(0.0F, 0.0F, 0.0F), 1.0F), targetX, yPos, targetZ, offsetX * (double) speedFactor, 0.15, offsetZ * (double) speedFactor);
            }
        }

    }

    public static void iceSpellPacket_handle0(IceSpellParticlePacket packet, Supplier<NetworkEvent.Context> context) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null) {
            if (packet.id == IceSpellParticlePacket.CIRCLE_PARTICLES) {
                if (mc.level.getEntities().get(packet.casterID) instanceof LivingEntity living) {
                    RandomSource random = living.random;
                    for (int i = 0; i< Mth.TWO_PI * packet.radius; i++) {
                        for (int j=1;j<5;j++) {
                            double x = Mth.cos(i/(Mth.TWO_PI ))* packet.radius + living.getX();
                            double z = Mth.sin(i/(Mth.TWO_PI )) * packet.radius + living.getZ();
                            mc.level.addParticle(ModParticleTypes.FROST_FLOWER.get(), x, living.getY() + 0.3F, z, random.triangle(0F, 0.02F), random.triangle(1F, 0.04F), random.triangle(0F, 0.02F));
                            mc.level.addParticle(ModParticleTypes.FROST_FLOWER.get(), x + random.triangle(0F, 0.1F), living.getY() + 0.3F + random.triangle(0.3F, 0.1F), z, random.triangle(0F, 0.02F) + random.triangle(0F, 0.1F), random.triangle(.4F, 0.04F), random.triangle(0F, 0.02F));
                        }
                    }
                }
            } else if (packet.id == IceSpellParticlePacket.TARGETS_PARTICLES) {
                if (mc.level.getEntities().get(packet.casterID) instanceof LivingEntity living) {
                    RandomSource random = living.random;
                    for (Entity entity : mc.level.getEntities(living, new AABB(living.blockPosition).inflate(packet.radius * (Math.sqrt(2) / 2) + 4), (entity -> EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity) && !EntityFinder.isAlliedTo(living, entity)))) {
                        if (entity instanceof LivingEntity target) {
                            double posX = target.getX();
                            double posY = target.getY();
                            double posZ = target.getZ();
                            doFrostParticles(posX, posY, posZ);
                            for (int i=0;i<16;i++) {
                                mc.level.addParticle(com.Polarice3.Goety.client.particles.ModParticleTypes.FROST_NOVA.get(), posX, posY+i/2F+0.5F, posZ, 0F, 0F, 0F);
                            }
                        }
                    }
                }
            }
        }
    }

}
