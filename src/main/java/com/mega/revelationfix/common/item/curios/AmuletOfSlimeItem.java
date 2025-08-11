package com.mega.revelationfix.common.item.curios;

import com.google.common.collect.ImmutableMultimap;
import com.mega.endinglib.api.client.cmc.CuriosMutableComponent;
import com.mega.endinglib.api.client.cmc.LoreStyle;
import com.mega.endinglib.api.item.curios.SimpleDescriptiveCurio;
import com.mega.revelationfix.common.config.ItemConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Rarity;

public class AmuletOfSlimeItem extends SimpleDescriptiveCurio {
    public AmuletOfSlimeItem() {
        super(new Properties().rarity(Rarity.RARE).stacksTo(1), "charm");
        this.defaultDesc(
                CuriosMutableComponent.create(Component.translatable("item.goety_revelation.amulet_of_slime.real_desc0"), LoreStyle.ATTRIBUTE_PREFIX),
                CuriosMutableComponent.create(Component.translatable("item.goety_revelation.amulet_of_slime.real_desc1"), LoreStyle.INDENTATION),
                CuriosMutableComponent.create(LoreStyle.INDENTATION).appendFormat("%s", is -> new Object[] { Component.translatable("item.goety_revelation.amulet_of_slime.real_desc2", ItemConfig.amuletOfSlimeCooldown).getString()})
        );
    }

    @Override
    public boolean enableSimpleDesc() {
        return false;
    }
}
