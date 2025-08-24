package com.mega.revelationfix.util.entity;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class CapabilityGetter {
    public static <T> T unsafeGetCapability(Capability<T> capability, Entity entity) {
        LazyOptional<T> optional = entity.getCapability(capability);
        if (optional.isPresent())
            return optional.orElseThrow(NullPointerException::new);
        throw new NullPointerException("lazy optional error");
    }
    public static  <T> T unsafeGetLazyOptional(LazyOptional<T> lazyOptional, Entity entity) {
        if (lazyOptional.isPresent())
            return lazyOptional.orElseThrow(NullPointerException::new);
        throw new NullPointerException("lazy optional error");
    }
}
