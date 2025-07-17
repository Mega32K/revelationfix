package com.mega.revelationfix.client.model.entity;

import com.github.alexthe666.citadel.repack.jcodec.codecs.mjpeg.tools.AssertionException;
import com.mega.endinglib.mixin.accessor.AccessorModelPart;
import com.mega.revelationfix.common.compat.SafeClass;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.netty.util.internal.shaded.org.jctools.util.UnsafeAccess;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.model.geom.ModelPart;
import org.jetbrains.annotations.NotNull;
import sun.misc.Unsafe;

import java.util.List;
import java.util.Map;

public class MuchLightModelPart extends ModelPart {
    public MuchLightModelPart(ModelPart modelPart) {
        super(new ObjectArrayList<>(((AccessorModelPart) modelPart).getCubes()), new Object2ObjectOpenHashMap<>(((AccessorModelPart) modelPart).getChildren()));
        this.x = modelPart.x;
        this.y = modelPart.y;
        this.z = modelPart.z;
        this.xRot = modelPart.x;
        this.yRot = modelPart.yRot;
        this.zRot = modelPart.zRot;
        this.xScale = modelPart.xScale;
        this.yScale = modelPart.yScale;
        this.zScale = modelPart.zScale;
        this.visible = modelPart.visible;
        this.skipDraw = modelPart.skipDraw;
        this.setInitialPose(modelPart.getInitialPose());
    }

    @Override
    public void render(@NotNull PoseStack p_104307_, @NotNull VertexConsumer p_104308_, int p_104309_, int p_104310_, float p_104311_, float p_104312_, float p_104313_, float p_104314_) {
        p_104309_ = 0xFF00F0;
        if (SafeClass.usingShaderPack())
            p_104309_ = 20711920;
        super.render(p_104307_, p_104308_, p_104309_, p_104310_, p_104311_, p_104312_, p_104313_, p_104314_);
    }

}
