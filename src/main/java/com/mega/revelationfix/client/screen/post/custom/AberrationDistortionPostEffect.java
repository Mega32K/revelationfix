package com.mega.revelationfix.client.screen.post.custom;

import com.Polarice3.Goety.common.blocks.entities.DarkAltarBlockEntity;
import com.mega.revelationfix.Revelationfix; 
import com.mega.revelationfix.client.screen.CustomScreenEffect;
import com.mega.revelationfix.client.screen.post.PostEffectHandler;
import com.mega.revelationfix.safe.level.ClientLevelExpandedContext;
import com.mega.revelationfix.safe.level.ClientLevelInterface;
import com.mega.endinglib.api.client.Easing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AberrationDistortionPostEffect implements CustomScreenEffect {
    public static AberrationDistortionPostEffect INSTANCE;
    int integer = -1;
    float nowRandMul = 0.0F;
    int stayingTime = 0;
    float maxSize = 20.0F;
    Minecraft mc = Minecraft.getInstance();

    public AberrationDistortionPostEffect() {
        INSTANCE = this;
    }

    @Override
    public String getName() {
        return "aberration_distortion_post_effect";
    }

    @Override
    public ResourceLocation getShaderLocation() {
        return new ResourceLocation(Revelationfix.MODID, "shaders/post/aberration.json");
    }

    @Override
    public void onRenderTick(float partialTicks) {
        if (mc.player != null) {
            ClientLevelExpandedContext context = ((ClientLevelInterface) mc.level).revelationfix$ECData();
            BlockPos blockPos = context.teEndRitualBE;
            float distance = (float) Math.sqrt(mc.player.distanceToSqr(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
            if (distance > 40) {
                BlockState state = mc.level.getBlockState(blockPos);
                if (state.isAir()
                        || (!(mc.level.getBlockEntity(blockPos) instanceof DarkAltarBlockEntity darkAltarBlockEntity)
                        || (darkAltarBlockEntity.castingPlayer == null || darkAltarBlockEntity.castingPlayerId == null))) {
                    context.teEndRitualBE = null;
                    context.teEndRitualRunning = false;
                }
                return;
            }
            {
                float f = (maxSize - distance) * .1F / (maxSize / 20.0F);
                PostEffectHandler.updateUniform_post(this, "Multiplier", Easing.IN_SINE.interpolate(f, 0F, 2F) * 0.01F + (float) (Math.random() - 0.5F) * (1.0F - distance / maxSize) * 0.08F * 0.2F);

            }
        }
    }

    @Override
    public boolean canUse() {
        LocalPlayer player = mc.player;
        if (player != null) {
            if (player.level().dimension() != Level.END) return false;
            ClientLevelExpandedContext context = ((ClientLevelInterface) player.level()).revelationfix$ECData();
            return !player.isSpectator() && context.teEndRitualBE != null && context.teEndRitualRunning;
        }
        return false;
    }
}
