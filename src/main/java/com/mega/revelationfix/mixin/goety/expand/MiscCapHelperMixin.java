package com.mega.revelationfix.mixin.goety.expand;

import com.Polarice3.Goety.utils.MiscCapHelper;
import com.mega.endinglib.util.annotation.DeprecatedMixin;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MiscCapHelper.class, remap = false)
@DeprecatedMixin
public class MiscCapHelperMixin {
    @Inject(method = "decreaseShields", at = @At("HEAD"))
    private static void decreaseShields(LivingEntity livingEntity, CallbackInfo ci) {

    }
}
