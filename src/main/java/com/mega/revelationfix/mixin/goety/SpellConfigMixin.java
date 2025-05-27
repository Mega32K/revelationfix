package com.mega.revelationfix.mixin.goety;

import com.Polarice3.Goety.config.SpellConfig;
import com.mega.revelationfix.common.config.GRSpellConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpellConfig.class)
public class SpellConfigMixin {
    @Shadow(remap = false)
    @Final
    public static ForgeConfigSpec.Builder BUILDER;

    @Inject(method = "<clinit>", at = @At(value =
            "INVOKE",
            target = "Lnet/minecraftforge/common/ForgeConfigSpec$Builder;push(Ljava/lang/String;)Lnet/minecraftforge/common/ForgeConfigSpec$Builder;",
            ordinal = 1,
            shift = At.Shift.AFTER,
            remap = false)
    )
    private static void insertConfig(CallbackInfo ci) {
        GRSpellConfig.init(BUILDER);
    }
}
