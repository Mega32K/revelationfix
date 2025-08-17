package com.mega.revelationfix.client;

import com.mega.revelationfix.client.model.curio.ApollyonRobeModel;
import com.mega.revelationfix.client.renderer.curio.ApollyonRobeRenderer;
import com.mega.revelationfix.common.init.GRItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import z1gned.goetyrevelation.ModMain;

public class CuriosRenderer {
    public static String folderPath = "textures/models/curio/";

    public static ResourceLocation render(String textureName) {
        return new ResourceLocation(ModMain.MODID, folderPath + textureName);
    }

    public static void register() {
        CuriosRendererRegistry.register(GRItems.APOLLYON_ROBE.get(), () -> new ApollyonRobeRenderer(render("apollyon_robe_normal.png"), render("apollyon_robe_nether.png"), new ApollyonRobeModel(bakeLayer(ApollyonRobeModel.LAYER_LOCATION))));
    }

    public static ModelPart bakeLayer(ModelLayerLocation layerLocation) {
        return Minecraft.getInstance().getEntityModels().bakeLayer(layerLocation);
    }
}
