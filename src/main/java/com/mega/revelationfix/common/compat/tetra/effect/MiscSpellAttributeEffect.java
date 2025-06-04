package com.mega.revelationfix.common.compat.tetra.effect;

import com.mega.revelationfix.common.compat.tetra.TetraVersionCompat;
import com.mega.revelationfix.common.init.ModAttributes;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import se.mickelus.tetra.blocks.workbench.gui.WorkbenchStatsGui;
import se.mickelus.tetra.gui.stats.bar.GuiStatBar;
import se.mickelus.tetra.gui.stats.getter.*;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloStatsGui;

public class MiscSpellAttributeEffect {
    public static void init() {
        createPercentAttributeBar(ModAttributes.SPELL_COOLDOWN.get(), "spell_cooldown");
        createPercentAttributeBar(ModAttributes.SPELL_POWER_MULTIPLIER.get(), "spell_power_multiplier");
        createPercentAttributeBar(ModAttributes.SPELL_POWER.get(), "spell_power");
        createPercentAttributeBar(ModAttributes.CAST_DURATION.get(), "cast_duration");
    }
    @OnlyIn(Dist.CLIENT)
    private static void createPercentAttributeBar(Attribute attribute, String languageKey) {
        try {
            StatGetterAttribute statGetterPercentAttribute = new HundredMultiStatGetterAttribute(attribute, true);
            GuiStatBar statBar = new GuiStatBar(0, 0, 59, attribute.getDescriptionId(),
                    0.0D, 100D, false, statGetterPercentAttribute, LabelGetterBasic.decimalLabel,
                   new TooltipGetterSimpleAttribute(attribute, languageKey));
            WorkbenchStatsGui.addBar(statBar);
            HoloStatsGui.addBar(statBar);
        } catch (Throwable throwable) {
        }
    }

    static class TooltipGetterSimpleAttribute implements ITooltipGetter {
        private final HundredMultiStatGetterAttribute levelGetter;
        private final String languageKey;

        public TooltipGetterSimpleAttribute(Attribute attribute, String languageKey) {
            this.levelGetter = new HundredMultiStatGetterAttribute(attribute);
            this.languageKey = languageKey;
        }

        public String getTooltipBase(Player player, ItemStack itemStack) {
            String level = String.format("%.0f%%", levelGetter.getValue(player, itemStack));
            return I18n.get("goety_revelation.effect." + languageKey +".tooltip", level);
        }

        public boolean hasExtendedTooltip(Player player, ItemStack itemStack) {
            return false;
        }
    }
    public static class HundredMultiStatGetterAttribute extends StatGetterAttribute {

        public HundredMultiStatGetterAttribute(Attribute attribute) {
            super(attribute);
        }

        public HundredMultiStatGetterAttribute(Attribute attribute, boolean ignoreBase) {
            super(attribute, ignoreBase);
        }

        public HundredMultiStatGetterAttribute(Attribute attribute, boolean ignoreBase, boolean ignoreBonuses) {
            super(attribute, ignoreBase, ignoreBonuses);
        }

        public HundredMultiStatGetterAttribute(Attribute attribute, boolean ignoreBase, boolean ignoreBonuses, double offset) {
            super(attribute, ignoreBase, ignoreBonuses, offset);
        }

        @Override
        public double getValue(@NotNull Player player, @NotNull ItemStack itemStack, @NotNull String slot, @NotNull String improvement) {
            return super.getValue(player, itemStack, slot, improvement) * 100;
        }

        @Override
        public double getValue(@NotNull Player player, @NotNull ItemStack itemStack) {
            return super.getValue(player, itemStack) * 100;
        }

        @Override
        public double getValue(@NotNull Player player, @NotNull ItemStack itemStack, @NotNull String slot) {
            return super.getValue(player, itemStack, slot) * 100;
        }
    }
}
