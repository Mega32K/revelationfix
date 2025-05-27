package com.mega.revelationfix.common.data;

import com.mega.revelationfix.common.data.brew.BrewReloadListener;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import z1gned.goetyrevelation.ModMain;

@Mod.EventBusSubscriber(modid = ModMain.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ReloadListenerEvents {
    @SubscribeEvent
    public static void dataListener(AddReloadListenerEvent event){
        event.addListener(new BrewReloadListener());
    }
}
