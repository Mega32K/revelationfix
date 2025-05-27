package com.mega.revelationfix.mixin.fantasy_ending.time.time;

import com.mega.revelationfix.common.client.RendererUtils;
import com.mega.revelationfix.safe.NoModDependsMixin;
import com.mega.revelationfix.util.time.TimeStopUtils;
import net.minecraft.client.renderer.PostChain;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PostChain.class)
@NoModDependsMixin("fantasy_ending")
public class PostChainMixin {
    @ModifyVariable(method = "process", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private float process(float partialTicks) {
        if (TimeStopUtils.isTimeStop && RendererUtils.isTimeStop_andSameDimension) partialTicks = 0;
        return partialTicks;
    }
}
