package com.mega.revelationfix.mixin.enigmaticlegacy.bugfix;

import com.aizistral.enigmaticlegacy.objects.Vector3;
import com.aizistral.etherium.core.EtheriumEventHandler;
import com.aizistral.etherium.core.IEtheriumConfig;
import com.aizistral.etherium.items.EtheriumArmor;
import com.mega.revelationfix.safe.mixinpart.ModDependsMixin;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EtheriumEventHandler.class)
@ModDependsMixin("enigmaticlegacy")
public class EtheriumEventHandlerFix {
    @Shadow(remap = false)
    @Final
    private IEtheriumConfig config;

    @Inject(remap = false, method = "onEntityHurt", at = @At("HEAD"), cancellable = true)
    private void onEntityHurt(LivingHurtEvent event, CallbackInfo ci) {
        ci.cancel();
        LivingEntity beHurt = event.getEntity();
        if (beHurt instanceof Player player) {
            if (event.getAmount() > 0.0F && EtheriumArmor.hasShield(player)) {
                if (event.getSource().getDirectEntity() instanceof LivingEntity direct && event.getSource().getEntity() instanceof LivingEntity owner) {
                    Vector3 vec = Vector3.fromEntityCenter(player).subtract(Vector3.fromEntityCenter(owner)).normalize();
                    owner.knockback(0.75, vec.x, vec.z);
                    player.level().playSound(null, player.blockPosition(), config.getShieldTriggerSound(), SoundSource.PLAYERS, 1.0F, 0.9F + (float) (Math.random() * 0.1));
                    player.level().playSound(null, player.blockPosition(), config.getShieldTriggerSound(), SoundSource.PLAYERS, 1.0F, 0.9F + (float) (Math.random() * 0.1));
                }

                event.setAmount(event.getAmount() * config.getShieldReduction().asModifierInverted());
            }
        }
    }
}
