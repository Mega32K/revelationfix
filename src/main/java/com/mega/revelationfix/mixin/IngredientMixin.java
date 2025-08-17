package com.mega.revelationfix.mixin;

import com.mega.endinglib.util.annotation.DeprecatedMixin;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = Ingredient.class, priority = 0)
@DeprecatedMixin
public abstract class IngredientMixin {
    @Shadow
    @Final
    private Ingredient.Value[] values;

    @Shadow(remap = false)
    public abstract boolean isVanilla();

    /*
    @Inject(method = {"test(Lnet/minecraft/world/item/ItemStack;)Z"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/Ingredient;getItems()[Lnet/minecraft/world/item/ItemStack;")}, cancellable = true)
    private void goety_revelation$fasterTagIngredientTest(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (isVanilla() && this.values.length == 1) {
            Ingredient.Value value = this.values[0];
            if (value instanceof Ingredient.TagValue) {
                Ingredient.TagValue tagValue = (Ingredient.TagValue)value;
                if (!MinecraftServerReloadTracker.isReloadActive())
                    cir.setReturnValue(stack.is(tagValue.tag));
            }
        }
    }
     */
}
