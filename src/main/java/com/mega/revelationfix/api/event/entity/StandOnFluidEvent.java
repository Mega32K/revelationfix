package com.mega.revelationfix.api.event.entity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class StandOnFluidEvent extends LivingEvent {
    private final FluidState fluid;

    public StandOnFluidEvent(LivingEntity entity, FluidState fluid) {
        super(entity);
        this.fluid = fluid;
    }

    public FluidState getFluidState() {
        return fluid;
    }
}