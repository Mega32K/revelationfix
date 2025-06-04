package com.mega.revelationfix.mixin.gr;

import com.Polarice3.Goety.client.render.ApostleRenderer;
import com.Polarice3.Goety.client.render.CultistRenderer;
import com.Polarice3.Goety.client.render.model.CultistModel;
import com.Polarice3.Goety.common.entities.boss.Apostle;
import com.mega.revelationfix.Revelationfix;
import com.mega.revelationfix.common.compat.SafeClass;
import com.mega.revelationfix.common.config.ClientConfig;
import com.mega.revelationfix.safe.entity.Apollyon2Interface;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import z1gned.goetyrevelation.util.ApollyonAbilityHelper;

@Mixin(ApostleRenderer.class)
public abstract class ApostleRendererMixin extends CultistRenderer<Apostle> {
    @Unique
    private static final ResourceLocation revelationfix$TEXTURE = new ResourceLocation("goety_revelation", "textures/entity/apollyon/apollyon.png");
    @Unique
    private static final ResourceLocation TEXTURE_2 = new ResourceLocation("goety_revelation", "textures/entity/apollyon/apollyon_second.png");
    @Unique
    private static final ResourceLocation NETHER_TEXTURE = new ResourceLocation(Revelationfix.MODID, "textures/entity/apollyon/apollyon.png");
    @Unique
    private static final ResourceLocation NETHER_TEXTURE_2 = new ResourceLocation(Revelationfix.MODID, "textures/entity/apollyon/apollyon2.png");

    protected ApostleRendererMixin(EntityRendererProvider.Context p_i50966_1_, CultistModel<Apostle> p_i50966_2_, float p_i50966_3_) {
        super(p_i50966_1_, p_i50966_2_, p_i50966_3_);
    }

    @Inject(
            at = {@At("HEAD")},
            method = {"getTextureLocation(Lcom/Polarice3/Goety/common/entities/boss/Apostle;)Lnet/minecraft/resources/ResourceLocation;"},
            cancellable = true,
            remap = false
    )
    private void setTexture(Apostle entity, CallbackInfoReturnable<ResourceLocation> cir) {
        if (((ApollyonAbilityHelper) entity).allTitlesApostle_1_20_1$isApollyon()) {
            if (entity.isInNether() && ClientConfig.enableNewNetherApollyonTexture)
                cir.setReturnValue(entity.isSecondPhase() ? NETHER_TEXTURE_2 : NETHER_TEXTURE);
            else cir.setReturnValue(entity.isSecondPhase() ? TEXTURE_2 : revelationfix$TEXTURE);
        }

    }

    @Inject(
            method = "render(Lcom/Polarice3/Goety/common/entities/boss/Apostle;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(value = "HEAD"),
            remap = false,
            cancellable = true
    )
    private void render(Apostle pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, CallbackInfo ci) {
        ApollyonAbilityHelper helper = (ApollyonAbilityHelper) pEntity;
        if (pEntity.isDeadOrDying() || pEntity.isRemoved()) return;
        if (helper.allTitlesApostle_1_20_1$isApollyon() &&
                pEntity.isSecondPhase() &&
                pEntity.isInNether() &&
                SafeClass.isDoom(pEntity)) {
            Apollyon2Interface apollyon2Interface = (Apollyon2Interface) pEntity;
            if (!apollyon2Interface.revelaionfix$illusionMode()) {
                ci.cancel();
                revelationfix$superRender(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
                return;
            }
            ci.cancel();
            Vec3[] avec3 = apollyon2Interface.revelaionfix$getIllusionOffsets(pPartialTicks);
            float f = this.getBob(pEntity, pPartialTicks);
            revelationfix$superRender(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
            for (int i = 0; i < avec3.length; ++i) {
                pMatrixStack.pushPose();
                pMatrixStack.translate(avec3[i].x + (double) Mth.cos((float) i + f * 0.5F) * 0.025D, avec3[i].y + (double) Mth.cos((float) i + f * 0.75F) * 0.0125D, avec3[i].z + (double) Mth.cos((float) i + f * 0.7F) * 0.025D);
                revelationfix$superRender(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
                pMatrixStack.popPose();
            }
        }
    }

    @Unique
    private void revelationfix$superRender(Apostle pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }

    @Override
    protected boolean isBodyVisible(Apostle pEntity) {
        ApollyonAbilityHelper helper = (ApollyonAbilityHelper) pEntity;
        if (helper.allTitlesApostle_1_20_1$isApollyon() &&
                pEntity.isSecondPhase() &&
                pEntity.isInNether() &&
                SafeClass.isDoom(pEntity)) {
            Apollyon2Interface apollyon2Interface = (Apollyon2Interface) pEntity;
            if (apollyon2Interface.revelaionfix$illusionMode())
                return true;
        }
        return super.isBodyVisible(pEntity);
    }
}
