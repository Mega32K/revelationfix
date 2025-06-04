package com.mega.revelationfix;

import net.minecraft.ChatFormatting;
import net.minecraft.world.item.Rarity;

public class GRRarity {
    private static Rarity REVELATION;
    private static Rarity FROST;
    public static Rarity REVELATION() {

        if (REVELATION == null) {

            REVELATION = Rarity.create("REVELATION", ChatFormatting.getByName("APOLLYON"));
        }
        return REVELATION;
    }
    public static Rarity FROST() {

        if (FROST == null) {

            FROST = Rarity.create("GR_FROST", ChatFormatting.getByName("GR_FROST"));
        }
        return FROST;
    }
}
