package com.mega.revelationfix.common.data.ingrident;

import com.mega.revelationfix.common.init.GRItems;
import com.mega.revelationfix.common.item.other.MysteryFragment;
import com.mega.revelationfix.safe.TheEndRitualItemContext;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class MysteryFragmentIngredient extends Ingredient {
    private final int fragmentIndex;
    public static TagKey<Item>[] TAGS = new TagKey[] {GRItems.MYSTERY_0, GRItems.MYSTERY_1, GRItems.MYSTERY_2, GRItems.MYSTERY_3};
    public MysteryFragmentIngredient(int fragmentIndex) {
        super(Stream.of(new TagValue(TAGS[fragmentIndex])));
        this.fragmentIndex = fragmentIndex;
    }

    @Override
    public boolean test(@Nullable ItemStack itemStack) {
        if (itemStack.getItem() instanceof MysteryFragment) {
            if (fragmentIndex == 0) {
                return (itemStack.hasTag() && itemStack.getTag().getInt("fragment") == 0);
            } else if (fragmentIndex == 1) {
                return (itemStack.hasTag() && itemStack.getTag().getInt("fragment") == 1);
            } else if (fragmentIndex == 2) {
                return (itemStack.hasTag() && itemStack.getTag().getInt("fragment") == 2);
            } else if (fragmentIndex == 3) {
                return (itemStack.hasTag() && itemStack.getTag().getInt("fragment") == 3);
            }
        } 
        return false;
    }

    public int getFragmentIndex() {
        return fragmentIndex;
    }

    @Override
    public IIngredientSerializer<? extends Ingredient> getSerializer() {
        return MysteryFragmentIngredientSerializer.INSTANCE;
    }

    public static MysteryFragmentIngredient puzzle(int index) {
        return new MysteryFragmentIngredient(index);
    }
}
