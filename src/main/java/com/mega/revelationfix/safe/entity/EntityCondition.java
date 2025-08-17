package com.mega.revelationfix.safe.entity;

import net.minecraft.world.entity.LivingEntity;

public interface EntityCondition {
    boolean test(LivingEntity entity, LivingEntity iterator);

    default EntityCondition opposite() {
        return ((entity, iterator) -> !this.test(entity, iterator));
    }
}
