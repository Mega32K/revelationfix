package com.mega.revelationfix.common.compat.tetra;

import com.Polarice3.Goety.api.magic.SpellType;
import com.mega.revelationfix.common.compat.SafeClass;
import com.mega.revelationfix.common.compat.tetra.effect.*;
import com.mega.revelationfix.common.init.ModAttributes;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import se.mickelus.tetra.blocks.workbench.gui.WorkbenchStatsGui;
import se.mickelus.tetra.gui.stats.bar.GuiStatBar;
import se.mickelus.tetra.gui.stats.getter.IStatGetter;
import se.mickelus.tetra.gui.stats.getter.LabelGetterBasic;
import se.mickelus.tetra.gui.stats.getter.StatFormat;
import se.mickelus.tetra.gui.stats.getter.StatGetterAttribute;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloStatsGui;

import java.util.Map;
import java.util.UUID;

public class TetraWrapped {
    public static ItemStack setupSchematic(String key, String details, String[] schematics, boolean isIntricate, int material, int tint, Integer... glyphs) {
        return ScrollHelper.setupSchematic(key, details, schematics, isIntricate, material, tint, glyphs);
    }

    public static ItemStack setupTreatise(String key, boolean isIntricate, int material, int tint, Integer... glyphs) {
        return ScrollHelper.setupTreatise(key, isIntricate, material, tint, glyphs);
    }

    public static void registerEffects() {
        DoomEffect.init();
        DeicideEffect.init();
        FadingEffect.init();
        SoulRepairEffect.init();
        CursedBladeEffect.init();
        IceBladeEffect.init();
        PositionBladeEffect.init();
        BeelzebubEffect.init();
        MysteriousBladeEffect.init();
        VizirEffect.init();
        ShadowWalkEffect.init();
        RulerOfNocturnalEffect.init();
        MiscSpellAttributeEffect.init();
        createPercentAttributeBar(ModAttributes.spellAttributes.get(SpellType.NONE).get(), "none_power");
        createPercentAttributeBar(ModAttributes.spellAttributes.get(SpellType.NECROMANCY).get(), "necromancy_power");
        createPercentAttributeBar(ModAttributes.spellAttributes.get(SpellType.STORM).get(), "storm_power");
        createPercentAttributeBar(ModAttributes.spellAttributes.get(SpellType.NETHER).get(), "nether_power");
        createPercentAttributeBar(ModAttributes.spellAttributes.get(SpellType.ILL).get(), "ill_power");
        createPercentAttributeBar(ModAttributes.spellAttributes.get(SpellType.FROST).get(), "frost_power");
        createPercentAttributeBar(ModAttributes.spellAttributes.get(SpellType.GEOMANCY).get(), "geomancy_power");
        createPercentAttributeBar(ModAttributes.spellAttributes.get(SpellType.WILD).get(), "wild_power");
        createPercentAttributeBar(ModAttributes.spellAttributes.get(SpellType.WIND).get(), "wind_power");
        createPercentAttributeBar(ModAttributes.spellAttributes.get(SpellType.ABYSS).get(), "abyss_power");
        createPercentAttributeBar(ModAttributes.spellAttributes.get(SpellType.VOID).get(), "void_power");
    }

    @OnlyIn(Dist.CLIENT)
    private static void createPercentAttributeBar(Attribute attribute, String languageKey) {
        createPercentAttributeBar(attribute, 10.0D, languageKey);
    }

    @OnlyIn(Dist.CLIENT)
    private static void createPercentAttributeBar(Attribute attribute, double max, String languageKey) {
        try {
            StatGetterAttribute statGetterPercentAttribute = new StatGetterAttribute(attribute, true);
            GuiStatBar statBar = new GuiStatBar(0, 0, 59, attribute.getDescriptionId(),
                    0.0D, max, false, statGetterPercentAttribute, LabelGetterBasic.decimalLabel,
                    TetraVersionCompat.createTGM("goety_revelation.effect." + languageKey + ".tooltip", new IStatGetter[]{statGetterPercentAttribute}, StatFormat.oneDecimal));
            WorkbenchStatsGui.addBar(statBar);
            HoloStatsGui.addBar(statBar);
        } catch (Throwable throwable) {
        }
    }

    public static void registerEvents() {
        MinecraftForge.EVENT_BUS.register(new DeicideEffect());
        MinecraftForge.EVENT_BUS.register(JesusDeathEvent.class);
        MinecraftForge.EVENT_BUS.register(new FadingEffect());
        MinecraftForge.EVENT_BUS.register(new CursedBladeEffect());
        MinecraftForge.EVENT_BUS.register(new IceBladeEffect());
        MinecraftForge.EVENT_BUS.register(new PositionBladeEffect());
        MinecraftForge.EVENT_BUS.register(new BeelzebubEffect());
        MinecraftForge.EVENT_BUS.register(new MysteriousBladeEffect());
        MinecraftForge.EVENT_BUS.register(new VizirEffect());
        MinecraftForge.EVENT_BUS.register(new ShadowWalkEffect());
        MinecraftForge.EVENT_BUS.register(new RulerOfNocturnalEffect());
    }

    public static void doomItemEffect(LivingEntity living, Entity target) {
        if (!SafeClass.isTetraLoaded()) return;
        DoomEffect.doPostHurt(living, target);
    }

    public static Object2ObjectOpenHashMap<Attribute, UUID> getAttributes() {
        return FadingEffect.getAttributes();
    }
}
