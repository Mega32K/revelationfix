package com.mega.revelationfix.mixin.gr;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import z1gned.goetyrevelation.item.AscensionHalo;

@Mixin(AscensionHalo.class)
public abstract class AscensionHaloMixin extends Item {
    public AscensionHaloMixin(Properties p_41383_) {
        super(p_41383_);
    }

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item$Properties;rarity(Lnet/minecraft/world/item/Rarity;)Lnet/minecraft/world/item/Item$Properties;"))
    private static Properties init(Properties instance, Rarity p_41498_) {
        return instance.rarity(p_41498_).fireResistant();
    }

    @ModifyConstant(method = "appendHoverText", constant = @Constant(stringValue = "tooltip.goety_revelation.tip"))
    private String hasAlt(String constant) {
        return "tooltip.revelationfix.holdAltEffect";
    }

    @ModifyConstant(method = "appendHoverText", constant = @Constant(stringValue = "tooltip.goety_revelation.tip0"))
    private String hasShift(String constant) {
        return "tooltip.revelationfix.holdShift";
    }
}
