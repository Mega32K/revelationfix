package com.mega.revelationfix.common.data.ritual.requirement.entity;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;

public class ItemFrameEntityRequirement extends EntityRequirement {
    private Ingredient ingredient;

    @Override
    protected void compileSelfData(JsonObject jsonObject) {
        if (jsonObject.has("display"))
            ingredient = CraftingHelper.getIngredient(jsonObject.get("display"), true);
    }

    @Override
    public boolean canUse(Level level, Entity entity) {
        return ingredient != null && entity instanceof ItemFrame frame && ingredient.test(frame.getItem());
    }
}
