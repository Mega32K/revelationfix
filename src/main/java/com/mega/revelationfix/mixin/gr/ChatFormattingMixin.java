package com.mega.revelationfix.mixin.gr;

import com.mega.revelationfix.client.enums.ModChatFormatting;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(value = ChatFormatting.class, priority = 232424314)
public class ChatFormattingMixin {
    @Shadow(remap = false)
    @Final
    @Mutable
    private static ChatFormatting[] $VALUES;

    ChatFormattingMixin(String id, int ordinal, String name, char code, int colorIndex, @Nullable Integer colorValue) {
        throw new AssertionError("NONE");
    }

    @Inject(
            at = {@At(
                    value = "FIELD",
                    shift = At.Shift.AFTER,
                    target = "Lnet/minecraft/ChatFormatting;$VALUES:[Lnet/minecraft/ChatFormatting;"
            )},
            method = {"<clinit>"}
    )
    private static void addFormatting(CallbackInfo ci) {
        int ordinal = $VALUES.length;

        $VALUES = Arrays.copyOf($VALUES, ordinal + 2);
        ModChatFormatting.APOLLYON = (ChatFormatting) (Object) (new ChatFormattingMixin("APOLLYON", ordinal, "APOLLYON", 'q', 0, 0));
        $VALUES[ordinal] = ModChatFormatting.APOLLYON;
        ModChatFormatting.EDEN = (ChatFormatting) (Object) (new ChatFormattingMixin("GR_EDEN", ordinal + 1, "GR_EDEN", '-', 18, 0x7f8492));
        $VALUES[ordinal + 1] = ModChatFormatting.EDEN;
    }
}

