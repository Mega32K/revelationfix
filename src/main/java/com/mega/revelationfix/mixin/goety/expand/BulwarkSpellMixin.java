package com.mega.revelationfix.mixin.goety.expand;

import com.Polarice3.Goety.common.magic.spells.BulwarkSpell;
import com.Polarice3.Goety.utils.MiscCapHelper;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.mega.revelationfix.common.capability.entity.GRPlayerCapability;
import com.mega.revelationfix.proxy.CommonProxy;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.apache.commons.lang3.mutable.MutableObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BulwarkSpell.class)
public class BulwarkSpellMixin {
    @Inject(method = "conditionsMet", at = @At("RETURN"), cancellable = true, remap = false)
    private void modifyConditionMet(ServerLevel worldIn, LivingEntity caster, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(cir.getReturnValueZ() || (caster instanceof Player player && CommonProxy.getPlayerCapOptional(player).map(GRPlayerCapability::getExtraMagicShield).orElse(0) > 0));
    }
    @WrapWithCondition(method = "SpellResult", at = @At(value = "INVOKE", target = "Lcom/Polarice3/Goety/utils/MiscCapHelper;setShields(Lnet/minecraft/world/entity/LivingEntity;I)V"), remap = false)
    private boolean fakeCondition$SpellResult(LivingEntity livingEntity, int amount) {
        if (livingEntity instanceof Player player) {
            MutableObject<Boolean> returnValue = new MutableObject<>(true);
            CommonProxy.getPlayerCapOptional(player).ifPresent(cap -> {
                int extraShieldCount = cap.getExtraMagicShield();
                if (extraShieldCount > 0) {
                    MiscCapHelper.setShields(player, amount + extraShieldCount);
                    cap.setExtraMagicShield(0);
                    returnValue.setValue(false);
                }
            });
            return returnValue.getValue();

        }
        return true;
    }
}
