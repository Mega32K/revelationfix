package com.mega.revelationfix.mixin.fantasy_ending.time;

import com.mega.revelationfix.safe.NoModDependsMixin;
import com.mega.revelationfix.util.time.TimeStopRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import javax.annotation.Nullable;

@Mixin(LevelRenderer.class)
@NoModDependsMixin("fantasy_ending")
public abstract class LevelRendererMixin {
    @Shadow
    private Minecraft minecraft;

    @Shadow
    @Nullable
    private ClientLevel level;

    @Shadow
    public abstract void levelEvent(int p_234305_, BlockPos p_234306_, int p_234307_);

    @Shadow
    public abstract void needsUpdate();

    @Redirect(method = "tickRain", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;create(J)Lnet/minecraft/util/RandomSource;"))
    private RandomSource tickRain(long p_216336_) {
        return new TimeStopRandom(p_216336_);
    }

    @Redirect(method = "drawStars", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;create(J)Lnet/minecraft/util/RandomSource;"))
    private RandomSource drawStars(long p_216336_) {
        return new TimeStopRandom(p_216336_);
    }
}
