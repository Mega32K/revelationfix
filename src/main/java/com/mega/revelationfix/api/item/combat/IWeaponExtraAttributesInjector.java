package com.mega.revelationfix.api.item.combat;

import com.google.common.collect.ImmutableMultimap;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public interface IWeaponExtraAttributesInjector {
    void injectExtraAttributes(ImmutableMultimap.Builder<Attribute, AttributeModifier> builder);
}
