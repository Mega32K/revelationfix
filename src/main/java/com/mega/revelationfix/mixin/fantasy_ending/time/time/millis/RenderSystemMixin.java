package com.mega.revelationfix.mixin.fantasy_ending.time.time.millis;

import com.mega.revelationfix.safe.NoModDependsMixin;
import com.mega.revelationfix.util.time.TimeContext;
import com.mojang.blaze3d.systems.RenderSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RenderSystem.class)
@NoModDependsMixin("fantasy_ending")
public class RenderSystemMixin {
    @Redirect(method = "pollEvents", at = @At(value = "INVOKE", target = "Lnet/minecraft/Util;getMillis()J"))
    private static long aLong() {
        return TimeContext.Both.getRealMillis();
    }

    @Redirect(method = "isFrozenAtPollEvents", at = @At(value = "INVOKE", target = "Lnet/minecraft/Util;getMillis()J"))
    private static long aLong2() {
        return TimeContext.Both.getRealMillis();
    }
}
