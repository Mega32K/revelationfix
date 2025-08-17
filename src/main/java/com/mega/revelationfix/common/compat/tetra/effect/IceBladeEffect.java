package com.mega.revelationfix.common.compat.tetra.effect;

import com.Polarice3.Goety.common.effects.GoetyEffects;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import se.mickelus.tetra.blocks.workbench.gui.WorkbenchStatsGui;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.gui.stats.bar.GuiStatBar;
import se.mickelus.tetra.gui.stats.getter.*;
import se.mickelus.tetra.items.modular.ModularItem;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloStatsGui;

public class IceBladeEffect {

    public static ItemEffect itemEffect = ItemEffect.get("goety_revelation.ice_blade");

    @OnlyIn(Dist.CLIENT)
    public static void init() {
        try {
            StatGetterEffectLevel statGetterEffectLevel = new StatGetterEffectLevel(itemEffect, 1.0D);
            GuiStatBar effectBar = new GuiStatBar(0, 0, 59, "goety_revelation.effect.ice_blade.name", 0.0D, 100.0, false, statGetterEffectLevel, LabelGetterBasic.decimalLabel, new TooltipGetterCursedBlade());
            WorkbenchStatsGui.addBar(effectBar);
            HoloStatsGui.addBar(effectBar);
        } catch (Throwable throwable) {
        }
    }

    @SubscribeEvent
    public void onPlayerAttack(LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof Player attacker) {
            ItemStack itemStack = attacker.getMainHandItem();
            if (itemStack.getItem() instanceof ModularItem modularItem) {
                int level = modularItem.getEffectLevel(itemStack, itemEffect);
                if (level <= 0) return;
                if (Math.random() * 100D <= level) {
                    int time = (int) (modularItem.getEffectEfficiency(itemStack, itemEffect) * 20F);
                    event.getEntity().addEffect(new MobEffectInstance(GoetyEffects.FREEZING.get(), time, 0));
                }
                if (Math.random() * 100D <= level * 0.05) {
                    int time = (int) (modularItem.getEffectEfficiency(itemStack, itemEffect) * 20F);
                    event.getEntity().addEffect(new MobEffectInstance(GoetyEffects.STUNNED.get(), time, 0));
                }
            }
        }
    }

    static class TooltipGetterCursedBlade implements ITooltipGetter {
        private static final IStatGetter efficiencyGetter;
        private static final IStatGetter levelGetter;
        private static final IStatGetter levelGetter2;

        static {
            efficiencyGetter = new StatGetterEffectEfficiency(itemEffect, 1.0);
            levelGetter = new StatGetterEffectLevel(itemEffect, 1.0);
            levelGetter2 = new StatGetterEffectLevel(itemEffect, 0.05);
        }

        public TooltipGetterCursedBlade() {
        }

        public String getTooltipBase(Player player, ItemStack itemStack) {
            String level = String.format("%.0f%%", levelGetter.getValue(player, itemStack));
            String efficiency = String.format("%s", efficiencyGetter.getValue(player, itemStack));
            String level2 = String.format("%.0f%%", levelGetter2.getValue(player, itemStack));
            return I18n.get("goety_revelation.effect.ice_blade.tooltip", level, efficiency, level2, efficiency);
        }

        public boolean hasExtendedTooltip(Player player, ItemStack itemStack) {
            return true;
        }

        public String getTooltipExtension(Player player, ItemStack itemStack) {
            return I18n.get("goety_revelation.effect.ice_blade.tooltip_extended");
        }
    }
}
