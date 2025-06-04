package com.mega.revelationfix.client;


import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector2ic;

import java.util.List;
import java.util.stream.Collectors;

@OnlyIn(Dist.CLIENT)
public class MyGuiGraphics extends GuiGraphics implements net.minecraftforge.client.extensions.IForgeGuiGraphics {
    public static int BACKGROUND_COLOR = -267386864;
    public static int BORDER_COLOR_TOP = 1347420415;
    public static int BORDER_COLOR_BOTTOM = 1344798847;
    public final Minecraft minecraft;
    public final PoseStack pose;
    public final MultiBufferSource.BufferSource bufferSource;
    public final ItemStack tooltipStack = ItemStack.EMPTY;

    public MyGuiGraphics(Minecraft p_282144_, PoseStack p_281551_, MultiBufferSource.BufferSource p_281460_) {
        super(p_282144_, p_281460_);
        this.minecraft = p_282144_;
        this.pose = p_281551_;
        this.bufferSource = p_281460_;
    }

    public MyGuiGraphics(Minecraft p_283406_, MultiBufferSource.BufferSource p_282238_) {
        this(p_283406_, new PoseStack(), p_282238_);
    }

    public MyGuiGraphics(GuiGraphics guiGraphics) {
        this(Minecraft.getInstance(), guiGraphics.pose(), guiGraphics.bufferSource());
    }

    public static MyGuiGraphics create() {
        return new MyGuiGraphics(Minecraft.getInstance(), Minecraft.getInstance().renderBuffers().bufferSource());
    }

    public static void renderTooltipBackground(MyGuiGraphics p_282666_, float p_281901_, float p_281846_, float p_281559_, float p_283336_, float p_283422_) {
        renderTooltipBackground(p_282666_, p_281901_, p_281846_, p_281559_, p_283336_, p_283422_, BACKGROUND_COLOR, BACKGROUND_COLOR, BORDER_COLOR_TOP, BORDER_COLOR_BOTTOM);
    }

    public static void renderTooltipBackground(MyGuiGraphics p_282666_, float p_281901_, float p_281846_, float p_281559_, float p_283336_, float p_283422_, float backgroundTop, float backgroundBottom, float borderTop, float borderBottom) {
        float i = p_281901_ - 3;
        float j = p_281846_ - 3;
        float k = p_281559_ + 3 + 3;
        float l = p_283336_ + 3 + 3;
        renderHorizontalLine(p_282666_, i, j - 1, k, p_283422_, backgroundTop);
        renderHorizontalLine(p_282666_, i, j + l, k, p_283422_, backgroundBottom);
        renderRectangle0(p_282666_, i, j, k, l, p_283422_, backgroundTop, backgroundBottom);
        renderVerticalLineGradient(p_282666_, i - 1, j, l, p_283422_, backgroundTop, backgroundBottom);
        renderVerticalLineGradient(p_282666_, i + k, j, l, p_283422_, backgroundTop, backgroundBottom);
        renderFrameGradient(p_282666_, i, j + 1, k, l, p_283422_, borderTop, borderBottom);
    }

    public static void renderFrameGradient(MyGuiGraphics p_282000_, float p_282055_, float p_281580_, float p_283284_, float p_282599_, float p_283432_, float p_282907_, float p_283153_) {
        renderVerticalLineGradient(p_282000_, p_282055_, p_281580_, p_282599_ - 2, p_283432_, p_282907_, p_283153_);
        renderVerticalLineGradient(p_282000_, p_282055_ + p_283284_ - 1, p_281580_, p_282599_ - 2, p_283432_, p_282907_, p_283153_);
        renderHorizontalLine(p_282000_, p_282055_, p_281580_ - 1, p_283284_, p_283432_, p_282907_);
        renderHorizontalLine(p_282000_, p_282055_, p_281580_ - 1 + p_282599_ - 1, p_283284_, p_283432_, p_283153_);
    }

    public static void renderVerticalLine(MyGuiGraphics p_281270_, float p_281928_, float p_281561_, float p_283155_, float p_282552_, float p_282221_) {
        p_281270_.fill(p_281928_, p_281561_, p_281928_ + 1, p_281561_ + p_283155_, p_282552_, p_282221_);
    }

    public static void renderVerticalLineGradient(MyGuiGraphics p_282478_, float p_282583_, float p_283262_, float p_283161_, float p_283322_, float p_282624_, float p_282756_) {
        p_282478_.fillGradient(p_282583_, p_283262_, p_282583_ + 1, p_283262_ + p_283161_, p_283322_, p_282624_, p_282756_);
    }

