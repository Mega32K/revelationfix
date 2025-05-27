package com.mega.revelationfix.mixin.fantasy_ending.time.time.millis;

import com.mega.revelationfix.safe.NoModDependsMixin;
import com.mega.revelationfix.util.time.TimeContext;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MinecraftServer.class)
@NoModDependsMixin("fantasy_ending")
public class MinecraftServerMixin {
    @Redirect(method = "prepareLevels", at = @At(value = "INVOKE", target = "Lnet/minecraft/Util;getMillis()J"))
    private long aLong() {
        return TimeContext.Both.getRealMillis();
    }

    @Redirect(method = "stopServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/Util;getMillis()J"))
    private long aLong2() {
        return TimeContext.Both.getRealMillis();
    }

    @Redirect(method = "runServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/Util;getMillis()J"))
    private long aLong3() {
        return TimeContext.Both.getRealMillis();
    }

    @Redirect(method = "haveTime", at = @At(value = "INVOKE", target = "Lnet/minecraft/Util;getMillis()J"))
    private long aLong4() {
        return TimeContext.Both.getRealMillis();
    }
}
