package com.mega.revelationfix.mixin.goety;

import com.Polarice3.Goety.utils.SEHelper;
import com.mega.revelationfix.common.config.CommonConfig;
import com.mega.revelationfix.common.init.ModAttributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
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
    @Redirect(method = "rawHandleKill", at = @At(value = "INVOKE", target = "Lcom/Polarice3/Goety/utils/SEHelper;increaseSouls(Lnet/minecraft/world/entity/player/Player;I)V"))
    private static void rawHandleKill(Player player, int souls) {
        AttributeInstance attributeInstance = player.getAttribute(ModAttributes.SOUL_STEALING.get());
        float f = souls;
        if (attributeInstance != null) {
            double value = attributeInstance.getValue();
            f *= value;
            f += Math.abs(value-1F) * 100F;
        }
        SEHelper.increaseSouls(player, (int) f);
    }
}
