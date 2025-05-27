package com.mega.revelationfix.safe;

import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

public class LevelExpandedContext {
    public final Level level;

    public LevelExpandedContext(Level level) {
        this.level = level;
    }

    public boolean isClient() {
        return level.isClientSide();
    }

    /**
     * @return true cancel tick()
     */
    public void tickHead(BooleanSupplier booleanSupplier, CallbackInfo ci) {
    }
}
