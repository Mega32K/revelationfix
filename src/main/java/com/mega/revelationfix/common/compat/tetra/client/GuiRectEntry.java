package com.mega.revelationfix.common.compat.tetra.client;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import se.mickelus.mutil.gui.GuiElement;

public class GuiRectEntry extends GuiElement {
    public static final CuttingShapeData EMPTY = new CuttingShapeData(-1, -1, CuttingShape.NONE);
    private final boolean offset;
    public int headerColor;
    public CuttingShapeData cuttingData;
    private int color;
    private int headerWidth = 2;
    private AnimationGuiRect.Animation animation = AnimationGuiRect.Animation.ONLY_ALPHA;

    public GuiRectEntry(int x, int y, int width, int height, int color) {
        this(x, y, width, height, color, false);
    }

    public GuiRectEntry(int x, int y, int width, int height, int color, boolean offset) {
        super(x, y, offset ? width + 1 : width, offset ? height + 1 : height);
        this.color = color;
        this.offset = offset;
        this.headerColor = 0x424248;
        this.cuttingData = EMPTY;
    }

    public GuiRectEntry(int x, int y, int width, int height, int color, int headerColor, CuttingShapeData cuttingData) {
        this(x, y, width, height, color);
        this.headerColor = headerColor;
        this.cuttingData = cuttingData;
    }

    public GuiRectEntry setColor(int color) {
        this.color = color;
        return this;
    }

    public GuiRectEntry setHeaderColor(int headerColor) {
        this.headerColor = headerColor;
        return this;
    }

    public GuiRectEntry setHeaderWidth(int headerWidth) {
        this.headerWidth = headerWidth;
        return this;
    }

    public GuiRectEntry setAnimation(AnimationGuiRect.Animation animation) {
        this.animation = animation;
        return this;
    }

