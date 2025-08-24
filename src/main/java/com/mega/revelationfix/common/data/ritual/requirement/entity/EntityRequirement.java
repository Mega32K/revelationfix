package com.mega.revelationfix.common.data.ritual.requirement.entity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mega.revelationfix.common.data.ritual.RitualData;
import com.mega.revelationfix.common.data.ritual.requirement.Requirement;
import com.mega.revelationfix.safe.entity.EntityExpandedContext;
import com.mega.revelationfix.util.ClassHandler;
import com.mega.revelationfix.util.LivingEntityEC;
import net.minecraft.CrashReport;
import net.minecraft.ReportedException;
import net.minecraft.Util;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

public abstract class EntityRequirement implements Requirement {
    public static final String TYPE_NORMAL = "normal";
    public static final String TYPE_ITEM_FRAME = "item_frame";
    public static final String TYPE_OR = "or";
    public static final String TYPE_AND = "and";
    public static Map<String, Class<? extends EntityRequirement>> BUILDERS = Util.make(() -> {
        Map<String, Class<? extends EntityRequirement>> m = new HashMap<>();
        m.put(TYPE_NORMAL, NormalEntityRequirement.class);
        m.put(TYPE_ITEM_FRAME, ItemFrameEntityRequirement.class);
        m.put(TYPE_OR, EntityOrRequirement.class);
        m.put(TYPE_AND, EntityAndRequirement.class);
        return m;
    });
    public String type = "normal";
    protected CompoundTag nbt;
    private int requiredCount;

    public static EntityRequirement createFromJson(JsonElement element) {
        EntityRequirement requirement = null;
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

    public static boolean isArray(Object obj) {
        return obj != null && obj.getClass().isArray();
    }

    // 比较两个数组内容是否相等
    public static boolean arrayEquals(Object arr1, Object arr2) {
        // 类型检查
        if (!isArray(arr1) || !isArray(arr2)) return false;

        // 长度检查
        int length1 = Array.getLength(arr1);
        int length2 = Array.getLength(arr2);
        if (length1 != length2) return false;

        // 元素比较
        for (int i = 0; i < length1; i++) {
            Object elem1 = Array.get(arr1, i);
            Object elem2 = Array.get(arr2, i);

            if (elem1 == elem2) continue;
            if (elem1 == null || elem2 == null) return false;

            if (elem1.getClass().isArray() && elem2.getClass().isArray()) {
                if (!arrayEquals(elem1, elem2)) return false;
            } else if (!elem1.equals(elem2)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String getType() {
        return RitualData.ENTITIES;
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
            if (canCompareNBT() && jsonObject.has("nbt")) {
                nbt = CraftingHelper.getNBT(jsonObject.get("nbt"));
            }
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

    public final boolean canUseRequirement(Level level, Entity entity) {
        boolean hasNbtAndCorrect = true;
        if (nbt != null && canCompareNBT()) {
            if (entity instanceof LivingEntity) {
                EntityExpandedContext ec = ((LivingEntityEC) entity).revelationfix$livingECData();
                if (ec.tempNbtForServer == null) {
                    ec.tempNbtForServer = NbtPredicate.getEntityTagToCompare(entity);
                    ec.usingTempNbtSeconds = 10;
                }
                hasNbtAndCorrect = contains(ec.tempNbtForServer, nbt);
            }
        }
        return hasNbtAndCorrect && canUse(level, entity);
    }

    protected abstract boolean canUse(Level level, Entity entity);

    /**
     * @param main    实体nbt
     * @param toCheck 要包含的nbt
     * @return 实体nbt是否包含所有toCheck
     */
    public boolean contains(CompoundTag main, CompoundTag toCheck) {
        for (String s : toCheck.getAllKeys()) {
            Tag tag = toCheck.get(s);
            if (tag.getId() == 10) {
                if (main.contains(s, 10)) {
                    CompoundTag compoundtag = main.getCompound(s);
                    return contains(compoundtag, (CompoundTag) tag);
                } else {
                    return false;
                }
            } else {
                if (main.contains(s)) {
                    if (!NbtUtils.compareNbt(main.get(s), tag, true))
                        return false;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    protected boolean canCompareNBT() {
        return true;
    }
}
