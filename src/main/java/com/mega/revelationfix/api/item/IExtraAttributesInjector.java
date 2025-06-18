package com.mega.revelationfix.api.item;

import com.google.common.collect.ImmutableMultimap;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;

public interface IExtraAttributesInjector {
    void injectExtraAttributes(ArmorMaterial armorMaterial, ArmorItem.Type type, ImmutableMultimap.Builder<Attribute, AttributeModifier> builder);
}
