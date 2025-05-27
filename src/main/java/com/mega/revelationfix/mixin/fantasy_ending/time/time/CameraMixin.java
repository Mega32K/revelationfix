package com.mega.revelationfix.mixin.fantasy_ending.time.time;

import com.mega.revelationfix.common.client.RendererUtils;
import com.mega.revelationfix.safe.NoModDependsMixin;
import com.mega.revelationfix.util.time.TimeContext;
import com.mega.revelationfix.util.time.TimeStopUtils;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Camera.class)
@NoModDependsMixin("fantasy_ending")
public class CameraMixin {
    @ModifyVariable(method = "setup", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private float partial(float value) {
        if (TimeStopUtils.isTimeStop && RendererUtils.isTimeStop_andSameDimension && Minecraft.getInstance().player != null) {
            value = TimeStopUtils.canMove(Minecraft.getInstance().player) ? TimeContext.Client.timer.partialTick : 0F;
        }
        return value;
    }
}
