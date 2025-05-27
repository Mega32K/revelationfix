package com.mega.revelationfix.common.item.armor;

import com.mega.revelationfix.common.init.GRItems;
import net.minecraft.Util;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.function.Supplier;

public enum ModArmorMaterials implements ArmorMaterial {
    APOCALYPTIUM("apocalyptium", Util.make(new EnumMap<>(ArmorItem.Type.class), (typeIntegerEnumMap) -> {
        typeIntegerEnumMap.put(ArmorItem.Type.BOOTS, 666);
        typeIntegerEnumMap.put(ArmorItem.Type.LEGGINGS, 666);
        typeIntegerEnumMap.put(ArmorItem.Type.CHESTPLATE, 666);
        typeIntegerEnumMap.put(ArmorItem.Type.HELMET, 666);
    }), Util.make(new EnumMap<>(ArmorItem.Type.class), (typeIntegerEnumMap) -> {
        typeIntegerEnumMap.put(ArmorItem.Type.BOOTS, 6);
        typeIntegerEnumMap.put(ArmorItem.Type.LEGGINGS, 9);
        typeIntegerEnumMap.put(ArmorItem.Type.CHESTPLATE, 11);
        typeIntegerEnumMap.put(ArmorItem.Type.HELMET, 6);
    }), 4F, 20, SoundEvents.ARMOR_EQUIP_NETHERITE, 0.3F, () -> Ingredient.of(GRItems.APOCALYPTIUM_INGOT_ITEM));
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
