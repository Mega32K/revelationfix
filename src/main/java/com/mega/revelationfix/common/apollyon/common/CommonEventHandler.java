package com.mega.revelationfix.common.apollyon.common;

import com.Polarice3.Goety.common.entities.boss.Apostle;
import com.Polarice3.Goety.common.entities.projectiles.DeathArrow;
import com.Polarice3.Goety.common.items.magic.MagicFocus;
import com.Polarice3.Goety.utils.ItemHelper;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.Goety.utils.WandUtil;
import com.mega.revelationfix.Revelationfix;
import com.mega.revelationfix.common.compat.FeModSafe;
import com.mega.revelationfix.common.compat.SafeClass;
import com.mega.revelationfix.common.compat.Wrapped2;
import com.mega.revelationfix.common.config.CommonConfig;
import com.mega.revelationfix.common.item.armor.ModArmorMaterials;
import com.mega.revelationfix.common.spell.nether.RevelationSpell;
import com.mega.revelationfix.safe.DeathArrowEC;
import com.mega.revelationfix.safe.EntityExpandedContext;
import com.mega.revelationfix.util.ATAHelper2;
import com.mega.revelationfix.util.LivingEntityEC;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import z1gned.goetyrevelation.util.ATAHelper;
import z1gned.goetyrevelation.util.ApollyonAbilityHelper;

@Mod.EventBusSubscriber(modid = Revelationfix.MODID)
public class CommonEventHandler {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void livingHealEvent(LivingHealEvent event) {
        if (event.getEntity().level.isClientSide) return;
        EntityExpandedContext livingEC = ((LivingEntityEC) event.getEntity()).revelationfix$livingECData();
        if (livingEC.banHealingTime > 0) {
            event.setAmount(0F);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void tickingEC(LivingEvent.LivingTickEvent event) {
        LivingEntity living = event.getEntity();
        if (living.level.isClientSide) return;
        EntityExpandedContext livingEC = ((LivingEntityEC) living).revelationfix$livingECData();
        if (livingEC.banHealingTime > 0) {
            livingEC.banHealingTime--;
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void haloInvulnerableTo(LivingAttackEvent event) {
        DamageSource source = event.getSource();
        //晋升环免疫部分伤害类型
        if (ATAHelper.hasHalo(event.getEntity())) {
            //爆炸 火焰 熔岩 窒息 摔落 挤压 溺水
            if (source.is(DamageTypeTags.IS_EXPLOSION) || source.is(DamageTypeTags.IS_FIRE) || source.is(DamageTypes.LAVA) || source.is(DamageTypes.IN_WALL) || source.is(DamageTypeTags.IS_FALL) || source.is(DamageTypes.CRAMMING) || source.is(DamageTypeTags.IS_DROWNING)) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void haloNoEffects(MobEffectEvent.Applicable event) {
        //终末环无视负面
        if (ATAHelper2.hasOdamane(event.getEntity())) {
            if (event.getEffectInstance().getEffect().getCategory() == MobEffectCategory.HARMFUL)
                event.setResult(Event.Result.DENY);
            FeModSafe.removeBanHealing(event);
            return;
        }//晋升环免疫效果
        if (ATAHelper.hasHalo(event.getEntity())) {
            boolean apo = (ItemHelper.armorSet(event.getEntity(), ModArmorMaterials.APOCALYPTIUM));
            MobEffect mobEffect = event.getEffectInstance().getEffect();
            if (mobEffect != MobEffects.NIGHT_VISION && mobEffect != MobEffects.BAD_OMEN) {
                if (apo && mobEffect.getCategory() == MobEffectCategory.HARMFUL)
                    event.setResult(Event.Result.DENY);
                if (!apo)
                    event.setResult(Event.Result.DENY);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void haloMaxDamage(LivingHurtEvent event) {
        //晋升环限伤
        DamageSource source = event.getSource();
        if (source.is(DamageTypes.FELL_OUT_OF_WORLD))
            return;
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player player) {
            if (MobUtil.isSpellCasting(player) && WandUtil.findFocus(player).getItem() instanceof MagicFocus magicFocus && magicFocus.spell instanceof RevelationSpell) {
                event.setAmount(event.getAmount() * 0.1F);
            }
            if (ATAHelper.hasHalo(player)) {
                if (event.getAmount() > CommonConfig.haloDamageCap)
                    event.setAmount((float) CommonConfig.haloDamageCap);
            }
        } else if (ATAHelper.hasHalo(entity)) {
            if (event.getAmount() > CommonConfig.haloDamageCap)
                event.setAmount((float) CommonConfig.haloDamageCap);
        }
    }

    @SubscribeEvent
    public static void deathArrowEffect(EntityJoinLevelEvent event) {
        if (event.getEntity().level.isClientSide) {
            if (event.getEntity() instanceof DeathArrow deathArrow) {
                ((DeathArrowEC) deathArrow).revelationfix$getTrailData().join(5);
            }
        }
    }

    @SubscribeEvent
    public static void apollyonChaiBanning(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getEntity();
        if (event.getTarget() instanceof Apostle apostle) {
            if (((ApollyonAbilityHelper) apostle).allTitlesApostle_1_20_1$isApollyon()) {
                if (SafeClass.isIAFLoaded()) {
                    Wrapped2.e1(event, player);
                }
            }
        }
    }

    @SubscribeEvent
    public static void apollyonChaiBanning2(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        if (SafeClass.isIAFLoaded()) {
            Wrapped2.e2(event, player);
        }
    }
}
