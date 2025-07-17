package com.mega.revelationfix.mixin;

import com.Polarice3.Goety.common.entities.projectiles.DeathArrow;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mega.revelationfix.common.apollyon.common.BypassInvulArrow;
import com.mega.revelationfix.safe.entity.DeathArrowEC;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin extends Projectile {
    @Unique
    private boolean doomArrowDisableParticle = true;

    public AbstractArrowMixin(EntityType<? extends Projectile> p_37248_, Level p_37249_) {
        super(p_37248_, p_37249_);
    }

    @WrapOperation(method = "onHitEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
    private boolean doomDeathArrow(Entity instance, DamageSource p_19946_, float p_19947_, Operation<Boolean> original) {
        if (this.getTags().contains(BypassInvulArrow.TAG_NAME))
            return BypassInvulArrow.doomDeathArrow((AbstractArrow) (Object) this, instance, p_19946_, p_19947_);
        else if (this.getTags().contains(BypassInvulArrow.TAG_BYPASS_NAME))
            return BypassInvulArrow.phase2Arrow((AbstractArrow) (Object) this, instance, p_19946_, p_19947_);
        return original.call(instance, p_19946_, p_19947_);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;isCritArrow()Z"))
    private void tick0(CallbackInfo ci) {
        if ((Object) this instanceof DeathArrow deathArrow)
            if (((DeathArrowEC) deathArrow).revelationfix$getTrailData().shouldRenderTrail())
                doomArrowDisableParticle = false;
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V", ordinal = 0, shift = At.Shift.AFTER))
    private void tick1(CallbackInfo ci) {
        if ((Object) this instanceof DeathArrow deathArrow)
            if (((DeathArrowEC) deathArrow).revelationfix$getTrailData().shouldRenderTrail())
                doomArrowDisableParticle = true;
    }

    @Inject(method = "isCritArrow", at = @At("HEAD"), cancellable = true)
    private void isCritArrow(CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof DeathArrow) {
            if (!doomArrowDisableParticle)
                cir.setReturnValue(false);
        }
    }
}
