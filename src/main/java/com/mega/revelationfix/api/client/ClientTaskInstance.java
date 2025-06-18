package com.mega.revelationfix.api.client;

import com.mega.revelationfix.client.task.ClientTaskManager;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;

import java.util.UUID;

public abstract class ClientTaskInstance {
    private volatile boolean removed;
    public int tickCount;
    public void setRemoved(boolean removed) {
        this.removed = removed;
    }
    public boolean isRemoved() {
        return removed;
    }

    public abstract void tick(Level level);

    public abstract void renderTick(TickEvent.RenderTickEvent event);
    public void onAddedToWorld() {
        ClientTaskManager.toAdd.add(this);
        this.setRemoved(false);
    }
    public interface E1 {
        void tick(Level level);
    }
    public interface E2 {
        void renderTick(TickEvent.RenderTickEvent event);
    }
}
