package com.mega.revelationfix.client.renderer.item;

import com.mega.revelationfix.client.model.item.KeeperSwordItemModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import z1gned.goetyrevelation.ModMain;

public class KeeperSwordItemRenderer extends BlockEntityWithoutLevelRenderer {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ModMain.MODID, "textures/item/combat/keeper_sword.png");
    private static final ResourceLocation GLOW_TEXTURE = new ResourceLocation(ModMain.MODID, "textures/item/combat/keeper_sword_layer.png");
    private final KeeperSwordItemModel<?> model;
    public KeeperSwordItemRenderer() {
        this(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }
    public KeeperSwordItemRenderer(BlockEntityRenderDispatcher renderDispatcher, EntityModelSet entityModelSet) {
        super(renderDispatcher, entityModelSet);
        this.model = new KeeperSwordItemModel<>(entityModelSet.bakeLayer(KeeperSwordItemModel.LAYER_LOCATION));
    }

    @Override
    public void renderByItem(@NotNull ItemStack itemStack, @NotNull ItemDisplayContext displayContext, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int packedLight, int overlay) {
        VertexConsumer vertexConsumer = ItemRenderer.getFoilBufferDirect(multiBufferSource, RenderType.entityCutoutNoCull(TEXTURE), true, itemStack.hasFoil());
        this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        VertexConsumer glowVC = ItemRenderer.getFoilBufferDirect(multiBufferSource, RenderType.eyes(GLOW_TEXTURE), true, itemStack.hasFoil());
        this.model.renderToBuffer(poseStack, glowVC, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }
}
