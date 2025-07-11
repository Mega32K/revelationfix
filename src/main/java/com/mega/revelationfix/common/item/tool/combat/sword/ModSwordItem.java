package com.mega.revelationfix.common.item.tool.combat.sword;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mega.revelationfix.api.item.combat.IWeaponExtraAttributesInjector;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import org.jetbrains.annotations.NotNull;

public class ModSwordItem extends SwordItem implements IWeaponExtraAttributesInjector {
    private final float attackDamage;
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;
    public ModSwordItem(Tier p_43269_, int p_43270_, float p_43271_, Properties p_43272_) {
        super(p_43269_, p_43270_, p_43271_, p_43272_);
        this.attackDamage = (float)p_43270_ + p_43269_.getAttackDamageBonus();
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", this.attackDamage, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", p_43271_, AttributeModifier.Operation.ADDITION));
        injectExtraAttributes(builder);
        this.defaultModifiers = builder.build();
    }

    public float getDamage() {
        return this.attackDamage;
    }

    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot p_43274_) {
        return p_43274_ == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(p_43274_);
    }

    @Override
    public void injectExtraAttributes(ImmutableMultimap.Builder<Attribute, AttributeModifier> builder) {

    }
}
