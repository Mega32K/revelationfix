package com.mega.revelationfix.mixin.curios;

import com.mega.endinglib.util.annotation.ModDependsMixin;
import com.mega.revelationfix.common.apollyon.common.PlayerTickrateExecutor;
import com.mega.revelationfix.common.config.CommonConfig;
import com.mega.revelationfix.common.init.GRItems;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.mixin.CuriosImplMixinHooks;

@Mixin(value = CuriosImplMixinHooks.class, priority = 0, remap = false)
@ModDependsMixin("curios")
public class CuriosImplMixinHooksMixin {
    @Inject(method = "isStackValid", at = @At("HEAD"), cancellable = true)
    private static void isStackValid(SlotContext slotContext, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (slotContext.entity() instanceof Player player) {
            if (stack.is(GRItems.HALO_OF_THE_END))
                cir.setReturnValue(true);
            else if (PlayerTickrateExecutor.isInDoom(player) && !CommonConfig.inWhitelist(stack.getItem()))
                cir.setReturnValue(false);
        }
    }
}
