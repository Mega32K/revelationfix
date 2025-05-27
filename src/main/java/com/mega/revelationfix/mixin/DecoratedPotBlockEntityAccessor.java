package com.mega.revelationfix.mixin;

import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DecoratedPotBlockEntity.class)
public interface DecoratedPotBlockEntityAccessor {
    @Accessor("decorations")
    void setDecorations(DecoratedPotBlockEntity.Decorations i);
}
