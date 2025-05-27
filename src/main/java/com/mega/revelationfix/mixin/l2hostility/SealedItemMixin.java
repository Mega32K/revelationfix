package com.mega.revelationfix.mixin.l2hostility;

import com.mega.revelationfix.common.init.GRItems;
import com.mega.revelationfix.safe.ModDependsMixin;
import dev.xkmc.l2hostility.content.item.traits.SealedItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = SealedItem.class, remap = false)
@ModDependsMixin("l2hostility")
public class SealedItemMixin {
    @Inject(method = "sealItem", at = @At("HEAD"), cancellable = true)
    private static void sealItem(ItemStack stack, int time, CallbackInfoReturnable<ItemStack> cir) {
        if (stack.is(GRItems.HALO_OF_THE_END)) cir.setReturnValue(stack);
    }
}
