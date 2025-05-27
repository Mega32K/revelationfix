package com.mega.revelationfix.common.network.s2c.timestop;

import com.mega.revelationfix.util.time.TimeStopUtilsWrapped;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class TimeStopSkillPacket {
    private final boolean isTimeStop;
    private final UUID user;
    private final boolean onlyRemoveEntity;
    private final boolean safelyCanCancel;

    public TimeStopSkillPacket(boolean isTimeStop, UUID user, boolean onlyRemoveEntity, boolean safelyCanCancel) {
        this.isTimeStop = isTimeStop;
        this.user = user;
        this.onlyRemoveEntity = onlyRemoveEntity;
        this.safelyCanCancel = safelyCanCancel;
    }

    public TimeStopSkillPacket(boolean isTimeStop, UUID user) {
        this(isTimeStop, user, false, true);
    }

    public static TimeStopSkillPacket decode(FriendlyByteBuf friendlyByteBuf) {
        return new TimeStopSkillPacket(friendlyByteBuf.readBoolean(), friendlyByteBuf.readUUID(), friendlyByteBuf.readBoolean(), friendlyByteBuf.readBoolean());
    }

    public static void encode(TimeStopSkillPacket packet, FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeBoolean(packet.isTimeStop);
        friendlyByteBuf.writeUUID(packet.user);
        friendlyByteBuf.writeBoolean(packet.onlyRemoveEntity);
        friendlyByteBuf.writeBoolean(packet.safelyCanCancel);
    }

    public static void handle(TimeStopSkillPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (packet != null)
                handle0(packet, context);
        });
        context.get().setPacketHandled(true);
    }

    static void handle0(TimeStopSkillPacket packet, Supplier<NetworkEvent.Context> context) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) System.exit(-1);
        if (packet.isTimeStop) {
            TimeStopUtilsWrapped.enable();
        } else {
            if (!packet.onlyRemoveEntity || packet.safelyCanCancel) {
                TimeStopUtilsWrapped.disable();
            }
        }
    }
}