    public static void renderHorizontalLine(MyGuiGraphics p_282981_, float p_282028_, float p_282141_, float p_281771_, float p_282734_, float p_281979_) {
        p_282981_.fill(p_282028_, p_282141_, p_282028_ + p_281771_, p_282141_ + 1, p_282734_, p_281979_);
    }

    public static void renderRectangle0(MyGuiGraphics p_281392_, float p_282294_, float p_283353_, float p_282640_, float p_281964_, float p_283211_, float p_282349_, float colorTo) {
        p_281392_.fillGradient(p_282294_, p_283353_, p_282294_ + p_282640_, p_283353_ + p_281964_, p_283211_, p_282349_, colorTo);
    }

    @Deprecated
    public static void renderRectangle(GuiGraphics p_281392_, int p_282294_, int p_283353_, int p_282640_, int p_281964_, int p_283211_, int p_282349_) {
        renderRectangle(p_281392_, p_282294_, p_283353_, p_282640_, p_281964_, p_283211_, p_282349_, p_282349_);
    }

    // Forge: Allow specifying a gradient instead of a single color for the background
    public static void renderRectangle(GuiGraphics p_281392_, int p_282294_, int p_283353_, int p_282640_, int p_281964_, int p_283211_, int p_282349_, int colorTo) {
        p_281392_.fillGradient(p_282294_, p_283353_, p_282294_ + p_282640_, p_283353_ + p_281964_, p_283211_, p_282349_, colorTo);
    }

    /**
     * @deprecated
     */
    @Deprecated
    public void drawManaged(Runnable p_286277_) {
        this.flush();
        p_286277_.run();
        this.flush();
    }

    public int guiWidth() {
        return this.minecraft.getWindow().getGuiScaledWidth();
    }

    public int guiHeight() {
        return this.minecraft.getWindow().getGuiScaledHeight();
    }

    public PoseStack pose() {
        return this.pose;
    }

    public MultiBufferSource.BufferSource bufferSource() {
        return this.bufferSource;
    }

    public void flush() {
        RenderSystem.disableDepthTest();
        this.bufferSource.endBatch();
        RenderSystem.enableDepthTest();
    }

    public void fill(float p_281437_, float p_283660_, float p_282606_, float p_283413_, float p_283428_, float p_283253_) {
        this.fill(RenderType.gui(), p_281437_, p_283660_, p_282606_, p_283413_, p_283428_, p_283253_);
    }

    public void fill(RenderType p_286711_, float p_286234_, float p_286444_, float p_286244_, float p_286411_, float p_286671_, float p_286599_) {
        Matrix4f matrix4f = this.pose.last().pose();
        float j;
        if (p_286234_ < p_286244_) {
            j = p_286234_;
            p_286234_ = p_286244_;
            p_286244_ = j;
        }

        if (p_286444_ < p_286411_) {
            j = p_286444_;
            p_286444_ = p_286411_;
            p_286411_ = j;
        }

        float f3 = (float) FastColor.ARGB32.alpha((int) p_286599_) / 255.0F;
        float f = (float) FastColor.ARGB32.red((int) p_286599_) / 255.0F;
        float f1 = (float) FastColor.ARGB32.green((int) p_286599_) / 255.0F;
        float f2 = (float) FastColor.ARGB32.blue((int) p_286599_) / 255.0F;
        VertexConsumer vertexconsumer = this.bufferSource.getBuffer(p_286711_);
        vertexconsumer.vertex(matrix4f, p_286234_, p_286444_, p_286671_).color(f, f1, f2, f3).endVertex();
        vertexconsumer.vertex(matrix4f, p_286234_, p_286411_, p_286671_).color(f, f1, f2, f3).endVertex();
        vertexconsumer.vertex(matrix4f, p_286244_, p_286411_, p_286671_).color(f, f1, f2, f3).endVertex();
        vertexconsumer.vertex(matrix4f, p_286244_, p_286444_, p_286671_).color(f, f1, f2, f3).endVertex();
        this.flushIfUnmanaged();
    }

    public void fillGradient(float p_282702_, float p_282331_, float p_281415_, float p_283118_, float p_282419_, float p_281954_, float p_282607_) {
        this.fillGradient(RenderType.gui(), p_282702_, p_282331_, p_281415_, p_283118_, p_281954_, p_282607_, p_282419_);
    }

