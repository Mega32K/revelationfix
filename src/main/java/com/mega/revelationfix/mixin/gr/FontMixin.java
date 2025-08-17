package com.mega.revelationfix.mixin.gr;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mega.endinglib.api.client.text.TextColorUtils;
import com.mega.endinglib.util.annotation.NoModDependsMixin;
import com.mega.revelationfix.client.enums.ModChatFormatting;
import com.mega.revelationfix.client.font.MinecraftFont;
import it.unimi.dsi.fastutil.ints.Int2CharOpenHashMap;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.FormattedCharSequence;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Font.class)
@NoModDependsMixin("modernui")
public abstract class FontMixin {

    @Shadow
    public boolean filterFishyGlyphs;

    public FontMixin() {
    }

    @Unique
    private static boolean revelationfix$hasGR(Int2CharOpenHashMap map) {
        for (char c : map.values())
            if (c == ModChatFormatting.APOLLYON.getChar() || c == ModChatFormatting.EDEN.getChar())
                return true;
        return false;
    }

    @WrapOperation(
            at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Font;drawInternal(Lnet/minecraft/util/FormattedCharSequence;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/gui/Font$DisplayMode;II)I")},
            method = {"drawInBatch(Lnet/minecraft/util/FormattedCharSequence;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/gui/Font$DisplayMode;II)I"}
    )
    private int drawOutLine(Font instance, FormattedCharSequence p_273025_, float p_273121_, float p_272717_, int p_273653_, boolean p_273531_, Matrix4f p_273265_, MultiBufferSource p_273560_, Font.DisplayMode p_273342_, int p_273373_, int p_273266_, Operation<Integer> original) {
        Int2CharOpenHashMap map = TextColorUtils.getColorChars(p_273025_);
        if (revelationfix$hasGR(map)) {
            return (int) MinecraftFont.INSTANCE.drawInBatchF(p_273025_, p_273121_, p_272717_, p_273653_, p_273531_, p_273265_, p_273560_, p_273342_, p_273373_, p_273266_, map);
        } else
            return original.call(instance, p_273025_, p_273121_, p_272717_, p_273653_, p_273531_, p_273265_, p_273560_, p_273342_, p_273373_, p_273266_);
    }
}
