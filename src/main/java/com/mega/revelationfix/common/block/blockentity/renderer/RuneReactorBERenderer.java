package com.mega.revelationfix.common.block.blockentity.renderer;

import com.mega.revelationfix.client.Easing;
import com.mega.revelationfix.common.block.RuneReactorBlock;
import com.mega.revelationfix.common.block.blockentity.RuneReactorBlockEntity;
import com.mega.revelationfix.common.compat.SafeClass;
import com.mega.revelationfix.common.event.handler.ClientEventHandler;
import com.mojang.blaze3d.Blaze3D;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class RuneReactorBERenderer implements BlockEntityRenderer<RuneReactorBlockEntity> {
    private final ItemRenderer itemRenderer;
    public RuneReactorBERenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }
    @Override
    public void render(@NotNull RuneReactorBlockEntity blockEntity, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int pPackedLight, int pPackedOverlay) {
        if (RuneReactorBlock.isRitualStructureCore(blockEntity.getInsertItem()))
            return;
        poseStack.pushPose();
        float frames100 = (float) (Blaze3D.getTime() * 100F);
        float posY = 13F/16F + 0.3F;
        float timeD1000 = frames100 % 1000F;
        float animY = 0F;
        if (timeD1000 < 300F) {
            animY = timeD1000 < 100F ? Easing.IN_OUT_QUINT.calculate(timeD1000/100F) : (timeD1000 >= 200F ? 1F -Easing.IN_OUT_EXPO.calculate(timeD1000/100F -2F) : 1F);
        }

        poseStack.translate(0.5F, posY + animY / 5F, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(blockEntity.randId + (float) (Blaze3D.getTime() * 45D)));
        ItemStack stack = blockEntity.getInsertItem();
        BakedModel bakedModel = itemRenderer.getModel(stack, blockEntity.getLevel(),null,0);
        itemRenderer.render(stack, ItemDisplayContext.GROUND,true, poseStack,bufferSource,0xF000F0 * (SafeClass.usingShaderPack() ? 2 : 1),pPackedOverlay,bakedModel);
        poseStack.popPose();
    }
}
