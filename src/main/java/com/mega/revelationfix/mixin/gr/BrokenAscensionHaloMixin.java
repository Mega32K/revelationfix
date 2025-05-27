package com.mega.revelationfix.mixin.gr;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import z1gned.goetyrevelation.item.BrokenAscensionHalo;

@Mixin(BrokenAscensionHalo.class)
public class BrokenAscensionHaloMixin {
    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item$Properties;rarity(Lnet/minecraft/world/item/Rarity;)Lnet/minecraft/world/item/Item$Properties;"))
    private static Item.Properties init(Item.Properties instance, Rarity p_41498_) {
        return instance.rarity(p_41498_).fireResistant();
    }
}
