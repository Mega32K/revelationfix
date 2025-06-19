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

import java.util.Iterator;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModLootInject {
    private static final ObjectOpenHashSet<ResourceLocation> ENTITY_LOOTS = Util.make(() -> {ObjectOpenHashSet<ResourceLocation> set = new ObjectOpenHashSet<>();
        set.add(new ResourceLocation("goety", "brood_mother"));
        return set;
    });
    private static final ObjectOpenHashSet<ResourceLocation> CHEST_LOOTS = Util.make(() -> {ObjectOpenHashSet<ResourceLocation> set = new ObjectOpenHashSet<>();
        set.add(new ResourceLocation("minecraft", "abandoned_mineshaft"));
        return set;
    });
    public ModLootInject() {
    }

    @SubscribeEvent
    public static void InjectLootTables(LootTableLoadEvent evt) {
        String name = evt.getName().toString();
        if (name.contains("entities")) {
            synchronized (ENTITY_LOOTS) {
                Iterator<ResourceLocation> iterator;
                while ((iterator = ENTITY_LOOTS.iterator()).hasNext()) {
                    ResourceLocation rl = iterator.next();
                    String key = rl.getNamespace();
                    String value = rl.getPath();
                    if (name.startsWith(key) && name.endsWith(value)) {
                        evt.getTable().addPool(getInjectPool("entities/"+key, value));
                    }
                }
            }
        } else if (name.contains("chests")) {
            synchronized (CHEST_LOOTS) {
                Iterator<ResourceLocation> iterator;
                while ((iterator = CHEST_LOOTS.iterator()).hasNext()) {
                    ResourceLocation rl = iterator.next();
                    String key = rl.getNamespace();
                    String value = rl.getPath();
                    if (name.startsWith(key) && name.endsWith(value)) {
                        evt.getTable().addPool(getInjectPool("chests/"+key, value));
                    }
                }
            }
        }

    }

    private static LootPool getInjectPool(String type, String entryName) {
        return LootPool.lootPool().add(getInjectEntry(type, entryName)).name("gr_inject_pool").build();
    }

    private static LootPoolEntryContainer.Builder<?> getInjectEntry(String type, String name) {
        return LootTableReference.lootTableReference(new ResourceLocation(ModMain.MODID, "inject/" + type + "/" + name));
    }
}
