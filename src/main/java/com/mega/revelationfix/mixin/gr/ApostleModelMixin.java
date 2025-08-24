package com.mega.revelationfix.mixin.gr;

import com.Polarice3.Goety.client.render.model.ApostleModel;
import com.Polarice3.Goety.client.render.model.CultistModel;
import com.Polarice3.Goety.common.entities.boss.Apostle;
import com.mega.revelationfix.common.compat.SafeClass;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import z1gned.goetyrevelation.util.ApollyonAbilityHelper;

@Mixin(ApostleModel.class)
public class ApostleModelMixin<T extends Apostle> extends CultistModel<T> {
    @Shadow(remap = false)
    public ModelPart halo1;
    @Shadow(remap = false)
    public ModelPart hat2;

    public ApostleModelMixin(ModelPart p_170677_) {
        super(p_170677_);
    }

    @Inject(
            at = {@At("RETURN")},
            method = {"setupAnim(Lcom/Polarice3/Goety/common/entities/boss/Apostle;FFFFF)V"},
            remap = false
    )
    private void hatVisible(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        if (((ApollyonAbilityHelper) entity).allTitlesApostle_1_20_1$isApollyon()) {
            this.hat2.visible = true;
            if (SafeClass.manbaDay())
                this.nose.visible = false;
        }
    }
}