    public void fillGradient(RenderType p_286522_, float p_286535_, float p_286839_, float p_286242_, float p_286856_, float p_286809_, float p_286833_, float p_286706_) {
        VertexConsumer vertexconsumer = this.bufferSource.getBuffer(p_286522_);
        this.fillGradient(vertexconsumer, p_286535_, p_286839_, p_286242_, p_286856_, p_286706_, p_286809_, p_286833_);
        this.flushIfUnmanaged();
    }

    public void fillGradient(VertexConsumer p_286862_, float p_283414_, float p_281397_, float p_283587_, float p_281521_, float p_283505_, float p_283131_, float p_282949_) {
        float f = (float) FastColor.ARGB32.alpha((int) p_283131_) / 255.0F;
        float f1 = (float) FastColor.ARGB32.red((int) p_283131_) / 255.0F;
        float f2 = (float) FastColor.ARGB32.green((int) p_283131_) / 255.0F;
        float f3 = (float) FastColor.ARGB32.blue((int) p_283131_) / 255.0F;
        float f4 = (float) FastColor.ARGB32.alpha((int) p_282949_) / 255.0F;
        float f5 = (float) FastColor.ARGB32.red((int) p_282949_) / 255.0F;
        float f6 = (float) FastColor.ARGB32.green((int) p_282949_) / 255.0F;
        float f7 = (float) FastColor.ARGB32.blue((int) p_282949_) / 255.0F;
        Matrix4f matrix4f = this.pose.last().pose();
        p_286862_.vertex(matrix4f, p_283414_, p_281397_, p_283505_).color(f1, f2, f3, f).endVertex();
        p_286862_.vertex(matrix4f, p_283414_, p_281521_, p_283505_).color(f5, f6, f7, f4).endVertex();
        p_286862_.vertex(matrix4f, p_283587_, p_281521_, p_283505_).color(f5, f6, f7, f4).endVertex();
        p_286862_.vertex(matrix4f, p_283587_, p_281397_, p_283505_).color(f1, f2, f3, f).endVertex();
    }

    public void renderTooltip(Font p_282269_, Component p_282572_, int p_282044_, int p_282545_, int background, int borderStart, int borderEnd) {
        this.renderTooltip(p_282269_, List.of(p_282572_.getVisualOrderText()), p_282044_, p_282545_, background, borderStart, borderEnd);
    }

    public void renderTooltip(Font p_282192_, List<? extends FormattedCharSequence> p_282297_, int p_281680_, int p_283325_, int background, int borderStart, int borderEnd) {
        this.renderTooltipInternal(p_282192_, p_282297_.stream().map(ClientTooltipComponent::create).collect(Collectors.toList()), p_281680_, p_283325_, DefaultTooltipPositioner.INSTANCE, background, borderStart, borderEnd);
    }

    public void renderTooltip(Font p_281627_, List<FormattedCharSequence> p_283313_, ClientTooltipPositioner p_283571_, int p_282367_, int p_282806_, int background, int borderStart, int borderEnd) {
        this.renderTooltipInternal(p_281627_, p_283313_.stream().map(ClientTooltipComponent::create).collect(Collectors.toList()), p_282367_, p_282806_, p_283571_, background, borderStart, borderEnd);
    }

