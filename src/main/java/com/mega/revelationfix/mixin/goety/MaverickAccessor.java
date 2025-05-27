package com.mega.revelationfix.mixin.goety;

import com.Polarice3.Goety.common.entities.hostile.cultists.Maverick;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = Maverick.class, remap = false)
public interface MaverickAccessor {
    @Accessor("fleeTime")
    void setFleeTime(int time);

    @Accessor("fleeTime")
    int fleeTime();
}
