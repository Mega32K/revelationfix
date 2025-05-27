package com.mega.revelationfix.mixin;

import net.minecraft.network.syncher.SynchedEntityData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SynchedEntityData.DataItem.class)
public interface BypassesDataItemAccessor<T> {
    @Accessor("value")
    T getValue();

    @Accessor("value")
    void setValue(T p_135398_);

    @Accessor("dirty")
    boolean isDirty();

    @Accessor("dirty")
    void setDirty(boolean p_135402_);
}
