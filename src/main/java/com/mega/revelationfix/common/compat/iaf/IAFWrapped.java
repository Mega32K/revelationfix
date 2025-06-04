package com.mega.revelationfix.common.compat.iaf;

import com.Polarice3.Goety.common.entities.boss.Apostle;
import com.mega.revelationfix.common.compat.SafeClass;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.registries.ForgeRegistries;
import z1gned.goetyrevelation.util.ApollyonAbilityHelper;

import java.util.Objects;

public class IAFWrapped {
    public static Item CHAIN;
    public static Item CHAIN_STICKY;

    static {
        if (SafeClass.isIAFLoaded()) {
            CHAIN = ForgeRegistries.ITEMS.getValue(new ResourceLocation("iceandfire", "chain"));
            CHAIN_STICKY = ForgeRegistries.ITEMS.getValue(new ResourceLocation("iceandfire", "chain_sticky"));
        }
    }

    public static void e1(PlayerInteractEvent.EntityInteract event, Player player) {
        if (event.getItemStack().is(CHAIN) || event.getItemStack().is(CHAIN_STICKY)) {
            event.setCanceled(true);
            if (player.level.isClientSide)
                player.sendSystemMessage(Component.translatable("message.apollyon.ban_chain").withStyle(Objects.requireNonNull(ChatFormatting.getByCode('q'))));
        }
    }

    public static void e2(PlayerInteractEvent.RightClickBlock event, Player player) {
        if (player.level.getEntitiesOfClass(Apostle.class, player.getBoundingBox().inflate(64.0D)).stream().anyMatch((e) -> ((ApollyonAbilityHelper) e).allTitlesApostle_1_20_1$isApollyon())) {
            if (event.getItemStack().is(CHAIN) || event.getItemStack().is(CHAIN_STICKY)) {
                event.setCanceled(true);
                if (player.level.isClientSide)
                    player.sendSystemMessage(Component.translatable("message.apollyon.ban_chain").withStyle(Objects.requireNonNull(ChatFormatting.getByCode('q'))));
            }
        }
    }
}
