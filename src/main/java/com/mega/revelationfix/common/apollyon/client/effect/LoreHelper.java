package com.mega.revelationfix.common.apollyon.client.effect;

import com.mega.revelationfix.safe.mixinpart.goety.ILevelWand;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class LoreHelper {
    public static final Map<ChatFormatting, String> codeMap = new Object2ObjectOpenHashMap<>();
    public static String[] staffLevelNameID = new String[] {"dark_wand", "first", "second", "third"};
    public static ChatFormatting[] staffLevelColors = new ChatFormatting[] {ChatFormatting.GRAY, ChatFormatting.GRAY, ChatFormatting.AQUA, ChatFormatting.LIGHT_PURPLE};

    static {
        for (ChatFormatting cf : ChatFormatting.values()) {
            codeMap.put(cf, String.valueOf(ChatFormatting.PREFIX_CODE) + cf.getChar());
        }
    }

    public static String codeMode(ChatFormatting formatting) {
        return codeMap.getOrDefault(formatting, String.valueOf(ChatFormatting.PREFIX_CODE) + formatting.getChar());
    }
    public static Component getStaffLevelDesc(ILevelWand staff, ItemStack stack) {
        int index = Mth.clamp(staff.getStaffLevel(), 0, staffLevelNameID.length-1);
        return Component.translatable("tooltip.goety_revelation.wand_level." + staffLevelNameID[index]).withStyle(staffLevelColors.length == staffLevelNameID.length ? staffLevelColors[index] : ChatFormatting.GRAY);
    }
}
