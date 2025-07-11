package com.mega.revelationfix.common.config;

import com.Polarice3.Goety.common.items.magic.MagicFocus;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockConfig {
    public static final ForgeConfigSpec SPEC;
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.ConfigValue<Integer> RUNE_REACTOR_ROOT_CORE_DELAY;
    private static final ForgeConfigSpec.ConfigValue<Integer> RUNE_REACTOR_SOUL_CORE_DELAY;
    private static final ForgeConfigSpec.ConfigValue<Integer> RUNE_REACTOR_TRANSFER_CORE_DELAY;
    private static final ForgeConfigSpec.ConfigValue<Integer> RUNE_REACTOR_ANIMATION_CORE_COST;
    private static final ForgeConfigSpec.ConfigValue<Integer> RUNE_REACTOR_HUNGER_CORE_COST;
    private static final ForgeConfigSpec.ConfigValue<Integer> RUNE_REACTOR_MYSTIC_CORE_COST;
    private static final ForgeConfigSpec.ConfigValue<Integer> RUNE_REACTOR_WIND_CORE_COST;
    private static final ForgeConfigSpec.ConfigValue<Integer> RUNE_REACTOR_ANIMATION_CORE_COST_FOCUS;
    private static final ForgeConfigSpec.ConfigValue<Integer> RUNE_REACTOR_HUNGER_CORE_COST_FOCUS;
    private static final ForgeConfigSpec.ConfigValue<Integer> RUNE_REACTOR_MYSTIC_CORE_COST_FOCUS;
    private static final ForgeConfigSpec.ConfigValue<Integer> RUNE_REACTOR_WIND_CORE_COST_FOCUS;
    private static final ForgeConfigSpec.ConfigValue<Double> SpellingCostMultiplier;
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> RUNE_REACTOR_BLACK_LIST_FOCUS;
    private static final ForgeConfigSpec.ConfigValue<Integer> DARK_ANVIL_BREAK_MAX_ENCHANTMENT_LEVEL;
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
    public static Set<String> blacklistSpellNames = new ReferenceOpenHashSet<>();
    public static int darkAnvilLimitLevel;
    static {
        BUILDER.push("Rune Reactor");
        RUNE_REACTOR_ROOT_CORE_DELAY = BUILDER.worldRestart()
                .comment("The delay from the Totem of Roots for the server-side tick effect of Rune Reactor, Default: 4")
                .defineInRange("rootTotemDelay", 4, 1, 32767);
        RUNE_REACTOR_SOUL_CORE_DELAY = BUILDER.worldRestart()
                .comment("The delay from the Totem of Souls for the server-side tick effect of Rune Reactor, Default: 4")
                .defineInRange("soulTotemDelay", 1, 1, 32767);
        RUNE_REACTOR_TRANSFER_CORE_DELAY = BUILDER.worldRestart()
                .comment("The delay from the Transfer Gem for the server-side tick effect of Rune Reactor, Default: 4")
                .defineInRange("transferCoreDelay", 1, 1, 32767);
        RUNE_REACTOR_ANIMATION_CORE_COST = BUILDER.worldRestart()
                .comment("The core's soul cost per second(normal structure), Default: 2")
                .defineInRange("normalStructure_AnimationCoreCost", 2, 0, 128);
        RUNE_REACTOR_HUNGER_CORE_COST = BUILDER.worldRestart()
                .comment("The core's soul cost per second(normal structure), Default: 2")
                .defineInRange("normalStructure_HungerCoreCost", 4, 0, 128);
        RUNE_REACTOR_MYSTIC_CORE_COST = BUILDER.worldRestart()
                .comment("The core's soul cost per second(normal structure), Default: 2")
                .defineInRange("normalStructure_MysticCoreCost", 2, 0, 128);
        RUNE_REACTOR_WIND_CORE_COST = BUILDER.worldRestart()
                .comment("The core's soul cost per second(normal structure), Default: 2")
                .defineInRange("normalStructure_WindCoreCost", 1, 0, 128);
        RUNE_REACTOR_ANIMATION_CORE_COST_FOCUS = BUILDER.worldRestart()
                .comment("The core's soul cost per second(auto-spelling structure), Default: 2")
                .defineInRange("normalStructure_AnimationCoreCost", 2, 0, 128);
        RUNE_REACTOR_HUNGER_CORE_COST_FOCUS = BUILDER.worldRestart()
                .comment("The core's soul cost per second(auto-spelling structure), Default: 2")
                .defineInRange("normalStructure_HungerCoreCost", 2, 0, 128);
        RUNE_REACTOR_MYSTIC_CORE_COST_FOCUS = BUILDER.worldRestart()
                .comment("The core's soul cost per second(auto-spelling structure), Default: 2")
                .defineInRange("normalStructure_MysticCoreCost", 2, 0, 128);
        RUNE_REACTOR_WIND_CORE_COST_FOCUS = BUILDER.worldRestart()
                .comment("The core's soul cost per second(auto-spelling structure), Default: 2")
                .defineInRange("normalStructure_WindCoreCost", 2, 0, 128);
        SpellingCostMultiplier = BUILDER.worldRestart()
                .comment("The total soul cost of auto-spelling structure, Default: 2")
                .defineInRange("runeReactor_spellingCostMultiplier", 1D, 0D, 128D );
        RUNE_REACTOR_BLACK_LIST_FOCUS = BUILDER.worldRestart().comment("A list of focus items will be banned when Rune Reactor auto-spelling.")
                .defineListAllowEmpty("blacklistSpellNames", List.of(), BlockConfig::validateFocusName);
        BUILDER.pop();
        BUILDER.push("Dark Anvil");
        DARK_ANVIL_BREAK_MAX_ENCHANTMENT_LEVEL = BUILDER.worldRestart()
                .comment("Defined the ability of Dark Anvil to break through the upper limit of enchantment levels for items, default : 4")
                .define("levelLimit", 4);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
    public static boolean validateFocusName(final Object obj) {
        return obj instanceof final String itemName && ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName)) instanceof MagicFocus;
    }
    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        if (SPEC.isLoaded()) {
            runeReactor_rootCoreDelay = RUNE_REACTOR_ROOT_CORE_DELAY.get();
            runeReactor_soulCoreDelay = RUNE_REACTOR_SOUL_CORE_DELAY.get();
            runeReactor_transferCoreDelay = RUNE_REACTOR_TRANSFER_CORE_DELAY.get();
            runeReactor_AnimationCoreCost = RUNE_REACTOR_ANIMATION_CORE_COST.get();
            runeReactor_HungerCoreCost = RUNE_REACTOR_HUNGER_CORE_COST.get();
            runeReactor_MysticCoreCost = RUNE_REACTOR_MYSTIC_CORE_COST.get();
            runeReactor_WindCoreCost = RUNE_REACTOR_WIND_CORE_COST.get();
            runeReactor_AnimationCoreCost_Focus = RUNE_REACTOR_ANIMATION_CORE_COST_FOCUS.get();
            runeReactor_HungerCoreCost_Focus = RUNE_REACTOR_HUNGER_CORE_COST_FOCUS.get();
            runeReactor_MysticCoreCost_Focus = RUNE_REACTOR_MYSTIC_CORE_COST_FOCUS.get();
            runeReactor_WindCoreCost_Focus = RUNE_REACTOR_WIND_CORE_COST_FOCUS.get();
            runeReactor_spellingCostMultiplier = SpellingCostMultiplier.get();
            blacklistSpellNames = RUNE_REACTOR_BLACK_LIST_FOCUS.get().stream().map(itemName -> ((MagicFocus)ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).getSpell().getClass().getName()).collect(Collectors.toSet());
            darkAnvilLimitLevel = DARK_ANVIL_BREAK_MAX_ENCHANTMENT_LEVEL.get();
        }
    }
}
