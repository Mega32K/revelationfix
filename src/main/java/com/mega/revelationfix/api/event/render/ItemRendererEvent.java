package com.mega.revelationfix.api.event.render;

import com.github.alexthe666.citadel.repack.jaad.aac.ps.PS;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

public abstract class ItemRendererEvent extends Event {
    private final float partialTicks;
    private final PoseStack poseStack;
    private final ItemStack itemStack;
    public ItemRendererEvent(ItemStack itemStack, PoseStack poseStack, float partialTicks) {
        this.partialTicks = partialTicks;
        this.poseStack = poseStack;
        this.itemStack = itemStack;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public PoseStack getPoseStack() {
        return poseStack;
    }
    public static class RenderModelListEvent extends ItemRendererEvent {
        private final ItemDisplayContext itemDisplayContext;
        private final VertexConsumer contextVC;
        private final BakedModel bakedModel;
        private final int packedLight;
        private final int overlay;

        public RenderModelListEvent(ItemStack itemStack, PoseStack poseStack, float partialTicks, ItemDisplayContext itemDisplayContext, VertexConsumer contextVC, BakedModel bakedModel, int packedLight, int overlay) {
            super(itemStack, poseStack, partialTicks);
            this.itemDisplayContext = itemDisplayContext;
            this.contextVC = contextVC;
            this.bakedModel = bakedModel;
            this.packedLight = packedLight;
            this.overlay = overlay;
        }

        public int getPackedLight() {
            return packedLight;
        }

        public int getOverlay() {
            return overlay;
        }

        public ItemDisplayContext getItemDisplayContext() {
            return itemDisplayContext;
        }

        public VertexConsumer getVertexConsumer() {
            return contextVC;
        }

        public BakedModel getBakedModel() {
            return bakedModel;
        }
    }
}
