package com.mega.revelationfix.common.config;

import com.Polarice3.Goety.common.effects.GoetyEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;

public class BrewConfig {
    public static int defaultModified(MobEffect effect) {
        if (effect == MobEffects.DAMAGE_RESISTANCE)
            return 3;
        else if (effect == GoetyEffects.EXPLOSIVE.get())
            return 3;
        else return 127;
    }

    public static int maxAmplier(MobEffect effect) {
        return defaultModified(effect);
    }
}
