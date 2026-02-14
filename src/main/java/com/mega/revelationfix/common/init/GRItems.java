package com.mega.revelationfix.common.init;

import com.Polarice3.Goety.common.effects.GoetyEffects;
import com.Polarice3.Goety.common.items.ModSpawnEggItem;
import com.Polarice3.Goety.common.items.ModSpawnEggs;
import com.Polarice3.Goety.common.items.magic.MagicFocus;
import com.Polarice3.Goety.init.ModCreativeTab;
import com.mega.endinglib.api.item.NameCenteredItem;
import com.mega.revelationfix.Revelationfix;
import com.mega.revelationfix.common.apollyon.common.RevelationRarity;
import com.mega.revelationfix.common.compat.SafeClass;
import com.mega.revelationfix.common.item.AllRightsServantEgg;
import com.mega.revelationfix.common.item.armor.*;
import com.mega.revelationfix.common.item.curios.*;
import com.mega.revelationfix.common.item.curios.enigmtic_legacy.EnigmaticLegacyItemInit;
import com.mega.revelationfix.common.item.curios.robe.ApollyonRobeItem;
import com.mega.revelationfix.common.item.disc.ArchIllagerDisc;
import com.mega.revelationfix.common.item.disc.ArchIllagerRemixDisc;
import com.mega.revelationfix.common.item.disc.DecisiveMomentDisc;
import com.mega.revelationfix.common.item.food.AscensionHardCandy;
import com.mega.revelationfix.common.item.other.*;
import com.mega.revelationfix.common.item.research.EdenResearchScrollItem;
import com.mega.revelationfix.common.item.research.GodForgingResearchScrollItem;
import com.mega.revelationfix.common.item.template.ApocalyptiumTemplateItem;
import com.mega.revelationfix.common.item.tool.combat.bow.BowOfRevelationItem;
import com.mega.revelationfix.common.item.tool.combat.sword.EnderKeeperSword;
import com.mega.revelationfix.common.item.tool.combat.sword.ValetteinItem;
import com.mega.revelationfix.common.item.tool.combat.trident.GungnirItem;
import com.mega.revelationfix.common.item.tool.combat.whip.SpectreWhipItem;
import com.mega.revelationfix.common.item.tool.combat.whip.VenomousSpiderWhipItem;
import com.mega.revelationfix.common.item.tool.eerie.EerieAxeItem;
import com.mega.revelationfix.common.item.tool.wand.FrostbloomStaff;
import com.mega.revelationfix.common.spell.EmptySpell;
import com.mega.revelationfix.common.spell.frost.IceSpell;
import com.mega.revelationfix.common.spell.nether.HereticSpell;
import com.mega.revelationfix.common.spell.nether.RevelationSpell;
import com.mega.revelationfix.common.spell.nether.WitherQuietusSpell;
import com.mega.revelationfix.util.java.Self;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.registries.RegistryObject;
import z1gned.goetyrevelation.ModMain;
import z1gned.goetyrevelation.item.ModItems;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class GRItems {
    public static final List<RegistryObject<Item>> CREATIVE_TAB_ITEMS = Collections.synchronizedList(new ObjectArrayList<>());
    @ObjectHolder(value = "goety_revelation:atonement_voucher", registryName = "item")
    public static final Item ATONEMENT_VOUCHER_ITEM = null;
    @ObjectHolder(value = "goety_revelation:curios_storage_crystal", registryName = "item")
    public static final Item CURIOS_STORAGE_CRYSTAL_ITEM = null;
    @ObjectHolder(value = "goety_revelation:halo_of_the_end", registryName = "item")
    public static final Item HALO_OF_THE_END = null;
    @ObjectHolder(value = "goety_revelation:random_display", registryName = "item")
    public static final Item RANDOM_DISPLAY_ITEM = null;
    @ObjectHolder(value = "goety_revelation:puzzle_display", registryName = "item")
    public static final Item PUZZLE_DISPLAY_ITEM = null;
    @ObjectHolder(value = "goety_revelation:bow_of_revelation", registryName = "item")
    public static final Item BOW_OF_REVELATION_ITEM = null;
    public static final String NBT_PUZZLES = "isTERitualPuzzle1";
    public static final String NBT_PUZZLES2 = "isTERitualPuzzle2";
    public static final String NBT_PUZZLES3 = "isTERitualPuzzle3";
    public static final String NBT_PUZZLES4 = "isTERitualPuzzle4";
    public static final String NBT_CRAFTING = "isTERitualCraft";
    //32者中被随机到的谜题之物
    public static final TagKey<Item> THE_END_PUZZLES = ItemTags.create(new ResourceLocation(Revelationfix.MODID, "te_puzzles"));
    public static final TagKey<Item> THE_END_PUZZLES2 = ItemTags.create(new ResourceLocation(Revelationfix.MODID, "te_puzzles2"));
    public static final TagKey<Item> THE_END_PUZZLES3 = ItemTags.create(new ResourceLocation(Revelationfix.MODID, "te_puzzles3"));
    public static final TagKey<Item> THE_END_PUZZLES4 = ItemTags.create(new ResourceLocation(Revelationfix.MODID, "te_puzzles4"));
    //谜题之物聚合物
    public static final TagKey<Item> THE_END_CRAFTING = ItemTags.create(new ResourceLocation(Revelationfix.MODID, "te_craft"));
    //谜底合成的神秘碎片tag
    public static final TagKey<Item> MYSTERY_0 = ItemTags.create(new ResourceLocation(Revelationfix.MODID, "mystery_0"));
    public static final TagKey<Item> MYSTERY_1 = ItemTags.create(new ResourceLocation(Revelationfix.MODID, "mystery_1"));
    public static final TagKey<Item> MYSTERY_2 = ItemTags.create(new ResourceLocation(Revelationfix.MODID, "mystery_2"));
    public static final TagKey<Item> MYSTERY_3 = ItemTags.create(new ResourceLocation(Revelationfix.MODID, "mystery_3"));
    //消失诅咒附魔书
    public static final TagKey<Item> VANISHING_CB = ItemTags.create(new ResourceLocation(Revelationfix.MODID, "vanishing_enchantment_book"));
    //不偷的饰品标签
    public static final TagKey<Item> NO_STEALING = ItemTags.create(new ResourceLocation(Revelationfix.MODID, "no_stealing"));
    //鞭子标签
    public static final TagKey<Item> WHIPS = ItemTags.create(new ResourceLocation("forge:tools/whips"));
    //蜘蛛类稀有物品标签
    public static final TagKey<Item> SPIDERS = ItemTags.create(new ResourceLocation(ModMain.MODID, "spiders"));
    //2.1
    @ObjectHolder(value = "goety_revelation:apocalyptium_helmet", registryName = "item")
    public static final Item A_HELMET = null;
    @ObjectHolder(value = "goety_revelation:apocalyptium_chestplate", registryName = "item")
    public static final Item A_CHESTPLATE = null;
    @ObjectHolder(value = "goety_revelation:apocalyptium_leggings", registryName = "item")
    public static final Item A_LEGGINGS = null;
    @ObjectHolder(value = "goety_revelation:apocalyptium_boots", registryName = "item")
    public static final Item A_BOOTS = null;
    @ObjectHolder(value = "goety_revelation:apocalyptium_ingot", registryName = "item")
    public static final Item APOCALYPTIUM_INGOT_ITEM = null;
    @ObjectHolder(value = "goety_revelation:the_tip_of_the_longinus", registryName = "item")
    public static final Item TIP_OF_THE_SPEAR_OF_LONGINUS_ITEM = null;
    public static Map<RegistryObject<Item>, Supplier<List<ItemStack>>> insertAfterTabMap = new HashMap<>();
    public static RegistryObject<Item> GOD_FORGING_SCROLL;
    public static RegistryObject<Item> ASCENSION_HARD_CANDY;
    public static RegistryObject<Item> VALETTEIN;
    public static RegistryObject<Item> GUNGNIR;
    public static RegistryObject<Item> BOW_OF_REVELATION;
    public static RegistryObject<Item> BROKEN_ENDER_KEEPER_SWORD;
    public static RegistryObject<Item> ENDER_KEEPER_SWORD;
    public static RegistryObject<Item> THE_NEEDLE;
    public static RegistryObject<Item> DIMENSIONAL_WILL;
    public static RegistryObject<Item> ETERNAL_WATCH;
    public static RegistryObject<Item> APOLLYON_ROBE;
    public static RegistryObject<Item> SOUL_OF_OBSIDIAN;
    public static RegistryObject<Item> AMULET_OF_SLIME;
    public static RegistryObject<Item> GOLD_FEATHER;
    public static RegistryObject<Item> APOCALYPTIUM_INGOT;
    public static RegistryObject<Item> APOCALYPTIUM_TEMPLATE_ITEM;
    public static RegistryObject<Item> APOCALYPTIUM_HELMET;
    public static RegistryObject<Item> APOCALYPTIUM_CHESTPLATE;
    public static RegistryObject<Item> APOCALYPTIUM_LEGGINGS;
    public static RegistryObject<Item> APOCALYPTIUM_BOOTS;
    public static RegistryObject<Item> PUZZLE_ITEM;
    @Self
    public static RegistryObject<Item> MYSTERY_FRAGMENT;
    public static RegistryObject<Item> ATONEMENT_VOUCHER;
    public static RegistryObject<Item> CURIOS_STORAGE_CRYSTAL;
    public static RegistryObject<Item> QUIETUS_STAR;
    @Self
    public static RegistryObject<Item> ODAMANE_HALO;
    @Self
    public static RegistryObject<Item> APOSTLE_SERVANT_SPAWN_EGG;
    @Self
    public static RegistryObject<Item> HERETIC_SERVANT_SPAWN_EGG;
    @Self
    public static RegistryObject<Item> MAVERICK_SERVANT_SPAWN_EGG;
    public static RegistryObject<Item> SPIDER_FANG;
    public static RegistryObject<Item> HAUNTED_WHIP_HILT;
    public static RegistryObject<Item> VENOMOUS_SPIDER_WHIP;
    public static RegistryObject<Item> SPIDER_HELMET;
    public static RegistryObject<Item> SPIDER_CHESTPLATE;
    public static RegistryObject<Item> SPIDER_LEGGINGS;
    public static RegistryObject<Item> SPIDER_BOOTS;
    public static RegistryObject<Item> SPECTRE_WHIP;
    public static RegistryObject<Item> SPECTRE_HELMET;
    public static RegistryObject<Item> SPECTRE_CHESTPLATE;
    public static RegistryObject<Item> SPECTRE_LEGGINGS;
    public static RegistryObject<Item> SPECTRE_BOOTS;

    public static RegistryObject<Item> EERIE_AXE;
    @Self
    public static RegistryObject<Item> SPIDER_DARKMAGE_HELMET;
    @Self
    public static RegistryObject<Item> SPIDER_DARKMAGE_CHESTPLATE;
    @Self
    public static RegistryObject<Item> SPIDER_DARKMAGE_LEGGINGS;
    @Self
    public static RegistryObject<Item> SPIDER_DARKMAGE_BOOTS;
    @Self
    public static RegistryObject<Item> SPECTRE_DARKMAGE_HELMET;
    @Self
    public static RegistryObject<Item> SPECTRE_DARKMAGE_CHESTPLATE;
    @Self
    public static RegistryObject<Item> SPECTRE_DARKMAGE_LEGGINGS;
    @Self
    public static RegistryObject<Item> SPECTRE_DARKMAGE_BOOTS;
    public static RegistryObject<Item> REVELATION_FOCUS;
    public static RegistryObject<Item> WITHER_QUIETUS_FOCUS;
    public static RegistryObject<Item> HERETIC_FOCUS;
    public static RegistryObject<Item> DEMENTOR_FOCUS;
    public static RegistryObject<Item> DRAGON_NIRVANA_FOCUS;
    public static RegistryObject<Item> DRAGON_SKYROCKET_FOCUS;
    public static RegistryObject<Item> ICE_FOCUS;
    public static RegistryObject<Item> SHADOW_CLONE_FOCUS;
    public static RegistryObject<Item> NETHER_METEOR_FOCUS;
    public static RegistryObject<Item> STAFF_FROSTBLOOM;
    @Self
    public static RegistryObject<Item> RANDOM_DISPLAY;
    @Self
    public static RegistryObject<Item> PUZZLE_DISPLAY;
    @Self
    public static RegistryObject<Item> TIP_OF_THE_SPEAR_OF_LONGINUS;
    @Self
    public static RegistryObject<Item> TIP_OF_THE_SPEAR_OF_AEGLOS;
    @Self
    public static RegistryObject<Item> DISC_1;
    @Self
    public static RegistryObject<Item> DISC_2;
    @Self
    public static RegistryObject<Item> DISC_3;
    public static RegistryObject<BlockItem> BI_RUNE_REACTOR;
    public static RegistryObject<BlockItem> BI_RUNESTONE_ENGRAVED_TABLE;
    @Self
    public static RegistryObject<Item> EDEN_SCROLL;
    public static RegistryObject<ModSpawnEggItem> ICE_HERMIT_SPAWN_EGG;

    public static void init(DeferredRegister<Item> ITEMS) {
        ODAMANE_HALO = ITEMS.register("halo_of_the_end", OdamaneHalo::new);
        QUIETUS_STAR = ITEMS.register("quietus_star", () -> new Item(new Item.Properties().rarity(Rarity.EPIC).fireResistant()));
        REVELATION_FOCUS = ITEMS.register("revelation_focus", () -> new MagicFocus(new RevelationSpell()));
        HERETIC_FOCUS = ITEMS.register("heretic_focus", () -> new MagicFocus(new HereticSpell()));
        WITHER_QUIETUS_FOCUS = ITEMS.register("wither_quietus_focus", () -> new MagicFocus(new WitherQuietusSpell()));
        DEMENTOR_FOCUS = ITEMS.register("dementor_focus", () -> new MagicFocus(new EmptySpell()));
        DRAGON_NIRVANA_FOCUS = ITEMS.register("dragon_nirvana_focus", () -> new MagicFocus(new EmptySpell()));
        DRAGON_SKYROCKET_FOCUS = ITEMS.register("dragon_skyrocket_focus", () -> new MagicFocus(new EmptySpell()));
        ICE_FOCUS = ITEMS.register("ice_focus", () -> new MagicFocus(new IceSpell()));
        SHADOW_CLONE_FOCUS = ITEMS.register("shadow_clone_focus", () -> new MagicFocus(new EmptySpell()));
        NETHER_METEOR_FOCUS = ITEMS.register("nether_meteor_focus", () -> new MagicFocus(new EmptySpell()));
        RANDOM_DISPLAY = ITEMS.register("random_display", () -> new RandomDisplayItem(new Item.Properties().stacksTo(1).fireResistant()));
        PUZZLE_DISPLAY = ITEMS.register("puzzle_display", () -> new PuzzleDisplayItem(new Item.Properties().stacksTo(1).fireResistant()));
        PUZZLE_ITEM = ITEMS.register("puzzle_item", () -> new PuzzleItem(new Item.Properties().stacksTo(1).fireResistant().rarity(Rarity.EPIC)));
        ATONEMENT_VOUCHER = ITEMS.register("atonement_voucher", () -> new AtonementVoucher(new Item.Properties().stacksTo(16).fireResistant().rarity(Rarity.UNCOMMON)));
        CURIOS_STORAGE_CRYSTAL = ITEMS.register("curios_storage_crystal", () -> new CuriosStorageCrystal(new Item.Properties().stacksTo(1).fireResistant().rarity(Rarity.EPIC)));
        APOSTLE_SERVANT_SPAWN_EGG = ITEMS.register("apostle_servant_spawn_egg", () -> new AllRightsServantEgg(ModEntities.APOSTLE_SERVANT, 526344, 16112170, new Item.Properties()));
        HERETIC_SERVANT_SPAWN_EGG = ITEMS.register("heretic_servant_spawn_egg", () -> new AllRightsServantEgg(ModEntities.HERETIC_SERVANT, 7210067, 16750336, new Item.Properties()));
        MAVERICK_SERVANT_SPAWN_EGG = ITEMS.register("maverick_servant_spawn_egg", () -> new AllRightsServantEgg(ModEntities.MAVERICK_SERVANT, 3685164, 3482663, new Item.Properties()));
        ASCENSION_HARD_CANDY = ITEMS.register("ascension_hard_candy", AscensionHardCandy::new);
        BOW_OF_REVELATION = ITEMS.register("bow_of_revelation", () -> new BowOfRevelationItem(new Item.Properties().durability(9001).fireResistant().rarity(Rarity.EPIC)));
        THE_NEEDLE = ITEMS.register("the_needle", () -> new TheNeedleItem(new Item.Properties().stacksTo(1).fireResistant().rarity(Rarity.EPIC)));
        APOCALYPTIUM_INGOT = ITEMS.register("apocalyptium_ingot", () -> new ApocalyptiumIngotItem(new Item.Properties().rarity(Rarity.UNCOMMON).fireResistant()));
        TIP_OF_THE_SPEAR_OF_LONGINUS = ITEMS.register("the_tip_of_the_longinus", () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON).fireResistant()));
        DIMENSIONAL_WILL = ITEMS.register("dimensional_will", DimensionalWillItem::new);
        ETERNAL_WATCH = ITEMS.register("eternal_watch", EternalWatchItem::new);
        APOCALYPTIUM_HELMET = ITEMS.register("apocalyptium_helmet", () -> new ApocalyptiumArmor(ArmorItem.Type.HELMET));
        APOCALYPTIUM_CHESTPLATE = ITEMS.register("apocalyptium_chestplate", () -> new ApocalyptiumArmor(ArmorItem.Type.CHESTPLATE));
        APOCALYPTIUM_LEGGINGS = ITEMS.register("apocalyptium_leggings", () -> new ApocalyptiumArmor(ArmorItem.Type.LEGGINGS));
        APOCALYPTIUM_BOOTS = ITEMS.register("apocalyptium_boots", () -> new ApocalyptiumArmor(ArmorItem.Type.BOOTS));
        APOCALYPTIUM_TEMPLATE_ITEM = ITEMS.register("apocalyptium_upgrade_smithing_template", ApocalyptiumTemplateItem::new);
        MYSTERY_FRAGMENT = ITEMS.register("mystery_fragment", () -> new MysteryFragment(new Item.Properties().fireResistant().rarity(Rarity.EPIC).stacksTo(1)));
        TIP_OF_THE_SPEAR_OF_AEGLOS = ITEMS.register("the_tip_of_the_aeglos", () -> new NameCenteredItem(new Item.Properties().stacksTo(1).rarity(RevelationRarity.EDEN_NAME).fireResistant()));
        VALETTEIN = ITEMS.register("valettein", ValetteinItem::new);
        GUNGNIR = ITEMS.register("gungnir", GungnirItem::new);
        DISC_1 = ITEMS.register("disc_arch_illager", ArchIllagerDisc::new);
        DISC_2 = ITEMS.register("disc_arch_illager_remix", ArchIllagerRemixDisc::new);
        DISC_3 = ITEMS.register("disc_decisive_moment_disc", DecisiveMomentDisc::new);
        STAFF_FROSTBLOOM = ITEMS.register("frostbloom_staff", FrostbloomStaff::new);
        BI_RUNE_REACTOR = ModBlocks.asBLockItem(ITEMS, ModBlocks.RUNE_REACTOR);
        BI_RUNESTONE_ENGRAVED_TABLE = ModBlocks.asBLockItem(ITEMS, ModBlocks.RUNESTONE_ENGRAVED_TABLE);
        SPIDER_FANG = ITEMS.register("spider_fang", () -> new Item(new Item.Properties().stacksTo(64).food(new FoodProperties.Builder().fast().nutrition(3).saturationMod(0.6F).effect(() -> new MobEffectInstance(GoetyEffects.ACID_VENOM.get(), 40, 2), 1F).build())));
        VENOMOUS_SPIDER_WHIP = ITEMS.register("venomous_spider_whip", () -> new VenomousSpiderWhipItem(1F, -2.2F));
        SPECTRE_WHIP = ITEMS.register("spectre_whip", () -> new SpectreWhipItem(1F, -2.2F));
        HAUNTED_WHIP_HILT = ITEMS.register("haunted_whip_hilt", () -> new Item(new Item.Properties().stacksTo(1)));
        SPIDER_HELMET = ITEMS.register("spider_helmet", () -> new SpiderArmor(ArmorItem.Type.HELMET));
        SPIDER_CHESTPLATE = ITEMS.register("spider_chestplate", () -> new SpiderArmor(ArmorItem.Type.CHESTPLATE));
        SPIDER_LEGGINGS = ITEMS.register("spider_leggings", () -> new SpiderArmor(ArmorItem.Type.LEGGINGS));
        SPIDER_BOOTS = ITEMS.register("spider_boots", () -> new SpiderArmor(ArmorItem.Type.BOOTS));
        SPECTRE_HELMET = ITEMS.register("spectre_helmet", () -> new SpectreArmor(ArmorItem.Type.HELMET));
        SPECTRE_CHESTPLATE = ITEMS.register("spectre_chestplate", () -> new SpectreArmor(ArmorItem.Type.CHESTPLATE));
        SPECTRE_LEGGINGS = ITEMS.register("spectre_leggings", () -> new SpectreArmor(ArmorItem.Type.LEGGINGS));
        SPECTRE_BOOTS = ITEMS.register("spectre_boots", () -> new SpectreArmor(ArmorItem.Type.BOOTS));
        SPIDER_DARKMAGE_HELMET = ITEMS.register("spider_darkmage_helmet", () -> new SpiderDarkmageArmor(ArmorItem.Type.HELMET));
        SPIDER_DARKMAGE_CHESTPLATE = ITEMS.register("spider_darkmage_chestplate", () -> new SpiderDarkmageArmor(ArmorItem.Type.CHESTPLATE));
        SPIDER_DARKMAGE_LEGGINGS = ITEMS.register("spider_darkmage_leggings", () -> new SpiderDarkmageArmor(ArmorItem.Type.LEGGINGS));
        SPIDER_DARKMAGE_BOOTS = ITEMS.register("spider_darkmage_boots", () -> new SpiderDarkmageArmor(ArmorItem.Type.BOOTS));
        SPECTRE_DARKMAGE_HELMET = ITEMS.register("spectre_darkmage_helmet", () -> new SpectreDarkmageArmor(ArmorItem.Type.HELMET));
        SPECTRE_DARKMAGE_CHESTPLATE = ITEMS.register("spectre_darkmage_chestplate", () -> new SpectreDarkmageArmor(ArmorItem.Type.CHESTPLATE));
        SPECTRE_DARKMAGE_LEGGINGS = ITEMS.register("spectre_darkmage_leggings", () -> new SpectreDarkmageArmor(ArmorItem.Type.LEGGINGS));
        SPECTRE_DARKMAGE_BOOTS = ITEMS.register("spectre_darkmage_boots", () -> new SpectreDarkmageArmor(ArmorItem.Type.BOOTS));
        GOD_FORGING_SCROLL = ITEMS.register("god_forging_scroll", GodForgingResearchScrollItem::new);
        EDEN_SCROLL = ITEMS.register("eden_scroll", EdenResearchScrollItem::new);
        APOLLYON_ROBE = ITEMS.register("apollyon_robe", ApollyonRobeItem::new);
        SOUL_OF_OBSIDIAN = ITEMS.register("soul_of_obsidian", SoulOfObsidianItem::new);
        AMULET_OF_SLIME = ITEMS.register("amulet_of_slime", AmuletOfSlimeItem::new);
        GOLD_FEATHER = ITEMS.register("gold_feather", GoldFeatherItem::new);
        EERIE_AXE = ITEMS.register("eerie_axe", EerieAxeItem::new);
        ICE_HERMIT_SPAWN_EGG = ITEMS.register("ice_hermit_spawn_egg",
                () -> new ModSpawnEggItem(ModEntities.ICE_HERMIT, 0xd1f2ff, 0x68b0f3, ModSpawnEggs.egg()));
        BROKEN_ENDER_KEEPER_SWORD = ITEMS.register("broken_ender_keeper", ()-> new SwordItem(Tiers.WOOD, 1, -3.5F, new Item.Properties().fireResistant().rarity(Rarity.EPIC)));
        ENDER_KEEPER_SWORD = ITEMS.register("ender_keeper_sword", EnderKeeperSword::new);
        if (SafeClass.isEnigmaticLegacyLoaded())
            EnigmaticLegacyItemInit.init();

        GRItems.insertAfterTabMap.put(ASCENSION_HARD_CANDY, () -> List.of(DISC_1.get().getDefaultInstance(), DISC_2.get().getDefaultInstance(), DISC_3.get().getDefaultInstance()));

        GRItems.insertAfterTabMap.put(THE_NEEDLE, () -> {
            ItemStack stack = GRItems.THE_NEEDLE.get().getDefaultInstance();
            stack.getOrCreateTag().putBoolean(TheNeedleItem.IS_REAL_NBT, true);
            return List.of(stack);
        });

        if (SafeClass.isTetraLoaded())
            GRItems.insertAfterTabMap.put(APOCALYPTIUM_BOOTS, () -> List.of(new ItemStack(GRItems.TIP_OF_THE_SPEAR_OF_LONGINUS_ITEM), new ItemStack(GRItems.TIP_OF_THE_SPEAR_OF_AEGLOS.get())));
        //GRItems.insertAfterTabMap.put(11, List.of(createFragment(0), createFragment(1), createFragment(2), createFragment(3)));

        GRItems.insertAfterTabMap.put(QUIETUS_STAR, () ->
                List.of(new ItemStack(ModItems.DOOM_MEDAL.get()),
                        new ItemStack(ModItems.WITHER_QUIETUS.get())
                ));

        GRItems.insertAfterTabMap.put(NETHER_METEOR_FOCUS, () ->
                List.of(new ItemStack(com.Polarice3.Goety.common.items.ModItems.DARK_WAND.get()),
                        new ItemStack(com.Polarice3.Goety.common.items.ModItems.OMINOUS_STAFF.get()),
                        new ItemStack(com.Polarice3.Goety.common.items.ModItems.NECRO_STAFF.get()),
                        new ItemStack(com.Polarice3.Goety.common.items.ModItems.GEO_STAFF.get()),
                        new ItemStack(com.Polarice3.Goety.common.items.ModItems.WIND_STAFF.get()),
                        new ItemStack(com.Polarice3.Goety.common.items.ModItems.STORM_STAFF.get()),
                        new ItemStack(com.Polarice3.Goety.common.items.ModItems.FROST_STAFF.get()),
                        new ItemStack(com.Polarice3.Goety.common.items.ModItems.WILD_STAFF.get()),
                        new ItemStack(com.Polarice3.Goety.common.items.ModItems.ABYSS_STAFF.get()),
                        new ItemStack(com.Polarice3.Goety.common.items.ModItems.VOID_STAFF.get()),
                        new ItemStack(com.Polarice3.Goety.common.items.ModItems.NETHER_STAFF.get()),
                        new ItemStack(com.Polarice3.Goety.common.items.ModItems.NAMELESS_STAFF.get())
                ));
        GRItems.insertAfterTabMap.put(SPIDER_BOOTS, () ->
                List.of(new ItemStack(SPIDER_DARKMAGE_HELMET.get()),
                        new ItemStack(SPIDER_DARKMAGE_CHESTPLATE.get()),
                        new ItemStack(SPIDER_DARKMAGE_LEGGINGS.get()),
                        new ItemStack(SPIDER_DARKMAGE_BOOTS.get())
                ));
        GRItems.insertAfterTabMap.put(SPECTRE_BOOTS, () ->
                List.of(new ItemStack(SPECTRE_DARKMAGE_HELMET.get()),
                        new ItemStack(SPECTRE_DARKMAGE_CHESTPLATE.get()),
                        new ItemStack(SPECTRE_DARKMAGE_LEGGINGS.get()),
                        new ItemStack(SPECTRE_DARKMAGE_BOOTS.get())
                ));
    }

    public static ItemStack createFragment(int index) {
        ItemStack stack = MYSTERY_FRAGMENT.get().getDefaultInstance();
        stack.getOrCreateTag().putInt("fragment", index);
        return stack;
    }
}
