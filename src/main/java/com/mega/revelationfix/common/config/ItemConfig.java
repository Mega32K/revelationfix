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
    private static final ForgeConfigSpec.ConfigValue<Double> blessingScrollDamageBoost;
    private static final ForgeConfigSpec.ConfigValue<Double> blessingScrollAttackSpeedBoost;
    private static final ForgeConfigSpec.ConfigValue<Double> blessingScrollDodgeBoost;
    private static final ForgeConfigSpec.ConfigValue<Double> blessingScrollMaxDodge;
    private static final ForgeConfigSpec.ConfigValue<Integer> dimensionalWillResistance;
    private static final ForgeConfigSpec.ConfigValue<Integer> dimensionalWillDeathEscape;
    private static final ForgeConfigSpec.ConfigValue<Integer> eternalWatchFreezingTime;
    private static final ForgeConfigSpec.ConfigValue<Integer> eternalWatchCooldown;
    private static final ForgeConfigSpec.ConfigValue<Double> eternalWatchFinalAttackPercentage;
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
    public static double ewFinalAttackPercentage;

    static {
        BUILDER.push("Blessing Scroll");
        blessingScrollDamageBoost = BUILDER.worldRestart().
                comment("Damage increase provided by Scroll of a Blessing for each lucky point, as percentage.").
                defineInRange("DamageBoost", 4D, 0D, 32768D);
        blessingScrollAttackSpeedBoost = BUILDER.worldRestart().
                comment("AttackSpeed increase provided by Scroll of a Blessing for each lucky point, as percentage.").
                defineInRange("AttackSpeedBoost", 6D, 0D, 32768D);
        blessingScrollDodgeBoost = BUILDER.worldRestart().
                comment("Dodge increase provided by Scroll of a Blessing for each lucky point, as percentage.").
                defineInRange("DodgeBoost", 2D, 0D, 100D);
        blessingScrollMaxDodge = BUILDER.worldRestart().
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
        dimensionalWillResistance = BUILDER.worldRestart()
                .comment("The resistance of damage provided by Dimensional Will, as percentage.")
                .defineInRange("DamageResistance", 70, 0, 100);
        dimensionalWillDeathEscape = BUILDER.worldRestart()
                .comment("The chance of escaping from death provided by Dimensional Will, as percentage.")
                .defineInRange("DeathEscape", 33, 0, 100);
        BUILDER.pop();
        BUILDER.push("Eternal Watch");
        eternalWatchFreezingTime = BUILDER.worldRestart()
                .comment("The time(in seconds) that The Eternal Watch can freeze.")
                .defineInRange("FreezingTime", 9, 0, 32768);
        eternalWatchCooldown = BUILDER.worldRestart()
                .comment("The cooldown of Eternal Watch after frozen the time.")
                .defineInRange("Cooldown", 70, 0, 32768);
        eternalWatchFinalAttackPercentage = BUILDER.worldRestart()
                .comment("The percentage of the max health of the target who was hurt by players who has the Eternal Watch")
                .defineInRange("Percentage", 0.1D, 0D, 1D);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        if (SPEC.isLoaded()) {
            bsDamageBoost = blessingScrollDamageBoost.get();
            bsAttackSpeedBoost = blessingScrollAttackSpeedBoost.get();
            bsDodgeBoost = blessingScrollDodgeBoost.get();
            bsMaxDodge = blessingScrollMaxDodge.get();
            needleArmorPenetration = NEEDLE_ARMOR_PENETRATION.get();
            needleEnchantmentPiercing = NEEDLE_ENCHANTMENT_PIERCING.get();
            needleAVMin = NEEDLE_ATTRIBUTE_INCREASEMENT_MIN.get();
            needleAVMax = NEEDLE_ATTRIBUTE_INCREASEMENT_MAX.get();
            needleAttributeWeight = NEEDLE_ATTRIBUTE_WEIGHT.get();
            dwResistance = dimensionalWillResistance.get();
            dwDeathEscape = dimensionalWillDeathEscape.get();
            ewFreezingTime = eternalWatchFreezingTime.get();
            ewCooldown = eternalWatchCooldown.get();
            ewFinalAttackPercentage = eternalWatchFinalAttackPercentage.get();
        }
    }
}
