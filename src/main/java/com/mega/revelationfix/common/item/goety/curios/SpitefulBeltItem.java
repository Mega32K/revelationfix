package com.mega.revelationfix.common.item.goety.curios;

import com.Polarice3.Goety.common.items.curios.SingleStackItem;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import java.util.UUID;

public class SpitefulBeltItem extends SingleStackItem {
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> map = HashMultimap.create();
        CuriosApi.addSlotModifier(map, "charm",
                UUID.fromString("bf084d19-7ea7-479a-8e19-6edf2d90cd6e"), 1.0D, AttributeModifier.Operation.ADDITION);
        return map;
    }
}
