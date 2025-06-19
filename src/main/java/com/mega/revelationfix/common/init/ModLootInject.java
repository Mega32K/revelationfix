package com.mega.revelationfix.common.init;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import z1gned.goetyrevelation.ModMain;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModLootInject {
    private static final Lock LOCK = new ReentrantLock();
    private static final Set<ResourceLocation> ENTITY_LOOTS = Util.make(() -> {ObjectOpenHashSet<ResourceLocation> set = new ObjectOpenHashSet<>();
        set.add(new ResourceLocation("goety", "brood_mother"));
        return set;
    });
    private static final Set<ResourceLocation> CHEST_LOOTS = Util.make(() -> {ObjectOpenHashSet<ResourceLocation> set = new ObjectOpenHashSet<>();
        set.add(new ResourceLocation("minecraft", "abandoned_mineshaft"));
        set.add(new ResourceLocation(ModMain.MODID, "church_food"));
        set.add(new ResourceLocation(ModMain.MODID, "church_food_2"));
        return set;
    });
    public ModLootInject() {
    }

    @SubscribeEvent
    public static void InjectLootTables(LootTableLoadEvent evt) {
        String name = evt.getName().toString();
        if (name.startsWith("goety:inject") || name.startsWith("goety_revelation:inject"))
            return;
        LOCK.lock();
            if (name.contains("entities")) {

                try {
                    for (ResourceLocation rl : ENTITY_LOOTS) {
                        String key = rl.getNamespace();
                        String value = rl.getPath();
                        if (name.startsWith(key) && name.endsWith(value)) {
                            evt.getTable().addPool(getInjectPool("entities/"+key, value));
                        }
                    }
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                } ;
            } else if (name.contains("chests")) {
                try {
                    for (ResourceLocation rl : CHEST_LOOTS) {
                        String key = rl.getNamespace();
                        String value = rl.getPath();
                        if (name.startsWith(key) && name.endsWith(value)) {
                            evt.getTable().addPool(getInjectPool("chests/"+key, value));
                        }
                    }
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }

            }
        LOCK.unlock();
    }

    private static LootPool getInjectPool(String type, String entryName) {
        return LootPool.lootPool().add(getInjectEntry(type, entryName)).name("gr_inject_pool").build();
    }

    private static LootPoolEntryContainer.Builder<?> getInjectEntry(String type, String name) {
        return LootTableReference.lootTableReference(new ResourceLocation(ModMain.MODID, "inject/" + type + "/" + name));
    }
}
