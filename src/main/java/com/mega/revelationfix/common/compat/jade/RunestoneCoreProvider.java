package com.mega.revelationfix.common.compat.jade;

import com.mega.revelationfix.common.block.ICoreInlaidBlock;
import com.mega.revelationfix.common.block.RunestoneEngravedTableBlock;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import z1gned.goetyrevelation.ModMain;

public enum RunestoneCoreProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (blockAccessor.getServerData().contains("CoreName")) {
            iTooltip.add(Component.translatable("tooltip.goety_revelation.jade.core", blockAccessor.getServerData().getString("CoreName")));
        } else if (blockAccessor.getBlockState() != null && blockAccessor.getBlock() instanceof ICoreInlaidBlock coreInlaidBlock) {
            BlockState state = blockAccessor.getBlockState();
            if (state.hasProperty(RunestoneEngravedTableBlock.CORE)) {
                iTooltip.add(Component.translatable("tooltip.goety_revelation.jade.core", Component.translatable(coreInlaidBlock.getCore(state.getValue(RunestoneEngravedTableBlock.CORE)).getDescriptionId()).getString()));
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        if (blockAccessor != null && blockAccessor.getBlockState() != null && blockAccessor.getBlock() instanceof ICoreInlaidBlock coreInlaidBlock) {
            BlockState state = blockAccessor.getBlockState();
            if (state.hasProperty(RunestoneEngravedTableBlock.CORE)) {
                compoundTag.putString("CoreName", Component.translatable(coreInlaidBlock.getCore(state.getValue(RunestoneEngravedTableBlock.CORE)).getDescriptionId()).getString());
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation(ModMain.MODID, "runestone_id");
    }
}
