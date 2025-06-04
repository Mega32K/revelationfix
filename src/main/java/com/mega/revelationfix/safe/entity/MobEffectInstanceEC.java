package com.mega.revelationfix.safe.entity;

import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

public interface MobEffectInstanceEC {
    void setOwnerEntity(Entity entity);

    @Nullable
    Entity getOwner();
}
