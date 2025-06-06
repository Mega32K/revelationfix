package com.mega.revelationfix.mixin.goety;

import com.Polarice3.Goety.common.magic.spells.WeakeningSpell;
import com.Polarice3.Goety.utils.MobUtil;
import com.mega.revelationfix.common.entity.FakeSpellerEntity;
import com.mega.revelationfix.util.entity.EntityFinder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobUtil.class)
public class MobUtilMixin {
    @ModifyVariable(remap = false, method = "areAllies", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private static Entity modifySpeller(Entity entity) {
        if (entity instanceof FakeSpellerEntity speller && speller.getTrueOwner() instanceof Player player)
            return player;
        return entity;
    }
    @ModifyVariable(remap = false, method = "areAllies", at = @At("HEAD"), argsOnly = true, ordinal = 1)
    private static Entity modifySpeller2(Entity entity) {
        if (entity instanceof FakeSpellerEntity speller && speller.getTrueOwner() instanceof Player player)
            return player;
        return entity;
    }
    @Inject(remap = false, method = "hurtCalculation", at = @At("RETURN"), cancellable = true)
    private static void hurtCalculation(LivingEntity livingEntity, DamageSource damageSource, float amount, CallbackInfoReturnable<Float> cir) {
        if (livingEntity instanceof FakeSpellerEntity)
            cir.setReturnValue(0F);
    }
}
