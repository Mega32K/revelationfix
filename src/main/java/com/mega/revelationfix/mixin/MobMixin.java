package com.mega.revelationfix.mixin;

import com.mega.revelationfix.safe.entity.GoalSelectorEC;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity  {
    @Shadow @Final public GoalSelector goalSelector;

    @Shadow @Final public GoalSelector targetSelector;

    public MobMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @Inject(method = "serverAiStep", at = @At("HEAD"))
    private void writeEntityToGoalSelectors(CallbackInfo ci) {
        ((GoalSelectorEC) goalSelector).revelationfix$setEntity(this);
        ((GoalSelectorEC) targetSelector).revelationfix$setEntity(this);
    }
}
