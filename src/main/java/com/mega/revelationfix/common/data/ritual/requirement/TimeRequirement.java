package com.mega.revelationfix.common.data.ritual.requirement;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mega.revelationfix.common.data.ritual.RitualData;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.Level;

public class TimeRequirement implements Requirement {
    private long[] timeRange = new long[2];

    public static TimeRequirement createFromJson(JsonElement element) {
        TimeRequirement requirement = null;
        if (element instanceof JsonObject jsonObject) {
            requirement = new TimeRequirement();
            requirement.compileData(jsonObject);
        }
        return requirement;
    }

    @Override
    public String getType() {
        return RitualData.TIME;
    }

    @Override
    public void compileData(JsonElement jsonElement) {
        timeRange = new long[]{Long.MIN_VALUE, Long.MAX_VALUE};
        if (jsonElement instanceof JsonObject jsonObject) {
            timeRange[0] = GsonHelper.getAsLong(jsonObject, "min", timeRange[0]);
            timeRange[1] = GsonHelper.getAsLong(jsonObject, "max", timeRange[1]);
        }
    }

    public boolean canUse(Level level) {
        long time = level.getDayTime();
        return time >= this.timeRange[0] && time <= this.timeRange[1];
    }
}
