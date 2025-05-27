package com.mega.revelationfix.mixin.fantasy_ending.time.time;

import com.mega.revelationfix.safe.NoModDependsMixin;
import com.mega.revelationfix.util.time.TimeStopUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.MusicManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MusicManager.class)
@NoModDependsMixin("fantasy_ending")
public class MusicManagerMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void tick(CallbackInfo ci) {
        if (TimeStopUtils.isTimeStop && TimeStopUtils.andSameDimension(minecraft.level))
            ci.cancel();
    }
}
