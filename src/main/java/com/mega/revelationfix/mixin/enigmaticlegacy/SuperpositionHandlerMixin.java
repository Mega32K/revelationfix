package com.mega.revelationfix.mixin.enigmaticlegacy;

import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.mega.revelationfix.safe.ModDependsMixin;
import com.mega.revelationfix.util.ATAHelper2;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = SuperpositionHandler.class, remap = false)
@ModDependsMixin("enigmaticlegacy")
public class SuperpositionHandlerMixin {
    @Inject(method = "canUnequipBoundRelics", at = @At("HEAD"), cancellable = true)
    private static void canUnequipBoundRelics(Player player, CallbackInfoReturnable<Boolean> cir) {
        if (ATAHelper2.hasOdamane(player))
            cir.setReturnValue(true);
    }
}
