package com.mega.revelationfix.client.renderer.entity;

import com.mega.revelationfix.common.entity.binding.FakeSpellerEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class FakeSpellerRenderer extends EntityRenderer<FakeSpellerEntity> {
    public FakeSpellerRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
    }

    @Override
    public boolean shouldRender(@NotNull FakeSpellerEntity p_114491_, @NotNull Frustum p_114492_, double p_114493_, double p_114494_, double p_114495_) {
        return false;
    }

    @Override
    public void render(@NotNull FakeSpellerEntity p_114485_, float p_114486_, float p_114487_, @NotNull PoseStack p_114488_, @NotNull MultiBufferSource p_114489_, int p_114490_) {
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull FakeSpellerEntity p_114482_) {
        return new ResourceLocation("");
    }
}
