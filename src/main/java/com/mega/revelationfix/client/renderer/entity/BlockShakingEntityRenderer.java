package com.mega.revelationfix.client.renderer.entity;

import com.mega.revelationfix.client.citadel.GRRenderTypes;
import com.mega.revelationfix.client.citadel.PostEffectRegistry;
import com.mega.revelationfix.client.screen.post.PostEffectHandler;
import com.mega.revelationfix.common.compat.SafeClass;
import com.mega.revelationfix.common.entity.binding.BlockShakingEntity;
import com.mega.revelationfix.proxy.ClientProxy;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;

public class BlockShakingEntityRenderer extends EntityRenderer<BlockShakingEntity> {
    private final BlockRenderDispatcher dispatcher;

    public BlockShakingEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.dispatcher = context.getBlockRenderDispatcher();
    }

    @Override
    public boolean shouldRender(BlockShakingEntity p_114491_, Frustum p_114492_, double p_114493_, double p_114494_, double p_114495_) {
        return true;
    }

    @Override
    public void render(BlockShakingEntity entity, float p_114635_, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        if (SafeClass.usingShaderPack()) return;
        BlockState state = entity.getBlockState();

        bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        PostEffectRegistry.renderEffectForNextTick(ClientProxy.HOLOGRAM_SHADER);
        PostEffectHandler.updateUniform_post(PostEffectRegistry.getPostChainFor(ClientProxy.HOLOGRAM_SHADER), "Alpha", entity.getProgress(partialTick) * 2.0F);
        BlockState blockstate = entity.getBlockState();
        RenderSystem.enablePolygonOffset();
        RenderSystem.polygonOffset(-1.0F, -10.0F);
        RenderType renderType = GRRenderTypes.SOLID();
        if (blockstate.getRenderShape() == RenderShape.MODEL) {
            Level level = entity.level();
            if (blockstate != level.getBlockState(entity.blockPosition()) && blockstate.getRenderShape() != RenderShape.INVISIBLE) {
                poseStack.pushPose();
                BlockPos blockpos = new BlockPos((int) entity.getX(), (int) entity.getBoundingBox().maxY, (int) entity.getZ());
                float scale = 1.002F;
                float offset = (scale - 1F) / 2f;
                poseStack.translate(-offset, -2.0F - offset,  - offset);
                poseStack.scale(scale, scale, scale);
                BakedModel model = this.dispatcher.getBlockModel(blockstate);
                this.dispatcher.getModelRenderer().tesselateBlock(level, model, blockstate, blockpos, poseStack, bufferSource.getBuffer(renderType), false, RandomSource.create(), blockstate.getSeed(entity.getStartPos()), OverlayTexture.NO_OVERLAY, ModelData.EMPTY, renderType);
                poseStack.popPose();

            }
        }
        RenderSystem.polygonOffset(0.0F, 0.0F);
        RenderSystem.disablePolygonOffset();
        Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
        ((MultiBufferSource.BufferSource) bufferSource).endBatch();
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull BlockShakingEntity blockShakingEntity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }

}
