package com.mega.revelationfix.mixin.goety;

import com.Polarice3.Goety.common.entities.hostile.servants.ObsidianMonolith;
import com.Polarice3.Goety.common.entities.neutral.AbstractMonolith;
import com.Polarice3.Goety.common.entities.neutral.Owned;
import com.mega.revelationfix.safe.OdamanePlayerExpandedContext;
import com.mega.revelationfix.util.ATAHelper2;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 终末黑曜石巨柱添加/移除列表<br>
 */
@Mixin(ObsidianMonolith.class)
public abstract class ObsidianMonolithMixin extends AbstractMonolith {

    public ObsidianMonolithMixin(EntityType<? extends Owned> type, Level worldIn) {
        super(type, worldIn);
    }

    @Inject(method = "aiStep", at = @At("HEAD"))
    private void aiStep(CallbackInfo ci) {
        if (!level.isClientSide) {
            if (this.getTrueOwner() instanceof Player player) {
                OdamanePlayerExpandedContext expandedContext = ATAHelper2.getOdamaneEC(player);
                if (!expandedContext.getOwnedMonoliths().contains((ObsidianMonolith) (Object) this)) {
                    expandedContext.getOwnedMonoliths().add((ObsidianMonolith) (Object) this);
                }
            }
        }
    }
}
