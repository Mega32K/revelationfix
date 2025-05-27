package com.mega.revelationfix.mixin;

import com.mega.revelationfix.common.compat.SafeClass;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    @Inject(method = "doPostDamageEffects", at = @At("TAIL"))
    private static void doPostDamageEffects(LivingEntity p_44897_, Entity p_44898_, CallbackInfo ci) {
        SafeClass.doomItemEffect(p_44897_, p_44898_);
    }
}
