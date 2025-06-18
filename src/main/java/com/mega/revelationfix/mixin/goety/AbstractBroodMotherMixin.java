package com.mega.revelationfix.mixin.goety;

import com.Polarice3.Goety.common.entities.ally.Summoned;
import com.Polarice3.Goety.common.entities.neutral.AbstractBroodMother;
import com.Polarice3.Goety.common.entities.neutral.Owned;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

/**
 * 添加掉落物
 */
@Mixin(AbstractBroodMother.class)
public abstract class AbstractBroodMotherMixin extends Summoned {
    AbstractBroodMotherMixin(EntityType<? extends Owned> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    public void dropAllDeathLoot(DamageSource p_21192_) {
        super.dropAllDeathLoot(p_21192_);
    }
}
