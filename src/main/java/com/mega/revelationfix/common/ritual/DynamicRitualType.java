package com.mega.revelationfix.common.ritual;

import com.Polarice3.Goety.api.ritual.IRitualType;
import com.Polarice3.Goety.common.blocks.entities.DarkAltarBlockEntity;
import com.Polarice3.Goety.common.blocks.entities.RitualBlockEntity;
import com.mega.revelationfix.common.data.ritual.RitualData;
import com.mega.revelationfix.common.data.ritual.RitualDataManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public record DynamicRitualType(RitualData ritualData) implements IRitualType {

    @Override
    public String getName() {
        return ritualData.getPluginName();
    }

    @Override
    public ItemStack getJeiIcon() {
        return ritualData.getIconItem();
    }

    @Override
    public boolean getRequirement(RitualBlockEntity pTileEntity, BlockPos pPos, Level pLevel) {
        return RitualDataManager.getProperStructure(ritualData, pTileEntity, pPos, pLevel);
    }

    @Override
    public boolean getRequirement(RitualBlockEntity pTileEntity, @Nullable Player pPlayer, BlockPos pPos, Level pLevel) {
        return RitualDataManager.getProperStructure(ritualData, pTileEntity, pPos, pLevel);
    }

    @Override
    public void onFinishRitual(Level world, BlockPos darkAltarPos, DarkAltarBlockEntity tileEntity, Player castingPlayer, ItemStack activationItem) {
        IRitualType.super.onFinishRitual(world, darkAltarPos, tileEntity, castingPlayer, activationItem);
    }
}
