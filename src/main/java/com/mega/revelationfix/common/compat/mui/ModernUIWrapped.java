package com.mega.revelationfix.common.compat.mui;

import com.mega.endinglib.api.client.text.TextColorUtils;
import com.mega.endinglib.api.client.text.mui.IModernTextRendererCall;
import com.mega.endinglib.api.client.text.mui.ModernTextRendererCall;
import com.mega.revelationfix.client.enums.ModChatFormatting;
import com.mega.revelationfix.client.font.FontTextBuilder;
import com.mega.revelationfix.client.font.OdamaneFont;
import icyllis.modernui.mc.text.ModernTextRenderer;
import icyllis.modernui.mc.text.TextLayout;
import icyllis.modernui.mc.text.TextLayoutEngine;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class ModernUIWrapped {
    public static void registerCalls() {
        ModernTextRendererCall.registerCall(ModChatFormatting.APOLLYON, new Apollyon());
        ModernTextRendererCall.registerCall(ModChatFormatting.EDEN, new Eden());
    }

    public static class Apollyon implements IModernTextRendererCall {
        @Override
        public void drawText(TextLayoutEngine mEngine, FormattedCharSequence text, float x, float y, int color, boolean dropShadow, Matrix4f matrix, MultiBufferSource source, Font.DisplayMode displayMode, int colorBackground, int packedLight, CallbackInfoReturnable<Float> cir) {
            boolean isBlack = (color & 16777215) == 0;
            int r;
            int g;
            int b;
            TextLayout layout = mEngine.lookupFormattedLayout(text);
            Matrix4f m4 = new Matrix4f(matrix);
            m4.translate(ModernTextRenderer.OUTLINE_OFFSET);
            layout.drawTextOutline(m4, source, x, y, 255, 0, 0, 255, packedLight);
        }
    }

    public static class Eden implements IModernTextRendererCall {
        @Override
        public boolean completelyReplaceRender() {
            return true;
        }

        @Override
        public float drawTextInstead(ModernTextRenderer modernTextRenderer, TextLayoutEngine mEngine, @NotNull FormattedCharSequence text, float x, float y, int color, boolean dropShadow, @NotNull Matrix4f matrix, @NotNull MultiBufferSource source, Font.DisplayMode displayMode, int colorBackground, int packedLight, ModernTextRendererCall call) {
            if (TextColorUtils.getCenteredTooltipWidth() < 0)
                IModernTextRendererCall.super.drawTextInstead(modernTextRenderer, mEngine, text, x, y, color, dropShadow, matrix, source, displayMode, colorBackground, packedLight, call);
            long millisTime = OdamaneFont.milliTime();
            float colorr = (float) millisTime * 0.0025F % 1.0F;
            float colorrStep = (float) OdamaneFont.rangeRemap(
                    Mth.sin(((float) OdamaneFont.milliTime() * 0.005F)) % 6.28318D, -0.9D, 2.5D, 0.025D, 0.15D);
            float posX = x;
            float xOffset = Mth.cos((float) millisTime * 0.000833F);
            matrix.translate(xOffset, 0, 0);
            String[] out = FontTextBuilder.formattedCharSequenceToStringEden(text);
            for (int i = 0; i < out[0].length(); i++) {
                float yOffset = Mth.sin((i * (0.5F) + (float) millisTime * 0.00166F));
                matrix.translate(0, yOffset, 0);

                int c = (int) (Mth.clamp((Mth.abs(Mth.cos(i * (0.2F) + (float) millisTime / 720F)) * 255), 70, 200)) << 24 | 8323072 | 33792 | 146;
                posX = modernTextRenderer.drawText(String.valueOf(out[0].charAt(i)), posX, y, c, dropShadow, matrix, source, displayMode, colorBackground, packedLight);
                matrix.translate(0, -yOffset, 0);
                colorr += colorrStep;
                colorr %= 1.0F;
            }
            matrix.translate(-xOffset, 0, 0);
            return posX;
        }
    }
}
