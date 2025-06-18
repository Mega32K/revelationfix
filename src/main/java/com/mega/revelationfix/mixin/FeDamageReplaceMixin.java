package com.mega.revelationfix.mixin;

import com.mega.revelationfix.common.apollyon.common.ExtraDamageTypes;
import com.mega.revelationfix.common.odamane.common.FeDamage;
import com.mega.revelationfix.util.entity.ATAHelper2;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = LivingEntity.class, priority = 114514)
public abstract class FeDamageReplaceMixin extends Entity {
    FeDamageReplaceMixin(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @ModifyVariable(method = "hurt", at = @At("HEAD"), index = 1, argsOnly = true)
    public DamageSource hurtMix(DamageSource source) {
        Entity entity = source.getEntity();
        if (entity instanceof Player player && ATAHelper2.hasOdamane(player) && !source.is(ExtraDamageTypes.FE_POWER)) {
            return FeDamage.get((LivingEntity) (Object) this, player);
        }
        return source;
    }
}
