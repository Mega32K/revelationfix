package com.mega.revelationfix.common.compat.tetra.effect;

import com.Polarice3.Goety.common.effects.GoetyEffects;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import se.mickelus.tetra.blocks.workbench.gui.WorkbenchStatsGui;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.gui.stats.bar.GuiStatBar;
import se.mickelus.tetra.gui.stats.getter.*;
import se.mickelus.tetra.items.modular.ModularItem;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloStatsGui;

public class ShadowWalkEffect {
    public static ItemEffect itemEffect = ItemEffect.get("goety_revelation.shadow_walk");

    @OnlyIn(Dist.CLIENT)
    public static void init() {
        try {
            StatGetterEffectLevel statGetterEffectLevel = new StatGetterEffectLevel(itemEffect, 1.0D);
            StatGetterEffectEfficiency statGetterEffectEfficiency = new StatGetterEffectEfficiency(itemEffect, 3.0D);
            GuiStatBar effectBar = new GuiStatBar(0, 0, 59, "goety_revelation.effect.shadow_walk.name", 0.0D, 100.0, false, statGetterEffectLevel, LabelGetterBasic.decimalLabel, new TooltipGetterShadowWalk());
            WorkbenchStatsGui.addBar(effectBar);
            HoloStatsGui.addBar(effectBar);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            System.exit(-1);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onHurtEntity(LivingDamageEvent event) {
        if (event.getSource().getEntity() instanceof Player attacker) {
            ItemStack itemStack = attacker.getMainHandItem();
            if (itemStack.getItem() instanceof ModularItem modularItem) {
                int level = modularItem.getEffectLevel(itemStack, itemEffect);
                if (level <= 0) return;
                if (Math.random() * 100D <= level) {
                    int time = (int) (modularItem.getEffectEfficiency(itemStack, itemEffect) * 20F * 3);
                    attacker.addEffect(new MobEffectInstance(GoetyEffects.SHADOW_WALK.get(), time, 1));
                }
            }
        }
    }

    static class TooltipGetterShadowWalk implements ITooltipGetter {
        private static final IStatGetter efficiencyGetter;
        private static final IStatGetter levelGetter;

        static {
            efficiencyGetter = new StatGetterEffectEfficiency(itemEffect, 1.0);
            levelGetter = new StatGetterEffectLevel(itemEffect, 1.0);
        }

        public TooltipGetterShadowWalk() {
        }

        public String getTooltipBase(Player player, ItemStack itemStack) {
            String level = String.format("%.0f%%", levelGetter.getValue(player, itemStack));
            String efficiency = String.format("%s", (int) efficiencyGetter.getValue(player, itemStack));
            return I18n.get("goety_revelation.effect.shadow_walk.tooltip", level, efficiency);
        }

        public boolean hasExtendedTooltip(Player player, ItemStack itemStack) {
            return true;
        }

        public String getTooltipExtension(Player player, ItemStack itemStack) {
            return I18n.get("goety_revelation.effect.shadow_walk.tooltip_extended");
        }
    }
}
