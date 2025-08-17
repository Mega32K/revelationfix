package com.mega.revelationfix.api.event.entity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraftforge.event.entity.living.LivingEvent;

import javax.annotation.Nullable;

public abstract class LivingHurtByTargetGoalEvent extends LivingEvent {
    private final HurtByTargetGoal goal;
    private final Mob mob;
    private final Class<?>[] toIgnoreDamage;
    private boolean alertSameType;
    private int timestamp;
    @Nullable
    private Class<?>[] toIgnoreAlert;

    public LivingHurtByTargetGoalEvent(@Nullable LivingEntity entity, HurtByTargetGoal goal, Mob mob, boolean alertSameType, int timestamp, Class<?>[] toIgnoreDamage, @Nullable Class<?>[] toIgnoreAlert) {
        super(entity);
        this.goal = goal;
        this.mob = mob;
        this.alertSameType = alertSameType;
        this.timestamp = timestamp;
        this.toIgnoreDamage = toIgnoreDamage;
        this.toIgnoreAlert = toIgnoreAlert;
    }

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

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public Class<?>[] getToIgnoreDamage() {
        return toIgnoreDamage;
    }

    @Nullable
    public Class<?>[] getToIgnoreAlert() {
        return toIgnoreAlert;
    }

    public void setToIgnoreAlert(@Nullable Class<?>[] toIgnoreAlert) {
        this.toIgnoreAlert = toIgnoreAlert;
    }

    @Override
    public @Nullable LivingEntity getEntity() {
        return super.getEntity();
    }

    @HasResult
    public static class CanUse extends LivingHurtByTargetGoalEvent {
        private final Phase eventPhase;

        public CanUse(LivingEntity entity, HurtByTargetGoal goal, Mob mob, boolean alertSameType, int timestamp, Class<?>[] toIgnoreDamage, @org.jetbrains.annotations.Nullable Class<?>[] toIgnoreAlert, Phase eventPhase) {
            super(entity, goal, mob, alertSameType, timestamp, toIgnoreDamage, toIgnoreAlert);
            this.eventPhase = eventPhase;
        }

        public Phase getEventPhase() {
            return eventPhase;
        }

        public enum Phase {
            HEAD, BEFORE_IGNORE, TAIL
        }
    }
}
