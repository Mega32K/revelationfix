package com.mega.revelationfix.mixin;

import com.mega.endinglib.util.annotation.DeprecatedMixin;
import com.mega.endinglib.util.annotation.NoModDependsMixin;
import com.mega.revelationfix.util.entity.EntityRedirectUtils;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(value = LivingEntity.class, priority = 0)
public class LivingDamageMixin {
    /**
     * (可能为null)从调用方法参数获取到的伤害类型<br>(actuallyHurt -> getDamageAfterMagicAbsorb && getDamageAfterArmorAbsorb)
     */
    @Unique
    private @Nullable DamageSource revelationfix$runtimeDamageSource;
    /**
     * (可能为null)从调用方法参数获取到的伤害类型和伤害(在抗性效果之前)<br>(actuallyHurt -> getDamageAfterMagicAbsorb)
     */
    @Unique
    private @Nullable Pair<DamageSource, Float> revelationfix$runtimeCorrectDamageBeforeResistance;

    @Inject(method = "getDamageAfterMagicAbsorb", at = @At("HEAD"))
    private void runtimeDamageSourceGetter0(DamageSource p_21193_, float p_21194_, CallbackInfoReturnable<Float> cir) {
        revelationfix$runtimeDamageSource = p_21193_;
        revelationfix$runtimeCorrectDamageBeforeResistance = Pair.of(p_21193_, p_21194_);
    }

    @Inject(method = "getDamageAfterArmorAbsorb", at = @At("HEAD"))
    private void runtimeDamageSourceGetter1(DamageSource p_21162_, float p_21163_, CallbackInfoReturnable<Float> cir) {
        revelationfix$runtimeDamageSource = p_21162_;
    }
}
