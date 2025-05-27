package com.mega.revelationfix.common.network.co;

import com.mega.revelationfix.common.client.screen.TheEndDeathScreen;
import com.mega.revelationfix.common.network.c2s.TheEndDeathPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class TheEndDeathPacketWrapped {
    public static void handle0(TheEndDeathPacket packet, Supplier<NetworkEvent.Context> context) {
        try {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player.getId() != packet.getUser()) return;
            mc.setScreen(new TheEndDeathScreen(Component.empty(), true));
            mc.player.setPos(new Vec3(packet.getRespawn()));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
