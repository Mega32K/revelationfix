package com.mega.revelationfix.mixin.gr;

import com.Polarice3.Goety.client.audio.BossLoopMusic;
import com.Polarice3.Goety.client.audio.PostBossMusic;
import com.Polarice3.Goety.common.entities.boss.Apostle;
import net.minecraft.client.resources.sounds.TickableSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import z1gned.goetyrevelation.ModMain;
import z1gned.goetyrevelation.util.ApollyonAbilityHelper;

@Mixin(BossLoopMusic.class)
public class BossLoopMusicMixin {

    @Shadow(remap = false)
    @Final
    protected Mob mobEntity;

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sounds/SoundManager;queueTickingSound(Lnet/minecraft/client/resources/sounds/TickableSoundInstance;)V", ordinal = 0), method = "tick")
    private void setPost(SoundManager instance, TickableSoundInstance p_120373_) {
        ApollyonAbilityHelper helper;
        if (this.mobEntity instanceof Apostle apostle && (helper = (ApollyonAbilityHelper) apostle).allTitlesApostle_1_20_1$isApollyon()) {
            if (apostle.isInNether()) {
                if (apostle.deathTime > 5) {
                    instance.queueTickingSound(new PostBossMusic(ModMain.APOLLYON_THEME_POST.get(), this.mobEntity));
                }
            } else instance.queueTickingSound(new PostBossMusic(ModMain.APOLLYON_THEME_POST.get(), this.mobEntity));
        } else {
            instance.queueTickingSound(p_120373_);
        }
    }

}