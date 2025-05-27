package com.mega.revelationfix.mixin.goety;

import com.Polarice3.Goety.common.effects.brew.BrewEffects;
import com.Polarice3.Goety.common.effects.brew.modifiers.BrewModifier;
import com.Polarice3.Goety.common.effects.brew.modifiers.CapacityModifier;
import com.Polarice3.Goety.common.items.ModItems;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BrewEffects.class, remap = false)
public abstract class BrewEffectsMixin {
    @Shadow(remap = false)
    protected abstract void modifierRegister(BrewModifier modifier, Item ingredient);

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        this.modifierRegister(new CapacityModifier(6), ModItems.UNHOLY_BLOOD.get());
    }
}
