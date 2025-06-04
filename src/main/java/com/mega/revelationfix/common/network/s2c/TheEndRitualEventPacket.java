package com.mega.revelationfix.common.network.s2c;

import com.mega.revelationfix.safe.level.ClientLevelExpandedContext;
import com.mega.revelationfix.safe.level.ClientLevelInterface;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class TheEndRitualEventPacket {
    private final BlockPos darkAltarPos;
    private final boolean isStart;


    public TheEndRitualEventPacket(BlockPos blockPos, boolean isStart) {
        this.darkAltarPos = blockPos;
        this.isStart = isStart;
    }

    public static TheEndRitualEventPacket decode(FriendlyByteBuf friendlyByteBuf) {
        return new TheEndRitualEventPacket(friendlyByteBuf.readBlockPos(), friendlyByteBuf.readBoolean());
    }

    public static void encode(TheEndRitualEventPacket packet, FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeBlockPos(packet.darkAltarPos);
        friendlyByteBuf.writeBoolean(packet.isStart);
    }

    public static void handle(TheEndRitualEventPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (packet != null)
                handle0(packet, context);
        });
        context.get().setPacketHandled(true);
    }

    static void handle0(TheEndRitualEventPacket packet, Supplier<NetworkEvent.Context> context) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null) {
            ClientLevelExpandedContext expandedContext = ((ClientLevelInterface) mc.level).revelationfix$ECData();
            expandedContext.teEndRitualBE = packet.darkAltarPos;
            expandedContext.teEndRitualRunning = packet.isStart;
        }
    }
}
