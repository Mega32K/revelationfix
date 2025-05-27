package com.mega.revelationfix.safe;

import icyllis.modernui.mc.text.ModernTextRenderer;
import icyllis.modernui.mc.text.TextLayout;
import icyllis.modernui.mc.text.TextLayoutEngine;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.FormattedCharSequence;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public record ModernTextRendererCall(TextLayoutEngine mEngine, float x) {
    private static boolean revelationfix$isApollyon(FormattedCharSequence fcs) {
        AtomicBoolean str = new AtomicBoolean(false);
        fcs.accept((index, style, codePoint) -> {
            Optional<TextColor> optional = Optional.ofNullable(style.getColor());
            if (optional.isPresent()) {
                String s = optional.get().serialize();
                if (s.charAt(0) == 'a' && s.length() == "apollyon".length())
                    str.set(true);
            }
            return true;
        });
        return str.get();
    }

    public void drawText(FormattedCharSequence text, float y, int color, boolean dropShadow, Matrix4f matrix, MultiBufferSource source, Font.DisplayMode displayMode, int colorBackground, int packedLight, CallbackInfoReturnable<Float> cir) {
        if (text != FormattedCharSequence.EMPTY) {
            if (revelationfix$isApollyon(text)) {
                boolean isBlack = (color & 16777215) == 0;
                int r;
                int g;
                int b;
                TextLayout layout = this.mEngine.lookupFormattedLayout(text);
                Matrix4f m4 = new Matrix4f(matrix);
                m4.translate(ModernTextRenderer.OUTLINE_OFFSET);
                layout.drawTextOutline(m4, source, x, y, 255, 0, 0, 255, packedLight);
            }
        }
    }
}
