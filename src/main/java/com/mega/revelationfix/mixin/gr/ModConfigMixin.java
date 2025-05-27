package com.mega.revelationfix.mixin.gr;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import z1gned.goetyrevelation.config.ModConfig;

@Mixin(ModConfig.class)
public class ModConfigMixin {
    @ModifyConstant(method = "<clinit>", constant = @Constant(stringValue = "apollyon_health"))
    private static String apollyon_health(String constant) {
        return "apollyon_health";
    }

    @ModifyConstant(method = "<clinit>", constant = @Constant(stringValue = "Sets the apollyon's health，Default: 560"))
    private static String d0(String constant) {
        return "Sets the apollyon's health，Default: 666";
    }

    @ModifyConstant(method = "<clinit>", constant = @Constant(doubleValue = 560.0))
    private static double d1(double constant) {
        return 666.0D;
    }
}
