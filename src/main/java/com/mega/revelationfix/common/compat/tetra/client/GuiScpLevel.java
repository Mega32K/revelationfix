package com.mega.revelationfix.common.compat.tetra.client;

import com.mega.revelationfix.Revelationfix;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.joml.Vector2d;
import org.joml.Vector4f;
import se.mickelus.mutil.gui.GuiElement;
import z1gned.goetyrevelation.client.event.NetherStarShaders;

public class GuiScpLevel extends GuiElement {
    public int level = 0;

    public GuiScpLevel(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    static void enableCosmicShader(float yaw, float pitch, float scale, int hurt, float a) {
        NetherStarShaders.cosmicTime.set((float) Util.getMillis() / 50.0F);
        NetherStarShaders.cosmicYaw.set(yaw);
        NetherStarShaders.cosmicPitch.set(pitch);
        NetherStarShaders.cosmicExternalScale.set(scale);
        NetherStarShaders.cosmicOpacity.set(a);
        NetherStarShaders.cosmicHurt.set(hurt);
        NetherStarShaders.cosmicShader.setCosmicIcon();
        NetherStarShaders.cosmicShader.apply();
        RenderSystem.setShader(() -> {
            return NetherStarShaders.cosmicShader;
        });
    }

    @Override
    public void draw(GuiGraphics graphics, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        if (level == 6)
            graphics.drawManaged(() -> blitCosmicLevel(graphics.pose(), refX + x, refY + y, width, height, 0, opacity * getOpacity()));
        else {
            blitLevel(graphics, graphics.pose(), refX + x, refY + y, width, height, 0, opacity * getOpacity());
        }
        super.draw(graphics, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);
    }

    public void blitCosmicLevel(PoseStack stack, float x, float y, float width, float height, int z, float a) {
        Minecraft mc = Minecraft.getInstance();
        RenderSystem.setShaderTexture(0, new ResourceLocation(Revelationfix.MODID, "textures/particle/white.png"));
        RenderSystem.setShader(() -> NetherStarShaders.cosmicShader);
        Camera camera = mc.gameRenderer.getMainCamera();
        float yd = (float) camera.getPosition().y;
        float xzd = (float) (new Vector2d(camera.getPosition().x, camera.getPosition().y)).distance(0.0, 0.0);
        float pitch = 0.0F;
        float yaw = 0.0F;
        if (mc.player != null) {
            yaw = (float) ((double) mc.player.getYRot() * 1.0E-5);
            pitch = (float) ((double) mc.player.getXRot() * 1.0E-5);
        }

        float scale = 128;
        enableCosmicShader(yaw, pitch, scale, 0, a);
        Matrix4f matrix4f = stack.last().pose();
        Vector4f color = new Vector4f(1.0F);
        LightTexture lightTexture = mc.gameRenderer.lightTexture();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
        lightTexture.turnOnLightLayer();
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.BLOCK);
        float yStep = height / 6.0F;
        for (float f = 0.0F; f < height; f += yStep) {
            bufferbuilder.vertex(matrix4f, x, y + f, (float) z).color(color.x, color.y, color.z, color.w).uv(0, 1).uv2(15728880).normal(0.0F, 0.0F, 0.0F).endVertex();
            bufferbuilder.vertex(matrix4f, x, y + f + yStep * 0.6F, (float) z).color(color.x, color.y, color.z, color.w).uv(1, 1).uv2(15728880).normal(0.0F, 0.0F, 0.0F).endVertex();
            bufferbuilder.vertex(matrix4f, x + width, y + f + yStep * 0.6F, (float) z).color(color.x, color.y, color.z, color.w).uv(1, 0).uv2(15728880).normal(0.0F, 0.0F, 0.0F).endVertex();
            bufferbuilder.vertex(matrix4f, x + width, y + f, (float) z).color(color.x, color.y, color.z, color.w).uv(0, 0).uv2(15728880).normal(0.0F, 0.0F, 0.0F).endVertex();
        }
        BufferUploader.drawWithShader(bufferbuilder.end());
        lightTexture.turnOffLightLayer();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }

    public void blitLevel(GuiGraphics graphics, PoseStack stack, float x, float y, float width, float height, int z, float a) {
        Minecraft mc = Minecraft.getInstance();
        RenderSystem.setShaderTexture(0, new ResourceLocation(Revelationfix.MODID, "textures/particle/white.png"));

        Matrix4f matrix4f = stack.last().pose();
        Vector4f color = new Vector4f(1.0F, 1.0F, 1.0F, a);
        LightTexture lightTexture = mc.gameRenderer.lightTexture();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
        lightTexture.turnOnLightLayer();
        VertexConsumer consumer = graphics.bufferSource().getBuffer(RenderType.gui());
        float yStep = height / (float) (level);
        for (float f = 0.0F; f < height; f += yStep) {
            consumer.vertex(matrix4f, x, y + f, (float) z).color(color.x, color.y, color.z, color.w).uv(0, 1).uv2(15728880).normal(0.0F, 0.0F, 0.0F).endVertex();
            consumer.vertex(matrix4f, x, y + f + yStep * 0.6F, (float) z).color(color.x, color.y, color.z, color.w).uv(1, 1).uv2(15728880).normal(0.0F, 0.0F, 0.0F).endVertex();
            consumer.vertex(matrix4f, x + width, y + f + yStep * 0.6F, (float) z).color(color.x, color.y, color.z, color.w).uv(1, 0).uv2(15728880).normal(0.0F, 0.0F, 0.0F).endVertex();
            consumer.vertex(matrix4f, x + width, y + f, (float) z).color(color.x, color.y, color.z, color.w).uv(0, 0).uv2(15728880).normal(0.0F, 0.0F, 0.0F).endVertex();
        }
        graphics.bufferSource().endBatch();
        lightTexture.turnOffLightLayer();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }
}
