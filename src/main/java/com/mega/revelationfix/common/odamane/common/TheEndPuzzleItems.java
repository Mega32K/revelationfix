package com.mega.revelationfix.common.odamane.common;

import com.mega.revelationfix.common.compat.SafeClass;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TheEndPuzzleItems {
    public static final List<Puzzle> srcPuzzles = new ArrayList<>();
    public static Map<Item, Puzzle> puzzleItems = new HashMap<>();
    //仅用作读取索引
    public static Map<Item, Integer> puzzleItems2 = new HashMap<>();
    public static Map<Integer, Puzzle> index2Puzzles = new Int2ObjectArrayMap<>();
    public static List<Item> vanillaItems;

    public static void bake() {
        if (srcPuzzles.isEmpty() || srcPuzzles.size() < (32 + 24)) {
            //原版+goety
            srcPuzzles.add(new Puzzle("minecraft:chorus_flower", 0));
            srcPuzzles.add(new Puzzle("goety:blazing_horn", 1));
            srcPuzzles.add(new Puzzle("goety:shadow_essence", 2));
            srcPuzzles.add(new Puzzle("goety:withered_manuscript", 3));
            srcPuzzles.add(new Puzzle("minecraft:sniffer_egg", 4));
            srcPuzzles.add(new Puzzle("minecraft:totem_of_undying", 5));
            srcPuzzles.add(new Puzzle("goety:philosophers_stone", 6));
            srcPuzzles.add(new Puzzle("minecraft:end_stone", 7));
            srcPuzzles.add(new Puzzle("goety:night_beacon", 8));
            srcPuzzles.add(new Puzzle("goety:pithos", 9));
            srcPuzzles.add(new Puzzle("minecraft:echo_shard", 10));
            srcPuzzles.add(new Puzzle("minecraft:bed", 11));
            srcPuzzles.add(new Puzzle("goety:arca", 12));
            srcPuzzles.add(new Puzzle("goety:animation_core", 13));
            srcPuzzles.add(new Puzzle("minecraft:glow_lichen", 14));
            srcPuzzles.add(new Puzzle("goety:totem_of_souls", 15));
            srcPuzzles.add(new Puzzle("minecraft:beacon", 16));
            srcPuzzles.add(new Puzzle("goety:howling_soul", 17));
            srcPuzzles.add(new Puzzle("goety:forbidden_piece", 18));
            srcPuzzles.add(new Puzzle("minecraft:end_crystal", 19));
            srcPuzzles.add(new Puzzle("minecraft:bone", 20));
            srcPuzzles.add(new Puzzle("minecraft:iron_ingot", 21));
            srcPuzzles.add(new Puzzle("minecraft:red_flower", 22));
            srcPuzzles.add(new Puzzle("goety:shriek_obelisk", 23));
            srcPuzzles.add(new Puzzle("goety:animator", 24));
            srcPuzzles.add(new Puzzle("goety:ectoplasm", 25));
            srcPuzzles.add(new Puzzle("minecraft:deepslate_emerald_ore", 26));
            srcPuzzles.add(new Puzzle("goety:tunnel_focus", 27));
            srcPuzzles.add(new Puzzle("goety:black_book", 28));
            srcPuzzles.add(new Puzzle("goety:infernal_tome", 29));
            srcPuzzles.add(new Puzzle("goety:raging_matter", 30));
            srcPuzzles.add(new Puzzle("minecraft:deadbush", 31));
            srcPuzzles.add(new Puzzle("minecraft:recovery_compass", 32));
            //它模组
            srcPuzzles.add(new Puzzle("irons_spellbooks:tarnished_helmet", 33));
            srcPuzzles.add(new Puzzle("irons_spellbooks:gold_crown", 34));
            srcPuzzles.add(new Puzzle("irons_spellbooks:permafrost_shard", 35));
            srcPuzzles.add(new Puzzle("irons_spellbooks:invisibility_ring", 36));
            srcPuzzles.add(new Puzzle("irons_spellbooks:arcane_debris", 37));
            srcPuzzles.add(new Puzzle("irons_spellbooks:ruined_book", 38));
            srcPuzzles.add(new Puzzle("cataclysm:ignitium_block", 39));
            srcPuzzles.add(new Puzzle("cataclysm:aptrgangr_head", 40));
            srcPuzzles.add(new Puzzle("cataclysm:void_lantern_block", 41));
            srcPuzzles.add(new Puzzle("alexsmobs:bear_dust", 42));
            srcPuzzles.add(new Puzzle("alexscaves:ominous_catalyst", 43));
            srcPuzzles.add(new Puzzle("ars_nouveau:wilden_tribute", 44));
            srcPuzzles.add(new Puzzle("aquamirae:rune_of_the_storm", 45));
            srcPuzzles.add(new Puzzle("enigmaticlegacy:the_infinitum", 46));
            srcPuzzles.add(new Puzzle("enigmaticlegacy:the_cube", 47));
            srcPuzzles.add(new Puzzle("enigmaticlegacy:cosmic_cake", 48));
            srcPuzzles.add(new Puzzle("enigmaticlegacy:evil_ingot", 49));
            srcPuzzles.add(new Puzzle("enigmaticaddons.evil_dagger", 50));
            srcPuzzles.add(new Puzzle("twilightforest:twilight_sapling", 51));
            srcPuzzles.add(new Puzzle("twilightforest:magic_beans", 52));
            srcPuzzles.add(new Puzzle("iceandfire:cannoli", 53));
            srcPuzzles.add(new Puzzle("jerotesvillage:bright_land_heart", 54));
            srcPuzzles.add(new Puzzle("fantasy_ending:uom_spawn_egg", 55));
            srcPuzzles.add(new Puzzle("l2complements:void_eye", 56));
            srcPuzzles.add(new Puzzle("alexsmobs:novelty_hat", 57));
            srcPuzzles.add(new Puzzle("alexscaves:game_controller", 58));
            srcPuzzles.add(new Puzzle("create:schematicannon", 59));
        }
        for (Puzzle puzzle : srcPuzzles) {
            if (!puzzle.isDisabled())
                puzzleItems.put(puzzle.safeGetItem(), puzzle);
        }
        for (Map.Entry<Item, Puzzle> e : puzzleItems.entrySet()) {
            puzzleItems2.put(e.getKey(), e.getValue().index);
        }
    }

    public static boolean validateItem(ResourceLocation resourceLocation) {
        return ForgeRegistries.ITEMS.containsKey(resourceLocation);
    }

    public static boolean uomEggBranch(ItemStack stack) {
        return SafeClass.isFantasyEndingLoaded() && stack.is(index2Puzzles.get(55).safeGetItem());
    }
}
