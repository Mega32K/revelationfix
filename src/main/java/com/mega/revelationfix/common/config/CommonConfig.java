package com.mega.revelationfix.common.config;

import com.mega.revelationfix.common.apollyon.common.AttackDamageChangeHandler;
import com.mega.revelationfix.common.compat.ToBeInject;
import com.mega.revelationfix.common.init.GRItems;
import com.mega.revelationfix.common.init.ModEntities;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonConfig {
    public static final ForgeConfigSpec SPEC;
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> WHITE_LIST_ITEM_STRINGS;
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> WHITE_LIST_ENTITIES;
    private static final ForgeConfigSpec.ConfigValue<Boolean> BARRIER_KILLING_MOBS_HEALING_MODE;
    private static final ForgeConfigSpec.ConfigValue<Boolean> HALO_UNDEAD_FRIENDLY;
    private static final ForgeConfigSpec.ConfigValue<Double> HALO_SOUL_REDUCTION;
    private static final ForgeConfigSpec.ConfigValue<Double> HALO_SPELL_CASTING_SPEED;
    private static final ForgeConfigSpec.ConfigValue<Double> HALO_SPEL_COOLDOWN_REDUCTION;
    private static final ForgeConfigSpec.ConfigValue<Double> HALO_DAMAGE_CAP;
    private static final ForgeConfigSpec.ConfigValue<Integer> APOLLYON_BARRIER_PREPARATION;
    private static final ForgeConfigSpec.ConfigValue<Integer> APOLLYON_SHOOTING_COOLDOWN;
    private static final ForgeConfigSpec.ConfigValue<Boolean> TARGET_REDIRECT_TO_SERVANTS;
    private static final ForgeConfigSpec.ConfigValue<Double> APOLLYON_PERMANENT_DAMAGE_REDUCTION;
    private static final ForgeConfigSpec.ConfigValue<Boolean> APOLLYON_DYNAMIC_DAMAGE_REDUCTION;
    private static final ForgeConfigSpec.ConfigValue<Integer> APOLLYON_CUSTOM_INVULNERABLE_TIME;
    private static final ForgeConfigSpec.ConfigValue<Double> NETHER_APOLLYON_PHASE2_DAMAGE_MULTIPLIER;
    private static final ForgeConfigSpec.ConfigValue<Double> NETHER_APOLLYON_PHASE_GENESIS_DAMAGE_MULTIPLIER;
    private static final ForgeConfigSpec.ConfigValue<Integer> APOLLYON_ARMOR_PIERCING_PERCENT;
    private static final ForgeConfigSpec.ConfigValue<Integer> APOLLYON_ENCHANTMENT_PIERCING_PERCENT;
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> BYPASSES_LIST_MOB_EFFECTS;
    public static Set<Item> whitelistItems;
    public static Set<EntityType<?>> whitelistEntities;
    public static boolean barrierKillingMobsHealingMode;
    //true 全亡灵友好
    //false 仅巫妖
    public static boolean haloUndeadSuppression;
    public static double haloSoulReduction;
    public static double haloSpellCastingSpeed;
    public static double haloSpellCooldownReduction;
    public static double haloDamageCap;
    public static int apollyonBarrierPreparation = 3;
    public static int apollyonShootingCooldown = 1600;
    public static boolean redirectTargetToServantOption;
    public static float apollyon_permanentDamageReduction;
    public static boolean apollyon_dynamicDamageReduction;
    public static float apollyon_nether_phase2DamageMultiplier;
    public static float apollyon_nether_phaseGenesisDamageMultiplier;
    public static float apollyon_armorPiercing;
    public static float apollyon_enchantmentPiercing;
    public static Set<MobEffect> bypassMobEffects;
    static {
        BUILDER.push("Others");
        BYPASSES_LIST_MOB_EFFECTS = BUILDER.worldRestart().comment("A list of mob effect that would never be cleared by curio abilities/bosses...")
                .defineListAllowEmpty("whitelistMobEffects", List.of(
                        "minecraft:night_vision",
                        "cataclysm:ghost_sickness",
                        "aquamirae:crystallization"
                ), CommonConfig::validateMobEffectName);
        BUILDER.pop();
        BUILDER.push("The Apocalypse");
        WHITE_LIST_ITEM_STRINGS = BUILDER.worldRestart().comment("A list of items(curios!) won't be banned when The Nether Apollyon in the phase \"The Apocalypse\".")
                .defineListAllowEmpty("whitelistItems", List.of(
                        "sophisticatedbackpacks:backpack",
                        "sophisticatedbackpacks:iron_backpack",
                        "sophisticatedbackpacks:gold_backpack",
                        "sophisticatedbackpacks:diamond_backpack",
                        "sophisticatedbackpacks:netherite_backpack",
                        "enigmaticlegacy:cursed_ring"
                ), CommonConfig::validateItemName);
        WHITE_LIST_ENTITIES = BUILDER.worldRestart().comment("A list of entity types(non LivingEntity) won't be removed when The Nether Apollyon in the phase \"The Apocalypse\".")
                .defineListAllowEmpty("whitelistEntities", List.of(
                        "revelationfix:fake_item_entity",
                        "minecraft:item",
                        "minecraft:experience_orb",
                        "enigmaticlegacy:permanent_item_entity",
                        "corpse:corpse"
                ), CommonConfig::validateETName);
        BARRIER_KILLING_MOBS_HEALING_MODE = BUILDER.worldRestart()
                .comment("If configured as true, when a creature is killed by Apollon's barrier, Apollon will return to the state of \"The Apocalypse\".")
                .define("barrierKillingMobsHealingMode", true);
        APOLLYON_BARRIER_PREPARATION = BUILDER.worldRestart()
                .comment("The time required for the barrier to fully open, default 3 (seconds)")
                .defineInRange("apollyonBarrierPreparation", 3, 3, 10);
        BUILDER.pop();
        BUILDER.push("Halo of Ascension");
        HALO_UNDEAD_FRIENDLY = BUILDER.worldRestart()
                .comment("All undead creatures dare not fight back against Halo Players.\n" +
                        "If it is set to false, only undead creatures with less than  GoetyMod Config:lichPowerfulFoesHealth(default 50 hearts)  of health dare not fight back against Halo Players.")
                .define("haloUndeadSuppression", true);
        HALO_SOUL_REDUCTION = BUILDER.worldRestart()
                .comment("How many times of soul energy reduction can Halo provide,Default: 4")
                .defineInRange("haloSoulReduction", 4D, 1D, 64D);
        HALO_SPELL_CASTING_SPEED = BUILDER.worldRestart()
                .comment("How many times faster can Halo players cast spells,Default: 4")
                .defineInRange("haloSpellCastingSpeed", 4D, 1D, 64D);
        HALO_SPEL_COOLDOWN_REDUCTION = BUILDER.worldRestart()
                .comment("How many times faster can Halo players accelerate the cooldowns of focus,Default: 2")
                .defineInRange("haloSpellCooldownReduction", 2D, 1D, 64D);
        HALO_DAMAGE_CAP = BUILDER.worldRestart()
                .comment("The maximum amount of damage a halo player can attain per hit, Default: 20.0")
                .defineInRange("haloDamageCap", 20.0D, 0D, Double.MAX_VALUE);
        BUILDER.pop();
        BUILDER.push("Apollyon");
        APOLLYON_SHOOTING_COOLDOWN = BUILDER.worldRestart()
                .comment("Define the cooldown of apollyon shooting skill(in ticks).default : 1600")
                .defineInRange("shootingCooldown", 1600, 0, 32767);
        APOLLYON_PERMANENT_DAMAGE_REDUCTION = BUILDER.worldRestart()
                .comment("Defined the permanent damage reduction value of the Apollyon.default : 25(percent)")
                .defineInRange("permanentDamageReduction", 25D, 0D, 100D);
        APOLLYON_DYNAMIC_DAMAGE_REDUCTION = BUILDER.worldRestart()
                .comment("If configured as true, will enable dynamic damage reduction ability for Apollyon.")
                .define("dynamicDamageReduction", true);
        APOLLYON_CUSTOM_INVULNERABLE_TIME = BUILDER.worldRestart()
                .comment("Defined the custom invulnerable time of apollyon (in ticks).default : 30")
                .defineInRange("invulnerableTime", 30, 10, 32767);
        NETHER_APOLLYON_PHASE2_DAMAGE_MULTIPLIER = BUILDER.worldRestart()
                .comment("Defined the damage multiplier of the Apollyon in second phase.default : 2.0")
                .defineInRange("netherSecondPhaseDamageMultiplier", 2.0, 0D, 100D);
        NETHER_APOLLYON_PHASE_GENESIS_DAMAGE_MULTIPLIER = BUILDER.worldRestart()
                .comment("Defined the damage multiplier of the Apollyon in second phase.default : 4.0")
                .defineInRange("netherPhaseGenesisDamageMultiplier", 4.0, 0D, 100D);
        APOLLYON_ARMOR_PIERCING_PERCENT= BUILDER.worldRestart()
                .comment("Defined the armor piercing attribute base value of apollyon.default : 30(percent)")
                .defineInRange("armorPiercingAttribute", 30, 0, 100);
        APOLLYON_ENCHANTMENT_PIERCING_PERCENT= BUILDER.worldRestart()
                .comment("Defined the enchantment piercing attribute base value of apollyon.default : 30(percent)")
                .defineInRange("enchantmentPiercingAttribute", 30, 0, 100);
        BUILDER.pop();
        BUILDER.push("Servants");
        TARGET_REDIRECT_TO_SERVANTS = BUILDER.worldRestart()
                .comment("If true, your servant will redirect the target of hostile creatures towards you to themselves.")
                .define("redirectTargetToServantOption", false);
        BUILDER.pop();
        SPEC = BUILDER.build();

    }

    public static boolean validateItemName(final Object obj) {
        return obj instanceof final String itemName && ForgeRegistries.ITEMS.containsKey(new ResourceLocation(itemName));
    }

    public static boolean validateETName(final Object obj) {
        return obj instanceof final String itemName && ForgeRegistries.ENTITY_TYPES.containsKey(new ResourceLocation(itemName));
    }
    public static boolean validateMobEffectName(final Object obj) {
        return obj instanceof final String itemName && ForgeRegistries.MOB_EFFECTS.containsKey(new ResourceLocation(itemName));
    }
    @SuppressWarnings("ALL")
    public static boolean inWhitelist(Item item) {
        if (item == GRItems.HALO_OF_THE_END || item == GRItems.ATONEMENT_VOUCHER_ITEM) return true;
        else if (item.builtInRegistryHolder().is(GRItems.NO_STEALING)) return true;
        else if (ToBeInject.inWhitelist(item)) return true;
        return whitelistItems.contains(item);
    }

    public static boolean inWhitelist(Entity entity) {
        if (ToBeInject.inWhitelist(entity)) return true;
        else if (entity.getType().is(ModEntities.PREVENT_DISCARD_BY_APOLLYON)) return true;
        return whitelistEntities.contains(entity.getType());
    }
    public static boolean inBypassEffect(MobEffect mobEffect) {
        return bypassMobEffects.contains(mobEffect);
    }
    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        if (SPEC.isLoaded()) {
            whitelistItems = new ObjectArraySet<>(WHITE_LIST_ITEM_STRINGS.get().stream().map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).collect(Collectors.toSet()));
            whitelistEntities = new ObjectArraySet<>(WHITE_LIST_ENTITIES.get().stream().map(itemName -> ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(itemName))).collect(Collectors.toSet()));
            barrierKillingMobsHealingMode = BARRIER_KILLING_MOBS_HEALING_MODE.get();
            haloUndeadSuppression = HALO_UNDEAD_FRIENDLY.get();
            haloSoulReduction = HALO_SOUL_REDUCTION.get();
            haloSpellCastingSpeed = HALO_SPELL_CASTING_SPEED.get();
            haloDamageCap = HALO_DAMAGE_CAP.get();
            haloSpellCooldownReduction = HALO_SPEL_COOLDOWN_REDUCTION.get();
            apollyonBarrierPreparation = APOLLYON_BARRIER_PREPARATION.get();
            apollyonShootingCooldown = APOLLYON_SHOOTING_COOLDOWN.get();
            redirectTargetToServantOption = TARGET_REDIRECT_TO_SERVANTS.get();
            apollyon_permanentDamageReduction = APOLLYON_PERMANENT_DAMAGE_REDUCTION.get().floatValue();
            apollyon_dynamicDamageReduction = APOLLYON_DYNAMIC_DAMAGE_REDUCTION.get();
            AttackDamageChangeHandler.vanillaLimitTime = APOLLYON_CUSTOM_INVULNERABLE_TIME.get();
            apollyon_nether_phase2DamageMultiplier = NETHER_APOLLYON_PHASE2_DAMAGE_MULTIPLIER.get().floatValue();
            apollyon_nether_phaseGenesisDamageMultiplier = NETHER_APOLLYON_PHASE_GENESIS_DAMAGE_MULTIPLIER.get().floatValue();
            bypassMobEffects = new ObjectArraySet<>(BYPASSES_LIST_MOB_EFFECTS.get().stream().map(itemName -> ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(itemName))).collect(Collectors.toSet()));
            //直接base值
            apollyon_armorPiercing = APOLLYON_ARMOR_PIERCING_PERCENT.get() / 100.0F + 1F;
            //直接base值
            apollyon_enchantmentPiercing = APOLLYON_ENCHANTMENT_PIERCING_PERCENT.get() / 100.0F + 1F;
        }
    }
}
