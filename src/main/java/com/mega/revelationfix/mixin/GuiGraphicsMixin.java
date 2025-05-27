package com.mega.revelationfix.mixin;

import com.mega.revelationfix.common.event.RenderTooltipPostEvent;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin {
    @Shadow(remap = false)
    private ItemStack tooltipStack;

    @Shadow
    public abstract int guiWidth();

    @Shadow
    public abstract int guiHeight();

    @Inject(method = "renderTooltipInternal", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void renderTooltipInternal(Font p_282675_, List<ClientTooltipComponent> p_282615_, int p_283230_, int p_283417_, ClientTooltipPositioner p_282442_, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new RenderTooltipPostEvent(this.tooltipStack, (GuiGraphics) (Object) this, p_283230_, p_283417_));
    }
}
