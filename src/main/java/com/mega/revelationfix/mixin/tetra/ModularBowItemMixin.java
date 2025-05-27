package com.mega.revelationfix.mixin.tetra;

import com.mega.revelationfix.safe.ModDependsMixin;
import net.minecraft.world.entity.projectile.AbstractArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import se.mickelus.tetra.items.modular.impl.bow.ModularBowItem;

@Mixin(ModularBowItem.class)
@ModDependsMixin("tetra")
public class ModularBowItemMixin {
    @Redirect(method = "fireProjectile", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;setBaseDamage(D)V", ordinal = 0))
    private static void fireProjectile(AbstractArrow instance, double p_36782_) {
        instance.setBaseDamage(p_36782_);
    }
}
