package com.mega.revelationfix.common.compat.tetra.client;

import com.mega.revelationfix.client.PreciseGuiGraphics;
import net.minecraft.client.gui.GuiGraphics;
import se.mickelus.mutil.gui.GuiElement;

public class AnimationGuiRect extends GuiElement {
    private final boolean offset;
    private Animation animation = Animation.ONLY_ALPHA;
    private int color;

    public AnimationGuiRect(int x, int y, int width, int height, int color) {
        this(x, y, width, height, color, false);
    }

    public AnimationGuiRect(int x, int y, int width, int height, int color, boolean offset) {
        super(x, y, offset ? width + 1 : width, offset ? height + 1 : height);
        this.color = color;
        this.offset = offset;
    }

    protected static void drawRectPrecisely(GuiGraphics graphics, float left, float top, float right, float bottom, int color, float opacity) {
        new PreciseGuiGraphics(graphics).fill(left, top, right, bottom, colorWithOpacity(color, opacity));
    }

    public AnimationGuiRect setColor(int color) {
        this.color = color;
        return this;
    }

    public AnimationGuiRect setAnimation(Animation animation) {
        this.animation = animation;
        return this;
    }

    public void draw(GuiGraphics graphics, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        super.draw(graphics, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);
        if (this.offset) {
            graphics.pose().pushPose();
            graphics.pose().translate(0.5F, 0.5F, 0.0F);
            switch (animation) {
                case ONLY_ALPHA ->
                        drawRect(graphics, refX + this.x, refY + this.y, refX + this.x + this.width - 1, refY + this.y + this.height - 1, this.color, opacity * this.getOpacity());
                case ALPHA_TO_RIGHT -> {
                    float a = opacity * this.getOpacity();
                    drawRectPrecisely(graphics, refX + this.x, refY + this.y, refX + this.x + this.width * Math.min(a, 1.0F) - 1, refY + this.y + this.height - 1, this.color, a);
                }
                case ALPHA_TO_LEFT -> {
                    float a = opacity * this.getOpacity();
                    drawRectPrecisely(graphics, refX + this.x + this.width * (1F - Math.min(a, 1.0F)), refY + this.y, refX + this.x + this.width - 1, refY + this.y + this.height - 1, this.color, a);
                }
                case ALPHA_TO_TOP -> {
                    float a = opacity * this.getOpacity();
                    drawRectPrecisely(graphics, refX + this.x, refY + this.y + this.height * (1F - Math.min(a, 1.0F)), refX + this.x + this.width - 1, refY + this.y + this.height - 1, this.color, a);
                }
                case ALPHA_TO_BOTTOM -> {
                    float a = opacity * this.getOpacity();
                    drawRectPrecisely(graphics, refX + this.x, refY + this.y, refX + this.x + this.width - 1, refY + this.y + this.height * Math.min(a, 1.0F) - 1, this.color, a);
                }
                default ->
                        getCustomAnimation(animation, graphics, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);
            }
            graphics.pose().popPose();
        } else {
            switch (animation) {
                case ONLY_ALPHA ->
                        drawRect(graphics, refX + this.x, refY + this.y, refX + this.x + this.width, refY + this.y + this.height, this.color, opacity * this.getOpacity());
                case ALPHA_TO_RIGHT -> {
                    float a = opacity * this.getOpacity();
                    drawRectPrecisely(graphics, refX + this.x, refY + this.y, refX + this.x + this.width * Math.min(a, 1.0F), refY + this.y + this.height, this.color, a);
                }
                case ALPHA_TO_LEFT -> {
                    float a = opacity * this.getOpacity();
                    drawRectPrecisely(graphics, refX + this.x + this.width * (1F - Math.min(a, 1.0F)), refY + this.y, refX + this.x + this.width, refY + this.y + this.height, this.color, a);
                }
                case ALPHA_TO_TOP -> {
                    float a = opacity * this.getOpacity();
                    drawRectPrecisely(graphics, refX + this.x, refY + this.y + this.height * (1F - Math.min(a, 1.0F)), refX + this.x + this.width, refY + this.y + this.height, this.color, a);
                }
                case ALPHA_TO_BOTTOM -> {
                    float a = opacity * this.getOpacity();
                    drawRectPrecisely(graphics, refX + this.x, refY + this.y, refX + this.x + this.width, refY + this.y + this.height * Math.min(a, 1.0F), this.color, a);
                }
                default ->
                        getCustomAnimation(animation, graphics, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);
            }
        }
    }

    public void getCustomAnimation(Animation animation, GuiGraphics graphics, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
    }

    public enum Animation {
        ONLY_ALPHA, ALPHA_TO_RIGHT, ALPHA_TO_LEFT, ALPHA_TO_TOP, ALPHA_TO_BOTTOM
    }
}
