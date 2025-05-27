package com.mega.revelationfix.mixin.goety.ritual;

import com.Polarice3.Goety.common.crafting.RitualRecipe;
import com.Polarice3.Goety.common.ritual.Ritual;
import com.Polarice3.Goety.common.ritual.SummonRitual;
import com.mega.revelationfix.common.entity.boss.ApostleServant;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SummonRitual.class)
public abstract class SummonRitualMixin extends Ritual {
    public SummonRitualMixin(RitualRecipe recipe) {
        super(recipe);
    }

    @Inject(method = "spawnEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancements/critereon/SummonedEntityTrigger;trigger(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/entity/Entity;)V", shift = At.Shift.AFTER))
    private void setApostleServant(Player castingPlayer, Entity entity, Level world, CallbackInfo ci) {
        if (entity instanceof ApostleServant apostleServant) {
            apostleServant.setTrueOwner(castingPlayer);
            apostleServant.setPersistenceRequired();
        }
    }
}
