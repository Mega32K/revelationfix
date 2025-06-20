package com.mega.revelationfix.client.renderer.entity;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.render.CultistRenderer;
import com.Polarice3.Goety.client.render.ModModelLayer;
import com.Polarice3.Goety.client.render.model.MaverickModel;
import com.Polarice3.Goety.client.render.model.VillagerArmorModel;
import com.mega.revelationfix.common.entity.cultists.MaverickServant;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class MaverickServantRenderer extends CultistRenderer<MaverickServant> {
    protected static final ResourceLocation TEXTURE = Goety.location("textures/entity/cultist/maverick.png");

    public MaverickServantRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new MaverickModel(renderManagerIn.bakeLayer(ModModelLayer.MAVERICK)), 0.5F);
        this.addLayer(new HumanoidArmorLayer(this, new VillagerArmorModel(renderManagerIn.bakeLayer(ModModelLayer.VILLAGER_ARMOR_INNER)), new VillagerArmorModel(renderManagerIn.bakeLayer(ModModelLayer.VILLAGER_ARMOR_OUTER)), renderManagerIn.getModelManager()));
        this.addLayer(new ItemInHandLayer(this, renderManagerIn.getItemInHandRenderer()));
    }

    public ResourceLocation getTextureLocation(MaverickServant entity) {
        return TEXTURE;
    }
}
