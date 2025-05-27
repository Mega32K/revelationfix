package com.mega.revelationfix.mixin.gr;

import com.mojang.blaze3d.Blaze3D;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;
import org.joml.Vector2d;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import z1gned.goetyrevelation.client.event.NetherStarShaders;
import z1gned.goetyrevelation.client.render.ui.NetherStarBar;

@Mixin(value = NetherStarBar.class, remap = false)
public abstract class NetherStarBarMixin {
    @Shadow
    @Final
    private static Minecraft mc;

    @Shadow
    static void enableCosmicShader(float yaw, float pitch, float scale, int hurt) {
    }

    @ModifyVariable(method = "enableCosmicShader", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private static float modifyYaw(float v) {
        return (float) Blaze3D.getTime() * 0.1F * Mth.DEG_TO_RAD;
    }

    /**
     * @author MegaDarkness
     * @reason replace all
     */
    @Overwrite
    public static void blitCosmicBar(PoseStack stack, ResourceLocation p_283461_, float p_281399_, float p_283222_, float p_283615_, float p_283430_, int p_281729_, float p_283247_, float p_282598_, float p_282883_, float p_283017_, boolean shake, int hurt) {
        RenderSystem.setShaderTexture(0, p_283461_);
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

        float scale = 100.0F;
        enableCosmicShader(yaw, pitch, scale, hurt);
        Matrix4f matrix4f = stack.last().pose();
        Vector4f color = new Vector4f(1.0F);
        LightTexture lightTexture = mc.gameRenderer.lightTexture();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
        lightTexture.turnOnLightLayer();
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.BLOCK);
        bufferbuilder.vertex(matrix4f, p_281399_, p_283615_, (float) p_281729_).color(color.x, color.y, color.z, color.w).uv(p_283247_, p_282883_).uv2(15728880).normal(0.0F, 0.0F, 0.0F).endVertex();
        bufferbuilder.vertex(matrix4f, p_281399_, p_283430_, (float) p_281729_).color(color.x, color.y, color.z, color.w).uv(p_283247_, p_283017_).uv2(15728880).normal(0.0F, 0.0F, 0.0F).endVertex();
        bufferbuilder.vertex(matrix4f, p_283222_, p_283430_, (float) p_281729_).color(color.x, color.y, color.z, color.w).uv(p_282598_, p_283017_).uv2(15728880).normal(0.0F, 0.0F, 0.0F).endVertex();
        bufferbuilder.vertex(matrix4f, p_283222_, p_283615_, (float) p_281729_).color(color.x, color.y, color.z, color.w).uv(p_282598_, p_282883_).uv2(15728880).normal(0.0F, 0.0F, 0.0F).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
        lightTexture.turnOffLightLayer();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }
}
