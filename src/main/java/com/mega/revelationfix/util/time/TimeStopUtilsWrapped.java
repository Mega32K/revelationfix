package com.mega.revelationfix.util.time;

import net.minecraft.client.Minecraft;

//Client
public class TimeStopUtilsWrapped {
    public static void enable() {
        if (!TimeStopUtils.isTimeStop) {
            Minecraft mc = Minecraft.getInstance();
            assert mc.level != null;
            mc.getSoundManager().pause();
            TimeStopUtils.isTimeStop = true;
        }
    }

    public static void disable() {
        if (TimeStopUtils.isTimeStop) {
            Minecraft mc = Minecraft.getInstance();
            TimeStopUtils.isTimeStop = false;
            Minecraft.getInstance().getSoundManager().resume();
        }
    }
}
