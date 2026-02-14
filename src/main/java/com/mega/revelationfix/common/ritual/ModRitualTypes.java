package com.mega.revelationfix.common.ritual;

import com.Polarice3.Goety.api.ritual.IRitualType;
import com.Polarice3.Goety.api.ritual.RitualType;

import javax.annotation.Nullable;

public class ModRitualTypes {
    public static final String THE_END = "the_end";
    public static final String THE_END_MAGIC = "te_magic";
    public static IRitualType register(IRitualType ritualType) {
        RitualType.addRitualType(ritualType.getName(),  ritualType);
        return ritualType;
    }
    @Nullable
    public static IRitualType unregister(IRitualType ritualType) {
        return RitualType.getRitualTypeList().remove(ritualType.getName());
    }
    @Nullable
    public static IRitualType unregister(String ritualName) {
        return RitualType.getRitualTypeList().remove(ritualName);
    }
    public static void initModRituals() {
        register(new TheEndRitualType());
        register(new TheEndMagicRitualType());
    }
}
