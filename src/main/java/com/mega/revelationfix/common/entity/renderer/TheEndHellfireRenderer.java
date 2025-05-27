package com.mega.revelationfix.common.entity.renderer;

import com.Polarice3.Goety.client.render.HellfireRenderer;
import com.Polarice3.Goety.client.render.ModModelLayer;
import com.Polarice3.Goety.client.render.model.IceBouquetModel;
import com.Polarice3.Goety.common.entities.projectiles.Hellfire;
import com.mega.revelationfix.common.client.citadel.GRRenderTypes;
import com.mega.revelationfix.common.compat.SafeClass;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class TheEndHellfireRenderer extends HellfireRenderer {
    private final IceBouquetModel<Hellfire> model;

    public TheEndHellfireRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn);
        this.model = new IceBouquetModel(renderManagerIn.bakeLayer(ModModelLayer.ICE_BOUQUET));
    }

    @Override
    public void render(Hellfire pEntity, float entityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource bufferIn, int packedLightIn) {
        float f = pEntity.getAnimationProgress(pPartialTicks);
        if (f >= 0.0F) {
            pMatrixStack.pushPose();
            pMatrixStack.mulPose(Axis.YP.rotationDegrees(90.0F - pEntity.getYRot()));
            pMatrixStack.scale(-f, -(f + 0.25F), f);
            pMatrixStack.translate(0.0, -1.45, 0.0);
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(this.model.renderType(getTextureLocation(pEntity)));
            this.model.setupAnim(pEntity, 0.0F, 0.0F, pPartialTicks / 10.0F, 0.0F, 0.0F);
            this.model.renderToBuffer(pMatrixStack, ivertexbuilder, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.15F);
            VertexConsumer glow = bufferIn.getBuffer(!SafeClass.usingShaderPack() ? RenderType.energySwirl(getTextureLocation(pEntity), 1.0F, 1.0F) : GRRenderTypes.energySwirl(getTextureLocation(pEntity), 1f, 1f));
            this.model.renderToBuffer(pMatrixStack, glow, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            pMatrixStack.popPose();
        }
    }

}
