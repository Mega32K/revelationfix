package com.mega.revelationfix.common.item.other;

import com.mega.revelationfix.common.item.IJEIInvisibleRitualResult;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RandomDisplayItem extends Item implements IJEIInvisibleRitualResult {
    public RandomDisplayItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        p_41423_.add(Component.translatable("item.goety_revelation.random_display.desc").withStyle(ChatFormatting.GRAY));
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
    }

    @Override
    public boolean isInvisibleInJEI(ItemStack stack) {
        return true;
    }
}
