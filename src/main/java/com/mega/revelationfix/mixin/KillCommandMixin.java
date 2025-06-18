package com.mega.revelationfix.mixin;

import com.mega.revelationfix.safe.DamageSourceInterface;
import com.mega.revelationfix.util.entity.ATAHelper2;
import com.mega.revelationfix.util.entity.EntityActuallyHurt;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.commands.KillCommand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Collection;
import java.util.Iterator;

@Mixin(KillCommand.class)
public class KillCommandMixin {
    @Inject(method = "kill", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;kill()V", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void kill(CommandSourceStack p_137814_, Collection<? extends Entity> p_137815_, CallbackInfoReturnable<Integer> cir, Iterator var2, Entity $$2) {
        if ($$2 instanceof Player player && ATAHelper2.hasOdamane(player)) {
            DamageSource source = player.damageSources().genericKill();
            ((DamageSourceInterface) source).revelationfix$trueKill(true);
            new EntityActuallyHurt(player).actuallyHurt(source, Float.MAX_VALUE);
        }
    }
}
