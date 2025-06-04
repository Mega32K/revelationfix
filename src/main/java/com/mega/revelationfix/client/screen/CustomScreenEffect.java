package com.mega.revelationfix.client.screen;

import com.mega.revelationfix.client.screen.post.PostProcessingShaders;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;


public interface CustomScreenEffect {
    String getName();

    ResourceLocation getShaderLocation();

    void onRenderTick(float partialTicks);

    boolean canUse();

    default PostChain current() {
        return PostProcessingShaders.postChains.get(this);
    }
}
