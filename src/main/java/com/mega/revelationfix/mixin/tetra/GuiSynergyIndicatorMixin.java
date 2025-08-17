package com.mega.revelationfix.mixin.tetra;

import com.mega.endinglib.util.annotation.DeprecatedMixin;
import com.mega.endinglib.util.annotation.ModDependsMixin;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import se.mickelus.tetra.gui.GuiSynergyIndicator;
import se.mickelus.tetra.module.data.SynergyData;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mixin(value = GuiSynergyIndicator.class, remap = false)
@DeprecatedMixin
@ModDependsMixin("tetra")
public class GuiSynergyIndicatorMixin {
    @Inject(method = "getHeaderLine", at = @At("HEAD"), cancellable = true)
    private void getHeaderLine(boolean isActive, SynergyData data, CallbackInfoReturnable<String> cir) {
        String var10000 = isActive ? ChatFormatting.GREEN + "» " + ChatFormatting.WHITE : ChatFormatting.BOLD + "  " + ChatFormatting.DARK_GRAY;
        cir.setReturnValue(var10000 +
                Stream.concat(
                        Arrays.stream(data.moduleVariants)
                                .map((key) -> I18n.get("tetra.variant." + key)),
                        Stream.concat(
                                Arrays.stream(data.modules)
                                        .map((key) -> I18n.get("tetra.module." + key + ".name")),
                                Arrays.stream(data.improvements)
                                        .filter((key) -> I18n.exists("tetra.improvement." + key + ".synergy_expanded_name"))
                                        .map((key) -> I18n.get("tetra.synergy.improvement.header") + I18n.get("tetra.improvement." + key + ".synergy_expanded_name"))
                        )
                ).collect(Collectors.joining(" + ")));
    }
}
