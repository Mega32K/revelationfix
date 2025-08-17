package com.mega.revelationfix.common.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ItemConfig {
    public static final ForgeConfigSpec SPEC;
    static final ForgeConfigSpec.ConfigValue<Double> NEEDLE_ARMOR_PENETRATION;
    static final ForgeConfigSpec.ConfigValue<Double> NEEDLE_ENCHANTMENT_PIERCING;
    static final ForgeConfigSpec.ConfigValue<Double> NEEDLE_ATTRIBUTE_INCREASE_MIN;
    static final ForgeConfigSpec.ConfigValue<Double> NEEDLE_ATTRIBUTE_INCREASE_MAX;
    static final ForgeConfigSpec.ConfigValue<List<? extends Float>> NEEDLE_ATTRIBUTE_WEIGHT;
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.ConfigValue<Double> BLESSING_SCROLL_DAMAGE_BOOST;
    private static final ForgeConfigSpec.ConfigValue<Double> BLESSING_SCROLL_ATTACK_SPEED_BOOST;
    private static final ForgeConfigSpec.ConfigValue<Double> BLESSING_SCROLL_DODGE_BOOST;
    private static final ForgeConfigSpec.ConfigValue<Double> BLESSING_SCROLL_MAX_DODGE;
    private static final ForgeConfigSpec.ConfigValue<Integer> DIMENSIONAL_WILL_RESISTANCE;
    private static final ForgeConfigSpec.ConfigValue<Integer> DIMENSIONAL_WILL_DEATH_ESCAPE;
    private static final ForgeConfigSpec.ConfigValue<Integer> ETERNAL_WATCH_FREEZING_TIME;
    private static final ForgeConfigSpec.ConfigValue<Integer> ETERNAL_WATCH_COOLDOWN;
    private static final ForgeConfigSpec.ConfigValue<Boolean> ETERNAL_WATCH_COOLDOWN_CAN_BE_REDUCED;
    private static final ForgeConfigSpec.ConfigValue<Boolean> APOCALYPTIUM_CHESTPLATE_TITLE_DISPLAY;
    private static final ForgeConfigSpec.ConfigValue<Double> AMULET_OF_SLIME_COOLDOWN;
    private static final ForgeConfigSpec.ConfigValue<Double> GOLD_FEATHER_SPEED_MULTIPLIER;
    private static final ForgeConfigSpec.ConfigValue<Double> SOUL_OF_OBSIDIAN_ATTRIBUTE_0;
    private static final ForgeConfigSpec.ConfigValue<Double> SOUL_OF_OBSIDIAN_ATTRIBUTE_1;
    private static final ForgeConfigSpec.ConfigValue<Double> SOUL_OF_OBSIDIAN_PERCENT_INCREASE;
    private static final ForgeConfigSpec.ConfigValue<Integer> EERIE_AXE_MAX_DEPTH_OF_DIGGING;
    public static double needleArmorPenetration;
    public static double needleEnchantmentPiercing;
    public static double needleAVMin;
    public static double needleAVMax;
    public static List<? extends Float> needleAttributeWeight;
    public static double blessingScrollDamageBoost;
    public static double blessingScrollAttackSpeedBoost;
    public static double blessingScrollDodgeBoost;
    public static double blessingScrollMaxDodge;
    public static int dimensionalWillResistance;
    public static int dimensionalWillDeathEscape;
    public static int eternalWatchFreezingTime;
    public static int eternalWatchCooldown;
    public static boolean eternalWatchCooldownsCanBeReduced;
    public static boolean apocalyptiumChestplateTitle;
    public static float amuletOfSlimeCooldown;
    public static float goldFeatherSpeedMultiplier;
    public static float soulOfObsidianSoulDecreaseEfficiency;
    public static float soulOfObsidianSoulStealing;
    public static float soulOfObsidianSpecialLootIncrease;
    public static int eerieAxeMaxDepth;

    static {
        BUILDER.push("Blessing Scroll");
        BLESSING_SCROLL_DAMAGE_BOOST = BUILDER.worldRestart().
                comment("Damage increase provided by Scroll of a Blessing for each lucky point, as percentage.").
                defineInRange("DamageBoost", 4D, 0D, 32768D);
        BLESSING_SCROLL_ATTACK_SPEED_BOOST = BUILDER.worldRestart().
                comment("AttackSpeed increase provided by Scroll of a Blessing for each lucky point, as percentage.").
                defineInRange("AttackSpeedBoost", 6D, 0D, 32768D);
        BLESSING_SCROLL_DODGE_BOOST = BUILDER.worldRestart().
                comment("Dodge increase provided by Scroll of a Blessing for each lucky point, as percentage.").
                defineInRange("DodgeBoost", 2D, 0D, 100D);
        BLESSING_SCROLL_MAX_DODGE = BUILDER.worldRestart().
                comment("Max Dodge provided by Scroll of a Blessing, as percentage.").
                defineInRange("MaxDodge", 85D, 0D, 100D);
        BUILDER.pop();
        BUILDER.push("The Needle");
        NEEDLE_ARMOR_PENETRATION = BUILDER.worldRestart()
                .comment("The armor penetration attribute value provided by The Needle, default:0.7")
                .defineInRange("needleArmorPenetration", 0.7D, 0D, 1.0D);
        NEEDLE_ENCHANTMENT_PIERCING = BUILDER.worldRestart()
                .comment("The enchantment piercing attribute value provided by The Needle, default:0.5")
                .defineInRange("needleEnchantmentPiercing", 0.5D, 0D, 1.0D);
        NEEDLE_ATTRIBUTE_INCREASE_MIN = BUILDER.worldRestart()
                .comment("The minimum attribute value that the player will increase after using The Needle, default:0.0666")
                .defineInRange("needleMinimumAttributeEnhancement", 0.0666D, 0D, 1.0D);
        NEEDLE_ATTRIBUTE_INCREASE_MAX = BUILDER.worldRestart()
                .comment("The maximum attribute value that the player will increase after using The Needle, default:0.0666")
                .defineInRange("needleMaximumAttributeEnhancement", 0.176D, 0D, 1.0D);
        NEEDLE_ATTRIBUTE_WEIGHT = BUILDER.worldRestart()
                .comment("The preciousness weight of the permanently augmented attributes provided by The Needle(default AttackDamage:0.12, MaxHealth:0.12, AttackSpeed:0.4, Resistance:3.5")
                .defineListAllowEmpty("needleAttributeWeight", List.of(0.12F, 0.12F, 0.4F, 3.5F), (i) -> i instanceof Float f && f >= 0F);
        BUILDER.pop();
        BUILDER.push("Dimensional Will");
        DIMENSIONAL_WILL_RESISTANCE = BUILDER.worldRestart()
                .comment("The resistance of damage provided by Dimensional Will, as percentage.")
                .defineInRange("DamageResistance", 70, 0, 100);
        DIMENSIONAL_WILL_DEATH_ESCAPE = BUILDER.worldRestart()
                .comment("The chance of escaping from death provided by Dimensional Will, as percentage.")
                .defineInRange("DeathEscape", 33, 0, 100);
        BUILDER.pop();
        BUILDER.push("Eternal Watch");
        ETERNAL_WATCH_FREEZING_TIME = BUILDER.worldRestart()
                .comment("The time(in seconds) that The Eternal Watch can freeze.")
                .defineInRange("FreezingTime", 9, 0, 32768);
        ETERNAL_WATCH_COOLDOWN = BUILDER.worldRestart()
                .comment("The cooldown of Eternal Watch after frozen the time.")
                .defineInRange("Cooldown", 70, 0, 32768);
        ETERNAL_WATCH_COOLDOWN_CAN_BE_REDUCED = BUILDER.worldRestart()
                .comment("if true, the cooldowns of the eternal watch can can be reduced by other items")
                .define("canCooldownsBeReduced", false);
        BUILDER.pop();
        BUILDER.push("Apocalyptium Armor");
        BUILDER.push("Chestplate");
        APOCALYPTIUM_CHESTPLATE_TITLE_DISPLAY = BUILDER.worldRestart()
                .comment("display player's apostle title when equipped the apocalyptium chestplate")
                .define("displayTitle", false);
        BUILDER.pop();
        BUILDER.pop();
        BUILDER.push("Amulet of Slime");
        AMULET_OF_SLIME_COOLDOWN = BUILDER.worldRestart()
                .comment("The skill cooldown of the amulet of slime, default 2.5(in seconds).")
                .defineInRange("cooldown", 2.5D, 0D, 32D);
        BUILDER.pop();
        BUILDER.push("Gold Feather");
        GOLD_FEATHER_SPEED_MULTIPLIER = BUILDER.worldRestart()
                .comment("Speed multiplier when using items (relative to original state), default 4.0.")
                .defineInRange("speedMultiplier", 4D, 1D, 32D);
        BUILDER.pop();
        BUILDER.push("Soul of Obsidian");
        SOUL_OF_OBSIDIAN_ATTRIBUTE_0 = BUILDER.worldRestart()
                .comment("The added value of Soul Stealing Attribute, default 5(in percent).")
                .defineInRange("soulStealing", 10D, 0D, 32767D);
        SOUL_OF_OBSIDIAN_ATTRIBUTE_1 = BUILDER.worldRestart()
                .comment("The added value of Soul Decrease Efficiency Attribute, default 5(in percent).")
                .defineInRange("soulDecreaseEfficiency", 5D, 0D, 32767D);
        SOUL_OF_OBSIDIAN_PERCENT_INCREASE = BUILDER.worldRestart()
                .comment("Defined the increase in probability of special death loot from some creatures for this charm, default 25(in percent).")
                .defineInRange("specialLootPercentIncrease", 25D, 0D, 100D);
        BUILDER.pop();
        BUILDER.push("Eerie Axe");
        EERIE_AXE_MAX_DEPTH_OF_DIGGING = BUILDER.worldRestart()
                .comment("Defined the maximum depth to which this tool can collect logs at once, default 32.")
                .defineInRange("maxDepth", 32, 0, 128);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        if (SPEC.isLoaded()) {
            blessingScrollDamageBoost = BLESSING_SCROLL_DAMAGE_BOOST.get();
            blessingScrollAttackSpeedBoost = BLESSING_SCROLL_ATTACK_SPEED_BOOST.get();
            blessingScrollDodgeBoost = BLESSING_SCROLL_DODGE_BOOST.get();
            blessingScrollMaxDodge = BLESSING_SCROLL_MAX_DODGE.get();
            needleArmorPenetration = NEEDLE_ARMOR_PENETRATION.get();
            needleEnchantmentPiercing = NEEDLE_ENCHANTMENT_PIERCING.get();
            needleAVMin = NEEDLE_ATTRIBUTE_INCREASE_MIN.get();
            needleAVMax = NEEDLE_ATTRIBUTE_INCREASE_MAX.get();
            needleAttributeWeight = NEEDLE_ATTRIBUTE_WEIGHT.get();
            dimensionalWillResistance = DIMENSIONAL_WILL_RESISTANCE.get();
            dimensionalWillDeathEscape = DIMENSIONAL_WILL_DEATH_ESCAPE.get();
            eternalWatchFreezingTime = ETERNAL_WATCH_FREEZING_TIME.get();
            eternalWatchCooldown = ETERNAL_WATCH_COOLDOWN.get();
            apocalyptiumChestplateTitle = APOCALYPTIUM_CHESTPLATE_TITLE_DISPLAY.get();
            eternalWatchCooldownsCanBeReduced = ETERNAL_WATCH_COOLDOWN_CAN_BE_REDUCED.get();
            amuletOfSlimeCooldown = AMULET_OF_SLIME_COOLDOWN.get().floatValue();
            goldFeatherSpeedMultiplier = GOLD_FEATHER_SPEED_MULTIPLIER.get().floatValue();
            soulOfObsidianSoulStealing = SOUL_OF_OBSIDIAN_ATTRIBUTE_0.get().floatValue();
            soulOfObsidianSoulDecreaseEfficiency = SOUL_OF_OBSIDIAN_ATTRIBUTE_1.get().floatValue();
            soulOfObsidianSpecialLootIncrease = SOUL_OF_OBSIDIAN_PERCENT_INCREASE.get().floatValue();
            eerieAxeMaxDepth = EERIE_AXE_MAX_DEPTH_OF_DIGGING.get();
        }
    }
}
