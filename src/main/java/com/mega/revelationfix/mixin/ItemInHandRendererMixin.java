package com.mega.revelationfix.mixin;

import com.mega.revelationfix.safe.mixinpart.mc.ItemInHandRendererData;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {

    @Inject(method = "renderArmWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getUseAnimation()Lnet/minecraft/world/item/UseAnim;"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void customAnim(AbstractClientPlayer p_109372_, float p_109373_, float p_109374_, InteractionHand p_109375_, float p_109376_, ItemStack p_109377_, float p_109378_, PoseStack p_109379_, MultiBufferSource p_109380_, int p_109381_, CallbackInfo ci, boolean flag, HumanoidArm humanoidarm, boolean flag3, int k) {
        ItemInHandRendererData.customAnim((ItemInHandRenderer) (Object) this, p_109372_, p_109373_, p_109374_, p_109375_, p_109376_, p_109377_, p_109378_, p_109379_, p_109380_, p_109381_, ci, flag, humanoidarm, flag3, k);
    }
}
