package com.mega.revelationfix.mixin.ironspellbooks.goety_revelation;

import com.mega.endinglib.api.item.armor.OptionArmorMaterial;
import com.mega.endinglib.util.annotation.ModDependsMixin;
import com.mega.endinglib.util.entity.armor.ArmorModifiersBuilder;
import com.mega.revelationfix.common.item.armor.BaseArmorItem;
import com.mega.revelationfix.common.item.armor.SpiderArmor;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

/**
 * 为GR装备添加铁魔法属性支持
 */
@Mixin(SpiderArmor.class)
@ModDependsMixin("irons_spellbooks")
public abstract class SpiderArmorMixin extends BaseArmorItem {
    public SpiderArmorMixin(OptionArmorMaterial optionArmorMaterial, Type armorType, Properties itemProperties) {
        super(optionArmorMaterial, armorType, itemProperties);
    }
    @Inject(method = "injectExtraArmorAttributes", at = @At("TAIL"), remap = false)
    private void injectExtraArmorAttributes(ArmorModifiersBuilder builder, CallbackInfo ci) {
        UUID uuid = EXTRA_MODIFIER_UUID_PER_TYPE.get(type);
        builder.addModifier(AttributeRegistry.MAX_MANA.get(), new AttributeModifier(BaseArmorItem.EXTRA_MODIFIER_UUID_PER_TYPE.get(type), "Max Mana", 75.0, AttributeModifier.Operation.ADDITION));
    }
}
