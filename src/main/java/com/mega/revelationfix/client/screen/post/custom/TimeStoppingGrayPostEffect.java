package com.mega.revelationfix.client.screen.post.custom;

import com.mega.revelationfix.Revelationfix;
import com.mega.revelationfix.client.Easing;
import com.mega.revelationfix.client.RendererUtils;
import com.mega.revelationfix.client.screen.CustomScreenEffect;
import com.mega.revelationfix.client.screen.post.PostEffectHandler;
import com.mega.revelationfix.common.config.ClientConfig;
import com.mega.revelationfix.util.time.TimeContext;
import com.mega.revelationfix.util.time.TimeStopUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TimeStoppingGrayPostEffect implements CustomScreenEffect {
    static Minecraft mc = Minecraft.getInstance();
    float time = 0F;
    float saturation = 1.0F;

    @Override
    public String getName() {
        return "gray_post_effect";
    }

    @Override
    public ResourceLocation getShaderLocation() {
        return new ResourceLocation(Revelationfix.MODID, "shaders/post/gray.json");
    }

    @Override
    public void onRenderTick(float partialTicks) {
        if (time == 0L) {
            time = TimeContext.Client.currentSeconds();
        }
        float time1 = TimeContext.Client.currentSeconds() - time;
        PostEffectHandler.updateUniform_post(this, "Blur", Easing.OUT_CUBIC.interpolate(Math.min(time1 * .5F, .4F), 0F, .4F));
        PostEffectHandler.updateUniform_post(this, "FallOff", Math.max(3F, 12 - time1 * 5));
        PostEffectHandler.updateUniform_post(this, "Saturation", Math.max(0.1F, saturation - time1 * 0.5F));
    }

    @Override
    public boolean canUse() {
        if (!ClientConfig.enableTimeFreezingGrayEffect)
            return false;
        boolean flag = TimeStopUtils.isTimeStop;
        if (!flag) time = 0L;
        if (!flag)
            return false;
        boolean flag0 = RendererUtils.isTimeStop_andSameDimension;
        return flag0;
    }
}
