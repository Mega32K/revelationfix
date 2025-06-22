package com.mega.revelationfix.mixin.eeeabsmobs;

import com.eeeab.eeeabsmobs.sever.entity.immortal.EntityImmortal;
import com.mega.endinglib.util.annotation.ModDependsMixin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityImmortal.class)
@ModDependsMixin("eeeabsmobs")
public interface EntityImmortalAccessor {
    @Accessor(value = "timeUntilBlock", remap = false)
    void setTimeUntilBlock(int t);

    @Accessor(value = "timeUntilBlock", remap = false)
    int timeUntilBlock();
}
