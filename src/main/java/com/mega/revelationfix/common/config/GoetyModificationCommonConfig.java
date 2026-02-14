package com.mega.revelationfix.common.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class GoetyModificationCommonConfig {
    public static final ForgeConfigSpec SPEC;
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.ConfigValue<Integer> SUMMON_SPELL_BUFF_LIMIT;
    private static final ForgeConfigSpec.ConfigValue<Boolean> APOSTLE_HELLFIRE_MODIFICATION;
    private static final ForgeConfigSpec.ConfigValue<Boolean> WAYFARERS_BELT_CHARM_EXTRA;
    private static final ForgeConfigSpec.ConfigValue<Boolean> NO_PERSIST;
    public static int summonSpellMaximumBuffLevel = 10;
    public static boolean apostleHellfireModification = true;
    public static boolean wayfarerBeltCharmExtra = true;
    public static boolean noPersist = true;
    static {
        BUILDER.comment("You can control the modification of Goety in this config");
        BUILDER.push("Items");
        WAYFARERS_BELT_CHARM_EXTRA = BUILDER.worldRestart()
                .comment("If true, the traveler's belt can add 1 charm curio slot.")
                .comment("启用后, 旅人腰带可以多加一个护符栏位.")
                .define("wayfarerBeltCharmExtra", true);
        NO_PERSIST = BUILDER.worldRestart()
                .comment("If true, the persist ability will be disabled.")
                .comment("开启后, 禁用goety的损坏能力.")
                .define("noPersist", true);
        BUILDER.pop();
        BUILDER.push("Spells");
        SUMMON_SPELL_BUFF_LIMIT = BUILDER.worldRestart()
                .comment("Define the maximum level that a summoning spell can affect on the summoned entity.(goety original limit is 10)")
                .comment("定义召唤咒语能对召唤出的实体生效的最大等级.(goety原版为10)")
                .defineInRange("summonSpellMaximumBuffLevel", 100000, 0, 2147483647);
        BUILDER.pop();

        BUILDER.push("Entities");

        BUILDER.push("Apostle");
        APOSTLE_HELLFIRE_MODIFICATION = BUILDER.worldRestart()
                .comment("Enable the modification of the damage caused by the Apostle's hellfire.")
                .comment("启用对使徒狱火伤害的修改.")
                .define("apostleHellfireModification", true);
        BUILDER.pop();

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        if (SPEC.isLoaded()) {
            summonSpellMaximumBuffLevel = SUMMON_SPELL_BUFF_LIMIT.get();
            apostleHellfireModification = APOSTLE_HELLFIRE_MODIFICATION.get();
            wayfarerBeltCharmExtra = WAYFARERS_BELT_CHARM_EXTRA.get();
            noPersist = NO_PERSIST.get();
        }
    }
}
