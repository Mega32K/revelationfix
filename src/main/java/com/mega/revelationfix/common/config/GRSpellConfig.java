package com.mega.revelationfix.common.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class GRSpellConfig {
    public static ForgeConfigSpec.ConfigValue<Integer> RevelationCost;
    public static ForgeConfigSpec.ConfigValue<Integer> RevelationCoolDown;
    public static ForgeConfigSpec.ConfigValue<Integer> HereticCost;
    public static ForgeConfigSpec.ConfigValue<Integer> HereticDuration;
    public static ForgeConfigSpec.ConfigValue<Integer> HereticCoolDown;
    public static ForgeConfigSpec.ConfigValue<Integer> HereticSummonDown;
    public static ForgeConfigSpec.ConfigValue<Integer> HereticLimit;
    public static ForgeConfigSpec.ConfigValue<Integer> WitherQuietusCost;
    public static ForgeConfigSpec.ConfigValue<Integer> WitherQuietusDuration;
    public static ForgeConfigSpec.ConfigValue<Integer> WitherQuietusCoolDown;
    public static ForgeConfigSpec.ConfigValue<Integer> IceCost;
    public static ForgeConfigSpec.ConfigValue<Integer> IceChargeUp;
    public static ForgeConfigSpec.ConfigValue<Integer> IceCoolDown;

    public static void init(ForgeConfigSpec.Builder BUILDER) {
        BUILDER.push("Revelation Spell");
        GRSpellConfig.RevelationCost = BUILDER.comment("Revelation Spell Cost, Default: 4500").defineInRange("revelationCost", 4500, 0, Integer.MAX_VALUE);
        GRSpellConfig.RevelationCoolDown = BUILDER.comment("Revelation Spell Cooldown, Default: 120").defineInRange("revelationCoolDown", 120, 0, Integer.MAX_VALUE);
        BUILDER.pop();
        BUILDER.push("Heretic Spell");
        HereticCost = BUILDER.comment("Heretic Spell Cost, Default: 60").defineInRange("hereticCost", 60, 0, Integer.MAX_VALUE);
        HereticDuration = BUILDER.comment("Time to cast Heretic Spell, Default: 120").defineInRange("ghastDuration", 120, 0, 72000);
        HereticCoolDown = BUILDER.comment("Heretic Spell Cooldown, Default: 300").defineInRange("hereticCoolDown", 300, 0, Integer.MAX_VALUE);
        HereticSummonDown = BUILDER.comment("Heretic Spell Summon Down, Default: 300").defineInRange("hereticSummonDown", 300, 0, 72000);
        HereticLimit = BUILDER.comment("Number of Mavericks(or Heretics-1) that can exist around the player, Default: 8").defineInRange("hereticLimit", 8, 1, Integer.MAX_VALUE);
        BUILDER.pop();
        BUILDER.push("Wither Quietus Spell");
        WitherQuietusCost = BUILDER.comment("Wither Quietus Spell Cost, Default: 2500").defineInRange("quietusCost", 2500, 0, Integer.MAX_VALUE);
        WitherQuietusDuration = BUILDER.comment("Time to cast Wither Quietus Spell, Default: 0").defineInRange("quietusTime", 0, 0, 72000);
        WitherQuietusCoolDown = BUILDER.comment("Wither Quietus Spell Cooldown, Default: 140").defineInRange("quietusCoolDown", 140, 0, Integer.MAX_VALUE);
        BUILDER.pop();
        BUILDER.push("Ice Spell");
        IceCost = BUILDER.comment("Ice Spell Cost, Default: 8").defineInRange("iceCost", 100, 0, Integer.MAX_VALUE);
        IceChargeUp = BUILDER.comment("How many ticks the Ice Spell much charge before casting, Default: 20").defineInRange("iceChargeUp", 0, 0, Integer.MAX_VALUE);
        IceCoolDown = BUILDER.comment("Ice Spell Cooldown, Default: 400").defineInRange("iceCoolDown", 200, 0, 72000);
        BUILDER.pop();
    }
}
