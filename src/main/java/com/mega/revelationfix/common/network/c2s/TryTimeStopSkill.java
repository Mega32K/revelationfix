package com.mega.revelationfix.common.network.c2s;

import com.mega.revelationfix.common.apollyon.common.CooldownsManager;
import com.mega.revelationfix.common.compat.SafeClass;
import com.mega.revelationfix.common.config.ItemConfig;
import com.mega.revelationfix.common.init.GRItems;
import com.mega.revelationfix.util.entity.ATAHelper2;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class TryTimeStopSkill {
    protected final int user;

    public TryTimeStopSkill(int user) {
        this.user = user;
    }

    public static TryTimeStopSkill decode(FriendlyByteBuf friendlyByteBuf) {
        return new TryTimeStopSkill(friendlyByteBuf.readInt());
    }

    public static void encode(TryTimeStopSkill packet, FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeInt(packet.user);
    }

    public static void handle(TryTimeStopSkill packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (packet != null)
                handle0(packet, context);
        });
        context.get().setPacketHandled(true);
    }

    static void handle0(TryTimeStopSkill packet, Supplier<NetworkEvent.Context> context) {
        if (context.get().getSender() == null)
            return;
        ServerPlayer sp = context.get().getSender();
        if (sp == null) return;
        try {
            ServerLevel serverLevel = sp.serverLevel();
            Entity entity = serverLevel.getEntity(packet.user);
            if (entity instanceof ServerPlayer serverPlayer) {
                if (!ATAHelper2.hasOdamane(serverPlayer) && !ATAHelper2.hasEternalWatch(serverPlayer))
                    return;
                if (SafeClass.isTimeStop(serverLevel)) {
                    SafeClass.enableTimeStop(serverPlayer, false);
                    CooldownsManager.setItemCooldowns(serverPlayer, GRItems.HALO_OF_THE_END, (int) (ItemConfig.eternalWatchCooldown * 20 * 1.666F));
                    CooldownsManager.setItemCooldowns(serverPlayer, GRItems.ETERNAL_WATCH.get(), ItemConfig.eternalWatchCooldown * 20);
                } else {
                    if (serverPlayer.isCreative()) {
                        SafeClass.enableTimeStop(serverPlayer, true, 300);
                    } else if (ATAHelper2.hasOdamane(serverPlayer) && !serverPlayer.getCooldowns().isOnCooldown(GRItems.HALO_OF_THE_END)) {
                        SafeClass.enableTimeStop(serverPlayer, true, 300);
                        CooldownsManager.setItemCooldowns(serverPlayer, GRItems.HALO_OF_THE_END, 1400);
                    } else if (ATAHelper2.hasEternalWatch(serverPlayer) && !serverPlayer.getCooldowns().isOnCooldown(GRItems.ETERNAL_WATCH.get())) {
                        SafeClass.enableTimeStop(serverPlayer, true, ItemConfig.eternalWatchFreezingTime * 20);
                        CooldownsManager.setItemCooldowns(serverPlayer, GRItems.ETERNAL_WATCH.get(), ItemConfig.eternalWatchCooldown * 20);
                    }
                }
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
