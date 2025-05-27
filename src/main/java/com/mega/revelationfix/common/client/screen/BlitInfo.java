package com.mega.revelationfix.common.client.screen;

import net.minecraft.resources.ResourceLocation;

public record BlitInfo(ResourceLocation texture, int startX, int startY, int width, int height) {
    public int x() {
        return startX;
    }

    public int y() {
        return startY;
    }

    public int endX() {
        return startX + width;
    }

    public int endY() {
        return startY + height;
    }
}