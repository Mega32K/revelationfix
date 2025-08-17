package com.mega.revelationfix.common.event;

import com.Polarice3.Goety.common.blocks.entities.DarkAltarBlockEntity;
import com.Polarice3.Goety.common.crafting.RitualRecipe;
import com.mega.revelationfix.api.event.block.DarkAltarEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;

public class GoetyEventHooks {
    public static boolean postStopRitualEvent(LevelAccessor level, BlockPos pos, BlockState state, DarkAltarBlockEntity altarBlockEntity, boolean isBefore, boolean isFinished, DarkAltarEvent.StopRitualEvent.Reason reason) {
        return MinecraftForge.EVENT_BUS.post(new DarkAltarEvent.StopRitualEvent(level, pos, state, altarBlockEntity, isBefore, isFinished, reason));
    }

    public static boolean postDarkAltarBETickEvent(LevelAccessor level, BlockPos pos, BlockState state, DarkAltarBlockEntity altarBlockEntity, DarkAltarEvent.TickEvent.TickPhase phase) {
        return MinecraftForge.EVENT_BUS.post(new DarkAltarEvent.TickEvent(level, pos, state, altarBlockEntity, phase));
    }

    public static boolean postStartRitualEvent(LevelAccessor level, BlockPos pos, BlockState state, DarkAltarBlockEntity altarBlockEntity, Player castingPlayer, ItemStack activationItem, RitualRecipe ritualRecipe) {
        return MinecraftForge.EVENT_BUS.post(new DarkAltarEvent.StartRitualEvent(level, pos, state, altarBlockEntity, castingPlayer, activationItem, ritualRecipe));
    }
}
