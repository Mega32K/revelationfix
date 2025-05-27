package com.mega.revelationfix.common.apollyon.client;

import com.Polarice3.Goety.common.entities.boss.Apostle;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;

public class SafePlayPostMusic {
    public static void playTotem(Apostle apostle, boolean revived) {
        if (!revived) {
            {
                Player player = apostle.level.getNearestPlayer(apostle, 128D);
                if (player != null) {
                    player.sendSystemMessage(Component.translatable("chat.type.advancement.goal", apostle.getDisplayName(), Component.literal("[" + Component.translatable("advancements.adventure.totem_of_undying.title").getString() + "]").withStyle(ChatFormatting.GREEN)));
                    player.playSound(SoundEvents.TOTEM_USE);
                }
            }

        }
    }
}
