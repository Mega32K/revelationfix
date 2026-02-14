package com.mega.revelationfix.client.screen.post;

import com.google.gson.JsonSyntaxException;
import com.mega.revelationfix.client.screen.CustomScreenEffect;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class PostProcessingShaders implements ResourceManagerReloadListener {
    public static final PostProcessingShaders INSTANCE = new PostProcessingShaders(Minecraft.getInstance());
    public static volatile boolean isReloading = false;
    public static List<Entity> currentCLEntities = new ArrayList<>();
    public static HashMap<CustomScreenEffect, PostChain> postChains = new HashMap<>();
    private final Minecraft minecraft;
    private final Logger LOGGER = LogManager.getLogger();
    private int prevWidth;
    private int prevHeight;

    public PostProcessingShaders(Minecraft minecraft) {
        this.minecraft = minecraft;
    }

    public void renderShaders(float partialTicks) {
        if (isReloading) return;
        if (minecraft.level != null && minecraft.player != null) {
            this.minecraft.getProfiler().push("revelation_post_effects");
            PostEffectHandler.getData().values().stream().filter(CustomScreenEffect::canUse).forEach(effect -> {
                PostChain postChain = postChains.get(effect);
                if (postChain != null) {
                    Window window = minecraft.getWindow();
                    if (prevWidth != window.getWidth() || prevHeight != window.getHeight())
                        postChain.resize(this.minecraft.getWindow().getWidth(), this.minecraft.getWindow().getHeight());
                    effect.onRenderTick(partialTicks);
                    postChain.process(partialTicks);
                    this.minecraft.getMainRenderTarget().bindWrite(false);
                }
            });
            this.minecraft.getProfiler().pop();
        }
        this.prevWidth = this.minecraft.getWindow().getWidth();
        this.prevHeight = this.minecraft.getWindow().getHeight();

    }

    public void initShader(ResourceManager manager) {
        isReloading = true;
        try {
            PostEffectHandler.getData().clear();
            PostEffectHandler.register();
            postChains.values().forEach(postChain -> {
                if (postChain != null) postChain.close();
            });

            postChains.clear();
            PostEffectHandler.getData().values().forEach(effect -> {
                try {
                    Window window = minecraft.getWindow();
                    PostChain postChain = new PostChain(this.minecraft.getTextureManager(), manager, this.minecraft.getMainRenderTarget(), effect.getShaderLocation());
                    postChain.resize(window.getWidth(), window.getHeight());
                    postChains.put(effect, postChain);
                } catch (JsonSyntaxException var3) {
                    LOGGER.warn("Failed to parse shader: {}", effect.getShaderLocation(), var3);
                } catch (IOException var4) {
                    LOGGER.warn("Failed to load shader: {}", effect.getShaderLocation(), var4);
                }
            });
        } catch (Throwable throwable) {
        }
        isReloading = false;

    }

    public void onResourceManagerReload(ResourceManager resourceManager) {
        this.initShader(resourceManager);
    }
}
