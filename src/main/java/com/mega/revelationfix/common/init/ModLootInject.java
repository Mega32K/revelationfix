package com.mega.revelationfix.common.init;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.compat.cataclysm.CataclysmLoaded;
import com.Polarice3.Goety.config.MainConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import z1gned.goetyrevelation.ModMain;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModLootInject {
    private static final List<String> CHEST_TABLES = List.of("abandoned_mineshaft");
    private static final List<String> ENTITY_TABLES = List.of("cave_spider");
    private static final List<String> GOETY_ENTITY_TABLES = List.of("brood_mother");
    public ModLootInject() {
    }

    @SubscribeEvent
    public static void InjectLootTables(LootTableLoadEvent evt) {

        String chestsPrefix = "minecraft:chests/";
        String entitiesPrefix = "minecraft:entities/";
        String entitiesPrefixGoety = "goety:entities/";
        String name = evt.getName().toString();
        if (name.startsWith(chestsPrefix) && CHEST_TABLES.contains(name.substring(chestsPrefix.length())) || name.startsWith(entitiesPrefix) && ENTITY_TABLES.contains(name.substring(entitiesPrefix.length()))) {
            String file = name.substring("minecraft:".length());
            evt.getTable().addPool(getInjectPool(file));
        } else
        if (name.startsWith(entitiesPrefixGoety) && GOETY_ENTITY_TABLES.contains(name.substring(entitiesPrefixGoety.length()))) {
            String file = name.substring("goety:".length()).replace("entities/", "goety_entities/");
            evt.getTable().addPool(getInjectPoolGoety(file));
        }

    }

    private static LootPool getInjectPool(String entryName) {
        return LootPool.lootPool().add(getInjectEntry(entryName)).name("gr_inject_pool").build();
    }

    private static LootPoolEntryContainer.Builder<?> getInjectEntry(String name) {
        /*
        if (CataclysmLoaded.CATACLYSM.isLoaded() && (Boolean)MainConfig.CataclysmLootCompat.get()) {
            if (Objects.equals(name, "chests/end_city_treasure")) {
                return LootTableReference.lootTableReference(Goety.location("inject/chests/end_city_treasure_cataclysm"));
            }


        }
         */
        return LootTableReference.lootTableReference(new ResourceLocation(ModMain.MODID, "inject/" + name));
    }
    private static LootPool getInjectPoolGoety(String entryName) {
        return LootPool.lootPool().add(getInjectEntryGoety(entryName)).name("goety_inject_pool").build();
    }

    private static LootPoolEntryContainer.Builder<?> getInjectEntryGoety(String name) {
        /*
        if (CataclysmLoaded.CATACLYSM.isLoaded() && (Boolean)MainConfig.CataclysmLootCompat.get()) {
            if (Objects.equals(name, "chests/end_city_treasure")) {
                return LootTableReference.lootTableReference(Goety.location("inject/chests/end_city_treasure_cataclysm"));
            }


        }
         */
        return LootTableReference.lootTableReference(new ResourceLocation(ModMain.MODID, "inject/" + name));
    }
}
