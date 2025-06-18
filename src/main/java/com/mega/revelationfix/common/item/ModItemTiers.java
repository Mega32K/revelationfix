package com.mega.revelationfix.common.item;

import com.mega.revelationfix.common.init.GRItems;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;

import java.util.function.Supplier;

public enum ModItemTiers implements Tier {
    APOCALYPTIUM(6, 6666, 12.0F, 12.0F, 30, () -> {
        return Ingredient.of(GRItems.APOCALYPTIUM_INGOT_ITEM);
    });
    private final int level;
    private final int uses;
    private final float speed;
    private final float damage;
    private final int enchantmentValue;
    private final LazyLoadedValue<Ingredient> repairIngredient;

    ModItemTiers(int level, int uses, float speed, float damage, int enchantmentLevel, Supplier<Ingredient> repair) {
        this.level = level;
        this.uses = uses;
        this.speed = speed;
        this.damage = damage;
        this.enchantmentValue = enchantmentLevel;
        this.repairIngredient = new LazyLoadedValue<>(repair);
    }

    public int getUses() {
        return this.uses;
    }

    public float getSpeed() {
        return this.speed;
    }

    public float getAttackDamageBonus() {
        return this.damage;
    }

    public int getLevel() {
        return this.level;
    }

    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    @org.jetbrains.annotations.Nullable
    public net.minecraft.tags.TagKey<net.minecraft.world.level.block.Block> getTag() {
        return Tags.Blocks.NEEDS_NETHERITE_TOOL;
    }

}
