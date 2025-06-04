package com.mega.revelationfix.mixin.gr;

import com.Polarice3.Goety.client.events.ClientEvents;
import com.Polarice3.Goety.common.entities.boss.Apostle;
import com.Polarice3.Goety.init.ModSounds;
import com.mega.revelationfix.common.config.ClientConfig;
import com.mega.revelationfix.common.entity.boss.ApostleServant;
import com.mega.revelationfix.safe.level.ClientLevelExpandedContext;
import com.mega.revelationfix.safe.level.ClientLevelInterface;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.entity.living.LivingEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import z1gned.goetyrevelation.ModMain;
import z1gned.goetyrevelation.util.ApollyonAbilityHelper;

@Mixin(ClientEvents.class)
public abstract class ClientEventsMixin {

    @Shadow(remap = false)
    public static void playBossMusic(SoundEvent soundEvent, SoundEvent postBossMusic, Mob mob) {

    }

    @Inject(at = @At(value = "INVOKE", target = "Lcom/Polarice3/Goety/client/events/ClientEvents;playBossMusic(Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/world/entity/Mob;)V"), method = "onEntityTick", cancellable = true, remap = false)
    private static void playSounds(LivingEvent.LivingTickEvent event, CallbackInfo ci) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Apostle apostle) {
            ci.cancel();
            if (((ApollyonAbilityHelper) apostle).allTitlesApostle_1_20_1$isApollyon()) {
                if (apostle.isInNether()) {
                    ClientLevelExpandedContext context = ((ClientLevelInterface) apostle.level).revelationfix$ECData();
                    if (context.isNetherApollyon()) {
                        SoundEvent soundEvent = ClientConfig.enableNewBossMusic ? com.mega.revelationfix.common.init.ModSounds.APOLLYON_NEW_NETHER_THEME.get() : ModMain.APOLLYON_NETHER_THEME.get();
                        playBossMusic(soundEvent, ModMain.APOLLYON_THEME_POST.get(), apostle);
                    }
                } else {
                    playBossMusic(ModMain.APOLLYON_OVERWORLD_THEME.get(), ModMain.APOLLYON_THEME_POST.get(), apostle);
                }
            } else if (!(apostle instanceof ApostleServant)) {
                playBossMusic(ModSounds.APOSTLE_THEME.get(), ModSounds.APOSTLE_THEME_POST.get(), apostle);
            }
        }
    }
}
