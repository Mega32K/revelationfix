package com.mega.revelationfix.client.model.entity;

import com.Polarice3.Goety.client.render.model.WitchHatModel;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import z1gned.goetyrevelation.ModMain;

public class SpectreDarkmageHatModel extends HumanoidModel<LivingEntity> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(ModMain.MODID, "spectre_darkmage_hat"), "main");
    public final ModelPart hat;
    public final ModelPart top;
    public final ModelPart top_top;
    public final ModelPart hat4;

    public static LayerDefinition createHatLayer() {
        MeshDefinition meshdefinition = HumanoidModel.createMesh(LayerDefinitions.OUTER_ARMOR_DEFORMATION, 0F);
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition hat2 = partdefinition.addOrReplaceChild("hat2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bottom = hat2.addOrReplaceChild("bottom", CubeListBuilder.create().texOffs(0, 40).addBox(-7.0F, -8.05F, -7.0F, 14.0F, 1.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition top = hat2.addOrReplaceChild("top", CubeListBuilder.create().texOffs(4, 16).addBox(-2.5368F, -10.8879F, -2.5669F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0524F, 0.0F, 0.0262F));

        PartDefinition core = top.addOrReplaceChild("core", CubeListBuilder.create().texOffs(0, 33).addBox(-0.9368F, -12.3879F, -3.4669F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition top_top = top.addOrReplaceChild("top_top", CubeListBuilder.create().texOffs(0, 25).addBox(-2.5168F, -14.379F, -3.0064F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1047F, 0.0F, 0.0524F));

        PartDefinition hat4 = top_top.addOrReplaceChild("hat4", CubeListBuilder.create().texOffs(16, 25).addBox(-2.4662F, -15.6457F, -4.3375F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2094F, 0.0F, 0.1047F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    public SpectreDarkmageHatModel(ModelPart root) {
        super(root);
        this.hat = root.getChild("hat2");
        this.top = this.hat.getChild("top");
        this.top_top = this.top.getChild("top_top");
        this.hat4 = this.top_top.getChild("hat4");
    }

    @Override
    public void setupAnim(LivingEntity p_102866_, float p_102867_, float p_102868_, float p_102869_, float p_102870_, float p_102871_) {
        super.setupAnim(p_102866_, p_102867_, p_102868_, p_102869_, p_102870_, p_102871_);
    }

    @Override
    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of(hat);
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of();
    }
}
