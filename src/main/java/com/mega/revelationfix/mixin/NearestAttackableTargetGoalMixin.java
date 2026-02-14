package com.mega.revelationfix.mixin;

import com.mega.endinglib.util.mc.entity.armor.ArmorUtils;
import com.mega.revelationfix.common.event.handler.ArmorEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(NearestAttackableTargetGoal.class)
public abstract class NearestAttackableTargetGoalMixin<T extends LivingEntity> extends TargetGoal {

    @Shadow
    @Final
    protected Class<T> targetType;

    @Shadow
    @Nullable
    protected LivingEntity target;

    public NearestAttackableTargetGoalMixin(Mob p_26140_, boolean p_26141_) {
        super(p_26140_, p_26141_);
    }

    @Inject(method = "canUse", at = @At("RETURN"), cancellable = true)
    private void canUse(CallbackInfoReturnable<Boolean> cir) {
        if (this.mob instanceof Spider && cir.getReturnValue()) {
            if (this.targetType.isAssignableFrom(Player.class)) {
                if (this.target != null) {
                    if (ArmorEvents.isSpiderSet(ArmorUtils.getArmorSet(this.target)))
                        cir.setReturnValue(false);
                }
            }
        }
    }
}
