package com.mega.revelationfix.common.apollyon.common;

import com.Polarice3.Goety.common.entities.boss.Apostle;
import com.mega.revelationfix.common.config.CommonConfig;
import com.mega.revelationfix.common.config.ModpackCommonConfig;
import com.mega.revelationfix.safe.entity.Apollyon2Interface;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import z1gned.goetyrevelation.util.ApollyonAbilityHelper;

/**
 * Only Apollyon (nether or normal)
 * 头衔索引0:不灭重生
 * 头衔索引1:憎恶本质
 * 头衔索引2:毒蝎之尾
 * 头衔索引12:万众归一
 * 头衔索引13:末日终结
 */
public class AttackDamageChangeHandler {
    public static final float OneDiv14 = 1F / 14F;
    public static int vanillaLimitTime = 30;//in ticks
    /**
     * 动态减伤最大间隔
     */
    public static EntityDataAccessor<Integer> LIMIT_TIME;
    public final Apostle apostle;
    public final ApollyonAbilityHelper helper1;
    public final Apollyon2Interface helper2;
    public int lastHurtTick;

    public AttackDamageChangeHandler(Apostle apostle) {
        this.apostle = apostle;
        this.helper1 = (ApollyonAbilityHelper) apostle;
        this.helper2 = (Apollyon2Interface) apostle;
    }

    /**
     * {@link com.mega.revelationfix.util.EventUtil#damageIncrease(LivingEntity, DamageSource, float)}
     */

    public float damageIncrease(float amount) {
        throw new AssertionError("");
    }

    public float redirectSetHealth(float srcHealth) {
        if (!helper1.allTitlesApostle_1_20_1$isApollyon()) return srcHealth;
        float beforeChangeHealth = helper2.revelaionfix$getApollyonHealth();
        boolean damaged = srcHealth - beforeChangeHealth < 0;
        if (damaged) {
            if (!apostle.isSpellcasting() && apostle.isInNether()) {
                if (isDoomOrGenesis(apostle)) {
                    //下界亚不施法时候无敌只有 万众or末终
                    srcHealth = beforeChangeHealth;
                    helper2.revelaionfix$setHitCooldown(vanillaLimitTime);
                }
            }
        }
        return srcHealth;
    }

    public float redirectActuallyHurtAmount(float amount) {
        //亚波伦常驻75%减伤，施法的时候减伤清零
        if (!apostle.isSpellcasting())
            amount *= CommonConfig.apollyon_permanentDamageReduction;
        if (CommonConfig.apollyon_dynamicDamageReduction) {
            int emptyTime = apostle.tickCount - lastHurtTick;
            if (emptyTime < this.getLimitTime()) {
                amount *= (float) Math.max(emptyTime, 0) / this.getLimitTime();
            }
        }
        this.lastHurtTick = apostle.tickCount;
        return amount;
    }

    public int getLimitTime() {
        return apostle.entityData.get(LIMIT_TIME);
    }

    public void setLimitTime(int time) {
        apostle.entityData.set(LIMIT_TIME, Math.max(time, 0));
    }
    public static boolean isDoomOrGenesis(Apostle apostle) {
        ApollyonAbilityHelper helper = (ApollyonAbilityHelper) apostle;
        if (helper.allTitlesApostle_1_20_1$isApollyon()) {
            return helper.allTitleApostle$getTitleNumber() == 12 || helper.getDoom();
        }
        return false;
    }
}
