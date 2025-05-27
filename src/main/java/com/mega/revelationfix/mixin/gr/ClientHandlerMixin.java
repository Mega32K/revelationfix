package com.mega.revelationfix.mixin.gr;

import com.Polarice3.Goety.client.render.model.ApostleModel;
import com.Polarice3.Goety.common.entities.boss.Apostle;
import net.minecraftforge.client.event.RenderLivingEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import z1gned.goetyrevelation.client.event.ClientHandler;

@Mixin(value = ClientHandler.class, remap = false)
public class ClientHandlerMixin {
    @Inject(method = "apollyonDeath", at = @At("HEAD"), cancellable = true)
    private static void cancel(RenderLivingEvent<Apostle, ApostleModel<Apostle>> event, CallbackInfo ci) {
        ci.cancel();
    }
}
