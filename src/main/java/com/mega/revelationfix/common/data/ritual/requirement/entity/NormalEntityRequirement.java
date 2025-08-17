package com.mega.revelationfix.common.data.ritual.requirement.entity;

import com.google.gson.JsonObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

public class NormalEntityRequirement extends EntityRequirement {
    private EntityType<?> entityType;
    private TagKey<EntityType<?>> typeTag;

    @Override
    protected void compileSelfData(JsonObject jsonObject) {
        if (jsonObject.has("entity")) {
            String entity = jsonObject.get("entity").getAsString();
            if (entity.startsWith("#")) {
                typeTag = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(entity.replace("#", "")));
            } else entityType = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(entity));
        }
    }

    @Override
    public boolean canUse(Level level, Entity entity) {
        if (entityType != null)
            return entityType.equals(entity.getType());
        else if (typeTag != null)
            return entity.getType().is(typeTag);
        return false;
    }
}
