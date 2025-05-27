package com.mega.revelationfix.common.apollyon.client.effect;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.ChatFormatting;

import java.util.Map;

public class LoreHelper {
    public static final Map<ChatFormatting, String> codeMap = new Object2ObjectOpenHashMap<>();

    static {
        for (ChatFormatting cf : ChatFormatting.values()) {
            codeMap.put(cf, String.valueOf(ChatFormatting.PREFIX_CODE) + cf.getChar());
        }
    }

    public static String codeMode(ChatFormatting formatting) {
        return codeMap.getOrDefault(formatting, String.valueOf(ChatFormatting.PREFIX_CODE) + formatting.getChar());
    }
}
