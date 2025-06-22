package com.mega.revelationfix.common.compat;

import com.Polarice3.Goety.common.entities.boss.Apostle;
import com.Polarice3.Goety.common.entities.hostile.cultists.Heretic;
import com.Polarice3.Goety.common.entities.hostile.cultists.SpellCastingCultist;
import com.Polarice3.Goety.common.entities.neutral.AbstractNecromancer;
import com.mega.endinglib.client.RendererUtils;
import com.mega.endinglib.util.time.TimeStopEntityData;
import com.mega.endinglib.util.time.TimeStopUtils;
import com.mega.revelationfix.common.compat.fantasy_ending.FeSafeClass;
import com.mega.revelationfix.common.compat.ironspell.IronSpellbooksSafeClass;
import com.mega.revelationfix.common.compat.tetra.TetraWrapped;
import com.mega.revelationfix.common.config.ModpackCommonConfig;
import com.mega.revelationfix.util.EarlyConfig;
import net.irisshaders.iris.api.v0.IrisApi;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModList;
import z1gned.goetyrevelation.goal.UseSpellGoal;
import z1gned.goetyrevelation.util.ApollyonAbilityHelper;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

public class SafeClass {
    private static boolean lastUsingShader;
    private static boolean usingShader;
    private static final Date date = new Date();
    private static int yearDate;
    private static int fantasyEndingLoaded = -1;
    private static int iafLoaded = -1;
    private static int modernuiLoaded = -1;
    private static int irisLoaded = -1;
    private static int eeeabLoaded = -1;
    private static int tetraLoaded = -1;
    private static int enigmaticLegacyLoaded = -1;
    private static int yes_steve_modelLoaded = -1;
    private static int youkaishomecoming_Loaded = -1;
    private static int kubejs_loaded = -1;
    private static int irons_spellbooks_loaded = -1;
    public static int yearDay() {
        if (yearDate == 0) {
            yearDate = Integer.parseInt(String.format("%s%s", date.getMonth() + 1, date.getDate()));
        }
        return yearDate;
    }
    public static boolean isIronSpellbookslLoaded() {
        if (irons_spellbooks_loaded == -1) {
            irons_spellbooks_loaded = (EarlyConfig.modIds.contains("irons_spellbooks") || ModList.get().isLoaded("irons_spellbooks")) ? 1 : 2;
        }
        return irons_spellbooks_loaded == 1;
    }
    public static boolean isKJSLoaded() {
        if (kubejs_loaded == -1) {
            kubejs_loaded = (EarlyConfig.modIds.contains("kubejs") || ModList.get().isLoaded("kubejs")) ? 1 : 2;
        }
        return kubejs_loaded == 1;
    }
    public static boolean isYoukaiLoaded() {
        if (youkaishomecoming_Loaded == -1) {
            youkaishomecoming_Loaded = (EarlyConfig.modIds.contains("youkaishomecoming") || ModList.get().isLoaded("youkaishomecoming")) ? 1 : 2;
        }
        return youkaishomecoming_Loaded == 1;
    }

    public static boolean isYSMLoaded() {
        if (yes_steve_modelLoaded == -1) {
            yes_steve_modelLoaded = (EarlyConfig.modIds.contains("yes_steve_model") || ModList.get().isLoaded("yes_steve_model")) ? 1 : 2;
        }
        return yes_steve_modelLoaded == 1;
    }

    public static boolean isEnigmaticLegacyLoaded() {
        if (enigmaticLegacyLoaded == -1) {
            enigmaticLegacyLoaded = (EarlyConfig.modIds.contains("enigmaticlegacy") || ModList.get().isLoaded("enigmaticlegacy")) ? 1 : 2;
        }
        return enigmaticLegacyLoaded == 1;
    }

    public static boolean isTetraLoaded() {
        if (tetraLoaded == -1) {
            tetraLoaded = (EarlyConfig.modIds.contains("tetra") || ModList.get().isLoaded("tetra")) ? 1 : 2;
        }
        return tetraLoaded == 1;
    }

    public static boolean isEEEABLoaded() {
        if (eeeabLoaded == -1) {
            eeeabLoaded = ModList.get().isLoaded("eeeabsmobs") ? 1 : 2;
        }
        return eeeabLoaded == 1;
    }

    public static boolean isFantasyEndingLoaded() {
        if (fantasyEndingLoaded == -1) {
            fantasyEndingLoaded = (EarlyConfig.modIds.contains("fantasy_ending") || ModList.get().isLoaded("fantasy_ending")) ? 1 : 2;
        }
        return fantasyEndingLoaded == 1;
    }

