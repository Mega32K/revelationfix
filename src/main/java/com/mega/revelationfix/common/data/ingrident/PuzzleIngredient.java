package com.mega.revelationfix.common.data.ingrident;

import com.mega.revelationfix.common.init.GRItems;
import com.mega.revelationfix.mixin.ItemStackMixin;
import com.mega.revelationfix.safe.TheEndRitualItemContext;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class PuzzleIngredient extends Ingredient {
    private final int puzzleIndex;
    public static TagKey<Item>[] TAGS = new TagKey[] {GRItems.THE_END_PUZZLES, GRItems.THE_END_PUZZLES2, GRItems.THE_END_PUZZLES3, GRItems.THE_END_PUZZLES4};
    public PuzzleIngredient(int puzzleIndex) {
        super(Stream.of(new TagValue(TAGS[puzzleIndex])));
        this.puzzleIndex = puzzleIndex;
    }

    @Override
    public boolean test(@Nullable ItemStack itemStack) {
        if (puzzleIndex == 0) {
            return ((itemStack.hasTag() && itemStack.getTag().getBoolean(GRItems.NBT_PUZZLES)) || itemStack.getItem() == TheEndRitualItemContext.PUZZLE1);
        } else if (puzzleIndex == 1) {
            return ((itemStack.hasTag() && itemStack.getTag().getBoolean(GRItems.NBT_PUZZLES2)) || itemStack.getItem() == TheEndRitualItemContext.PUZZLE2);
        } else if (puzzleIndex == 2) {
            return ((itemStack.hasTag() && itemStack.getTag().getBoolean(GRItems.NBT_PUZZLES3)) || itemStack.getItem() == TheEndRitualItemContext.PUZZLE3);
        } else if (puzzleIndex == 3) {
            return ((itemStack.hasTag() && itemStack.getTag().getBoolean(GRItems.NBT_PUZZLES4)) || itemStack.getItem() == TheEndRitualItemContext.PUZZLE4);
        } else return false;
    }
    public int getPuzzleIndex() {
        return puzzleIndex;
    }

    @Override
    public IIngredientSerializer<? extends Ingredient> getSerializer() {
        return PuzzleIngredientSerializer.INSTANCE;
    }
}
