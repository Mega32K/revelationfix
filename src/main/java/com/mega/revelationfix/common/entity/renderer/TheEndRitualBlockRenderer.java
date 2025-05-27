package com.mega.revelationfix.common.entity.renderer;

import com.mega.revelationfix.common.client.citadel.GRRenderTypes;
import com.mega.revelationfix.common.client.citadel.PostEffectRegistry;
import com.mega.revelationfix.common.client.screen.post.PostEffectHandler;
import com.mega.revelationfix.common.compat.SafeClass;
import com.mega.revelationfix.common.entity.TheEndRitualBlockEntity;
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

public class TheEndRitualBlockRenderer extends EntityRenderer<TheEndRitualBlockEntity> {
    private final BlockRenderDispatcher dispatcher;

    public TheEndRitualBlockRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.dispatcher = context.getBlockRenderDispatcher();
    }

    @Override
    public boolean shouldRender(TheEndRitualBlockEntity p_114491_, Frustum p_114492_, double p_114493_, double p_114494_, double p_114495_) {
        return true;
    }

    @Override
    public void render(TheEndRitualBlockEntity entity, float p_114635_, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        if (SafeClass.usingShaderPack()) return;
        BlockState state = entity.getBlockState();

        bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        PostEffectRegistry.renderEffectForNextTick(ClientProxy.HOLOGRAM_SHADER);
        PostEffectHandler.updateUniform_post(PostEffectRegistry.getPostChainFor(ClientProxy.HOLOGRAM_SHADER), "Alpha", entity.getProgress(partialTick) * 2.0F);
        BlockState blockstate = entity.getBlockState();
        RenderSystem.enablePolygonOffset();
        RenderSystem.polygonOffset(-1.0F, -10.0F);
        if (blockstate.getRenderShape() == RenderShape.MODEL) {
            Level level = entity.level();
            if (blockstate != level.getBlockState(entity.blockPosition()) && blockstate.getRenderShape() != RenderShape.INVISIBLE) {
                poseStack.pushPose();
                BlockPos blockpos = new BlockPos((int) entity.getX(), (int) entity.getBoundingBox().maxY, (int) entity.getZ());
                float scale = 1.001F;
                float offset = (scale - 1F) / 8F;
                poseStack.translate(-0, -2.0, -0);
                poseStack.scale(scale, scale, scale);
                RenderSystem.disableDepthTest();
                RenderSystem.depthMask(false);
                BakedModel model = this.dispatcher.getBlockModel(blockstate);

                RenderType renderType = GRRenderTypes.SOLID();
                this.dispatcher.getModelRenderer().tesselateBlock(level, model, blockstate, blockpos, poseStack, bufferSource.getBuffer(renderType), false, RandomSource.create(), blockstate.getSeed(entity.getStartPos()), OverlayTexture.NO_OVERLAY, ModelData.EMPTY, renderType);

                poseStack.popPose();
                super.render(entity, p_114635_, partialTick, poseStack, bufferSource, packedLight);
                RenderSystem.depthMask(true);
                RenderSystem.enableDepthTest();
            }
        }
        RenderSystem.polygonOffset(0.0F, 0.0F);
        RenderSystem.disablePolygonOffset();
        ((MultiBufferSource.BufferSource) bufferSource).endBatch();
        Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull TheEndRitualBlockEntity theEndRitualBlockEntity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }

}
