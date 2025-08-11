package com.mega.revelationfix.common.item.curios;

import com.google.common.collect.ImmutableMultimap;
import com.mega.endinglib.api.client.cmc.CuriosMutableComponent;
import com.mega.endinglib.api.client.cmc.LoreStyle;
import com.mega.endinglib.api.client.text.TextColorUtils;
import com.mega.endinglib.api.item.curios.SimpleDescriptiveCurio;
import com.mega.revelationfix.common.config.CommonConfig;
import com.mega.revelationfix.common.config.ItemConfig;
import com.mega.revelationfix.common.init.ModAttributes;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class SoulOfObsidianItem extends SimpleDescriptiveCurio {
    public SoulOfObsidianItem() {
        super(new Properties().rarity(Rarity.RARE).stacksTo(1), "charm", () -> {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> multimap = ImmutableMultimap.builder();
            multimap.put(ModAttributes.SOUL_DECREASE_EFFICIENCY.get(), new AttributeModifier(UUID.fromString("00033006-39a9-42f7-b601-a31d69b0b492"), "Curio Modifier", ItemConfig.soulOfObsidianSoulStealing * 0.01F, AttributeModifier.Operation.MULTIPLY_BASE));
            multimap.put(ModAttributes.SOUL_STEALING.get(), new AttributeModifier(UUID.fromString("00033006-39a9-42f7-b601-a31d69b0b492"), "Curio Modifier", ItemConfig.soulOfObsidianSoulDecreaseEfficiency * 0.01F, AttributeModifier.Operation.MULTIPLY_BASE));
            return multimap.build();
        });
        this.withHead(
                CuriosMutableComponent.create(Component.translatable("item.goety_revelation.soul_of_obsidian.real_desc0"), LoreStyle.NONE),
                CuriosMutableComponent.create(Component.translatable("item.goety_revelation.soul_of_obsidian.real_desc1"), LoreStyle.NONE),
                CuriosMutableComponent.create(Component.translatable("item.goety_revelation.soul_of_obsidian.real_desc2"), LoreStyle.NONE)
        ).withTail(
                CuriosMutableComponent.create().appendComponent(Component.translatable("item.goety_revelation.soul_of_obsidian.desc").withStyle(TextColorUtils.MIDDLE, ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC))
        );
    }

    @Override
    public boolean enableSimpleDesc() {
        return false;
    }

    @Override
    public boolean canBeHurtBy(@NotNull DamageSource source) {
        if (source.is(DamageTypeTags.IS_EXPLOSION))
            return false;
        return super.canBeHurtBy(source);
    }
}
