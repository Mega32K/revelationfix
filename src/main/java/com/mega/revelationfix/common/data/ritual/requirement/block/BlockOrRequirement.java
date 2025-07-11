package com.mega.revelationfix.common.data.ritual.requirement.block;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mega.revelationfix.common.data.ritual.requirement.entity.EntityRequirement;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class BlockOrRequirement extends BlockRequirement {
    private BlockRequirement[] or;
    @Override
    protected void compileSelfData(JsonObject jsonObject) {
        if (jsonObject.get("members") instanceof JsonArray jsonArray) {
            or = new BlockRequirement[jsonArray.size()];
            for (int i=0;i<jsonArray.size();i++) {
                or[i] = BlockRequirement.createFromJson(jsonArray.get(i));
            }
        }
    }

    @Override
    public boolean canUse(Level level, BlockPos blockPos, BlockState state) {
        if (or != null && or.length > 0) {
            for (int i=0;i<or.length;i++)
                if (or[i].canUse(level, blockPos, state))
                    return true;
            return false;
        }
        return true;
    }
}
