package com.mega.revelationfix.common.compat.tetra.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import se.mickelus.mutil.gui.GuiTexture;

public class ZGuiTexture extends GuiTexture {
    private int z;
    private boolean offset;

    public ZGuiTexture(int x, int y, int width, int height, ResourceLocation textureLocation, int z) {
        super(x, y, width, height, textureLocation);
        this.z = z;
    }

    public ZGuiTexture(int x, int y, int width, int height, int textureX, int textureY, ResourceLocation textureLocation, int z) {
        super(x, y, width, height, textureX, textureY, textureLocation);
        this.z = z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    @Override
    protected void drawTexture(GuiGraphics graphics, ResourceLocation textureLocation, int x, int y, int width, int height, int u, int v, int color, float opacity) {
        if (offset) {
            PoseStack stack = graphics.pose();
            stack.pushPose();
            stack.translate(0.5F, 0.5F, 0F);
            graphics.innerBlit(textureLocation, x, x + width, y, y + height, z, (float) u / (float) this.textureWidth, (float) (u + width) / (float) this.textureWidth, (float) v / (float) this.textureHeight, (float) (v + height) / (float) this.textureHeight, (float) (color >> 16 & 255) / 255.0F, (float) (color >> 8 & 255) / 255.0F, (float) (color & 255) / 255.0F, opacity);
            stack.popPose();
        } else {
            graphics.innerBlit(textureLocation, x, x + width, y, y + height, z, (float) u / (float) this.textureWidth, (float) (u + width) / (float) this.textureWidth, (float) v / (float) this.textureHeight, (float) (v + height) / (float) this.textureHeight, (float) (color >> 16 & 255) / 255.0F, (float) (color >> 8 & 255) / 255.0F, (float) (color & 255) / 255.0F, opacity);

        }
    }
}
