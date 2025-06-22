package com.mega.revelationfix.safe.level;

import com.mega.revelationfix.common.data.TimeStopSavedData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ServerExpandedContext {
    public final MinecraftServer server;

    public ServerExpandedContext(MinecraftServer server) {
        this.server = server;
    }

    public void update() {
    }
}
