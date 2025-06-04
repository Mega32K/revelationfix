package com.mega.revelationfix.common.block;

import com.Polarice3.Goety.common.items.ModItems;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public interface ICoreInlaidBlock {
    int getCore(Item item);
    Item getCore(int core);
    boolean isCore(Item item);
}
