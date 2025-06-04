package com.mega.revelationfix.mixin.fantasy_ending.time.time.millis;

import com.mega.revelationfix.safe.mixinpart.NoModDependsMixin;
import com.mega.revelationfix.util.time.TimeContext;
import com.mojang.realmsclient.client.Ping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Ping.class)
@NoModDependsMixin("fantasy_ending")
public class PingMixin {
    @Redirect(method = "now", at = @At(value = "INVOKE", target = "Lnet/minecraft/Util;getMillis()J"))
    private static long aLong() {
        return TimeContext.Both.getRealMillis();
    }
}