    public void draw(GuiGraphics graphics, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        super.draw(graphics, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);
        float a = opacity * this.getOpacity();
        if (this.offset) {
            graphics.pose().pushPose();
            graphics.pose().translate(0.5F, 0.5F, 0.0F);
            if (cuttingData == EMPTY) {
                animationScissors(graphics, refX + this.x, refY + this.y, refX + this.x + this.width - 1, refY + this.y + this.height - 1, a);

                drawRect(graphics, refX + this.x, refY + this.y, refX + this.x + this.width - 1, refY + this.y + this.height - 1, this.color, a);

                endScissors(graphics);
            } else if (cuttingData != null) {
                if (cuttingData.shape() == CuttingShape.RECT) {
                    animationScissors(graphics, refX + this.x, refY + this.y, refX + this.x + this.width - 1, refY + this.y + this.height - 1, a);

                    drawRect(graphics, refX + this.x, refY + this.y, refX + this.x + this.width / 2 - 1, refY + this.y + this.height - 1, cuttingData.color0(), a);
                    drawRect(graphics, refX + this.x + this.width / 2, refY + this.y, refX + this.x + this.width - 1, refY + this.y + this.height - 1, cuttingData.color1(), a);

                    endScissors(graphics);
                } else if (cuttingData.shape() == CuttingShape.TRAPEZIUM) {
                    animationScissors(graphics, refX + this.x, refY + this.y, refX + this.x + this.width / 3F * 2F - 1, refY + this.y + this.height - 1, a);
                    drawCuttingRect(graphics, new Vector2f(refX + this.x, refY + this.y), new Vector2f(refX + this.x + this.width / 3F - 1, refY + this.y), new Vector2f(refX + this.x + this.width / 3F * 2F - 1, refY + this.y + this.height - 1), new Vector2f(refX + this.x, refY + this.y + this.height - 1), cuttingData.color0(), a);
                    endScissors(graphics);

                    animationScissorsReflect(graphics, refX + this.x + this.width / 3F - 1, refY + this.y, refX + this.x + this.width - 1, refY + this.y + this.height - 1, a);
                    drawCuttingRect(graphics, new Vector2f(refX + this.x + this.width / 3F - 1, refY + this.y), new Vector2f(refX + this.x + this.width - 1, refY + this.y), new Vector2f(refX + this.x + this.width - 1, refY + this.y + this.height - 1), new Vector2f(refX + this.x + this.width / 3F * 2F, refY + this.y + this.height - 1), cuttingData.color1(), a);
                    endScissors(graphics);
                }
            }
            drawRect(graphics, refX + this.x - this.headerWidth, refY + this.y, refX + this.x - 1, refY + this.y + this.height - 1, this.headerColor, a);
            graphics.pose().popPose();
        } else {
            if (cuttingData == EMPTY) {
                animationScissors(graphics, refX + this.x, refY + this.y, refX + this.x + this.width, refY + this.y + this.height, a);

                drawRect(graphics, refX + this.x, refY + this.y, refX + this.x + this.width, refY + this.y + this.height, this.color, a);

                endScissors(graphics);
            } else {
                if (cuttingData.shape() == CuttingShape.RECT) {
                    animationScissors(graphics, refX + this.x, refY + this.y, refX + this.x + this.width, refY + this.y + this.height, a);
                    drawRect(graphics, refX + this.x, refY + this.y, refX + this.x + this.width / 2, refY + this.y + this.height, cuttingData.color0(), a);
                    drawRect(graphics, refX + this.x + this.width / 2, refY + this.y, refX + this.x + this.width, refY + this.y + this.height, cuttingData.color1(), a);
                    endScissors(graphics);
                } else if (cuttingData.shape() == CuttingShape.TRAPEZIUM) {
                    animationScissors(graphics, refX + this.x, refY + this.y, refX + this.x + this.width / 3F * 2F, refY + this.y + this.height, a);
                    drawCuttingRect(graphics, new Vector2f(refX + this.x, refY + this.y), new Vector2f(refX + this.x + this.width / 3F, refY + this.y), new Vector2f(refX + this.x + this.width / 3F * 2F, refY + this.y + this.height), new Vector2f(refX + this.x, refY + this.y + this.height), cuttingData.color0(), a);
                    endScissors(graphics);

                    animationScissorsReflect(graphics, refX + this.x + this.width / 3F, refY + this.y, refX + this.x + this.width, refY + this.y + this.height, a);
                    drawCuttingRect(graphics, new Vector2f(refX + this.x + this.width / 3F, refY + this.y), new Vector2f(refX + this.x + this.width, refY + this.y), new Vector2f(refX + this.x + this.width, refY + this.y + this.height), new Vector2f(refX + this.x + this.width / 3F * 2F, refY + this.y + this.height), cuttingData.color1(), a);
                    endScissors(graphics);
                }
            }
            drawRect(graphics, refX + this.x - this.headerWidth, refY + this.y, refX + this.x, refY + this.y + this.height, this.headerColor, a);
        }

    }

    //lerp -> partial from to
    public void animationScissors(GuiGraphics graphics, float startX, float startY, float endX, float endY, float opacity) {
        if (animation == AnimationGuiRect.Animation.ONLY_ALPHA || opacity >= 1.0F) return;
        opacity = Math.min(opacity, 1.0F);
        switch (animation) {
            case ALPHA_TO_LEFT -> startX = Mth.lerp(opacity, endX, startX);
            case ALPHA_TO_RIGHT -> endX = Mth.lerp(opacity, startX, endX);
            case ALPHA_TO_TOP -> startY = Mth.lerp(opacity, endY, startY);
            case ALPHA_TO_BOTTOM -> endY = Mth.lerp(opacity, startY, endY);
        }
        graphics.enableScissor((int) startX, (int) startY, (int) endX, (int) endY);
    }

