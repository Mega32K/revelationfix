package com.mega.revelationfix.common.compat.fantasy_ending;

import com.mega.uom.render.RendererUtils;
import com.mega.uom.util.time.TimeStopEntityData;
import com.mega.uom.util.time.TimeStopUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

public class FeSafeClass {
    public static void use(boolean z, LivingEntity living, boolean force, int ticks) {
        if (force && !z)
            TimeStopEntityData.setTimeStopCount(living, 0);
        TimeStopUtils.use(z, living, force, ticks);
    }

    public static int getTimeStopCount(LivingEntity living) {
        return TimeStopEntityData.getTimeStopCount(living);
    }

    public static boolean isFieldTimeStop() {
        return TimeStopUtils.isTimeStop;
    }

    public static boolean isTimeStop(ServerLevel serverLevel) {
        return TimeStopUtils.isTimeStop && TimeStopUtils.andSameDimension(serverLevel);
    }

    public static boolean isClientTimeStop() {
        return TimeStopUtils.isTimeStop && RendererUtils.isTimeStop_andSameDimension;
    }
}
