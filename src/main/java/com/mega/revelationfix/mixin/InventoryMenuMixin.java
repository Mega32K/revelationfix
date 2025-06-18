package com.mega.revelationfix.mixin;

import com.mega.revelationfix.common.init.GRItems;
import com.mega.revelationfix.util.entity.ATAHelper2;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.world.inventory.InventoryMenu$1")
public abstract class InventoryMenuMixin extends Slot {
    InventoryMenuMixin(Container p_40223_, int p_40224_, int p_40225_, int p_40226_) {
        super(p_40223_, p_40224_, p_40225_, p_40226_);
    }

    @Inject(method = "mayPickup", at = @At(value = "HEAD"), cancellable = true)
    private void mayPickup(Player p_39744_, CallbackInfoReturnable<Boolean> cir) {
        ItemStack itemstack = this.getItem();
        if (!itemstack.isEmpty() && !p_39744_.isCreative() && EnchantmentHelper.hasBindingCurse(itemstack))
            if (ATAHelper2.hasOdamane(p_39744_) && !itemstack.is(GRItems.HALO_OF_THE_END))
                cir.setReturnValue(true);
    }
}