    //lerp -> partial from to
    public void animationScissorsReflect(GuiGraphics graphics, float startX, float startY, float endX, float endY, float opacity) {
        if (animation == AnimationGuiRect.Animation.ONLY_ALPHA || opacity >= 1.0F) return;
        opacity = Math.min(opacity, 1.0F);
        if (animation == AnimationGuiRect.Animation.ALPHA_TO_LEFT)
            animation = AnimationGuiRect.Animation.ALPHA_TO_RIGHT;
        else if (animation == AnimationGuiRect.Animation.ALPHA_TO_RIGHT)
            animation = AnimationGuiRect.Animation.ALPHA_TO_LEFT;
        else if (animation == AnimationGuiRect.Animation.ALPHA_TO_TOP)
            animation = AnimationGuiRect.Animation.ALPHA_TO_BOTTOM;
        else if (animation == AnimationGuiRect.Animation.ALPHA_TO_BOTTOM)
            animation = AnimationGuiRect.Animation.ALPHA_TO_TOP;
        switch (animation) {
            case ALPHA_TO_LEFT -> startX = Mth.lerp(opacity, endX, startX);
            case ALPHA_TO_RIGHT -> endX = Mth.lerp(opacity, startX, endX);
            case ALPHA_TO_TOP -> startY = Mth.lerp(opacity, endY, startY);
            case ALPHA_TO_BOTTOM -> endY = Mth.lerp(opacity, startY, endY);
        }
        graphics.enableScissor((int) startX, (int) startY, (int) endX, (int) endY);
    }

    public void endScissors(GuiGraphics graphics) {
        if (animation == AnimationGuiRect.Animation.ONLY_ALPHA || opacity >= 1.0F) return;
        graphics.disableScissor();
    }

    protected void drawCuttingRect(GuiGraphics graphics, Vector2f leftTop, Vector2f rightTop, Vector2f rightBottom, Vector2f leftBottom, int color, float opacity) {
        this.fill(graphics, leftTop, rightTop, rightBottom, leftBottom, colorWithOpacity(color, opacity), opacity);
    }

    public void fill(GuiGraphics graphics, Vector2f leftTop, Vector2f rightTop, Vector2f rightBottom, Vector2f leftBottom, int color, float opacity) {
        this.fill(graphics, leftTop, rightTop, rightBottom, leftBottom, 0, color, opacity);
    }

    public void fill(GuiGraphics graphics, Vector2f leftTop, Vector2f rightTop, Vector2f rightBottom, Vector2f leftBottom, int zOffset, int color, float opacity) {
        this.fill(graphics, RenderType.gui(), leftTop, rightTop, rightBottom, leftBottom, zOffset, color, opacity);
    }

    public void fill(GuiGraphics graphics, RenderType renderType, Vector2f leftTop, Vector2f rightTop, Vector2f rightBottom, Vector2f leftBottom, int zOffset, int color, float opacity) {
        Matrix4f matrix4f = graphics.pose().last().pose();

        float f3 = (float) FastColor.ARGB32.alpha(color) / 255.0F;
        float f = (float) FastColor.ARGB32.red(color) / 255.0F;
        float f1 = (float) FastColor.ARGB32.green(color) / 255.0F;
        float f2 = (float) FastColor.ARGB32.blue(color) / 255.0F;
        VertexConsumer vertexconsumer = graphics.bufferSource().getBuffer(renderType);
        vertexconsumer.vertex(matrix4f, leftTop.x, leftTop.y, (float) zOffset).color(f, f1, f2, f3 * opacity).endVertex();
        vertexconsumer.vertex(matrix4f, leftBottom.x, leftBottom.y, (float) zOffset).color(f, f1, f2, f3 * opacity).endVertex();
        vertexconsumer.vertex(matrix4f, rightBottom.x, rightBottom.y, (float) zOffset).color(f, f1, f2, f3 * opacity).endVertex();
        vertexconsumer.vertex(matrix4f, rightTop.x, rightTop.y, (float) zOffset).color(f, f1, f2, f3 * opacity).endVertex();
        graphics.flushIfUnmanaged();
    }
}
