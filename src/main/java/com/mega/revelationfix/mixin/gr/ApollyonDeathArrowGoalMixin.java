package com.mega.revelationfix.mixin.gr;

import com.Polarice3.Goety.common.entities.boss.Apostle;
import com.Polarice3.Goety.common.entities.hostile.cultists.SpellCastingCultist;
import com.mega.revelationfix.common.compat.SafeClass;
import com.mega.revelationfix.common.config.CommonConfig;
import com.mega.revelationfix.util.RevelationFixMixinPlugin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import z1gned.goetyrevelation.goal.ApollyonDeathArrowGoal;
import z1gned.goetyrevelation.goal.CastingGoal;
import z1gned.goetyrevelation.util.ApollyonAbilityHelper;

@Mixin(ApollyonDeathArrowGoal.class)
public abstract class ApollyonDeathArrowGoalMixin extends CastingGoal {
    @Unique
    private int shootingWillEndTime = 0;
    @Shadow(remap = false) private Apostle apostle;

    ApollyonDeathArrowGoalMixin(SpellCastingCultist mob) {
        super(mob);
    }

    @Inject(method = "getCastingInterval", at = @At("HEAD"), cancellable = true, remap = false)
    private void getCastingInterval(CallbackInfoReturnable<Integer> cir) {
        boolean isDoom = SafeClass.isDoom(apostle);
        cir.setReturnValue(isDoom ? (int)(CommonConfig.apollyonShootingCooldown * 0.5) : CommonConfig.apollyonShootingCooldown);
    }

    @Override
    public void tick() {
        if (this.spellWarmup > 0) {
            Level level = this.apostle.level();
            if (!level.isClientSide)
                level.levelEvent(499366777, apostle.blockPosition(), 0);
        }
        super.tick();
    }
    @Inject(method = "start", at = @At("HEAD"), cancellable = true)
    private void originStart(CallbackInfo ci) {
        super.start();
        this.shootingWillEndTime = this.apostle.tickCount + 30 * 20;
        ci.cancel();
    }
    @Inject(method = "getCastWarmupTime", at = @At("HEAD"), cancellable = true, remap = false)
    private void getCastWarmupTime(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(50);
    }
    @Inject(method = "castSpell", at = @At("HEAD"), remap = false)
    private void castSpell(CallbackInfo ci) {
        ((ApollyonAbilityHelper)this.apostle).allTitlesApostle_1_20_1$setShooting(true);
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity livingentity = this.apostle.getTarget();
        return livingentity != null && livingentity.isAlive() && (this.spellWarmup > 0 || apostle.tickCount < this.shootingWillEndTime);
    }
}
