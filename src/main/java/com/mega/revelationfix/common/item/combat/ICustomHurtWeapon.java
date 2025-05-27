package com.mega.revelationfix.common.item.combat;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.EventPriority;

public interface ICustomHurtWeapon {
    default void onAttack(ItemStack itemStack, LivingAttackEvent event) {

    }

    default void onHurt(ItemStack itemStack, LivingHurtEvent event) {

    }

    default void onDamage(ItemStack itemStack, LivingDamageEvent event) {

    }

    default void onAttackEntity(ItemStack stack, AttackEntityEvent event) {

    }

    default void onDeath(ItemStack itemStack, LivingDeathEvent event, EventPriority priority) {

    }
}
