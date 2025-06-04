package com.mega.revelationfix.mixin.goety;

import com.Polarice3.Goety.common.entities.neutral.AbstractWraith;
import com.mega.revelationfix.util.LivingEntityEC;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractWraith.class)
public class AbstractWraithMixin {
    @Inject(method = "magicFire", at = @At("HEAD"), cancellable = true, remap = false)
    private void magicFire(LivingEntity livingEntity, CallbackInfo ci) {
        if (((LivingEntityEC) this).revelationfix$livingECData().banAnySpelling > 0)
            ci.cancel();
    }
}
