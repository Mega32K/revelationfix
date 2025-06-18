package com.mega.revelationfix.mixin.gr;

import com.Polarice3.Goety.common.entities.projectiles.GroundProjectile;
import com.Polarice3.Goety.common.entities.projectiles.Hellfire;
import com.mega.revelationfix.util.entity.ATAHelper2;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import z1gned.goetyrevelation.config.ModConfig;

@Mixin(Hellfire.class)
public abstract class HellfireMixin extends GroundProjectile {
    @Unique
    LivingEntity allTitlesApostle_1_20_1$target;

    public HellfireMixin(EntityType<? extends Entity> p_i50170_1_, Level p_i50170_2_) {
        super(p_i50170_1_, p_i50170_2_);
    }

    @Shadow(remap = false)
    public abstract int getAnimation();

    @Inject(at = @At("HEAD"), method = "dealDamageTo", remap = false)
    private void getTarget(LivingEntity target, CallbackInfo ci) {
        this.allTitlesApostle_1_20_1$target = target;
    }

    @ModifyVariable(
            at = @At("STORE"),
            method = {"dealDamageTo"},
            remap = false
    )
    private float damageAmount(float value) {
        if (ATAHelper2.hasOdamane(this.getOwner()))
            return this.allTitlesApostle_1_20_1$target.getMaxHealth() * 0.044F;
        return (float) ((double) this.allTitlesApostle_1_20_1$target.getMaxHealth() * ModConfig.HELLFIRE_DAMAGE_AMOUNT.get());
    }
}
