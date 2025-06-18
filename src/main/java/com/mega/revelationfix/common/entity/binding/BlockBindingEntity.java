package com.mega.revelationfix.common.entity.binding;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public interface BlockBindingEntity {
    BlockPos getOwnerBlock();
    /**
     * @param flag <br>0 : BEFORE <br>1 : AFTER <br>2 : COUNT_4
     */
    void blockOwnerCheckEvent(Level level, BlockPos pos, BlockState blockState, @Nullable BlockEntity blockEntity, int flag);
    /**
     * @param flag <br>0 : NORMAL_REMOVE
     */
    void blockOwnerRemoveEvent(Level level, BlockPos pos, BlockState blockState, @Nullable BlockEntity blockEntity, int flag);

    /**
     * @param flag <br>0 : BEFORE <br>1 : AFTER
     */
    void blockOwnerTickEvent(Level level, BlockPos pos, BlockState blockState, @Nullable BlockEntity blockEntity, int flag);
}
