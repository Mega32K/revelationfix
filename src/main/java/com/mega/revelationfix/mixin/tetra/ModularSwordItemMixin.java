package com.mega.revelationfix.mixin.tetra;

import com.mega.revelationfix.common.compat.tetra.effect.FadingEffect;
import com.mega.revelationfix.safe.mixinpart.ModDependsMixin;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import se.mickelus.tetra.items.modular.ItemModularHandheld;
import se.mickelus.tetra.items.modular.impl.ModularBladedItem;

@Mixin(ModularBladedItem.class)
@ModDependsMixin("tetra")
public abstract class ModularSwordItemMixin extends ItemModularHandheld {

    public ModularSwordItemMixin(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(@NotNull ItemStack itemStack) {
        if (itemStack.getItem() instanceof ModularBladedItem bladedItem) {
            if (bladedItem.getEffectLevel(itemStack, FadingEffect.itemEffect) > 0)
                return false;
        }
        return super.isFoil(itemStack);
    }
}
