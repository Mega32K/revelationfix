package com.mega.revelationfix.client.renderer.entity;

import com.mega.endinglib.mixin.accessor.AccessorLivingEntityRenderer;
import com.mega.revelationfix.common.entity.ShadowPlayerEntity;
import com.mega.revelationfix.safe.entity.PlayerModelSuperCaller;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.phys.Vec3;

public class WrappedPlayerRenderer extends PlayerRenderer {
    public WrappedPlayerRenderer(EntityRendererProvider.Context p_174557_, boolean p_174558_) {
        super(p_174557_, p_174558_);
        ((AccessorLivingEntityRenderer) this).getLayers().removeIf(layer -> layer instanceof ItemInHandLayer<?, ?>);
    }
    private static HumanoidModel.ArmPose getArmPose(ShadowPlayerEntity shadowPlayer, AbstractClientPlayer p_117795_, InteractionHand hand) {
        ItemStack itemstack = hand == InteractionHand.MAIN_HAND ? shadowPlayer.sMainHandItem : shadowPlayer.sOffHandItem;
        if (itemstack.isEmpty()) {
            return HumanoidModel.ArmPose.EMPTY;
        } else {
            if (p_117795_.getUsedItemHand() == hand && p_117795_.getUseItemRemainingTicks() > 0) {
                UseAnim useanim = itemstack.getUseAnimation();
                if (useanim == UseAnim.BLOCK) {
                    return HumanoidModel.ArmPose.BLOCK;
                }

                if (useanim == UseAnim.BOW) {
                    return HumanoidModel.ArmPose.BOW_AND_ARROW;
                }

                if (useanim == UseAnim.SPEAR) {
                    return HumanoidModel.ArmPose.THROW_SPEAR;
                }

                if (useanim == UseAnim.CROSSBOW && hand == p_117795_.getUsedItemHand()) {
                    return HumanoidModel.ArmPose.CROSSBOW_CHARGE;
                }

                if (useanim == UseAnim.SPYGLASS) {
                    return HumanoidModel.ArmPose.SPYGLASS;
                }

                if (useanim == UseAnim.TOOT_HORN) {
                    return HumanoidModel.ArmPose.TOOT_HORN;
                }

                if (useanim == UseAnim.BRUSH) {
                    return HumanoidModel.ArmPose.BRUSH;
                }
            } else if (!p_117795_.swinging && itemstack.getItem() instanceof CrossbowItem && CrossbowItem.isCharged(itemstack)) {
                return HumanoidModel.ArmPose.CROSSBOW_HOLD;
            }

            HumanoidModel.ArmPose forgeArmPose = net.minecraftforge.client.extensions.common.IClientItemExtensions.of(itemstack).getArmPose(p_117795_, hand, itemstack);
            if (forgeArmPose != null) return forgeArmPose;

            return HumanoidModel.ArmPose.ITEM;
        }
    }

