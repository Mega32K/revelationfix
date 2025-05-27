package com.mega.revelationfix.mixin.forge;

import net.minecraftforge.eventbus.EventBus;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBusInvokeDispatcher;
import net.minecraftforge.eventbus.api.IEventListener;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = EventBus.class)
public abstract class RevelationBusThrowableFix {
    @Shadow(remap = false)
    @Final
    private static Logger LOGGER;

    @Inject(remap = false, method = "post(Lnet/minecraftforge/eventbus/api/Event;Lnet/minecraftforge/eventbus/api/IEventBusInvokeDispatcher;)Z", at = @At(remap = false, value = "INVOKE", target = "Lnet/minecraftforge/eventbus/api/IEventExceptionHandler;handleException(Lnet/minecraftforge/eventbus/api/IEventBus;Lnet/minecraftforge/eventbus/api/Event;[Lnet/minecraftforge/eventbus/api/IEventListener;ILjava/lang/Throwable;)V", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
    private void post(Event event, IEventBusInvokeDispatcher wrapper, CallbackInfoReturnable<Boolean> cir, IEventListener[] listeners, int index, Throwable throwable) {

    }
}
