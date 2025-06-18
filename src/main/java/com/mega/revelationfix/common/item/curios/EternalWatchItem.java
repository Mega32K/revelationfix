package com.mega.revelationfix.common.item.curios;

import com.google.common.collect.HashMultimap;
import com.mega.revelationfix.common.apollyon.client.effect.CuriosMutableComponent;
import com.mega.revelationfix.common.apollyon.client.effect.LoreStyle;
import com.mega.revelationfix.common.config.ItemConfig;
import com.mega.revelationfix.common.item.FontItemExtensions;
import com.mega.revelationfix.api.item.ICenterDescItem;
import com.mega.revelationfix.api.item.IJEIInvisibleRitualResult;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class EternalWatchItem extends SimpleDescriptiveCurio implements IJEIInvisibleRitualResult, ICenterDescItem {
    public EternalWatchItem() {
        super(new Properties().fireResistant().stacksTo(1).rarity(Rarity.UNCOMMON), "necklace", HashMultimap.create());
        this.withHead(
                CuriosMutableComponent.create().appendFormat("%s", s -> new Object[]{I18n.get("item.goety_revelation.eternal_watch.lore0")}),
                CuriosMutableComponent.create().appendFormat("%s", s -> new Object[]{I18n.get("item.goety_revelation.eternal_watch.lore1")})
        ).withTail(
                CuriosMutableComponent.create().appendFormat("%s", (s) -> new Object[]{I18n.get("tooltip.goety_revelation.currentKeybind", KeyMapping.createNameSupplier("key.revelationfix.curios_skill").get().getString().toUpperCase())}),
                CuriosMutableComponent.EMPTY,
                CuriosMutableComponent.create().appendComponent(Component.translatable("item.goety_revelation.eternal_watch.desc0").withStyle(ChatFormatting.ITALIC, ChatFormatting.DARK_GRAY)),
                CuriosMutableComponent.create().appendComponent(Component.translatable("item.goety_revelation.eternal_watch.desc1").withStyle(ChatFormatting.ITALIC, ChatFormatting.DARK_GRAY)),
                CuriosMutableComponent.create().appendComponent(Component.translatable("item.goety_revelation.eternal_watch.desc2").withStyle(ChatFormatting.ITALIC, ChatFormatting.DARK_GRAY)),
                CuriosMutableComponent.create().appendComponent(Component.translatable("item.goety_revelation.eternal_watch.desc3").withStyle(ChatFormatting.ITALIC, ChatFormatting.DARK_GRAY))
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
    public void appendShiftShowMore(ItemStack stack, List<Component> components) {
        List<CuriosMutableComponent> list = new ArrayList<>();
        list.add(CuriosMutableComponent.create(Component.translatable("tooltip.goety_revelation.skill"), LoreStyle.NONE));
        list.add(CuriosMutableComponent.create(LoreStyle.ATTRIBUTE_PREFIX).appendFormat("%s", stack1 -> new Object[]{I18n.get("item.goety_revelation.eternal_watch.real_desc0", ItemConfig.ewFreezingTime)}));
        list.add(CuriosMutableComponent.EMPTY);
        list.add(CuriosMutableComponent.create().appendFormat("%s", s -> new Object[]{I18n.get("tooltip.goety_revelation.cooldowns_seconds", ItemConfig.ewCooldown)}));
        list.add(CuriosMutableComponent.EMPTY);
        list.add(CuriosMutableComponent.create(Component.translatable("tooltip.goety_revelation.passive_skill"), LoreStyle.NONE));
        list.add(CuriosMutableComponent.create(Component.translatable("item.goety_revelation.eternal_watch.real_desc1"), LoreStyle.ATTRIBUTE_PREFIX));
        list.add(CuriosMutableComponent.create(LoreStyle.INDENTATION).appendFormat("%s", s -> new Object[]{I18n.get("item.goety_revelation.eternal_watch.real_desc2", String.format("%.1f", ItemConfig.ewFinalAttackPercentage * 100.0F))}));
        list.add(CuriosMutableComponent.create(LoreStyle.INDENTATION).appendComponent(Component.translatable("item.goety_revelation.eternal_watch.real_desc3")));
        components.addAll(CuriosMutableComponent.listBake(list, stack));
    }

    @Override
    public boolean enableSimpleDesc() {
        return false;
    }

    @Override
    public boolean isFoil(@NotNull ItemStack p_41453_) {
        return false;
    }

    @Override
    public boolean isInvisibleInJEI(ItemStack stack) {
        return true;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new FontItemExtensions());
    }
}
