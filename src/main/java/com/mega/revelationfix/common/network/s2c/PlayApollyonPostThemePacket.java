package com.mega.revelationfix.common.network.s2c;

import com.Polarice3.Goety.common.entities.boss.Apostle;
import com.mega.revelationfix.common.compat.Wrapped;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class PlayApollyonPostThemePacket {
    private final UUID uuid;

    public PlayApollyonPostThemePacket(UUID uuid) {
        this.uuid = uuid;
    }

    public static PlayApollyonPostThemePacket decode(FriendlyByteBuf friendlyByteBuf) {
        return new PlayApollyonPostThemePacket(friendlyByteBuf.readUUID());
    }

    public static void encode(PlayApollyonPostThemePacket packet, FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeUUID(packet.uuid);
    }

    public static void handle(PlayApollyonPostThemePacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (packet != null)
                handle0(packet, context);
        });
        context.get().setPacketHandled(true);
    }

    static void handle0(PlayApollyonPostThemePacket packet, Supplier<NetworkEvent.Context> context) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null) {
            Entity entity = mc.level.entityStorage.getEntityGetter().get(packet.uuid);
            if (entity instanceof Apostle mob)
                Wrapped.play(mob);
        }
    }
}
