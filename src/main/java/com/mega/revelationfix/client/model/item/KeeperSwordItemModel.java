package com.mega.revelationfix.client.model.item;// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.WardenRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import z1gned.goetyrevelation.ModMain;

public class KeeperSwordItemModel<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(ModMain.MODID, "keeper_sword"), "main");
	private final ModelPart sword;
	private final ModelPart sword_rot;
	private final ModelPart blade;
	private final ModelPart edge;
	private final ModelPart tip;
	private final ModelPart handle;
	private final ModelPart guard;
	private final ModelPart hilt;
	private final ModelPart pommel;

	public KeeperSwordItemModel(ModelPart root) {
		this.sword = root.getChild("sword");
		this.sword_rot = this.sword.getChild("sword_rot");
		this.blade = this.sword_rot.getChild("blade");
		this.edge = this.blade.getChild("edge");
		this.tip = this.edge.getChild("tip");
		this.handle = this.sword_rot.getChild("handle");
		this.guard = this.handle.getChild("guard");
		this.hilt = this.handle.getChild("hilt");
		this.pommel = this.handle.getChild("pommel");
	}

	public static LayerDefinition createBodyLayer() {

		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition sword = partdefinition.addOrReplaceChild("sword", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition sword_rot = sword.addOrReplaceChild("sword_rot", CubeListBuilder.create(), PartPose.offsetAndRotation(1.0F, 0.0F, 9.2207F, 1.5708F, 0.0F, -1.5708F));

		PartDefinition blade = sword_rot.addOrReplaceChild("blade", CubeListBuilder.create().texOffs(0, 10).addBox(-4.0F, -29.0F, -23.5F, 8.0F, 28.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 47).addBox(-4.5F, -1.0F, -24.0F, 9.0F, 16.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -29.0F, 24.0F));

		PartDefinition edge = blade.addOrReplaceChild("edge", CubeListBuilder.create().texOffs(38, 10).addBox(-3.5F, -12.0F, -0.5F, 7.0F, 24.0F, 1.0F, new CubeDeformation(0.005F)), PartPose.offset(0.0F, -41.0F, -23.0F));

		PartDefinition tip = edge.addOrReplaceChild("tip", CubeListBuilder.create(), PartPose.offset(11.0F, -3.5F, 0.0F));

		PartDefinition cube_r1 = tip.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(68, 7).addBox(-2.5F, -2.5F, -0.5F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.007F)), PartPose.offsetAndRotation(-11.0F, -8.5F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition handle = sword_rot.addOrReplaceChild("handle", CubeListBuilder.create(), PartPose.offset(0.0F, -6.0F, 1.0F));

		PartDefinition guard = handle.addOrReplaceChild("guard", CubeListBuilder.create().texOffs(23, 47).addBox(-4.0F, -4.0F, -2.5F, 8.0F, 8.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r2 = guard.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(68, 21).addBox(-2.5F, -2.5F, -0.5F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(68, 14).addBox(-2.5F, -2.5F, -6.5F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 3.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r3 = guard.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(50, 47).addBox(-8.0F, -16.0F, 0.0F, 10.0F, 16.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(19, 10).addBox(-1.5F, -16.0F, -4.5F, 0.0F, 16.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(23, 61).addBox(-3.0F, -10.0F, -1.5F, 3.0F, 10.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.3927F));

		PartDefinition cube_r4 = guard.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(19, 36).addBox(-16.0F, -8.0F, 0.0F, 16.0F, 10.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-16.0F, -1.5F, -4.5F, 16.0F, 0.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(51, 0).addBox(-10.0F, -3.0F, -1.5F, 10.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 4.0F, 0.0F, 0.0F, 0.0F, -0.3927F));

		PartDefinition hilt = handle.addOrReplaceChild("hilt", CubeListBuilder.create().texOffs(55, 7).addBox(5.5F, 5.0F, -3.0F, 3.0F, 14.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(55, 25).addBox(5.5F, 5.0F, -3.0F, 3.0F, 14.0F, 3.0F, new CubeDeformation(0.25F)), PartPose.offset(-7.0F, -10.0F, 1.5F));

		PartDefinition pommel = handle.addOrReplaceChild("pommel", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r5 = pommel.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(62, 64).addBox(0.0F, 0.0F, 0.0F, 0.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.25F, -2.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition cube_r6 = pommel.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(53, 64).addBox(0.0F, 0.0F, -4.0F, 0.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.25F, 2.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition cube_r7 = pommel.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 40).addBox(2.0F, -2.0F, -1.0F, 5.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 66).addBox(-2.0F, 2.0F, -1.0F, 4.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(36, 64).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		sword.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}