package com.mega.revelationfix.common.item.armor;

import com.Polarice3.Goety.api.items.ISoulRepair;
import com.Polarice3.Goety.api.items.armor.ISoulDiscount;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mega.endinglib.api.client.cmc.CuriosMutableComponent;
import com.mega.endinglib.api.client.cmc.LoreStyle;
import com.mega.endinglib.api.item.armor.OptionArmorMaterial;
import com.mega.endinglib.util.entity.armor.ArmorModifiersBuilder;
import com.mega.revelationfix.common.apollyon.common.RevelationRarity;
import com.mega.revelationfix.common.init.ModAttributes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class SpectreArmor extends BaseArmorItem implements ISoulRepair, ISoulDiscount {
    public static final AttributeModifier ARMOR_ATTACK_DAMAGE_MODIFIER = new AttributeModifier(UUID.fromString("8ec494ac-5668-44be-af1f-3672b1c8caf7"), "ArmorSet modifier", 2, AttributeModifier.Operation.ADDITION);

    public SpectreArmor(OptionArmorMaterial optionArmorMaterial, Type armorType, Properties itemProperties) {
        super(optionArmorMaterial, armorType, itemProperties);
    }

    public SpectreArmor(Type p_40387_) {
        super(ModArmorMaterials.SPECTRE, p_40387_, new Properties().rarity(RevelationRarity.SPECTRE));
    }

    @Override
    public int getSoulDiscount(EquipmentSlot equipmentSlot) {
        return 4;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getSetAttributesModifiers(LivingEntity living) {
        return ImmutableMultimap.of(Attributes.ATTACK_DAMAGE, SpectreArmor.ARMOR_ATTACK_DAMAGE_MODIFIER);
    }
    @Override
    public boolean immuneEffects(LivingEntity living, MobEffectInstance mobEffect) {
        if (this.type == Type.HELMET) {
            MobEffect effect = mobEffect.getEffect();
            return effect == MobEffects.BLINDNESS || effect == MobEffects.DARKNESS;
        }
        return false;
    }

    @Override
    public void onLivingAttack(LivingAttackEvent event, ItemStack armorStack) {
        if (this.type == Type.CHESTPLATE) {
            DamageSource damageSource = event.getSource();
            if (damageSource.is(DamageTypes.SONIC_BOOM))
                event.setCanceled(true);
        }
    }

    @Override
    public boolean hasSetDescription() {
        return true;
    }
    @Override
    public void addSetDescription(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<CuriosMutableComponent> components, @NotNull TooltipFlag tooltipFlag) {
        components.add(CuriosMutableComponent.create(LoreStyle.INDENTATION_ATTRIBUTE_PREFIX).appendAttributeFormat_noPercent(0, new CuriosMutableComponent.AttributeDescFunction2("attribute.name.generic.attack_damage", (s)-> ARMOR_ATTACK_DAMAGE_MODIFIER.getAmount())));
    }

    @Override
    public void addSimpleDescription(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<CuriosMutableComponent> components, @NotNull TooltipFlag tooltipFlag) {
        switch (type) {
            case BOOTS -> {
                components.add(CuriosMutableComponent.create(Component.translatable("item.goety_revelation.spectre_boots.desc0"), LoreStyle.INDENTATION_ATTRIBUTE_PREFIX));
            }
            case HELMET -> {
                components.add(CuriosMutableComponent.create(Component.translatable("item.goety_revelation.spectre_helmet.desc0"), LoreStyle.INDENTATION_ATTRIBUTE_PREFIX));
            }
            case CHESTPLATE -> {
                components.add(CuriosMutableComponent.create(Component.translatable("item.goety_revelation.spectre_chestplate.desc0"), LoreStyle.INDENTATION_ATTRIBUTE_PREFIX));
            }
        }
        super.addSimpleDescription(itemStack, level, components, tooltipFlag);
    }
}
