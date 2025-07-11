package com.mega.revelationfix.mixin;

import com.Polarice3.Goety.common.entities.boss.Apostle;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.ThornsEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import z1gned.goetyrevelation.util.ApollyonAbilityHelper;

@Mixin(ThornsEnchantment.class)
public class ThornsEnchantmentMixin {
    @Inject(method = "doPostHurt", at = @At("HEAD"), cancellable = true)
    private void disableWhenApollyon(LivingEntity p_45215_, Entity p_45216_, int p_45217_, CallbackInfo ci) {
        if (p_45216_ instanceof Apostle apostle && ((ApollyonAbilityHelper) apostle).allTitlesApostle_1_20_1$isApollyon())
            ci.cancel();
    }
}
