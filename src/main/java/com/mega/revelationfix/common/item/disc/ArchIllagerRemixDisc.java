package com.mega.revelationfix.common.item.disc;


import net.minecraft.world.item.Rarity;
import z1gned.goetyrevelation.ModMain;

public class ArchIllagerRemixDisc extends ModDiscItem {
    public ArchIllagerRemixDisc() {
        super(1, ModMain.APOLLYON_NETHER_THEME, new Properties().fireResistant().stacksTo(1).rarity(Rarity.EPIC), 301 * 20 + 1);
    }
}
