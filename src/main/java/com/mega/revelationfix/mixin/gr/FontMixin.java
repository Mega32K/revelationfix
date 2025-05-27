package com.mega.revelationfix.mixin.gr;

import com.mega.revelationfix.common.client.font.MinecraftFont;
import com.mega.revelationfix.common.compat.SafeClass;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import z1gned.goetyrevelation.util.ATAHelper;

@Mixin(Font.class)

public abstract class FontMixin {

    @Shadow
    private boolean filterFishyGlyphs;

    public FontMixin() {
    }

    @Shadow
    public static int adjustColor(int p_92720_) {
        return 0;
    }

    @Shadow
    public abstract void drawInBatch8xOutline(FormattedCharSequence var1, float var2, float var3, int var4, int var5, Matrix4f var6, MultiBufferSource var7, int var8);

    @Shadow
    public abstract FontSet getFontSet(ResourceLocation p_92864_);

    @Inject(
            at = {@At("TAIL")},
            method = {"drawInBatch(Lnet/minecraft/util/FormattedCharSequence;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/gui/Font$DisplayMode;II)I"},
            cancellable = true
    )
    private void drawOutLine(FormattedCharSequence p_273262_, float p_273006_, float p_273254_, int p_273375_, boolean p_273674_, Matrix4f p_273525_, MultiBufferSource p_272624_, Font.DisplayMode p_273418_, int p_273330_, int p_272981_, CallbackInfoReturnable<Integer> cir) {
        if (!SafeClass.isModernUILoaded())
            if (ATAHelper.getFormattingName(p_273262_).equals("apollyon")) {
                cir.setReturnValue(MinecraftFont.INSTANCE.drawInBatch(p_273262_, p_273006_, p_273254_, p_273375_, p_273674_, p_273525_, p_272624_, p_273418_, p_273330_, p_272981_));
            }

    }
}
