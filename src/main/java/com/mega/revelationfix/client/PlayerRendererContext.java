package com.mega.revelationfix.client;

import com.mega.revelationfix.common.odamane.client.OdamaneHaloModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class PlayerRendererContext {
    public OdamaneHaloModel<AbstractClientPlayer> ODAMANE_HALO_MODEL;

    public void init(EntityRendererProvider.Context context) {
        ODAMANE_HALO_MODEL = new OdamaneHaloModel<>(context.bakeLayer(OdamaneHaloModel.LAYER_LOCATION), false);
    }
}
