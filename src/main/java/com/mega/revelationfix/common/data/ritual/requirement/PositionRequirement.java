package com.mega.revelationfix.common.data.ritual.requirement;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mega.revelationfix.common.data.ritual.RitualData;
import net.minecraft.core.BlockPos;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class PositionRequirement implements Requirement {
    public static final double width = 1000000000D;
    private AABB range;
    @Override
    public String getType() {
        return RitualData.POSITION;
    }

    @Override
    public void compileData(JsonElement jsonElement) {
        range = new AABB(-width, -width, -width, width, width, width);
        if (jsonElement instanceof JsonObject jsonObject) {
            double[] newRange = new double[] {-width, -width, -width, width, width, width};
            if (jsonObject.get("posX") instanceof JsonObject part) {
                newRange[0] = GsonHelper.getAsDouble(part, "min", newRange[0]);
                newRange[3] = GsonHelper.getAsDouble(part, "max", newRange[3]);
            }
            if (jsonObject.get("posY") instanceof JsonObject part) {
                newRange[1] = GsonHelper.getAsDouble(part, "min", newRange[1]);
                newRange[4] = GsonHelper.getAsDouble(part, "max", newRange[4]);
            }
            if (jsonObject.get("posZ") instanceof JsonObject part) {
                newRange[2] = GsonHelper.getAsDouble(part, "min", newRange[2]);
                newRange[5] = GsonHelper.getAsDouble(part, "max", newRange[5]);
            }
            range = new AABB(newRange[0], newRange[1], newRange[2], newRange[3], newRange[4], newRange[5]);
        }
    }
    public boolean canUse(Level level, BlockPos actorPos) {
        if (this.range != null) {
            return range.contains(actorPos.getCenter());
        }
        return false;
    }
    public static PositionRequirement createFromJson(JsonElement element) {
        PositionRequirement requirement = null;
        if (element instanceof JsonObject jsonObject) {
            requirement = new PositionRequirement();
            requirement.compileData(jsonObject);
        }
        return requirement;
    }
}