    public void renderTooltipInternal(Font font, List<ClientTooltipComponent> p_282615_, int x, int y, ClientTooltipPositioner p_282442_, int background, int borderStart, int borderEnd) {
        if (!p_282615_.isEmpty()) {
            int i = 0;
            int j = p_282615_.size() == 1 ? -2 : 0;

            for (ClientTooltipComponent clienttooltipcomponent : p_282615_) {
                int k = clienttooltipcomponent.getWidth(font);
                if (k > i) {
                    i = k;
                }

                j += clienttooltipcomponent.getHeight();
            }

            int i2 = i;
            int j2 = j;
            Vector2ic vector2ic = p_282442_.positionTooltip(this.guiWidth(), this.guiHeight(), x, y, i2, j2);
            int l = vector2ic.x();
            int i1 = vector2ic.y();
            this.pose.pushPose();
            int j1 = 400;
            this.drawManaged(() -> {
                net.minecraftforge.client.event.RenderTooltipEvent.Color colorEvent = net.minecraftforge.client.ForgeHooksClient.onRenderTooltipColor(this.tooltipStack, new GuiGraphics(minecraft, bufferSource), l, i1, font, p_282615_);
                TooltipRenderUtil.renderTooltipBackground(new GuiGraphics(minecraft, bufferSource), l, i1, i2, j2, 400, background, background, borderStart, borderEnd);
            });
            this.pose.translate(0.0F, 0.0F, 400.0F);
            int k1 = i1;

            for (int l1 = 0; l1 < p_282615_.size(); ++l1) {
                ClientTooltipComponent clienttooltipcomponent1 = p_282615_.get(l1);
                clienttooltipcomponent1.renderText(font, l, k1, this.pose.last().pose(), this.bufferSource);
                k1 += clienttooltipcomponent1.getHeight() + (l1 == 0 ? 2 : 0);
            }

            k1 = i1;

            for (int k2 = 0; k2 < p_282615_.size(); ++k2) {
                ClientTooltipComponent clienttooltipcomponent2 = p_282615_.get(k2);
                clienttooltipcomponent2.renderImage(font, l, k1, new GuiGraphics(minecraft, bufferSource));
                k1 += clienttooltipcomponent2.getHeight() + (k2 == 0 ? 2 : 0);
            }

            this.pose.popPose();
        }
    }


    public void blit(float p_282225_, float p_281487_, float p_281985_, float p_281329_, float p_283035_, TextureAtlasSprite p_281614_) {
        this.innerBlit(p_281614_.atlasLocation(), p_282225_, p_282225_ + p_281329_, p_281487_, p_281487_ + p_283035_, p_281985_, p_281614_.getU0(), p_281614_.getU1(), p_281614_.getV0(), p_281614_.getV1());
    }

    public void blit(float p_282416_, float p_282989_, float p_282618_, float p_282755_, float p_281717_, TextureAtlasSprite p_281874_, float p_283559_, float p_282730_, float p_283530_, float p_282246_) {
        this.innerBlit(p_281874_.atlasLocation(), p_282416_, p_282416_ + p_282755_, p_282989_, p_282989_ + p_281717_, p_282618_, p_281874_.getU0(), p_281874_.getU1(), p_281874_.getV0(), p_281874_.getV1(), p_283559_, p_282730_, p_283530_, p_282246_);
    }

    public void blit(ResourceLocation p_283377_, float p_281970_, float p_282111_, float p_283134_, float p_282778_, float p_281478_, float p_281821_) {
        this.blit(p_283377_, p_281970_, p_282111_, 0, p_283134_, p_282778_, p_281478_, p_281821_, 256, 256);
    }

    public void blit(ResourceLocation p_283573_, float p_283574_, float p_283670_, float p_283545_, float p_283029_, float p_283061_, float p_282845_, float p_282558_, float p_282832_, float p_281851_) {
        this.blit(p_283573_, p_283574_, p_283574_ + p_282845_, p_283670_, p_283670_ + p_282558_, p_283545_, p_282845_, p_282558_, p_283029_, p_283061_, p_282832_, p_281851_);
    }

    public void blit(ResourceLocation p_282034_, float p_283671_, float p_282377_, float p_282058_, float p_281939_, float p_282285_, float p_283199_, float p_282186_, float p_282322_, float p_282481_, float p_281887_) {
        this.blit(p_282034_, p_283671_, p_283671_ + p_282058_, p_282377_, p_282377_ + p_281939_, 0, p_282186_, p_282322_, p_282285_, p_283199_, p_282481_, p_281887_);
    }

    public void blit(ResourceLocation p_283272_, float p_283605_, float p_281879_, float p_282809_, float p_282942_, float p_281922_, float p_282385_, float p_282596_, float p_281699_) {
        this.blit(p_283272_, p_283605_, p_281879_, p_281922_, p_282385_, p_282809_, p_282942_, p_281922_, p_282385_, p_282596_, p_281699_);
    }

    public void blit(ResourceLocation p_282639_, float x0, float x1, float y0, float y1, float z, float p_282193_, float p_281980_, float p_282660_, float p_281522_, float p_282315_, float p_281436_) {
        this.innerBlit(p_282639_, x0, x1, y0, y1, z, (p_282660_ + 0.0F) / p_282315_, (p_282660_ + p_282193_) / p_282315_, (p_281522_ + 0.0F) / p_281436_, (p_281522_ + p_281980_) / p_281436_);
    }

