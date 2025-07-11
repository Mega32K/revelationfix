package com.mega.revelationfix.mixin.gr;

import com.Polarice3.Goety.common.entities.boss.Apostle;
import com.mega.revelationfix.common.init.GRItems;
import com.mega.revelationfix.safe.GRSavedDataExpandedContext;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import z1gned.goetyrevelation.data.DefeatApollyonInNetherState;
import z1gned.goetyrevelation.event.LivingDeathListener;
import z1gned.goetyrevelation.item.ModItems;
import z1gned.goetyrevelation.util.ApollyonAbilityHelper;

@Mixin(value = LivingDeathListener.class, remap = false)
public class LivingDeathListenerFix {
    @Inject(method = "onLivingDeath", at = @At("HEAD"), cancellable = true)
    private static void fix(LivingDeathEvent event, CallbackInfo ci) {
        ci.cancel();

    }
}
