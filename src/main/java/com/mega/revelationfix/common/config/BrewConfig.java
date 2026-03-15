package com.mega.revelationfix.common.config;

import com.Polarice3.Goety.common.effects.GoetyEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.common.ForgeConfigSpec;

/**
 * 这里的配置选项将生成在Goety本体药酿配置处
 */
public class BrewConfig {
    public static ForgeConfigSpec.ConfigValue<Integer> Level6Capacity;
    public static ForgeConfigSpec.ConfigValue<Integer> Level7Capacity;
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
    public static void init(ForgeConfigSpec.Builder BUILDER) {
        Level6Capacity = BUILDER.comment("How much Capacity is given for sixth level, Default: 8").defineInRange("level6Capacity", 8, 0, Integer.MAX_VALUE);
        Level7Capacity = BUILDER.comment("How much Capacity is given for seventh level, Default: 8").defineInRange("level7Capacity", 8, 0, Integer.MAX_VALUE);

    }
}
