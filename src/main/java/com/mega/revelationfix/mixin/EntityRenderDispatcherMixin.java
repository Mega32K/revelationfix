package com.mega.revelationfix.mixin;

import com.Polarice3.Goety.common.entities.boss.Apostle;
import com.mega.revelationfix.common.compat.SafeClass;
import com.mega.revelationfix.safe.entity.Apollyon2Interface;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import z1gned.goetyrevelation.util.ApollyonAbilityHelper;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
    @Inject(method = "renderHitbox", at = @At("HEAD"), cancellable = true)
    private static void cancelApollyoDoomBox(PoseStack p_114442_, VertexConsumer p_114443_, Entity p_114444_, float p_114445_, CallbackInfo ci) {
        if (p_114444_ instanceof Apostle apostle) {
            ApollyonAbilityHelper helper = (ApollyonAbilityHelper) apostle;
            if (helper.allTitlesApostle_1_20_1$isApollyon() && apostle.isSecondPhase() && apostle.isInNether() && SafeClass.isDoom(apostle))
                if (((Apollyon2Interface) apostle).revelaionfix$illusionMode())
                    ci.cancel();
        }
    }
}
