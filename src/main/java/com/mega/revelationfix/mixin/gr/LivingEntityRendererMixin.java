package com.mega.revelationfix.mixin.gr;

import com.mega.revelationfix.common.compat.SafeClass;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import z1gned.goetyrevelation.client.layer.PlayerHaloLayer;
import z1gned.goetyrevelation.client.layer.PlayerInvulLayer;

@Mixin({LivingEntityRenderer.class})
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements RenderLayerParent<T, M> {
    protected LivingEntityRendererMixin(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
    }

    @Inject(
            at = {@At("TAIL")},
            method = {"<init>"}
    )
    private void addPlayerLayer(EntityRendererProvider.Context p_174289_, EntityModel p_174290_, float p_174291_, CallbackInfo ci) {
        if (SafeClass.isYSMLoaded()) return;
        LivingEntityRenderer var6 = (LivingEntityRenderer) (Object) this;
        if (var6 instanceof PlayerRenderer playerRenderer) {
            playerRenderer.addLayer(new PlayerHaloLayer(playerRenderer, p_174289_.getModelSet()));
            playerRenderer.addLayer(new PlayerInvulLayer(playerRenderer, p_174289_.getModelSet()));
        }

    }
}
