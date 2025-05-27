package com.mega.revelationfix.mixin;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.concurrent.locks.ReadWriteLock;

@Mixin(SynchedEntityData.class)
public interface SyncEntityDataAccessor {
    @Accessor("entity")
    Entity caller();

    @Accessor("itemsById")
    Int2ObjectMap<SynchedEntityData.DataItem<?>> itemsById();

    @Accessor("lock")
    ReadWriteLock lock();

    @Accessor("isDirty")
    boolean isDirty();

    @Accessor("isDirty")
    void setIsDirty(boolean dirty);
}
