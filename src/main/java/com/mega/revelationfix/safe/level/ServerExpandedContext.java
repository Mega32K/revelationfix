package com.mega.revelationfix.safe.level;

import net.minecraft.server.MinecraftServer;

public class ServerExpandedContext {
    public final MinecraftServer server;

    public ServerExpandedContext(MinecraftServer server) {
        this.server = server;
    }

    public void update() {
    }
}
