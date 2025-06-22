package com.mega.revelationfix.mixin.patchouli;

import com.Polarice3.Goety.common.effects.brew.BrewEffect;
import com.Polarice3.Goety.common.effects.brew.BrewEffects;
import com.Polarice3.Goety.compat.patchouli.BrewingSacrificeProcessor;
import com.mega.endinglib.util.annotation.ModDependsMixin;
import com.mega.revelationfix.common.data.brew.BrewData;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = BrewingSacrificeProcessor.class, remap = false)
@ModDependsMixin("patchouli")
public class BrewingSacrificeProcessorMixin {
    @Redirect(method = "setup", at = @At(value = "INVOKE", target = "Lcom/Polarice3/Goety/common/effects/brew/BrewEffects;getBrewEffect(Ljava/lang/String;)Lcom/Polarice3/Goety/common/effects/brew/BrewEffect;"))
    private BrewEffect redirectInit0(BrewEffects instance, String s) {
        if (BrewEffects.INSTANCE == null) {
            BrewEffects.INSTANCE = new BrewEffects();
            BrewData.reRegister();
        }
        instance = BrewEffects.INSTANCE;
        return instance.getBrewEffect(s);
    }
    @Redirect(method = "process", at = @At(value = "INVOKE", target = "Lcom/Polarice3/Goety/common/effects/brew/BrewEffects;getSacrificeFromEffect(Ljava/lang/String;)Lnet/minecraft/world/entity/EntityType;"))
    private EntityType<?> redirectInit1(BrewEffects instance, String string) {
        if (BrewEffects.INSTANCE == null) {
            BrewEffects.INSTANCE = new BrewEffects();
            BrewData.reRegister();
        }
        instance = BrewEffects.INSTANCE;
        return instance.getSacrificeFromEffect(string);
    }
}
