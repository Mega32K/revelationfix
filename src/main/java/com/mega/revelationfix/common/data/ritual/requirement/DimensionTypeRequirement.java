package com.mega.revelationfix.common.data.ritual.requirement;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mega.revelationfix.common.data.ritual.RitualData;
import com.mega.revelationfix.common.data.ritual.requirement.block.BlockRequirement;
import com.mega.revelationfix.util.ClassHandler;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

public class DimensionTypeRequirement implements Requirement {
    private ResourceLocation dimensionType;
    @Override
    public String getType() {
        return RitualData.DIMENSION;
    }

    public ResourceLocation getDimensionType() {
        return dimensionType;
    }

    @Override
    public void compileData(JsonElement jsonElement) {
        if (jsonElement instanceof JsonObject jsonObject) {
             String dimension = GsonHelper.getAsString(jsonObject, "dimension", "minecraft:overworld");
             this.dimensionType = new ResourceLocation(dimension);
        }
    }
    public boolean canUse(Level level) {
        if (dimensionType != null) {
            return level.dimension().location().equals(dimensionType);
        }
        return false;
    }
    public static DimensionTypeRequirement createFromJson(JsonElement element) {
        DimensionTypeRequirement requirement = null;
        if (element instanceof JsonObject jsonObject) {
            requirement = new DimensionTypeRequirement();
            requirement.compileData(jsonObject);
        }
        return requirement;
    }
}
