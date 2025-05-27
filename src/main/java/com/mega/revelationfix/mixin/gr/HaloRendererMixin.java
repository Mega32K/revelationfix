package com.mega.revelationfix.mixin.gr;

import com.mojang.blaze3d.Blaze3D;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import z1gned.goetyrevelation.client.layer.PlayerHaloLayer;

@Mixin(PlayerHaloLayer.class)
public class HaloRendererMixin {
    @ModifyArg(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/player/AbstractClientPlayer;FFFFFF)V", at = @At(value = "INVOKE", target = "Lcom/mojang/math/Axis;rotationDegrees(F)Lorg/joml/Quaternionf;"))
    private float render(float p_253800_) {
        return (float) Blaze3D.getTime() * 18.0F;
    }
}
