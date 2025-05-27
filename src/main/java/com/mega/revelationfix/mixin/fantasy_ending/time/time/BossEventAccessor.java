package com.mega.revelationfix.mixin.fantasy_ending.time.time;

import net.minecraft.world.BossEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.UUID;

@Mixin(BossEvent.class)
public interface BossEventAccessor {
    @Mutable
    @Accessor("id")
    void setUUID(UUID id);
}
