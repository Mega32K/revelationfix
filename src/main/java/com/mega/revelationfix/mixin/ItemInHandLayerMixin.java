package com.mega.revelationfix.mixin;

import com.mega.revelationfix.safe.mixinpart.mc.ItemInHandRendererData;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ItemInHandLayer.class, priority = 0)
public abstract class ItemInHandLayerMixin<T extends LivingEntity, M extends EntityModel<T> & ArmedModel> extends RenderLayer<T, M> {
    @Shadow
    @Final
    private ItemInHandRenderer itemInHandRenderer;

    public ItemInHandLayerMixin(RenderLayerParent<T, M> p_117346_) {
        super(p_117346_);
    }

    @Inject(method = "renderArmWithItem", at = @At("HEAD"), cancellable = true)
    private void render(LivingEntity p_117185_, ItemStack p_117186_, ItemDisplayContext p_270970_, HumanoidArm p_117188_, PoseStack p_117189_, MultiBufferSource p_117190_, int p_117191_, CallbackInfo ci) {
        ItemInHandRendererData.render((ItemInHandLayer<T, M>) (Object) this, p_117185_, p_117186_, p_270970_, p_117188_, p_117189_, p_117190_, p_117191_, ci);
    }
}
