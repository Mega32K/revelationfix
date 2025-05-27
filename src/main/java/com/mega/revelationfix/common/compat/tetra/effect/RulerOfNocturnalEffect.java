package com.mega.revelationfix.common.compat.tetra.effect;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import se.mickelus.tetra.blocks.workbench.gui.WorkbenchStatsGui;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.gui.stats.bar.GuiStatBar;
import se.mickelus.tetra.gui.stats.getter.ITooltipGetter;
import se.mickelus.tetra.gui.stats.getter.LabelGetterBasic;
import se.mickelus.tetra.gui.stats.getter.StatGetterEffectLevel;
import se.mickelus.tetra.items.modular.ModularItem;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloStatsGui;

public class RulerOfNocturnalEffect {
    public static ItemEffect itemEffect = ItemEffect.get("goety_revelation.ruler_of_nocturnal");

    @OnlyIn(Dist.CLIENT)
    public static void init() {
        try {
            StatGetterEffectLevel statGetterEffectLevel = new StatGetterEffectLevel(itemEffect, 1.0D);
            GuiStatBar effectBar = new GuiStatBar(0, 0, 59, "goety_revelation.effect.ruler_of_nocturnal.name", 0.0D, 1D, false, statGetterEffectLevel, LabelGetterBasic.noLabel, new TooltipGetterRON());
            WorkbenchStatsGui.addBar(effectBar);
            HoloStatsGui.addBar(effectBar);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            System.exit(-1);
        }
    }

    @SubscribeEvent
    public void onHurtEntity(LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof Player attacker) {
            ItemStack itemStack = attacker.getMainHandItem();
            if (itemStack.getItem() instanceof ModularItem modularItem) {
                int level = modularItem.getEffectLevel(itemStack, itemEffect);
                if (level <= 0) return;
                if (attacker.level.getSkyDarken() > 0) {
                    event.setAmount(event.getAmount() * 1.6666F);
                } else event.setAmount(event.getAmount() * (1F - .6666F));
            }
        }
    }

    static class TooltipGetterRON implements ITooltipGetter {

        public TooltipGetterRON() {
        }

        public String getTooltipBase(Player player, ItemStack itemStack) {
            return I18n.get("goety_revelation.effect.ruler_of_nocturnal.tooltip");
        }

        public boolean hasExtendedTooltip(Player player, ItemStack itemStack) {
            return false;
        }

        public String getTooltipExtension(Player player, ItemStack itemStack) {
            return "";
        }
    }
}
