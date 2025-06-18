package com.mega.revelationfix.client.task;

import com.google.common.collect.EvictingQueue;
import com.google.common.collect.Queues;
import com.mega.revelationfix.api.client.ClientTaskInstance;
import com.mega.revelationfix.client.renderer.item.ItemRendererContext;
import com.mega.revelationfix.common.compat.Wrapped;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Iterator;
import java.util.Queue;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientTaskManager {
    public static final Queue<ClientTaskInstance> toAdd = Queues.newArrayDeque();
    public static final Queue<ClientTaskInstance> queue = EvictingQueue.create(512);
    static {
        ItemRendererContext.startTracker();
    }
    public static void addTask(ClientTaskInstance instance) {
        toAdd.add(instance);
    }
    @SubscribeEvent
    public static void renderTick(TickEvent.RenderTickEvent event) {
        for (ClientTaskInstance taskInstance : queue)
            taskInstance.renderTick(event);
    }
    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START && !Minecraft.getInstance().isPaused()) {
            Level level = Wrapped.clientLevel();
            if (!queue.isEmpty()) {
                Iterator<ClientTaskInstance> iterator = queue.iterator();
                while (iterator.hasNext()) {
                    ClientTaskInstance taskInstance = iterator.next();
                    taskInstance.tick(level);
                    taskInstance.tickCount++;
                    if (taskInstance.isRemoved()) {
                        iterator.remove();
                    }
                }
            }
            if (!toAdd.isEmpty()) {
                ClientTaskInstance clientTaskInstance;
                while((clientTaskInstance = toAdd.poll()) != null) {
                    queue.add(clientTaskInstance);
                }
            }
        }
    }
}
