package com.mega.revelationfix.common.item.curios;

import com.google.common.collect.HashMultimap;
import com.mega.endinglib.api.client.cmc.CuriosMutableComponent;
import com.mega.endinglib.api.client.cmc.LoreStyle;
import com.mega.endinglib.api.client.text.TextColorUtils;
import com.mega.endinglib.api.item.curios.SimpleDescriptiveCurio;
import com.mega.revelationfix.common.config.ItemConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DimensionalWillItem extends SimpleDescriptiveCurio {
    public DimensionalWillItem() {
        super(new Properties().fireResistant().stacksTo(1).rarity(Rarity.EPIC), "back");
        this.withHead(
                CuriosMutableComponent.create().appendFormat("%s", s -> new Object[]{I18n.get("item.goety_revelation.dimensional_will.real_desc")})
        ).withTail(
                CuriosMutableComponent.create().appendComponent(Component.translatable("item.goety_revelation.dimensional_will.desc0").withStyle(TextColorUtils.MIDDLE, ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC)),
                CuriosMutableComponent.create().appendComponent(Component.translatable("item.goety_revelation.dimensional_will.desc1").withStyle(TextColorUtils.MIDDLE, ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC))
        );
    }

    @Override
    public List<Component> getSlotsTooltip(List<Component> tooltips, ItemStack stack) {
        showTitle = false;
        return super.getSlotsTooltip(tooltips, stack);
    }

    @Override
    public boolean enableShiftShowMore(ItemStack stack) {
        return true;
    }

    @Override
    public boolean enableSimpleDesc() {
        return false;
    }

    @Override
    public void appendShiftShowMore(ItemStack stack, List<Component> components) {
        List<CuriosMutableComponent> list = new ArrayList<>();
        list.add(CuriosMutableComponent.create(Component.translatable("tooltip.goety_revelation.passive_skill"), LoreStyle.NONE));
        list.add(CuriosMutableComponent.create(Component.translatable("item.goety_revelation.dimensional_will.real_desc0"), LoreStyle.ATTRIBUTE_PREFIX));
        list.add(CuriosMutableComponent.create(LoreStyle.INDENTATION).appendFormat("%s", s -> new Object[]{I18n.get("item.goety_revelation.dimensional_will.real_desc1", ItemConfig.dimensionalWillResistance)}));
        list.add(CuriosMutableComponent.create(LoreStyle.INDENTATION).appendFormat("%s", s -> new Object[]{I18n.get("item.goety_revelation.dimensional_will.real_desc2", ItemConfig.dimensionalWillDeathEscape)}));
        components.addAll(CuriosMutableComponent.listBake(list, stack));
    }

    @Override
    public boolean isFoil(@NotNull ItemStack p_41453_) {
        return false;
    }
}
