package com.mega.revelationfix.mixin.goety;

import com.Polarice3.Goety.api.magic.ISpell;
import com.Polarice3.Goety.common.items.magic.DarkWand;
import com.mega.revelationfix.util.ATAHelper2;
import com.mega.revelationfix.util.EventUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DarkWand.class)
public class DarkWandMixin {
    @Inject(remap = false, method = "SoulUse", at = @At("RETURN"), cancellable = true)
    private void SoulUse(LivingEntity entityLiving, ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (ATAHelper2.hasOdamane(entityLiving))
            cir.setReturnValue(Math.min(1, cir.getReturnValue()));
    }

    @Inject(method = "MagicResults", at = @At("HEAD"), cancellable = true, remap = false)
    private void MagicResults(ItemStack stack, Level worldIn, LivingEntity caster, CallbackInfo ci) {
        if (!(caster instanceof Player)) ci.cancel();
    }

    /*
    @Redirect(method = "setSpellConditions", at = @At(value = "INVOKE", target = "Lcom/Polarice3/Goety/api/magic/ISpell;castDuration(Lnet/minecraft/world/entity/LivingEntity;)I"), remap = false)
    private int duration(ISpell instance, LivingEntity caster) {
        return EventUtil.castDuration(instance.castDuration(caster), instance, caster);
    }
     */

    @Redirect(method = "MagicResults", at = @At(value = "INVOKE", target = "Lcom/Polarice3/Goety/api/magic/ISpell;SpellResult(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;)V"), remap = false)
    private void SpellResults(ISpell instance, ServerLevel worldIn, LivingEntity caster, ItemStack staff) {
        EventUtil.SpellResult(worldIn, caster, staff, instance);
    }
}
