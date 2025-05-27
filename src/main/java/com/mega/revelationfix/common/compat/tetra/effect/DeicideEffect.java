package com.mega.revelationfix.common.compat.tetra.effect;

import com.mega.revelationfix.common.compat.tetra.TetraVersionCompat;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import se.mickelus.tetra.blocks.workbench.gui.WorkbenchStatsGui;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.gui.stats.StatsHelper;
import se.mickelus.tetra.gui.stats.bar.GuiStatBar;
import se.mickelus.tetra.gui.stats.getter.LabelGetterBasic;
import se.mickelus.tetra.gui.stats.getter.StatFormat;
import se.mickelus.tetra.gui.stats.getter.StatGetterEffectEfficiency;
import se.mickelus.tetra.gui.stats.getter.StatGetterEffectLevel;
import se.mickelus.tetra.items.modular.ModularItem;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloStatsGui;

public class DeicideEffect {
    public static ItemEffect itemEffect = ItemEffect.get("goety_revelation.deicide");

    @OnlyIn(Dist.CLIENT)
    public static void init() {
        try {
            StatGetterEffectLevel statGetterEffectLevel = new StatGetterEffectLevel(itemEffect, 1.0D);
            GuiStatBar effectBar = new GuiStatBar(0, 0, 59, "goety_revelation.effect.deicide.name", 0.0D, 100.0, false, statGetterEffectLevel, LabelGetterBasic.decimalLabel, TetraVersionCompat.createTGM("goety_revelation.effect.deicide.tooltip", StatsHelper.withStats(statGetterEffectLevel, new StatGetterEffectEfficiency(itemEffect, 1.0D)), StatFormat.noDecimal, StatFormat.noDecimal));
            WorkbenchStatsGui.addBar(effectBar);
            HoloStatsGui.addBar(effectBar);
        } catch (Throwable throwable) {
        }
    }

    @SubscribeEvent
    public void livingHurtEvent(LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof LivingEntity attacker) {
            LivingEntity beHurt = event.getEntity();
            ItemStack itemStack = attacker.getMainHandItem();
            if (itemStack.getItem() instanceof ModularItem modularItem && (beHurt instanceof Player || beHurt.getType().is(Tags.EntityTypes.BOSSES))) {
                int level = modularItem.getEffectLevel(itemStack, itemEffect);
                if (level <= 0) return;
                event.setAmount(event.getAmount() * (1.0F + level / 10.0F));
            }
        }
    }
}
