package com.mega.revelationfix.mixin.patchouli;

import com.Polarice3.Goety.common.effects.brew.BrewEffect;
import com.Polarice3.Goety.common.effects.brew.BrewEffects;
import com.Polarice3.Goety.compat.patchouli.BrewingCatalystProcessor;
import com.mega.endinglib.util.annotation.ModDependsMixin;
import com.mega.revelationfix.common.data.brew.BrewData;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = BrewingCatalystProcessor.class, remap = false)
@ModDependsMixin("patchouli")
public class BrewingCatalystProcessorMixin {
    @Redirect(method = "setup", at = @At(value = "INVOKE", target = "Lcom/Polarice3/Goety/common/effects/brew/BrewEffects;getBrewEffect(Ljava/lang/String;)Lcom/Polarice3/Goety/common/effects/brew/BrewEffect;"))
    private BrewEffect redirectInit0(BrewEffects instance, String s) {
        if (BrewEffects.INSTANCE == null) {
            BrewEffects.INSTANCE = new BrewEffects();
            BrewData.reRegister();
        }
        instance = BrewEffects.INSTANCE;
        return instance.getBrewEffect(s);
    }
    @Redirect(method = "process", at = @At(value = "INVOKE", target = "Lcom/Polarice3/Goety/common/effects/brew/BrewEffects;getCatalystFromEffect(Ljava/lang/String;)Lnet/minecraft/world/item/ItemStack;"))
    private ItemStack redirectInit1(BrewEffects instance, String string) {
        if (BrewEffects.INSTANCE == null) {
            BrewEffects.INSTANCE = new BrewEffects();
            BrewData.reRegister();
        }
        instance = BrewEffects.INSTANCE;
        return instance.getCatalystFromEffect(string);
    }
}
