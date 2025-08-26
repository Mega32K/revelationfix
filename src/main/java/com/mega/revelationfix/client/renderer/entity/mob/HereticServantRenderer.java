package com.mega.revelationfix.client.renderer.entity.mob;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.render.ModModelLayer;
import com.Polarice3.Goety.client.render.model.HereticModel;
import com.mega.revelationfix.common.entity.cultists.HereticServant;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CrossedArmsItemLayer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.resources.ResourceLocation;

public class HereticServantRenderer extends MobRenderer<HereticServant, HereticModel<HereticServant>> {

    private static final ResourceLocation WITCH_LOCATION = Goety.location("textures/entity/cultist/heretic.png");

    public HereticServantRenderer(EntityRendererProvider.Context p_174443_) {
        super(p_174443_, new HereticModel(p_174443_.bakeLayer(ModModelLayer.HERETIC)), 0.5F);
        this.addLayer(new CrossedArmsItemLayer(this, p_174443_.getItemInHandRenderer()));
        this.addLayer(new CustomHeadLayer(this, p_174443_.getModelSet(), p_174443_.getItemInHandRenderer()));
    }

    public void render(HereticServant p_116412_, float p_116413_, float p_116414_, PoseStack p_116415_, MultiBufferSource p_116416_, int p_116417_) {
        /*
        PostEffectRegistry.renderEffectForNextTick(ClientProxy.HOLOGRAM_SHADER);
        for (PostPass postPass : PostEffectRegistry.getPostChainFor(ClientProxy.HOLOGRAM_SHADER).passes)
            if (postPass.getName().endsWith("rewind"))
                postPass.getEffect().safeGetUniform("GameTime").set((float) Blaze3D.getTime());
         */
        super.render(p_116412_, p_116413_, p_116414_, p_116415_, p_116416_, p_116417_);
        //Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
    }

    public ResourceLocation getTextureLocation(HereticServant p_116410_) {
        return WITCH_LOCATION;
    }

    //@Nullable
    //@Override
    //protected RenderType getRenderType(HereticServant p_115322_, boolean p_115323_, boolean p_115324_, boolean p_115325_) {
    // return GRRenderTypes.getHologram(this.getTextureLocation(p_115322_));
    //}

    protected void scale(HereticServant p_116419_, PoseStack p_116420_, float p_116421_) {
        float f = 0.9375F;
        p_116420_.scale(f, f, f);
    }
}
