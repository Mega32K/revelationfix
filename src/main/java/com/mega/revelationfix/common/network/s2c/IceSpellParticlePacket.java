package com.mega.revelationfix.common.network.s2c;

import com.mega.revelationfix.common.init.ModParticleTypes;
import com.mega.revelationfix.common.network.PacketClientProxy;
import com.mega.revelationfix.common.spell.frost.IceSpell;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.network.NetworkEvent;
import org.joml.Vector3f;

import java.util.UUID;
import java.util.function.Supplier;

public class IceSpellParticlePacket {
    public static final byte CIRCLE_PARTICLES = 0;
    public static final byte TARGETS_PARTICLES = 1;
    public final UUID casterID;
    public final float radius;
    public final byte id;
    public IceSpellParticlePacket(UUID casterID, float radius, byte id) {
        this.casterID = casterID;
        this.radius = radius;
        this.id = id;
    }


    public static IceSpellParticlePacket decode(FriendlyByteBuf friendlyByteBuf) {
        return new IceSpellParticlePacket(friendlyByteBuf.readUUID(), friendlyByteBuf.readFloat(), friendlyByteBuf.readByte());
    }

    public static void encode(IceSpellParticlePacket packet, FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeUUID(packet.casterID);
        friendlyByteBuf.writeFloat(packet.radius);
        friendlyByteBuf.writeByte(packet.id);
    }

    public static void handle(IceSpellParticlePacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (packet != null)
                handle0(packet, context);
        });
        context.get().setPacketHandled(true);
    }

    static void handle0(IceSpellParticlePacket packet, Supplier<NetworkEvent.Context> context) {
        PacketClientProxy.iceSpellPacket_handle0(packet, context);
    }

}
