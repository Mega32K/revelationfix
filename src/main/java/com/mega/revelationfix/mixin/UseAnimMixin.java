package com.mega.revelationfix.mixin;

import com.mega.revelationfix.common.client.enums.ModUseAnim;
import com.mega.revelationfix.util.EventUtil;
import net.minecraft.world.item.UseAnim;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(UseAnim.class)
public class UseAnimMixin {

    @Shadow(remap = false)
    @Final
    @Mutable
    private static UseAnim[] $VALUES;

    UseAnimMixin(String id, int ordinal) {
        throw new AssertionError("NONE");
    }

    @Inject(
            at = {@At(
                    value = "FIELD",
                    shift = At.Shift.AFTER,
                    target = "Lnet/minecraft/world/item/UseAnim;$VALUES:[Lnet/minecraft/world/item/UseAnim;"
            )},
            method = {"<clinit>"}
    )
    private static void addFormatting(CallbackInfo ci) {
        int ordinal = $VALUES.length;
        $VALUES = Arrays.copyOf($VALUES, ordinal + 1);
        ModUseAnim.GUNGNIR_SPEAR = (UseAnim) (Object) (new UseAnimMixin("GUNGNIR_SPEAR", ordinal));
        $VALUES[ordinal] = ModUseAnim.GUNGNIR_SPEAR;
    }
}
