package com.mega.revelationfix.common.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

/**
 * 全部都是modpack模式开启才用
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModpackCommonConfig {
    public static final ForgeConfigSpec SPEC;
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.ConfigValue<Boolean> APOLLYON_MODPACK_MODE;
    private static final ForgeConfigSpec.ConfigValue<Double> APOLLYON_SELF_HEALING;
    private static final ForgeConfigSpec.ConfigValue<Double> APOLLYON_ARROW_HEALING;
    private static final ForgeConfigSpec.ConfigValue<Double> APOLLYON_SECOND_PHASE_ARROW_HEALING;
    private static final ForgeConfigSpec.ConfigValue<Double> NETHER_THE_DOOM_PERCENT;
    public static boolean apollyonModpackMode;
    public static double apollyonSelfHealing;
    public static double apollyonArrowHealing;
    public static double apollyonSecondPhaseArrowHealing;
    public static double netherTheDoomPercent;

    static {
        BUILDER.push("Healing");
        APOLLYON_MODPACK_MODE = BUILDER.worldRestart()
                .comment("If true then Apollyon 's healing system will use config data.")
                .define("apollyonModpackMode", false);
        APOLLYON_SELF_HEALING = BUILDER.worldRestart()
                .comment("The natural healing rate of Apollon (for its max health)")
                .defineInRange("apollyonSelfHealing", 0.002D, 0D, 1D);
        APOLLYON_ARROW_HEALING = BUILDER.worldRestart()
                .comment("The arrow-hit healing rate of Apollon (for its max health)")
                .defineInRange("apollyonArrowHealing", 0.005D, 0D, 1D);
        APOLLYON_SECOND_PHASE_ARROW_HEALING = BUILDER.worldRestart()
                .comment("The arrow-hit healing rate of Apollon when in Nether dimension (for its max health)")
                .defineInRange("apollyonSecondPhaseArrowHealing", 0.01D, 0D, 1D);
        BUILDER.pop();
        BUILDER.push("The Apocalypse");
        NETHER_THE_DOOM_PERCENT = BUILDER.worldRestart()
                .comment("Describe the percent of Apollyon's maximum health during \"The Apocalypse\"")
                .defineInRange("netherTheDoomPercent", 1D / 14D, 0D, 1D);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        if (SPEC.isLoaded()) {
            apollyonModpackMode = APOLLYON_MODPACK_MODE.get();
            apollyonSelfHealing = APOLLYON_SELF_HEALING.get();
            apollyonArrowHealing = APOLLYON_ARROW_HEALING.get();
            apollyonSecondPhaseArrowHealing = APOLLYON_SECOND_PHASE_ARROW_HEALING.get();
            netherTheDoomPercent = NETHER_THE_DOOM_PERCENT.get();
        }
    }
}
