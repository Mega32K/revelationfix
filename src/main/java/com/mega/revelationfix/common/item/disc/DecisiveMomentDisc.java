package com.mega.revelationfix.common.item.disc;


import com.mega.revelationfix.common.init.ModSounds;
import net.minecraft.world.item.Rarity;

public class DecisiveMomentDisc extends ModDiscItem {
    public DecisiveMomentDisc() {
        super(1, ModSounds.APOLLYON_NEW_NETHER_THEME, new Properties().fireResistant().stacksTo(1).rarity(Rarity.EPIC), 216 * 20 + 1);
    }
}
