package com.mega.revelationfix.mixin;

import com.mega.endinglib.util.annotation.DeprecatedMixin;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Inventory.class)
@DeprecatedMixin
public class InventoryMixin {
}
