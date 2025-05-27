package com.mega.revelationfix.mixin;

import com.mega.revelationfix.common.event.EarlyLivingDeathEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ForgeHooks.class)
public class ForgeHooksMixin {
    @Inject(method = "onLivingDeath", at = @At("HEAD"), cancellable = true, remap = false)
    private static void onLivingDeath(LivingEntity entity, DamageSource src, CallbackInfoReturnable<Boolean> cir) {
        boolean flag = MinecraftForge.EVENT_BUS.post(new EarlyLivingDeathEvent(entity, src));
        if (flag)
            cir.setReturnValue(true);
    }
}
