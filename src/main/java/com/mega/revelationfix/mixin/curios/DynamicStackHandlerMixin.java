package com.mega.revelationfix.mixin.curios;

import com.mega.revelationfix.common.init.GRItems;
import com.mega.revelationfix.util.ATAHelper2;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import top.theillusivec4.curios.common.inventory.DynamicStackHandler;

@Mixin(DynamicStackHandler.class)
public abstract class DynamicStackHandlerMixin extends ItemStackHandler {
    @Redirect(method = "extractItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isCreative()Z"))
    private boolean extractItem(Player instance, int slot, int amount, boolean simulate) {
        if (ATAHelper2.hasOdamane(instance) && !this.stacks.get(slot).is(GRItems.HALO_OF_THE_END)) {
            return true;
        } else return instance.isCreative();
    }
}
