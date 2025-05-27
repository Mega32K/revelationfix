package com.mega.revelationfix.mixin.goety;

import com.Polarice3.Goety.common.items.ModItems;
import com.Polarice3.Goety.init.ModCreativeTab;
import com.mega.revelationfix.common.init.GRItems;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModCreativeTab.class)
public class ModCreativeTabMixin {
    @Inject(remap = false, method = "lambda$static$13", at = @At("HEAD"))
    private static void registerServantEggs(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output, CallbackInfo ci) {
        output.accept(GRItems.APOSTLE_SERVANT_SPAWN_EGG.get());
        output.accept(GRItems.HERETIC_SERVANT_SPAWN_EGG.get());
        output.accept(GRItems.MAVERICK_SERVANT_SPAWN_EGG.get());
    }

    @Inject(remap = false, method = "lambda$static$9", at = @At("HEAD"))
    private static void registerFocus(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output, CallbackInfo ci) {
        z1gned.goetyrevelation.item.ModItems.ITEMS.getEntries().forEach((i) -> {
            if (i.isPresent() && ModItems.isFocus(i.get())) {
                output.accept(i.get());
            }
        });
    }
}
