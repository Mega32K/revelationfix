package com.mega.revelationfix.server;

import com.google.common.collect.Queues;
import com.mega.endinglib.api.server.ServerTaskInstance;
import com.mega.endinglib.util.java.Args;
import com.mega.revelationfix.mixin.ServerPlayerGameModeAccessor;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Queue;

public class TreeKillerServerTask extends ServerTaskInstance {
    public static final Vec3i[] LOGS_FOUND_DELTA = new Vec3i[] {
            new Vec3i(1, 0, 1),
            new Vec3i(1, 0, 0),
            new Vec3i(1, 0, -1),
            new Vec3i(0, 0, 1),
            new Vec3i(0, 0, -1),
            new Vec3i(-1, 0, 1),
            new Vec3i(-1, 0, 0),
            new Vec3i(-1, 0, -1),


            new Vec3i(1, 1, 1),
            new Vec3i(1, 1, 0),
            new Vec3i(1, 1, -1),
            new Vec3i(0, 1, 1),
            new Vec3i(0, 1, 0),
            new Vec3i(0, 1, -1),
            new Vec3i(-1, 1, 1),
            new Vec3i(-1, 1, 0),
            new Vec3i(-1, 1, -1),
    };
    final int maxPhase = 32;
    public Queue<BlockPos> nextTickToBreak = Queues.newArrayDeque();
    int phase = 0;
    int brokeCount = 0;
    ItemStack eerieAxe = ItemStack.EMPTY;

    public TreeKillerServerTask(Args args) {
        super(args);
    }

    @Override
    public void update(Args args) {
        if (phase < maxPhase)
            phase++;
        Player player = this.getUser();
        BlockPos origin = this.getOriginBlockPos();
        if (eerieAxe.isEmpty())
            eerieAxe = player.getMainHandItem();
        if (phase % 2 == 0) {
            if (player instanceof ServerPlayer serverPlayer && origin != null && player.isAlive() && player.level() instanceof ServerLevel level) {
                if (phase == 2) {
                    nextTickToBreak.addAll(findLogs(origin, level));
                }
                if (!nextTickToBreak.isEmpty()) {
                    List<BlockPos> toAddList = new ObjectArrayList<>();
                    BlockPos pos;
                    while ((pos = nextTickToBreak.poll()) != null) {
                        if (mineLog(level, level.getBlockState(pos), pos, serverPlayer, eerieAxe)) {
                            brokeCount++;
                            toAddList.add(pos);
                        }
                    }
                    if (!toAddList.isEmpty()) {
                        for (BlockPos nextToAdd : toAddList) {
                            nextTickToBreak.addAll(findLogs(nextToAdd, level));
                        }
                    }
                }
            } else this.setRemoved(true);
        }
        if (phase >= maxPhase)
            this.setRemoved(true);
    }

    public Player getUser() {
        return this.getArgs().get(0);
    }

    public BlockPos getOriginBlockPos() {
        return this.getArgs().get(1);
    }

    public List<BlockPos> findLogs(BlockPos origin, Level level) {
        List<BlockPos> blockPos = new ObjectArrayList<>(18);
        for (Vec3i delta : LOGS_FOUND_DELTA) {
            BlockPos blockPos1 = origin.offset(delta);
            BlockState state = level.getBlockState(blockPos1);
            if (state.is(BlockTags.LOGS))
                blockPos.add(blockPos1);
        }
        return blockPos;
    }

    public boolean mineLog(ServerLevel level, BlockState blockstate, BlockPos blockPos, ServerPlayer player, ItemStack itemStack) {
        if (blockstate.isAir()) return false;
        ItemStack itemstack1 = itemStack.copy();
        Item item = itemStack.getItem();
        BlockEntity blockentity = level.getBlockEntity(blockPos);
        Block block = blockstate.getBlock();
        ServerPlayerGameMode playerGameMode = player.gameMode;
        boolean flag1 = blockstate.canHarvestBlock(level, blockPos, player); // previously player.hasCorrectToolForDrops(blockstate)
        {
            if (!level.isClientSide && blockstate.getDestroySpeed(level, blockPos) != 0.0F) {
                if (itemStack.getMaxDamage() - itemStack.getDamageValue() < 2)
                    return false;
                itemStack.hurtAndBreak(1, player, (p_40992_) -> {
                    p_40992_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
                });
            }
            player.awardStat(Stats.ITEM_USED.get(item));
        }
        if (itemStack.isEmpty() && !itemstack1.isEmpty())
            net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(player, itemstack1, InteractionHand.MAIN_HAND);
        boolean flag = ((ServerPlayerGameModeAccessor) playerGameMode).invokeRemoveBlock(blockPos, flag1);

        if (flag && flag1) {
            block.playerDestroy(level, player, blockPos, blockstate, blockentity, itemstack1);
        }
        if (brokeCount < 64)
            level.levelEvent(232424314, blockPos, 8);
        int exp = net.minecraftforge.common.ForgeHooks.onBlockBreakEvent(level, playerGameMode.getGameModeForPlayer(), player, blockPos);

        if (flag && exp > 0)
            blockstate.getBlock().popExperience(level, blockPos, exp);
        return true;
    }

    @Override
    public void onRemove() {
        this.brokeCount = 0;
        this.eerieAxe = ItemStack.EMPTY;
    }
}
