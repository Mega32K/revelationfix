package com.mega.revelationfix.api.event.block;

import com.mega.revelationfix.common.block.blockentity.RuneReactorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Cancelable;

public abstract class RuneReactorChangeEvent extends BlockEvent {
    private final RuneReactorBlockEntity blockEntity;

    public RuneReactorChangeEvent(LevelAccessor level, BlockPos pos, BlockState state, RuneReactorBlockEntity blockEntity) {
        super(level, pos, state);
        this.blockEntity = blockEntity;
    }

    public RuneReactorBlockEntity getBlockEntity() {
        return blockEntity;
    }

    public static class CheckStructureEvent extends RuneReactorChangeEvent {
        public final Phase phase;

        public CheckStructureEvent(LevelAccessor level, BlockPos pos, BlockState state, RuneReactorBlockEntity blockEntity, Phase phase) {
            super(level, pos, state, blockEntity);
            this.phase = phase;
        }

        public enum Phase {
            BEFORE_MODIFY, AFTER_MODIFY, COUNT_4
        }
    }

    @Cancelable
    public static class ChangeSimpleCodeEvent extends RuneReactorChangeEvent {
        private final int originCode;
        private int currentCode;

        public ChangeSimpleCodeEvent(LevelAccessor level, BlockPos pos, BlockState state, RuneReactorBlockEntity blockEntity, int originCode, int currentCode) {
            super(level, pos, state, blockEntity);
            this.originCode = originCode;
            this.currentCode = currentCode;
        }

        public int getOriginCode() {
            return originCode;
        }

        public int getCurrentCode() {
            return currentCode;
        }

        public void setCurrentCode(int currentCode) {
            this.currentCode = currentCode;
        }

        @Override
        public boolean isCancelable() {
            return true;
        }
    }
}
