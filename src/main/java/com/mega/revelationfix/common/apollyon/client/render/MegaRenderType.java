package com.mega.revelationfix.common.apollyon.client.render;

import com.mega.revelationfix.common.client.citadel.GRShaders;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class MegaRenderType extends RenderType {
    protected static final RenderStateShard.TransparencyStateShard EYES_ALPHA_TRANSPARENCY = new RenderStateShard.TransparencyStateShard("eyes_alpha_transparency", () -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    });
    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_LIGHT_BEACON_BEAM_SHADER = new RenderStateShard.ShaderStateShard(GRShaders::getLightBeaconBeam);
    public static int id = 1;

    public MegaRenderType(String p_173178_, VertexFormat p_173179_, VertexFormat.Mode p_173180_, int p_173181_, boolean p_173182_, boolean p_173183_, Runnable p_173184_, Runnable p_173185_) {
        super(p_173178_, p_173179_, p_173180_, p_173181_, p_173182_, p_173183_, p_173184_, p_173185_);
    }

    public static RenderType particle(ResourceLocation location) {
        id++;
        TextureStateShard textureStateShard = new TextureStateShard(location, false, false);
        CompositeState compositeState = CompositeState.builder()
                .setTextureState(textureStateShard)
                .setShaderState(RenderStateShard.POSITION_COLOR_TEX_LIGHTMAP_SHADER)
                .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                .setCullState(NO_CULL)
                .setOverlayState(NO_OVERLAY)
                .setWriteMaskState(COLOR_WRITE)
                .createCompositeState(false);
        return create("uom" + ":particle" + id, DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, true, true, compositeState);
    }

    public static RenderType lightParticle(ResourceLocation location) {
        id++;
        RenderStateShard.TextureStateShard textureStateShard = new RenderStateShard.TextureStateShard(location, false, false);
        RenderType.CompositeState compositeState = RenderType.CompositeState.builder()
                .setTextureState(textureStateShard)
                .setShaderState(RENDERTYPE_LIGHT_BEACON_BEAM_SHADER)
                .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                .setCullState(NO_CULL)
                .setOverlayState(OVERLAY)
                .setOutputState(RenderStateShard.PARTICLES_TARGET)
                .setWriteMaskState(COLOR_WRITE)
                .createCompositeState(false);
        return create("uom" + ":light_particle" + id, DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 2097152, true, true, compositeState);
    }

    public static RenderType getEyesAlphaEnabled(ResourceLocation locationIn) {
        RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder().setShaderState(RENDERTYPE_EYES_SHADER).setTextureState(new TextureStateShard(locationIn, false, false)).setTransparencyState(EYES_ALPHA_TRANSPARENCY).setOutputState(RenderStateShard.PARTICLES_TARGET).setCullState(NO_CULL).setLightmapState(LIGHTMAP).setOverlayState(OVERLAY).setDepthTestState(EQUAL_DEPTH_TEST).createCompositeState(true);
        return create("eye_alpha", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, false, rendertype$compositestate);
    }

    public static RenderType getEyesAlphaEnabled() {
        RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder().setShaderState(RENDERTYPE_EYES_SHADER).setTextureState(RenderStateShard.BLOCK_SHEET_MIPPED).setTransparencyState(EYES_ALPHA_TRANSPARENCY).setCullState(NO_CULL).setLightmapState(LIGHTMAP).setOverlayState(OVERLAY).setDepthTestState(EQUAL_DEPTH_TEST).createCompositeState(true);
        return create("eye_alpha", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, false, rendertype$compositestate);
    }
}
