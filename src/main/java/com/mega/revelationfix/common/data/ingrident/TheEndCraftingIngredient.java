package com.mega.revelationfix.common.data.ingrident;

import com.mega.revelationfix.common.init.GRItems;
import com.mega.revelationfix.common.item.other.RandomDisplayItem;
import com.mega.revelationfix.safe.TheEndRitualItemContext;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class TheEndCraftingIngredient extends Ingredient {
    public static TheEndCraftingIngredient INSTANCE = new TheEndCraftingIngredient();
    public TheEndCraftingIngredient() {
        super(Stream.of(new TagValue(GRItems.THE_END_CRAFTING)));
    }

    @Override
    public boolean test(@Nullable ItemStack itemStack) {
        return (itemStack.getItem() == TheEndRitualItemContext.THE_END_CRAFT && (itemStack.hasTag() && itemStack.getTag().getBoolean(GRItems.NBT_CRAFTING)) || itemStack.getItem() instanceof RandomDisplayItem);
    }

    @Override
    public IIngredientSerializer<? extends Ingredient> getSerializer() {
        return TheEndCraftingIngredientSerializer.INSTANCE;
    }
}
