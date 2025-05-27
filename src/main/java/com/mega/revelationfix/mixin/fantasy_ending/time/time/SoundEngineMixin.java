package com.mega.revelationfix.mixin.fantasy_ending.time.time;

import com.mega.revelationfix.common.client.RendererUtils;
import com.mega.revelationfix.safe.NoModDependsMixin;
import com.mega.revelationfix.util.time.TimeStopUtils;
import net.minecraft.client.sounds.SoundEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundEngine.class)
@NoModDependsMixin("fantasy_ending")
public class SoundEngineMixin {
    @Inject(method = "tickNonPaused", at = @At("HEAD"), cancellable = true)
    private void tick(CallbackInfo ci) {
        if (TimeStopUtils.isTimeStop && RendererUtils.isTimeStop_andSameDimension)
            ci.cancel();
    }

    @Inject(method = "resume", at = @At("HEAD"), cancellable = true)
    private void resume(CallbackInfo ci) {
        if (TimeStopUtils.isTimeStop && RendererUtils.isTimeStop_andSameDimension)
            ci.cancel();
    }
}
