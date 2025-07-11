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
    static final ForgeConfigSpec.ConfigValue<Double> NEEDLE_ATTRIBUTE_INCREASEMENT_MIN;
    static final ForgeConfigSpec.ConfigValue<Double> NEEDLE_ATTRIBUTE_INCREASEMENT_MAX;
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
    public static double needleArmorPenetration;
    public static double needleEnchantmentPiercing;
    public static double needleAVMin;
    public static double needleAVMax;
    public static List<? extends Float> needleAttributeWeight;
    public static double bsDamageBoost;
    public static double bsAttackSpeedBoost;
    public static double bsDodgeBoost;
    public static double bsMaxDodge;
    public static int dwResistance;
    public static int dwDeathEscape;
    public static int ewFreezingTime;
    public static int ewCooldown;
    public static boolean ewCooldownsCanBeReduced;
    public static boolean apocalyptiumChestplateTitle;
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
        BUILDER.push("THe Needle");
        NEEDLE_ARMOR_PENETRATION = BUILDER.worldRestart()
                .comment("The armor penetration attribute value provided by The Needle, default:0.7")
                .defineInRange("needleArmorPenetration", 0.7D, 0D, 1.0D);
        NEEDLE_ENCHANTMENT_PIERCING = BUILDER.worldRestart()
                .comment("The enchantment piercing attribute value provided by The Needle, default:0.5")
                .defineInRange("needleEnchantmentPiercing", 0.5D, 0D, 1.0D);
        NEEDLE_ATTRIBUTE_INCREASEMENT_MIN = BUILDER.worldRestart()
                .comment("The minimum attribute value that the player will increase after using The Needle, default:0.0666")
                .defineInRange("needleMinimumAttributeEnhancement", 0.0666D, 0D, 1.0D);
        NEEDLE_ATTRIBUTE_INCREASEMENT_MAX = BUILDER.worldRestart()
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
        SPEC = BUILDER.build();
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        if (SPEC.isLoaded()) {
            bsDamageBoost = BLESSING_SCROLL_DAMAGE_BOOST.get();
            bsAttackSpeedBoost = BLESSING_SCROLL_ATTACK_SPEED_BOOST.get();
            bsDodgeBoost = BLESSING_SCROLL_DODGE_BOOST.get();
            bsMaxDodge = BLESSING_SCROLL_MAX_DODGE.get();
            needleArmorPenetration = NEEDLE_ARMOR_PENETRATION.get();
            needleEnchantmentPiercing = NEEDLE_ENCHANTMENT_PIERCING.get();
            needleAVMin = NEEDLE_ATTRIBUTE_INCREASEMENT_MIN.get();
            needleAVMax = NEEDLE_ATTRIBUTE_INCREASEMENT_MAX.get();
            needleAttributeWeight = NEEDLE_ATTRIBUTE_WEIGHT.get();
            dwResistance = DIMENSIONAL_WILL_RESISTANCE.get();
            dwDeathEscape = DIMENSIONAL_WILL_DEATH_ESCAPE.get();
            ewFreezingTime = ETERNAL_WATCH_FREEZING_TIME.get();
            ewCooldown = ETERNAL_WATCH_COOLDOWN.get();
            apocalyptiumChestplateTitle = APOCALYPTIUM_CHESTPLATE_TITLE_DISPLAY.get();
            ewCooldownsCanBeReduced = ETERNAL_WATCH_COOLDOWN_CAN_BE_REDUCED.get();
        }
    }
}
