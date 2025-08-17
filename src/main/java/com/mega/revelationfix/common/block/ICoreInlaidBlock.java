package com.mega.revelationfix.common.block;

import net.minecraft.world.item.Item;

public interface ICoreInlaidBlock {
    int getCore(Item item);

    Item getCore(int core);

    boolean isCore(Item item);
}
