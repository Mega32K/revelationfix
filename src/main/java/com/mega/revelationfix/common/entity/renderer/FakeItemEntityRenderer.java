package com.mega.revelationfix.common.entity.renderer;

import com.mega.revelationfix.common.apollyon.client.render.VFRBuilders;
import com.mega.revelationfix.common.apollyon.client.render.trail.TrailPoint;
import com.mega.revelationfix.common.compat.SafeClass;
import com.mega.revelationfix.common.config.ClientConfig;
import com.mega.revelationfix.common.entity.FakeItemEntity;
import com.mega.revelationfix.common.event.handler.ClientEventHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class FakeItemEntityRenderer extends EntityRenderer<FakeItemEntity> {

    private static final float ITEM_BUNDLE_OFFSET_SCALE = 0.15F;
    private static final int ITEM_COUNT_FOR_5_BUNDLE = 48;
    private static final int ITEM_COUNT_FOR_4_BUNDLE = 32;
    private static final int ITEM_COUNT_FOR_3_BUNDLE = 16;
    private static final int ITEM_COUNT_FOR_2_BUNDLE = 1;
    private static final float FLAT_ITEM_BUNDLE_OFFSET_X = 0.0F;
    private static final float FLAT_ITEM_BUNDLE_OFFSET_Y = 0.0F;
    private static final float FLAT_ITEM_BUNDLE_OFFSET_Z = 0.09375F;
    private final ItemRenderer itemRenderer;
    private final RandomSource random = RandomSource.create();

    public FakeItemEntityRenderer(EntityRendererProvider.Context p_174198_) {
        super(p_174198_);
        this.itemRenderer = p_174198_.getItemRenderer();
        this.shadowRadius = 0.15F;
        this.shadowStrength = 0.75F;
    }

    protected int getRenderAmount(ItemStack p_115043_) {
        int i = 1;
        if (p_115043_.getCount() > 48) {
            i = 5;
        } else if (p_115043_.getCount() > 32) {
            i = 4;
        } else if (p_115043_.getCount() > 16) {
            i = 3;
        } else if (p_115043_.getCount() > 1) {
            i = 2;
        }

        return i;
    }

    public void render(FakeItemEntity itemEntity, float yaw, float partialTicks, PoseStack p_115039_, MultiBufferSource p_115040_, int p_115041_) {
        if (ClientConfig.enableTrailRenderer) {
            double x = Mth.lerp(partialTicks, itemEntity.xOld, itemEntity.getX());
            double y = Mth.lerp(partialTicks, itemEntity.yOld, itemEntity.getY());
            double z = Mth.lerp(partialTicks, itemEntity.zOld, itemEntity.getZ());
            final List<TrailPoint> trailPoints = itemEntity.wrappedTrailUpdate.trailPoints;
            if (!trailPoints.isEmpty()) {
                label0:
                {
                    if (Minecraft.getInstance().player != null && SafeClass.isClientTimeStop()) break label0;
                    itemEntity.wrappedTrailUpdate.update(x, y, z, partialTicks);
                }
            } else itemEntity.wrappedTrailUpdate.join(5);
            VFRBuilders.WorldVFRTrailBuilder trailBuilder = ClientEventHandler.normalStarTrailsBuilder;
            if (trailBuilder != null)
                trailBuilder.addTrailListRenderTask(new FakeItemTrailTask(trailPoints));
        }
        p_115039_.pushPose();
        ItemStack itemstack = itemEntity.getItem();
        int i = itemstack.isEmpty() ? 187 : Item.getId(itemstack.getItem()) + itemstack.getDamageValue();
        this.random.setSeed(i);
        BakedModel bakedmodel = this.itemRenderer.getModel(itemstack, itemEntity.level(), null, itemEntity.getId());
        boolean flag = bakedmodel.isGui3d();
        int j = this.getRenderAmount(itemstack);
        float f = 0.25F;
        float f1 = shouldBob() ? Mth.sin(((float) itemEntity.getAge() + partialTicks) / 10.0F + itemEntity.bobOffs) * 0.1F + 0.1F : 0;
        float f2 = bakedmodel.getTransforms().getTransform(ItemDisplayContext.GROUND).scale.y();
        p_115039_.translate(0.0F, f1 + 0.25F * f2, 0.0F);
        float f3 = itemEntity.getSpin(partialTicks);
        p_115039_.mulPose(Axis.YP.rotation(f3));
        if (!flag) {
            float f7 = -0.0F * (float) (j - 1) * 0.5F;
            float f8 = -0.0F * (float) (j - 1) * 0.5F;
            float f9 = -0.09375F * (float) (j - 1) * 0.5F;
            p_115039_.translate(f7, f8, f9);
        }

        for (int k = 0; k < j; ++k) {
            p_115039_.pushPose();
            if (k > 0) {
                if (flag) {
                    float f11 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    float f13 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    float f10 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    p_115039_.translate(shouldSpreadItems() ? f11 : 0, shouldSpreadItems() ? f13 : 0, shouldSpreadItems() ? f10 : 0);
                } else {
                    float f12 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
                    float f14 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
                    p_115039_.translate(shouldSpreadItems() ? f12 : 0, shouldSpreadItems() ? f14 : 0, 0.0D);
                }
            }

            this.itemRenderer.render(itemstack, ItemDisplayContext.GROUND, false, p_115039_, p_115040_, p_115041_, OverlayTexture.NO_OVERLAY, bakedmodel);
            p_115039_.popPose();
            if (!flag) {
                p_115039_.translate(0.0, 0.0, 0.09375F);
            }
        }

        p_115039_.popPose();
        super.render(itemEntity, yaw, partialTicks, p_115039_, p_115040_, p_115041_);
    }

    public ResourceLocation getTextureLocation(FakeItemEntity p_115034_) {
        return TextureAtlas.LOCATION_BLOCKS;
    }

    /*==================================== FORGE START ===========================================*/

    /**
     * @return If items should spread out when rendered in 3D
     */
    public boolean shouldSpreadItems() {
        return true;
    }

    /**
     * @return If items should have a bob effect
     */
    public boolean shouldBob() {
        return true;
    }

    /*==================================== FORGE END =============================================*/
    record FakeItemTrailTask(List<TrailPoint> trailPoints) implements VFRBuilders.WorldVFRTrailBuilder.TrailRenderTask {
        @Override
        public void task(PoseStack matrix, VFRBuilders.WorldVFRTrailBuilder vfrTrailBuilder) {
            if (!trailPoints.isEmpty()) {
                vfrTrailBuilder.r = 1F;
                vfrTrailBuilder.g = 0F;
                vfrTrailBuilder.b = 0F;
                vfrTrailBuilder.a = 1.5F;
                vfrTrailBuilder.renderTrail(matrix, trailPoints, f -> (f < 0.3F ? 1.0F : 1.3F - f) * 0.2F);
            }
        }
    }
}
