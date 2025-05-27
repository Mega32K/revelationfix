package com.mega.revelationfix.common.odamane.client;

import com.mega.revelationfix.Revelationfix;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class OdamaneHaloModel<T extends AbstractClientPlayer> extends PlayerModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Revelationfix.MODID, "halo_of_the_end"), "main");
    /*
    public final ModelPart BackStar;
    public final ModelPart hole2;
     */
    public final ModelPart Halo;
    public final ModelPart bone;
    public final ModelPart bone2;
    public final ModelPart bone3;
    public final ModelPart bone4;
    public final ModelPart bone5;

    public OdamaneHaloModel(ModelPart root, boolean slim) {
        super(root, slim);
        /*
        this.BackStar = this.head.getChild("BackStar");
        this.hole2 = this.head.getChild("hole2");
         */
        this.Halo = this.head.getChild("Halo");
        this.bone = this.Halo.getChild("bone");
        this.bone2 = this.Halo.getChild("bone2");
        this.bone3 = this.Halo.getChild("bone3");
        this.bone4 = this.Halo.getChild("bone4");
        this.bone5 = this.Halo.getChild("bone5");
    }

    public static void renderModelPart(ModelPart modelPart, PoseStack p_104302_, VertexConsumer p_104303_, int p_104304_, int p_104305_, float alpha) {
        modelPart.render(p_104302_, p_104303_, p_104304_, p_104305_, 1.0F, 1.0F, 1.0F, alpha);
    }

    public static LayerDefinition createBodyLayer() {
        /*

        MeshDefinition meshdefinition = PlayerModel.createMesh(CubeDeformation.NONE, true);
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition head = partdefinition.getChild("head");
        PartDefinition BackStar = head.addOrReplaceChild("BackStar", CubeListBuilder.create(), PartPose.offset(0.0F, -5.3714F, 9.6F));

        PartDefinition BackStar_r1 = BackStar.addOrReplaceChild("BackStar_r1", CubeListBuilder.create().texOffs(12, 14).addBox(-2.5F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.4151F, 7.6384F, 0.0F, 0.0F, 0.0F, 0.5236F));

        PartDefinition BackStar_r2 = BackStar.addOrReplaceChild("BackStar_r2", CubeListBuilder.create().texOffs(0, 14).addBox(-2.5F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.6471F, 4.4063F, 0.0F, 0.0F, 0.0F, 1.0472F));

        PartDefinition BackStar_r3 = BackStar.addOrReplaceChild("BackStar_r3", CubeListBuilder.create().texOffs(12, 12).addBox(-2.5F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.6471F, -4.4238F, 0.0F, 0.0F, 0.0F, -1.0472F));

        PartDefinition BackStar_r4 = BackStar.addOrReplaceChild("BackStar_r4", CubeListBuilder.create().texOffs(12, 10).addBox(-2.5F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.4151F, -7.6558F, 0.0F, 0.0F, 0.0F, -0.5236F));

        PartDefinition BackStar_r5 = BackStar.addOrReplaceChild("BackStar_r5", CubeListBuilder.create().texOffs(4, 24).addBox(-1.0F, -0.9699F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-20.8529F, -0.0329F, 0.5F, 0.0F, 0.0F, -0.7854F));

        PartDefinition BackStar_r6 = BackStar.addOrReplaceChild("BackStar_r6", CubeListBuilder.create().texOffs(0, 24).addBox(0.0F, -0.9699F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(20.8529F, -0.0329F, 0.5F, 0.0F, 0.0F, 0.7854F));

        PartDefinition BackStar_r7 = BackStar.addOrReplaceChild("BackStar_r7", CubeListBuilder.create().texOffs(0, 6).addBox(-6.3147F, -1.8344F, -0.5F, 13.1F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.0014F, -0.0087F, 0.0F, 0.0F, 0.0F, -0.1745F));

        PartDefinition BackStar_r8 = BackStar.addOrReplaceChild("BackStar_r8", CubeListBuilder.create().texOffs(0, 4).addBox(-6.3147F, 0.8344F, -0.5F, 13.1F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.0014F, -0.0087F, 0.0F, 0.0F, 0.0F, 0.1745F));

        PartDefinition BackStar_r9 = BackStar.addOrReplaceChild("BackStar_r9", CubeListBuilder.create().texOffs(0, 2).addBox(-6.7853F, -1.8344F, -0.5F, 13.1F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.0014F, -0.0087F, 0.0F, 0.0F, 0.0F, 0.1745F));

        PartDefinition BackStar_r10 = BackStar.addOrReplaceChild("BackStar_r10", CubeListBuilder.create().texOffs(0, 0).addBox(-6.7853F, 0.8344F, -0.5F, 13.1F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.0014F, -0.0087F, 0.0F, 0.0F, 0.0F, -0.1745F));

        PartDefinition BackStar_r11 = BackStar.addOrReplaceChild("BackStar_r11", CubeListBuilder.create().texOffs(12, 8).addBox(-2.5F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.4151F, -7.6558F, 0.0F, 0.0F, 0.0F, 0.5236F));

        PartDefinition BackStar_r12 = BackStar.addOrReplaceChild("BackStar_r12", CubeListBuilder.create().texOffs(0, 12).addBox(-2.5F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.6471F, -4.4238F, 0.0F, 0.0F, 0.0F, 1.0472F));

        PartDefinition BackStar_r13 = BackStar.addOrReplaceChild("BackStar_r13", CubeListBuilder.create().texOffs(0, 10).addBox(-2.5F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.6471F, 4.4063F, 0.0F, 0.0F, 0.0F, -1.0472F));

        PartDefinition BackStar_r14 = BackStar.addOrReplaceChild("BackStar_r14", CubeListBuilder.create().texOffs(0, 8).addBox(-2.5F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.4151F, 7.6384F, 0.0F, 0.0F, 0.0F, -0.5236F));

        PartDefinition hole2 = head.addOrReplaceChild("hole2", CubeListBuilder.create().texOffs(0, 16).addBox(-1.4109F, 5.393F, -0.325F, 3.25F, 0.65F, 0.65F, new CubeDeformation(0.0F))
                .texOffs(8, 16).addBox(-1.7359F, -6.0733F, -0.325F, 3.25F, 0.65F, 0.65F, new CubeDeformation(0.0F))
                .texOffs(0, 22).addBox(-1.8391F, 5.393F, -0.325F, 3.25F, 0.65F, 0.65F, new CubeDeformation(0.0F))
                .texOffs(8, 22).addBox(-1.5141F, -6.0733F, -0.325F, 3.25F, 0.65F, 0.65F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.3714F, 9.6F));

        PartDefinition hole2_r1 = hole2.addOrReplaceChild("hole2_r1", CubeListBuilder.create().texOffs(16, 22).addBox(0.3351F, -2.6696F, -0.325F, 3.25F, 0.65F, 0.65F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0311F, -0.0087F, 0.0F, 0.0F, 0.0F, -1.0472F));

        PartDefinition hole2_r2 = hole2.addOrReplaceChild("hole2_r2", CubeListBuilder.create().texOffs(16, 20).addBox(-5.02F, -4.1045F, -0.325F, 3.25F, 0.65F, 0.65F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0311F, -0.0087F, 0.0F, 0.0F, 0.0F, 0.5236F));

        PartDefinition hole2_r3 = hole2.addOrReplaceChild("hole2_r3", CubeListBuilder.create().texOffs(8, 20).addBox(-3.5851F, -2.6696F, -0.325F, 3.25F, 0.65F, 0.65F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0311F, -0.0087F, 0.0F, 0.0F, 0.0F, 1.0472F));

        PartDefinition hole2_r4 = hole2.addOrReplaceChild("hole2_r4", CubeListBuilder.create().texOffs(0, 20).addBox(-3.5851F, 2.0196F, -0.325F, 3.25F, 0.65F, 0.65F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0311F, -0.0087F, 0.0F, 0.0F, 0.0F, -1.0472F));

        PartDefinition hole2_r5 = hole2.addOrReplaceChild("hole2_r5", CubeListBuilder.create().texOffs(16, 18).addBox(-5.02F, 3.4545F, -0.325F, 3.25F, 0.65F, 0.65F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0311F, -0.0087F, 0.0F, 0.0F, 0.0F, -0.5236F));

        PartDefinition hole2_r6 = hole2.addOrReplaceChild("hole2_r6", CubeListBuilder.create().texOffs(8, 18).addBox(1.77F, -4.1045F, -0.325F, 3.25F, 0.65F, 0.65F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0311F, -0.0087F, 0.0F, 0.0F, 0.0F, -0.5236F));

        PartDefinition hole2_r7 = hole2.addOrReplaceChild("hole2_r7", CubeListBuilder.create().texOffs(0, 18).addBox(0.3351F, 2.0196F, -0.325F, 3.25F, 0.65F, 0.65F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0311F, -0.0087F, 0.0F, 0.0F, 0.0F, 1.0472F));

        PartDefinition hole2_r8 = hole2.addOrReplaceChild("hole2_r8", CubeListBuilder.create().texOffs(16, 16).addBox(1.77F, 3.4545F, -0.325F, 3.25F, 0.65F, 0.65F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0311F, -0.0087F, 0.0F, 0.0F, 0.0F, 0.5236F));

        return LayerDefinition.create(meshdefinition, 32, 32);
         */

        MeshDefinition meshdefinition = PlayerModel.createMesh(CubeDeformation.NONE, true);
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition head = partdefinition.getChild("head");
        PartDefinition Halo = head.addOrReplaceChild("Halo", CubeListBuilder.create(), PartPose.offset(0.0F, -5.0F, 10.0F));

        PartDefinition bone = Halo.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, -9.0F, 0.0F, 18.0F, 18.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 18).addBox(9.0F, -3.0F, 0.0F, 8.0F, 6.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(16, 18).addBox(-17.0F, -3.0F, 0.0F, 8.0F, 6.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 24).addBox(-1.5F, 9.0F, 0.0F, 4.0F, 6.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(8, 24).addBox(-1.5F, -15.0F, 0.0F, 4.0F, 6.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(8, 24).mirror().addBox(-2.5F, -15.0F, 0.0F, 4.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 24).mirror().addBox(-2.5F, 9.0F, 0.0F, 4.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bone2 = Halo.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(16, 31).addBox(20.5F, -1.5F, 0.0F, 7.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r1 = bone2.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(3, 35).mirror().addBox(0.2164F, -0.9763F, 0.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(21.0237F, -0.7836F, 0.0F, 0.0F, 0.0F, -1.789F));

        PartDefinition cube_r2 = bone2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(5, 34).mirror().addBox(-1.0F, 0.0F, 0.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(19.7456F, 1.653F, 0.0F, 0.0F, 0.0F, -1.2654F));

        PartDefinition cube_r3 = bone2.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(1, 34).mirror().addBox(1.0F, 19.0F, 0.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0005F)).mirror(false), PartPose.offsetAndRotation(1.0F, 2.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

        PartDefinition bone3 = Halo.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(16, 31).mirror().addBox(-27.5F, -1.5F, 0.0F, 7.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r4 = bone3.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(7, 35).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-19.7456F, 1.653F, 0.0F, 0.0F, 0.0F, 1.2654F));

        PartDefinition cube_r5 = bone3.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(1, 36).addBox(-3.0F, 19.0F, 0.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0005F)), PartPose.offsetAndRotation(-1.0F, 2.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

        PartDefinition cube_r6 = bone3.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(1, 35).addBox(-2.2164F, -0.9763F, 0.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-21.0237F, -0.7836F, 0.0F, 0.0F, 0.0F, 1.789F));

        PartDefinition bone4 = Halo.addOrReplaceChild("bone4", CubeListBuilder.create().texOffs(16, 36).addBox(-0.5F, 17.5F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(-0.005F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r7 = bone4.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(16, 29).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(-0.0005F)).mirror(false), PartPose.offsetAndRotation(-0.7218F, 17.0F, 1.0F, 0.0F, 0.0F, 0.2182F));

        PartDefinition cube_r8 = bone4.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(17, 29).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(-0.0005F)).mirror(false), PartPose.offsetAndRotation(0.7218F, 17.0F, 1.0F, 0.0F, 0.0F, -0.2182F));

        PartDefinition bone5 = Halo.addOrReplaceChild("bone5", CubeListBuilder.create().texOffs(16, 36).addBox(-0.5F, -18.5F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(-0.0009F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r9 = bone5.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(16, 29).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.7218F, -17.0F, 1.0F, 0.0F, 0.0F, 0.2182F));

        PartDefinition cube_r10 = bone5.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(18, 29).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7218F, -17.0F, 1.0F, 0.0F, 0.0F, -0.2182F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void setupAnim(T p_103395_, float p_103396_, float p_103397_, float p_103398_, float p_103399_, float p_103400_) {
        this.head.visible = !p_103395_.isInvisible();
        /*
        this.BackStar.visible = !p_103395_.isInvisible();
        this.hole2.visible = !p_103395_.isInvisible();
         */
        this.Halo.visible = !p_103395_.isInvisible();
        super.setupAnim(p_103395_, p_103396_, p_103397_, p_103398_, p_103399_, p_103400_);
    }
}