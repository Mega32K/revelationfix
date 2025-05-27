package com.mega.revelationfix.common.network.s2c.timestop;

import com.mega.revelationfix.util.time.TimeStopUtilsWrapped;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class TimeStopClientEffectPacket {
    public TimeStopClientEffectPacket() {
    }

    public static TimeStopClientEffectPacket decode(FriendlyByteBuf friendlyByteBuf) {
        return new TimeStopClientEffectPacket();
    }

    public static void encode(TimeStopClientEffectPacket packet, FriendlyByteBuf friendlyByteBuf) {
    }

    public static void handle(TimeStopClientEffectPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (packet != null)
                handle0(packet, context);
        });
        context.get().setPacketHandled(true);
    }

    static void handle0(TimeStopClientEffectPacket packet, Supplier<NetworkEvent.Context> context) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) System.exit(-1);
        TimeStopUtilsWrapped.enable();
    }
}
