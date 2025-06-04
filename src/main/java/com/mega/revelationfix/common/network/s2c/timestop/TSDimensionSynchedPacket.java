package com.mega.revelationfix.common.network.s2c.timestop;

import com.mega.revelationfix.safe.level.ClientLevelExpandedContext;
import com.mega.revelationfix.safe.level.LevelEC;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class TSDimensionSynchedPacket {
    private final ResourceLocation toRemoveDimensionID;
    private final ResourceLocation toAddDimensionID;

    public TSDimensionSynchedPacket(ResourceLocation id, ResourceLocation id2) {
        this.toRemoveDimensionID = id;
        this.toAddDimensionID = id2;
    }

    public static TSDimensionSynchedPacket decode(FriendlyByteBuf friendlyByteBuf) {
        return new TSDimensionSynchedPacket(friendlyByteBuf.readResourceLocation(), friendlyByteBuf.readResourceLocation());
    }

    public static void encode(TSDimensionSynchedPacket packet, FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeResourceLocation(packet.toRemoveDimensionID);
        friendlyByteBuf.writeResourceLocation(packet.toAddDimensionID);
    }

    public static void handle(TSDimensionSynchedPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (packet != null)
                handle0(packet, context);
        });
        context.get().setPacketHandled(true);
    }

    static void handle0(TSDimensionSynchedPacket packet, Supplier<NetworkEvent.Context> context) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) System.exit(-1);
        ClientLevelExpandedContext levelExpandedContext = (ClientLevelExpandedContext) ((LevelEC) mc.level).uom$levelECData();
        if (!packet.toAddDimensionID.getPath().isEmpty()) {
            ResourceKey<Level> toAdd = ResourceKey.create(Registries.DIMENSION, packet.toAddDimensionID);
            if (mc.level.dimension() == toAdd) {
                levelExpandedContext.currentTimeStopDimension = toAdd;
            }
        }
        if (!packet.toRemoveDimensionID.getPath().isEmpty()) {
            ResourceKey<Level> toRemove = ResourceKey.create(Registries.DIMENSION, packet.toRemoveDimensionID);
            if (mc.level.dimension() == toRemove)
                levelExpandedContext.currentTimeStopDimension = null;
        }
    }
}
