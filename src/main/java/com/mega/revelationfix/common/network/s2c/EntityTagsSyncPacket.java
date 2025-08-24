package com.mega.revelationfix.common.network.s2c;

import com.mega.revelationfix.common.compat.Wrapped;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Supplier;

public class EntityTagsSyncPacket {
    private final int id;
    private final int size;
    private final String[] tags;
    public EntityTagsSyncPacket(int id, Set<String> tags) {
        this.id = id;
        this.size = tags.size();
        this.tags = tags.toArray(new String[0]);
    }
    public EntityTagsSyncPacket(int id, String... tags) {
        this.id = id;
        this.size = tags.length;
        this.tags = tags;
    }

    public static EntityTagsSyncPacket decode(FriendlyByteBuf friendlyByteBuf) {
        int id = friendlyByteBuf.readInt();
        int size = friendlyByteBuf.readInt();
        String[] tags = new String[size];
        for (int i=0;i<size;i++) {
            tags[i] = friendlyByteBuf.readUtf();
        }

        return new EntityTagsSyncPacket(id, tags);
    }

    public static void encode(EntityTagsSyncPacket packet, FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeInt(packet.id);
        friendlyByteBuf.writeInt(packet.size);
        if (packet.size > 0)
            for (String tag : packet.tags) {
                friendlyByteBuf.writeUtf(tag);
            }
    }

    public static void handle(EntityTagsSyncPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (packet != null)
                handle0(packet, context);
        });
        context.get().setPacketHandled(true);
    }

    @SuppressWarnings({"UseBulkOperation", "ManualArrayToCollectionCopy"})
    static void handle0(EntityTagsSyncPacket packet, Supplier<NetworkEvent.Context> context) {
        Level level = Wrapped.clientLevel();
        Entity entity = level.getEntity(packet.id);
        if (entity != null) {
            synchronized (entity.getTags()) {
                Set<String> tags = entity.getTags();
                tags.clear();
                for (String tag : packet.tags)
                    tags.add(tag);
            }
        }
    }
}