    private void setModelProperties(ShadowPlayerEntity s, AbstractClientPlayer p_117819_) {
        PlayerModel<AbstractClientPlayer> playermodel = this.getModel();
        if (p_117819_.isSpectator()) {
            playermodel.setAllVisible(false);
            playermodel.head.visible = true;
            playermodel.hat.visible = true;
        } else {
            playermodel.setAllVisible(true);
            playermodel.hat.visible = p_117819_.isModelPartShown(PlayerModelPart.HAT);
            playermodel.jacket.visible = p_117819_.isModelPartShown(PlayerModelPart.JACKET);
            playermodel.leftPants.visible = p_117819_.isModelPartShown(PlayerModelPart.LEFT_PANTS_LEG);
            playermodel.rightPants.visible = p_117819_.isModelPartShown(PlayerModelPart.RIGHT_PANTS_LEG);
            playermodel.leftSleeve.visible = p_117819_.isModelPartShown(PlayerModelPart.LEFT_SLEEVE);
            playermodel.rightSleeve.visible = p_117819_.isModelPartShown(PlayerModelPart.RIGHT_SLEEVE);
            playermodel.crouching = p_117819_.isCrouching();
            HumanoidModel.ArmPose humanoidmodel$armpose = getArmPose(s, p_117819_, InteractionHand.MAIN_HAND);
            HumanoidModel.ArmPose humanoidmodel$armpose1 = getArmPose(s, p_117819_, InteractionHand.OFF_HAND);
            if (humanoidmodel$armpose.isTwoHanded()) {
                humanoidmodel$armpose1 = s.sOffHandItem.isEmpty() ? HumanoidModel.ArmPose.EMPTY : HumanoidModel.ArmPose.ITEM;
            }

            if (p_117819_.getMainArm() == HumanoidArm.RIGHT) {
                playermodel.rightArmPose = humanoidmodel$armpose;
                playermodel.leftArmPose = humanoidmodel$armpose1;
            } else {
                playermodel.rightArmPose = humanoidmodel$armpose1;
                playermodel.leftArmPose = humanoidmodel$armpose;
            }
        }

    }
    protected void setupRotations(ShadowPlayerEntity shadowPlayer, AbstractClientPlayer p_117802_, PoseStack p_117803_, float p_117804_, float p_117805_, float p_117806_) {
        float f = shadowPlayer.getSwimAmount(p_117806_);
        if (p_117802_.isFallFlying()) {
            ssetupRotations(shadowPlayer,p_117802_, p_117803_, p_117804_, p_117805_, p_117806_);
            float f1 = (float)p_117802_.getFallFlyingTicks() + p_117806_;
            float f2 = Mth.clamp(f1 * f1 / 100.0F, 0.0F, 1.0F);
            if (!p_117802_.isAutoSpinAttack()) {
                p_117803_.mulPose(Axis.XP.rotationDegrees(f2 * (-90.0F - shadowPlayer.sXRot)));
            }

            Vec3 vec3 = p_117802_.getViewVector(p_117806_);
            Vec3 vec31 = p_117802_.getDeltaMovementLerped(p_117806_);
            double d0 = vec31.horizontalDistanceSqr();
            double d1 = vec3.horizontalDistanceSqr();
            if (d0 > 0.0D && d1 > 0.0D) {
                double d2 = (vec31.x * vec3.x + vec31.z * vec3.z) / Math.sqrt(d0 * d1);
                double d3 = vec31.x * vec3.z - vec31.z * vec3.x;
                p_117803_.mulPose(Axis.YP.rotation((float)(Math.signum(d3) * Math.acos(d2))));
            }
        } else if (f > 0.0F) {
            ssetupRotations(shadowPlayer,p_117802_, p_117803_, p_117804_, p_117805_, p_117806_);
            float f3 = p_117802_.isInWater() || p_117802_.isInFluidType((fluidType, height) -> p_117802_.canSwimInFluidType(fluidType)) ? -90.0F - p_117802_.getXRot() : -90.0F;
            float f4 = Mth.lerp(f, 0.0F, f3);
            p_117803_.mulPose(Axis.XP.rotationDegrees(f4));
            if (p_117802_.isVisuallySwimming()) {
                p_117803_.translate(0.0F, -1.0F, 0.3F);
            }
        } else {
            ssetupRotations(shadowPlayer,p_117802_, p_117803_, p_117804_, p_117805_, p_117806_);
        }

    }

