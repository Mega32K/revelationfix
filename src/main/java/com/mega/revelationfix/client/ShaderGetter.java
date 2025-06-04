package com.mega.revelationfix.client;

import com.mega.revelationfix.mixin.GameRendererAccessor;
import com.mojang.blaze3d.shaders.Uniform;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FogType;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.nio.FloatBuffer;

public class ShaderGetter {
    public static double fov;
    public static Minecraft mc = Minecraft.getInstance();
    public static GameRenderer gameRenderer = mc.gameRenderer;

    public static PostChain currentEffect() {
        return gameRenderer.currentEffect();
    }

    public static void load(ResourceLocation rs) {
        mc.gameRenderer.loadEffect(rs);
    }

    @SuppressWarnings("DataFlowIssue")
    public static void updateUniform_post(String name, float value) {
        if (currentEffect() == null)
            return;
        for (PostPass s : currentEffect().passes) {
            s.getEffect().safeGetUniform(name).set(value);
        }
    }

    @SuppressWarnings("DataFlowIssue")
    public static void updateUniform_post(String name, float[] value) {
        if (currentEffect() == null)
            return;
        for (PostPass s : currentEffect().passes) {
            s.getEffect().safeGetUniform(name).set(value);
        }
    }

    public static float getUniform_post(String name) {
        if (currentEffect() == null)
            return 0F;
        for (PostPass s : currentEffect().passes) {
            Uniform uniform = s.getEffect().getUniform(name);
            if (uniform != null) {
                FloatBuffer floatBuffer = uniform.getFloatBuffer();
                float value = floatBuffer.get(0);
                uniform.markDirty();
                return value;
            }
        }
        return 0F;
    }

    public static float getFloatUniform(String name) {
        if (currentEffect() == null)
            return Float.NaN;
        for (PostPass s : currentEffect().passes) {
            Uniform su = s.getEffect().getUniform(name);
            if (su != null) {
                su.getFloatBuffer().get(0);
            }
        }
        return Float.NaN;
    }

    public static float[] getMatrixUniform(String name) {
        if (currentEffect() == null)
            return new float[]{Float.NaN};
        for (PostPass s : currentEffect().passes) {
            Uniform su = s.getEffect().getUniform(name);
            if (su != null) {
                su.getFloatBuffer().array();
            }
        }
        return new float[]{Float.NaN};
    }

    public static void updateUniform_post(String name, int value) {
        if (currentEffect() == null)
            return;
        for (PostPass s : currentEffect().passes) {
            Uniform su = s.getEffect().getUniform(name);
            if (su != null) {
                su.set(value);
            }
        }
    }

    @SuppressWarnings("DataFlowIssue")
    public static void updateUniform_core(ShaderInstance ins, String name, float value) {
        if (ins == null)
            return;
        ins.getUniform(name).set(value);
    }


    @SuppressWarnings("DataFlowIssue")
    public static void updateUniform_core(ShaderInstance ins, String name, float[] value) {
        if (ins == null)
            return;
        ins.getUniform(name).set(value);
    }

    public static boolean nameEquals(String name) throws NullPointerException {
        return currentEffect() != null && currentEffect().getName().contains(name);
    }

    /**
     * @param x      in world pos x
     * @param y      in world pos y
     * @param z      in world pos z
     * @param camera the Camera
     * @return the normalized XY position [0.0, 1.0]
     */
    public static Vector2f get021ScreenPosFromWorldPos(double x, double y, double z, Camera camera) {
        //camera position
        double p0 = camera.getPosition().x;
        double p1 = camera.getPosition().y;
        double p2 = camera.getPosition().z;
        //after model view
        Vector4f relativePos = new Vector4f((float) (x - p0), (float) (y - p1), (float) -(z - p2), 1.0F);
        relativePos.rotate(camera.rotation());
        Vector4f rP = relativePos.mul(-1F, -1F, -1F, 1F);
        //屏幕宽高比系数
        float aspect = aspect();
        //近大远小系数
        float f = xyFactor(camera);
        //不需要深度信息的投影透视分割矩阵(投影后归一化)
        Matrix4f matrix4f = new Matrix4f(
                f / aspect / rP.z, 0, 0, 0,
                0, f * aspect / rP.z, 0, 0,
                0, 0, 0, 0,
                0, 0, 1, 0
        );
        rP.mul(matrix4f);
        return new Vector2f(rP.x / 4.0F + .5F, 1 - (-rP.y / 4.0F + .5F));
    }

    public static float aspect() {
        return (float) mc.getWindow().getWidth() / mc.getWindow().getHeight();
    }

    public static float xyFactor(Camera camera) {
        return 1.0F / (float) Math.tan(Math.toRadians(getFov(camera, mc.getPartialTick(), true)) / 2.0F);
    }

    public static double getFov(Camera p_109142_, float p_109143_, boolean p_109144_) {
        GameRendererAccessor accessor = (GameRendererAccessor) mc.gameRenderer;
        if (accessor.panoramicMode()) {
            fov = 90.0D;
        } else {
            double d0 = 70.0D;
            if (p_109144_) {
                d0 = mc.options.fov().get().intValue();
                d0 *= Mth.lerp(p_109143_, accessor.oldFov(), accessor.fov());
            }

            if (p_109142_.getEntity() instanceof LivingEntity && ((LivingEntity) p_109142_.getEntity()).isDeadOrDying()) {
                float f = Math.min((float) ((LivingEntity) p_109142_.getEntity()).deathTime + p_109143_, 20.0F);
                d0 /= (1.0F - 500.0F / (f + 500.0F)) * 2.0F + 1.0F;
            }

            FogType fogtype = p_109142_.getFluidInCamera();
            if (fogtype == FogType.LAVA || fogtype == FogType.WATER) {
                d0 *= Mth.lerp(mc.options.fovEffectScale().get(), 1.0D, 0.85714287F);
            }

            fov = net.minecraftforge.client.ForgeHooksClient.getFieldOfView(mc.gameRenderer, p_109142_, p_109143_, d0, p_109144_);
        }
        return fov;
    }
}
