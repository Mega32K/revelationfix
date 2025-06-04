package com.mega.revelationfix.mixin.fantasy_ending.time;

import com.mega.revelationfix.safe.level.LevelEC;
import com.mega.revelationfix.safe.level.LevelExpandedContext;
import com.mega.revelationfix.safe.mixinpart.NoModDependsMixin;
import com.mega.revelationfix.util.time.TimeStopEntityData;
import com.mega.revelationfix.util.time.TimeStopUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
@NoModDependsMixin("fantasy_ending")
public abstract class EntityMixin {
    @Shadow
    private Level level;

    @Shadow
    public abstract boolean equals(Object p_20245_);

    @Shadow
    public abstract Level level();

    @Shadow
    protected abstract Vec3 collide(Vec3 p_20273_);

    @Inject(method = "setRemoved", at = @At("HEAD"))
    private void setRemoved(Entity.RemovalReason p_146876_, CallbackInfo ci) {
        LevelExpandedContext context = ((LevelEC) level).uom$levelECData();
        if (TimeStopUtils.isTimeStop) {
            if (((Object) this) instanceof LivingEntity living) {
                if (TimeStopEntityData.getTimeStopCount(living) > 0 && !level().isClientSide) {
                    ServerLevel serverLevel = (ServerLevel) level;

                    //实体意外消失的时候进行时停解除
                    TimeStopUtils.use(false, living);
                    if (serverLevel.players().isEmpty())
                        TimeStopUtils.isTimeStop = true;
                }
            }
        }
    }
}
