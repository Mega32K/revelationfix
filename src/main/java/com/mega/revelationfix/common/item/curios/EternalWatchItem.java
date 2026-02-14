package com.mega.revelationfix.common.item.curios;

import com.mega.endinglib.api.client.cmc.CuriosMutableComponent;
import com.mega.endinglib.api.client.cmc.LoreStyle;
import com.mega.endinglib.api.client.text.TextColorUtils;
import com.mega.endinglib.api.item.curios.SimpleDescriptiveCurio;
import com.mega.revelationfix.api.item.IJEIInvisibleRitualResult;
import com.mega.revelationfix.common.config.ItemConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class EternalWatchItem extends SimpleDescriptiveCurio implements IJEIInvisibleRitualResult {
    public EternalWatchItem() {
        super(new Properties().fireResistant().stacksTo(1).rarity(Rarity.UNCOMMON), "necklace");
        this.withHead(
                CuriosMutableComponent.create().appendFormat("%s", s -> new Object[]{I18n.get("item.goety_revelation.eternal_watch.lore0")}),
                CuriosMutableComponent.create().appendFormat("%s", s -> new Object[]{I18n.get("item.goety_revelation.eternal_watch.lore1")})
        ).withTail(
                CuriosMutableComponent.create().appendFormat("%s", (s) -> new Object[]{I18n.get("tooltip.goety_revelation.armor.ability_skill_toggle", KeyMapping.createNameSupplier("key.revelationfix.armor_skill").get().getString().toUpperCase())}),
                CuriosMutableComponent.EMPTY,
                CuriosMutableComponent.create().appendComponent(Component.translatable("item.goety_revelation.eternal_watch.desc0").withStyle(TextColorUtils.MIDDLE, ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC)),
                CuriosMutableComponent.create().appendComponent(Component.translatable("item.goety_revelation.eternal_watch.desc1").withStyle(TextColorUtils.MIDDLE, ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC)),
                CuriosMutableComponent.create().appendComponent(Component.translatable("item.goety_revelation.eternal_watch.desc2").withStyle(TextColorUtils.MIDDLE, ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC)),
                CuriosMutableComponent.create().appendComponent(Component.translatable("item.goety_revelation.eternal_watch.desc3").withStyle(TextColorUtils.MIDDLE, ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC))
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
        list.add(CuriosMutableComponent.create(LoreStyle.ATTRIBUTE_PREFIX).appendFormat("%s", stack1 -> new Object[]{I18n.get("item.goety_revelation.eternal_watch.real_desc0", ItemConfig.eternalWatchFreezingTime)}));
        list.add(CuriosMutableComponent.EMPTY);
        list.add(CuriosMutableComponent.create().appendFormat("%s", s -> new Object[]{I18n.get("tooltip.goety_revelation.cooldowns_seconds", ItemConfig.eternalWatchCooldown)}));
        list.add(CuriosMutableComponent.EMPTY);
        list.add(CuriosMutableComponent.create(Component.translatable("tooltip.goety_revelation.passive_skill"), LoreStyle.NONE));
        list.add(CuriosMutableComponent.create(Component.translatable("item.goety_revelation.eternal_watch.real_desc1"), LoreStyle.ATTRIBUTE_PREFIX));
        list.add(CuriosMutableComponent.create(LoreStyle.INDENTATION).appendFormat("%s", s -> new Object[]{I18n.get("item.goety_revelation.eternal_watch.real_desc2", 10.0F)}));
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
}
