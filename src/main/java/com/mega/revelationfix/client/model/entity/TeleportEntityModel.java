package com.mega.revelationfix.client.model.entity;// Made with Blockbench 4.12.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mega.revelationfix.api.client.Easing;
import com.mega.revelationfix.client.renderer.MegaRenderType;
import com.mega.revelationfix.common.compat.SafeClass;
import com.mega.revelationfix.common.entity.binding.TeleportEntity;
import com.mojang.blaze3d.Blaze3D;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import z1gned.goetyrevelation.ModMain;

public class TeleportEntityModel extends EntityModel<TeleportEntity> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(ModMain.MODID, "teleport_entity"), "main");
	private final ModelPart main;

	public TeleportEntityModel(ModelPart root) {
		super(MegaRenderType::alwaysTransparencyEntityCutoutNoCull);
		this.main = root.getChild("main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 16).addBox(8.5F, 7.5F, 8.5F, -17.0F, -14.0F, -17.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 17.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	@Override
	public void setupAnim(@NotNull TeleportEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float p = entity.tickCount + ageInTicks;
		this.main.yRot = p % 80 < 30 ? Easing.IN_OUT_SINE.calculate((p % 80) / 30) * Mth.TWO_PI : 0;
	}

	@Override
	public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		if (SafeClass.usingShaderPack()) {
			packedLight = (int) (0xFF00F0 * (Math.min(0F, Mth.cos((float) (Blaze3D.getTime() * 90F * Mth.DEG_TO_RAD))) / 2F + 1f));
			alpha *= 0.6F * ((float) packedLight / (float) (0xFF00F0));
		}
		main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}