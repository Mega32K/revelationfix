package com.mega.revelationfix.client.renderer.entity;

import com.mega.revelationfix.client.renderer.VFRBuilders;
import com.mega.revelationfix.client.renderer.trail.TrailPoint;
import com.mega.revelationfix.common.compat.SafeClass;
import com.mega.revelationfix.common.config.ClientConfig;
import com.mega.revelationfix.common.entity.projectile.GungnirSpearEntity;
import com.mega.revelationfix.common.entity.projectile.StarArrow;
import com.mega.revelationfix.common.event.handler.ClientEventHandler;
import com.mega.revelationfix.common.init.GRItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class GungnirSpearRenderer extends EntityRenderer<GungnirSpearEntity> {
    static GameRenderer gameRenderer = Minecraft.getInstance().gameRenderer;
    private final ItemRenderer itemRenderer;
    private ItemStack itemStack;

    public GungnirSpearRenderer(EntityRendererProvider.Context p_174420_) {
        super(p_174420_);
        this.itemRenderer = p_174420_.getItemRenderer();
    }

    public void render(@NotNull GungnirSpearEntity spearEntity, float nothing, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        try {
            if (spearEntity.getOwner() instanceof LivingEntity living) {
                if (itemStack == null)
                    itemStack = new ItemStack(GRItems.GUNGNIR.get());
                spearEntity.updateWrappedTrail(spearEntity, nothing, partialTicks, poseStack, bufferSource, packedLight);
                poseStack.pushPose();
                packedLight = 0xFF00F0;
                poseStack.scale(2F, 2F, 2.3F);
                poseStack.translate(0F, 0F, -0.F);
                poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, spearEntity.yRotO, spearEntity.getYRot()) - 90.0F));
                poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, spearEntity.xRotO, spearEntity.getXRot()) + 90.0F));
                renderItem(spearEntity, itemStack, ItemDisplayContext.NONE, false, poseStack, bufferSource, packedLight);
                poseStack.popPose();
            }

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        super.render(spearEntity, nothing, partialTicks, poseStack, bufferSource, packedLight);
    }

    public void renderItem(Entity p_270072_, ItemStack p_270793_, ItemDisplayContext p_270837_, boolean p_270203_, PoseStack p_270974_, MultiBufferSource p_270686_, int p_270103_) {
        if (!p_270793_.isEmpty()) {
            itemRenderer.renderStatic(null, p_270793_, p_270837_, p_270203_, p_270974_, p_270686_, p_270072_.level(), p_270103_, OverlayTexture.NO_OVERLAY, p_270072_.getId() + p_270837_.ordinal());
        }
    }

    public @NotNull ResourceLocation getTextureLocation(@NotNull GungnirSpearEntity p_116109_) {
        return new ResourceLocation("");
    }

}