package com.mega.revelationfix.mixin;

import com.mega.revelationfix.api.event.render.ItemRendererEvent;
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
    @Inject(method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;renderModelLists(Lnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/world/item/ItemStack;IILcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;)V",
                    shift = At.Shift.BEFORE
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void render(ItemStack p_115144_, ItemDisplayContext p_270188_, boolean p_115146_, PoseStack p_115147_, MultiBufferSource p_115148_, int p_115149_, int p_115150_, BakedModel p_115151_, CallbackInfo ci, boolean flag, boolean flag1, Iterator var11, BakedModel model, Iterator var13, RenderType rendertype, VertexConsumer vertexconsumer) {
        MinecraftForge.EVENT_BUS.post(new ItemRendererEvent.RenderModelListEvent(p_115144_, p_115147_, minecraft.getPartialTick(), p_270188_, vertexconsumer, model, p_115149_, p_115150_));
    }
}
