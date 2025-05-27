package com.mega.revelationfix.common.item.curios;

import com.google.common.collect.HashMultimap;
import com.mega.revelationfix.common.apollyon.client.effect.CuriosMutableComponent;
import com.mega.revelationfix.common.apollyon.client.effect.LoreStyle;
import com.mega.revelationfix.common.config.ItemConfig;
import com.mega.revelationfix.common.item.FontItemExtensions;
import com.mega.revelationfix.common.item.ICenterDescItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class DimensionalWillItem extends SimpleDescriptiveCurio implements ICenterDescItem {
    public DimensionalWillItem() {
        super(new Properties().fireResistant().stacksTo(1).rarity(Rarity.EPIC), "back", HashMultimap.create());
        this.withHead(
                CuriosMutableComponent.create().appendFormat("%s", s -> new Object[]{I18n.get("item.goety_revelation.dimensional_will.real_desc")})
        ).withTail(
                CuriosMutableComponent.create().appendComponent(Component.translatable("item.goety_revelation.dimensional_will.desc0").withStyle(ChatFormatting.ITALIC, ChatFormatting.DARK_GRAY)),
                CuriosMutableComponent.create().appendComponent(Component.translatable("item.goety_revelation.dimensional_will.desc1").withStyle(ChatFormatting.ITALIC, ChatFormatting.DARK_GRAY))
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
        list.add(CuriosMutableComponent.create(LoreStyle.INDENTATION).appendFormat("%s", s -> new Object[]{I18n.get("item.goety_revelation.dimensional_will.real_desc1", ItemConfig.dwResistance)}));
        list.add(CuriosMutableComponent.create(LoreStyle.INDENTATION).appendFormat("%s", s -> new Object[]{I18n.get("item.goety_revelation.dimensional_will.real_desc2", ItemConfig.dwDeathEscape)}));
        components.addAll(CuriosMutableComponent.listBake(list, stack));
    }

    @Override
    public boolean isFoil(@NotNull ItemStack p_41453_) {
        return false;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new FontItemExtensions());
    }
}
