package com.mega.revelationfix.mixin.modernui;

import com.mega.revelationfix.client.font.FontTextBuilder;
import com.mega.revelationfix.safe.mixinpart.ModDependsMixin;
import com.mega.revelationfix.safe.ModernTextRendererCall;
import icyllis.modernui.mc.text.ModernTextRenderer;
import icyllis.modernui.mc.text.TextLayoutEngine;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.FormattedCharSequence;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Mixin(value = ModernTextRenderer.class, remap = false)
@ModDependsMixin("modernui")
//OLD
public abstract class ModernTextRendererMixin {
    @Shadow
    @Final
    public static Vector3f OUTLINE_OFFSET;
    @Shadow
    public static volatile boolean sAllowShadow;
    @Shadow
    public static volatile float sShadowOffset;
    @Shadow
    @Final
    public static Vector3f SHADOW_OFFSET;
    @Shadow
    @Final
    private TextLayoutEngine mEngine;
    @Unique
    private ModernTextRendererCall call = null;

    @Unique
    private static FormattedCharSequence bakedOutline(FormattedCharSequence src) {
        String text = FontTextBuilder.formattedCharSequenceToStringApollyon(src);
        return FormattedCharSequence.forward(text, Style.EMPTY);
    }

    @Unique
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

    @Shadow
    public abstract int chooseMode(Matrix4f ctm, Font.DisplayMode displayMode);

    @Inject(method = "drawText(Lnet/minecraft/util/FormattedCharSequence;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/gui/Font$DisplayMode;II)F", at = @At(value = "INVOKE", target = "Licyllis/modernui/mc/text/TextLayoutEngine;lookupFormattedLayout(Lnet/minecraft/util/FormattedCharSequence;)Licyllis/modernui/mc/text/TextLayout;"), remap = false)
    private void drawText0(FormattedCharSequence text, float x, float y, int color, boolean dropShadow, Matrix4f matrix, MultiBufferSource source, Font.DisplayMode displayMode, int colorBackground, int packedLight, CallbackInfoReturnable<Float> cir) {
        if (text != FormattedCharSequence.EMPTY) {
            if (revelationfix$isApollyon(text)) {
                call = new ModernTextRendererCall(this.mEngine, x);
            }
        }
    }

    @Inject(method = "drawText(Lnet/minecraft/util/FormattedCharSequence;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/gui/Font$DisplayMode;II)F", at = @At(value = "RETURN", ordinal = 1), remap = false)
    private void drawText(FormattedCharSequence text, float x, float y, int color, boolean dropShadow, Matrix4f matrix, MultiBufferSource source, Font.DisplayMode displayMode, int colorBackground, int packedLight, CallbackInfoReturnable<Float> cir) {
        if (call != null) {
            call.drawText(text, y, color, dropShadow, matrix, source, displayMode, colorBackground, packedLight, cir);
            call = null;
        }
    }
}
