package com.mega.revelationfix.common.item.other;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class PuzzleItem extends Item {
    public PuzzleItem(Properties p_41383_) {
        super(p_41383_);
    }

    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack p_43105_) {
        return UseAnim.BOW;
    }

    public int getUseDuration(@NotNull ItemStack p_43107_) {
        return 72000;
    }

    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level p_43099_, Player p_43100_, @NotNull InteractionHand p_43101_) {
        ItemStack itemstack = p_43100_.getItemInHand(p_43101_);
        p_43100_.startUsingItem(p_43101_);
        return InteractionResultHolder.consume(itemstack);
    }
}
