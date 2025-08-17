package com.mega.revelationfix.mixin.goety;

import com.Polarice3.Goety.common.entities.projectiles.IceSpike;
import com.mega.revelationfix.common.init.ModParticleTypes;
import com.mega.revelationfix.safe.mixinpart.goety.IceSpikeEC;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(IceSpike.class)
public abstract class IceSpikeMixin extends AbstractArrow implements IceSpikeEC {
    @Unique
    private boolean revelationfix$isFrostPower;

    public IceSpikeMixin(EntityType<? extends AbstractArrow> p_36721_, Level p_36722_) {
        super(p_36721_, p_36722_);
    }

    @Override
    public void revelationfix$setFrostPower(boolean z) {
        this.revelationfix$isFrostPower = z;
    }

    @Override
    public boolean revelationfix$hasFrostFlower() {
        return this.revelationfix$isFrostPower;
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"))
    private void redirectParticle(Level instance, ParticleOptions p_46631_, double d1, double d2, double d3, double d4, double d5, double d6) {
        if (revelationfix$hasFrostFlower())
            instance.addParticle(ModParticleTypes.FROST_FLOWER.get(), d1, d2, d3, d4, d5, d6);
        else instance.addParticle(p_46631_, d1, d2, d3, d4, d5, d6);
    }
}
