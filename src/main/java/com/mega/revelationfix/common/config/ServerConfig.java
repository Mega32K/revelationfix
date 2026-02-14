package com.mega.revelationfix.common.config;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ServerConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    static final ForgeConfigSpec.ConfigValue<Double> TETRA_MAX_FADING_EFFECT_DECREASE;
    public static final ForgeConfigSpec SPEC;
    public static double tetra_maxFadingEffectDecrease;

    static {
        BUILDER.push("Compat");
        BUILDER.push("Tetra");
        TETRA_MAX_FADING_EFFECT_DECREASE = BUILDER
                .defineInRange("MaxFadingEffectDecrease", 0.99999, 0, 1.0);
        BUILDER.pop();
        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    @SubscribeEvent
    static void onLoad(ModConfigEvent event) {
        update(event);
    }

    public static void update(ModConfigEvent event) {
        if (SPEC.isLoaded()) {
            tetra_maxFadingEffectDecrease = TETRA_MAX_FADING_EFFECT_DECREASE.get();
        }
    }
}
