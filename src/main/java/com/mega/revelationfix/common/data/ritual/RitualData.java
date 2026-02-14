package com.mega.revelationfix.common.data.ritual;

import com.mega.revelationfix.common.data.ritual.requirement.Requirement;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class RitualData {
    public static final String BLOCKS = "blocks";
    public static final String ENTITIES = "entities";
    public static final String DIMENSION = "dimension_type";
    public static final String POSITION = "position";
    public static final String TIME = "time";
    private final String ritual;
    public Vec3i range = new Vec3i(8, 8, 8);
    public Map<String, Set<Requirement>> requirements = new Object2ObjectOpenHashMap<>();
    private String iconItemKey;
    private ItemStack iconItem;

    public RitualData(String ritual) {
        this.ritual = ritual;
        initRequirements();
    }

    public String getPluginName() {
        return ritual;
    }

    public String getIconItemKey() {
        return iconItemKey;
    }

    public void setIconItemKey(String iconItemKey) {
        this.iconItemKey = iconItemKey;
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(iconItemKey));
        if (item != null)
            this.iconItem = item.getDefaultInstance();
    }

    public ItemStack getIconItem() {
        if (iconItem == null) {
            setIconItemKey(iconItemKey);
            if (iconItem == null) {
                return Items.AIR.getDefaultInstance();
            }
        }
        return iconItem;
    }

    public void setIconItem(ItemStack iconItem) {
        this.iconItem = iconItem;
    }

    public Vec3i getRange() {
        return range;
    }

    public void setRange(int[] range) {
        this.range = new Vec3i(range[0], range[1], range[2]);
    }

    public void initRequirements() {
        this.requirements.put(BLOCKS, new ObjectOpenHashSet<>());
        this.requirements.put(ENTITIES, new ObjectOpenHashSet<>());
        this.requirements.put(DIMENSION, new ObjectOpenHashSet<>());
        this.requirements.put(POSITION, new ObjectOpenHashSet<>());
        this.requirements.put(TIME, new ObjectOpenHashSet<>());
    }

    public void setRequirements(String type, Collection<Requirement> collection) {
        Set<Requirement> set = this.requirements.get(type);
        if (set == null) return;
        set.addAll(collection);
    }

    public void setRequirements(String type, Requirement requirement) {
        Set<Requirement> set = this.requirements.get(type);
        if (set == null) return;
        set.add(requirement);
    }
}
