package com.mega.revelationfix.mixin.fantasy_ending.time.time;

import com.mega.revelationfix.safe.NoModDependsMixin;
import com.mega.revelationfix.util.time.TimeContext;
import com.mega.revelationfix.util.time.TimeStopRandom;
import net.minecraft.client.renderer.entity.EndermanRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.RandomSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EndermanRenderer.class)
@NoModDependsMixin("fantasy_ending")
public class EndermanRendererMixin {
    @Mutable
    @Shadow
    @Final
    private RandomSource random;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(EntityRendererProvider.Context p_173992_, CallbackInfo ci) {
        this.random = new TimeStopRandom(TimeContext.Client.generateUniqueSeed());
    }
}
