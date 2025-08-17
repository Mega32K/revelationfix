package com.mega.revelationfix.mixin.slashblade;

import com.mega.endinglib.util.annotation.ModDependsMixin;
import com.mega.revelationfix.common.compat.ToBeInject;
import mods.flammpfeil.slashblade.entity.IShootable;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ToBeInject.class)
@ModDependsMixin("slashblade")
public class ToBeInjectMixin {
    @Inject(method = "inWhitelist(Lnet/minecraft/world/item/Item;)Z", at = @At("HEAD"), cancellable = true, remap = false)
    private static void inWhitelist(Item item, CallbackInfoReturnable<Boolean> cir) {
        if (item instanceof ItemSlashBlade)
            cir.setReturnValue(true);
    }

    @Inject(method = "inWhitelist(Lnet/minecraft/world/entity/Entity;)Z", at = @At("HEAD"), cancellable = true, remap = false)
    private static void inWhitelist(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof IShootable)
            cir.setReturnValue(true);
    }
}
