package com.mega.revelationfix.common.init;

import com.mega.revelationfix.common.block.blockentity.RuneReactorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

public interface RunestoneRitualExe {
    void run(ServerLevel serverLevel, BlockPos reactorPos, BlockState reactorState, RuneReactorBlockEntity runeReactorBlock);
}
