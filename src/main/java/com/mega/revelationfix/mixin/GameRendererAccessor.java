package com.mega.revelationfix.mixin;

import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GameRenderer.class)
public interface GameRendererAccessor {
    @Accessor("oldFov")
    float oldFov();

    @Accessor("fov")
    float fov();

    @Accessor("panoramicMode")
    boolean panoramicMode();
}
