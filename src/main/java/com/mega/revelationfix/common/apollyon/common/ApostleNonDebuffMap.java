package com.mega.revelationfix.common.apollyon.common;

import com.Polarice3.Goety.common.entities.boss.Apostle;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import z1gned.goetyrevelation.util.ApollyonAbilityHelper;

import java.util.HashMap;

public class ApostleNonDebuffMap extends HashMap<MobEffect, MobEffectInstance> {
    public Apostle apostle;

    public ApostleNonDebuffMap(Apostle apostle) {
        this.apostle = apostle;
    }

    @Override
    public MobEffectInstance put(MobEffect key, MobEffectInstance value) {
        if (((ApollyonAbilityHelper) apostle).allTitlesApostle_1_20_1$isApollyon() && key != null && key.getCategory() == MobEffectCategory.HARMFUL)
            return null;
        return super.put(key, value);
    }
}
