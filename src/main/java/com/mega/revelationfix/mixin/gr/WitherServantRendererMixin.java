package com.mega.revelationfix.mixin.gr;

import com.mega.revelationfix.safe.mixinpart.ModDependsMixin;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import z1gned.goetyrevelation.ModMain;
import z1gned.goetyrevelation.client.render.WitherServantRenderer;

@Mixin(WitherServantRenderer.class)
@ModDependsMixin("com.mega.revelationfix.mixin.gr")
public class WitherServantRendererMixin {
    @Mutable
    @Shadow(remap = false)
    @Final
    private static ResourceLocation WITHER_LOCATION;

    @Mutable
    @Shadow(remap = false)
    @Final
    private static ResourceLocation WITHER_INVULNERABLE_LOCATION;

    @Inject(
            at = {@At(
                    value = "FIELD",
                    shift = At.Shift.AFTER,
                    target = "Lz1gned/goetyrevelation/client/render/WitherServantRenderer;WITHER_LOCATION:Lnet/minecraft/resources/ResourceLocation;",
                    remap = false
            )},
            method = {"<clinit>"}
    )
    private static void replace0(CallbackInfo ci) {
        WITHER_LOCATION = new ResourceLocation(ModMain.MODID, "textures/entity/wither_servant/wither.png");
    }

    @Inject(
            at = {@At(
                    value = "FIELD",
                    shift = At.Shift.AFTER,
                    target = "Lz1gned/goetyrevelation/client/render/WitherServantRenderer;WITHER_INVULNERABLE_LOCATION:Lnet/minecraft/resources/ResourceLocation;",
                    remap = false
            )},
            method = {"<clinit>"}
    )
    private static void replace1(CallbackInfo ci) {
        WITHER_INVULNERABLE_LOCATION = new ResourceLocation(ModMain.MODID, "textures/entity/wither_servant/wither2.png");
    }
}
