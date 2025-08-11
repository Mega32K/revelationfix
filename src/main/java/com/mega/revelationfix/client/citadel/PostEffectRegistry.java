package com.mega.revelationfix.client.citadel;

import com.google.gson.JsonSyntaxException;
import com.mega.revelationfix.common.compat.SafeClass;
import com.mega.revelationfix.util.RevelationFixMixinPlugin;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostEffectRegistry {
    private static final List<ResourceLocation> registry = new ArrayList<>();

    private static final Map<ResourceLocation, PostEffect> postEffects = new HashMap<>();

    public static void clear() {
        for (PostEffect postEffect : postEffects.values()) {
            postEffect.close();
        }
        postEffects.clear();
    }

    public static void registerEffect(ResourceLocation resourceLocation) {
        registry.add(resourceLocation);
    }

    public static void onInitializeOutline() {
        clear();
        Minecraft minecraft = Minecraft.getInstance();
        for (ResourceLocation resourceLocation : registry) {
            PostChain postChain;
            RenderTarget renderTarget;
            try {
                postChain = new PostChain(minecraft.getTextureManager(), minecraft.getResourceManager(), minecraft.getMainRenderTarget(), resourceLocation);
                postChain.resize(minecraft.getWindow().getWidth(), minecraft.getWindow().getHeight());
                renderTarget = postChain.getTempTarget("final");
            } catch (IOException ioexception) {
                RevelationFixMixinPlugin.LOGGER.warn("Failed to load shader: {}", resourceLocation, ioexception);
                postChain = null;
                renderTarget = null;
            } catch (JsonSyntaxException jsonsyntaxexception) {
                RevelationFixMixinPlugin.LOGGER.warn("Failed to parse shader: {}", resourceLocation, jsonsyntaxexception);
                postChain = null;
                renderTarget = null;
            }
            postEffects.put(resourceLocation, new PostEffect(postChain, renderTarget, false));
        }
    }

    public static void resize(int x, int y) {
        for (PostEffect postEffect : postEffects.values()) {
            postEffect.resize(x, y);
        }
    }

    public static PostChain getPostChainFor(ResourceLocation resourceLocation) {
        PostEffect effect = postEffects.get(resourceLocation);
        return effect == null ? null : effect.getPostChain();
    }

    public static RenderTarget getRenderTargetFor(ResourceLocation resourceLocation) {
        PostEffect effect = postEffects.get(resourceLocation);
        return effect == null ? null : effect.getRenderTarget();
    }

    public static void renderEffectForNextTick(ResourceLocation resourceLocation) {
        PostEffect effect = postEffects.get(resourceLocation);
        if (effect != null) {
            effect.setEnabled(true);
        }
    }

    public static void blitEffects() {
        Minecraft mc = Minecraft.getInstance();
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        for (PostEffect postEffect : postEffects.values()) {
            if (postEffect.getPostChain() != null && postEffect.isEnabled()) {
                postEffect.getRenderTarget().blitToScreen(mc.getWindow().getWidth(), mc.getWindow().getHeight(), false);
                postEffect.getRenderTarget().clear(Minecraft.ON_OSX);
                mc.getMainRenderTarget().bindWrite(false);
                postEffect.setEnabled(false);
            }
        }
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    }

    public static void clearAndBindWrite(RenderTarget mainTarget) {
        for (PostEffect postEffect : postEffects.values()) {
            if (postEffect.getPostChain() != null && postEffect.isEnabled()) {
                postEffect.getRenderTarget().clear(Minecraft.ON_OSX);
                mainTarget.bindWrite(false);
            }
        }
    }

    public static void processEffects(RenderTarget mainTarget, float f) {
        for (PostEffect postEffect : postEffects.values()) {
            if (postEffect.isEnabled() && postEffect.postChain != null) {
                postEffect.postChain.process(Minecraft.getInstance().getFrameTime());
                mainTarget.bindWrite(false);
            }
        }
    }


    private static class PostEffect {
        private final PostChain postChain;
        private final RenderTarget renderTarget;
        private boolean enabled;

        public PostEffect(PostChain postChain, RenderTarget renderTarget, boolean enabled) {
            this.postChain = postChain;
            this.renderTarget = renderTarget;
            this.enabled = enabled;
        }

        public PostChain getPostChain() {
            return postChain;
        }

        public RenderTarget getRenderTarget() {
            return renderTarget;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public void close() {
            if (postChain != null) {
                postChain.close();
            }
        }

        public void resize(int x, int y) {
            if (postChain != null) {
                postChain.resize(x, y);
            }
        }
    }
}