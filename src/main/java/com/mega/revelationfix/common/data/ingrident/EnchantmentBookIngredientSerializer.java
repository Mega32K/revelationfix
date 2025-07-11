package com.mega.revelationfix.common.data.ingrident;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.crafting.IIngredientSerializer;

public class EnchantmentBookIngredientSerializer implements IIngredientSerializer<EnchantmentBookIngredient>
{
    public static final EnchantmentBookIngredientSerializer INSTANCE  = new EnchantmentBookIngredientSerializer();
    public static EnchantmentBookIngredient valueFromJson(JsonObject p_289797_) {
        if (p_289797_.get("enchantment") instanceof JsonPrimitive jp) {
            return new EnchantmentBookIngredient(jp.getAsString());
        } else {
            throw new JsonParseException("A enchantment ingredient entry needs enchantment id");
        }
    }

    public EnchantmentBookIngredient parse(FriendlyByteBuf buffer)
    {
        return new EnchantmentBookIngredient(buffer.readUtf());
    }

    public EnchantmentBookIngredient parse(JsonObject json)
    {
        return valueFromJson(json);
    }

    public void write(FriendlyByteBuf buffer, EnchantmentBookIngredient ingredient)
    {
        buffer.writeUtf(ingredient.getEnchantmentID().toString());
    }
}
