package com.mega.revelationfix.common.odamane.common;

import com.mega.endinglib.util.time.TimeStopEntityData;
import com.mega.revelationfix.common.compat.SafeClass;
import com.mega.revelationfix.common.config.ItemConfig;
import com.mega.revelationfix.common.init.GRItems;
import com.mega.revelationfix.safe.OdamanePlayerExpandedContext;
import com.mega.revelationfix.safe.entity.PlayerInterface;
import com.mega.revelationfix.util.entity.ATAHelper2;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * @author MegaDarkness<br>
 * 有关终末之环的花里胡哨能力的事件处理类
 */
@Mod.EventBusSubscriber
public class AbilityEvents {
    /**
     * 终末玩家非boss绝对友善，boss中立效果
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onMobChangeTarget(LivingChangeTargetEvent event) {
        if (event.getNewTarget() instanceof Player player && ATAHelper2.hasOdamane(player)) {
            LivingEntity thisE = event.getEntity();
            if (thisE.getType().is(Tags.EntityTypes.BOSSES)) {
                OdamanePlayerExpandedContext playerEC = ((PlayerInterface) player).revelationfix$odamaneHaloExpandedContext();
                event.setCanceled(!playerEC.recentlyAttackedBoss(thisE));
            } else event.setCanceled(true);
        }
    }

    /**
     * 监听玩家伤害boss事件
     */
    @SubscribeEvent
    public static void onPlayerAttackingEntity(AttackEntityEvent event) {
        if (!event.getEntity().level().isClientSide)
            ATAHelper2.getOdamaneEC(event.getEntity()).tryAttackBoss(event.getTarget());
    }

    /**
     * 此为防止事件触发阶段伤害类型免疫<br>
     * 佩戴终末之环的玩家免疫魔法，燃烧，坠落，仙人掌，溺水，闪电，爆炸，岩浆，挤压，铁砧坠落，动能（鞘翅撞击），冰冻细雪，窒息伤害。
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingAttack(LivingAttackEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (ATAHelper2.hasOdamane(player)) {
                if (ATAHelper2.getOdamaneEC(player).isInvulnerable())
                    event.setCanceled(true);
                if (ATAHelper2.getOdamaneEC(player).isInvulnerable() || OdamanePlayerExpandedContext.isInvulnerableTo(event.getSource()))
                    event.setCanceled(true);
            }
        }
    }

    /**
     * 此为免疫的伤害类型造成伤害 | 特殊伤害减免 | 饰品减伤<br>
     * 佩戴终末之环的玩家免疫魔法，燃烧，坠落，仙人掌，溺水，闪电，爆炸，岩浆，挤压，铁砧坠落，动能（鞘翅撞击），冰冻细雪，窒息伤害。
     * 佩戴终末之环的玩家对弹射物有额外85％的减伤，对虚空伤害拥有66.6％的减伤。
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingDamage(LivingDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (ATAHelper2.hasOdamane(player)) {
                if (OdamanePlayerExpandedContext.isInvulnerableTo(event.getSource()))
                    event.setCanceled(true);
                    //虚空伤害拥有66.6％的减伤。
                else if (event.getSource().is(DamageTypes.FELL_OUT_OF_WORLD))
                    event.setAmount(event.getAmount() * (1.0F - OdamanePlayerExpandedContext.VOID_DAMAGE_REDUCE));
                    //弹射物有额外85％的减伤
                else if (event.getSource().is(DamageTypeTags.IS_PROJECTILE) || event.getSource().getEntity() instanceof Projectile || event.getSource().getDirectEntity() instanceof Projectile)
                    event.setAmount(event.getAmount() * (1.0F - OdamanePlayerExpandedContext.PROJECTILE_DAMAGE_REDUCE));
                //维度意志玩家减伤
            }
            if (ATAHelper2.hasDimensionalWill(player)) {
                event.setAmount(event.getAmount() * (100 - ItemConfig.dimensionalWillResistance) / 100.0F);
            }
        }
    }

    /**
     * 终末玩家死亡时，且无冷却的时候尝试复活<br>
     * 失败时去{@link OdamanePlayerExpandedContext#reviveCheck()}尝试复活<br>
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onOdamanePlayerDeath(LivingDeathEvent event) {
        Entity entity = event.getEntity();
        Level level = entity.level();
        if (entity instanceof Player player && !level.isClientSide) {
            if (ATAHelper2.hasOdamane(player)) {
                OdamanePlayerExpandedContext context = ATAHelper2.getOdamaneEC(player);
                if (player.getRandom().nextFloat() < 0.85F) {
                    player.setHealth(1F);
                    player.heal(7.0F);
                    player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 40, 126));
                    level.broadcastEntityEvent(player, OdamanePlayerExpandedContext.REVIVE_EVENT);
                    event.setCanceled(true);
                }
                if (context.getNextReviveCooldowns() <= 0) {
                    event.setCanceled(true);
                    player.setHealth(player.getMaxHealth() * 0.75F);
                    if (player.getHealth() > 0) {
                        context.onRevive();
                        level.broadcastEntityEvent(player, OdamanePlayerExpandedContext.ODAMANE_REVIVE_EVENT);
                        if (!player.getCooldowns().isOnCooldown(GRItems.HALO_OF_THE_END))
                            SafeClass.enableTimeStop(player, true, 300);
                    }
                }
            } else if (ATAHelper2.hasDimensionalWill(player)) {
                if (player.getRandom().nextFloat() < ItemConfig.dimensionalWillDeathEscape / 100.0F) {
                    event.setCanceled(true);
                    player.setHealth(1F);
                    player.heal(7.0F);
                    player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 40, 126));
                    level.broadcastEntityEvent(player, OdamanePlayerExpandedContext.REVIVE_EVENT);
                }
            }
        }
    }
    /**
     * 时停表的固定伤害
     */
    @SubscribeEvent
    public static void eternalWatchDamage(LivingDamageEvent event) {
        if (event.getSource().getEntity() instanceof LivingEntity living && ATAHelper2.hasEternalWatch(living) && TimeStopEntityData.getTimeStopCount(living) > 1) {
            event.setAmount(event.getAmount() + 10.0F);
            TimeStopEntityData.setTimeStopCount(living, 1);
        }
    }
}
