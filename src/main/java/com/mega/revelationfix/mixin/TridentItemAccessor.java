package com.mega.revelationfix.mixin;

import com.google.common.collect.Multimap;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.TridentItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TridentItem.class)
public interface TridentItemAccessor {
    @Accessor("defaultModifiers")
    void resetModifiers(Multimap<Attribute, AttributeModifier> defaultModifiers);
}
