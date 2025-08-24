package com.mega.revelationfix.mixin.goety;

import com.Polarice3.Goety.common.commands.GoetyCommand;
import com.mega.revelationfix.common.command.GoetyRevelationCommand;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GoetyCommand.class)
public class GoetyCommandMixin {
    @Inject(remap = false, method = "register", at = @At("HEAD"))
    private static void register(CommandDispatcher<CommandSourceStack> pDispatcher, CommandBuildContext p_250122_, CallbackInfo ci) {
        GoetyRevelationCommand.register(pDispatcher, p_250122_, ci);
    }
}
