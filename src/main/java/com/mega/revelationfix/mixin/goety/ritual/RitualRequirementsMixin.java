package com.mega.revelationfix.mixin.goety.ritual;

import com.Polarice3.Goety.common.blocks.entities.RitualBlockEntity;
import com.Polarice3.Goety.common.ritual.RitualRequirements;
import com.mega.revelationfix.common.data.ingrident.PuzzleIngredient;
import com.mega.revelationfix.common.data.ritual.RitualData;
import com.mega.revelationfix.common.data.ritual.RitualDataManager;
import com.mega.revelationfix.common.init.GRItems;
import com.mega.revelationfix.common.ritual.ModRitualTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EnchantmentTableBlock;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.atomic.AtomicBoolean;

@Mixin(value = RitualRequirements.class, remap = false)
public class RitualRequirementsMixin {
    @Shadow
    @Final
    public static int RANGE;

    @Inject(method = "getProperStructure", at = @At("HEAD"), cancellable = true)
    private static void getProperStructure(String craftType, RitualBlockEntity pTileEntity, BlockPos pPos, Level pLevel, CallbackInfoReturnable<Boolean> cir) {
        if (RitualDataManager.isCustomRitual(craftType)) {
            cir.setReturnValue(RitualDataManager.getProperStructure(craftType, pTileEntity, pPos, pLevel));
        } else if (craftType.equals(ModRitualTypes.THE_END)) {
            cir.setReturnValue(pPos.getY() <= 10 && revelationfix$getStructuresTheEnd(craftType, pPos, pLevel) && pLevel.dimension() == Level.END);
        } else if (craftType.equals(ModRitualTypes.THE_END_MAGIC)) {
            cir.setReturnValue(revelationfix$getStructuresTheEnd(craftType, pPos, pLevel));
        }
    }

    @Unique
    private static boolean revelationfix$getStructuresTheEnd(String craftType, BlockPos pPos, Level pLevel) {
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
        for (int i = -RANGE; i <= RANGE; ++i) {
            for (int j = -RANGE; j <= RANGE; ++j) {
                for (int k = -RANGE; k <= RANGE; ++k) {
                    BlockPos blockpos1 = pPos.offset(i, j, k);
                    BlockState blockstate = pLevel.getBlockState(blockpos1);
                    switch (craftType) {
                        case ModRitualTypes.THE_END -> {
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
                        case ModRitualTypes.THE_END_MAGIC -> {
                            totalFirst = 16;
                            totalSecond = 1;
                            totalThird = 1;
                            if (blockstate.getEnchantPowerBonus(pLevel, blockpos1) > 0) {
                                firstCount += (int) blockstate.getEnchantPowerBonus(pLevel, blockpos1);
                            }
                            if (blockstate.getBlock() instanceof LecternBlock) {
                                if (blockstate.hasBlockEntity() && pLevel.getBlockEntity(blockpos1) instanceof LecternBlockEntity lecternTileEntity) {
                                    if (!lecternTileEntity.getBook().isEmpty()) {
                                        ++secondCount;
                                    }
                                }
                            }
                            if (blockstate.getBlock() instanceof EnchantmentTableBlock) {
                                ++thirdCount;
                            }
                        }
                    }
                }
            }
        }
        if (craftType.equals(ModRitualTypes.THE_END)) {
            for (ItemFrame frame : pLevel.getEntitiesOfClass(ItemFrame.class, new AABB(pPos).inflate(RANGE))) {
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
        }
        return firstCount >= totalFirst
                && secondCount >= totalSecond
                && thirdCount >= totalThird
                && fourthCount >= totalFourth;
    }
}
