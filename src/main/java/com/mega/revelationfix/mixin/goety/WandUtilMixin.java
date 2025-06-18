package com.mega.revelationfix.mixin.goety;

import com.Polarice3.Goety.utils.WandUtil;
import com.mega.revelationfix.util.EventUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WandUtil.class)
public class WandUtilMixin {
    @Inject(method = "getLevels", at = @At("RETURN"), cancellable = true, remap = false)
    private static void getLevels(Enchantment enchantment, LivingEntity livingEntity, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(EventUtil.getLevels(enchantment, livingEntity, cir.getReturnValue()));
    }
}
