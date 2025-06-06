package com.mega.revelationfix.common.data.ritual.requirement.entity;

import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

public class NormalEntityRequirement extends EntityRequirement {
    private Pair<EntityType<?>, TagKey<EntityType<?>>> ingredient;
    @Override
    protected void compileSelfData(JsonObject jsonObject) {
        if (jsonObject.has("entity")) {
            String entity = jsonObject.get("entity").getAsString();
            if (entity.startsWith("#")) {
                ingredient = Pair.of(null, TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(entity.replace("#", ""))));
            } else ingredient = Pair.of(ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(entity)), null);
        }
    }

    @Override
    public boolean canUse(Level level, Entity entity) {
         if (ingredient != null) {
            if (ingredient.left() != null)
                return entity.getType().equals(ingredient.left());
            else if (ingredient.right() != null)
                return entity.getType().is(ingredient.right());
        }
        return false;
    }
}
