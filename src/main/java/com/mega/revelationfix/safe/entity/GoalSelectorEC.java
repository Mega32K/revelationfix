package com.mega.revelationfix.safe.entity;

import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public interface GoalSelectorEC {
    @Nullable LivingEntity revelationfix$getEntity();

    void revelationfix$setEntity(LivingEntity entity);
}
