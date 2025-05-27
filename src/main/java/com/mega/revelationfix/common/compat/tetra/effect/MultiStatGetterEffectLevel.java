package com.mega.revelationfix.common.compat.tetra.effect;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.gui.stats.getter.StatGetterEffectLevel;

public class MultiStatGetterEffectLevel extends StatGetterEffectLevel {
    private final StatGetterEffectLevel parent;
    private final double multiplier0;

    public MultiStatGetterEffectLevel(ItemEffect effect, double multiplier, double base, StatGetterEffectLevel parent) {
        super(effect, multiplier, base);
        this.parent = parent;
        this.multiplier0 = multiplier;
    }

    @Override
    public double getValue(Player player, ItemStack itemStack, String slot) {
        return parent.getValue(player, itemStack, slot) * multiplier0;
    }

    @Override
    public double getValue(Player player, ItemStack itemStack) {
        return parent.getValue(player, itemStack) * multiplier0;
    }

    @Override
    public double getValue(Player player, ItemStack itemStack, String slot, String improvement) {
        return parent.getValue(player, itemStack, slot, improvement) * multiplier0;
    }
}
