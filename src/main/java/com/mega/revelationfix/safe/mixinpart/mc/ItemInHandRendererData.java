package com.mega.revelationfix.safe.mixinpart.mc;

import com.mega.revelationfix.client.enums.ModUseAnim;
import com.mega.revelationfix.common.item.combat.GungnirItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class ItemInHandRendererData {
    public static void customAnim(ItemInHandRenderer itemInHandRenderer, AbstractClientPlayer p_109372_, float p_109373_, float p_109374_, InteractionHand p_109375_, float p_109376_, ItemStack p_109377_, float p_109378_, PoseStack p_109379_, MultiBufferSource p_109380_, int p_109381_, CallbackInfo ci, boolean flag, HumanoidArm humanoidarm, boolean flag3, int k) {
        if (p_109377_.getUseAnimation() == ModUseAnim.GUNGNIR_SPEAR) {
            itemInHandRenderer.applyItemArmTransform(p_109379_, humanoidarm, p_109378_);
            p_109379_.translate((float) k * -0.5F, 0.7F, 0.1F);
            p_109379_.mulPose(Axis.XP.rotationDegrees(-55.0F));
            p_109379_.mulPose(Axis.YP.rotationDegrees((float) k * 35.3F));
            p_109379_.mulPose(Axis.ZP.rotationDegrees((float) k * -9.785F));
            float f7 = (float) p_109377_.getUseDuration() - ((float) itemInHandRenderer.minecraft.player.getUseItemRemainingTicks() - p_109373_ + 1.0F);
            float f11 = f7 / 10.0F;
            if (f11 > 1.0F) {
                f11 = 1.0F;
            }

            if (f11 > 0.1F) {
                float f14 = Mth.sin((f7 - 0.1F) * 1.3F);
                float f17 = f11 - 0.1F;
                float f19 = f14 * f17;
                p_109379_.translate(f19 * 0.0F, f19 * 0.004F, f19 * 0.0F);
            }

            p_109379_.translate(0.0F, 0.0F, f11 * 0.2F);
            p_109379_.scale(1.0F, 1.0F, 1.0F + f11 * 0.2F);
            p_109379_.mulPose(Axis.YN.rotationDegrees((float) k * 45.0F));
        }
    }

    public static <T extends LivingEntity, M extends EntityModel<T> & ArmedModel> void render(ItemInHandLayer<T, M> layer, LivingEntity p_117185_, ItemStack p_117186_, ItemDisplayContext p_270970_, HumanoidArm p_117188_, PoseStack p_117189_, MultiBufferSource p_117190_, int p_117191_, CallbackInfo ci) {
        if (!p_117186_.isEmpty() && p_117186_.getItem() instanceof GungnirItem) {
            p_117189_.pushPose();

            layer.getParentModel().translateToHand(p_117188_, p_117189_);
            p_117189_.mulPose(Axis.XP.rotationDegrees(-90.0F));
            p_117189_.mulPose(Axis.YP.rotationDegrees(-180.0F));
            boolean flag = p_117188_ == HumanoidArm.LEFT;

            p_117189_.translate((float) (flag ? -1 : 1) / 16.0F, 0, -0.625F);
            p_117189_.pushPose();
            if (p_117185_.getUseItem() == p_117186_) {
                p_117189_.mulPose(Axis.ZP.rotationDegrees(180.0F));
            }
            layer.itemInHandRenderer.renderItem(p_117185_, p_117186_, p_270970_, flag, p_117189_, p_117190_, p_117191_);
            p_117189_.popPose();
            p_117189_.popPose();
            ci.cancel();
        }
    }
}
