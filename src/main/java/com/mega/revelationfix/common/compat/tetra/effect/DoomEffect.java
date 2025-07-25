package com.mega.revelationfix.common.compat.tetra.effect;

import com.mega.endinglib.util.entity.DamageSourceGenerator;
import com.mega.revelationfix.common.apollyon.common.ExtraDamageTypes;
import com.mega.revelationfix.common.compat.SafeClass;
import com.mega.revelationfix.common.compat.tetra.TetraVersionCompat;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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

public class DoomEffect {
    public static ItemEffect itemEffect = ItemEffect.get("goety_revelation.doom");

    @OnlyIn(Dist.CLIENT)
    public static void init() {
        try {
            StatGetterEffectLevel statGetterEffectLevel = new StatGetterEffectLevel(itemEffect, 1.0D);
            GuiStatBar effectBar = new GuiStatBar(0, 0, 59, "goety_revelation.effect.doom.name", 0.0D, 100.0, false, statGetterEffectLevel, LabelGetterBasic.decimalLabel, TetraVersionCompat.createTGM("goety_revelation.effect.doom.tooltip", StatsHelper.withStats(statGetterEffectLevel, new StatGetterEffectEfficiency(itemEffect, 1.0D)), StatFormat.noDecimal, StatFormat.noDecimal));
            WorkbenchStatsGui.addBar(effectBar);
            HoloStatsGui.addBar(effectBar);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            System.exit(-1);
        }
    }

    public static void doPostHurt(LivingEntity attacker, Entity target) {
        if (!SafeClass.isTetraLoaded()) return;
        if (target instanceof LivingEntity livingTarget && attacker != null) {
            ItemStack heldStack = attacker.getMainHandItem();
            if (heldStack.getItem() instanceof ModularItem item) {
                int level = item.getEffectLevel(heldStack, itemEffect);
                if (level <= 0) return;
                if (livingTarget.isAlive() && livingTarget.getHealth() <= livingTarget.getMaxHealth() * (level / 100.0F)) {
                    DamageSource damageSource = new DamageSourceGenerator(attacker).source(ExtraDamageTypes.FE_POWER, attacker);
                    livingTarget.hurt(damageSource, livingTarget.getHealth() * 2.0F);
                }
            }
        }
    }
}
