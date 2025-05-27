package com.mega.revelationfix.common.item.disc;


import net.minecraft.world.item.Rarity;
import z1gned.goetyrevelation.ModMain;

public class ArchIllagerDisc extends ModDiscItem {
    public ArchIllagerDisc() {
        super(1, ModMain.APOLLYON_OVERWORLD_THEME, new Properties().fireResistant().stacksTo(1).rarity(Rarity.EPIC), 149 * 20 + 1);
    }
}
