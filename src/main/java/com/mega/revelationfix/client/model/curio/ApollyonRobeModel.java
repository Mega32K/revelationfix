package com.mega.revelationfix.client.model.curio;// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


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

public class ApollyonRobeModel extends HumanoidModel<LivingEntity> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(ModMain.MODID, "apollyon_robe"), "main");
    public final ModelPart robe;

    public ApollyonRobeModel(ModelPart root) {
        super(root);
        this.robe = root.getChild("robe_layer");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = HumanoidModel.createMesh(LayerDefinitions.OUTER_ARMOR_DEFORMATION, 0.25F);
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition robe_layer = partdefinition.addOrReplaceChild("robe_layer", CubeListBuilder.create().texOffs(0, 38).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 18.0F, 6.0F, new CubeDeformation(0.125F, 0.125F, -0.5F))
                .texOffs(29, 45).addBox(-4.0F, 0.0F, -2.5F, 8.0F, 12.0F, 1.0F, new CubeDeformation(0.1F, 0.1F, -0.3F)), PartPose.offset(0.0F, 0.0F, 2.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(LivingEntity p_102866_, float p_102867_, float p_102868_, float p_102869_, float p_102870_, float p_102871_) {
        this.robe.copyFrom(this.body);
        if (this.crouching)
            this.robe.z = -1.0F;
        else this.robe.z = -0F;
        this.robe.visible = !p_102866_.isInvisible();
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        robe.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(robe);
    }
}