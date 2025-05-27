package com.mega.revelationfix.mixin.fantasy_ending.time.time;

import com.mega.revelationfix.safe.LevelEC;
import com.mega.revelationfix.safe.LevelExpandedContext;
import com.mega.revelationfix.safe.NoModDependsMixin;
import com.mega.revelationfix.util.time.TimeStopUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(Level.class)
@NoModDependsMixin("fantasy_ending")
public abstract class LevelMixin implements LevelEC {
    @Inject(method = "guardEntityTick", at = @At("HEAD"), cancellable = true)
    private <T extends Entity> void guardEntityTick(Consumer<T> consumer, T entity, CallbackInfo ci) {
        if (TimeStopUtils.isTimeStop && TimeStopUtils.andSameDimension((Level) (Object) this)) {
            if (entity == null)
                ci.cancel();
            else {
                if (!TimeStopUtils.canMove(entity))
                    ci.cancel();
            }
        }
    }

    @Override
    public LevelExpandedContext uom$levelECData() {
        return null;
    }

    @Override
    public void uom$setECData(LevelExpandedContext data) {
    }
}
