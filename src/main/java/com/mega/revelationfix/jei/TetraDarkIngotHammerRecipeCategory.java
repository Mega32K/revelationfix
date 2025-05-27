package com.mega.revelationfix.jei;

import com.mega.revelationfix.common.init.GRItems;
import mezz.jei.api.gui.builder.IIngredientAcceptor;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import mezz.jei.common.util.StringUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import se.mickelus.tetra.items.modular.impl.ModularDoubleHeadedItem;

import java.util.ArrayList;
import java.util.List;

public class TetraDarkIngotHammerRecipeCategory extends AbstractRecipeCategory<TetraDarkIngotHammerInfoRecipe> {
    private static final int recipeWidth = 170;
    private static final int recipeHeight = 155;
    private final IGuiHelper guiHelper;

    public TetraDarkIngotHammerRecipeCategory(IGuiHelper guiHelper) {
        super(
                JeiPlugin.INFORMATION_0,
                Component.translatable("gui.jei.category.itemInformation").append(": ").append(Component.translatable("jei.goety_revelation.dark_ingot_hammer.title")),
                JeiPlugin.HAMMER_ICON,
                recipeWidth,
                recipeHeight
        );
        this.guiHelper = guiHelper;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, TetraDarkIngotHammerInfoRecipe recipe, IFocusGroup focuses) {
        int xPos = (recipeWidth - 16) / 2;
        Item item;

        {
            item = ForgeRegistries.ITEMS.getValue(new ResourceLocation("tetra:hammer_base"));
            IRecipeSlotBuilder inputSlotBuilder = builder.addInputSlot(xPos, 1)
                    .setStandardSlotBackground();
            IIngredientAcceptor<?> outputSlotBuilder = builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT);
            inputSlotBuilder.addItemLike(item);
            outputSlotBuilder.addItemLike(item);
        }
        xPos = recipeWidth / 3 * 2 + 16 * 2 + 2;
        int yPos = 22 + 2;
        {
            item = ForgeRegistries.ITEMS.getValue(new ResourceLocation("tetra:pristine_lapis"));
            addOutInSlot(builder, item, xPos, yPos);
        }
        yPos = yPos + (16 + 2);
        {
            item = ForgeRegistries.ITEMS.getValue(new ResourceLocation("tetra:pristine_emerald"));
            addOutInSlot(builder, item, xPos, yPos);
        }
        yPos = yPos + (16 + 2);
        {
            item = ForgeRegistries.ITEMS.getValue(new ResourceLocation("tetra:pristine_diamond"));
            addOutInSlot(builder, item, xPos, yPos);
        }
        yPos = yPos + (16 + 2);
        {
            item = ForgeRegistries.ITEMS.getValue(new ResourceLocation("tetra:pristine_amethyst"));
            addOutInSlot(builder, item, xPos, yPos);
            addOutInSlot(builder, new ArrayList<>(ModularDoubleHeadedItem.getCreativeTabItemStacks()), xPos - 16 - 2 - 8, yPos - 9);
        }
        yPos = yPos + (16 + 2);
        {
            item = Items.NETHER_STAR;
            addOutInSlot(builder, item, xPos, yPos);
        }
        yPos = yPos + (16 + 2);
        {
            item = GRItems.QUIETUS_STAR.get();
            addOutInSlot(builder, item, xPos, yPos);
        }
        yPos = yPos + (16 + 2);
        {
            item = ForgeRegistries.ITEMS.getValue(new ResourceLocation("goety:dark_metal_block"));
            addOutInSlot(builder, item, xPos, yPos);
        }
    }

    private void addOutInSlot(IRecipeLayoutBuilder builder, ItemLike item, int xPos, int yPos) {
        IRecipeSlotBuilder inputSlotBuilder = builder.addInputSlot(xPos, yPos)
                .setStandardSlotBackground();
        IIngredientAcceptor<?> outputSlotBuilder = builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT);
        inputSlotBuilder.addItemLike(item);
        outputSlotBuilder.addItemLike(item);
    }

    private void addOutInSlot(IRecipeLayoutBuilder builder, List<ItemStack> iS, int xPos, int yPos) {
        IRecipeSlotBuilder inputSlotBuilder = builder.addInputSlot(xPos, yPos)
                .setStandardSlotBackground();
        IIngredientAcceptor<?> outputSlotBuilder = builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT);
        inputSlotBuilder.addItemStacks(iS);
        outputSlotBuilder.addItemStacks(iS);
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, TetraDarkIngotHammerInfoRecipe recipe, IFocusGroup focuses) {
        int yPos = 22;
        int height = recipeHeight - yPos;
        builder.addScrollBoxWidget(
                        recipeWidth / 3 * 2,
                        height,
                        0,
                        yPos
                )
                .setContents(StringUtil.expandNewlines(Component.translatable("block.tetra.hammer_base.guide")));
        IDrawable arrow = guiHelper.createDrawable(new ResourceLocation("goety", "textures/gui/jei/down_arrow.png"), 0, 0, 13, 14);
        builder.addDrawable(arrow, recipeWidth / 3 * 2 + 8, (int) ((yPos + 2) * 2.5 + yPos + 8));
    }

    @Override
    public @Nullable ResourceLocation getRegistryName(TetraDarkIngotHammerInfoRecipe recipe) {
        return null;
    }
}
