package com.mega.revelationfix.common.client.citadel;

import com.mega.revelationfix.proxy.ClientProxy;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public abstract class GRRenderTypes extends RenderType {
    public static final Function<ResourceLocation, RenderType> ITEM_ENTITY_TRANSLUCENT_CULL = Util.memoize((p_286155_) -> {
        RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder().setShaderState(RENDERTYPE_ITEM_ENTITY_TRANSLUCENT_CULL_SHADER).setTextureState(new RenderStateShard.TextureStateShard(p_286155_, false, false)).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setOutputState(RenderStateShard.OUTLINE_TARGET).setLightmapState(LIGHTMAP).setOverlayState(OVERLAY).setWriteMaskState(RenderStateShard.COLOR_DEPTH_WRITE).createCompositeState(true);
        return create("item_entity_translucent_cull", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, rendertype$compositestate);
    });
    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_HOLOGRAM_SHADER = new RenderStateShard.ShaderStateShard(GRShaders::getRenderTypeHologramShader);
    protected static final RenderStateShard.OutputStateShard HOLOGRAM_OUTPUT = new RenderStateShard.OutputStateShard("hologram_target", () -> {
        RenderTarget target = PostEffectRegistry.getRenderTargetFor(ClientProxy.HOLOGRAM_SHADER);
        if (target != null) {
            target.copyDepthFrom(Minecraft.getInstance().getMainRenderTarget());
            target.bindWrite(false);
        }
    }, () -> {
        Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
    });
    protected static final RenderStateShard.OutputStateShard ODAMANE_OUTPUT = new RenderStateShard.OutputStateShard("odamane_target", () -> {
        RenderTarget target = PostEffectRegistry.getRenderTargetFor(ClientProxy.ODAMANE_SHADER);
        if (target != null) {
            target.copyDepthFrom(Minecraft.getInstance().getMainRenderTarget());
            target.bindWrite(false);
        }
    }, () -> {
        Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
    });
    private static final Function<ResourceLocation, RenderType> WRAITH = Util.memoize((p_173253_) -> {
        RenderStateShard.TextureStateShard renderstateshard$texturestateshard = new RenderStateShard.TextureStateShard(p_173253_, false, false);
        return RenderType.create("wraith", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, CompositeState.builder().setShaderState(RENDERTYPE_EYES_SHADER).setTextureState(renderstateshard$texturestateshard).setTransparencyState(ADDITIVE_TRANSPARENCY).setCullState(RenderStateShard.CULL).setOverlayState(OVERLAY).setWriteMaskState(RenderStateShard.COLOR_DEPTH_WRITE).createCompositeState(false));
    });

    public GRRenderTypes(String p_173178_, VertexFormat p_173179_, VertexFormat.Mode p_173180_, int p_173181_, boolean p_173182_, boolean p_173183_, Runnable p_173184_, Runnable p_173185_) {
        super(p_173178_, p_173179_, p_173180_, p_173181_, p_173182_, p_173183_, p_173184_, p_173185_);
    }

    public static RenderType energySwirl(ResourceLocation p_110437_, float p_110438_, float p_110439_) {
        return create("energy_swirl", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_EYES_SHADER).setTextureState(new RenderStateShard.TextureStateShard(p_110437_, false, false)).setTexturingState(new RenderStateShard.OffsetTexturingStateShard(p_110438_, p_110439_)).setTransparencyState(ADDITIVE_TRANSPARENCY).setCullState(NO_CULL).setLightmapState(LIGHTMAP).setOverlayState(OVERLAY).createCompositeState(false));
    }

    public static RenderType wraith(ResourceLocation p_110459_) {
        return WRAITH.apply(p_110459_);
    }

    public static RenderType getHologramLights() {
        return create("hologram_lights", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, true, true, RenderType.CompositeState.builder()
                .setShaderState(RENDERTYPE_HOLOGRAM_SHADER)
                .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                .setCullState(CULL)
                .setDepthTestState(LEQUAL_DEPTH_TEST)
                .setLightmapState(NO_LIGHTMAP)
                .setOutputState(HOLOGRAM_OUTPUT)
                .createCompositeState(false));
    }

    public static RenderType SOLID() {
        return create("solid_hologram", DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 2097152, true, false,
                RenderType.CompositeState.builder()
                        .setLightmapState(LIGHTMAP)
                        .setShaderState(RENDERTYPE_SOLID_SHADER)
                        .setTextureState(BLOCK_SHEET_MIPPED)
                        .setOutputState(HOLOGRAM_OUTPUT)
                        .createCompositeState(false));
    }

    public static RenderType getHologram(ResourceLocation locationIn) {
        return create("hologram", DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder()
                .setShaderState(RENDERTYPE_BEACON_BEAM_SHADER)
                .setCullState(NO_CULL)
                .setTextureState(new RenderStateShard.TextureStateShard(locationIn, false, false))
                .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                .setWriteMaskState(COLOR_DEPTH_WRITE)
                .setDepthTestState(LEQUAL_DEPTH_TEST)
                .setOutputState(HOLOGRAM_OUTPUT)
                .createCompositeState(false));
    }

    public static RenderType getOdamane(ResourceLocation locationIn) {
        return create("hologram", DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder()
                .setShaderState(RENDERTYPE_BEACON_BEAM_SHADER)
                .setCullState(NO_CULL)
                .setTextureState(new RenderStateShard.TextureStateShard(locationIn, false, false))
                .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                .setWriteMaskState(COLOR_DEPTH_WRITE)
                .setDepthTestState(LEQUAL_DEPTH_TEST)
                .setOutputState(ODAMANE_OUTPUT)
                .createCompositeState(false));
    }
}
