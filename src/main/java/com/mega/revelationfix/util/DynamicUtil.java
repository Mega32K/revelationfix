package com.mega.revelationfix.util;

import com.mega.revelationfix.common.init.GRItems;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class DynamicUtil {
    public static List<RegistryObject<Item>> getDynamicCreativeTabItems() {
        return GRItems.CREATIVE_TAB_ITEMS;
    }
}
