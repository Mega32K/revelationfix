package com.mega.revelationfix.mixin;

import com.mega.revelationfix.common.event.handler.ClientEventHandler;
import com.mega.revelationfix.common.init.GRItems;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
    @ModifyVariable(method = "render", at = @At("HEAD"), argsOnly = true)
    private ItemStack render(ItemStack stack) {
        if (stack.is(GRItems.RANDOM_DISPLAY_ITEM)) {
            return ClientEventHandler.randomDisplayItem.getDefaultInstance();
        } else if (stack.is(GRItems.RANDOM_DISPLAY_ITEM)) {
            return ClientEventHandler.randomPuzzleDisplayItem.getDefaultInstance();
        }
        return stack;
    }

    @ModifyVariable(method = "renderStatic(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/level/Level;III)V", at = @At("HEAD"), argsOnly = true)
    private ItemStack renderStatic(ItemStack stack) {
        if (stack.is(GRItems.RANDOM_DISPLAY_ITEM)) {
            return ClientEventHandler.randomDisplayItem.getDefaultInstance();
        } else if (stack.is(GRItems.RANDOM_DISPLAY_ITEM)) {
            return ClientEventHandler.randomPuzzleDisplayItem.getDefaultInstance();
        }
        return stack;
    }

    @ModifyVariable(method = "getModel", at = @At("HEAD"), argsOnly = true)
    private ItemStack getModel(ItemStack stack) {
        if (stack.is(GRItems.RANDOM_DISPLAY_ITEM)) {
            return ClientEventHandler.randomDisplayItem.getDefaultInstance();
        } else if (stack.is(GRItems.PUZZLE_DISPLAY_ITEM)) {
            return ClientEventHandler.randomPuzzleDisplayItem.getDefaultInstance();
        }
        return stack;
    }
}
