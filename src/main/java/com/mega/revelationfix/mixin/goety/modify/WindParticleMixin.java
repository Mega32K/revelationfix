package com.mega.revelationfix.mixin.goety.modify;

import com.Polarice3.Goety.client.particles.WindParticle;
import com.mega.revelationfix.common.config.GoetyModificationClientConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WindParticle.class)
public abstract class WindParticleMixin {
    @Inject(method = "getLightColor", at = @At("HEAD"), cancellable = true)
    private void brighter(float pPartialTick, CallbackInfoReturnable<Integer> cir) {
        if (GoetyModificationClientConfig.brighterWindParticle)
            cir.setReturnValue(15728880);
    }
}
