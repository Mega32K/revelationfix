package com.mega.revelationfix.mixin;

import com.mega.revelationfix.safe.OdamanePlayerExpandedContext;
import com.mega.revelationfix.util.entity.ATAHelper2;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SynchedEntityData.class)
public abstract class SynchedEntityDataMixin {
    @Shadow
    @Final
    private Entity entity;

    @Shadow
    protected abstract <T> SynchedEntityData.DataItem<T> getItem(EntityDataAccessor<T> p_135380_);

    @Inject(method = "set(Lnet/minecraft/network/syncher/EntityDataAccessor;Ljava/lang/Object;Z)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/syncher/SynchedEntityData$DataItem;setValue(Ljava/lang/Object;)V",
                    shift = At.Shift.BEFORE
            ),
            cancellable = true
    )
    private <T> void odamaneHaloDamageLimit(EntityDataAccessor<T> entityDataAccessor, T amount, boolean p_276370_, CallbackInfo ci) {
        if (entityDataAccessor.equals(LivingEntity.DATA_HEALTH_ID) && amount instanceof Float fValue && entity instanceof Player player) {
            if (ATAHelper2.hasOdamane(player)) {
                final float damageLimit = OdamanePlayerExpandedContext.damageLimit(player);
                OdamanePlayerExpandedContext context = ATAHelper2.getOdamaneEC(player);
                float lastHealthData = player.getHealth();
                if (context.isInvulnerable()) {
                    if (lastHealthData - fValue >= 0) {
                        ci.cancel();
                    }
                } else if (lastHealthData - fValue > damageLimit) {
                    this.getItem(LivingEntity.DATA_HEALTH_ID).setValue(lastHealthData - damageLimit);
                    context.setInvulnerableTicks();
                    ci.cancel();
                } else if (lastHealthData - fValue > 0)
                    context.setInvulnerableTicks();
            }
        }
    }
}
