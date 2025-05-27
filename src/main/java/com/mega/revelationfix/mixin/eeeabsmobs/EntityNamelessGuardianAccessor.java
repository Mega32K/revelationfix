package com.mega.revelationfix.mixin.eeeabsmobs;

import com.eeeab.eeeabsmobs.sever.entity.guling.EntityNamelessGuardian;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityNamelessGuardian.class)
public interface EntityNamelessGuardianAccessor {
    @Accessor(value = "guardianInvulnerableTime", remap = false)
    int guardianInvulnerableTime();

    @Accessor(value = "guardianInvulnerableTime", remap = false)
    void setGuardianInvulnerableTime(int t);
}
