package com.mega.revelationfix.common.ritual;

import com.Polarice3.Goety.api.ritual.IRitualType;
import com.Polarice3.Goety.common.blocks.entities.RitualBlockEntity;
import com.Polarice3.Goety.common.ritual.type.MagicRitualType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class TheEndMagicRitualType implements IRitualType {
    private final MagicRitualType magicRitualType = new MagicRitualType();
    @Override
    public String getName() {
        return ModRitualTypes.THE_END_MAGIC;
    }

    @Override
    public ItemStack getJeiIcon() {
        return magicRitualType.getJeiIcon();
    }

    @Override
    public boolean getRequirement(RitualBlockEntity pTileEntity, BlockPos pPos, Level pLevel) {
        return magicRitualType.getRequirement(pTileEntity, pPos, pLevel);
    }

    @Override
    public boolean getRequirement(RitualBlockEntity pTileEntity, @Nullable Player pPlayer, BlockPos pPos, Level pLevel) {
        return magicRitualType.getRequirement(pTileEntity, pPlayer, pPos, pLevel);
    }
}
