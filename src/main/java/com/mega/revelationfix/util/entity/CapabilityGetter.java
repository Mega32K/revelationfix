package com.mega.revelationfix.util.entity;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import java.util.function.Supplier;

public class CapabilityGetter {
    public static <T> T unsafeGetCapability(Capability<T> capability, Entity entity, Supplier<T> else_) {
        LazyOptional<T> optional = entity.getCapability(capability);
        return optional.orElse(else_.get());
    }
    public static  <T> T unsafeGetLazyOptional(LazyOptional<T> lazyOptional, Entity entity) {
        if (lazyOptional.isPresent())
            return lazyOptional.orElseThrow(NullPointerException::new);
        throw new NullPointerException("lazy optional error");
    }
}
