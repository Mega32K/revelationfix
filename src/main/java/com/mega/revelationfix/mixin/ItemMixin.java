package com.mega.revelationfix.mixin;

import com.mega.revelationfix.common.data.ingrident.TheEndCraftingIngredient;
import com.mega.revelationfix.safe.TheEndRitualItemContext;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "isFoil", at = @At("HEAD"), cancellable = true)
    private void isFoil(ItemStack p_41453_, CallbackInfoReturnable<Boolean> cir) {
        Item item = p_41453_.getItem();
        if (item == TheEndRitualItemContext.THE_END_CRAFT && TheEndCraftingIngredient.INSTANCE.test(p_41453_))
            cir.setReturnValue(true);
    }

}
