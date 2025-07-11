package com.mega.revelationfix.client.font;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.FontManager;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;

import java.awt.*;
import java.util.Map;
import java.util.function.Function;

public class OdamaneFont extends Font {
    public static final ObjectArrayList<CenterComponentGroup> centerComponentGroups = new ObjectArrayList<>();
    public static FontManager fontManager = Minecraft.getInstance().fontManager;
    public static OdamaneFont INSTANCE;
    static Component HALO_DESC_2 = null;

    static {
        INSTANCE = new OdamaneFont((p_284586_) -> fontManager.fontSets.getOrDefault(fontManager.renames.getOrDefault(p_284586_, p_284586_), fontManager.missingFontSet), false, fontManager.fontSets);
        new CenterComponentGroup(Component.translatable("item.goety_revelation.halo_of_the_end.default_1"), Component.translatable("item.goety_revelation.halo_of_the_end.default_2"));
        new CenterComponentGroup(Component.translatable("item.goety_revelation.bow_of_revelation.desc0"), Component.translatable("item.goety_revelation.bow_of_revelation.desc1"), Component.translatable("item.goety_revelation.bow_of_revelation.desc2"));
        new CenterComponentGroup(Component.translatable("item.goety_revelation.the_needle.tail0"), Component.translatable("item.goety_revelation.the_needle.tail1"));

        new CenterComponentGroup(Component.translatable("item.goety_revelation.eternal_watch.desc0"), Component.translatable("item.goety_revelation.eternal_watch.desc1"), Component.translatable("item.goety_revelation.eternal_watch.desc2"), Component.translatable("item.goety_revelation.eternal_watch.desc3"));
        new CenterComponentGroup(Component.translatable("item.goety_revelation.dimensional_will.desc0"), Component.translatable("item.goety_revelation.dimensional_will.desc1"));
    }

    private float width;

    public OdamaneFont(Function<ResourceLocation, FontSet> p_243253_, boolean p_243245_, Map<ResourceLocation, FontSet> f) {
        super(p_243253_, p_243245_);
    }

    public void push(float width) {
        this.width = width;
    }

    public void pop() {
        this.width = 0;
    }

    @Override
    public int drawInBatch(FormattedCharSequence p_273262_, float startX, float startY, int iColor, boolean p_273674_, Matrix4f matrix4f, MultiBufferSource bufferSource, DisplayMode displayMode, int overlay, int light) {
        String text = FontTextBuilder.formattedCharSequenceToString(p_273262_);
        if (width > 0F) {
            for (CenterComponentGroup ccg : centerComponentGroups) {
                if (ccg.is(text)) {
                    matrix4f.translate(-width(p_273262_) / 2F + width / 2F, 0, 0);
                    int finalColor = 0;
                    String text2 = FontTextBuilder.formattedCharSequenceToStringEden(p_273262_);
                    if (!text2.isEmpty() && (!text2.replace(" ", "").isEmpty())) {
                        finalColor = renderEden(text2, startX, startY, iColor, p_273674_, matrix4f, bufferSource, displayMode, overlay, light);;
                    } else {
                        finalColor = super.drawInBatch(p_273262_, startX, startY, iColor, p_273674_, matrix4f, bufferSource, displayMode, overlay, light);
                    }
                    matrix4f.translate(width(p_273262_) / 2F - width / 2F, 0, 0);
                    return finalColor;
                }
            }
        }
        String text2 = FontTextBuilder.formattedCharSequenceToStringEden(p_273262_);

        if (!text2.isEmpty() && (!text2.replace(" ", "").isEmpty())) {
            return renderEden(text2, startX, startY, iColor, p_273674_, matrix4f, bufferSource, displayMode, overlay, light);
        }
        return super.drawInBatch(p_273262_, startX, startY, iColor, p_273674_, matrix4f, bufferSource, displayMode, overlay, light);
    }
    public int renderEden(String text2, float startX, float startY, int iColor, boolean p_273674_, Matrix4f matrix4f, MultiBufferSource bufferSource, DisplayMode displayMode, int overlay, int light) {
        float colorr = (float) milliTime() * 0.0025F % 1.0F;
        float colorrStep = (float) rangeRemap(
                Mth.sin(((float) milliTime()* 0.005F)) % 6.28318D, -0.9D, 2.5D, 0.025D, 0.15D);
        float posX = startX;
        float xOffset = Mth.cos((float) milliTime() * 0.000833F);
        matrix4f.translate(xOffset, 0, 0);
        for (int i = 0; i < text2.length(); i++) {
            float yOffset = Mth.sin((i * (0.5F) + (float) milliTime() * 0.00166F));
            matrix4f.translate(0, yOffset, 0);

            int c = (int) (Mth.clamp((Mth.abs(Mth.cos(i * (0.2F) + (float) milliTime() / 720F)) * 255), 70, 200)) << 24 | 8323072 | 33792 | 146;
            posX = super.drawInBatch(String.valueOf(text2.charAt(i)), posX, startY, c, p_273674_, matrix4f, bufferSource, displayMode, overlay, light);
            matrix4f.translate(0, -yOffset, 0);
            colorr += colorrStep;
            colorr %= 1.0F;
        }
        matrix4f.translate(-xOffset, 0, 0);
        return (int) posX;
    }
    public static long milliTime() {
        return Util.getMillis();
    }
    public static double rangeRemap(double value, double low1, double high1, double low2, double high2) {
        return low2 + (value - low1) * (high2 - low2) / (high1 - low1);
    }
}
