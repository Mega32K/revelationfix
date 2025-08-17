package com.mega.revelationfix.common.network.s2c;

import com.mega.revelationfix.common.network.PacketClientProxy;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

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
