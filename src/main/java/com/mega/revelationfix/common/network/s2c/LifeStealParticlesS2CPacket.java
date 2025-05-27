package com.mega.revelationfix.common.network.s2c;

import com.mega.revelationfix.common.network.LifeStealPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class LifeStealParticlesS2CPacket {
    private final double xPos;
    private final double yPos;
    private final double zPos;

    public LifeStealParticlesS2CPacket(double xPos, double yPos, double zPos) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.zPos = zPos;
    }


    public static LifeStealParticlesS2CPacket decode(FriendlyByteBuf friendlyByteBuf) {
        return new LifeStealParticlesS2CPacket(friendlyByteBuf.readDouble(), friendlyByteBuf.readDouble(), friendlyByteBuf.readDouble());
    }

    public static void encode(LifeStealParticlesS2CPacket packet, FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeDouble(packet.xPos);
        friendlyByteBuf.writeDouble(packet.yPos);
        friendlyByteBuf.writeDouble(packet.zPos);
    }

    public static void handle(LifeStealParticlesS2CPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (packet != null)
                handle0(packet, context);
        });
        context.get().setPacketHandled(true);
    }

    static void handle0(LifeStealParticlesS2CPacket packet, Supplier<NetworkEvent.Context> context) {
        LifeStealPacketHandler.doLifestealParticles(packet.xPos, packet.yPos, packet.zPos);
    }
}
