package com.mega.revelationfix.common.data.ritual.requirement.entity;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class EntityOrRequirement extends EntityRequirement {
    private EntityRequirement[] or;
    @Override
    protected void compileSelfData(JsonObject jsonObject) {
        if (jsonObject.get("members") instanceof JsonArray jsonArray) {
            or = new EntityRequirement[jsonArray.size()];
            for (int i=0;i<jsonArray.size();i++) {
                or[i] = EntityRequirement.createFromJson(jsonArray.get(i));
            }
        }
    }

    @Override
    protected boolean canUse(Level level, Entity entity) {
        if (or != null && or.length > 0) {
            for (int i=0;i<or.length;i++)
                if (or[i].canUse(level, entity))
                    return true;
            return false;
        }
        return true;
    }

    @Override
    protected boolean canCompareNBT() {
        return false;
    }
}
