package com.mega.revelationfix.common.network.c2s;

import com.mega.revelationfix.common.network.co.TheEndDeathPacketWrapped;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.joml.Vector3f;

import java.util.function.Supplier;

public class TheEndDeathPacket {
    protected final int user;
    protected final Vector3f respawn;

    public TheEndDeathPacket(int user, Vector3f respawn) {
        this.user = user;
        this.respawn = respawn;
    }

    public static TheEndDeathPacket decode(FriendlyByteBuf friendlyByteBuf) {
        return new TheEndDeathPacket(friendlyByteBuf.readInt(), friendlyByteBuf.readVector3f());
    }

    public static void encode(TheEndDeathPacket packet, FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeInt(packet.user);
        friendlyByteBuf.writeVector3f(packet.respawn);
    }

    public static void handle(TheEndDeathPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (packet != null)
                handle0(packet, context);
        });
        context.get().setPacketHandled(true);
    }

    static void handle0(TheEndDeathPacket packet, Supplier<NetworkEvent.Context> context) {
        TheEndDeathPacketWrapped.handle0(packet, context);
    }

    public int getUser() {
        return user;
    }

    public Vector3f getRespawn() {
        return respawn;
    }
}
