package com.mega.revelationfix.api.item.armor;

import com.mega.endinglib.api.client.cmc.CuriosMutableComponent;
import com.mega.endinglib.api.client.cmc.LoreStyle;
import com.mega.revelationfix.common.event.handler.ArmorEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface IGoetyDamageResistanceArmor {
    default List<CuriosMutableComponent> damageResistanceTooltipCMC(ArmorItem armorItem, ItemStack itemStack) {
        return List.of(
                CuriosMutableComponent.create(LoreStyle.INDENTATION_ATTRIBUTE_PREFIX).appendAttributeFormat(
                        1,
                        new CuriosMutableComponent.AttributeDescFunction2("tooltip.goety.armor.reduce_magic_damage", (is) -> ArmorEvents.getDamageReduction(DamageTypeTags.WITCH_RESISTANT_TO, armorItem) * 100.0F)
                ),
                CuriosMutableComponent.create(LoreStyle.INDENTATION_ATTRIBUTE_PREFIX).appendAttributeFormat(
                        1,
                        new CuriosMutableComponent.AttributeDescFunction2("tooltip.goety.armor.reduce_fire_damage", (is) -> ArmorEvents.getDamageReduction(DamageTypeTags.IS_FIRE, armorItem) * 100.0F)
                ),
                CuriosMutableComponent.create(LoreStyle.INDENTATION_ATTRIBUTE_PREFIX).appendAttributeFormat(
                        1,
                        new CuriosMutableComponent.AttributeDescFunction2("tooltip.goety.armor.reduce_explosion_damage", (is) -> ArmorEvents.getDamageReduction(DamageTypeTags.IS_EXPLOSION, armorItem) * 100.0F)
                )
        );
    }
    default List<Component> damageResistanceTooltip(ArmorItem armorItem, ItemStack itemStack) {
        return CuriosMutableComponent.listBake(damageResistanceTooltipCMC(armorItem, itemStack), itemStack);
    }
}
