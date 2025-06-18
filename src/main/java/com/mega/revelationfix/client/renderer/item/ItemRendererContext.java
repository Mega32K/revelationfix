package com.mega.revelationfix.client.renderer.item;

import com.mega.revelationfix.api.client.ClientTaskInstance;
import com.mega.revelationfix.api.client.LambdaClientTaskInstance;
import com.mega.revelationfix.client.task.ClientTaskManager;
import com.mega.revelationfix.common.network.PacketHandler;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;

import java.util.UUID;

public class ItemRendererContext {
    public static int TRACKED_COUNT = 0;
    private static final ClientTaskInstance ITEM_RENDERER_TRACKER =  new TrackerTaskInstance();
    static class TrackerTaskInstance extends ClientTaskInstance {
        public Item item;

        @Override
        public void tick(Level level) {

            if (TRACKED_COUNT == 0) {
                tickCount = 0;
            }
            TRACKED_COUNT = 0;
        }
        public void setItem(Item item) {
            if (this.item != item) {
                tickCount = 0;
                this.item = item;
            }
        }

        @Override
        public void renderTick(TickEvent.RenderTickEvent event) {
        }
    }
    public static float getTrackerTime(float partialTicks) {
        return ITEM_RENDERER_TRACKER.tickCount + partialTicks;
    }
    public static boolean isTrackerShutdown() {
        return ITEM_RENDERER_TRACKER.isRemoved();
    }
    public static void startTracker() {
        ITEM_RENDERER_TRACKER.setRemoved(false);
        ClientTaskManager.queue.add(ITEM_RENDERER_TRACKER);
    }
}
