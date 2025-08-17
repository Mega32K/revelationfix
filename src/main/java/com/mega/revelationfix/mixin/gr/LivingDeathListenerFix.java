package com.mega.revelationfix.mixin.gr;

import net.minecraftforge.event.entity.living.LivingDeathEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import z1gned.goetyrevelation.event.LivingDeathListener;

@Mixin(value = LivingDeathListener.class, remap = false)
public class LivingDeathListenerFix {
    @Inject(method = "onLivingDeath", at = @At("HEAD"), cancellable = true)
    private static void fix(LivingDeathEvent event, CallbackInfo ci) {
        ci.cancel();

    }
}
