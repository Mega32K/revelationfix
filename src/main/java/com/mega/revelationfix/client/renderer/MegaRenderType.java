package com.mega.revelationfix.client.renderer;

import com.mega.revelationfix.client.citadel.GRShaders;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiFunction;

public class MegaRenderType extends RenderType {
    public static int id = 1;
    private static final BiFunction<ResourceLocation, Boolean, RenderType> ALWAYS_TRANSPARENCY_CORE = Util.memoize((p_286166_, p_286167_) -> {
        RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder().setShaderState(RenderStateShard.RENDERTYPE_EYES_SHADER).setTextureState(new RenderStateShard.TextureStateShard(p_286166_, false, false)).setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY).setDepthTestState(RenderStateShard.NO_DEPTH_TEST).createCompositeState(p_286167_);
        return create("always_transparency_core", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, false, rendertype$compositestate);
    });
    protected static final RenderStateShard.TransparencyStateShard EYES_ALPHA_TRANSPARENCY = new RenderStateShard.TransparencyStateShard("eyes_alpha_transparency", () -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    });
    private static final RenderType  GUI_OVERLAY_NO_CULL = RenderType.create("gui_overlay_no_cull", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_GUI_OVERLAY_SHADER).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setDepthTestState(NO_DEPTH_TEST).setCullState(NO_CULL).setWriteMaskState(COLOR_WRITE).createCompositeState(false));

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_LIGHT_BEACON_BEAM_SHADER = new RenderStateShard.ShaderStateShard(GRShaders::getLightBeaconBeam);


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

    public static RenderType alwaysEnergySwirl(ResourceLocation p_110437_, float p_110438_, float p_110439_) {
        return create("always_energy_swirl", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_ENERGY_SWIRL_SHADER).setTextureState(new RenderStateShard.TextureStateShard(p_110437_, false, false)).setTexturingState(new RenderStateShard.OffsetTexturingStateShard(p_110438_, p_110439_)).setDepthTestState(RenderStateShard.NO_DEPTH_TEST).setTransparencyState(ADDITIVE_TRANSPARENCY).setCullState(NO_CULL).setLightmapState(LIGHTMAP).setOverlayState(OVERLAY).createCompositeState(false));
    }
    public static RenderType alwaysTransparencyEntityCutoutNoCull(ResourceLocation p_110444_) {
        return ALWAYS_TRANSPARENCY_CORE.apply(p_110444_, false);
    }
    public static RenderType guiOverlayNoCull() {
        return GUI_OVERLAY_NO_CULL;
    }
}
