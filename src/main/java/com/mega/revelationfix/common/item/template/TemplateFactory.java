package com.mega.revelationfix.common.item.template;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import z1gned.goetyrevelation.ModMain;

import java.util.List;

public class TemplateFactory {
    public static final ChatFormatting TITLE_FORMAT = ChatFormatting.GRAY;
    public static final ChatFormatting DESCRIPTION_FORMAT = ChatFormatting.BLUE;
    public static final Component INGREDIENTS_TITLE = Component.translatable(Util.makeDescriptionId("item", new ResourceLocation("smithing_template.ingredients"))).withStyle(TITLE_FORMAT);
    public static final Component APPLIES_TO_TITLE = Component.translatable(Util.makeDescriptionId("item", new ResourceLocation("smithing_template.applies_to"))).withStyle(TITLE_FORMAT);
    public static final Component APOCALYPTIUM_UPGRADE = Component.translatable(Util.makeDescriptionId("upgrade", new ResourceLocation(ModMain.MODID, "apocalyptium_upgrade"))).withStyle(TITLE_FORMAT);
    public static final Component ARMOR_TRIM_APPLIES_TO = Component.translatable(Util.makeDescriptionId("item", new ResourceLocation("smithing_template.armor_trim.applies_to"))).withStyle(DESCRIPTION_FORMAT);
    public static final Component ARMOR_TRIM_INGREDIENTS = Component.translatable(Util.makeDescriptionId("item", new ResourceLocation("smithing_template.armor_trim.ingredients"))).withStyle(DESCRIPTION_FORMAT);
    public static final Component ARMOR_TRIM_BASE_SLOT_DESCRIPTION = Component.translatable(Util.makeDescriptionId("item", new ResourceLocation("smithing_template.armor_trim.base_slot_description")));
    public static final Component ARMOR_TRIM_ADDITIONS_SLOT_DESCRIPTION = Component.translatable(Util.makeDescriptionId("item", new ResourceLocation("smithing_template.armor_trim.additions_slot_description")));
    public static final Component APOCALYPTIUM_UPGRADE_APPLIES_TO = Component.translatable(Util.makeDescriptionId("item", new ResourceLocation(ModMain.MODID, "smithing_template.apocalyptium_upgrade.applies_to"))).withStyle(DESCRIPTION_FORMAT);
    public static final Component APOCALYPTIUM_UPGRADE_INGREDIENTS = Component.translatable(Util.makeDescriptionId("item", new ResourceLocation(ModMain.MODID, "smithing_template.apocalyptium_upgrade.ingredients"))).withStyle(DESCRIPTION_FORMAT);
    public static final Component APOCALYPTIUM_UPGRADE_BASE_SLOT_DESCRIPTION = Component.translatable(Util.makeDescriptionId("item", new ResourceLocation(ModMain.MODID, "smithing_template.apocalyptium_upgrade.base_slot_description")));
    public static final Component APOCALYPTIUM_UPGRADE_ADDITIONS_SLOT_DESCRIPTION = Component.translatable(Util.makeDescriptionId("item", new ResourceLocation(ModMain.MODID, "smithing_template.apocalyptium_upgrade.additions_slot_description")));
    public static final ResourceLocation EMPTY_SLOT_HELMET = new ResourceLocation("item/empty_armor_slot_helmet");
    public static final ResourceLocation EMPTY_SLOT_CHESTPLATE = new ResourceLocation("item/empty_armor_slot_chestplate");
    public static final ResourceLocation EMPTY_SLOT_LEGGINGS = new ResourceLocation("item/empty_armor_slot_leggings");
    public static final ResourceLocation EMPTY_SLOT_BOOTS = new ResourceLocation("item/empty_armor_slot_boots");
    public static final ResourceLocation EMPTY_SLOT_HOE = new ResourceLocation("item/empty_slot_hoe");
    public static final ResourceLocation EMPTY_SLOT_AXE = new ResourceLocation("item/empty_slot_axe");
    public static final ResourceLocation EMPTY_SLOT_SWORD = new ResourceLocation("item/empty_slot_sword");
    public static final ResourceLocation EMPTY_SLOT_SHOVEL = new ResourceLocation("item/empty_slot_shovel");
    public static final ResourceLocation EMPTY_SLOT_PICKAXE = new ResourceLocation("item/empty_slot_pickaxe");
    public static final ResourceLocation EMPTY_SLOT_INGOT = new ResourceLocation("item/empty_slot_ingot");
    public static final ResourceLocation EMPTY_SLOT_REDSTONE_DUST = new ResourceLocation("item/empty_slot_redstone_dust");
    public static final ResourceLocation EMPTY_SLOT_QUARTZ = new ResourceLocation("item/empty_slot_quartz");
    public static final ResourceLocation EMPTY_SLOT_EMERALD = new ResourceLocation("item/empty_slot_emerald");
    public static final ResourceLocation EMPTY_SLOT_DIAMOND = new ResourceLocation("item/empty_slot_diamond");
    public static final ResourceLocation EMPTY_SLOT_LAPIS_LAZULI = new ResourceLocation("item/empty_slot_lapis_lazuli");
    public static final ResourceLocation EMPTY_SLOT_AMETHYST_SHARD = new ResourceLocation("item/empty_slot_amethyst_shard");

    public static List<ResourceLocation> createTrimmableMaterialIconList() {
        return List.of(EMPTY_SLOT_INGOT, EMPTY_SLOT_REDSTONE_DUST, EMPTY_SLOT_LAPIS_LAZULI, EMPTY_SLOT_QUARTZ, EMPTY_SLOT_DIAMOND, EMPTY_SLOT_EMERALD, EMPTY_SLOT_AMETHYST_SHARD);
    }

    public static List<ResourceLocation> createUpgradeMaterialList() {
        return List.of(EMPTY_SLOT_INGOT);
    }

    public static List<ResourceLocation> createUpgradeIconList() {
        return List.of(EMPTY_SLOT_HELMET, EMPTY_SLOT_SWORD, EMPTY_SLOT_CHESTPLATE, EMPTY_SLOT_PICKAXE, EMPTY_SLOT_LEGGINGS, EMPTY_SLOT_AXE, EMPTY_SLOT_BOOTS, EMPTY_SLOT_HOE, EMPTY_SLOT_SHOVEL);
    }
}
