package com.mega.revelationfix.common.odamane.client;

import com.mega.endinglib.api.client.Easing;
import com.mega.endinglib.util.time.TimeContext;
import com.mega.revelationfix.Revelationfix;
import com.mega.revelationfix.client.citadel.GRRenderTypes;
import com.mega.revelationfix.client.citadel.PostEffectRegistry;
import com.mega.revelationfix.client.renderer.MegaRenderType;
import com.mega.revelationfix.common.compat.SafeClass;
import com.mega.revelationfix.common.config.ClientConfig;
import com.mega.revelationfix.proxy.ClientProxy;
import com.mega.revelationfix.util.entity.ATAHelper2;
import com.mojang.blaze3d.Blaze3D;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;


@OnlyIn(Dist.CLIENT)
public class OdamaneHaloLayer implements ICurioRenderer {
    /*
    public static final ResourceLocation HALO_TEX = new ResourceLocation(Revelationfix.MODID, "textures/entity/player/uom_halo.png");
     */
    public static final ResourceLocation HALO_TEX = new ResourceLocation(Revelationfix.MODID, "textures/entity/player/halo_the_end.png");
    static RenderType secondVanilla;
    static RenderType vanilla;
    static RenderType shader;
    static RenderType eyesAlpha;
    private OdamaneHaloModel<AbstractClientPlayer> model;

