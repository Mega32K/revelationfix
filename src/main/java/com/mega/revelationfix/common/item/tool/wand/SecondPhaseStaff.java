package com.mega.revelationfix.common.item.tool.wand;

import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.items.magic.DarkWand;
import com.Polarice3.Goety.common.magic.Spell;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mega.revelationfix.common.apollyon.common.RevelationRarity;
import com.mega.revelationfix.common.init.ModAttributes;
import com.mega.revelationfix.safe.mixinpart.goety.ILevelWand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class SecondPhaseStaff extends DarkWand implements ILevelWand {
    protected static final UUID BASE_FROST_POWER_UUID = UUID.fromString("2be35700-1df2-488b-816e-6f50cc301179");
    protected static final UUID BASE_SPELL_POWER_MULTIPLIER_UUID = UUID.fromString("e5000524-58b4-4c77-8eb5-ee95192b2386");
    protected static final UUID BASE_SPELL_COOLDOWN_UUID = UUID.fromString("cd09f208-2208-4dc3-8dd5-55f287777162");
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;
    private final Multimap<Attribute, AttributeModifier> offHandModifiers;

    public SecondPhaseStaff(UUID customSpellAttributeUUID) {
        super(new Properties().fireResistant().rarity(RevelationRarity.SPECTRE).setNoRepair().stacksTo(1), SpellType.FROST);
        {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 6.5F, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -2.2F, AttributeModifier.Operation.ADDITION));
            builder.put(ModAttributes.spellAttribute(spellType), new AttributeModifier(customSpellAttributeUUID, "Tool modifier", 1.5F, AttributeModifier.Operation.ADDITION));
            builder.put(ModAttributes.SPELL_POWER_MULTIPLIER.get(), new AttributeModifier(BASE_SPELL_POWER_MULTIPLIER_UUID, "Tool modifier", .20F, AttributeModifier.Operation.MULTIPLY_BASE));
            builder.put(ModAttributes.SPELL_COOLDOWN.get(), new AttributeModifier(BASE_SPELL_COOLDOWN_UUID, "Tool modifier", .25F, AttributeModifier.Operation.MULTIPLY_BASE));
            this.defaultModifiers = builder.build();
        }
        {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(ModAttributes.spellAttribute(spellType), new AttributeModifier(customSpellAttributeUUID, "Tool modifier", 1.5F, AttributeModifier.Operation.ADDITION));
            builder.put(ModAttributes.SPELL_POWER_MULTIPLIER.get(), new AttributeModifier(BASE_SPELL_POWER_MULTIPLIER_UUID, "Tool modifier", .20F, AttributeModifier.Operation.MULTIPLY_BASE));
            builder.put(ModAttributes.SPELL_COOLDOWN.get(), new AttributeModifier(BASE_SPELL_COOLDOWN_UUID, "Tool modifier", .25F, AttributeModifier.Operation.MULTIPLY_BASE));
            this.offHandModifiers = builder.build();
        }
    }

    @Override
    public int getStaffLevel() {
        return 2;
    }

    @Override
    public boolean expandedRightStaffLogic(Spell spell, ItemStack stack) {
        return spell.getSpellType() == spellType;
    }

    @Override
    public boolean expandedTypeStaffLogic(SpellType spellType, ItemStack stack) {
        return spellType == this.spellType;
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot pEquipmentSlot, ItemStack stack) {
        if (pEquipmentSlot == EquipmentSlot.MAINHAND) {
            return defaultModifiers;
        } else if (pEquipmentSlot == EquipmentSlot.OFFHAND) {
            return offHandModifiers;
        } else return super.getAttributeModifiers(pEquipmentSlot, stack);
    }
}
