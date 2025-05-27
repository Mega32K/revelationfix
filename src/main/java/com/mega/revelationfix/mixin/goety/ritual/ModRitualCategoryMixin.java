package com.mega.revelationfix.mixin.goety.ritual;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.crafting.RitualRecipe;
import com.Polarice3.Goety.compat.jei.ModRitualCategory;
import com.mega.revelationfix.common.compat.Wrapped;
import com.mega.revelationfix.common.init.GRItems;
import com.mega.revelationfix.common.item.IJEIInvisibleRitualResult;
import com.mega.revelationfix.util.ATAHelper2;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ModRitualCategory.class, remap = false)
public class ModRitualCategoryMixin {
    @Shadow
    @Final
    private Component localizedName;

    private static boolean isInvisible(ItemStack resultItem) {
        if (Wrapped.clientPlayer() != null && ATAHelper2.hasOdamane(Wrapped.clientPlayer()))
            return false;
        if (resultItem.getItem() instanceof IJEIInvisibleRitualResult itf && itf.isInvisibleInJEI(resultItem))
            return true;
        else return (resultItem.is(GRItems.THE_END_CRAFTING));
    }

    @Inject(method = "setRecipe(Lmezz/jei/api/gui/builder/IRecipeLayoutBuilder;Lcom/Polarice3/Goety/common/crafting/RitualRecipe;Lmezz/jei/api/recipe/IFocusGroup;)V", at = @At("HEAD"), cancellable = true)
    private void setRecipe(IRecipeLayoutBuilder recipeLayout, RitualRecipe recipe, IFocusGroup ingredients, CallbackInfo ci) {
        if (recipe.getRitualType().equals(Goety.location("craft"))) {
            ItemStack resultItem = recipe.getResultItem(null);
            if (resultItem != null && isInvisible(resultItem))
                ci.cancel();
        }
    }

    @Inject(method = "draw(Lcom/Polarice3/Goety/common/crafting/RitualRecipe;Lmezz/jei/api/gui/ingredient/IRecipeSlotsView;Lnet/minecraft/client/gui/GuiGraphics;DD)V", at = @At("HEAD"), cancellable = true)
    private void draw(RitualRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics stack, double mouseX, double mouseY, CallbackInfo ci) {
        if (recipe.getRitualType().equals(Goety.location("craft"))) {
            ItemStack resultItem = recipe.getResultItem(null);
            if (resultItem != null && isInvisible(resultItem))
                ci.cancel();
        }
    }
}
