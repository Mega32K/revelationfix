package com.mega.revelationfix.mixin;

import com.mega.revelationfix.safe.entity.DeathArrowEC;
import net.minecraft.world.entity.projectile.Arrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Arrow.class)
public abstract class ArrowMixin {
    @Inject(method = "makeParticle", at = @At("HEAD"), cancellable = true)
    private void makeParticle(int p_36877_, CallbackInfo ci) {
        if (this instanceof DeathArrowEC deathArrowEC) {
            if (deathArrowEC.revelationfix$getTrailData().shouldRenderTrail())
                ci.cancel();
        }
    }
}
