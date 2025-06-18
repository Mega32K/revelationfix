package com.mega.revelationfix.api.event.entity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraftforge.event.entity.living.LivingEvent;

import javax.annotation.Nullable;

public abstract class LivingHurtByTargetGoalEvent extends LivingEvent {
    private final HurtByTargetGoal goal;
    private final Mob mob;
    private boolean alertSameType;
    private int timestamp;
    private final Class<?>[] toIgnoreDamage;
    @Nullable
    private Class<?>[] toIgnoreAlert;

    public HurtByTargetGoal getGoal() {
        return goal;
    }

    public Mob getGoalMob() {
        return mob;
    }

    public boolean isAlertSameType() {
        return alertSameType;
    }

    public void setAlertSameType(boolean alertSameType) {
        this.alertSameType = alertSameType;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public void setToIgnoreAlert(@Nullable Class<?>[] toIgnoreAlert) {
        this.toIgnoreAlert = toIgnoreAlert;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public Class<?>[] getToIgnoreDamage() {
        return toIgnoreDamage;
    }

    @Nullable
    public Class<?>[] getToIgnoreAlert() {
        return toIgnoreAlert;
    }
    @Override
    public @Nullable LivingEntity getEntity() {
        return super.getEntity();
    }
    public LivingHurtByTargetGoalEvent(@Nullable LivingEntity entity, HurtByTargetGoal goal, Mob mob, boolean alertSameType, int timestamp, Class<?>[] toIgnoreDamage, @Nullable Class<?>[] toIgnoreAlert) {
        super(entity);
        this.goal = goal;
        this.mob = mob;
        this.alertSameType = alertSameType;
        this.timestamp = timestamp;
        this.toIgnoreDamage = toIgnoreDamage;
        this.toIgnoreAlert = toIgnoreAlert;
    }


    @HasResult
    public static class CanUse extends LivingHurtByTargetGoalEvent {
        public enum Phase {
            HEAD, BEFORE_IGNORE, TAIL;
        }

        public Phase getEventPhase() {
            return eventPhase;
        }

        public CanUse(LivingEntity entity, HurtByTargetGoal goal, Mob mob, boolean alertSameType, int timestamp, Class<?>[] toIgnoreDamage, @org.jetbrains.annotations.Nullable Class<?>[] toIgnoreAlert, Phase eventPhase) {
            super(entity, goal, mob, alertSameType, timestamp, toIgnoreDamage, toIgnoreAlert);
            this.eventPhase = eventPhase;
        }

        private final Phase eventPhase;
    }
}
