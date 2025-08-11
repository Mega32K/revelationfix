package com.mega.revelationfix.mixin;

import com.mega.revelationfix.common.config.ItemConfig;
import com.mega.revelationfix.util.entity.ATAHelper2;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends AbstractClientPlayer {
    @Shadow public Input input;

    LocalPlayerMixin(ClientLevel p_250460_, GameProfile p_249912_) {
        super(p_250460_, p_249912_);
    }

    @Inject(method = "aiStep", at = @At(value = "FIELD", target = "Lnet/minecraft/client/player/Input;forwardImpulse:F", opcode = Opcodes.PUTFIELD, ordinal = 0, shift = At.Shift.AFTER))
    private void aiStep(CallbackInfo ci) {
        if (ATAHelper2.hasGoldFeather(this)) {
            this.input.forwardImpulse *= ItemConfig.goldFeatherSpeedMultiplier;
            this.input.leftImpulse *= ItemConfig.goldFeatherSpeedMultiplier;
        }
    }
}
