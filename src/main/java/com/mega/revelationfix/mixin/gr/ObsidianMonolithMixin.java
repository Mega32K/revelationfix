package com.mega.revelationfix.mixin.gr;

import com.Polarice3.Goety.common.entities.hostile.servants.ObsidianMonolith;
import com.Polarice3.Goety.common.entities.neutral.AbstractMonolith;
import com.Polarice3.Goety.common.entities.neutral.AbstractObsidianMonolith;
import com.Polarice3.Goety.common.entities.neutral.Owned;
import com.mega.endinglib.util.annotation.DeprecatedMixin;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import z1gned.goetyrevelation.util.ApollyonAbilityHelper;

@Mixin(ObsidianMonolith.class)
@DeprecatedMixin
public abstract class ObsidianMonolithMixin extends AbstractObsidianMonolith implements ApollyonAbilityHelper {

    public ObsidianMonolithMixin(EntityType<? extends AbstractMonolith> type, Level worldIn) {
        super(type, worldIn);
    }
}
