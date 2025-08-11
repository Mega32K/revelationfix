package com.mega.revelationfix.proxy;

import com.mega.revelationfix.common.data.ritual.RitualDataManager;
import com.mega.revelationfix.common.network.PacketHandler;
import com.mega.revelationfix.common.network.s2c.data.RitualDataSyncPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.List;
public class ServerProxy implements ModProxy {
    public ServerProxy(FMLJavaModLoadingContext context) {
    }
}
