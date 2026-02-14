package com.mega.revelationfix.mixin.goety.ritual;

import com.Polarice3.Goety.common.crafting.RitualRecipe;
import com.mega.revelationfix.safe.RitualRecipeInterface;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Inject;

import java.util.Map;
import java.util.Set;

@Mixin(RitualRecipe.class)
public class RitualRecipeMixin implements RitualRecipeInterface {
    @Unique
    private boolean revelationfix$keepingNBT = false;
    @Unique
    private Set<Integer> inputIndexsToRemaing = new IntArraySet();
    @Unique
    private Map<Integer, ItemStack> remaing = new Int2ObjectArrayMap<>();

    @Override
    public boolean revelationfix$isKeepingNbt() {
        return revelationfix$keepingNBT;
    }

    @Override
    public void revelationfix$setKeepingNBT(boolean keeping) {
        revelationfix$keepingNBT = keeping;
    }
    @Override
    public Set<Integer> inputsToRemaining() {
        return inputIndexsToRemaing;
    }

    @Override
    public Map<Integer, ItemStack> remaining() {
        return remaing;
    }
}
