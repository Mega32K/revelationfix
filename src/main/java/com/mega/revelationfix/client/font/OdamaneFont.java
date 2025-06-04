package com.mega.revelationfix.client.font;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.FontManager;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import org.joml.Matrix4f;

import java.util.Map;
import java.util.function.Function;

public class OdamaneFont extends Font {
    public static final ObjectArrayList<CenterComponentGroup> centerComponentGroups = new ObjectArrayList<>();
    public static FontManager fontManager = Minecraft.getInstance().fontManager;
    public static OdamaneFont INSTANCE;
    static Component HALO_DESC_2 = null;

    static {
        INSTANCE = new OdamaneFont((p_284586_) -> fontManager.fontSets.getOrDefault(fontManager.renames.getOrDefault(p_284586_, p_284586_), fontManager.missingFontSet), false, fontManager.fontSets);
        new CenterComponentGroup(Component.translatable("item.goety_revelation.halo_of_the_end.default_1"), Component.translatable("item.goety_revelation.halo_of_the_end.default_2"));
        new CenterComponentGroup(Component.translatable("item.goety_revelation.bow_of_revelation.desc0"), Component.translatable("item.goety_revelation.bow_of_revelation.desc1"), Component.translatable("item.goety_revelation.bow_of_revelation.desc2"));
        new CenterComponentGroup(Component.translatable("item.goety_revelation.the_needle.tail0"), Component.translatable("item.goety_revelation.the_needle.tail1"));

        new CenterComponentGroup(Component.translatable("item.goety_revelation.eternal_watch.desc0"), Component.translatable("item.goety_revelation.eternal_watch.desc1"), Component.translatable("item.goety_revelation.eternal_watch.desc2"), Component.translatable("item.goety_revelation.eternal_watch.desc3"));
        new CenterComponentGroup(Component.translatable("item.goety_revelation.dimensional_will.desc0"), Component.translatable("item.goety_revelation.dimensional_will.desc1"));
    }

    private float width;

    public OdamaneFont(Function<ResourceLocation, FontSet> p_243253_, boolean p_243245_, Map<ResourceLocation, FontSet> f) {
        super(p_243253_, p_243245_);
    }

    public void push(float width) {
        this.width = width;
    }

    public void pop() {
        this.width = 0;
    }

    @Override
    public int drawInBatch(FormattedCharSequence p_273262_, float p_273006_, float p_273254_, int p_273375_, boolean p_273674_, Matrix4f p_273525_, MultiBufferSource p_272624_, DisplayMode p_273418_, int p_273330_, int p_272981_) {
        String text = FontTextBuilder.formattedCharSequenceToString(p_273262_);
        if (width > 0F) {
            for (CenterComponentGroup ccg : centerComponentGroups) {
                if (ccg.is(text)) {
                    p_273525_.translate(-width(p_273262_) / 2F + width / 2F, 0, 0);
                    int i = super.drawInBatch(p_273262_, p_273006_, p_273254_, p_273375_, p_273674_, p_273525_, p_272624_, p_273418_, p_273330_, p_272981_);
                    p_273525_.translate(width(p_273262_) / 2F - width / 2F, 0, 0);
                    return i;
                }
            }
        }
        return super.drawInBatch(p_273262_, p_273006_, p_273254_, p_273375_, p_273674_, p_273525_, p_272624_, p_273418_, p_273330_, p_272981_);
    }
}
