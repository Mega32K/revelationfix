package com.mega.revelationfix.common.enchantment;

import com.Polarice3.Goety.common.entities.boss.Apostle;
import com.Polarice3.Goety.common.entities.boss.EnderKeeper;
import com.Polarice3.Goety.common.entities.boss.Vizier;
import com.eeeab.eeeabsmobs.sever.entity.guling.EntityNamelessGuardian;
import com.eeeab.eeeabsmobs.sever.entity.immortal.EntityImmortal;
import com.mega.revelationfix.common.compat.SafeClass;
import com.mega.revelationfix.mixin.eeeabsmobs.EntityImmortalAccessor;
import com.mega.revelationfix.mixin.eeeabsmobs.EntityNamelessGuardianAccessor;
import com.mega.revelationfix.safe.entity.Apollyon2Interface;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.jetbrains.annotations.NotNull;
import z1gned.goetyrevelation.util.ApollyonAbilityHelper;

public class RealityPiercerEnchantment extends Enchantment {
    public RealityPiercerEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentCategory.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND});
    }

    @Override
    public void doPostHurt(@NotNull LivingEntity beHurt, @NotNull Entity source, int p_44694_) {
    }

    @Override
    public void doPostAttack(@NotNull LivingEntity living, @NotNull Entity beHurt, int p_44694_) {
        int maxTicks = Math.min(20, p_44694_ * 2);
        beHurt.invulnerableTime -= maxTicks;
        if (beHurt.invulnerableTime < 0) beHurt.invulnerableTime = 0;
        if (beHurt instanceof Apostle apostle) {
            apostle.moddedInvul -= maxTicks;
            if (apostle.moddedInvul < 0) apostle.moddedInvul = 0;
        } else if (beHurt instanceof EnderKeeper enderKeeper) {
            enderKeeper.moddedInvul -= maxTicks;
            if (enderKeeper.moddedInvul < 0) enderKeeper.moddedInvul = 0;
        } else if (beHurt instanceof Vizier vizier) {
            vizier.moddedInvul -= maxTicks;
            if (vizier.moddedInvul < 0) vizier.moddedInvul = 0;
        }
        if (beHurt instanceof Apostle apostle && apostle instanceof ApollyonAbilityHelper helper && apostle instanceof Apollyon2Interface apollyon2Interface) {
            if (helper.allTitlesApostle_1_20_1$isApollyon()) {

                {
                    if (helper.allTitlesApostle_1_20_1$getHitCooldown() >= 1) {
                        helper.allTitlesApostle_1_20_1$setHitCooldown(helper.allTitlesApostle_1_20_1$getHitCooldown() - maxTicks);
                        if (helper.allTitlesApostle_1_20_1$getHitCooldown() <= 0)
                            helper.allTitlesApostle_1_20_1$setHitCooldown(1);
                    }
                }

                {
                    if (helper.getApollyonTime() >= 1) {
                        helper.setApollyonTime(helper.getApollyonTime() - maxTicks);
                        if (helper.getApollyonTime() <= 0)
                            helper.setApollyonTime(1);
                    }
                }

                {
                    if (apollyon2Interface.revelaionfix$getHitCooldown() >= 1) {
                        apollyon2Interface.revelaionfix$setHitCooldown(apollyon2Interface.revelaionfix$getHitCooldown() - maxTicks);
                        if (apollyon2Interface.revelaionfix$getHitCooldown() <= 0)
                            apollyon2Interface.revelaionfix$setHitCooldown(1);
                    }
                }

            }
        } else if (SafeClass.isEEEABLoaded()) {
            if (beHurt instanceof EntityImmortal entityImmortal) {
                EntityImmortalAccessor accessor = (EntityImmortalAccessor) entityImmortal;
                accessor.setTimeUntilBlock(accessor.timeUntilBlock() - maxTicks);
                if (accessor.timeUntilBlock() < 0)
                    accessor.setTimeUntilBlock(0);
            } else if (beHurt instanceof EntityNamelessGuardian guardian) {
                EntityNamelessGuardianAccessor accessor = (EntityNamelessGuardianAccessor) guardian;
                accessor.setGuardianInvulnerableTime(accessor.guardianInvulnerableTime() - maxTicks);
                if (accessor.guardianInvulnerableTime() < 0)
                    accessor.setGuardianInvulnerableTime(0);
            }
        }
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }
}
