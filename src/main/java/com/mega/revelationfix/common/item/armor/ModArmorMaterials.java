package com.mega.revelationfix.common.item.armor;

import com.mega.revelationfix.common.init.GRItems;
import net.minecraft.Util;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.function.Supplier;

public enum ModArmorMaterials implements ArmorMaterial {
    APOCALYPTIUM("goety_revelation:apocalyptium_armor", Util.make(new EnumMap<>(ArmorItem.Type.class), (typeIntegerEnumMap) -> {
        typeIntegerEnumMap.put(ArmorItem.Type.BOOTS, 666);
        typeIntegerEnumMap.put(ArmorItem.Type.LEGGINGS, 666);
        typeIntegerEnumMap.put(ArmorItem.Type.CHESTPLATE, 666);
        typeIntegerEnumMap.put(ArmorItem.Type.HELMET, 666);
    }), Util.make(new EnumMap<>(ArmorItem.Type.class), (typeIntegerEnumMap) -> {
        typeIntegerEnumMap.put(ArmorItem.Type.BOOTS, 6);
        typeIntegerEnumMap.put(ArmorItem.Type.LEGGINGS, 9);
        typeIntegerEnumMap.put(ArmorItem.Type.CHESTPLATE, 11);
        typeIntegerEnumMap.put(ArmorItem.Type.HELMET, 6);
    }), 4F, 25, SoundEvents.ARMOR_EQUIP_NETHERITE, 0.3F, () -> Ingredient.of(GRItems.APOCALYPTIUM_INGOT_ITEM)),
    SPIDER("goety_revelation:spider_armor", Util.make(new EnumMap<>(ArmorItem.Type.class), (typeIntegerEnumMap) -> {
        typeIntegerEnumMap.put(ArmorItem.Type.BOOTS, 520);
        typeIntegerEnumMap.put(ArmorItem.Type.LEGGINGS, 600);
        typeIntegerEnumMap.put(ArmorItem.Type.CHESTPLATE, 640);
        typeIntegerEnumMap.put(ArmorItem.Type.HELMET, 440);
    }), Util.make(new EnumMap<>(ArmorItem.Type.class), (typeIntegerEnumMap) -> {
        typeIntegerEnumMap.put(ArmorItem.Type.BOOTS, 3);
        typeIntegerEnumMap.put(ArmorItem.Type.LEGGINGS, 6);
        typeIntegerEnumMap.put(ArmorItem.Type.CHESTPLATE, 8);
        typeIntegerEnumMap.put(ArmorItem.Type.HELMET, 3);
    }), 2F, 18, SoundEvents.ARMOR_EQUIP_NETHERITE, 0.1F, () -> Ingredient.of(GRItems.SPIDERS)),
    SPECTRE("goety_revelation:spectre_armor", Util.make(new EnumMap<>(ArmorItem.Type.class), (typeIntegerEnumMap) -> {
        typeIntegerEnumMap.put(ArmorItem.Type.BOOTS, 650);
        typeIntegerEnumMap.put(ArmorItem.Type.LEGGINGS, 750);
        typeIntegerEnumMap.put(ArmorItem.Type.CHESTPLATE, 800);
        typeIntegerEnumMap.put(ArmorItem.Type.HELMET, 550);
    }), Util.make(new EnumMap<>(ArmorItem.Type.class), (typeIntegerEnumMap) -> {
        typeIntegerEnumMap.put(ArmorItem.Type.BOOTS, 3);
        typeIntegerEnumMap.put(ArmorItem.Type.LEGGINGS, 6);
        typeIntegerEnumMap.put(ArmorItem.Type.CHESTPLATE, 8);
        typeIntegerEnumMap.put(ArmorItem.Type.HELMET, 3);
    }), 2F, 18, SoundEvents.ARMOR_EQUIP_NETHERITE, 0.1F, () -> Ingredient.of(Items.ECHO_SHARD)),
    SPIDER_DARKMAGE("goety_revelation:spider_darkmage_armor", Util.make(new EnumMap<>(ArmorItem.Type.class), (typeIntegerEnumMap) -> {
        typeIntegerEnumMap.put(ArmorItem.Type.BOOTS, 920);
        typeIntegerEnumMap.put(ArmorItem.Type.LEGGINGS, 1000);
        typeIntegerEnumMap.put(ArmorItem.Type.CHESTPLATE, 1040);
        typeIntegerEnumMap.put(ArmorItem.Type.HELMET, 840);
    }), Util.make(new EnumMap<>(ArmorItem.Type.class), (typeIntegerEnumMap) -> {
        typeIntegerEnumMap.put(ArmorItem.Type.BOOTS, 4);
        typeIntegerEnumMap.put(ArmorItem.Type.LEGGINGS, 7);
        typeIntegerEnumMap.put(ArmorItem.Type.CHESTPLATE, 9);
        typeIntegerEnumMap.put(ArmorItem.Type.HELMET, 4);
    }), 3.5F, 25, SoundEvents.ARMOR_EQUIP_NETHERITE, 0.1F, () -> Ingredient.of(GRItems.SPIDERS)),
    SPECTRE_DARKMAGE("goety_revelation:spectre_darkmage_armor", Util.make(new EnumMap<>(ArmorItem.Type.class), (typeIntegerEnumMap) -> {
        typeIntegerEnumMap.put(ArmorItem.Type.BOOTS, 1050);
        typeIntegerEnumMap.put(ArmorItem.Type.LEGGINGS, 1150);
        typeIntegerEnumMap.put(ArmorItem.Type.CHESTPLATE, 1200);
        typeIntegerEnumMap.put(ArmorItem.Type.HELMET, 950);
    }), Util.make(new EnumMap<>(ArmorItem.Type.class), (typeIntegerEnumMap) -> {
        typeIntegerEnumMap.put(ArmorItem.Type.BOOTS, 4);
        typeIntegerEnumMap.put(ArmorItem.Type.LEGGINGS, 7);
        typeIntegerEnumMap.put(ArmorItem.Type.CHESTPLATE, 8);
        typeIntegerEnumMap.put(ArmorItem.Type.HELMET, 4);
    }), 3.5F, 25, SoundEvents.ARMOR_EQUIP_NETHERITE, 0.1F, () -> Ingredient.of(Items.ECHO_SHARD));
    private final String name;
    private final EnumMap<ArmorItem.Type, Integer> durabilityFunctionForType;
    private final EnumMap<ArmorItem.Type, Integer> protectionFunctionForType;
    private final float toughness;
    private final int enchantmentValue;
    private final SoundEvent sound;
    private final float knockbackResistance;
    private final LazyLoadedValue<Ingredient> repairIngredient;

    ModArmorMaterials(String name,
                      EnumMap<ArmorItem.Type, Integer> durabilityFunctionForType,
                      EnumMap<ArmorItem.Type, Integer> protectionFunctionForType,
                      float toughness,
                      int enchantmentValue,
                      SoundEvent soundEvent,
                      float knockbackResistance,
                      Supplier<Ingredient> repairIngredient) {
        this.name = name;
        this.durabilityFunctionForType = durabilityFunctionForType;
        this.protectionFunctionForType = protectionFunctionForType;
        this.toughness = toughness;
        this.enchantmentValue = enchantmentValue;
        this.sound = soundEvent;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = new LazyLoadedValue<>(repairIngredient);
    }

    public int getDurabilityForType(ArmorItem.@NotNull Type type) {
        return durabilityFunctionForType.get(type);
    }

    public int getDefenseForType(ArmorItem.@NotNull Type type) {
        return this.protectionFunctionForType.get(type);
    }

    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    public SoundEvent getEquipSound() {
        return this.sound;
    }

    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    public String getName() {
        return this.name;
    }

    public float getToughness() {
        return this.toughness;
    }

    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}
