package com.mega.revelationfix.jei;

import com.mega.revelationfix.Revelationfix;
import com.mega.revelationfix.common.compat.SafeClass;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.common.Internal;
import mezz.jei.common.gui.elements.DrawableSprite;
import mezz.jei.common.gui.textures.Textures;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import z1gned.goetyrevelation.ModMain;
import z1gned.goetyrevelation.item.ModItems;

import java.util.List;

@mezz.jei.api.JeiPlugin
public class JeiPlugin implements IModPlugin {
    public static final RecipeType<TetraDarkIngotHammerInfoRecipe> INFORMATION_0 = RecipeType.create(ModMain.MODID, "dark_ingot_hammer", TetraDarkIngotHammerInfoRecipe.class);
    public static IJeiHelpers jeiHelper;
    public static IDrawableStatic HAMMER_ICON;

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        jeiHelper = registration.getJeiHelpers();
        Textures textures = Internal.getTextures();
        HAMMER_ICON = new DrawableSprite(textures.getSpriteUploader(), new ResourceLocation("jei", "icons/dark_ingot_hammer"), 16, 16);
        registration.addRecipeCategories(new TetraDarkIngotHammerRecipeCategory(jeiHelper.getGuiHelper()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        if (SafeClass.isTetraLoaded()) {
            registration.addRecipeCatalyst(ForgeRegistries.ITEMS.getValue(new ResourceLocation("tetra:hammer_base")).asItem(), INFORMATION_0);
        }
    }

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Revelationfix.MODID, "jei_plugin");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        ModItems.ITEMS.getEntries().stream().map(RegistryObject::get).forEach((item) -> {
            if (I18n.exists(String.format("%s.guide", item.getDescriptionId())))
                registration.addItemStackInfo(new ItemStack(item), Component.translatable(String.format("%s.guide", item.getDescriptionId())));
        });
        if (SafeClass.isTetraLoaded()) {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation("tetra:hammer_base"));
            registration.addRecipes(INFORMATION_0, List.of(new TetraDarkIngotHammerInfoRecipe()));
            if (item != null)
                registration.addItemStackInfo(new ItemStack(item), Component.translatable(String.format(item.getDescriptionId() + ".guide", item.getDescriptionId())));
        }
    }
}
