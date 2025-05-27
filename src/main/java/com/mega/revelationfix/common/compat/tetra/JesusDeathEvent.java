package com.mega.revelationfix.common.compat.tetra;

import com.Polarice3.Goety.init.ModSounds;
import com.mega.revelationfix.common.init.GRItems;
import com.mega.revelationfix.common.network.PacketHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import se.mickelus.tetra.items.modular.impl.ModularSingleHeadedItem;

import java.util.Locale;

public class JesusDeathEvent {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onJesusDeath(LivingDeathEvent event) {
        try {
            if (!event.isCanceled() && event.getEntity() instanceof Villager villager) {
                if (event.getSource().getEntity() instanceof Player player) {
                    ItemStack itemStack = player.getMainHandItem();
                    if (itemStack.getItem() instanceof ModularSingleHeadedItem singleHeadedItem && isJesus(villager)) {
                        if (itemStack.getOrCreateTag().getString(ModularSingleHeadedItem.headKey).equals("single/spearhead")) {
                            itemStack.hurtAndBreak(1000000, player, (l) -> {
                            });
                            itemStack.shrink(1);
                            if (player instanceof ServerPlayer serverPlayer)
                                PacketHandler.playSound(serverPlayer, ModSounds.BIOMINE_SPAWN.get(), SoundSource.PLAYERS, 2f, (float) player.random.triangle(1F, 0.1F));
                            ItemEntity itemEntity = player.spawnAtLocation(GRItems.TIP_OF_THE_SPEAR_OF_LONGINUS_ITEM.getDefaultInstance());
                            if (itemEntity != null) {
                                itemEntity.setGlowingTag(true);
                                itemEntity.setNoGravity(true);
                                itemEntity.setPickUpDelay(100);
                                itemEntity.hurtMarked = true;
                                itemEntity.setDeltaMovement(Vec3.ZERO);
                                itemEntity.move(MoverType.SELF, new Vec3(player.getLookAngle().x * 0.1F, 0.001F, player.getLookAngle().z * 0.1F));
                                itemEntity.setDeltaMovement(itemEntity.getDeltaMovement());
                                player.playSound(SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR, 3F, 0.4F);
                            }
                        }
                    }
                }
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @SubscribeEvent
    public static void nameTagEvent(LivingEvent.LivingTickEvent event) {
        if (event.getEntity() instanceof Villager villager) {
            if (!villager.level.isClientSide && !villager.isNoAi())
                if (isJesus(villager)) {
                    villager.playSound(SoundEvents.BEACON_DEACTIVATE, 4F, 0.3F);
                    villager.setXRot(20);
                    villager.setSilent(true);
                    villager.setGlowingTag(true);
                    villager.setNoAi(true);
                }
        }
    }

    static boolean isJesus(Villager villager) {
        return (villager.hasCustomName() && (villager.getDisplayName().getString().equals(Component.translatable("misc.goety_revelation.jesus").getString()) || villager.getDisplayName().getString().toLowerCase(Locale.ROOT).contains("jesus")));
    }
}
