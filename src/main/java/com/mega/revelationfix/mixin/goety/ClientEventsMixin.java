package com.mega.revelationfix.mixin.goety;

import com.Polarice3.Goety.client.events.ClientEvents;
import com.mega.revelationfix.common.config.ClientConfig;
import com.mega.revelationfix.common.init.ModSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import z1gned.goetyrevelation.ModMain;

@Mixin(value = ClientEvents.class, remap = false)
public abstract class ClientEventsMixin {

    @Shadow(remap = false)
    public static void playBossMusic(SoundEvent soundEvent, Mob mob, float volume, float pitch) {
    }
    @ModifyVariable(method = "playBossMusic(Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/world/entity/Mob;FF)V", at = @At("HEAD"), argsOnly = true, index = 0)
    private static SoundEvent replaceOldApollyonBossMusic(SoundEvent soundEvent) {
        if (ClientConfig.enableNewBossMusic)
            if (soundEvent == ModMain.APOLLYON_NETHER_THEME.get()) {
                soundEvent = ModSounds.APOLLYON_NEW_NETHER_THEME.get();
            }
        return soundEvent;
    }
}
