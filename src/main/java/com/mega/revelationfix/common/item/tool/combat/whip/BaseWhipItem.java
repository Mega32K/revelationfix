package com.mega.revelationfix.common.item.tool.combat.whip;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class BaseWhipItem extends SwordItem {
    protected static final UUID BASE_ENTITY_REACH_UUID = UUID.fromString("83a920dc-b9ba-43d2-8283-a76ebd658798");
    private final float attackDamage;
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public BaseWhipItem(Tier tier, float attackDamageAddition, float attackSpeedAddition, float reachAddition, @Nullable Properties properties) {
        super(Tiers.DIAMOND, (int) attackDamageAddition, attackSpeedAddition, properties == null ? new Properties() : properties);
        this.attackDamage = attackDamageAddition + tier.getAttackDamageBonus();
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", this.attackDamage, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", attackSpeedAddition, AttributeModifier.Operation.ADDITION));
        builder.put(ForgeMod.ENTITY_REACH.get(), new AttributeModifier(BASE_ENTITY_REACH_UUID, "Weapon modifier", reachAddition, AttributeModifier.Operation.ADDITION));

        this.defaultModifiers = builder.build();
    }

    public BaseWhipItem(float attackDamageAddition, float attackSpeedAddition, float reachAddition) {
        this(Tiers.DIAMOND, attackDamageAddition, attackSpeedAddition, reachAddition, null);
    }

    @Override
    public float getDamage() {
        return this.attackDamage;
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack itemStack, @NotNull LivingEntity entity, @NotNull LivingEntity player) {
        return super.hurtEnemy(itemStack, entity, player);
    }

    @Override
    public @NotNull AABB getSweepHitBox(@NotNull ItemStack stack, @NotNull Player player, @NotNull Entity target) {
        double maxInflation = Mth.clamp(EnchantmentHelper.getEnchantmentLevel(Enchantments.SWEEPING_EDGE, player) * 0.3, 0D, 2D);
        return super.getSweepHitBox(stack, player, target).inflate(0.5D + maxInflation * 0.5F, 0D, 0.5D + maxInflation);
    }

    @Override
    public float getDestroySpeed(@NotNull ItemStack itemStack, @NotNull BlockState blockState) {
        if (blockState.is(BlockTags.LEAVES) || blockState.is(BlockTags.PLANKS))
            return 15.0F;
        return super.getDestroySpeed(itemStack, blockState) * 0.75F;
    }

    @Override
    public boolean isCorrectToolForDrops(BlockState blockState) {
        return blockState.is(Blocks.COBWEB) || blockState.is(BlockTags.LEAVES) || blockState.is(BlockTags.PLANKS);
    }

    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot p_43274_) {
        return p_43274_ == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(p_43274_);
    }
}
