package com.mega.revelationfix.common.apollyon.common;

import com.mega.revelationfix.client.enums.ModChatFormatting;
import net.minecraft.world.item.Rarity;

public class RevelationRarity {
    public static Rarity REVELATION = Rarity.create("REVELATION", ModChatFormatting.APOLLYON);
    public static Rarity SPECTRE = Rarity.create("GR_SPECTRE", style -> style.withColor(0x8ec5fc));
    public static Rarity SPIDER = Rarity.create("GR_SPIDER", style -> style.withColor(0x6f4853));

    public static Rarity EDEN_NAME = Rarity.create("GR_EDEN_NAME", ModChatFormatting.EDEN);
}