    float smoothstep(float t1, float t2, float x) {
        float time = x * 2F;
        return (Easing.IN_OUT_QUART.interpolate((time % 180.0F) / 180.0F, 0F, 1F) * 180.0F) * ((float) Math.PI / 180F);
    }

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack itemStack, SlotContext slotContext, PoseStack poseStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource multiBufferSource, int light, float limbSwing,
                                                                          float limbSwingAmount,
                                                                          float partialTicks,
                                                                          float ageInTicks,
                                                                          float netHeadYaw,
                                                                          float headPitch) {
        if (model == null) {
            model = ClientProxy.getInstance().getPlayerRendererContext().ODAMANE_HALO_MODEL;
            if (model == null) return;
        }
        if (secondVanilla == null || vanilla == null || shader == null || eyesAlpha == null) {
            secondVanilla = GRRenderTypes.getOdamane(HALO_TEX);
            vanilla = ClientProxy.getInstance().getPlayerRendererContext().ODAMANE_HALO_MODEL.renderType(HALO_TEX);
            shader = RenderType.energySwirl(HALO_TEX, 1f, 1f);
            eyesAlpha = MegaRenderType.getEyesAlphaEnabled(HALO_TEX);
        }
        Player entity;
        if (slotContext.entity() instanceof Player player)
            entity = player;
        else return;
        /*

        if (!SafeClass.usingShaderPack()) {
            if (ATAHelper2.hasOdamane(entity) && !entity.isInvisible()) {
                float zRot = smoothstep(0.0F, 180.0F, (entity.tickCount + partialTicks));
                light = 0xFF00F0;
                this.model.head.translateAndRotate(poseStack);
                boolean isSecondPhase = entity.getHealth() / entity.getMaxHealth() < 0.5F;
                RenderType renderType = isSecondPhase ? (GRRenderTypes.getOdamane(HALO_TEX)) : this.model.renderType(HALO_TEX);

                poseStack.pushPose();
                this.model.BackStar.zRot = zRot;
                this.model.hole2.zRot = (entity.tickCount + partialTicks) / 22.5F;
                MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
                VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
                if (isSecondPhase)
                    PostEffectRegistry.renderEffectForNextTick(ClientProxy.ODAMANE_SHADER);
                this.model.hole2.render(poseStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY);
                this.model.BackStar.render(poseStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY);
                if (isSecondPhase) {
                    bufferSource.endBatch();
                    Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
                }
                poseStack.popPose();
            }
        } else
        if (ATAHelper2.hasOdamane(entity) && !entity.isInvisible()) {
            float zRot = smoothstep(0.0F, 180.0F, (entity.tickCount + partialTicks) );
            light = 0xFF00F0;
            this.model.head.translateAndRotate(poseStack);
            boolean isSecondPhase = entity.getHealth() / entity.getMaxHealth() < 0.5F;
            RenderType renderType = isSecondPhase ?  RenderType.energySwirl(HALO_TEX, 1f, 1f) : this.model.renderType(HALO_TEX);
            poseStack.pushPose();
            this.model.BackStar.zRot = zRot;
            this.model.hole2.zRot = (entity.tickCount + partialTicks) / 22.5F;
            MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
            VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
            this.model.hole2.render(poseStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY);
            this.model.BackStar.render(poseStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY);
            bufferSource.endBatch();
            VertexConsumer vertexConsumer2 = bufferSource.getBuffer(MegaRenderType.getEyesAlphaEnabled(HALO_TEX));
            RenderSystem.setShaderColor(1f, 1f, 1f, isSecondPhase ? 2F : 1f + Mth.abs(Mth.sin(TimeContext.Client.getCommonDegrees() / 44.5F) / 1.5F) + 0.1F);
            this.model.hole2.render(poseStack, vertexConsumer2, light, OverlayTexture.NO_OVERLAY);
            this.model.BackStar.render(poseStack, vertexConsumer2, light, OverlayTexture.NO_OVERLAY);
            bufferSource.endLastBatch();
            poseStack.popPose();
        }
         */


        if (!SafeClass.usingShaderPack()) {
            if (ATAHelper2.hasOdamane(entity) && !entity.isInvisible()) {
                //float zRot = smoothstep(0.0F, 180.0F, ());
                float zRot = (Mth.cos(player.getName().getString().length()) * 3201123F + (float) Blaze3D.getTime() * 90F) * Mth.DEG_TO_RAD;
                light = 0xFF00F0;
                poseStack.pushPose();
                this.model.head.translateAndRotate(poseStack);
                this.model.Halo.translateAndRotate(poseStack);
                boolean isSecondPhase = entity.getHealth() / entity.getMaxHealth() < 0.5F;
                RenderType renderType = isSecondPhase ? (ClientConfig.enableSpecialHaloEffect ? secondVanilla : shader) : vanilla;


                poseStack.translate(0, -.0, -.5);
                this.model.bone2.zRot = zRot;
                this.model.bone3.zRot = zRot;
                this.model.bone4.zRot = -zRot;
                this.model.bone5.zRot = -zRot;
                this.model.bone.y = (Mth.cos(zRot) + 1);
                MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
                VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
                if (isSecondPhase && ClientConfig.enableSpecialHaloEffect)
                    PostEffectRegistry.renderEffectForNextTick(ClientProxy.ODAMANE_SHADER);
                this.model.Halo.render(poseStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY);
                if (isSecondPhase && ClientConfig.enableSpecialHaloEffect) {
                    bufferSource.endBatch();
                    Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
                }
                poseStack.popPose();
            }
        } else if (ATAHelper2.hasOdamane(entity) && !entity.isInvisible()) {
            float zRot = (Mth.cos(player.getName().getString().length()) * 3201123F + (float) Blaze3D.getTime() * 90F) * Mth.DEG_TO_RAD;
            light = 0xFF00F0;
            poseStack.pushPose();
            this.model.head.translateAndRotate(poseStack);
            this.model.Halo.translateAndRotate(poseStack);
            boolean isSecondPhase = entity.getHealth() / entity.getMaxHealth() < 0.5F;
            RenderType renderType = isSecondPhase ? shader : vanilla;

            poseStack.translate(0, .3, -.5);
            this.model.bone2.zRot = zRot;
            this.model.bone3.zRot = zRot;
            this.model.bone4.zRot = -zRot;
            this.model.bone5.zRot = -zRot;
            this.model.bone.y = (Mth.cos(zRot) + 1);
            MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
            VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
            this.model.Halo.render(poseStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY);
            bufferSource.endBatch();
            VertexConsumer vertexConsumer2 = bufferSource.getBuffer(eyesAlpha);
            RenderSystem.setShaderColor(1f, 1f, 1f, isSecondPhase ? 2F : 1f + Mth.abs(Mth.sin(TimeContext.Client.getCommonDegrees() / 44.5F) / 1.5F) + 0.1F);
            this.model.Halo.render(poseStack, vertexConsumer2, light, OverlayTexture.NO_OVERLAY);
            bufferSource.endLastBatch();
            poseStack.popPose();
        }

    }
}
