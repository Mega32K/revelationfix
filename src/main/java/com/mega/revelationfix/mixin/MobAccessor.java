package com.mega.revelationfix.mixin;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Mob.class)
public interface MobAccessor {
    @Accessor("lootTable")
    void setLootTable(ResourceLocation resourceLocation);
}
