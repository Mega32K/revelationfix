package com.mega.revelationfix.client.renderer.entity;

import com.mega.revelationfix.client.model.entity.TeleportEntityModel;
import com.mega.revelationfix.client.renderer.VFRBuilders;
import com.mega.revelationfix.common.compat.Wrapped;
import com.mega.revelationfix.common.entity.binding.TeleportEntity;
import com.mega.revelationfix.safe.OdamanePlayerExpandedContext;
import com.mega.revelationfix.safe.entity.PlayerInterface;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;
import z1gned.goetyrevelation.ModMain;

public class TeleportEntityRenderer extends EntityRenderer<TeleportEntity> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(ModMain.MODID, "textures/entity/teleport_entity/teleport_entity.png");
    protected TeleportEntityModel model;

    public TeleportEntityRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn);
        this.model = new TeleportEntityModel(renderManagerIn.bakeLayer(TeleportEntityModel.LAYER_LOCATION));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull TeleportEntity p_114482_) {
        return VFRBuilders.beam;
    }

    @Override
    public void render(@NotNull TeleportEntity teleportEntity, float entityYaw, float pPartialTicks, @NotNull PoseStack pMatrixStack, @NotNull MultiBufferSource bufferIn, int packedLightIn) {
        try {
            pMatrixStack.pushPose();
            pMatrixStack.translate(.5, -0.5 - (3F / 16F), .5);
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(this.model.renderType(getTextureLocation(teleportEntity)));
            this.model.setupAnim(teleportEntity, 0.0F, 0.0F, pPartialTicks, 0.0F, 0.0F);
            Player player = Wrapped.clientPlayer();
            OdamanePlayerExpandedContext context = ((PlayerInterface) player).revelationfix$odamaneHaloExpandedContext();
            float a = Mth.lerp(pPartialTicks, context.teleportStayingTimeOld, context.teleportStayingTime) / 33F;
            RenderSystem.depthFunc(GL11.GL_ALWAYS);
            this.model.renderToBuffer(pMatrixStack, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 52F / 255F, 153F / 255F, 136F / 255F, a);
            pMatrixStack.popPose();
            /*

            pMatrixStack.pushPose();
            pMatrixStack.translate(1., 1.5, .5);
            pMatrixStack.scale(0.5F, 0.7F, 0.5F);
            pMatrixStack.mulPose(Minecraft.getInstance().gameRenderer.getMainCamera().rotation());
            pMatrixStack.mulPose(Axis.YP.rotationDegrees(180));
            Matrix4f matrix4f = pMatrixStack.last().pose();
            RenderSystem.setShaderTexture(0, RendererUtils.GUI_ICON);
            RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
            RenderSystem.enableBlend();
            RenderSystem.disableDepthTest();
            RenderSystem.disableCull();
            BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
            boolean frame = teleportEntity.tickCount % 40 < 20;
            if (frame) {
                bufferbuilder.vertex(matrix4f, -.5F, -.5F, 0F).color(1F, 1F, 1F, 1F).uv(0F/256F, 7F/256F).endVertex();
                bufferbuilder.vertex(matrix4f, -.5F, .5F, 0F).color(1F, 1F, 1F, 1F).uv(0F/256F, 0F/256F).endVertex();
                bufferbuilder.vertex(matrix4f, .5F, .5F, 0F).color(1F, 1F, 1F, 1F).uv(5F/256F, 0F/256F).endVertex();
                bufferbuilder.vertex(matrix4f, .5F, -.5F, 0F).color(1F, 1F, 1F, 1F).uv(5F/256F, 7F/256F).endVertex();
            } else {
                bufferbuilder.vertex(matrix4f, -.5F, -.5F, 0F).color(1F, 1F, 1F, 1F).uv((0F + 6F)/256F, 7F/256F).endVertex();
                bufferbuilder.vertex(matrix4f, -.5F, .5F, 0F).color(1F, 1F, 1F, 1F).uv((0F + 6F)/256F, 0F/256F).endVertex();
                bufferbuilder.vertex(matrix4f, .5F, .5F, 0F).color(1F, 1F, 1F, 1F).uv((5F + 6F)/256F, 0F/256F).endVertex();
                bufferbuilder.vertex(matrix4f, .5F, -.5F, 0F).color(1F, 1F, 1F, 1F).uv((5F + 6F)/256F, 7F/256F).endVertex();
            }
            BufferUploader.drawWithShader(bufferbuilder.end());
            RenderSystem.disableBlend();
            RenderSystem.enableCull();
            RenderSystem.enableDepthTest();
            pMatrixStack.popPose();
             */
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public boolean shouldRender(@NotNull TeleportEntity p_114491_, @NotNull Frustum p_114492_, double p_114493_, double p_114494_, double p_114495_) {
        Player player = Wrapped.clientPlayer();
        if (!(player != null && player.isAlive() && player.isShiftKeyDown())) {
            return false;
        }
        return super.shouldRender(p_114491_, p_114492_, p_114493_, p_114494_, p_114495_);
    }
}
