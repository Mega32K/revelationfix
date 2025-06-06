package com.mega.revelationfix.common.compat.jei;

import com.Polarice3.Goety.common.crafting.ModRecipeSerializer;
import com.Polarice3.Goety.common.crafting.RitualRecipe;
import com.Polarice3.Goety.compat.jei.JeiRecipeTypes;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;

public class GoetyJeiPlugin2 {
    public static IJeiHelpers goetyJeiHelper;

    public static void setGoetyJeiHelper(IJeiHelpers goetyJeiHelper) {
        GoetyJeiPlugin2.goetyJeiHelper = goetyJeiHelper;
    }

    public static IJeiHelpers getJeiHelper() {
        return goetyJeiHelper;
    }
    public void registerRitualType(IRecipeRegistration registration, RecipeManager recipeManager, String type) {
        this.registerRitualType(registration, recipeManager, type, type);
    }

    public void registerRitualType(IRecipeRegistration registration, RecipeManager recipeManager, String type, String type2) {
        registration.addRecipes(JeiRecipeTypes.getRitual(type), this.ritualTypeRecipe(recipeManager, type2));
    }

    public List<RitualRecipe> ritualTypeRecipe(RecipeManager recipeManager, String type) {
        return recipeManager.getAllRecipesFor( ModRecipeSerializer.RITUAL_TYPE.get()).stream().filter((ritualRecipe) -> ritualRecipe.getCraftType().contains(type)).toList();
    }
}
