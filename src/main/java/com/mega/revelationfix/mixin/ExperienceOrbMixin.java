package com.mega.revelationfix.mixin;

import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import z1gned.goetyrevelation.item.ModItems;

import java.util.Map;

@Mixin(ExperienceOrb.class)
public class ExperienceOrbMixin {
    @Inject(method = "repairPlayerItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;setDamageValue(I)V"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void repairPlayerItems(Player p_147093_, int p_147094_, CallbackInfoReturnable<Integer> cir, Map.Entry entry, ItemStack itemstack, int i) {
        if (itemstack.is(ModItems.WITHER_QUIETUS.get()))
            cir.setReturnValue(0);
    }
}
