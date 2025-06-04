package com.mega.revelationfix.client.citadel;

import net.minecraft.client.renderer.ShaderInstance;

import javax.annotation.Nullable;

public class GRShaders {
    private static ShaderInstance renderTypeHologramShader;
    private static ShaderInstance LIGHT_BEACON_BEAM;

    @Nullable
    public static ShaderInstance getRenderTypeHologramShader() {
        return renderTypeHologramShader;
    }

    public static void setRenderTypeHologramShader(ShaderInstance instance) {
        renderTypeHologramShader = instance;
    }

    public static ShaderInstance getLightBeaconBeam() {
        return LIGHT_BEACON_BEAM;
    }

    public static void setLightBeaconBeam(ShaderInstance instance) {
        LIGHT_BEACON_BEAM = instance;
    }
}
