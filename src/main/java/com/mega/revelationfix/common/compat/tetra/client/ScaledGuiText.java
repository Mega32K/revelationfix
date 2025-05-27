package com.mega.revelationfix.common.compat.tetra.client;

import com.mega.revelationfix.safe.mixinpart.tetra.GuiElementEC;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import se.mickelus.mutil.gui.GuiText;

import java.util.Iterator;
import java.util.List;

public class ScaledGuiText extends GuiText {
    public float scaleX = 1.0F;
    public float scaleY = 1.0F;
    public float scaleZ = 1.0F;
    public boolean modifyAlpha = true;
    Font fontRenderer = Minecraft.getInstance().font;
    String string = "";
    int color = 16777215;
    boolean center;

    public ScaledGuiText(int x, int y, int width, String string) {
        super(x, y, width, string);
        this.setString(string);
    }

    protected static void renderText(ScaledGuiText scaledGuiText, GuiGraphics graphics, Font fontRenderer, String string, int x, int y, int width, int color, float opacity) {
        List<FormattedCharSequence> list = fontRenderer.split(Component.literal(string), width);

        for (Iterator var9 = list.iterator(); var9.hasNext(); y += 9) {
            FormattedCharSequence line = (FormattedCharSequence) var9.next();
            if (scaledGuiText.center) {
                graphics.drawCenteredString(fontRenderer, line, x, y, colorWithOpacity(color, opacity));
            } else graphics.drawString(fontRenderer, line, x, y, colorWithOpacity(color, opacity));
        }

    }

    public ScaledGuiText setScale(float scale) {
        this.scaleX = this.scaleY = this.scaleZ = scale;
        return this;
    }

    public ScaledGuiText setScale(float x, float y, float z) {
        this.scaleX = x;
        this.scaleY = y;
        this.scaleZ = z;
        return this;
    }

    public void setString(String string) {
        if (this.fontRenderer == null)
            this.fontRenderer = Minecraft.getInstance().font;
        this.string = string.replace("\\n", "\n");
        this.height = this.fontRenderer.wordWrapHeight(this.string, this.width);
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setCenter(boolean center) {
        this.center = center;
    }

    @Override
    public void draw(GuiGraphics graphics, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        opacity = getOpacity();
        GuiElementEC ec = (GuiElementEC) this;
        if (ec.tetraClip$parent() != null)
            opacity *= ec.tetraClip$parent().getOpacity();
        PoseStack stack = graphics.pose();
        stack.pushPose();
        stack.translate(refX + this.x, refY + this.y, 0);
        stack.scale(this.scaleX, this.scaleY, this.scaleZ);
        if (modifyAlpha) {
            label0:
            {
                opacity = Mth.clamp(opacity, 0, 1.0F);
                if (opacity <= 0.0F)
                    break label0;
                color = ((int) ((color >>> 24) * opacity)) << 24 | (color >> 16 & 255) << 16 | (color >> 8 & 255) << 8 | (color & 255);
                renderText(this, graphics, this.fontRenderer, this.string, 0, 0, this.width, this.color, opacity);
            }
        } else renderText(this, graphics, this.fontRenderer, this.string, 0, 0, this.width, this.color, opacity);
        //super.drawString(graphics, text, x, y, color, opacity, drawShadow);


        stack.popPose();
        drawChildren(graphics, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);
    }
}
