package com.mega.revelationfix.common.compat.jei;

import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public abstract class EmptyRecipeCategory implements IRecipeCategory<Object> {
    public final String titleS;
    private final IGuiHelper guiHelper;
    private final int iconWidth;

    public EmptyRecipeCategory(IGuiHelper guiHelper, String title) {
        this.iconWidth = 16;
        this.guiHelper = guiHelper;
        this.titleS = title;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("jei.goety_revelation.title." + titleS);
    }

    @Override
    public int getWidth() {
        return 176;
    }

    @Override
    public int getHeight() {
        return 140;
    }
}
