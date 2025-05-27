package com.mega.revelationfix.mixin.fantasy_ending.time.time;

import com.mega.revelationfix.safe.NoModDependsMixin;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.DeathScreen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(DeathScreen.class)
@NoModDependsMixin("fantasy_ending")
public class DeathScreenMixin {
    @Shadow
    @Final
    private List<Button> exitButtons;

    @Shadow
    @Final
    private boolean hardcore;

    @Inject(method = "init", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
    }
}
