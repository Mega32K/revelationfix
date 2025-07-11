package com.mega.revelationfix.common.data.ritual.requirement.block;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class BlockAndRequirement extends BlockRequirement {
    private BlockRequirement[] and;
    @Override
    protected void compileSelfData(JsonObject jsonObject) {
        if (jsonObject.get("members") instanceof JsonArray jsonArray) {
            and = new BlockRequirement[jsonArray.size()];
            for (int i=0;i<jsonArray.size();i++) {
                and[i] = BlockRequirement.createFromJson(jsonArray.get(i));
            }
        }
    }

    @Override
    public boolean canUse(Level level, BlockPos blockPos, BlockState state) {
        if (and != null && and.length > 0) {
            for (int i = 0; i< and.length; i++)
                if (!and[i].canUse(level, blockPos, state))
                    return false;
            return true;
        }
        return true;
    }
}
