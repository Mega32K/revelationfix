package com.mega.revelationfix.common.data.ingrident;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.crafting.IIngredientSerializer;

public class TheEndCraftingIngredientSerializer implements IIngredientSerializer<TheEndCraftingIngredient> {
    public static final TheEndCraftingIngredientSerializer INSTANCE = new TheEndCraftingIngredientSerializer();

    public TheEndCraftingIngredient parse(FriendlyByteBuf buffer) {
        return new TheEndCraftingIngredient();
    }

    public TheEndCraftingIngredient parse(JsonObject json) {
        return new TheEndCraftingIngredient();
    }

    public void write(FriendlyByteBuf buffer, TheEndCraftingIngredient ingredient) {
    }
}
