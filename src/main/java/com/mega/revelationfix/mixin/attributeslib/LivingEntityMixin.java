package com.mega.revelationfix.mixin.attributeslib;

import com.mega.endinglib.util.annotation.DeprecatedMixin;
import com.mega.endinglib.util.annotation.ModDependsMixin;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LivingEntity.class)
@ModDependsMixin("attributeslib")
@DeprecatedMixin
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }
}

