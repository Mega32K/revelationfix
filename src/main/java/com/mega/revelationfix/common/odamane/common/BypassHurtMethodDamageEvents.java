package com.mega.revelationfix.common.odamane.common;

import com.mega.revelationfix.safe.DamageSourceInterface;
import com.mega.revelationfix.util.entity.ATAHelper2;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * 实现越过hurt限伤的第二部分实现<br>
 * 第一部分见:<br>
 * {@link com.mega.revelationfix.mixin.DamageSourcesMixin}<br>
 * {@link com.mega.revelationfix.mixin.DamageSourceMixin}<br>
 * {@link com.mega.revelationfix.mixin.LivingDamageMixin}<br>
 * {@link com.mega.revelationfix.common.effect.QuietusEffect#quietusArmorAbility(LivingEntity, float, float, DamageSource)}<br>
 * {@link com.mega.revelationfix.common.effect.QuietusEffect#quietusEnchantmentAbility(LivingEntity, float, float, DamageSource, Pair)}<br>
 */
@Mod.EventBusSubscriber
public class BypassHurtMethodDamageEvents {
    /**
     * 为终末玩家附加fe power
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingAttack(LivingAttackEvent event) {
        if (isSourceOdamane(event.getSource()))
            ((DamageSourceInterface) event.getSource()).revelationfix$fePower(true);
    }

    /**
     * 为终末玩家附加fe power
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingHurt(LivingHurtEvent event) {
        if (isSourceOdamane(event.getSource()))
            ((DamageSourceInterface) event.getSource()).revelationfix$fePower(true);
    }

    /**
     * 为终末玩家附加fe power
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingHurt(LivingDamageEvent event) {
        if (isSourceOdamane(event.getSource()))
            ((DamageSourceInterface) event.getSource()).revelationfix$fePower(true);
    }

    private static boolean isSourceOdamane(DamageSource damageSource) {
        if (damageSource.getEntity() instanceof Player living && ATAHelper2.hasOdamane(living)) {
            return true;
        } else return damageSource.getDirectEntity() instanceof Player living && ATAHelper2.hasOdamane(living);
    }
}
