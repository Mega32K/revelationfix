package com.mega.revelationfix.mixin.fantasy_ending.time.time.millis;

import com.mega.revelationfix.safe.NoModDependsMixin;
import com.mega.revelationfix.util.time.TimeContext;
import net.minecraft.client.gui.screens.LoadingOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LoadingOverlay.class)
@NoModDependsMixin("fantasy_ending")
public class LoadingOverlayMixin {
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/Util;getMillis()J"))
    private long aLong() {
        return TimeContext.Both.getRealMillis();
    }
}
