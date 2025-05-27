package com.mega.revelationfix.mixin.goety;

import com.Polarice3.Goety.common.entities.hostile.cultists.Heretic;
import com.mega.revelationfix.common.entity.cultists.HereticServant;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Heretic.class)
public class HereticMixin {
    @Redirect(method = "registerGoals", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/GoalSelector;addGoal(ILnet/minecraft/world/entity/ai/goal/Goal;)V", ordinal = 2))
    private void redirectCastingSpell(GoalSelector instance, int p_25353_, Goal p_25354_) {
        if ((Object) this instanceof HereticServant servant)
            instance.addGoal(p_25353_, new HereticServant.ChantAtTargetGoal(servant));
        else instance.addGoal(p_25353_, p_25354_);
    }
}
