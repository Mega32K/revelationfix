package com.mega.revelationfix.common.item.curios.el;

import com.mega.revelationfix.common.init.GRItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.registries.RegistryObject;
import z1gned.goetyrevelation.item.ModItems;

import java.util.List;

public class EnigmaticLegacyItemInit {
    @ObjectHolder(value = "goety_revelation:blessing_scroll", registryName = "item")
    public static final Item BLESSING_SCROLL_ITEM = null;
    public static RegistryObject<Item> BLESSING_SCROLL;

    public static void init() {
        DeferredRegister<Item> deferredRegister = ModItems.ITEMS;
        BLESSING_SCROLL = deferredRegister.register("blessing_scroll", BlessingScroll::new);

        GRItems.insertAfterTabMap.put(GRItems.ETERNAL_WATCH, () ->List.of(new ItemStack(BLESSING_SCROLL_ITEM)));
    }

    public static void tryAddToTab(CreativeModeTab.Output output) {
        output.accept(BLESSING_SCROLL_ITEM);
    }
}