    public void innerBlit(ResourceLocation p_283461_, float x0, float x1, float y0, float y1, float z, float u0, float u1, float v0, float v1) {
        RenderSystem.setShaderTexture(0, p_283461_);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        Matrix4f matrix4f = this.pose.last().pose();
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(matrix4f, x0, y0, z).uv(u0, v0).endVertex();
        bufferbuilder.vertex(matrix4f, x0, y1, z).uv(u0, v1).endVertex();
        bufferbuilder.vertex(matrix4f, x1, y1, z).uv(u1, v1).endVertex();
        bufferbuilder.vertex(matrix4f, x1, y0, z).uv(u1, v0).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
    }

    public void innerBlit(ResourceLocation p_283254_, float p_283092_, float p_281930_, float p_282113_, float p_281388_, float p_283583_, float p_281327_, float p_281676_, float p_283166_, float p_282630_, float p_282800_, float p_282850_, float p_282375_, float p_282754_) {
        RenderSystem.setShaderTexture(0, p_283254_);
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        RenderSystem.enableBlend();
        Matrix4f matrix4f = this.pose.last().pose();
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
        bufferbuilder.vertex(matrix4f, p_283092_, p_282113_, p_283583_).color(p_282800_, p_282850_, p_282375_, p_282754_).uv(p_281327_, p_283166_).endVertex();
        bufferbuilder.vertex(matrix4f, p_283092_, p_281388_, p_283583_).color(p_282800_, p_282850_, p_282375_, p_282754_).uv(p_281327_, p_282630_).endVertex();
        bufferbuilder.vertex(matrix4f, p_281930_, p_281388_, p_283583_).color(p_282800_, p_282850_, p_282375_, p_282754_).uv(p_281676_, p_282630_).endVertex();
        bufferbuilder.vertex(matrix4f, p_281930_, p_282113_, p_283583_).color(p_282800_, p_282850_, p_282375_, p_282754_).uv(p_281676_, p_283166_).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
        RenderSystem.disableBlend();
    }

    public void drawCenteredString(Font p_282901_, Component p_282456_, float p_283083_, float p_282276_, int p_281457_) {
        FormattedCharSequence formattedcharsequence = p_282456_.getVisualOrderText();
        this.drawString(p_282901_, formattedcharsequence, p_283083_ - p_282901_.width(formattedcharsequence) / 2F, p_282276_, p_281457_);
    }

    public int drawString(Font p_283019_, FormattedCharSequence p_283376_, float p_283379_, float p_283346_, int p_282119_) {
        return this.drawString(p_283019_, p_283376_, p_283379_, p_283346_, p_282119_, true);
    }

    public void drawCenteredString(Font p_282122_, String p_282898_, float p_281490_, float p_282853_, int p_281258_) {
        this.drawString(p_282122_, p_282898_, p_281490_ - p_282122_.width(p_282898_) / 2F, p_282853_, p_281258_);
    }

    public int drawString(Font p_282003_, @Nullable String p_281403_, float p_282714_, float p_282041_, int p_281908_) {
        return this.drawString(p_282003_, p_281403_, p_282714_, p_282041_, p_281908_, true);
    }

    public int drawString(Font p_283343_, @Nullable String p_281896_, int p_283569_, int p_283418_, int p_281560_, boolean p_282130_) {
        return this.drawString(p_283343_, p_281896_, (float) p_283569_, (float) p_283418_, p_281560_, p_282130_);
    }

    public void drawCenteredString8x8(Font p_282122_, String p_282898_, float p_281490_, float p_282853_, int p_281258_, int out) {
        this.drawString8x8(p_282122_, p_282898_, p_281490_ - p_282122_.width(p_282898_) / 2F, p_282853_, p_281258_, out);
    }

    public void drawString8x8(Font p_283343_, @Nullable String p_281896_, int p_283569_, int p_283418_, int p_281560_, int out) {
        this.drawString8x8(p_283343_, p_281896_, (float) p_283569_, (float) p_283418_, p_281560_, out);
    }

    public void drawString8x8(Font p_283343_, @Nullable String p_281896_, float p_283569_, float p_283418_, int p_281560_, int out) {
        if (p_281896_ != null) {
            p_283343_.drawInBatch8xOutline(FormattedCharSequence.forward(p_281896_, Style.EMPTY), p_283569_, p_283418_, out, p_281560_, this.pose.last().pose(), this.bufferSource, 15728880);
            this.flushIfUnmanaged();
        }
    }
}
