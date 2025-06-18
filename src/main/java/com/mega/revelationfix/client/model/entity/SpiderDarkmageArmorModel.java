package com.mega.revelationfix.client.model.entity;// Made with Blockbench 4.12.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import z1gned.goetyrevelation.ModMain;

public class SpiderDarkmageArmorModel extends HumanoidModel<LivingEntity> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation OUTER = new ModelLayerLocation(new ResourceLocation(ModMain.MODID, "spider_darkmage_armor"), "main");
	public final ModelPart head;
	public final ModelPart body_1;
	public final ModelPart right_arm;
	public final ModelPart left_arm;
	public final ModelPart right_leg_1;
	public final ModelPart left_leg_1;
	public final ModelPart body_2;
	public final ModelPart right_leg_2;
	public final ModelPart left_leg_2;

	public SpiderDarkmageArmorModel(ModelPart root) {
		super(root);
		this.head = root.getChild("head");
		this.body_1 = root.getChild("body_1");
		this.right_arm = root.getChild("right_arm");
		this.left_arm = root.getChild("left_arm");
		this.right_leg_1 = root.getChild("right_leg_1");
		this.left_leg_1 = root.getChild("left_leg_1");
		this.body_2 = root.getChild("body_2");
		this.right_leg_2 = root.getChild("right_leg_2");
		this.left_leg_2 = root.getChild("left_leg_2");
	}
	public static LayerDefinition creteOuter() {
		return createBodyLayer(LayerDefinitions.OUTER_ARMOR_DEFORMATION);
	}
	public static LayerDefinition createBodyLayer(CubeDeformation cubeDeformation) {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(cubeDeformation, 0F);
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(1.0F))
		.texOffs(32, 0).addBox(-4.0F, -6.5F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(1.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition body_1 = partdefinition.addOrReplaceChild("body_1", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(1.01F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Fang_r1 = body_1.addOrReplaceChild("Fang_r1", CubeListBuilder.create().texOffs(1, 65).addBox(-2.0F, -16.0F, 9.0F, 15.0F, 24.0F, 0.001F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, 8.0F, -5.0F, 0.0F, -0.3927F, 0.0F));

		PartDefinition Fang_r2 = body_1.addOrReplaceChild("Fang_r2", CubeListBuilder.create().texOffs(1, 65).mirror().addBox(-13.0F, -16.0F, 9.0F, 15.0F, 24.0F, 0.001F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-6.0F, 8.0F, -5.0F, 0.0F, 0.3927F, 0.0F));

		PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(1.0F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

		PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(1.0F)).mirror(false), PartPose.offset(5.0F, 2.0F, 0.0F));

		PartDefinition right_leg_1 = partdefinition.addOrReplaceChild("right_leg_1", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(1.0F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

		PartDefinition left_leg_1 = partdefinition.addOrReplaceChild("left_leg_1", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(1.0F)).mirror(false), PartPose.offset(1.9F, 12.0F, 0.0F));

		PartDefinition body_2 = partdefinition.addOrReplaceChild("body_2", CubeListBuilder.create().texOffs(16, 47).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.51F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition right_leg_2 = partdefinition.addOrReplaceChild("right_leg_2", CubeListBuilder.create().texOffs(0, 47).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

		PartDefinition left_leg_2 = partdefinition.addOrReplaceChild("left_leg_2", CubeListBuilder.create().texOffs(0, 47).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)).mirror(false), PartPose.offset(1.9F, 12.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	protected Iterable<ModelPart> headParts() {
		return ImmutableList.of(this.head);
	}

	@Override
	protected Iterable<ModelPart> bodyParts() {
		return ImmutableList.of(this.body_1, this.rightArm, this.leftArm, this.right_leg_1, this.left_leg_1, this.body_2, this.right_leg_2, this.left_leg_2, this.hat);
	}
}