package com.mega.revelationfix.util;

import com.Polarice3.Goety.api.items.magic.ITotem;
import com.Polarice3.Goety.api.magic.ISpell;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.entities.boss.Apostle;
import com.Polarice3.Goety.common.magic.SpellStat;
import com.Polarice3.Goety.utils.SEHelper;
import com.Polarice3.Goety.utils.TotemFinder;
import com.Polarice3.Goety.utils.WandUtil;
import com.mega.revelationfix.common.block.blockentity.RuneReactorBlockEntity;
import com.mega.revelationfix.common.compat.Wrapped;
import com.mega.revelationfix.common.config.CommonConfig;
import com.mega.revelationfix.common.entity.binding.FakeSpellerEntity;
import com.mega.revelationfix.common.init.ModAttributes;
import com.mega.revelationfix.common.init.ModEffects;
import com.mega.revelationfix.safe.entity.EntityExpandedContext;
import com.mega.revelationfix.safe.mixinpart.goety.SpellStatEC;
import com.mega.revelationfix.util.asm.GoetyClassNodeProcessor;
import com.mega.revelationfix.util.entity.ATAHelper2;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import z1gned.goetyrevelation.util.ATAHelper;
import z1gned.goetyrevelation.util.ApollyonAbilityHelper;

public class EventUtil {
    public static final String EVENT_UTIL_CLASS = "com/mega/revelationfix/util/EventUtil";
    public static int increaseSouls(Player player, int souls) {
        double attributeV = player.getAttributeValue(ModAttributes.SOUL_INCREASE_EFFICIENCY.get());
        if (Math.abs(attributeV - 1.0D) > 0.0e-6) {
            souls = (int) (attributeV * souls);
        }
        return souls;
    }
    public static int decreaseSouls(Player player, int souls) {
        if (!SEHelper.getSEActive(player)) {
            double attributeV = player.getAttributeValue(ModAttributes.SOUL_DECREASE_EFFICIENCY.get());
            if (Math.abs(attributeV - 1.0D) > 0.0e-6) {
                souls = Math.max(0, (int) ((2D - attributeV) * souls));
            }
        }
        return souls;
    }
    public static int decreaseSESouls(Player player, int souls) {
        double attributeV = player.getAttributeValue(ModAttributes.SOUL_DECREASE_EFFICIENCY.get());
        if (Math.abs(attributeV - 1.0D) > 0.0e-6) {
            souls = Math.max(0, (int) ((2D - attributeV) * souls));
        }
        return souls;
    }
    public static int getLevels(Enchantment enchantment, LivingEntity livingEntity, int srcLevel) {
        if (enchantment == ModEnchantments.POTENCY.get()) {
            MobEffectInstance effect = livingEntity.getEffect(ModEffects.COUNTERSPELL.get());
            if (effect != null) {
                srcLevel = Math.max(0, ((effect.getAmplifier()+1)+1) / 2);
            }
        }
        return srcLevel;
    }
    public static LivingEntity modifyOwner(LivingEntity entity) {
        if (entity instanceof FakeSpellerEntity spellerEntity) {
            if (spellerEntity.getOwner() != null)
                entity = spellerEntity.getOwner();
        }
        return entity;
    }
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
            duration *= Math.max(0.1F, (2F - player.getAttributeValue(ModAttributes.CAST_DURATION.get())));
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
            Player player = Wrapped.clientPlayer();
            if (player != null)
                duration = Math.round(duration * (float)Math.max(0F, (2F - player.getAttributeValue(ModAttributes.SPELL_COOLDOWN.get()))));
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
        modifySpellStatsByRuneReactor(iSpell, worldIn, caster, staff, spellStat);
        iSpell.SpellResult(worldIn, caster, staff, spellStat);
    }

    public static void redirectUseSpell(ISpell iSpell, ServerLevel worldIn, LivingEntity caster, ItemStack staff, int castTime, SpellStat spellStat) {
        modifySpellStatsByRuneReactor(iSpell, worldIn, caster, staff, spellStat);
        iSpell.useSpell(worldIn, caster, staff, castTime, spellStat);
    }

    public static void redirectStartSpell(ISpell iSpell, ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat) {
        modifySpellStatsByRuneReactor(iSpell, worldIn, caster, staff, spellStat);
        iSpell.startSpell(worldIn, caster, staff, spellStat);
    }
    public static void redirectStopSpell(ISpell spell, ServerLevel worldIn, LivingEntity caster, ItemStack staff, ItemStack focus, int castTime, SpellStat spellStat) {
        modifySpellStatsByRuneReactor(spell, worldIn, caster, staff, spellStat);
        spell.stopSpell(worldIn, caster, staff, focus, castTime, spellStat);
    }

    /**
     * 被巫术反应台修改基本法术属性<br>
     * 这个方法应当优先被调用在改调用处
     */
    public static void modifySpellStatsByRuneReactor(ISpell spell, ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat src) {
        Player player = null;
        if (caster instanceof FakeSpellerEntity spellerEntity && spellerEntity.getOwner() instanceof Player) {
            SpellStatEC statItf = (SpellStatEC) src;
            if (!statItf.isModifiedByRuneReactor()) {
                if (worldIn.getBlockEntity(spellerEntity.getReactorPos()) instanceof RuneReactorBlockEntity blockEntity) {
                    blockEntity.modifySpellStats(src);
                    statItf.giveModifiedTag((byte) 1, true);
                }
            }
        }
    }

    /**
     * POTENCY因为数据问题转移到此处全局修改
     * 没有附魔方法调用的时候用于修改基本属性
     */
    public static void modifySpellStatsWithoutEnchantment(ISpell spell, ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat src) {
        Player player = null;
        if (caster instanceof Player)
            player = (Player) caster;
        else if (caster instanceof FakeSpellerEntity spellerEntity && spellerEntity.getOwner() instanceof Player) {
            player = (Player) spellerEntity.getOwner();
        }
        SpellStatEC statItf = (SpellStatEC) src;
        if (player != null && !statItf.isModifiedByAttributes()) {
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
                    src.setPotency(Math.round(src.getPotency() * (float) attributeValue));
                }
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        if (!WandUtil.enchantedFocus(caster)) {
            src.setDuration(spellStatISTORE(spell, worldIn, caster, staff, src.getDuration(), GoetyClassNodeProcessor.SpellStatField.DURATION));
            src.setRange(spellStatISTORE(spell, worldIn, caster, staff, src.getRange(), GoetyClassNodeProcessor.SpellStatField.RANGE));
            src.setBurning(spellStatISTORE(spell, worldIn, caster, staff, src.getBurning(), GoetyClassNodeProcessor.SpellStatField.BURNING));
            src.setVelocity(spellStatFSTORE(spell, worldIn, caster, staff, src.getVelocity(), GoetyClassNodeProcessor.SpellStatField.VELOCITY));
            src.setRadius(spellStatDSTORE(spell, worldIn, caster, staff, src.getRadius(), GoetyClassNodeProcessor.SpellStatField.RADIUS));
        }
        statItf.giveModifiedTag((byte) 0, true);
    }
    public static int spellStatISTORE(ISpell spell, ServerLevel worldIn, LivingEntity caster, ItemStack staff, int src, GoetyClassNodeProcessor.SpellStatField statField) {
        AttributeInstance attribute;
        if (caster == null) return src;
        switch (statField) {
            case RANGE -> {
                attribute = caster.getAttribute(ModAttributes.SPELL_RANGE.get());
                if (attribute != null)
                    src = (int) Math.round(src + attribute.getValue());
            }
            case BURNING -> {
                attribute = caster.getAttribute(ModAttributes.SPELL_BURNING.get());
                if (attribute != null)
                    src = (int) Math.round(src + attribute.getValue());
            }
            case DURATION -> {
                attribute = caster.getAttribute(ModAttributes.SPELL_DURATION.get());
                if (attribute != null)
                    src = (int) Math.round(src * attribute.getValue());
            }
        }
        return src;
    }
    public static float spellStatFSTORE(ISpell spell, ServerLevel worldIn, LivingEntity caster, ItemStack staff, float src, GoetyClassNodeProcessor.SpellStatField statField) {
        AttributeInstance attribute;
        if (caster == null) return src;
        if (statField == GoetyClassNodeProcessor.SpellStatField.VELOCITY) {
            attribute = caster.getAttribute(ModAttributes.SPELL_VELOCITY.get());
            if (attribute != null)
                src = src * (float) attribute.getValue();
        }
        return src;
    }
    public static double spellStatDSTORE(ISpell spell, ServerLevel worldIn, LivingEntity caster, ItemStack staff, double src, GoetyClassNodeProcessor.SpellStatField statField) {
        AttributeInstance attribute;
        if (caster == null) return src;
        if (statField == GoetyClassNodeProcessor.SpellStatField.RADIUS) {
            attribute = caster.getAttribute(ModAttributes.SPELL_RADIUS.get());
            if (attribute != null)
                src = src + (float) attribute.getValue();
        }
        return src;
    }
    public static float damageIncrease(LivingEntity living, DamageSource source, float amount) {
        if (source.getEntity() instanceof Apostle apostle) {
            ApollyonAbilityHelper abilityHelper = (ApollyonAbilityHelper) apostle;
            if (abilityHelper.allTitlesApostle_1_20_1$isApollyon()) {
                if (apostle.isInNether()) {
                    //下界亚二阶段默认双倍伤害
                    if (apostle.isSecondPhase())
                        amount *= CommonConfig.apollyon_nether_phase2DamageMultiplier;
                    //下界亚万众阶段默认四倍伤害
                    if (abilityHelper.allTitleApostle$getTitleNumber() == 12) {
                        amount *= CommonConfig.apollyon_nether_phaseGenesisDamageMultiplier;
                    }
                }
            }
        }
        return amount;
    }

}
