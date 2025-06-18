package com.mega.revelationfix.api.client;

import com.mega.revelationfix.client.task.ClientTaskManager;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;

public class LambdaClientTaskInstance extends ClientTaskInstance {
    private boolean removed;
    public int tickCount;
    private final E1 exe1;
    private final E2 exe2;
    public LambdaClientTaskInstance(E1 e1, E2 e2) {
        this.exe1 = e1;
        this.exe2 = e2;
    }
    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    public boolean isRemoved() {
        return removed;
    }
    @Override
    public void tick(Level level) {
        if (exe1 != null)
            exe1.tick(level);
    }

    @Override
    public void renderTick(TickEvent.RenderTickEvent event) {
        if (exe2 != null)
            exe2.renderTick(event);
    }
    public void onAddedToWorld() {
        this.setRemoved(false);
        ClientTaskManager.toAdd.add(this);
    }
    public interface E1 {
        void tick(Level level);
    }
    public interface E2 {
        void renderTick(TickEvent.RenderTickEvent event);
    }
}
