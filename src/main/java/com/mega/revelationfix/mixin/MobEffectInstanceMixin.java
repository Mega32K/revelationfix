package com.mega.revelationfix.mixin;

import com.mega.revelationfix.safe.MobEffectInstanceEC;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;

@Mixin(MobEffectInstance.class)
public class MobEffectInstanceMixin implements MobEffectInstanceEC {
    @Unique
    private @Nullable Entity owner;

    @Override
    public void setOwnerEntity(Entity entity) {
        owner = entity;
    }

    @Override
    public @Nullable Entity getOwner() {
        return owner;
    }
}
