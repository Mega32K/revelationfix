package com.mega.revelationfix.common.item.curios;

import com.mega.endinglib.api.client.cmc.CuriosMutableComponent;
import com.mega.endinglib.api.client.cmc.LoreStyle;
import com.mega.endinglib.api.client.text.TextColorUtils;
import com.mega.endinglib.api.item.curios.SimpleDescriptiveCurio;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Rarity;

public class GoldFeatherItem extends SimpleDescriptiveCurio {
    public GoldFeatherItem() {
        super(new Properties().rarity(Rarity.RARE).stacksTo(1), "charm");
        this.defaultDesc(
                CuriosMutableComponent.create(Component.translatable("item.goety_revelation.gold_feather.real_desc0"), LoreStyle.ATTRIBUTE_PREFIX)
        ).withTail(
                CuriosMutableComponent.create().appendComponent(Component.translatable("item.goety_revelation.gold_feather.desc").withStyle(TextColorUtils.MIDDLE, ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC))
        );
    }

    @Override
    public boolean enableSimpleDesc() {
        return false;
    }
}
