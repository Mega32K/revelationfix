package com.mega.revelationfix.mixin;

import com.mega.revelationfix.api.event.entity.LivingHurtByTargetGoalEvent;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(HurtByTargetGoal.class)
public abstract class HurtByTargetGoalMixin extends TargetGoal {
    @Shadow private boolean alertSameType;

    @Shadow private int timestamp;

    @Shadow @Final private Class<?>[] toIgnoreDamage;

    @Shadow @Nullable private Class<?>[] toIgnoreAlert;

    HurtByTargetGoalMixin(Mob p_26140_, boolean p_26141_) {
        super(p_26140_, p_26141_);
    }

    @Inject(method = "canUse", at = @At("HEAD"), cancellable = true)
    private void canUse$head(CallbackInfoReturnable<Boolean> cir) {
        LivingHurtByTargetGoalEvent.CanUse event = new LivingHurtByTargetGoalEvent.CanUse(this.mob.getLastHurtByMob(), ((HurtByTargetGoal) (Object)this), this.mob, this.alertSameType, this.timestamp, this.toIgnoreDamage, this.toIgnoreAlert, LivingHurtByTargetGoalEvent.CanUse.Phase.HEAD);
        MinecraftForge.EVENT_BUS.post(event);
        this.timestamp = event.getTimestamp();
        this.alertSameType = event.isAlertSameType();
        this.toIgnoreAlert = event.getToIgnoreAlert();
        Event.Result result = event.getResult();
        if (result == Event.Result.DENY)
            cir.setReturnValue(false);
        else if (result == Event.Result.ALLOW)
            cir.setReturnValue(true);
    }
    @Inject(method = "canUse", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/ai/goal/target/HurtByTargetGoal;toIgnoreDamage:[Ljava/lang/Class;", shift = At.Shift.BEFORE), cancellable = true)
    private void canUse$beforeIgnore(CallbackInfoReturnable<Boolean> cir) {
        LivingHurtByTargetGoalEvent.CanUse event = new LivingHurtByTargetGoalEvent.CanUse(this.mob.getLastHurtByMob(), ((HurtByTargetGoal) (Object)this), this.mob, this.alertSameType, this.timestamp, this.toIgnoreDamage, this.toIgnoreAlert, LivingHurtByTargetGoalEvent.CanUse.Phase.BEFORE_IGNORE);
        MinecraftForge.EVENT_BUS.post(event);
        this.timestamp = event.getTimestamp();
        this.alertSameType = event.isAlertSameType();
        this.toIgnoreAlert = event.getToIgnoreAlert();
        Event.Result result = event.getResult();
        if (result == Event.Result.DENY)
            cir.setReturnValue(false);
        else if (result == Event.Result.ALLOW)
            cir.setReturnValue(true);
    }
    @Inject(method = "canUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/target/HurtByTargetGoal;canAttack(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/ai/targeting/TargetingConditions;)Z", shift = At.Shift.BEFORE), cancellable = true)
    private void canUse$tail(CallbackInfoReturnable<Boolean> cir) {
        LivingHurtByTargetGoalEvent.CanUse event = new LivingHurtByTargetGoalEvent.CanUse(this.mob.getLastHurtByMob(), ((HurtByTargetGoal) (Object)this), this.mob, this.alertSameType, this.timestamp, this.toIgnoreDamage, this.toIgnoreAlert, LivingHurtByTargetGoalEvent.CanUse.Phase.TAIL);
        MinecraftForge.EVENT_BUS.post(event);
        this.timestamp = event.getTimestamp();
        this.alertSameType = event.isAlertSameType();
        this.toIgnoreAlert = event.getToIgnoreAlert();
        Event.Result result = event.getResult();
        if (result == Event.Result.DENY)
            cir.setReturnValue(false);
        else if (result == Event.Result.ALLOW)
            cir.setReturnValue(true);
    }
}
