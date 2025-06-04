package com.mega.revelationfix.mixin.cyclic;

import com.Polarice3.Goety.common.entities.boss.Apostle;
import com.lothrazar.cyclic.enchant.DisarmEnchant;
import com.mega.revelationfix.safe.mixinpart.ModDependsMixin;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DisarmEnchant.class)
@ModDependsMixin("cyclic")
public class DisarmEnchantMixin {
    @Inject(remap = false, method = "m_7677_", at = @At("HEAD"), cancellable = true)
    private void doPostAttack(LivingEntity user, Entity target, int level, CallbackInfo ci) {
        if (target instanceof Apostle)
            ci.cancel();
    }
}
