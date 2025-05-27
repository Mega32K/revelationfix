package com.mega.revelationfix.mixin.mousetweaks;

import com.mega.revelationfix.safe.ModDependsMixin;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import yalter.mousetweaks.Main;
import yalter.mousetweaks.forge.MouseTweaksForge;

@Mixin(MouseTweaksForge.class)
@ModDependsMixin("mousetweaks")
public class MouseTweaksForgeFixMixin {
    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lyalter/mousetweaks/Main;initialize()V", remap = false))
    private void init() {
        boolean z = true;
        try {
            Minecraft mc = Minecraft.getInstance();
            mc.getProfiler().push("");
            mc.getProfiler().pop();
        } catch (Throwable throwable) {
            z = false;
        }
        if (z) {
            Main.initialize();
        }
    }

}
