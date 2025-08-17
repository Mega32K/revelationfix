package com.mega.revelationfix.client.font;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.AtomicDouble;
import com.mega.endinglib.api.client.text.TextColorInterface;
import com.mega.endinglib.mixin.accessor.AccessorFont;
import com.mega.endinglib.mixin.accessor.AccessorFontManager;
import com.mega.endinglib.mixin.accessor.AccessorMC;
import com.mega.revelationfix.client.enums.ModChatFormatting;
import com.mega.revelationfix.mixin.EmptyGlyphAccessor;
import com.mojang.blaze3d.font.GlyphInfo;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.ints.Int2CharOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.FontManager;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.gui.font.GlyphRenderTypes;
import net.minecraft.client.gui.font.glyphs.EmptyGlyph;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.util.StringDecomposer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class MinecraftFont extends Font {
    public static FontManager fontManager = ((AccessorMC) Minecraft.getInstance()).getFontManager();
    public static AccessorFontManager accessorFontManager = (AccessorFontManager) fontManager;
    public static MinecraftFont INSTANCE;

    static {
        INSTANCE = new MinecraftFont((p_284586_) -> accessorFontManager.getFontSets().getOrDefault(accessorFontManager.getRenames().getOrDefault(p_284586_, p_284586_), accessorFontManager.getMissingFontSet()), false, accessorFontManager.getFontSets());
    }

    public final AccessorFont accessorFont;

    public MinecraftFont(Function<ResourceLocation, FontSet> p_243253_, boolean p_243245_, Map<ResourceLocation, FontSet> f) {
        super(p_243253_, p_243245_);
        this.accessorFont = (AccessorFont) this;
    }

    public static boolean isEden(Int2CharOpenHashMap map) {
        for (char c : map.values()) {
            if (c == ModChatFormatting.EDEN.getChar())
                return true;
        }
        return false;
    }

    @Override
    public int drawInBatch(String p_272780_, float p_272811_, float p_272610_, int p_273422_, boolean p_273016_, Matrix4f p_273443_, MultiBufferSource p_273387_, DisplayMode p_273551_, int p_272706_, int p_273114_, boolean p_273022_) {

        return super.drawInBatch(p_272780_, p_272811_, p_272610_, p_273422_, p_273016_, p_273443_, p_273387_, p_273551_, p_272706_, p_273114_, p_273022_);
    }

    public float drawInBatchF(@NotNull FormattedCharSequence p_273262_, float p_273006_, float p_273254_, int p_273375_, boolean p_273674_, @NotNull Matrix4f p_273525_, @NotNull MultiBufferSource p_272624_, @NotNull DisplayMode p_273418_, int p_273330_, int p_272981_, Int2CharOpenHashMap foundCodes) {
        boolean isCentered = false;
        float returnV = 0F;
        if (isEden(foundCodes)) {
            String[] eden_no_eden = FontTextBuilder.formattedCharSequenceToStringEden(p_273262_);
            returnV = this.renderEden(eden_no_eden[0], p_273006_, p_273254_, p_273375_, p_273674_, p_273525_, p_272624_, p_273418_, p_273330_, p_272981_);
            this.drawInBatch(eden_no_eden[1], p_273006_, p_273254_, p_273375_, p_273674_, p_273525_, p_272624_, p_273418_, p_273330_, p_272981_);
        } else {
            returnV = this.drawInternalF(p_273262_, p_273006_, p_273254_, p_273375_, p_273674_, p_273525_, p_272624_, p_273418_, p_273330_, p_272981_);
        }
        return returnV;
    }

    public float renderEden(String text2, float startX, float startY, int iColor, boolean p_273674_, Matrix4f matrix4f, MultiBufferSource bufferSource, DisplayMode displayMode, int overlay, int light) {
        long milliTime = OdamaneFont.milliTime();
        float colorr = (float) milliTime * 0.0025F % 1.0F;
        float colorrStep = (float) OdamaneFont.rangeRemap(
                Mth.sin(((float) milliTime * 0.005F)) % 6.28318D, -0.9D, 2.5D, 0.025D, 0.15D);
        float posX = startX;
        float xOffset = Mth.cos((float) milliTime * 0.000833F);
        matrix4f.translate(xOffset, 0, 0);
        for (int i = 0; i < text2.length(); i++) {
            float yOffset = Mth.sin((i * (0.5F) + (float) milliTime * 0.00166F));
            matrix4f.translate(0, yOffset, 0);

            int c = (int) (Mth.clamp((Mth.abs(Mth.cos(i * (0.2F) + (float) milliTime / 720F)) * 255), 70, 200)) << 24 | 8323072 | 33792 | 146;
            posX = this.drawInternalF(String.valueOf(text2.charAt(i)), posX, startY, c, p_273674_, matrix4f, bufferSource, displayMode, overlay, light, false);
            matrix4f.translate(0, -yOffset, 0);
            colorr += colorrStep;
            colorr %= 1.0F;
        }
        matrix4f.translate(-xOffset, 0, 0);
        return posX;
    }

    public float drawInternalF(@NotNull FormattedCharSequence p_273025_, float p_273121_, float p_272717_, int p_273653_, boolean p_273531_, @NotNull Matrix4f p_273265_, @NotNull MultiBufferSource p_273560_, @NotNull DisplayMode p_273342_, int p_273373_, int p_273266_) {
        p_273653_ = AccessorFont.callAdjustColor(p_273653_);
        Matrix4f matrix4f = new Matrix4f(p_273265_);
        if (p_273531_) {
            this.renderText(p_273025_, p_273121_, p_272717_, p_273653_, true, p_273265_, p_273560_, p_273342_, p_273373_, p_273266_);
            matrix4f.translate(AccessorFont.shadowLifting());
        }

        p_273121_ = this.renderText(p_273025_, p_273121_, p_272717_, p_273653_, false, matrix4f, p_273560_, p_273342_, p_273373_, p_273266_);
        return p_273121_ + (p_273531_ ? 1 : 0);
    }

    private float drawInternalF(String p_273658_, float p_273086_, float p_272883_, int p_273547_, boolean p_272778_, Matrix4f p_272662_, MultiBufferSource p_273012_, Font.DisplayMode p_273381_, int p_272855_, int p_272745_, boolean p_272785_) {
        if (p_272785_) {
            p_273658_ = this.bidirectionalShaping(p_273658_);
        }

        p_273547_ = AccessorFont.callAdjustColor(p_273547_);
        Matrix4f matrix4f = new Matrix4f(p_272662_);
        if (p_272778_) {
            this.renderText(p_273658_, p_273086_, p_272883_, p_273547_, true, p_272662_, p_273012_, p_273381_, p_272855_, p_272745_);
            matrix4f.translate(AccessorFont.shadowLifting());
        }

        p_273086_ = this.renderText(p_273658_, p_273086_, p_272883_, p_273547_, false, matrix4f, p_273012_, p_273381_, p_272855_, p_272745_);
        return p_273086_;
    }

    private float renderText(String p_273765_, float p_273532_, float p_272783_, int p_273217_, boolean p_273583_, Matrix4f p_272734_, MultiBufferSource p_272595_, Font.DisplayMode p_273610_, int p_273727_, int p_273199_) {
        StringRenderOutputVanilla font$stringrenderoutput = new StringRenderOutputVanilla(p_272595_, p_273532_, p_272783_, p_273217_, p_273583_, p_272734_, p_273610_, p_273199_);
        StringDecomposer.iterateFormatted(p_273765_, Style.EMPTY, font$stringrenderoutput);
        return font$stringrenderoutput.finish(p_273727_, p_273532_);
    }

    public float renderText(FormattedCharSequence p_273322_, float p_272632_, float p_273541_, int p_273200_, boolean p_273312_, @NotNull Matrix4f p_273276_, @NotNull MultiBufferSource p_273392_, @NotNull DisplayMode p_272625_, int p_273774_, int p_273371_) {
        StringRenderOutputVanilla font$stringrenderoutput = new StringRenderOutputVanilla(p_273392_, p_272632_, p_273541_, p_273200_, p_273312_, p_273276_, p_272625_, p_273371_);
        p_273322_.accept(font$stringrenderoutput);
        return font$stringrenderoutput.finish(p_273774_, p_272632_);
    }

    public void renderChar0(BakedGlyph p_254105_, boolean p_254001_, boolean p_254262_, float p_254256_, float p_253753_, float p_253629_, Matrix4f p_254014_, VertexConsumer p_253852_, float p_254317_, float p_253809_, float p_253870_, float p_254287_, int p_253905_) {
        p_254105_ = BakedGlyph.createByEmpty(p_254105_);
        p_254105_.render(p_254262_, p_253753_, p_253629_, p_254014_, p_253852_, p_254317_, p_253809_, p_253870_, p_254287_, p_253905_);
        if (p_254001_) {
            p_254105_.render(p_254262_, p_253753_ + p_254256_, p_253629_, p_254014_, p_253852_, p_254317_, p_253809_, p_253870_, p_254287_, p_253905_);
        }
    }

    public enum CharStyle {
        NONE(' '),
        REVELATION('q');
        public final char code;

        CharStyle(char code) {
            this.code = code;
        }
    }

    public static class BakedGlyph extends net.minecraft.client.gui.font.glyphs.BakedGlyph {
        private final GlyphRenderTypes renderTypes;
        private final float u0;
        private final float u1;
        private final float v0;
        private final float v1;
        private final float left;
        private final float right;
        private final float up;
        private final float down;

        public BakedGlyph(GlyphRenderTypes p_285527_, float p_285271_, float p_284970_, float p_285098_, float p_285023_, float p_285242_, float p_285043_, float p_285100_, float p_284948_) {
            super(p_285527_, p_285271_, p_284970_, p_285098_, p_285023_, p_285242_, p_285043_, p_285100_, p_284948_);
            this.renderTypes = p_285527_;
            this.u0 = p_285271_;
            this.u1 = p_284970_;
            this.v0 = p_285098_;
            this.v1 = p_285023_;
            this.left = p_285242_;
            this.right = p_285043_;
            this.up = p_285100_;
            this.down = p_284948_;
        }

        public static BakedGlyph createByEmpty(net.minecraft.client.gui.font.glyphs.BakedGlyph glyph) {
            EmptyGlyphAccessor accessor = (EmptyGlyphAccessor) glyph;
            return new BakedGlyph(accessor.renderTypes(), accessor.u0(), accessor.u1(), accessor.v0(), accessor.v1(), accessor.left(), accessor.right(), accessor.up(), accessor.down());
        }

        public void render(boolean p_95227_, float p_95228_, float p_95229_, @NotNull Matrix4f p_253706_, VertexConsumer p_95231_, float p_95232_, float p_95233_, float p_95234_, float p_95235_, int p_95236_) {
            int i = 3;
            float f = p_95228_ + this.left;
            float f1 = p_95228_ + this.right;
            float f2 = this.up - 3.0F;
            float f3 = this.down - 3.0F;
            float f4 = p_95229_ + f2;
            float f5 = p_95229_ + f3;
            float f6 = p_95227_ ? 1.0F - 0.25F * f2 : 0.0F;
            float f7 = p_95227_ ? 1.0F - 0.25F * f3 : 0.0F;
            p_95231_.vertex(p_253706_, f + f6, f4, 0.0F).color(p_95232_, p_95233_, p_95234_, p_95235_).uv(this.u0, this.v0).uv2(p_95236_).normal(0F, 0F, -1F).endVertex();
            p_95231_.vertex(p_253706_, f + f7, f5, 0.0F).color(p_95232_, p_95233_, p_95234_, p_95235_).uv(this.u0, this.v1).uv2(p_95236_).normal(0F, 0F, -1F).endVertex();
            p_95231_.vertex(p_253706_, f1 + f7, f5, 0.0F).color(p_95232_, p_95233_, p_95234_, p_95235_).uv(this.u1, this.v1).uv2(p_95236_).normal(0F, 0F, -1F).endVertex();
            p_95231_.vertex(p_253706_, f1 + f6, f4, 0.0F).color(p_95232_, p_95233_, p_95234_, p_95235_).uv(this.u1, this.v0).uv2(p_95236_).normal(0F, 0F, -1F).endVertex();
        }

        public void renderEffect(BakedGlyph.Effect p_95221_, Matrix4f p_254370_, VertexConsumer p_95223_, int p_95224_) {
            p_95223_.vertex(p_254370_, p_95221_.x0, p_95221_.y0, p_95221_.depth).color(p_95221_.r, p_95221_.g, p_95221_.b, p_95221_.a).uv(this.u0, this.v0).uv2(p_95224_).normal(0F, 0F, -1F).endVertex();
            p_95223_.vertex(p_254370_, p_95221_.x1, p_95221_.y0, p_95221_.depth).color(p_95221_.r, p_95221_.g, p_95221_.b, p_95221_.a).uv(this.u0, this.v1).uv2(p_95224_).normal(0F, 0F, -1F).endVertex();
            p_95223_.vertex(p_254370_, p_95221_.x1, p_95221_.y1, p_95221_.depth).color(p_95221_.r, p_95221_.g, p_95221_.b, p_95221_.a).uv(this.u1, this.v1).uv2(p_95224_).normal(0F, 0F, -1F).endVertex();
            p_95223_.vertex(p_254370_, p_95221_.x0, p_95221_.y1, p_95221_.depth).color(p_95221_.r, p_95221_.g, p_95221_.b, p_95221_.a).uv(this.u1, this.v0).uv2(p_95224_).normal(0F, 0F, -1F).endVertex();
        }

        public @NotNull RenderType renderType(Font.@NotNull DisplayMode p_181388_) {
            return this.renderTypes.select(p_181388_);
        }

        @OnlyIn(Dist.CLIENT)
        public static class Effect {
            protected final float x0;
            protected final float y0;
            protected final float x1;
            protected final float y1;
            protected final float depth;
            protected final float r;
            protected final float g;
            protected final float b;
            protected final float a;

            public Effect(float p_95247_, float p_95248_, float p_95249_, float p_95250_, float p_95251_, float p_95252_, float p_95253_, float p_95254_, float p_95255_) {
                this.x0 = p_95247_;
                this.y0 = p_95248_;
                this.x1 = p_95249_;
                this.y1 = p_95250_;
                this.depth = p_95251_;
                this.r = p_95252_;
                this.g = p_95253_;
                this.b = p_95254_;
                this.a = p_95255_;
            }
        }
    }

    class StringRenderOutputVanilla extends StringRenderOutput {
        final MultiBufferSource bufferSource;
        private final boolean dropShadow;
        private final float dimFactor;
        private final float r;
        private final float g;
        private final float b;
        private final float a;
        private final Matrix4f pose;
        private final DisplayMode mode;
        private final int packedLightCoords;
        float x;
        float y;
        @Nullable
        private List<net.minecraft.client.gui.font.glyphs.BakedGlyph.Effect> effects;
        private CharStyle lastStyle = CharStyle.NONE;

        public StringRenderOutputVanilla(MultiBufferSource p_181365_, float p_181366_, float p_181367_, int p_181368_, boolean p_181369_, Matrix4f p_254510_, DisplayMode p_181371_, int p_181372_) {
            super(p_181365_, p_181366_, p_181367_, p_181368_, p_181369_, p_254510_, p_181371_, p_181372_);
            this.bufferSource = p_181365_;
            this.x = p_181366_;
            this.y = p_181367_;
            this.dropShadow = p_181369_;
            this.dimFactor = p_181369_ ? 0.25F : 1.0F;
            this.r = (float) (p_181368_ >> 16 & 255) / 255.0F * this.dimFactor;
            this.g = (float) (p_181368_ >> 8 & 255) / 255.0F * this.dimFactor;
            this.b = (float) (p_181368_ & 255) / 255.0F * this.dimFactor;
            this.a = (float) (p_181368_ >> 24 & 255) / 255.0F;
            this.pose = p_254510_;
            this.mode = p_181371_;
            this.packedLightCoords = p_181372_;
        }

        public void addEffect(net.minecraft.client.gui.font.glyphs.BakedGlyph.@NotNull Effect p_92965_) {
            if (this.effects == null) {
                this.effects = Lists.newArrayList();
            }

            this.effects.add(p_92965_);
        }

        public boolean accept(int p_92967_, Style style, int p_92969_) {
            FontSet fontset = MinecraftFont.this.accessorFont.invokeGetFontSet(style.getFont());
            GlyphInfo glyphinfo = fontset.getGlyphInfo(p_92969_, MinecraftFont.this.accessorFont.isFilterFishyGlyphs());
            net.minecraft.client.gui.font.glyphs.BakedGlyph bakedglyph = style.isObfuscated() && p_92969_ != 32 ? fontset.getRandomGlyph(glyphinfo) : fontset.getGlyph(p_92969_);
            boolean flag = style.isBold();
            float f3 = this.a;
            TextColor textcolor = style.getColor();
            float f;
            float f1;
            float f2;
            if (textcolor != null) {
                int i = textcolor.getValue();
                f = (float) (i >> 16 & 255) / 255.0F * this.dimFactor;
                f1 = (float) (i >> 8 & 255) / 255.0F * this.dimFactor;
                f2 = (float) (i & 255) / 255.0F * this.dimFactor;
            } else {
                f = this.r;
                f1 = this.g;
                f2 = this.b;
            }
            if (!(bakedglyph instanceof EmptyGlyph)) {
                float f5 = flag ? glyphinfo.getBoldOffset() : 0.0F;
                float f4 = this.dropShadow ? glyphinfo.getShadowOffset() : 0.0F;
                TextColorInterface codeChecker = (TextColorInterface) (Object) textcolor;
                char code = codeChecker == null ? ' ' : codeChecker.endinglib$getCode();
                AtomicDouble atomicX = new AtomicDouble(x);
                VertexConsumer vertexconsumer = this.bufferSource.getBuffer(bakedglyph.renderType(this.mode));
                {
                    if (code == 'q') {
                        style = style.withColor(AccessorFont.callAdjustColor(11141120));
                        textcolor = style.getColor();
                        if (textcolor != null) {
                            int i = textcolor.getValue();
                            float r = (float) (i >> 16 & 255) / 255.0F * this.dimFactor;
                            float g = (float) (i >> 8 & 255) / 255.0F * this.dimFactor;
                            float b = (float) (i & 255) / 255.0F * this.dimFactor;
                            for (int j = -1; j <= 1; ++j) {
                                for (int k = -1; k <= 1; ++k) {
                                    if (j != 0 || k != 0) {
                                        float x8x = atomicX.floatValue() + (float) j * glyphinfo.getShadowOffset();
                                        float y8x = y + (float) k * glyphinfo.getShadowOffset();
                                        MinecraftFont.this.accessorFont.callRenderChar(bakedglyph, flag, style.isItalic(), f5, x8x + f4, y8x + f4, this.pose, vertexconsumer, r, g, b, f3, this.packedLightCoords);
                                    }
                                }
                            }
                        }
                        lastStyle = CharStyle.REVELATION;
                    } else lastStyle = CharStyle.NONE;
                    MinecraftFont.this.accessorFont.callRenderChar(bakedglyph, flag, style.isItalic(), f5, this.x + f4, this.y + f4, this.pose, vertexconsumer, f, f1, f2, f3, this.packedLightCoords);
                }
            }

            float f6 = glyphinfo.getAdvance(flag);
            float f7 = this.dropShadow ? 1.0F : 0.0F;
            if (style.isStrikethrough()) {
                this.addEffect(new net.minecraft.client.gui.font.glyphs.BakedGlyph.Effect(this.x + f7 - 1.0F, this.y + f7 + 4.5F, this.x + f7 + f6, this.y + f7 + 4.5F - 1.0F, 0.01F, f, f1, f2, f3));
            }

            if (style.isUnderlined()) {
                this.addEffect(new net.minecraft.client.gui.font.glyphs.BakedGlyph.Effect(this.x + f7 - 1.0F, this.y + f7 + 9.0F, this.x + f7 + f6, this.y + f7 + 9.0F - 1.0F, 0.01F, f, f1, f2, f3));
            }

            this.x += f6;
            return true;
        }

        public float finish(int p_92962_, float p_92963_) {
            if (p_92962_ != 0) {
                float f = (float) (p_92962_ >> 24 & 255) / 255.0F;
                float f1 = (float) (p_92962_ >> 16 & 255) / 255.0F;
                float f2 = (float) (p_92962_ >> 8 & 255) / 255.0F;
                float f3 = (float) (p_92962_ & 255) / 255.0F;
                this.addEffect(new net.minecraft.client.gui.font.glyphs.BakedGlyph.Effect(p_92963_ - 1.0F, this.y + 9.0F, this.x + 1.0F, this.y - 1.0F, 0.01F, f1, f2, f3, f));
            }

            if (this.effects != null) {
                net.minecraft.client.gui.font.glyphs.BakedGlyph bakedglyph = MinecraftFont.this.accessorFont.invokeGetFontSet(Style.DEFAULT_FONT).whiteGlyph();
                VertexConsumer vertexconsumer = this.bufferSource.getBuffer(bakedglyph.renderType(this.mode));

                for (net.minecraft.client.gui.font.glyphs.BakedGlyph.Effect bakedglyph$effect : this.effects) {
                    bakedglyph.renderEffect(bakedglyph$effect, this.pose, vertexconsumer, this.packedLightCoords);
                }
            }

            return this.x;
        }
    }
}
