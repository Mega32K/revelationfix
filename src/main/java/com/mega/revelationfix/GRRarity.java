package com.mega.revelationfix;

import net.minecraft.ChatFormatting;
import net.minecraft.world.item.Rarity;

public class GRRarity {
    private static Rarity REVELATION;

    public static Rarity REVELATION() {
        if (REVELATION == null) {
            REVELATION = Rarity.create("REVELATION", ChatFormatting.getByName("APOLLYON"));
        }
        return REVELATION;
    }
}
