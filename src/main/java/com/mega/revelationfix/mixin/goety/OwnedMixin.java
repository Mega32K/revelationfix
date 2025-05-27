package com.mega.revelationfix.mixin.goety;

import com.Polarice3.Goety.common.entities.boss.Apostle;
import com.Polarice3.Goety.common.entities.neutral.Owned;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import z1gned.goetyrevelation.util.ApollyonAbilityHelper;

import javax.annotation.Nullable;

@Mixin(Owned.class)
public abstract class OwnedMixin extends PathfinderMob {
    OwnedMixin(EntityType<? extends PathfinderMob> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
    }

    @Shadow(remap = false)
    @Nullable
    public abstract LivingEntity getTrueOwner();

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        if (!level.isClientSide) {
            if (this.getTrueOwner() instanceof Apostle apostle) {
                ApollyonAbilityHelper abilityHelper = (ApollyonAbilityHelper) apostle;
                if (abilityHelper.allTitlesApostle_1_20_1$isApollyon() && this.distanceToSqr(apostle) < 64D * 64D) {
                    int i = abilityHelper.allTitleApostle$getTitleNumber();
                    if (i == 13)
                        this.discard();
                    if (i == 10 || i == 12) {
                        int level = 0;
                        if (apostle.isInNether()) {
                            level = 2;
                        }
                        this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20 * 5, level));
                    }
                    if (i == 4 || i == 12)
                        this.addEffect(new MobEffectInstance(MobEffects.GLOWING, 20 * 5, 0));
                    if (i == 11 || i == 12) {
                        this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20 * 5, 1));
                        this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20 * 5, 1));
                    }
                }
            }
        }
    }
}
