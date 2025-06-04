package com.mega.revelationfix.mixin.goety;

import com.Polarice3.Goety.common.effects.GoetyEffects;
import com.Polarice3.Goety.common.entities.projectiles.IceSpear;
import com.mega.revelationfix.safe.mixinpart.goety.IceSpikeEC;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(IceSpear.class)
public abstract class IceSpearMixin implements IceSpikeEC {
    @Inject(method = "onHitEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;addEffect(Lnet/minecraft/world/effect/MobEffectInstance;)Z"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void onHitEntity(EntityHitResult p_37626_, CallbackInfo ci, float baseDamage, Entity entity, Entity entity1, boolean flag, LivingEntity livingEntity) {
        if (revelationfix$hasFrostFlower()) {
            MobEffect busted = GoetyEffects.BUSTED.get();
            MobEffect sapped = GoetyEffects.SAPPED.get();
            if (!livingEntity.hasEffect(busted))
                livingEntity.addEffect(new MobEffectInstance(busted, 120, 0));
            else if (livingEntity.getEffect(busted).getAmplifier() <= 1)
                livingEntity.addEffect(new MobEffectInstance(busted, 120, 1));

            if (!livingEntity.hasEffect(sapped))
                livingEntity.addEffect(new MobEffectInstance(sapped, 120, 0));
            else {
                int amplifier = livingEntity.getEffect(sapped).getAmplifier();
                if (amplifier <= 4)
                    livingEntity.addEffect(new MobEffectInstance(sapped, 120, Math.min(4, amplifier+1)));
            }
        }
    }
}
