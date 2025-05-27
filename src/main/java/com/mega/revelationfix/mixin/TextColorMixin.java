package com.mega.revelationfix.mixin;

import com.mega.revelationfix.safe.TextColorInterface;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TextColor.class)
public class TextColorMixin implements TextColorInterface {
    @Unique
    private char revelationfix$chatCode = ' ';

    @Override
    public void revelationfix$setCode(char code) {
        this.revelationfix$chatCode = code;
    }

    @Override
    public char revelationfix$getCode() {
        return revelationfix$chatCode;
    }

    @Inject(method = "<init>(ILjava/lang/String;)V", at = @At("RETURN"))
    private void init(int p_131263_, String p_131264_, CallbackInfo ci) {
        ChatFormatting chatFormatting;
        if ((chatFormatting = ChatFormatting.getByName(p_131264_)) != null)
            this.revelationfix$setCode(chatFormatting.getChar());
    }
}
