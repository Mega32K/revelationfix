package com.mega.revelationfix.common.data.ritual.requirement.entity;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class EntityAndRequirement extends EntityRequirement {
    private EntityRequirement[] and;
    @Override
    protected void compileSelfData(JsonObject jsonObject) {
        if (jsonObject.get("members") instanceof JsonArray jsonArray) {
            and = new EntityRequirement[jsonArray.size()];
            for (int i=0;i<jsonArray.size();i++) {
                and[i] = EntityRequirement.createFromJson(jsonArray.get(i));
            }
        }
    }

    @Override
    protected boolean canUse(Level level, Entity entity) {
        if (and != null && and.length > 0) {
            for (int i = 0; i< and.length; i++)
                if (!and[i].canUse(level, entity))
                    return false;
            return true;
        }
        return true;
    }

    @Override
    protected boolean canCompareNBT() {
        return false;
    }
}
