package com.mega.revelationfix.safe.level;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

public class ServerLevelExpandedContext extends LevelExpandedContext {
    public ServerLevelExpandedContext(Level level) {
        super(level);
    }

    private ServerExpandedContext serverEC() {
        return ((ServerEC) ((ServerLevel) level).getServer()).uom$serverECData();
    }

    @Override
    public void tickHead(BooleanSupplier booleanSupplier, CallbackInfo ci) {
    }
}
