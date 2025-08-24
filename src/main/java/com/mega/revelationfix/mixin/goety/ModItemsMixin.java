package com.mega.revelationfix.mixin.goety;

import com.Polarice3.Goety.common.items.ModItems;
import com.Polarice3.Goety.common.items.curios.SingleStackItem;
import com.mega.revelationfix.common.item.goety.curios.SpitefulBeltItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.function.Supplier;

@Mixin(ModItems.class)
public abstract class ModItemsMixin {
    @Redirect(method = "<clinit>",
            slice = @Slice(
                    from = @At(value = "CONSTANT", args = "stringValue=spiteful_belt")
            ),
            at = @At(
                    remap = false,
                    ordinal = 0,
                    value = "INVOKE",
                    target = "Lnet/minecraftforge/registries/DeferredRegister;register(Ljava/lang/String;Ljava/util/function/Supplier;)Lnet/minecraftforge/registries/RegistryObject;"
            )
    )
    private static RegistryObject<SingleStackItem> redirectSpitefulBelt(DeferredRegister<Item> instance, String s, Supplier<? extends Item> name) {
        return instance.register(s, SpitefulBeltItem::new);
    }
}
