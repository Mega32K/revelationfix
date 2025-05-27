package com.mega.revelationfix.mixin.gr;

import com.mega.revelationfix.common.init.GRItems;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import z1gned.goetyrevelation.item.ModItems;

@Mixin(ModItems.class)
public class ModItemsMixin {
    @Shadow(remap = false)
    @Final
    public static DeferredRegister<Item> ITEMS;

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/registries/DeferredRegister;register(Ljava/lang/String;Ljava/util/function/Supplier;)Lnet/minecraftforge/registries/RegistryObject;", ordinal = 4, remap = false), index = 0)
    private static String modifyRevelationRegistryName(String name) {
        return "revelation";
    }

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void registerItems(CallbackInfo ci) {
        GRItems.init(ITEMS);
    }
}
