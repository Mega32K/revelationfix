package com.mega.revelationfix.common.command;

import com.mega.revelationfix.common.data.TimeStopSavedData;
import com.mega.revelationfix.common.init.GRItems;
import com.mega.revelationfix.common.init.ModAttributes;
import com.mega.revelationfix.common.item.curios.TheNeedleItem;
import com.mega.revelationfix.safe.TheEndRitualItemContext;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public class GoetyRevelationCommand {
    public static void register(CommandDispatcher<CommandSourceStack> pDispatcher, CommandBuildContext p_250122_, CallbackInfo ci) {
        pDispatcher.register(
                Commands.literal("goety").requires((r) -> r.hasPermission(2)).then(Commands.literal("revelation")
                        .then(Commands.literal("clearNeedle")
                                .then(Commands.argument("players", EntityArgument.players())
                                        .executes(ctx -> tryClearNeedles(ctx, EntityArgument.getPlayers(ctx, "players")))
                                )
                        )
                        .then(Commands.literal("solvePuzzle")
                                .then(Commands.argument("player", EntityArgument.player())
                                        .executes(ctx -> givePuzzles(ctx, EntityArgument.getPlayer(ctx, "player")))
                                )
                        )
                )
        );
    }


    @Unique
    private static int givePuzzles(CommandContext<CommandSourceStack> ctx, ServerPlayer player) {
        {
            Item puzzle = GRItems.PUZZLE_ITEM.get();
            Item fragment = GRItems.MYSTERY_FRAGMENT.get();
            Item vanillaItem = TheEndRitualItemContext.PUZZLE1;
            ItemStack is1 = new ItemStack(puzzle);
            is1.getOrCreateTag().putBoolean(GRItems.NBT_PUZZLES, true);
            player.getInventory().add(is1);
            ItemStack is2 = new ItemStack(fragment);
            is2.getOrCreateTag().putInt("fragment", 0);
            player.getInventory().add(is2);
            player.getInventory().add(new ItemStack(vanillaItem));
        }
        {
            Item puzzle = GRItems.PUZZLE_ITEM.get();
            Item fragment = GRItems.MYSTERY_FRAGMENT.get();
            Item vanillaItem = TheEndRitualItemContext.PUZZLE2;
            ItemStack is1 = new ItemStack(puzzle);
            is1.getOrCreateTag().putBoolean(GRItems.NBT_PUZZLES2, true);
            player.getInventory().add(is1);
            ItemStack is2 = new ItemStack(fragment);
            is2.getOrCreateTag().putInt("fragment", 1);
            player.getInventory().add(is2);
            player.getInventory().add(new ItemStack(vanillaItem));
        }
        {
            Item puzzle = GRItems.PUZZLE_ITEM.get();
            Item fragment = GRItems.MYSTERY_FRAGMENT.get();
            Item vanillaItem = TheEndRitualItemContext.PUZZLE3;
            ItemStack is1 = new ItemStack(puzzle);
            is1.getOrCreateTag().putBoolean(GRItems.NBT_PUZZLES3, true);
            player.getInventory().add(is1);
            ItemStack is2 = new ItemStack(fragment);
            is2.getOrCreateTag().putInt("fragment", 2);
            player.getInventory().add(is2);
            player.getInventory().add(new ItemStack(vanillaItem));
        }
        {
            Item puzzle = GRItems.PUZZLE_ITEM.get();
            Item fragment = GRItems.MYSTERY_FRAGMENT.get();
            Item vanillaItem = TheEndRitualItemContext.PUZZLE4;
            ItemStack is1 = new ItemStack(puzzle);
            is1.getOrCreateTag().putBoolean(GRItems.NBT_PUZZLES4, true);
            player.getInventory().add(is1);
            ItemStack is2 = new ItemStack(fragment);
            is2.getOrCreateTag().putInt("fragment", 3);
            player.getInventory().add(is2);
            player.getInventory().add(new ItemStack(vanillaItem));
        }
        {
            Item vanillaItem = TheEndRitualItemContext.THE_END_CRAFT;
            ItemStack stack = new ItemStack(vanillaItem);
            stack.getOrCreateTag().putBoolean(GRItems.NBT_CRAFTING, true);
            player.getInventory().add(stack);
        }
        return 1;
    }

    @Unique
    private static int tryClearNeedles(CommandContext<CommandSourceStack> ctx, Collection<ServerPlayer> players) {
        TimeStopSavedData data = TimeStopSavedData.readOrCreate(ctx.getSource().getServer());
        Map<UUID, TimeStopSavedData.SimplePlayerData> playerDataMap = data.playerDataMap;
        for (ServerPlayer entity : players) {
            if (playerDataMap.containsKey(entity.getUUID()))
                playerDataMap.get(entity.getUUID()).setNeedleTimes(0);
            entity.getAttribute(Attributes.MAX_HEALTH).removeModifier(TheNeedleItem.HEALTH.getId());
            entity.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(TheNeedleItem.ATTACK_DAMAGE.getId());
            entity.getAttribute(Attributes.ATTACK_SPEED).removeModifier(TheNeedleItem.ATTACK_SPEED.getId());
            entity.getAttribute(ModAttributes.DAMAGE_RESISTANCE.get()).removeModifier(TheNeedleItem.RESISTANCE.getId());
            entity.getAttributes().getDirtyAttributes().add(entity.getAttribute(Attributes.ATTACK_DAMAGE));
        }
        data.setDirty();

        if (players.size() == 1) {
            ctx.getSource().sendSuccess(() -> Component.translatable("commands.goety_revelation.needle_clear.success.single", players.iterator().next().getDisplayName()), true);
        } else {
            ctx.getSource().sendSuccess(() -> Component.translatable("commands.goety_revelation.needle_clear.success.multiple", players.size()), true);
        }
        return players.size();
    }
}
