package com.mega.revelationfix.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.phys.AABB;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public class RendererUtils {
    public static void renderRegularIcosahedron(PoseStack matrix, MultiBufferSource.BufferSource bufferSource, float radius, int packedLight, float r, float g, float b, float a, float percent) {
        float PI = 3.1415927F;
        RenderType type = RenderType.lines();
        VertexConsumer bb = bufferSource.getBuffer(type);
        a = Math.max(0.0F, a);
        float alpha = 1.1074114F;
        float beta = PI * 0.4F;
        float ri = 1.309017F;
        float l = 1.0F;
        matrix.pushPose();
        matrix.mulPose(Axis.XP.rotation(-PI / 2.0F));
        matrix.scale(radius, radius, radius);
        z1gned.goetyrevelation.util.RendererUtils.renderRegularPolygon5(matrix.last().pose(), matrix.last().normal(), bb, l, packedLight, r, g, b, a, percent, ri);

        int i;
        for (i = 0; i < 5; ++i) {
            matrix.mulPose(Axis.ZP.rotation(beta));
            matrix.pushPose();
            matrix.mulPose(Axis.ZP.rotation(PI));
            matrix.mulPose(Axis.YP.rotation(alpha));
            z1gned.goetyrevelation.util.RendererUtils.renderRegularPolygon5div1(matrix.last().pose(), matrix.last().normal(), bb, l, packedLight, r, g, b, a, percent, ri);
            matrix.popPose();
        }

        matrix.mulPose(Axis.YP.rotation(PI));
        z1gned.goetyrevelation.util.RendererUtils.renderRegularPolygon5(matrix.last().pose(), matrix.last().normal(), bb, l, packedLight, r, g, b, a, percent, ri);

        for (i = 0; i < 5; ++i) {
            matrix.mulPose(Axis.ZP.rotationDegrees(72.0F));
            matrix.pushPose();
            matrix.mulPose(Axis.ZP.rotation(PI));
            matrix.mulPose(Axis.YP.rotation(alpha));
            z1gned.goetyrevelation.util.RendererUtils.renderRegularPolygon5div1(matrix.last().pose(), matrix.last().normal(), bb, l, packedLight, r, g, b, a, percent, ri);
            matrix.popPose();
        }

        matrix.popPose();
        bufferSource.endBatch(type);
        //RenderSystem.lineWidth(1.0F);
    }

    public static void renderLineBox(PoseStack p_109647_, VertexConsumer p_109648_, AABB p_109649_, float p_109650_, float p_109651_, float p_109652_, float p_109653_) {
        renderLineBox(p_109647_, p_109648_, (float) p_109649_.minX, (float) p_109649_.minY, (float) p_109649_.minZ, (float) p_109649_.maxX, (float) p_109649_.maxY, (float) p_109649_.maxZ, p_109650_, p_109651_, p_109652_, p_109653_, p_109650_, p_109651_, p_109652_);
    }

    public static void renderLineBox(PoseStack poseStack, VertexConsumer vertexConsumer, float minX, float minY, float minZ, float maxX, float maxY, float maxZ, float r, float g, float b, float a, float r2, float g2, float b2) {
        Matrix4f matrix4f = poseStack.last().pose();
        Matrix3f matrix3f = poseStack.last().normal();
        vertexConsumer.vertex(matrix4f, minX, minY, minZ).color(r, g2, b2, a).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
        vertexConsumer.vertex(matrix4f, maxX, minY, minZ).color(r, g2, b2, a).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
        vertexConsumer.vertex(matrix4f, minX, minY, minZ).color(r2, g, b2, a).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        vertexConsumer.vertex(matrix4f, minX, maxY, minZ).color(r2, g, b2, a).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        vertexConsumer.vertex(matrix4f, minX, minY, minZ).color(r2, g2, b, a).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
        vertexConsumer.vertex(matrix4f, minX, minY, maxZ).color(r2, g2, b, a).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
        vertexConsumer.vertex(matrix4f, maxX, minY, minZ).color(r, g, b, a).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        vertexConsumer.vertex(matrix4f, maxX, maxY, minZ).color(r, g, b, a).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        vertexConsumer.vertex(matrix4f, maxX, maxY, minZ).color(r, g, b, a).normal(matrix3f, -1.0F, 0.0F, 0.0F).endVertex();
        vertexConsumer.vertex(matrix4f, minX, maxY, minZ).color(r, g, b, a).normal(matrix3f, -1.0F, 0.0F, 0.0F).endVertex();
        vertexConsumer.vertex(matrix4f, minX, maxY, minZ).color(r, g, b, a).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
        vertexConsumer.vertex(matrix4f, minX, maxY, maxZ).color(r, g, b, a).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
        vertexConsumer.vertex(matrix4f, minX, maxY, maxZ).color(r, g, b, a).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
        vertexConsumer.vertex(matrix4f, minX, minY, maxZ).color(r, g, b, a).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
        vertexConsumer.vertex(matrix4f, minX, minY, maxZ).color(r, g, b, a).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
        vertexConsumer.vertex(matrix4f, maxX, minY, maxZ).color(r, g, b, a).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
        vertexConsumer.vertex(matrix4f, maxX, minY, maxZ).color(r, g, b, a).normal(matrix3f, 0.0F, 0.0F, -1.0F).endVertex();
        vertexConsumer.vertex(matrix4f, maxX, minY, minZ).color(r, g, b, a).normal(matrix3f, 0.0F, 0.0F, -1.0F).endVertex();
        vertexConsumer.vertex(matrix4f, minX, maxY, maxZ).color(r, g, b, a).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
        vertexConsumer.vertex(matrix4f, maxX, maxY, maxZ).color(r, g, b, a).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
        vertexConsumer.vertex(matrix4f, maxX, minY, maxZ).color(r, g, b, a).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        vertexConsumer.vertex(matrix4f, maxX, maxY, maxZ).color(r, g, b, a).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        vertexConsumer.vertex(matrix4f, maxX, maxY, minZ).color(r, g, b, a).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
        vertexConsumer.vertex(matrix4f, maxX, maxY, maxZ).color(r, g, b, a).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
    }

    public static void vertex(VertexConsumer p_254464_, Matrix4f p_254085_, Matrix3f p_253962_, int light, float x, float y, float u, float v, Vector4f color) {
        p_254464_.vertex(p_254085_, x - 0.5F, y - 0.5F, 0.0F).color(color.x, color.y, color.z, color.w).uv(u, v).overlayCoords(OverlayTexture.WHITE_OVERLAY_V).uv2(light).normal(p_253962_, 0.0F, 1.0F, 0.0F).endVertex();
    }
}
