package com.mega.revelationfix.common.compat;

import com.Polarice3.Goety.common.entities.boss.Apostle;
import com.mega.revelationfix.common.client.RendererUtils;
import com.mega.revelationfix.common.compat.tetra.TetraWrapped;
import com.mega.revelationfix.common.config.ModpackCommonConfig;
import com.mega.revelationfix.util.EarlyConfig;
import com.mega.revelationfix.util.time.TimeStopEntityData;
import com.mega.revelationfix.util.time.TimeStopUtils;
import net.irisshaders.iris.api.v0.IrisApi;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModList;
import z1gned.goetyrevelation.util.ApollyonAbilityHelper;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

public class SafeClass {
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

    public static int yearDay() {
        if (yearDate == 0) {
            yearDate = Integer.parseInt(String.format("%s%s", date.getMonth() + 1, date.getDate()));
        }
        return yearDate;
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
        if (isIrisLoaded())
            return IrisApi.getInstance().isShaderPackInUse();
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
        if (isFantasyEndingLoaded()) {
            Wrapped.use(z, srcE, true, ticks);
        } else TimeStopUtils.use(z, srcE, true, ticks);
    }

    public static void enableTimeStop(LivingEntity srcE, boolean z) {
        if (isFantasyEndingLoaded()) {
            Wrapped.use(z, srcE, true, 180);
        } else {
            TimeStopEntityData.setTimeStopCount(srcE, !z ? 0 : 300);
            TimeStopUtils.use(z, srcE);
        }
    }

    public static int getTimeStopCount(LivingEntity living) {
        return isFantasyEndingLoaded() ? Wrapped.getTimeStopCount(living) : TimeStopEntityData.getTimeStopCount(living);
    }

    public static boolean isClientTimeStop() {
        if (isFantasyEndingLoaded()) {
            return Wrapped.isClientTimeStop();
        } else {
            return TimeStopUtils.isTimeStop && RendererUtils.isTimeStop_andSameDimension;
        }
    }

    public static boolean isFieldTimeStop() {
        if (isFantasyEndingLoaded()) {
            return Wrapped.isFieldTimeStop();
        } else {
            return TimeStopUtils.isTimeStop;
        }
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
}
