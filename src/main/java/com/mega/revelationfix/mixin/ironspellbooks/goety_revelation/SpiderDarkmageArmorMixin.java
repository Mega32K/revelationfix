package com.mega.revelationfix.mixin.ironspellbooks.goety_revelation;

import com.google.common.collect.ImmutableMultimap;
import com.mega.endinglib.util.annotation.ModDependsMixin;
import com.mega.endinglib.util.entity.armor.ArmorModifiersBuilder;
import com.mega.revelationfix.common.init.ModAttributes;
import com.mega.revelationfix.common.item.armor.BaseArmorItem;
import com.mega.revelationfix.common.item.armor.SpiderArmor;
import com.mega.revelationfix.common.item.armor.SpiderDarkmageArmor;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.spells.IPresetSpellContainer;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

import java.util.UUID;

@Mixin(SpiderDarkmageArmor.class)
@ModDependsMixin("irons_spellbooks")
public class SpiderDarkmageArmorMixin extends SpiderArmor implements IPresetSpellContainer {
    SpiderDarkmageArmorMixin(Type p_40387_) {
        super(p_40387_);
    }

    @Override
    public void injectExtraArmorAttributes(ArmorModifiersBuilder builder) {
        UUID uuid = EXTRA_MODIFIER_UUID_PER_TYPE.get(type);
        builder.addModifier(ModAttributes.SPELL_POWER.get(), new AttributeModifier(uuid, "Armor modifier", 0.35, AttributeModifier.Operation.ADDITION));
        builder.addModifier(ModAttributes.SPELL_POWER_MULTIPLIER.get(), new AttributeModifier(uuid, "Armor modifier", .1, AttributeModifier.Operation.ADDITION));
        builder.addModifier(ModAttributes.CAST_DURATION.get(), new AttributeModifier(uuid, "Armor modifier", .1, AttributeModifier.Operation.ADDITION));
        builder.addModifier(ModAttributes.SPELL_COOLDOWN.get(), new AttributeModifier(uuid, "Armor modifier", .1, AttributeModifier.Operation.ADDITION));
        builder.addModifier(AttributeRegistry.MAX_MANA.get(), new AttributeModifier(BaseArmorItem.EXTRA_MODIFIER_UUID_PER_TYPE.get(type), "Max Mana", 125.0, AttributeModifier.Operation.ADDITION));
        builder.addModifier(AttributeRegistry.SPELL_POWER.get(), new AttributeModifier(BaseArmorItem.EXTRA_MODIFIER_UUID_PER_TYPE.get(type), "Base Power", 0.05, AttributeModifier.Operation.MULTIPLY_BASE));
        builder.addModifier(AttributeRegistry.BLOOD_SPELL_POWER.get(), new AttributeModifier(BaseArmorItem.EXTRA_MODIFIER_UUID_PER_TYPE.get(type), "Base Power", 0.05, AttributeModifier.Operation.MULTIPLY_BASE));

    }
    @Override
    public void initializeSpellContainer(ItemStack itemStack) {
        if (itemStack != null) {
            if (itemStack.getItem() instanceof ArmorItem armorItem) {
                if (armorItem.getType() == Type.CHESTPLATE && !ISpellContainer.isSpellContainer(itemStack)) {
                    ISpellContainer spellContainer = ISpellContainer.create(1, true, true);
                    spellContainer.save(itemStack);
                }
            }

        }
    }
}