package com.mega.revelationfix.common.config;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec.ConfigValue<Boolean> NEW_BOSS_MUSIC;
    public static final ForgeConfigSpec.ConfigValue<Boolean> NEW_NETHER_APOLLYON_TEXTURE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> NEW_NETHER_APOLLYON_BOSSBAR;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_SCARLET_RAIN;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_TRAIL_RENDERER;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_DYNAMIC_SCARLET_RAIN;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DISPLAY_APOSTLE_SERVANT_BOSSBAR;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_SPECIAL_HALO_EFFECT;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_TIME_FREEZING_GRAY_EFFECT;
    public static final ForgeConfigSpec SPEC;
    public static boolean enableNewBossMusic;
    public static boolean enableNewNetherApollyonTexture;
    public static boolean enableNewNetherApollyonBossbar;
    public static boolean enableScarletRain;
    public static boolean enableTrailRenderer;
    public static boolean enableDynamicScarletRain;
    public static boolean displayApostleServantBossbar;
    public static boolean enableSpecialHaloEffect;
    public static boolean enableTimeFreezingGrayEffect;

    static {
        BUILDER.push("Music");
        NEW_BOSS_MUSIC = BUILDER.comment("Enable new Apollyon boss music (when \"Apollyon\" in dimension Nether)").define("enableNewBossMusic", true);
        BUILDER.pop();
        BUILDER.push("Render");
        NEW_NETHER_APOLLYON_TEXTURE = BUILDER.comment("Enable new Apollyon model texture (when \"Apollyon\" in dimension Nether)").define("enableNewNetherApollyonTexture", true);
        NEW_NETHER_APOLLYON_BOSSBAR = BUILDER.comment("Enable new Apollyon bossbar (when \"Apollyon\" in dimension Nether)").define("enableNewNetherApollyonBossbar", true);
        ENABLE_SCARLET_RAIN = BUILDER.comment("Enable Nether ScarletRain (when \"Apollyon\" in dimension Nether)").define("enableScarletRain", true);
        ENABLE_DYNAMIC_SCARLET_RAIN = BUILDER.comment("Make Nether ScarletRain more beautiful (when \"Apollyon\" in dimension Nether)").define("enableDynamicScarletRain", false);
        ENABLE_TRAIL_RENDERER = BUILDER.comment("Enable TrailRenderers (For example, the trails of the death arrow shot by \"Apollyon\" in dimension Nether) )").define("enableTrailRenderer", true);
        DISPLAY_APOSTLE_SERVANT_BOSSBAR = BUILDER.comment("Display the bossbar of ApostleServant )").define("displayApostleServantBossbar", false);
        ENABLE_SPECIAL_HALO_EFFECT = BUILDER.comment("enable special effect of the halos").define("enableSpecialHaloEffect", true);
        ENABLE_TIME_FREEZING_GRAY_EFFECT = BUILDER.comment("enable gray screen effect when time is frozen").define("enableTimeFreezingGrayEffect", true);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    @SubscribeEvent
    static void onLoad(ModConfigEvent event) {
        update();
    }

    public static void update() {
        enableNewBossMusic = NEW_BOSS_MUSIC.get();
        enableNewNetherApollyonTexture = NEW_NETHER_APOLLYON_TEXTURE.get();
        enableNewNetherApollyonBossbar = NEW_NETHER_APOLLYON_BOSSBAR.get();
        enableScarletRain = ENABLE_SCARLET_RAIN.get();
        enableTrailRenderer = ENABLE_TRAIL_RENDERER.get();
        enableDynamicScarletRain = ENABLE_DYNAMIC_SCARLET_RAIN.get();
        displayApostleServantBossbar = DISPLAY_APOSTLE_SERVANT_BOSSBAR.get();
        enableSpecialHaloEffect = ENABLE_SPECIAL_HALO_EFFECT.get();
        enableTimeFreezingGrayEffect = ENABLE_TIME_FREEZING_GRAY_EFFECT.get();
    }
}
