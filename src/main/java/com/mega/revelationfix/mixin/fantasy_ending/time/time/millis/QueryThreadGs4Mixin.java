package com.mega.revelationfix.mixin.fantasy_ending.time.time.millis;

import com.mega.revelationfix.safe.NoModDependsMixin;
import com.mega.revelationfix.util.time.TimeContext;
import net.minecraft.server.rcon.thread.QueryThreadGs4;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(QueryThreadGs4.class)
@NoModDependsMixin("fantasy_ending")
public class QueryThreadGs4Mixin {
    @Redirect(method = "buildRuleResponse", at = @At(value = "INVOKE", target = "Lnet/minecraft/Util;getMillis()J"))
    private long aLong() {
        return TimeContext.Both.getRealMillis();
    }

    @Redirect(method = "pruneChallenges", at = @At(value = "INVOKE", target = "Lnet/minecraft/Util;getMillis()J"))
    private long aLong2() {
        return TimeContext.Both.getRealMillis();
    }

    @Redirect(method = "run", at = @At(value = "INVOKE", target = "Lnet/minecraft/Util;getMillis()J"))
    private long aLong3() {
        return TimeContext.Both.getRealMillis();
    }
}
