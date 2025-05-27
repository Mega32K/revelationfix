package com.mega.revelationfix.mixin.special;

import com.mega.revelationfix.safe.LivingEventEC;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEvent.class)
public abstract class LivingEventMixin extends EntityEvent implements LivingEventEC {
    @Unique
    private boolean revelationfix$unCancelable;

    public LivingEventMixin(Entity entity) {
        super(entity);
    }

    @Override
    public boolean revelationfix$isHackedUnCancelable() {
        return revelationfix$unCancelable;
    }

    @Override
    public void revelationfix$hackedUnCancelable(boolean target) {
        revelationfix$unCancelable = target;
    }

    @Override
    public boolean revelationfix$isHackedOnlyAmountUp() {
        return false;
    }

    @Override
    public void revelationfix$hackedOnlyAmountUp(boolean target) {
    }

    @Override
    public void setCanceled(boolean cancel) {
        if (this.revelationfix$unCancelable)
            return;
        super.setCanceled(cancel);
    }
}
