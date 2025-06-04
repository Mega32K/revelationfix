package com.mega.revelationfix.util;

import com.Polarice3.Goety.api.magic.ISpell;
import com.Polarice3.Goety.common.entities.boss.Apostle;
import com.Polarice3.Goety.common.magic.SpellStat;
import com.mega.revelationfix.common.block.blockentity.RuneReactorBlockEntity;
import com.mega.revelationfix.common.compat.Wrapped;
import com.mega.revelationfix.common.config.CommonConfig;
import com.mega.revelationfix.common.entity.FakeSpellerEntity;
import com.mega.revelationfix.common.init.ModAttributes;
import com.mega.revelationfix.safe.entity.EntityExpandedContext;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import z1gned.goetyrevelation.util.ATAHelper;
import z1gned.goetyrevelation.util.ApollyonAbilityHelper;

public class EventUtil {

    public static void tryCaughtThrowable(Throwable throwable) {
        System.exit(-1);
        RevelationFixMixinPlugin.LOGGER.error("RevelationBusFix try caught :" + throwable.getMessage());
        RevelationFixMixinPlugin.LOGGER.throwing(throwable);
    }

    //终末之后会将法术吟唱速度*8
    public static int castDuration(int duration, ISpell spell, LivingEntity caster) {
        if (caster instanceof FakeSpellerEntity spellerEntity && spellerEntity.getOwner() != null)
            caster = spellerEntity.getOwner();
        if (caster instanceof Player player) {
            if (ATAHelper2.hasOdamane(player))
                duration = (int) (duration / Math.max(8.0F, CommonConfig.haloSpellCastingSpeed * 2.0F));
            else if (ATAHelper.hasHalo(player))
                duration = (int) (duration / CommonConfig.haloSpellCastingSpeed);
            duration *= (2F - player.getAttributeValue(ModAttributes.CAST_DURATION.get()));
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
    public static void redirectSpellResult(ISpell iSpell, ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat) {
        EntityExpandedContext expandedContext = ((LivingEntityEC) caster).revelationfix$livingECData();
        if (expandedContext.banAnySpelling > 0) {
            if (caster instanceof ServerPlayer player) {
                player.displayClientMessage(Component.translatable("info.goety_revelation.no_spells").withStyle(ChatFormatting.RED), true);
            }
            return;
        }
        spellStat = modifySpellStats(iSpell, worldIn, caster, staff, spellStat);
        iSpell.SpellResult(worldIn, caster, staff, spellStat);
    }
    public static SpellStat modifySpellStats(ISpell spell, ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat src) {
        Player player = null;
        boolean useSpeller = false;
        if (caster instanceof Player)
            player = (Player) caster;
        else if (caster instanceof FakeSpellerEntity spellerEntity && spellerEntity.getOwner() instanceof Player) {
            player = (Player) spellerEntity.getOwner();
            useSpeller = true;
        }
            if (player != null) {
                try {
                    {
                        double attributeValue = player.getAttributeValue(ModAttributes.spellAttribute(spell.getSpellType()));
                        if (attributeValue > 0.0001D) {
                            src.setPotency((int) (src.getPotency() + Math.round(attributeValue)));
                        }
                    }
                    {
                        double attributeValue = player.getAttributeValue(ModAttributes.SPELL_POWER.get());
                        src.setPotency((int) (src.getPotency() + Math.round(attributeValue)));
                    }
                    {
                        double attributeValue = player.getAttributeValue(ModAttributes.SPELL_POWER_MULTIPLIER.get());
                        src.setPotency(Math.round(src.getPotency() * (float)attributeValue));
                    }
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
            if (useSpeller) {
                FakeSpellerEntity spellerEntity = (FakeSpellerEntity) caster;
                if (worldIn.getBlockEntity(spellerEntity.getReactorPos()) instanceof RuneReactorBlockEntity blockEntity) {
                    src = blockEntity.modifySpellStats(src);
                }
            }
            return src;
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