    protected void ssetupRotations(ShadowPlayerEntity shadowPlayer, AbstractClientPlayer p_115317_, PoseStack p_115318_, float p_115319_, float p_115320_, float p_115321_) {
        if (this.isShaking(p_115317_)) {
            p_115320_ += (float)(Math.cos((double)p_115317_.tickCount * 3.25D) * Math.PI * (double)0.4F);
        }

        if (!shadowPlayer.hasPose(Pose.SLEEPING)) {
            p_115318_.mulPose(Axis.YP.rotationDegrees(180.0F - p_115320_));
        }
        if (p_115317_.isAutoSpinAttack()) {
            p_115318_.mulPose(Axis.XP.rotationDegrees(-90.0F - shadowPlayer.sXRot));
            p_115318_.mulPose(Axis.YP.rotationDegrees(((float)p_115317_.tickCount + p_115321_) * -75.0F));
        } else if (shadowPlayer.hasPose(Pose.SLEEPING)) {
            Direction direction = p_115317_.getBedOrientation();
            float f1 = direction != null ? sleepDirectionToRotation(direction) : p_115320_;
            p_115318_.mulPose(Axis.YP.rotationDegrees(f1));
            p_115318_.mulPose(Axis.ZP.rotationDegrees(this.getFlipDegrees(p_115317_)));
            p_115318_.mulPose(Axis.YP.rotationDegrees(270.0F));
        } else if (isEntityUpsideDown(p_115317_)) {
            p_115318_.translate(0.0F, p_115317_.getBbHeight() + 0.1F, 0.0F);
            p_115318_.mulPose(Axis.ZP.rotationDegrees(180.0F));
        }

    }
    private static float sleepDirectionToRotation(Direction p_115329_) {
        switch (p_115329_) {
            case SOUTH:
                return 90.0F;
            case WEST:
                return 0.0F;
            case NORTH:
                return 270.0F;
            case EAST:
                return 180.0F;
            default:
                return 0.0F;
        }
    }
    public void render(ShadowPlayerEntity shadowPlayer, AbstractClientPlayer clientPlayer, float p_117789_, float pTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        this.setModelProperties(shadowPlayer, clientPlayer);
        poseStack.pushPose();
        this.model.attackTime = shadowPlayer.sAttackAnim;

        boolean shouldSit = clientPlayer.isPassenger() && (clientPlayer.getVehicle() != null && clientPlayer.getVehicle().shouldRiderSit());
        this.model.riding = shouldSit;
        this.model.young = clientPlayer.isBaby();
        float f = Mth.rotLerp(pTicks, shadowPlayer.sYBodyRotOld, shadowPlayer.sYBodyRot);
        float f1 = Mth.rotLerp(pTicks, shadowPlayer.sYHeadRotOld, shadowPlayer.sYHeadRot);
        float f2 = f1 - f;
        if (shouldSit && clientPlayer.getVehicle() instanceof LivingEntity) {
            LivingEntity livingentity = (LivingEntity)clientPlayer.getVehicle();
            f = Mth.rotLerp(pTicks, shadowPlayer.sYBodyRotOld, shadowPlayer.sYBodyRot);
            f2 = f1 - f;
            float f3 = Mth.wrapDegrees(f2);
            if (f3 < -85.0F) {
                f3 = -85.0F;
            }

            if (f3 >= 85.0F) {
                f3 = 85.0F;
            }

            f = f1 - f3;
            if (f3 * f3 > 2500.0F) {
                f += f3 * 0.2F;
            }

            f2 = f1 - f;
        }

        float f6 = Mth.lerp(pTicks, shadowPlayer.sXRotOld, shadowPlayer.sXRot);
        if (isEntityUpsideDown(clientPlayer)) {
            f6 *= -1.0F;
            f2 *= -1.0F;
        }

        float f7 = this.getBob(clientPlayer, pTicks);
        this.setupRotations(shadowPlayer, clientPlayer, poseStack, f7, f, pTicks);
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        this.scale(clientPlayer, poseStack, pTicks);
        poseStack.translate(0.0F, -1.501F, 0.0F);
        float f8 = 0.0F;
        float f5 = 0.0F;
        if (!shouldSit && clientPlayer.isAlive()) {
            f8 = shadowPlayer.sWalkSpeed;
            f5 = shadowPlayer.sWalkPosition;
            if (clientPlayer.isBaby()) {
                f5 *= 3.0F;
            }

            if (f8 > 1.0F) {
                f8 = 1.0F;
            }
        }

        this.model.crouching = shadowPlayer.crouching;
        this.model.prepareMobModel(clientPlayer, f5, f8, pTicks);
        ((PlayerModelSuperCaller) this.model).setupAnimPublic(clientPlayer, f5, f8, f7, f2, f6);
        Minecraft minecraft = Minecraft.getInstance();
        boolean flag = this.isBodyVisible(clientPlayer);
        boolean flag1 = !flag && !clientPlayer.isInvisibleTo(minecraft.player);
        boolean flag2 = minecraft.shouldEntityAppearGlowing(clientPlayer);
        RenderType rendertype = this.getRenderType(clientPlayer, flag, flag1, flag2);
        if (rendertype != null) {
            VertexConsumer vertexconsumer = bufferSource.getBuffer(rendertype);
            int i = getOverlayCoords(clientPlayer, this.getWhiteOverlayProgress(clientPlayer, pTicks));
            this.model.renderToBuffer(poseStack, vertexconsumer, light, i, 1F, 1F, 1F, Mth.clamp(1.8F - (shadowPlayer.tickCount + pTicks) / 7F, 0.0F, 1.0F));
        }

        if (!clientPlayer.isSpectator()) {
            for(var renderlayer : this.layers) {
                renderlayer.render(poseStack, bufferSource, light, clientPlayer, f5, f8, pTicks, f7, f2, f6);
            }
        }

        poseStack.popPose();

    }
}
