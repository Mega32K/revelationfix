package com.mega.revelationfix.common.item.other;

import com.mega.revelationfix.api.item.IInvulnerableItem;
import com.mega.revelationfix.api.item.IJEIInvisibleRitualResult;
import com.mega.revelationfix.common.odamane.common.TheEndPuzzleItems;
import com.mega.revelationfix.safe.TheEndRitualItemContext;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MysteryFragment extends PuzzleItem implements IInvulnerableItem, IJEIInvisibleRitualResult {
    public static final String TYPE = "mysteryType";

    public MysteryFragment(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public boolean canBeHurtBy(DamageSource p_41387_) {
        return p_41387_.is(DamageTypeTags.BYPASSES_INVULNERABILITY);
    }

    @Override
    public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains("fragment", 99))
            tag.putInt("fragment", -1);
        super.onInventoryTick(stack, level, player, slotIndex, selectedIndex);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag tooltipFlag) {
        CompoundTag tag = stack.getOrCreateTag();
        if (tag.contains("fragment", 99)) {
            int id = tag.getInt("fragment");
            Item item;
            if (id == 3)
                item = TheEndRitualItemContext.PUZZLE4;
            else if (id == 2)
                item = TheEndRitualItemContext.PUZZLE3;
            else if (id == 1)
                item = TheEndRitualItemContext.PUZZLE2;
            else if (id == 0)
                item = TheEndRitualItemContext.PUZZLE1;
            else return;
            list.add(Component.translatable("item.goety_revelation.puzzle." + TheEndPuzzleItems.puzzleItems2.get(item)).withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
        }
        super.appendHoverText(stack, level, list, tooltipFlag);
    }

    @Override
    public boolean isInvisibleInJEI(ItemStack stack) {
        return true;
    }
}
