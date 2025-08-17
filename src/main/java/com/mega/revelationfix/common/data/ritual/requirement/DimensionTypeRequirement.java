package com.mega.revelationfix.common.data.ritual.requirement;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mega.revelationfix.common.data.ritual.RitualData;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.Set;

public class DimensionTypeRequirement implements Requirement {
    private final Set<ResourceLocation> dimensionIngredient = new ObjectArraySet<>();

    public static DimensionTypeRequirement createFromJson(JsonElement element) {
        DimensionTypeRequirement requirement = null;
        if (element instanceof JsonObject jsonObject) {
            requirement = new DimensionTypeRequirement();
            requirement.compileData(jsonObject);
        }
        return requirement;
    }

    @Override
    public String getType() {
        return RitualData.DIMENSION;
    }

    @Override
    public void compileData(JsonElement jsonElement) {
        if (jsonElement instanceof JsonPrimitive jp) {
            String dimension = jp.getAsString();
            dimensionIngredient.add(new ResourceLocation(dimension));
        } else if (jsonElement instanceof JsonArray jsonArray) {
            for (JsonElement je : jsonArray) {
                if (je instanceof JsonPrimitive primitive) {
                    dimensionIngredient.add(new ResourceLocation(primitive.getAsString()));
                }
            }
        }
    }

    public boolean canUse(Level level) {
        return dimensionIngredient.contains(new ResourceLocation(level.dimension().location().toString()));
    }
}
