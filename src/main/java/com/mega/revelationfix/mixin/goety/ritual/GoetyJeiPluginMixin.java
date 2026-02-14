package com.mega.revelationfix.mixin.goety.ritual;

import com.Polarice3.Goety.compat.jei.GoetyJeiPlugin;
import com.Polarice3.Goety.compat.jei.JeiRecipeTypes;
import com.Polarice3.Goety.compat.jei.ModRitualCategory;
import com.mega.revelationfix.common.compat.jei.GoetyJeiPlugin2;
import com.mega.revelationfix.common.data.ritual.RitualDataManager;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(value = GoetyJeiPlugin.class, remap = false)
public abstract class GoetyJeiPluginMixin {
    @Shadow
    public static IJeiHelpers jeiHelper;

    @Shadow
    public abstract void registerRitualType(IRecipeRegistration registration, RecipeManager recipeManager, String type);

    @Inject(method = "registerCategories", at = @At("HEAD"))
    private void registerCategories(IRecipeCategoryRegistration registration, CallbackInfo ci) {
        if (GoetyJeiPlugin2.goetyJeiHelper == null)
            GoetyJeiPlugin2.setGoetyJeiHelper(jeiHelper);
        //for (var entry : RitualDataManager.getRegistries().keySet()) {
        //    registration.addRecipeCategories(new ModRitualCategory(registration.getJeiHelpers().getGuiHelper(), entry));
        //}
    }

    @Inject(method = "registerRecipeCatalysts", at = @At("HEAD"))
    private void registerRecipeCatalysts(IRecipeCatalystRegistration registration, CallbackInfo ci) {
        if (GoetyJeiPlugin2.goetyJeiHelper == null)
            GoetyJeiPlugin2.setGoetyJeiHelper(jeiHelper);
        //for (var entry : RitualDataManager.getRegistries().entrySet()) {
        //    registration.addRecipeCatalyst(entry.getValue().getIconItem(), JeiRecipeTypes.getRitual(entry.getKey()));
        //}
    }

    @Inject(method = "registerRecipes",
            at = @At(value = "INVOKE", target = "Lcom/Polarice3/Goety/compat/jei/GoetyJeiPlugin;registerRitualType(Lmezz/jei/api/registration/IRecipeRegistration;Lnet/minecraft/world/item/crafting/RecipeManager;Ljava/lang/String;)V", ordinal = 0),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void registerRecipes(IRecipeRegistration registration, CallbackInfo ci, ClientLevel world, RecipeManager recipeManager, IIngredientManager ingredientManager, IVanillaRecipeFactory vanillaRecipeFactory, List cursedRecipes, List ritualRecipes) {
        if (GoetyJeiPlugin2.goetyJeiHelper == null)
            GoetyJeiPlugin2.setGoetyJeiHelper(jeiHelper);
        //Internal.getJeiRuntime().getRecipeManager().addRecipes();
        //for (var entry : RitualDataManager.getRegistries().keySet()) {
        //    this.registerRitualType(registration, recipeManager, entry);
        //}
    }
}
