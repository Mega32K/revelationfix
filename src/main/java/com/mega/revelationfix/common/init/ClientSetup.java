package com.mega.revelationfix.common.init;

import com.mega.revelationfix.common.entity.renderer.QuietusVirtualRenderer;
import com.mega.revelationfix.common.odamane.client.OdamaneHaloModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(QuietusVirtualRenderer.MODEL_LAYER_LOCATION, QuietusVirtualRenderer::createBodyLayer);
        event.registerLayerDefinition(OdamaneHaloModel.LAYER_LOCATION, OdamaneHaloModel::createBodyLayer);
    }
}
