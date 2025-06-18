package com.mega.revelationfix.mixin.goety;

import com.Polarice3.Goety.utils.SEHelper;
import com.mega.revelationfix.common.config.CommonConfig;
import com.mega.revelationfix.common.event.handler.ArmorEvents;
import com.mega.revelationfix.common.item.armor.ModArmorMaterials;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import z1gned.goetyrevelation.util.ATAHelper;

@Mixin(value = SEHelper.class, remap = false)
public class SEHelperMixin {
    @Inject(method = "soulDiscount", at = @At("RETURN"), cancellable = true)
    private static void soulDiscount(LivingEntity living, CallbackInfoReturnable<Float> cir) {
        if (living instanceof Player player) {
            if (ATAHelper.hasHalo(player))
                cir.setReturnValue(cir.getReturnValue() / (float) CommonConfig.haloSoulReduction);
        }
    }
}
