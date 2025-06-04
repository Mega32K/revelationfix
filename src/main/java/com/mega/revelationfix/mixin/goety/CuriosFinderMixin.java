package com.mega.revelationfix.mixin.goety;

import com.Polarice3.Goety.utils.CuriosFinder;
import com.mega.revelationfix.common.entity.FakeSpellerEntity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(CuriosFinder.class)
public class CuriosFinderMixin {
    @ModifyVariable(remap = false, method = "findCurio(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/ItemStack;", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private static LivingEntity spellerCorrect(LivingEntity src) {
        if (src instanceof FakeSpellerEntity spellerEntity && spellerEntity.getOwner() != null) {
            src = spellerEntity.getOwner();
        }
        return src;
    }
}
