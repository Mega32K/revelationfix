package com.mega.revelationfix.common.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockConfig {
    public static final ForgeConfigSpec SPEC;
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.ConfigValue<Integer> RuneReactorRootCoreDelay;
    private static final ForgeConfigSpec.ConfigValue<Integer> RuneReactorSoulCoreDelay;
    private static final ForgeConfigSpec.ConfigValue<Integer> RuneReactorTransferCoreDelay;
    private static final ForgeConfigSpec.ConfigValue<Integer> RuneReactorAnimationCoreCost;
    private static final ForgeConfigSpec.ConfigValue<Integer> RuneReactorHungerCoreCost;
    private static final ForgeConfigSpec.ConfigValue<Integer> RuneReactorMysticCoreCost;
    private static final ForgeConfigSpec.ConfigValue<Integer> RuneReactorWindCoreCost;
    private static final ForgeConfigSpec.ConfigValue<Integer> RuneReactorAnimationCoreCost_Focus;
    private static final ForgeConfigSpec.ConfigValue<Integer> RuneReactorHungerCoreCost_Focus;
    private static final ForgeConfigSpec.ConfigValue<Integer> RuneReactorMysticCoreCost_Focus;
    private static final ForgeConfigSpec.ConfigValue<Integer> RuneReactorWindCoreCost_Focus;
    private static final ForgeConfigSpec.ConfigValue<Double> SpellingCostMultiplier;
    public static int runeReactor_rootCoreDelay;
    public static int runeReactor_soulCoreDelay;
    public static int runeReactor_transferCoreDelay;
    public static int runeReactor_AnimationCoreCost;
    public static int runeReactor_HungerCoreCost;
    public static int runeReactor_MysticCoreCost;
    public static int runeReactor_WindCoreCost;
    public static int runeReactor_AnimationCoreCost_Focus;
    public static int runeReactor_HungerCoreCost_Focus;
    public static int runeReactor_MysticCoreCost_Focus;
    public static int runeReactor_WindCoreCost_Focus;
    public static double runeReactor_spellingCostMultiplier = 1D;
    static {
        BUILDER.push("Rune Reactor");
        RuneReactorRootCoreDelay = BUILDER.worldRestart()
                .comment("The delay from the Totem of Roots for the server-side tick effect of Rune Reactor, Default: 4")
                .defineInRange("rootCoreDelay", 4, 1, 32767);
        RuneReactorSoulCoreDelay = BUILDER.worldRestart()
                .comment("The delay from the Totem of Souls for the server-side tick effect of Rune Reactor, Default: 4")
                .defineInRange("soulCoreDelay", 2, 1, 32767);
        RuneReactorTransferCoreDelay = BUILDER.worldRestart()
                .comment("The delay from the Transfer Gem for the server-side tick effect of Rune Reactor, Default: 4")
                .defineInRange("transferCoreDelay", 1, 1, 32767);
        RuneReactorAnimationCoreCost = BUILDER.worldRestart()
                .comment("The core's soul cost per second(normal structure), Default: 2")
                .defineInRange("normalStructure_AnimationCoreCost", 2, 0, 128);
        RuneReactorHungerCoreCost = BUILDER.worldRestart()
                .comment("The core's soul cost per second(normal structure), Default: 2")
                .defineInRange("normalStructure_HungerCoreCost", 4, 0, 128);
        RuneReactorMysticCoreCost = BUILDER.worldRestart()
                .comment("The core's soul cost per second(normal structure), Default: 2")
                .defineInRange("normalStructure_MysticCoreCost", 2, 0, 128);
        RuneReactorWindCoreCost = BUILDER.worldRestart()
                .comment("The core's soul cost per second(normal structure), Default: 2")
                .defineInRange("normalStructure_WindCoreCost", 1, 0, 128);
        RuneReactorAnimationCoreCost_Focus = BUILDER.worldRestart()
                .comment("The core's soul cost per second(auto-spelling structure), Default: 2")
                .defineInRange("normalStructure_AnimationCoreCost", 2, 0, 128);
        RuneReactorHungerCoreCost_Focus = BUILDER.worldRestart()
                .comment("The core's soul cost per second(auto-spelling structure), Default: 2")
                .defineInRange("normalStructure_HungerCoreCost", 2, 0, 128);
        RuneReactorMysticCoreCost_Focus = BUILDER.worldRestart()
                .comment("The core's soul cost per second(auto-spelling structure), Default: 2")
                .defineInRange("normalStructure_MysticCoreCost", 2, 0, 128);
        RuneReactorWindCoreCost_Focus = BUILDER.worldRestart()
                .comment("The core's soul cost per second(auto-spelling structure), Default: 2")
                .defineInRange("normalStructure_WindCoreCost", 2, 0, 128);
        SpellingCostMultiplier = BUILDER.worldRestart()
                .comment("The total soul cost of auto-spelling structure, Default: 2")
                .defineInRange("runeReactor_spellingCostMultiplier", 1D, 0D, 128D );
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        if (SPEC.isLoaded()) {
            runeReactor_rootCoreDelay = RuneReactorRootCoreDelay.get();
            runeReactor_soulCoreDelay = RuneReactorSoulCoreDelay.get();
            runeReactor_transferCoreDelay = RuneReactorTransferCoreDelay.get();
            runeReactor_AnimationCoreCost = RuneReactorAnimationCoreCost.get();
            runeReactor_HungerCoreCost = RuneReactorHungerCoreCost.get();
            runeReactor_MysticCoreCost = RuneReactorMysticCoreCost.get();
            runeReactor_WindCoreCost = RuneReactorWindCoreCost.get();
            runeReactor_AnimationCoreCost_Focus = RuneReactorAnimationCoreCost_Focus.get();
            runeReactor_HungerCoreCost_Focus = RuneReactorHungerCoreCost_Focus.get();
            runeReactor_MysticCoreCost_Focus = RuneReactorMysticCoreCost_Focus.get();
            runeReactor_WindCoreCost_Focus = RuneReactorWindCoreCost_Focus.get();
            runeReactor_spellingCostMultiplier = SpellingCostMultiplier.get();
        }
    }
}
