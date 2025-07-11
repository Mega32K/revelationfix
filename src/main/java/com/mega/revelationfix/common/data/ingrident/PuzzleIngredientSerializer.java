package com.mega.revelationfix.common.data.ingrident;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.crafting.IIngredientSerializer;

public class PuzzleIngredientSerializer implements IIngredientSerializer<PuzzleIngredient>
{
    public static final PuzzleIngredientSerializer INSTANCE  = new PuzzleIngredientSerializer();
    public static PuzzleIngredient valueFromJson(JsonObject p_289797_) {
        if (p_289797_.get("puzzle") instanceof JsonPrimitive jp) {
            return new PuzzleIngredient(jp.getAsInt());
        } else {
            throw new JsonParseException("A puzzle ingredient entry needs puzzle index");
        }
    }

    public PuzzleIngredient parse(FriendlyByteBuf buffer)
    {
        return new PuzzleIngredient(buffer.readInt());
    }

    public PuzzleIngredient parse(JsonObject json)
    {
        return valueFromJson(json);
    }

    public void write(FriendlyByteBuf buffer, PuzzleIngredient ingredient)
    {
        buffer.writeInt(ingredient.getPuzzleIndex());
    }
}
