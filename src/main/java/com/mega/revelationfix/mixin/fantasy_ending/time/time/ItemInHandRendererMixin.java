package com.mega.revelationfix.mixin.fantasy_ending.time.time;

import com.mega.revelationfix.common.client.RendererUtils;
import com.mega.revelationfix.safe.NoModDependsMixin;
import com.mega.revelationfix.util.time.TimeContext;
import com.mega.revelationfix.util.time.TimeStopUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemInHandRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemInHandRenderer.class)
@NoModDependsMixin("fantasy_ending")
public class ItemInHandRendererMixin {
    @ModifyVariable(method = "renderHandsWithItems", at = @At("HEAD"), argsOnly = true)
    private float renderHandsWithItems(float p_109315_) {
        if (TimeStopUtils.isTimeStop
                && RendererUtils.isTimeStop_andSameDimension
                && Minecraft.getInstance().player != null && TimeStopUtils.canMove(Minecraft.getInstance().player))
            return TimeContext.Client.timer.partialTick;
        return p_109315_;
    }
}
