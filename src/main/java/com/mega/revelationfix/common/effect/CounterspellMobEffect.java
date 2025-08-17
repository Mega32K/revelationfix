package com.mega.revelationfix.common.effect;

import com.mega.revelationfix.common.init.ModAttributes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CounterspellMobEffect extends MobEffect {
    private static final UUID SPELL_POWER_MODIFIER = UUID.fromString("4f755d02-edcc-480a-beb0-4a6c5eaccc4b");

    public CounterspellMobEffect() {
        super(MobEffectCategory.HARMFUL, 11141290);
        this.addAttributeModifier(ModAttributes.SPELL_COOLDOWN.get(), "d654243e-4f8d-4420-9e92-479ad1c02439", -0.05, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(ModAttributes.CAST_DURATION.get(), "3b60e3fa-6255-469e-91b5-fc6738846a92", -0.05, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(ModAttributes.SPELL_POWER.get(), SPELL_POWER_MODIFIER.toString(), -0.025, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public double getAttributeModifierValue(int amplifier, @NotNull AttributeModifier attributeModifier) {
        if (attributeModifier.getId().equals(SPELL_POWER_MODIFIER)) {
            return amplifier == 0 ? -0.1 : -0.1 + super.getAttributeModifierValue(amplifier - 1, attributeModifier);
        }
        return super.getAttributeModifierValue(amplifier, attributeModifier);
    }
}
