package com.mega.revelationfix.safe;

import com.Polarice3.Goety.common.crafting.RitualRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Map;
import java.util.Set;

public interface RitualRecipeInterface {
    static RitualRecipeInterface of(RitualRecipe recipe) {
        return (RitualRecipeInterface) recipe;
    }
    boolean revelationfix$isKeepingNbt();
    void revelationfix$setKeepingNBT(boolean keeping);
    Set<Integer> inputsToRemaining();
    Map<Integer, ItemStack> remaining();
}
