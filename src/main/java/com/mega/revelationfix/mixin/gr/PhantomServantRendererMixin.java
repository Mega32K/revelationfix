package com.mega.revelationfix.mixin.gr;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import z1gned.goetyrevelation.ModMain;
import z1gned.goetyrevelation.client.render.PhantomServantRenderer;
import z1gned.goetyrevelation.entitiy.PhantomServant;

@Mixin(PhantomServantRenderer.class)
public class PhantomServantRendererMixin {
    @Unique
    private static final ResourceLocation NETHER_TEXTURE = new ResourceLocation(ModMain.MODID, "textures/entity/phantom_servant/phantom_servant.png");

    @Inject(remap = false, method = "getTextureLocation(Lz1gned/goetyrevelation/entitiy/PhantomServant;)Lnet/minecraft/resources/ResourceLocation;", at = @At("HEAD"), cancellable = true)
    private void getTextureLocation(PhantomServant p_115679_, CallbackInfoReturnable<ResourceLocation> cir) {
        if (p_115679_.level.dimension() == Level.NETHER)
            cir.setReturnValue(NETHER_TEXTURE);
    }
}
