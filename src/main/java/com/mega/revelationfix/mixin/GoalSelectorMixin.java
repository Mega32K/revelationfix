package com.mega.revelationfix.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mega.revelationfix.common.compat.SafeClass;
import com.mega.revelationfix.safe.entity.GoalSelectorEC;
import com.mega.revelationfix.util.LivingEntityEC;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GoalSelector.class)
public class GoalSelectorMixin implements GoalSelectorEC {
    @Unique
    private @Nullable LivingEntity revelationfix$entity;

    @Override
    public @Nullable LivingEntity revelationfix$getEntity() {
        return this.revelationfix$entity;
    }

    @Override
    public void revelationfix$setEntity(LivingEntity entity) {
        this.revelationfix$entity = entity;
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/WrappedGoal;start()V"))
    private void redirectStart(WrappedGoal instance, Operation<Void> original) {
        if (revelationfix$entity != null && ((LivingEntityEC) revelationfix$entity).revelationfix$livingECData().banAnySpelling > 0) {
            if (!SafeClass.isSpellGoal(instance.getGoal())) {
                original.call(instance);
            }
        } else {
            original.call(instance);
        }
    }

    @WrapOperation(method = "tickRunningGoals", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/WrappedGoal;tick()V"))
    private void redirectTickRunningGoals(WrappedGoal instance, Operation<Void> original) {
        if (revelationfix$entity != null && ((LivingEntityEC) revelationfix$entity).revelationfix$livingECData().banAnySpelling > 0) {
            if (!SafeClass.isSpellGoal(instance.getGoal())) {
                original.call(instance);
            }
        } else {
            original.call(instance);
        }
    }
}
