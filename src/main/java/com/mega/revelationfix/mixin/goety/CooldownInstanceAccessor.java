package com.mega.revelationfix.mixin.goety;

import com.Polarice3.Goety.common.capabilities.soulenergy.FocusCooldown;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FocusCooldown.CooldownInstance.class)
public interface CooldownInstanceAccessor {
    @Accessor(value = "totalTime", remap = false)
    @Mutable
    void setTotal(int time);

    @Accessor(value = "time", remap = false)
    void setTime(int time);

    @Accessor(value = "time", remap = false)
    int time();
}
