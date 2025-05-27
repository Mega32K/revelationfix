package com.mega.revelationfix.mixin;

import net.minecraft.world.entity.monster.PatrollingMonster;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PatrollingMonster.class)
public interface PatrollingMonsterAccessor {
    @Accessor("patrolling")
    boolean patrolling();
}
