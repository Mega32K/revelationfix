package com.mega.revelationfix.util.time;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.LivingEntity;

public class TimeStopEntityData {
    public static String TIME_STOP_COUNT_NAME = "timeStopCount";
    public static EntityDataAccessor<Integer> TIME_STOP_COUNT;

    public static int getTimeStopCount(LivingEntity livingEntity) {
        return livingEntity.getEntityData().get(TIME_STOP_COUNT);
    }

    public static void setTimeStopCount(LivingEntity livingEntity, int p_32284_) {
        livingEntity.getEntityData().set(TIME_STOP_COUNT, p_32284_);
    }
}
