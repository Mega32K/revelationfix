package com.mega.revelationfix.mixin;

import com.mega.endinglib.util.annotation.DeprecatedMixin;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemEntity.class)
@DeprecatedMixin
public abstract class ItemEntityMixin {
}
