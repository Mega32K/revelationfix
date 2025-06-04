package com.mega.revelationfix.mixin.fantasy_ending.time.time;

import com.mega.revelationfix.client.RendererUtils;
import com.mega.revelationfix.safe.mixinpart.NoModDependsMixin;
import com.mega.revelationfix.util.time.TimeStopUtils;
import net.minecraft.client.sounds.SoundManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(SoundManager.class)
@NoModDependsMixin("fantasy_ending")
public class SoundManagerMixin {
    @ModifyVariable(method = "tick", at = @At("HEAD"), argsOnly = true)
    private boolean tick(boolean v) {
        if (TimeStopUtils.isTimeStop && RendererUtils.isTimeStop_andSameDimension) v = true;
        return v;
    }
}
