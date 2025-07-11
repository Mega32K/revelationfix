package com.mega.revelationfix.common.data.ingrident;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.crafting.IIngredientSerializer;

public class MysteryFragmentIngredientSerializer implements IIngredientSerializer<MysteryFragmentIngredient>
{
    public static final MysteryFragmentIngredientSerializer INSTANCE  = new MysteryFragmentIngredientSerializer();
    public static MysteryFragmentIngredient valueFromJson(JsonObject p_289797_) {
        if (p_289797_.get("fragment") instanceof JsonPrimitive jp) {
            return new MysteryFragmentIngredient(jp.getAsInt());
        } else {
            throw new JsonParseException("A puzzle ingredient entry needs puzzle index");
        }
    }

    public MysteryFragmentIngredient parse(FriendlyByteBuf buffer)
    {
        return new MysteryFragmentIngredient(buffer.readInt());
    }

    public MysteryFragmentIngredient parse(JsonObject json)
    {
        return valueFromJson(json);
    }

    public void write(FriendlyByteBuf buffer, MysteryFragmentIngredient ingredient)
    {
        buffer.writeInt(ingredient.getFragmentIndex());
    }
}