    public static boolean isIAFLoaded() {
        if (iafLoaded == -1) {
            iafLoaded = ModList.get().isLoaded("iceandfire") ? 1 : 2;
        }
        return iafLoaded == 1;
    }

    public static boolean isModernUILoaded() {
        if (modernuiLoaded == -1) {
            modernuiLoaded = ModList.get().isLoaded("modernui") ? 1 : 2;
        }
        return modernuiLoaded == 1;
    }

    public static boolean isIrisLoaded() {
        if (irisLoaded == -1) {
            irisLoaded = ModList.get().isLoaded("oculus") ? 1 : 2;
        }
        return irisLoaded == 1;
    }

    public static boolean usingShaderPack() {
        if (isIrisLoaded()) {
            usingShader = IrisApi.getInstance().isShaderPackInUse();
            if (usingShader != lastUsingShader) {
                Wrapped.onShaderModeChange();
            }
            lastUsingShader = usingShader;
            return usingShader;
        }
        else return false;
    }

    public static boolean isDoom(Apostle apostle) {
        if (!ModpackCommonConfig.apollyonModpackMode || !apostle.isInNether())
            return apostle.getHealth() / apostle.getMaxHealth() <= (1 / 14F);
        else return apostle.getHealth() / apostle.getMaxHealth() <= ModpackCommonConfig.netherTheDoomPercent;
    }

    public static boolean isNetherDoomApollyon(Apostle apostle) {
        return ((ApollyonAbilityHelper) apostle).allTitlesApostle_1_20_1$isApollyon() && apostle.isSecondPhase() && apostle.isInNether() && SafeClass.isDoom(apostle);
    }

    @OnlyIn(Dist.CLIENT)
    public static boolean isNetherApollyonLoaded(Predicate<Apostle> p) {
        return Wrapped.isNetherApollyonLoaded(p);
    }

    public static void enableTimeStop(LivingEntity srcE, boolean z, int ticks) {
        TimeStopUtils.use(z, srcE, true, ticks);
    }

    public static void enableTimeStop(LivingEntity srcE, boolean z) {
        TimeStopEntityData.setTimeStopCount(srcE, !z ? 0 : 300);
        TimeStopUtils.use(z, srcE);
    }

    public static int getTimeStopCount(LivingEntity living) {
        return TimeStopEntityData.getTimeStopCount(living);
    }

    public static boolean isClientTimeStop() {
        return TimeStopUtils.isTimeStop && RendererUtils.isTimeStop_andSameDimension;
    }
    public static boolean isTimeStop(ServerLevel serverLevel) {
        return TimeStopUtils.isTimeStop && TimeStopUtils.andSameDimension(serverLevel);
    }
    public static boolean isFieldTimeStop() {
        return TimeStopUtils.isTimeStop;
    }

    public static ItemStack setupSchematics(String key, String details, String[] schematics, boolean isIntricate, int material, int tint, Integer... glyphs) {
        return TetraWrapped.setupSchematic(key, details, schematics, isIntricate, material, tint, glyphs);
    }

    public static ItemStack setupTreatise(String key, boolean isIntricate, int material, int tint, Integer... glyphs) {
        return TetraWrapped.setupTreatise(key, isIntricate, material, tint, glyphs);
    }

    public static void registerTetraItemEffects() {
        TetraWrapped.registerEffects();
    }

    public static void registerTetraEvents() {
        TetraWrapped.registerEvents();
    }

    public static void doomItemEffect(LivingEntity living, Entity target) {
        if (!SafeClass.isTetraLoaded()) return;
        TetraWrapped.doomItemEffect(living, target);
    }

    public static Map<Attribute, UUID> getAttributes() {
        return TetraWrapped.getAttributes();
    }
    public static boolean isSpellGoal(Goal goal) {
        if (goal instanceof UseSpellGoal || goal instanceof SpellCastingCultist.UseSpellGoal || goal instanceof SpellCastingCultist.CastingASpellGoal || goal instanceof AbstractNecromancer.NecromancerRangedGoal || goal instanceof Heretic.CastingGoal) {
            return true;
        }
        if (isIronSpellbookslLoaded()) {
            if (IronSpellbooksSafeClass.isSpellGoal(goal))
                return true;
        }
        String classSimpleName = goal.getClass().getSimpleName();
        String className = goal.getClass().getName();
        return classSimpleName.contains("Spell") || classSimpleName.contains("Summon") || classSimpleName.equals("ShockGoal");
    }
}
