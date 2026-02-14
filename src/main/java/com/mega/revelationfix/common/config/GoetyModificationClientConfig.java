package com.mega.revelationfix.common.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class GoetyModificationClientConfig {
    public static final ForgeConfigSpec SPEC;
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.ConfigValue<Boolean> BRIGHTER_WIND_PARTICLE;
    public static boolean brighterWindParticle = true;
    static {
        BUILDER.comment("You can control the modification of Goety in this config");
        BUILDER.push("Particles");
        BRIGHTER_WIND_PARTICLE = BUILDER.worldRestart()
                .comment("If true, the wind particle will become brighter.")
                .comment("启用后, wind粒子将会更亮.")
                .define("wayfarerBeltCharmExtra", true);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        if (SPEC.isLoaded()) {
            brighterWindParticle = BRIGHTER_WIND_PARTICLE.get();
        }
    }
}
