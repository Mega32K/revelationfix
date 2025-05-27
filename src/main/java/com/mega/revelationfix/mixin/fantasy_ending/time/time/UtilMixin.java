package com.mega.revelationfix.mixin.fantasy_ending.time.time;

import com.mega.revelationfix.safe.NoModDependsMixin;
import com.mega.revelationfix.util.time.TimeContext;
import net.minecraft.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Util.class)
@NoModDependsMixin("fantasy_ending")
public class UtilMixin {
    @Inject(method = "getMillis", at = @At("HEAD"), cancellable = true)

    private static void getMillis(CallbackInfoReturnable<Long> cir) {
        cir.setReturnValue(TimeContext.Both.timeStopModifyMillis);
    }
}
