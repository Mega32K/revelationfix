package com.mega.revelationfix.safe.entity;

import com.Polarice3.Goety.common.entities.boss.Apostle;
import com.mega.revelationfix.common.apollyon.common.AttackDamageChangeHandler;
import com.mega.revelationfix.safe.level.ClientLevelExpandedContext;
import com.mega.revelationfix.safe.level.ClientLevelInterface;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import z1gned.goetyrevelation.config.ModConfig;
import z1gned.goetyrevelation.util.ApollyonAbilityHelper;

public class ApollyonExpandedContext {
    public final Apostle apostle;
    public AttackDamageChangeHandler attackDamageReducer;
    public float lastPercent = 1.0F;
    public float percent = 1.0F;
    public LivingEntity lastTarget;
    public ApollyonExpandedContext(Apostle apostle) {
        this.apostle = apostle;
        this.attackDamageReducer = new AttackDamageChangeHandler(apostle);
    }

    public static float getPercent(float health) {
        return health / ModConfig.APOLLYON_HEALTH.get().floatValue() * 182F;
    }

    public void readFromNBT(CompoundTag compoundTag) {
    }

    public void writeToNBT(CompoundTag compoundTag) {
    }

    public void tickHead() {
        Level level = apostle.level();
        if (level.isClientSide)
            this.lastPercent = this.percent;
        Apollyon2Interface a2i = (Apollyon2Interface) apostle;
        if (a2i.revelaionfix$getHitCooldown() > 0) {
            a2i.revelaionfix$setHitCooldown(a2i.revelaionfix$getHitCooldown() - 1);
        }
        if (!level.isClientSide) {
            if (apostle.getTarget() != null && apostle.getTarget().isAlive()) {
                if (!a2i.revelaionfix$illusionMode()) {
                    a2i.revelaionfix$setIllusionMode(true);
                }
            } else {
                if (a2i.revelaionfix$illusionMode()) {
                    a2i.revelaionfix$setIllusionMode(false);
                }
            }
        }
    }

    public void tickTail() {
        if (apostle.level().isClientSide) {
            this.percent = getPercent(apostle.getHealth());
            ClientLevelExpandedContext clientLevelExpandedContext = ((ClientLevelInterface) apostle.level()).revelationfix$ECData();
            Apostle ca = clientLevelExpandedContext.currentNetherApollyon;
            if (ca == null || !(ca.isAlive() && ((ApollyonAbilityHelper) ca).allTitlesApostle_1_20_1$isApollyon() && ca.isSecondPhase())) {
                clientLevelExpandedContext.currentNetherApollyon = apostle;
            }
        }
    }

    public float getBossBarPercent(float partialTicks) {
        return Mth.lerp(partialTicks, lastPercent, percent);
    }
}
