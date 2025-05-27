package com.mega.revelationfix.util;

import com.Polarice3.Goety.api.magic.ISpell;
import com.Polarice3.Goety.common.entities.boss.Apostle;
import com.Polarice3.Goety.common.magic.SpellStat;
import com.mega.revelationfix.common.compat.Wrapped;
import com.mega.revelationfix.common.config.CommonConfig;
import com.mega.revelationfix.common.init.ModAttributes;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import se.mickelus.tetra.module.data.SynergyData;
import z1gned.goetyrevelation.util.ATAHelper;
import z1gned.goetyrevelation.util.ApollyonAbilityHelper;

import java.util.Arrays;

public class EventUtil {
    public static Object2BooleanOpenHashMap<Class<?>> canUseAttributesSpells = new Object2BooleanOpenHashMap<>();

    public static void tryCaughtThrowable(Throwable throwable) {
        System.exit(-1);
        RevelationFixMixinPlugin.LOGGER.error("RevelationBusFix try caught :" + throwable.getMessage());
        RevelationFixMixinPlugin.LOGGER.throwing(throwable);
    }

    //终末之后会将法术吟唱速度*8
    public static int castDuration(int duration, ISpell spell, LivingEntity caster) {
        if (caster instanceof Player player) {
            if (ATAHelper2.hasOdamane(player))
                duration = (int) (duration / Math.max(8.0F, CommonConfig.haloSpellCastingSpeed * 2.0F));
            else if (ATAHelper.hasHalo(player))
                duration = (int) (duration / CommonConfig.haloSpellCastingSpeed);
        }
        return duration;
    }

    //终末玩家极快冷却
    public static int spellCooldown(int duration, ISpell spell) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            if (Wrapped.isClientPlayerHalo()) {
                duration = (int) (duration / CommonConfig.haloSpellCooldownReduction);
            }
            if (Wrapped.isClientPlayerOdamane()) {
                duration = Math.min(2, duration);
            }
        }
        return duration;
    }

    public static void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, ISpell spell) {
        Class<?> spellClass = spell.getClass();
        if (!canUseAttributesSpells.containsKey(spellClass)) {
            boolean shouldUseAttribtues = true;
            try {
                spell.getClass().getDeclaredMethod("SpellResult", ServerLevel.class, LivingEntity.class, ItemStack.class, SpellStat.class);
            } catch (NoSuchMethodException error) {
                shouldUseAttribtues = false;
            }
            canUseAttributesSpells.put(spellClass, shouldUseAttribtues);
        }
        boolean shouldUseAttribtues = canUseAttributesSpells.getBoolean(spellClass);
        if (shouldUseAttribtues) {
            SpellStat src = spell.defaultStats();
            SpellStat spellStat = new SpellStat(src.potency, src.duration, src.range, src.radius, src.burning, src.velocity);
            if (caster instanceof Player player) {
                try {
                    {
                        double attributeValue = player.getAttributeValue(ModAttributes.spellAttribute(spell.getSpellType()));
                        if (attributeValue > 0.0001D) {
                            spellStat.setPotency((int) (spellStat.getPotency() + Math.round(attributeValue)));
                        }
                    }
                    {
                        double attributeValue = player.getAttributeValue(ModAttributes.SPELL_POWER.get()) + 1;
                        spellStat.setPotency(Math.round(spellStat.getPotency() * (float)attributeValue));
                    }
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
            spell.SpellResult(worldIn, caster, staff, spellStat);
        } else spell.SpellResult(worldIn, caster, staff);
    }

    public static float damageIncrease(LivingEntity living, DamageSource source, float amount) {
        if (source.getEntity() instanceof Apostle apostle) {
            ApollyonAbilityHelper abilityHelper = (ApollyonAbilityHelper) apostle;
            if (abilityHelper.allTitlesApostle_1_20_1$isApollyon()) {
                if (apostle.isInNether()) {
                    //下界亚二阶段双倍伤害
                    if (apostle.isSecondPhase())
                        amount *= 2.0F;
                    //下界亚万众阶段四倍伤害
                    if (abilityHelper.allTitleApostle$getTitleNumber() == 12) {
                        amount *= 4.0F;
                    }
                }
            }
        }
        return amount;
    }
}
