package com.mega.revelationfix.common.apollyon.client;

import com.Polarice3.Goety.client.render.model.ApostleModel;
import com.Polarice3.Goety.common.entities.boss.Apostle;
import com.mega.revelationfix.common.apollyon.common.DeathPerformance;
import com.mega.revelationfix.util.LivingEntityEC;
import com.mojang.blaze3d.Blaze3D;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import z1gned.goetyrevelation.util.Easing;
import z1gned.goetyrevelation.util.RendererUtils;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(
        bus = Mod.EventBusSubscriber.Bus.FORGE,
        value = {Dist.CLIENT}
)
public class ClientDeathPerformance {
    @SubscribeEvent
    public static void apollyonDeath(RenderLivingEvent<Apostle, ApostleModel<Apostle>> event) {
        LivingEntity var2 = event.getEntity();
        if (var2 instanceof Apostle apostle) {
            if (apostle.isInNether() && apostle.isDeadOrDying()) {
                //from 560 to 0
                int leftTime = DeathPerformance.getLeftTime(apostle);
                if (leftTime <= 0) {
                    return;
                }
                //from 0 to 560
                int finalDeathTime = DeathPerformance.getFinalDeathTime(apostle);
                int time = DeathPerformance.getDeathGrowingTime(apostle);
                int oldTime = ((LivingEntityEC) apostle).revelationfix$livingECData().apollyonLastGrowingTime;
                boolean isEnding = finalDeathTime != -1;
                if (isEnding) {
                    time += finalDeathTime;
                    oldTime += finalDeathTime;
                }
                float partialTick = Mth.lerp(event.getPartialTick(), oldTime, time);
                /*
                    0 -> 60
                    partial : partialTime / 60 == [0F->1F]

                    60 -> 500 : 1F

                    500 -> 560 : 1F - ((partialTime - 500) / 60) == 1F - ([0F->60F]/60) == 1F - [0F->1F] == [1F->0F]
                 */
                float percent = Easing.IN_OUT_SINE.calculate(partialTick < DeathPerformance.CHANGE_TIME ? partialTick / DeathPerformance.CHANGE_TIME : (isEnding ? 1.0F - (partialTick - (DeathPerformance.MAX_TIME - DeathPerformance.CHANGE_TIME)) / DeathPerformance.CHANGE_TIME : 1.0F));
                float scale = 9F * 1.309017F * Math.max(0.0F, percent);
                PoseStack stack = event.getPoseStack();
                MultiBufferSource.BufferSource source = Minecraft.getInstance().renderBuffers().bufferSource();
                stack.pushPose();
                float degrees = (float) Blaze3D.getTime() * 50.0F;
                stack.mulPose(Axis.XP.rotationDegrees(degrees));
                stack.mulPose(Axis.YP.rotationDegrees(degrees));
                stack.mulPose(Axis.ZP.rotationDegrees(degrees));
                RendererUtils.renderRegularIcosahedron(stack, source, scale, 15728880, 0.5411765F, 0.0F, 0.105882354F, percent, percent);
                stack.popPose();
            }
        }
    }
}
