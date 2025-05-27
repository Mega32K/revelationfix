package com.mega.revelationfix.mixin.gr;

import com.mega.revelationfix.common.config.CommonConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import z1gned.goetyrevelation.goal.ApollyonDeathArrowGoal;

@Mixin(ApollyonDeathArrowGoal.class)
public class ApollyonDeathArrowGoalMixin {
    @Inject(method = "getCastingInterval", at = @At("HEAD"), cancellable = true, remap = false)
    private void getCastingInterval(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(CommonConfig.apollyonShootingCooldown);
    }
}
