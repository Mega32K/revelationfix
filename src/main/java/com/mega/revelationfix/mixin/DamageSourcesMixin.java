package com.mega.revelationfix.mixin;

import com.mega.revelationfix.safe.DamageSourceInterface;
import com.mega.revelationfix.util.ATAHelper2;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 检测playerAttack附加破无敌
 */
@Mixin(DamageSources.class)
public class DamageSourcesMixin {
    @Inject(method = "playerAttack", at = @At("RETURN"))
    private void playerAttack(Player p_270723_, CallbackInfoReturnable<DamageSource> cir) {
        if (ATAHelper2.hasOdamane(p_270723_))
            ((DamageSourceInterface) cir.getReturnValue()).revelationfix$fePower(true);
    }
}
