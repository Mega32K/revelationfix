package com.mega.revelationfix.mixin;

import com.mega.revelationfix.safe.entity.AttributeInstanceInterface;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

@Mixin(AttributeInstance.class)
public abstract class AttributeInstanceMixin implements AttributeInstanceInterface {
    @Shadow
    @Final
    private Attribute attribute;
    @Unique
    private boolean revelationfix$alwaysBase = false;
    @Unique
    private boolean revelationfix$quietus = false;

    @Shadow
    public abstract double getBaseValue();

    @Shadow
    protected abstract Collection<AttributeModifier> getModifiersOrEmpty(AttributeModifier.Operation p_22117_);

    @Override
    public void revelationfix$setAlwaysBase(boolean z) {
        if (z) {
            if (this.attribute == Attributes.ATTACK_DAMAGE)
                z = false;
        }
        revelationfix$alwaysBase = z;
    }

    @Override
    public boolean revelationfix$alwaysBase() {
        return revelationfix$alwaysBase;
    }

    @Override
    public boolean revelationfix$isQuietus() {
        return revelationfix$quietus;
    }

    @Override
    public void revelationfix$setQuietus(boolean z) {
        revelationfix$quietus = z;
    }

    @Inject(method = "calculateValue", at = @At("HEAD"), cancellable = true)
    private void calculateValue(CallbackInfoReturnable<Double> cir) {
        if (this.revelationfix$alwaysBase()) {
            double d1 = this.getBaseValue();
            if (this.attribute == Attributes.MOVEMENT_SPEED) {
                for (AttributeModifier attributemodifier2 : this.getModifiersOrEmpty(AttributeModifier.Operation.MULTIPLY_TOTAL)) {
                    if (attributemodifier2 == LivingEntity.SPEED_MODIFIER_SPRINTING) {
                        d1 *= 1.0D + attributemodifier2.getAmount();
                        break;
                    }
                }
            } else if (this.attribute == Attributes.ATTACK_SPEED) {
                for (AttributeModifier attributemodifier2 : this.getModifiersOrEmpty(AttributeModifier.Operation.ADDITION)) {
                    if (attributemodifier2.getId() == Item.BASE_ATTACK_SPEED_UUID) {
                        d1 += attributemodifier2.getAmount();
                        break;
                    }
                }
            }
            cir.setReturnValue(this.attribute.sanitizeValue(d1));
        }
    }

    @Inject(method = "calculateValue", at = @At("RETURN"), cancellable = true)
    private void calculateValue2(CallbackInfoReturnable<Double> cir) {
        if (this.revelationfix$isQuietus()) {
            if (this.attribute == Attributes.MAX_HEALTH) {
                cir.setReturnValue(cir.getReturnValueD() / 2D);
            }
        }
    }
}
