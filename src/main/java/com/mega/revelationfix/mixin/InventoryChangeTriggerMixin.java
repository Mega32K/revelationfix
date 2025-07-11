package com.mega.revelationfix.mixin;

import com.mega.revelationfix.common.advancement.ModCriteriaTriggers;
import com.mega.revelationfix.common.data.ingrident.TheEndCraftingIngredient;
import com.mega.revelationfix.common.init.GRItems;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryChangeTrigger.class)
public class InventoryChangeTriggerMixin {
    @Inject(method = "trigger(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/item/ItemStack;)V", at = @At("HEAD"))
    private void trigger(ServerPlayer p_43150_, Inventory p_43151_, ItemStack p_43152_, CallbackInfo ci) {
        if (TheEndCraftingIngredient.INSTANCE.test(p_43152_))
            ModCriteriaTriggers.TE_CRAFT_TRIGGER.trigger(p_43150_);
    }
}
