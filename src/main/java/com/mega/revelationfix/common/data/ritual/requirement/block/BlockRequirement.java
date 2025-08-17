package com.mega.revelationfix.common.data.ritual.requirement.block;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mega.revelationfix.common.data.ritual.RitualData;
import com.mega.revelationfix.common.data.ritual.requirement.Requirement;
import com.mega.revelationfix.util.ClassHandler;
import net.minecraft.CrashReport;
import net.minecraft.ReportedException;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public abstract class BlockRequirement implements Requirement {
    public static final String TYPE_NORMAL = "normal";
    public static final String TYPE_CHEST = "check_chest";
    public static final String TYPE_OR = "or";
    public static final String TYPE_AND = "and";
    public static Map<String, Class<? extends BlockRequirement>> BUILDERS = Util.make(() -> {
        Map<String, Class<? extends BlockRequirement>> m = new HashMap<>();
        m.put(TYPE_NORMAL, NormalBlockRequirement.class);
        m.put(TYPE_CHEST, CheckChestBlockRequirement.class);
        m.put(TYPE_OR, BlockOrRequirement.class);
        m.put(TYPE_AND, BlockAndRequirement.class);
        return m;
    });
    public String type = "normal";
    private int requiredCount;

    public static BlockRequirement createFromJson(JsonElement element) {

        BlockRequirement requirement = null;
        if (element instanceof JsonObject jsonObject) {
            String type = GsonHelper.getAsString(jsonObject, "type", TYPE_NORMAL);
            if (BUILDERS.containsKey(type)) {
                requirement = ClassHandler.newInstance(BUILDERS.get(type));
                if (requirement != null)
                    requirement.compileData(jsonObject);
            }
        }
        return requirement;
    }

    @Override
    public String getType() {
        return RitualData.BLOCKS;
    }

    public String getReadType() {
        return this.type;
    }

    public int getRequiredCount() {
        return requiredCount;
    }

    @Override
    public final void compileData(JsonElement jsonElement) {
        if (jsonElement instanceof JsonObject jsonObject) {
            this.requiredCount = GsonHelper.getAsInt(jsonObject, "count", 1);
            try {
                this.compileSelfData(jsonObject);
            } catch (Throwable throwable) {
                CrashReport report = CrashReport.forThrowable(throwable, "Reading Requirement: %s, jsonData : %s".formatted(this.getClass(), jsonElement));
                new ReportedException(report).printStackTrace();
                throwable.printStackTrace();
            }
        }
    }

    protected abstract void compileSelfData(JsonObject jsonObject);

    public abstract boolean canUse(Level level, BlockPos blockPos, BlockState state);
}
