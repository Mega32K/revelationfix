package com.mega.revelationfix.mixin.goety;

import com.Polarice3.Goety.client.events.ClientEvents;
import com.mega.revelationfix.common.config.ClientConfig;
import com.mega.revelationfix.common.init.ModSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import z1gned.goetyrevelation.ModMain;

@Mixin(value = ClientEvents.class, remap = false)
public abstract class ClientEventsMixin {
    @Shadow
    public static void playBossMusic(SoundEvent soundEvent, Mob mob, float volume) {
    }

    @Inject(method = "playBossMusic(Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/world/entity/Mob;)V", at = @At("HEAD"), cancellable = true)
    private static void playBossMusic(SoundEvent soundEvent, Mob mob, CallbackInfo ci) {
        if (ClientConfig.enableNewBossMusic)
            if (soundEvent == ModMain.APOLLYON_NETHER_THEME.get()) {
                ci.cancel();
                playBossMusic(ModSounds.APOLLYON_NEW_NETHER_THEME.get(), mob, 1.0F);
            }
    }
}
