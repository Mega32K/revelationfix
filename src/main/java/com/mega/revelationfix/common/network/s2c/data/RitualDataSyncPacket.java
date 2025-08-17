package com.mega.revelationfix.common.network.s2c.data;

import com.google.common.collect.ImmutableMap;
import com.mega.revelationfix.common.data.ritual.RitualData;
import com.mega.revelationfix.common.data.ritual.RitualDataManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.Map;
import java.util.function.Supplier;

public class RitualDataSyncPacket {
    private final Map<String, RitualData> copiedData;

    public RitualDataSyncPacket(Map<String, RitualData> copiedData) {
        this.copiedData = ImmutableMap.copyOf(copiedData);
    }

    public static RitualDataSyncPacket decode(FriendlyByteBuf friendlyByteBuf) {
        return new RitualDataSyncPacket(friendlyByteBuf.readMap(RitualDataManager.PLUGIN_NAME_READER, RitualDataManager.SIMPLE_RITUAL_DATA_READER));
    }

    public static void encode(RitualDataSyncPacket packet, FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeMap(packet.copiedData, RitualDataManager.PLUGIN_NAME_WRITER, RitualDataManager.SIMPLE_RITUAL_DATA_WRITER);
    }

    public static void handle(RitualDataSyncPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (packet != null)
                handle0(packet, context);
        });
        context.get().setPacketHandled(true);
    }

    static void handle0(RitualDataSyncPacket packet, Supplier<NetworkEvent.Context> context) {
        RitualDataManager.LOCK.lock();
        try {
            synchronized (RitualDataManager.REGISTRIES) {
                Map<String, RitualData> data = packet.copiedData;
                for (var entry : data.entrySet()) {
                    RitualData packetRitualData = entry.getValue();
                    RitualData ritualData = RitualDataManager.getRitualByPlugin(entry.getKey());
                    if (ritualData == null)
                        ritualData = packetRitualData;
                    else {
                        ritualData.setIconItemKey(packetRitualData.getIconItemKey());
                        ritualData.setIconItem(packetRitualData.getIconItem());
                    }
                    RitualDataManager.REGISTRIES.put(entry.getKey(), ritualData);
                }
            }
        } finally {
            RitualDataManager.LOCK.unlock();
        }
    }
}
