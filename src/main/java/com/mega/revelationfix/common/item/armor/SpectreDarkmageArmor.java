package com.mega.revelationfix.common.item.armor;

import com.google.common.collect.ImmutableMultimap;
import com.mega.revelationfix.api.item.IDamageLimitItem;
import com.mega.revelationfix.api.item.IDragonLightRendererItem;
import com.mega.revelationfix.common.apollyon.common.RevelationRarity;
import com.mega.revelationfix.common.init.ModAttributes;
import com.mega.revelationfix.common.item.tool.wand.FrostbloomStaff;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class SpectreDarkmageArmor extends SpectreArmor implements IDragonLightRendererItem, IDamageLimitItem {
    public SpectreDarkmageArmor(Type p_40387_) {
        super(ModArmorMaterials.SPECTRE_DARKMAGE, p_40387_, new Properties().rarity(RevelationRarity.SPECTRE), () -> {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
            UUID uuid = EXTRA_MODIFIER_UUID_PER_TYPE.get(p_40387_);
            builder.put(ModAttributes.SPELL_POWER.get(), new AttributeModifier(uuid, "Armor modifier", 0.35, AttributeModifier.Operation.ADDITION));
            builder.put(ModAttributes.SPELL_POWER_MULTIPLIER.get(), new AttributeModifier(uuid, "Armor modifier", .1, AttributeModifier.Operation.ADDITION));
            builder.put(ModAttributes.CAST_DURATION.get(), new AttributeModifier(uuid, "Armor modifier", .1, AttributeModifier.Operation.ADDITION));
            builder.put(ModAttributes.SPELL_COOLDOWN.get(), new AttributeModifier(uuid, "Armor modifier", .1, AttributeModifier.Operation.ADDITION));
            return builder;
        });
    }

    @Override
    public int getSoulDiscount(EquipmentSlot equipmentSlot) {
        return super.getSoulDiscount(equipmentSlot) + 2;
    }

    @Override
    public boolean enableDragonLightRenderer(ItemStack stack) {
        return true;
    }

    @Override
    public int getUseDamageLimit(ItemStack stack) {
        return 20;
    }
}
