package com.mega.revelationfix.mixin.fantasy_ending.time;

import com.mega.revelationfix.client.RendererUtils;
import com.mega.revelationfix.safe.mixinpart.NoModDependsMixin;
import com.mega.revelationfix.util.time.TimeContext;
import com.mega.revelationfix.util.time.TimeStopUtils;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Gui.class)
@NoModDependsMixin("fantasy_ending")
public class GuiMixin {
    @ModifyVariable(method = "render", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private float partial(float value) {
        if (TimeStopUtils.isTimeStop && RendererUtils.isTimeStop_andSameDimension)
            value = TimeContext.Client.timer.partialTick;
        return value;
    }
}
