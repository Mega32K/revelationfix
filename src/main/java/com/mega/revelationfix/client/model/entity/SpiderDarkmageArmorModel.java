package com.mega.revelationfix.client.model.entity;// Made with Blockbench 4.12.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

import com.google.common.collect.ImmutableList;
import com.mega.endinglib.api.client.Easing;
import com.mega.revelationfix.safe.entity.EntityExpandedContext;
import com.mega.revelationfix.util.LivingEntityEC;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import z1gned.goetyrevelation.ModMain;

public class SpiderDarkmageArmorModel extends HumanoidModel<LivingEntity> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation OUTER = new ModelLayerLocation(new ResourceLocation(ModMain.MODID, "spider_darkmage_armor"), "main");
	public final ModelPart head;
	public final ModelPart body_1;
	public final ModelPart right_fang_0;
	public final ModelPart right_fang_1;
	public final ModelPart right_fang_2;
	public final ModelPart left_fang_0;
	public final ModelPart left_fang_1;
	public final ModelPart left_fang_2;
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
		this.left_fang_0 = this.body_1.getChild("left_fang_0");
		this.left_fang_1 = this.body_1.getChild("left_fang_1");
		this.left_fang_2 = this.body_1.getChild("left_fang_2");
		this.right_fang_0 = this.body_1.getChild("right_fang_0");
		this.right_fang_1 = this.body_1.getChild("right_fang_1");
		this.right_fang_2 = this.body_1.getChild("right_fang_2");
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

		PartDefinition left_fang_2_r1 = body_1.addOrReplaceChild("left_fang_2", CubeListBuilder.create().texOffs(-22, 90).addBox(-7.5F, 0.0F, -11.0F, 15.0F, 0.0F, 22.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(9.8806F, 17.0F, 0.2836F, -0.054F, -0.3892F, 0.1415F));

		PartDefinition left_fang_1_r1 = body_1.addOrReplaceChild("left_fang_1", CubeListBuilder.create().texOffs(-22, 90).addBox(-2.0F, 8.0F, -13.35F, 15.0F, 0.0F, 22.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(3.9F, 1.0F, 0.35F, 0.0F, -0.6109F, 0.0F));

		PartDefinition left_fang_0_r1 = body_1.addOrReplaceChild("left_fang_0", CubeListBuilder.create().texOffs(-22, 90).addBox(-7.5F, 0.0F, -11.0F, 15.0F, 0.0F, 22.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(9.8806F, 1.0F, 0.2836F, 0.054F, -0.3892F, -0.1415F));

		PartDefinition right_fang_2_r1 = body_1.addOrReplaceChild("right_fang_1", CubeListBuilder.create().texOffs(-22, 66).addBox(-13.0F, 8.0F, -13.0F, 15.0F, 0.0F, 22.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(-4.0F, 1.0F, 0.0F, 0.0F, 0.6109F, 0.0F));

		PartDefinition right_fang_1_r1 = body_1.addOrReplaceChild("right_fang_2", CubeListBuilder.create().texOffs(-22, 66).addBox(-7.5F, 0.0F, -11.0F, 15.0F, 0.0F, 22.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(-9.8467F, 17.0F, 0.257F, -0.054F, 0.3892F, -0.1415F));

		PartDefinition right_fang_0_r1 = body_1.addOrReplaceChild("right_fang_0", CubeListBuilder.create().texOffs(-22, 66).addBox(-7.5F, 0.0F, -11.0F, 15.0F, 0.0F, 22.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(-9.8467F, 1.0F, 0.257F, 0.054F, 0.3892F, 0.1415F));

		PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(1.0F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

		PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(1.0F)).mirror(false), PartPose.offset(5.0F, 2.0F, 0.0F));

		PartDefinition right_leg_1 = partdefinition.addOrReplaceChild("right_leg_1", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(1.0F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

		PartDefinition left_leg_1 = partdefinition.addOrReplaceChild("left_leg_1", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(1.0F)).mirror(false), PartPose.offset(1.9F, 12.0F, 0.0F));

		PartDefinition body_2 = partdefinition.addOrReplaceChild("body_2", CubeListBuilder.create().texOffs(16, 48).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.51F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition right_leg_2 = partdefinition.addOrReplaceChild("right_leg_2", CubeListBuilder.create().texOffs(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

		PartDefinition left_leg_2 = partdefinition.addOrReplaceChild("left_leg_2", CubeListBuilder.create().texOffs(0, 48).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)).mirror(false), PartPose.offset(1.9F, 12.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(LivingEntity livingEntity, float p_102867_, float p_102868_, float time, float degHeadBody, float xRot) {
		float partialTicks = Minecraft.getInstance().getPartialTick();
		EntityExpandedContext entityEC = ((LivingEntityEC) livingEntity).revelationfix$livingECData();
		boolean shouldSit = livingEntity.isPassenger() && (livingEntity.getVehicle() != null && livingEntity.getVehicle().shouldRiderSit());
		float f5 = 0.0F;
		float f8 = 0.0F;
		if (!shouldSit && livingEntity.isAlive()) {
			f8 = livingEntity.walkAnimation.speed(partialTicks);
			if (entityEC.customArmorWalkAnimState.speed() > 0.001F)
				f8 += entityEC.customArmorWalkAnimState.speed(partialTicks);
			f5 = livingEntity.walkAnimation.position(partialTicks);
			if (livingEntity.isBaby()) {
				f5 *= 3.0F;
			}

			if (f8 > 1.0F) {
				f8 = 1.0F;
			}
		}
		{
			float f = Mth.rotLerp(partialTicks, livingEntity.yBodyRotO, livingEntity.yBodyRot);
			float f1 = Mth.rotLerp(partialTicks, livingEntity.yHeadRotO, livingEntity.yHeadRot);
			float f2 = f1 - f;
			if (shouldSit && livingEntity.getVehicle() instanceof LivingEntity livingentity) {
				f = Mth.rotLerp(partialTicks, livingentity.yBodyRotO, livingentity.yBodyRot);
				f2 = f1 - f;
				float f3 = Mth.wrapDegrees(f2);
				if (f3 < -85.0F) {
					f3 = -85.0F;
				}

				if (f3 >= 85.0F) {
					f3 = 85.0F;
				}

				f = f1 - f3;
				if (f3 * f3 > 2500.0F) {
					f += f3 * 0.2F;
				}

				f2 = f1 - f;
			}

			float f6 = Mth.lerp(partialTicks, livingEntity.xRotO, livingEntity.getXRot());
			if (LivingEntityRenderer.isEntityUpsideDown(livingEntity)) {
				f6 *= -1.0F;
				f2 *= -1.0F;
			}
			degHeadBody = f2;
			xRot = f6;
		}
		time = time + partialTicks;
		time = time % 240;
		float cos;
		float rad0;

		float speedAnim = Math.min(1.0F, f8) * 15.0F * Mth.DEG_TO_RAD;
		right_fang_0.yRot = 0.3892F + speedAnim;
		left_fang_0.yRot = -right_fang_0.yRot;
		right_fang_1.yRot = 0.6109F + speedAnim * 1.2F;
		left_fang_1.yRot = -right_fang_1.yRot;
		right_fang_2.yRot = 0.3892F + speedAnim;
		left_fang_2.yRot = -right_fang_2.yRot;

		right_fang_0.zRot = 0.1415F + speedAnim;
		left_fang_0.zRot = -right_fang_0.zRot;
		right_fang_2.zRot = -0.1415F - speedAnim;
		left_fang_2.zRot = -right_fang_2.zRot;
		if (!livingEntity.onClimbable()) {
			if (time < 40) {
				cos = (time - 20) / 20F;
				float f2 = cos < 0 ? Easing.IN_OUT_CUBIC.calculate(cos + 1F) : Easing.IN_OUT_CUBIC.calculate(1 - cos);
				rad0 = f2 * 7F * Mth.DEG_TO_RAD;
				right_fang_0.yRot += rad0;
				left_fang_0.yRot -= rad0;
			}
			if (time >= 30 && time < 70) {
				//-1 -> 1
				cos = (time - 50) / 20F;
				float f2 = cos < 0 ? Easing.IN_OUT_CUBIC.calculate(cos + 1F) : Easing.IN_OUT_CUBIC.calculate(1 - cos);
				rad0 = f2 * 7F * Mth.DEG_TO_RAD;
				right_fang_1.yRot += rad0;
				left_fang_1.yRot -= rad0;
			}
			if (time >= 60 && time < 100) {
				cos = (time - 80) / 20F;
				float f2 = cos < 0 ? Easing.IN_OUT_CUBIC.calculate(cos + 1F) : Easing.IN_OUT_CUBIC.calculate(1 - cos);
				rad0 = f2 * 7F * Mth.DEG_TO_RAD;
				right_fang_2.yRot += rad0;
				left_fang_2.yRot -= rad0;
			}
		}
		float hurtDuration = livingEntity.hurtDuration;
		float hurtTime = (hurtDuration - livingEntity.hurtTime);
		if (hurtTime < hurtDuration) {
			hurtTime+= partialTicks;
			float center = hurtDuration * 0.5F;
			float radians = hurtTime < center ? Easing.OUT_EXPO.calculate(hurtTime / center) : Easing.IN_SINE.calculate(1F - (hurtTime - center) / center);
			radians *= -60F * Mth.DEG_TO_RAD;
			right_fang_0.yRot += radians;
			right_fang_1.yRot += radians;
			right_fang_2.yRot += radians;
			left_fang_0.yRot -= radians;
			left_fang_1.yRot -= radians;
			left_fang_2.yRot -= radians;
			right_fang_0.zRot += radians * 0.2F;
			left_fang_0.zRot -= radians * 0.2F;
			right_fang_2.zRot += radians * 0.2F;
			left_fang_2.zRot -= radians * 0.2F;
		}
	}

	@Override
	public void renderToBuffer(PoseStack p_102034_, VertexConsumer p_102035_, int p_102036_, int p_102037_, float p_102038_, float p_102039_, float p_102040_, float p_102041_) {
		p_102036_ = 0xFF00F0;

		super.renderToBuffer(p_102034_, p_102035_, p_102036_, p_102037_, p_102038_, p_102039_, p_102040_, p_102041_);
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