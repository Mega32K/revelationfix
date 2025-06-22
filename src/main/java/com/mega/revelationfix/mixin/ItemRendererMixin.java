package com.mega.revelationfix.mixin;

import com.mega.revelationfix.common.event.handler.ClientEventHandler;
import com.mega.revelationfix.common.init.GRItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
    @Shadow @Final private Minecraft minecraft;

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
