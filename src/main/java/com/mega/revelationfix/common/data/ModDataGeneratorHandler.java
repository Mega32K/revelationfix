package com.mega.revelationfix.common.data;

import com.mega.revelationfix.Revelationfix;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Revelationfix.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModDataGeneratorHandler {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        ExistingFileHelper efh = event.getExistingFileHelper();
        var lp = event.getLookupProvider();
        event.getGenerator().addProvider(event.includeServer(), (DatapackBuiltinEntriesProvider.Factory<ModWorldGen>) pOutput -> new ModWorldGen(pOutput, lp));
    }
}
