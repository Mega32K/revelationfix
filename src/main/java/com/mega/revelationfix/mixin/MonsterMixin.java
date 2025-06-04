package com.mega.revelationfix.mixin;

import com.Polarice3.Goety.api.entities.IOwned;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Monster.class)
public class MonsterMixin {
    @Inject(method = "isPreventingPlayerRest", at = @At("HEAD"), cancellable = true)
    private void isPreventingPlayerRest(Player p_33036_, CallbackInfoReturnable<Boolean> cir) {
        if (this instanceof IOwned)
            cir.setReturnValue(false);
    }
}
