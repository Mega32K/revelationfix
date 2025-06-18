package com.mega.revelationfix.mixin;

import com.mega.revelationfix.api.item.IInvulnerableItem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
    @Shadow(remap = false)
    public int lifespan;
    @Shadow
    private int health;

    @Shadow
    public abstract ItemStack getItem();

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        if (revelationfix$isInvulItem()) {
            health++;
            lifespan = Integer.MAX_VALUE;
        }
    }

    @Unique
    private boolean revelationfix$isInvulItem() {
        return this.getItem().getItem() instanceof IInvulnerableItem;
    }
}
