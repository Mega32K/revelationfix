package com.mega.revelationfix.util.entity;

import com.mega.revelationfix.common.apollyon.common.ExtraDamageTypes;
import com.mega.revelationfix.common.init.ModAttributes;
import com.mega.revelationfix.common.init.ModEffects;
import com.mega.revelationfix.safe.DamageSourceInterface;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class EntityRedirectUtils {
    /**
     * @param armoredDamage 收到被盔甲减免后的伤害
     * @param living        受伤实体
     * @param srcDamage     源伤害
     * @param damageSource  (可能为null)从调用方法参数获取到的伤害类型
     * @return 伤害
     */
    public static float quietusArmorAbility(float armoredDamage, LivingEntity living, float srcDamage, DamageSource damageSource) {
        if (damageSource != null && damageSource.is(ExtraDamageTypes.FE_POWER) && damageSource.getEntity() != living)
            return Math.max(srcDamage, armoredDamage);
        if (living.hasEffect(ModEffects.QUIETUS.get())) {
            if (armoredDamage < srcDamage) {
                armoredDamage = armoredDamage + (srcDamage - armoredDamage) * (living.getEffect(ModEffects.QUIETUS.get()).getAmplifier() + 1) * 0.1F;
            }
        }
        if (damageSource != null && damageSource.getEntity() instanceof LivingEntity sourceEntity) {
            if (armoredDamage < srcDamage) {
                armoredDamage = armoredDamage + (srcDamage - armoredDamage) * (float) (Mth.clamp(sourceEntity.getAttributeValue(ModAttributes.ARMOR_PENETRATION.get()), 1F, 2F) - 1.0F);
            }
        }
        return armoredDamage;
    }

    /**
     * @param enchantedDamage 收到被盔甲减免后的伤害
     * @param living          受伤实体
     * @param srcDamage       源伤害
     * @param damageSource    (可能为null)从调用方法参数获取到的伤害类型
     * @param pair            (可能为null)从调用方法参数获取到的伤害类型和伤害(在抗性效果之前)(actuallyHurt -> getDamageAfterMagicAbsorb)
     * @return 伤害
     */
    public static float quietusEnchantmentAbility(float enchantedDamage, LivingEntity living, float srcDamage, DamageSource damageSource, Pair<DamageSource, Float> pair) {
        if (damageSource != null && damageSource.is(ExtraDamageTypes.FE_POWER) && damageSource.getEntity() != living) {
            if (pair != null && pair.first().equals(damageSource))
                return Math.max(Math.max(srcDamage, enchantedDamage), pair.right());
            return Math.max(srcDamage, enchantedDamage);
        }
        if (living.hasEffect(ModEffects.QUIETUS.get())) {
            if (pair != null && pair.first().equals(damageSource))
                if (pair.right() > srcDamage)
                    srcDamage = pair.right();
            if (enchantedDamage < srcDamage) {
                enchantedDamage = enchantedDamage + (srcDamage - enchantedDamage) * (living.getEffect(ModEffects.QUIETUS.get()).getAmplifier() + 1) * 0.1F;
            }
        }

        if (damageSource != null) {
            if (damageSource.getEntity() instanceof LivingEntity sourceEntity) {
                if (enchantedDamage < srcDamage) {
                    enchantedDamage = enchantedDamage + (srcDamage - enchantedDamage) * (float) (Mth.clamp(sourceEntity.getAttributeValue(ModAttributes.ENCHANTMENT_PIERCING.get()), 1F, 2F) - 1.0F);
                }
            }
            DamageSourceInterface dsi = (DamageSourceInterface) damageSource;

            if (dsi.hasTag((byte) 5)) {
                if (enchantedDamage < srcDamage) {
                    enchantedDamage = enchantedDamage + (srcDamage - enchantedDamage) * (1 - 0.6F);
                }
            } else if (dsi.hasTag((byte) 4)) {
                if (enchantedDamage < srcDamage) {
                    enchantedDamage = srcDamage;
                }
            }
        }
        return enchantedDamage;
    }

    public static float quietusHealingAbility(LivingEntity living, float srcAmount) {
        if (living.hasEffect(ModEffects.QUIETUS.get())) {
            srcAmount *= (1.0F - (living.getEffect(ModEffects.QUIETUS.get()).getAmplifier() + 1) * 0.1F);
        }
        return srcAmount;
    }
}
