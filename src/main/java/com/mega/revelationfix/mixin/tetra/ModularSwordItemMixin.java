package com.mega.revelationfix.mixin.tetra;

import com.mega.endinglib.api.client.text.TextColorUtils;
import com.mega.endinglib.api.item.NameCenteredItem;
import com.mega.endinglib.util.SafeClass;
import com.mega.endinglib.util.annotation.ModDependsMixin;
import com.mega.revelationfix.common.compat.tetra.effect.FadingEffect;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
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

    @Override
    public Component getName(ItemStack stack) {
        if (stack.getItem() instanceof ModularBladedItem bladedItem) {
            if (bladedItem.getEffectLevel(stack, FadingEffect.itemEffect) > 0) {
                Component component = super.getName(stack);
                if (!SafeClass.isModernUILoaded() && component instanceof MutableComponent mc) {
                    return mc.withStyle(TextColorUtils.MIDDLE);
                } else {
                    return component;
                }
            }
        }
        return super.getName(stack);
    }
}
