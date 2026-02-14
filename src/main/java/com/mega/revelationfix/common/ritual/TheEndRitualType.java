package com.mega.revelationfix.common.ritual;

import com.Polarice3.Goety.api.ritual.IRitualType;
import com.Polarice3.Goety.common.blocks.entities.DarkAltarBlockEntity;
import com.Polarice3.Goety.common.blocks.entities.RitualBlockEntity;
import com.Polarice3.Goety.common.ritual.RitualRequirements;
import com.mega.revelationfix.common.data.ingrident.PuzzleIngredient;
import com.mega.revelationfix.common.init.GRItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EnchantmentTableBlock;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;

public class TheEndRitualType implements IRitualType {
    @Override
    public String getName() {
        return ModRitualTypes.THE_END;
    }

    @Override
    public ItemStack getJeiIcon() {
        return GRItems.DIMENSIONAL_WILL.get().getDefaultInstance();
    }

    @Override
    public boolean getRequirement(RitualBlockEntity pTileEntity, BlockPos pPos, Level pLevel) {
        int firstCount = 0;
        int secondCount = 0;
        int thirdCount = 0;
        int fourthCount = 0;

        int totalFirst = 0;
        int totalSecond = 0;
        int totalThird = 0;
        int totalFourth = 0;
        AtomicBoolean puzzle1 = new AtomicBoolean(false);
        AtomicBoolean puzzle2 = new AtomicBoolean(false);
        AtomicBoolean puzzle3 = new AtomicBoolean(false);
        AtomicBoolean puzzle4 = new AtomicBoolean(false);
        for (int i = -RitualRequirements.RANGE; i <= RitualRequirements.RANGE; ++i) {
            for (int j = -RitualRequirements.RANGE; j <= RitualRequirements.RANGE; ++j) {
                for (int k = -RitualRequirements.RANGE; k <= RitualRequirements.RANGE; ++k) {
                    BlockPos blockpos1 = pPos.offset(i, j, k);
                    BlockState blockstate = pLevel.getBlockState(blockpos1);
                    if (pPos.getY() > 10 || pLevel.dimension() != Level.END) return false;
                    totalFirst = 4;
                    totalSecond = 8;
                    totalThird = 16;
                    totalFourth = 4;
                    if (blockstate.is(Blocks.END_ROD)) {
                        ++firstCount;
                    }
                    if (blockstate.is(Blocks.PURPUR_BLOCK)) {
                        ++secondCount;
                    }
                    if (blockstate.is(Blocks.END_STONE_BRICKS)) {
                        ++thirdCount;
                    }
                }
            }
        }
        for (ItemFrame frame : pLevel.getEntitiesOfClass(ItemFrame.class, new AABB(pPos).inflate(RitualRequirements.RANGE))) {
            if (!puzzle1.get() && PuzzleIngredient.puzzle(0).test(frame.getItem())) {
                fourthCount++;
                puzzle1.set(true);
            }
            if (!puzzle2.get() && PuzzleIngredient.puzzle(1).test(frame.getItem())) {
                fourthCount++;
                puzzle2.set(true);
            }
            if (!puzzle3.get() && PuzzleIngredient.puzzle(2).test(frame.getItem())) {
                fourthCount++;
                puzzle3.set(true);
            }
            if (!puzzle4.get() && PuzzleIngredient.puzzle(3).test(frame.getItem())) {
                fourthCount++;
                puzzle4.set(true);
            }
        }
        return firstCount >= totalFirst
                && secondCount >= totalSecond
                && thirdCount >= totalThird
                && fourthCount >= totalFourth;
    }

    @Override
    public boolean getRequirement(RitualBlockEntity pTileEntity, @Nullable Player pPlayer, BlockPos pPos, Level pLevel) {
        return this.getRequirement(pTileEntity, pPos, pLevel);
    }
}
