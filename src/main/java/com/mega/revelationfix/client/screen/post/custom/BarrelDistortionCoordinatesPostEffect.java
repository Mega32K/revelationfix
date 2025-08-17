package com.mega.revelationfix.client.screen.post.custom;

import com.mega.revelationfix.Revelationfix;
import com.mega.revelationfix.client.screen.CustomScreenEffect;
import com.mega.revelationfix.client.screen.post.PostEffectHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BarrelDistortionCoordinatesPostEffect implements CustomScreenEffect {
    public static int tickCount;
    public static Vec2 centerOffset;

    @Override
    public String getName() {
        return "barrel_distortion_coordinates";
    }

    @Override
    public ResourceLocation getShaderLocation() {
        return new ResourceLocation(Revelationfix.MODID, "shaders/post/barrel_distortion_coordinates.json");
    }

    @Override
    public void onRenderTick(float partialTicks) {
        float p = (float) Math.sin((tickCount + partialTicks) / 10F * Mth.PI) * 0.3F;
        PostEffectHandler.updateUniform_post(this, "Distortion", p);
        PostEffectHandler.updateUniform_post(this, "CenterOffset", new float[]{centerOffset.x, centerOffset.y});
    }

    @Override
    public boolean canUse() {

        return tickCount > 0;
    }
}
