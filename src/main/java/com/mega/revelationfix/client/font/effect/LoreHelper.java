package com.mega.revelationfix.client.font.effect;

import com.mega.revelationfix.safe.mixinpart.goety.ILevelWand;
import com.mojang.blaze3d.platform.InputConstants;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class LoreHelper {
    public static final Map<ChatFormatting, String> codeMap = new Object2ObjectOpenHashMap<>();
    public static String[] staffLevelNameID = new String[]{"dark_wand", "first", "second", "third"};
    public static ChatFormatting[] staffLevelColors = new ChatFormatting[]{ChatFormatting.GRAY, ChatFormatting.GRAY, ChatFormatting.AQUA, ChatFormatting.LIGHT_PURPLE};

    static {
        for (ChatFormatting cf : ChatFormatting.values()) {
            codeMap.put(cf, String.valueOf(ChatFormatting.PREFIX_CODE) + cf.getChar());
        }
    }

    public static String codeMode(ChatFormatting formatting) {
        return codeMap.getOrDefault(formatting, String.valueOf(ChatFormatting.PREFIX_CODE) + formatting.getChar());
    }
    public static Component TRUE() {
        return Component.translatable("message.goety_revelation.true");
    }
    public static Component FALSE() {
        return Component.translatable("message.goety_revelation.false");
    }
    public static Component getStaffLevelDesc(ILevelWand staff, ItemStack stack) {
        int index = Mth.clamp(staff.getStaffLevel(), 0, staffLevelNameID.length - 1);
        return Component.translatable("tooltip.goety_revelation.wand_level." + staffLevelNameID[index]).withStyle(staffLevelColors.length == staffLevelNameID.length ? staffLevelColors[index] : ChatFormatting.GRAY);
    }
    public static String getSoulCostText(int cost) {
        if (cost < 700) {
            return I18n.get("tooltip.goety_revelation.soul_cost", cost);
        }
        return I18n.get("tooltip.goety_revelation.soul_cost_kilo", cost * 0.001);
    }
    public static boolean hasControlDown() {
        if (Minecraft.ON_OSX) {
            return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 343) || InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 347);
        } else {
            return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 341) || InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 345);
        }
    }

    public static boolean hasShiftDown() {
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 340) || InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 344);
    }

    public static boolean hasAltDown() {
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 342) || InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 346);
    }

    public static boolean isCut(int p_96629_) {
        return p_96629_ == 88 && hasControlDown() && !hasShiftDown() && !hasAltDown();
    }

    public static boolean isPaste(int p_96631_) {
        return p_96631_ == 86 && hasControlDown() && !hasShiftDown() && !hasAltDown();
    }

    public static boolean isCopy(int p_96633_) {
        return p_96633_ == 67 && hasControlDown() && !hasShiftDown() && !hasAltDown();
    }

    public static boolean isSelectAll(int p_96635_) {
        return p_96635_ == 65 && hasControlDown() && !hasShiftDown() && !hasAltDown();
    }
}
