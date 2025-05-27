package com.mega.revelationfix.common.odamane.common;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

public class Puzzle {
    public String modid;
    public String registryName;
    public ResourceLocation rl;
    public Item item;
    /**
     * 语言文件用，唯一索引
     */
    public int index;
    /**
     * modid不存在(物品不存在)或者被其他puzzle禁用时为true
     */
    public boolean isDisabled;

    public Puzzle(String modid, String registryName, int index) {
        try {
            this.modid = (modid == null || modid.isEmpty()) ? "minecraft" : modid;
            this.registryName = registryName;
            this.rl = new ResourceLocation(modid, registryName);
            this.index = index;
            isDisabled = !ForgeRegistries.ITEMS.containsKey(rl);
            TheEndPuzzleItems.index2Puzzles.put(index, this);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            System.exit(-1);
        }
    }

    public Puzzle(ResourceLocation rl, int index) {
        try {
            this.modid = rl.getNamespace();
            this.registryName = rl.getPath();
            this.rl = rl;
            this.index = index;
            isDisabled = !ForgeRegistries.ITEMS.containsKey(rl);
            TheEndPuzzleItems.index2Puzzles.put(index, this);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            System.exit(-1);
        }
    }

    public Puzzle(String rl, int index) {
        this(new ResourceLocation(rl), index);
    }

    public boolean isDisabled() {
        return isDisabled;
    }

    public Puzzle setDisabled(boolean disabled) {
        isDisabled = disabled;
        return this;
    }

    public Item safeGetItem() {
        if (this.isDisabled) throw new AssertionError("disabled : " + this);
        if (item == null) {
            item = ForgeRegistries.ITEMS.getValue(rl);
        }
        return item;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[" + rl.toString() + ", isDisabled:" + isDisabled + "]";
    }
}
