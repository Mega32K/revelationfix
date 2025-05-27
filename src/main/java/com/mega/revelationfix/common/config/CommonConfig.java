package com.mega.revelationfix.common.config;

import com.mega.revelationfix.common.init.GRItems;
import net.minecraft.resources.ResourceLocation;
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

    static {
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
        BUILDER.pop();
        SPEC = BUILDER.build();

    }

    public static boolean validateItemName(final Object obj) {
        return obj instanceof final String itemName && ForgeRegistries.ITEMS.containsKey(new ResourceLocation(itemName));
    }

    public static boolean validateETName(final Object obj) {
        return obj instanceof final String itemName && ForgeRegistries.ENTITY_TYPES.containsKey(new ResourceLocation(itemName));
    }

    @SuppressWarnings("ALL")
    public static boolean inWhitelist(Item item) {
        if (item == GRItems.HALO_OF_THE_END || item == GRItems.ATONEMENT_VOUCHER_ITEM) return true;
        if (item.builtInRegistryHolder().is(GRItems.NO_STEALING)) return true;
        return whitelistItems.contains(item);
    }

    public static boolean inWhitelist(EntityType<?> entityType) {
        return whitelistEntities.contains(entityType);
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        if (SPEC.isLoaded()) {
            whitelistItems = WHITE_LIST_ITEM_STRINGS.get().stream().map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).collect(Collectors.toSet());
            whitelistEntities = WHITE_LIST_ENTITIES.get().stream().map(itemName -> ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(itemName))).collect(Collectors.toSet());
            barrierKillingMobsHealingMode = BARRIER_KILLING_MOBS_HEALING_MODE.get();
            haloUndeadSuppression = HALO_UNDEAD_FRIENDLY.get();
            haloSoulReduction = HALO_SOUL_REDUCTION.get();
            haloSpellCastingSpeed = HALO_SPELL_CASTING_SPEED.get();
            haloDamageCap = HALO_DAMAGE_CAP.get();
            haloSpellCooldownReduction = HALO_SPEL_COOLDOWN_REDUCTION.get();
            apollyonBarrierPreparation = APOLLYON_BARRIER_PREPARATION.get();
            apollyonShootingCooldown = APOLLYON_SHOOTING_COOLDOWN.get();
        }
    }
}
