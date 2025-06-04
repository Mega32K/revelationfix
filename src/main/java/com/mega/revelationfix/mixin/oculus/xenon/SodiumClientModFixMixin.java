package com.mega.revelationfix.mixin.oculus.xenon;

import com.mega.revelationfix.safe.mixinpart.DevEnvMixin;
import me.jellysquid.mods.sodium.client.SodiumClientMod;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SodiumClientMod.class)
@DevEnvMixin
public abstract class SodiumClientModFixMixin {
    @Shadow(remap = false)
    private static void updateFingerprint() {
    }

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lme/jellysquid/mods/sodium/client/SodiumClientMod;updateFingerprint()V", remap = false) )
    private void updateFingerprint2() {
        boolean z = true;
        try {
            Minecraft mc = Minecraft.getInstance();
            mc.getProfiler().push("");
            mc.getProfiler().pop();
        } catch (Throwable throwable) {
            z = false;
        }
        if (z) {
            updateFingerprint();
        }
    }
}
