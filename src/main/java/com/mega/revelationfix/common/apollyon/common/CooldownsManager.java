package com.mega.revelationfix.common.apollyon.common;

import com.mega.endinglib.mixin.accessor.AccessorItemCooldowns;
import com.mega.revelationfix.common.config.ItemConfig;
import com.mega.revelationfix.common.init.GRItems;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;

public class CooldownsManager {
    public static void setItemCooldowns(Player player, Item item, int tickCount) {
        ItemCooldowns cooldowns = player.getCooldowns();
        AccessorItemCooldowns accessorItemCooldowns = (AccessorItemCooldowns) cooldowns;
        accessorItemCooldowns.getCooldowns().put(item, new ItemCooldowns.CooldownInstance(accessorItemCooldowns.getTickCount(), accessorItemCooldowns.getTickCount() + tickCount));
        accessorItemCooldowns.invokeOnCooldownStarted(item, tickCount);
    }

    //终末玩家所有冷却时间缩短到0.1s
    public static void odamaneDecreaseCooldowns(Player player, Item item) {
        if (item == GRItems.HALO_OF_THE_END) return;
        if (item == GRItems.ETERNAL_WATCH.get() && !ItemConfig.eternalWatchCooldownsCanBeReduced) return;
        ItemCooldowns cooldowns = player.getCooldowns();
        if (cooldowns.isOnCooldown(item)) {
            AccessorItemCooldowns accessorItemCooldowns = (AccessorItemCooldowns) cooldowns;
            int maxCooldown = 2;
            accessorItemCooldowns.getCooldowns().put(item, new ItemCooldowns.CooldownInstance(maxCooldown, maxCooldown));
            accessorItemCooldowns.invokeOnCooldownStarted(item, maxCooldown);
        }
    }
}
