package com.mega.revelationfix.common.compat.tetra.hammer;

import com.mega.revelationfix.common.init.GRItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class DarkIngotHammerContext {

    public static List<Item> improveNeeds = new ArrayList<>();

    static {
        improveNeeds.add(0, ForgeRegistries.ITEMS.getValue(new ResourceLocation("tetra:pristine_lapis")));
        improveNeeds.add(1, ForgeRegistries.ITEMS.getValue(new ResourceLocation("tetra:pristine_emerald")));
        improveNeeds.add(2, ForgeRegistries.ITEMS.getValue(new ResourceLocation("tetra:pristine_diamond")));
        improveNeeds.add(3, ForgeRegistries.ITEMS.getValue(new ResourceLocation("tetra:pristine_amethyst")));
        improveNeeds.add(4, Items.NETHER_STAR);
        improveNeeds.add(5, GRItems.QUIETUS_STAR.get());
        improveNeeds.add(6, ForgeRegistries.ITEMS.getValue(new ResourceLocation("goety:dark_metal_block")));
    }
}
