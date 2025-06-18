package com.mega.revelationfix.api.item;

import net.minecraft.world.item.ItemStack;

public interface IDamageLimitItem {
    int getUseDamageLimit(ItemStack stack);
}
