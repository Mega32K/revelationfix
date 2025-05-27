package com.mega.revelationfix.safe;

import com.mega.revelationfix.common.data.TimeStopSavedData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ServerExpandedContext {
    public final MinecraftServer server;
    public final Set<ResourceKey<Level>> timeStopDimensions = new HashSet<>();

    public ServerExpandedContext(MinecraftServer server) {
        this.server = server;
    }

    public void update() {
        synchronized (timeStopDimensions) {
            timeStopDimensions.clear();
            List<ResourceKey<Level>> list = TimeStopSavedData.readOrCreate(server).asResourceKeys();
            if (list != null)
                timeStopDimensions.addAll(list);
        }
    }
}
