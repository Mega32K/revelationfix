package com.mega.revelationfix.mixin;

import com.mega.revelationfix.common.effect.QuietusEffect;
import com.mega.tetraclip.util.NoModDependsMixin;
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
@NoModDependsMixin("attributeslib")
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

    @Redirect(method = "getDamageAfterArmorAbsorb", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/CombatRules;getDamageAfterAbsorb(FFF)F"))
    private float getDamageAfterArmorAbsorb(float p_19273_, float p_19274_, float p_19275_) {
        return QuietusEffect.quietusArmorAbility((LivingEntity) (Object) this, p_19273_, CombatRules.getDamageAfterAbsorb(p_19273_, p_19274_, p_19275_), revelationfix$runtimeDamageSource);
    }

    @Redirect(method = "getDamageAfterMagicAbsorb", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/CombatRules;getDamageAfterMagicAbsorb(FF)F"))
    private float getDamageAfterProtection(float p_19270_, float p_19271_) {
        return QuietusEffect.quietusEnchantmentAbility((LivingEntity) (Object) this, p_19270_, CombatRules.getDamageAfterMagicAbsorb(p_19270_, p_19271_), revelationfix$runtimeDamageSource, revelationfix$runtimeCorrectDamageBeforeResistance);
    }

    @ModifyVariable(method = "heal", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private float healAmount(float value) {
        return QuietusEffect.quietusHealingAbility((LivingEntity) (Object) this, value);
    }
}
