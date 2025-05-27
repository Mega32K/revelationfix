package com.mega.revelationfix.mixin;

import net.minecraft.client.gui.font.GlyphRenderTypes;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BakedGlyph.class)
public interface EmptyGlyphAccessor {
    @Accessor("renderTypes")
    GlyphRenderTypes renderTypes();

    @Accessor("u0")
    float u0();

    @Accessor("u1")
    float u1();

    @Accessor("v0")
    float v0();

    @Accessor("v1")
    float v1();

    @Accessor("left")
    float left();

    @Accessor("right")
    float right();

    @Accessor("up")
    float up();

    @Accessor("down")
    float down();
}
