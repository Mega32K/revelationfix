package com.mega.revelationfix.util.time;

import com.mega.revelationfix.client.RendererUtils;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Timer;
import net.minecraft.util.FastColor;
import net.minecraft.util.TimeSource;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector4f;

import java.awt.*;
import java.util.concurrent.atomic.AtomicLong;

public class TimeContext {

    public static float safeClientPartialTicks() {
        return Minecraft.getInstance().getFrameTime();
    }

    @OnlyIn(Dist.CLIENT)
    public static class Client {
        private static final AtomicLong SEED_UNIQUIFIER = new AtomicLong(8682522807148012L);
        public static Timer timer = new Timer(20.0F, 0L);
        /**
         * 永远每1/100s自增1
         */
        public static long count = 0L;
        public static long timeStopGLFW = 0L;

        public static float getCommonDegrees() {
            return timeStopGLFW / 100F;
        }

        public static float currentSeconds() {
            return count / 1000F;
        }

        public static float currentSecondsTS() {
            return timeStopGLFW / 1000F;
        }

        public static long generateUniqueSeed() {
            return (TimeStopUtils.isTimeStop && RendererUtils.isTimeStop_andSameDimension) ? (SEED_UNIQUIFIER.get() * 1181783497276652981L) ^ (Both.timeStopModifyMillis * 1000000L) : SEED_UNIQUIFIER.updateAndGet((p_224601_) -> {
                return p_224601_ * 1181783497276652981L;
            }) ^ (Both.timeStopModifyMillis * 1000000L);
        }

        public static float alwaysPartial() {
            return (TimeStopUtils.isTimeStop || Minecraft.getInstance().isPaused()) ? Client.timer.partialTick : Minecraft.getInstance().getPartialTick();
        }

        public static float getPartialTickCount(Entity owner, boolean always) {
            return owner.tickCount + (always ? alwaysPartial() : Minecraft.getInstance().getPartialTick());
        }
    }

    public static class Both {
        public static long timeStopModifyMillis = 0L;
        public static TimeSource.NanoTimeSource timeSource = System::nanoTime;

        public static long getRealMillis() {
            return getNanos() / 1000000L;
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
}
