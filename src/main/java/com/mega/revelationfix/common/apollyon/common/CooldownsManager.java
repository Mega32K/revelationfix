package com.mega.revelationfix.common.apollyon.common;

import com.mega.revelationfix.common.config.ItemConfig;
import com.mega.revelationfix.common.init.GRItems;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;

public class CooldownsManager {
    public static void setItemCooldowns(Player player, Item item, int tickCount) {
        ItemCooldowns cooldowns = player.getCooldowns();
        cooldowns.cooldowns.put(item, new ItemCooldowns.CooldownInstance(cooldowns.tickCount, cooldowns.tickCount + tickCount));
        cooldowns.onCooldownStarted(item, tickCount);
    }

    //终末玩家所有冷却时间缩短到0.1s
    public static void odamaneDecreaseCooldowns(Player player, Item item) {
        if (item == GRItems.HALO_OF_THE_END) return;
        if (item == GRItems.ETERNAL_WATCH.get() && !ItemConfig.ewCooldownsCanBeReduced) return;
        ItemCooldowns cooldowns = player.getCooldowns();
        if (cooldowns.isOnCooldown(item)) {
            int maxCooldown = 2;
            cooldowns.cooldowns.put(item, new ItemCooldowns.CooldownInstance(maxCooldown, maxCooldown));
            cooldowns.onCooldownStarted(item, maxCooldown);
        }
    }
}
