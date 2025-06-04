package com.mega.revelationfix.mixin.fantasy_ending.time.time;

import com.mega.revelationfix.client.RendererUtils;
import com.mega.revelationfix.safe.mixinpart.NoModDependsMixin;
import com.mega.revelationfix.util.time.TimeStopUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleEngine.class)
@NoModDependsMixin("fantasy_ending")
public class ParticleManagerMixin {
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void tick(CallbackInfo ci) {
        if (TimeStopUtils.isTimeStop && RendererUtils.isTimeStop_andSameDimension) ci.cancel();
    }

    @ModifyVariable(method = "render*", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private float render(float value) {
        if (TimeStopUtils.isTimeStop && RendererUtils.isTimeStop_andSameDimension)
            return Minecraft.getInstance().getFrameTime();
        return value;
    }
}
