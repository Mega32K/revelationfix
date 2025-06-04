package com.mega.revelationfix.client;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.FastColor;
import org.joml.Matrix4f;

public class PreciseGuiGraphics {
    private GuiGraphics graphics;

    public PreciseGuiGraphics(GuiGraphics src) {
        this.graphics = src;
    }

    public GuiGraphics getGraphics() {
        return graphics;
    }

    public void setGraphics(GuiGraphics graphics) {
        this.graphics = graphics;
    }


    public void fill(float left, float top, float right, float bottom, int iColor) {
        this.fill(left, top, right, bottom, 0, iColor);
    }

    public void fill(float left, float top, float right, float bottom, int zOffset, int iColor) {
        this.fill(RenderType.gui(), left, top, right, bottom, zOffset, iColor);
    }

    public void fill(RenderType renderType, float left, float top, float right, float bottom, int iColor) {
        this.fill(renderType, left, top, right, bottom, 0, iColor);
    }

    public void fill(RenderType renderType, float left, float top, float right, float bottom, int zOffset, int iColor) {
        Matrix4f matrix4f = this.graphics.pose().last().pose();
        if (left < right) {
            float i = left;
            left = right;
            right = i;
        }

        if (top < bottom) {
            float j = top;
            top = bottom;
            bottom = j;
        }

        float f3 = (float) FastColor.ARGB32.alpha(iColor) / 255.0F;
        float f = (float) FastColor.ARGB32.red(iColor) / 255.0F;
        float f1 = (float) FastColor.ARGB32.green(iColor) / 255.0F;
        float f2 = (float) FastColor.ARGB32.blue(iColor) / 255.0F;
        VertexConsumer vertexconsumer = this.graphics.bufferSource().getBuffer(renderType);
        vertexconsumer.vertex(matrix4f, left, top, (float) zOffset).color(f, f1, f2, f3).endVertex();
        vertexconsumer.vertex(matrix4f, left, bottom, (float) zOffset).color(f, f1, f2, f3).endVertex();
        vertexconsumer.vertex(matrix4f, right, bottom, (float) zOffset).color(f, f1, f2, f3).endVertex();
        vertexconsumer.vertex(matrix4f, right, top, (float) zOffset).color(f, f1, f2, f3).endVertex();
        this.graphics.flushIfUnmanaged();
    }
}
