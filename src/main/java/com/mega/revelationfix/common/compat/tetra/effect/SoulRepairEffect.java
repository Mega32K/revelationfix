package com.mega.revelationfix.common.compat.tetra.effect;

import com.mega.revelationfix.common.compat.tetra.TetraVersionCompat;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import se.mickelus.tetra.blocks.workbench.gui.WorkbenchStatsGui;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.gui.stats.bar.GuiStatBar;
import se.mickelus.tetra.gui.stats.getter.IStatGetter;
import se.mickelus.tetra.gui.stats.getter.LabelGetterBasic;
import se.mickelus.tetra.gui.stats.getter.StatFormat;
import se.mickelus.tetra.gui.stats.getter.StatGetterEffectLevel;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloStatsGui;

public class SoulRepairEffect {
    public static ItemEffect itemEffect = ItemEffect.get("goety_revelation.soul_repair");

    @OnlyIn(Dist.CLIENT)
    public static void init() {
        try {
            StatGetterEffectLevel statGetterEffectLevel = new StatGetterEffectLevel(itemEffect, 1.0D);
            GuiStatBar effectBar = new GuiStatBar(0, 0, 59, "goety_revelation.effect.soul_repair.name", 0.0D, 10.0, false, statGetterEffectLevel, LabelGetterBasic.integerLabel, TetraVersionCompat.createTGM("goety_revelation.effect.soul_repair.tooltip", new IStatGetter[]{statGetterEffectLevel}, StatFormat.noDecimal));
            WorkbenchStatsGui.addBar(effectBar);
            HoloStatsGui.addBar(effectBar);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            System.exit(-1);
        }
    }
}
