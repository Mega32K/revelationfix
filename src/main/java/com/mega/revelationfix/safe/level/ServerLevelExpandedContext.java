package com.mega.revelationfix.safe.level;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BooleanSupplier;

public class ServerLevelExpandedContext extends LevelExpandedContext {
    public ServerLevelExpandedContext(Level level) {
        super(level);
    }

    private ServerExpandedContext serverEC() {
        return ((ServerEC) ((ServerLevel) level).server).uom$serverECData();
    }

    @Override
    public void tickHead(BooleanSupplier booleanSupplier, CallbackInfo ci) {
    }
}
