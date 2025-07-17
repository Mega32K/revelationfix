package com.mega.revelationfix.common.network.s2c;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class TheEndStructureMessagePacket {

    private final int i;
    private final int max;
    private final BlockPos pos;

    public TheEndStructureMessagePacket(int i, int max, BlockPos pos) {
        this.i = i;
        this.max = max;
        this.pos = pos;
    }

    public static TheEndStructureMessagePacket decode(FriendlyByteBuf friendlyByteBuf) {
        return new TheEndStructureMessagePacket(friendlyByteBuf.readInt(), friendlyByteBuf.readInt(), friendlyByteBuf.readBlockPos());
    }

    public static void encode(TheEndStructureMessagePacket packet, FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeInt(packet.i);
        friendlyByteBuf.writeInt(packet.max);
        friendlyByteBuf.writeBlockPos(packet.pos);
    }

    public static void handle(TheEndStructureMessagePacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (packet != null)
                handle0(packet, context);
        });
        context.get().setPacketHandled(true);
    }

    static void handle0(TheEndStructureMessagePacket packet, Supplier<NetworkEvent.Context> context) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && mc.level != null && mc.level.dimension() == Level.END && mc.player.position().distanceTo(packet.pos.getCenter()) < 64) {
            mc.player.displayClientMessage(Component.translatable("info.revelationfix.ritual.structure.fail", packet.i, packet.max), false);

        }
    }
}
