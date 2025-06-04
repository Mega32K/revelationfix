package com.mega.revelationfix.client;

import net.minecraft.Util;
import net.minecraft.util.FastColor;
import net.minecraft.util.TimeSource;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

public class TimeContext {
    public static long timeStopModifyMillis = 0L;
    public static TimeSource.NanoTimeSource timeSource = System::nanoTime;

    public static long getRealMillis() {
        return getNanos() / 1000000L;
    }

    public static float getCommonDegrees() {
        return (float) (GLFW.glfwGetTime() / 10F);
    }

    public static Color rainbow(float f0, float saturation, float lgiht) {
        float colorr = (float) milliTime() / f0 % 1.0F;
        Color color = Color.getHSBColor(colorr, saturation, lgiht);
        return color;
    }

    public static Vector4f rainbowV4(float f0, float saturation, float lgiht) {
        float colorr = (float) milliTime() / f0 % 1.0F;
        Color color = Color.getHSBColor(colorr, saturation, lgiht);
        return new Vector4f(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public static int argbRainbow(float f0, float saturation, float lgiht) {
        Color color = rainbow(f0, saturation, lgiht);
        return FastColor.ARGB32.color(color.getAlpha(), color.getRed(), color.getGreen(), color.getBlue());
    }

    public static long getNanos() {
        return timeSource.getAsLong();
    }

    public static long milliTime() {
        return Util.getMillis();
    }
}
