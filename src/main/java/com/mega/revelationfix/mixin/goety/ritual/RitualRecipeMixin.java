package com.mega.revelationfix.mixin.goety.ritual;

import com.Polarice3.Goety.common.crafting.RitualRecipe;
import com.mega.revelationfix.safe.RitualRecipeInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(RitualRecipe.class)
public class RitualRecipeMixin implements RitualRecipeInterface {
    @Unique
    private boolean revelationfix$keepingNBT = false;

    @Override
    public boolean revelationfix$isKeepingNbt() {
        return revelationfix$keepingNBT;
    }

    @Override
    public void revelationfix$setKeepingNBT(boolean keeping) {
        revelationfix$keepingNBT = keeping;
    }
}
