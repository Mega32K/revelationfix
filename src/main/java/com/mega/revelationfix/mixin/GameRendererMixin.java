package com.mega.revelationfix.mixin;

import com.mega.revelationfix.common.client.screen.post.PostProcessingShaders;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(method = "renderLevel", at = @At("TAIL"))
    public void renderLevelTail(float partialTicks, long l, PoseStack stack, CallbackInfo ci) {
        PostProcessingShaders.INSTANCE.renderShaders(partialTicks);
    }

    @Inject(method = {"resize"}, at = {@At("HEAD")})
    public void resize(int p_109098_, int p_109099_, CallbackInfo callbackInfo) {
        PostProcessingShaders.postChains.keySet().forEach(effect -> effect.current().resize(p_109098_, p_109099_));
    }
}
