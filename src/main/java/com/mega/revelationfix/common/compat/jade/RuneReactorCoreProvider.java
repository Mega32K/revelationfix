package com.mega.revelationfix.common.compat.jade;

import com.mega.revelationfix.common.block.ICoreInlaidBlock;
import com.mega.revelationfix.common.block.RuneReactorBlock;
import com.mega.revelationfix.common.block.RunestoneEngravedTableBlock;
import com.mega.revelationfix.common.block.blockentity.RuneReactorBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.checkerframework.checker.units.qual.C;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import z1gned.goetyrevelation.ModMain;

import java.util.UUID;

public enum RuneReactorCoreProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;
    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        CompoundTag compoundTag = blockAccessor.getServerData();
        if (compoundTag.hasUUID("Owner")) {
            iTooltip.add(Component.translatable("tooltip.goety.blockOwner").withStyle(ChatFormatting.GRAY).append(getOwnerName(blockAccessor, compoundTag.getUUID("Owner"))));
        } else if (blockAccessor.getBlockState() != null && blockAccessor.getLevel().getBlockEntity(blockAccessor.getPosition()) instanceof RuneReactorBlockEntity blockEntity) {
            iTooltip.add(Component.translatable("tooltip.goety.blockOwner").withStyle(ChatFormatting.GRAY).append(getOwnerName(blockAccessor, blockEntity)));
        }
        if (compoundTag.contains("CoreName")) {
            iTooltip.add(Component.translatable("tooltip.goety_revelation.jade.core", compoundTag.getString("CoreName")));
        } else if (blockAccessor.getBlockState() != null && blockAccessor.getLevel().getBlockEntity(blockAccessor.getPosition()) instanceof RuneReactorBlockEntity blockEntity) {
            BlockState state = blockAccessor.getBlockState();
            iTooltip.add(
                    Component.translatable("tooltip.goety_revelation.jade.core", Component.translatable(blockEntity.getInsertItem().getItem().getDescriptionId()).getString())
                            .append(blockEntity.getInsertItem().isEmpty() ? Component.literal("") : (RuneReactorBlock.isKindOfCore(blockEntity.getInsertItem()) ? Component.translatable("tooltip.goety_revelation.jade.available") : Component.translatable("tooltip.goety_revelation.jade.not_available")))
            );

        }
        if (compoundTag.contains("StructureIntegrity")) {
            iTooltip.add(Component.translatable("tooltip.goety_revelation.jade.rune_reactor_structure", compoundTag.getInt("StructureIntegrity"), 4));
        } else if (blockAccessor.getBlockState() != null && blockAccessor.getLevel().getBlockEntity(blockAccessor.getPosition()) instanceof RuneReactorBlockEntity blockEntity) {
            BlockState state = blockAccessor.getBlockState();
            iTooltip.add(Component.translatable("tooltip.goety_revelation.jade.rune_reactor_structure", RuneReactorBlock.structuralIntegrity(blockAccessor.getLevel(), blockAccessor.getPosition(), blockAccessor.getBlockState()), 4));
        }
    }
    public static Component getOwnerName(BlockAccessor blockAccessor, UUID uuid) {
        Player player;
        if (uuid == null)
            return Component.literal("<null>").withStyle(ChatFormatting.RED);
        else if ((player = blockAccessor.getLevel().getPlayerByUUID(uuid)) != null)
            return Component.literal(player.getDisplayName().getString());
        else return Component.literal("<empty>").withStyle(ChatFormatting.RED);
    }
    public static Component getOwnerName(BlockAccessor blockAccessor, RuneReactorBlockEntity blockEntity) {
        return getOwnerName(blockAccessor, blockEntity.getOwnerID());
    }
    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        if (blockAccessor != null && blockAccessor.getBlockState() != null && blockAccessor.getLevel().getBlockEntity(blockAccessor.getPosition()) instanceof RuneReactorBlockEntity blockEntity) {
            BlockState state = blockAccessor.getBlockState();
            if (blockEntity.getOwnerID() != null)
                compoundTag.putUUID("Owner", blockEntity.getOwnerID());
            compoundTag.putString("CoreName",
                    Component.translatable(blockEntity.getInsertItem().getItem().getDescriptionId()).getString() +
                            (blockEntity.getInsertItem().isEmpty() ? "" : (RuneReactorBlock.isKindOfCore(blockEntity.getInsertItem()) ? Component.translatable("tooltip.goety_revelation.jade.available").getString() : Component.translatable("tooltip.goety_revelation.jade.not_available").getString()))
            );
            compoundTag.putInt("StructureIntegrity", RuneReactorBlock.structuralIntegrity(blockAccessor.getLevel(), blockAccessor.getPosition(), blockAccessor.getBlockState()));

        }
    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation(ModMain.MODID, "rune_reactor_id");
    }
}
