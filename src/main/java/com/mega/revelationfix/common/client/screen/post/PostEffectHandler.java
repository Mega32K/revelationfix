package com.mega.revelationfix.common.client.screen.post;

import com.mega.revelationfix.common.client.screen.CustomScreenEffect;
import com.mojang.blaze3d.shaders.Uniform;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.nio.FloatBuffer;

@OnlyIn(Dist.CLIENT)
public class PostEffectHandler {
    private static final Int2ObjectArrayMap<CustomScreenEffect> data = new Int2ObjectArrayMap<>();
    private static int index = 0;

    /**
     * @param effect
     */
    public static void registerEffect(CustomScreenEffect effect) {
        if (effect == null)
            throw new NullPointerException("null post effect, caller:" + new Exception().getStackTrace()[2].getClassName());
        data.put(index, effect);
        index++;
    }

    public static Int2ObjectArrayMap<CustomScreenEffect> getData() {
        return data;
    }

    @SuppressWarnings("DataFlowIssue")
    public static void updateUniform_post(CustomScreenEffect effect, String name, float value) {
        if (effect == null || effect.current() == null)
            return;
        for (PostPass s : effect.current().passes) {
            s.getEffect().safeGetUniform(name).set(value);
        }
    }

    @SuppressWarnings("DataFlowIssue")
    public static void updateUniform_post(PostChain chain, String name, float value) {
        if (chain == null)
            return;
        for (PostPass s : chain.passes) {
            s.getEffect().safeGetUniform(name).set(value);
        }
    }

    @SuppressWarnings("DataFlowIssue")
    public static void updateUniform_post(PostChain chain, String name, float[] values) {
        if (chain == null)
            return;
        for (PostPass s : chain.passes) {
            s.getEffect().safeGetUniform(name).set(values);
        }
    }

    @SuppressWarnings("DataFlowIssue")
    public static void updateUniform_post(CustomScreenEffect effect, String name, float[] values) {
        if (effect == null || effect.current() == null)
            return;
        for (PostPass s : effect.current().passes) {
            s.getEffect().safeGetUniform(name).set(values);
        }
    }

    public static float getUniform_post(CustomScreenEffect effect, String name) {
        if (effect == null || effect.current() == null)
            return 0F;
        for (PostPass s : effect.current().passes) {
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

    @SuppressWarnings("DataFlowIssue")
    public static float[] getUniform_post(CustomScreenEffect effect, String name, int length) {
        if (effect == null || effect.current() == null)
            return new float[]{};
        for (PostPass s : effect.current().passes) {
            Uniform uniform = s.getEffect().getUniform(name);
            if (uniform != null) {
                FloatBuffer floatBuffer = uniform.getFloatBuffer();
                float[] floats = new float[length];
                for (int i = 0; i < length; i++)
                    floats[i] = floatBuffer.get(i);
                uniform.markDirty();
                return floats;
            }
        }
        return new float[]{};
    }
}
