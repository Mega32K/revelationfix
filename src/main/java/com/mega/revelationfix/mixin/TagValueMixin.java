package com.mega.revelationfix.mixin;

import com.mega.revelationfix.common.init.GRItems;
import com.mega.revelationfix.safe.TheEndRitualItemContext;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(Ingredient.TagValue.class)
public class TagValueMixin {
    @Shadow
    @Final
    private TagKey<Item> tag;

    @ModifyVariable(method = "getItems", at = @At(value = "STORE", ordinal = 0))
    private List<ItemStack> getItems(List<ItemStack> list) {
        ItemStack stack = ItemStack.EMPTY;
        if (tag == GRItems.THE_END_CRAFTING) {
            list.add(TheEndRitualItemContext.THE_END_CRAFT.getDefaultInstance());
            list.add(GRItems.RANDOM_DISPLAY_ITEM.getDefaultInstance());
        }
        if (tag == GRItems.VANISHING_CB) {
            list.add(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(Enchantments.VANISHING_CURSE, Enchantments.VANISHING_CURSE.getMaxLevel())));
        }
        if (TheEndRitualItemContext.PUZZLE1 != null && (stack = TheEndRitualItemContext.PUZZLE1.getDefaultInstance()).is(tag)) {
            list.add(stack);
        }
        if (TheEndRitualItemContext.PUZZLE2 != null && (stack = TheEndRitualItemContext.PUZZLE2.getDefaultInstance()).is(tag))
            list.add(stack);
        if (TheEndRitualItemContext.PUZZLE3 != null && (stack = TheEndRitualItemContext.PUZZLE3.getDefaultInstance()).is(tag))
            list.add(stack);
        if (TheEndRitualItemContext.PUZZLE4 != null && (stack = TheEndRitualItemContext.PUZZLE4.getDefaultInstance()).is(tag))
            list.add(stack);
        if (tag == GRItems.MYSTERY_0) {
            list.add(GRItems.createFragment(0));
        } else if (tag == GRItems.MYSTERY_1) {
            list.add(GRItems.createFragment(1));
        } else if (tag == GRItems.MYSTERY_2) {
            list.add(GRItems.createFragment(2));
        } else if (tag == GRItems.MYSTERY_3) {
            list.add(GRItems.createFragment(3));
        }
        return list;
    }
}
