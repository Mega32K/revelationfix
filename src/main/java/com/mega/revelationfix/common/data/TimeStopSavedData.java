package com.mega.revelationfix.common.data;

import com.mega.revelationfix.safe.PlayerInterface;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

import javax.annotation.Nullable;
import java.util.*;

public class TimeStopSavedData extends SavedData {
    public MinecraftServer server;
    public Map<UUID, SimplePlayerData> playerDataMap = new HashMap<>();
    public Set<ResourceLocation> dimensions = new HashSet<>();

    public static TimeStopSavedData create(CompoundTag tag) {
        TimeStopSavedData data = new TimeStopSavedData();
        {
            ListTag tags = tag.getList("Dimensions", 10);
            for (int i = 0; i < tags.size(); i++) {
                CompoundTag compoundTag = tags.getCompound(i);
                if (compoundTag.contains("id")) {
                    data.dimensions.add(new ResourceLocation(compoundTag.getString("id")));
                }
            }
        }
        {
            ListTag tags = tag.getList("PlayerDataMap", 10);
            for (int i = 0; i < tags.size(); i++) {
                CompoundTag compoundTag = tags.getCompound(i);
                if (compoundTag.hasUUID("PlayerID")) {
                    data.playerDataMap.put(compoundTag.getUUID("PlayerID"), data.readFromTag(compoundTag));
                }
            }
        }
        return data;
    }

    public static TimeStopSavedData readOrCreate(MinecraftServer server) {
        TimeStopSavedData data = server.overworld().getDataStorage().computeIfAbsent(TimeStopSavedData::create, TimeStopSavedData::new, "time_stop_saved_data");
        data.server = server;
        return data;
    }

    public static boolean isPlayerBlasphemous(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            try {
                return readOrCreate(serverPlayer.server).playerDataMap.getOrDefault(player.getUUID(), new SimplePlayerData(0)).getNeedleTimes() >= 14 || ((PlayerInterface) player).revelationfix$odamaneHaloExpandedContext().isBlasphemous();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                return false;
            }
        }
        return ((PlayerInterface) player).revelationfix$odamaneHaloExpandedContext().isBlasphemous();
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        {
            ListTag listTag = new ListTag();
            for (ResourceLocation resourceLocation : dimensions) {
                CompoundTag compoundTag = new CompoundTag();
                compoundTag.putString("id", resourceLocation.toString());
                listTag.add(compoundTag);
            }
            //tag.put("Dimensions", listTag);
        }
        {
            ListTag listTag = new ListTag();
            for (Map.Entry<UUID, SimplePlayerData> dataEntry : playerDataMap.entrySet()) {
                CompoundTag compoundTag = new CompoundTag();
                compoundTag.putUUID("PlayerID", dataEntry.getKey());
                compoundTag.putInt("NeedleTimes", dataEntry.getValue().getNeedleTimes());
                listTag.add(compoundTag);
            }
            tag.put("PlayerDataMap", listTag);
        }
        return tag;
    }

    private SimplePlayerData readFromTag(CompoundTag tag) {
        SimplePlayerData data = new SimplePlayerData(tag.getInt("NeedleTimes"));
        return data;
    }

    public void addTsDimension(ResourceKey<Level> dimension) {
        this.dimensions.add(dimension.location());
        setDirty();
    }

    public void removeTsDimension(ResourceKey<Level> dimension) {
        dimensions.remove(dimension.location());
        setDirty();
    }

    public @Nullable List<ResourceKey<Level>> asResourceKeys() {
        return dimensions.stream().map(r -> ResourceKey.create(Registries.DIMENSION, r)).toList();
    }

    public @Nullable List<ServerLevel> asServerLevel(MinecraftServer server) {
        return dimensions.stream().map(r -> server.getLevel(ResourceKey.create(Registries.DIMENSION, r))).toList();
    }

    public void setPlayerNeedleTimes(ServerPlayer player, int times) {
        SimplePlayerData playerData = playerDataMap.getOrDefault(player.getUUID(), new SimplePlayerData(0));
        playerData.setNeedleTimes(times);
        if (playerData.getNeedleTimes() >= 14)
            ((PlayerInterface) player).revelationfix$odamaneHaloExpandedContext().setBlasphemous(true);
        playerDataMap.put(player.getUUID(), playerData);
        setDirty();
    }

    public int getPlayerNeedleTimes(ServerPlayer player) {
        return playerDataMap.getOrDefault(player.getUUID(), new SimplePlayerData(0)).getNeedleTimes();
    }

    public static class SimplePlayerData {
        int needleTimes;

        public SimplePlayerData(int needleTimes) {
            this.needleTimes = needleTimes;
        }

        public int getNeedleTimes() {
            return needleTimes;
        }

        public void setNeedleTimes(int needleTimes) {
            this.needleTimes = needleTimes;
        }
    }
}
