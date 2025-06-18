package com.mega.revelationfix.common.item.armor;

import com.Polarice3.Goety.api.items.ISoulRepair;
import com.Polarice3.Goety.api.items.armor.ISoulDiscount;
import com.google.common.collect.ImmutableMultimap;
import com.mega.revelationfix.api.item.IExtraAttributesInjector;
import com.mega.revelationfix.common.apollyon.common.RevelationRarity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.UUID;
import java.util.function.Supplier;

public class SpectreArmor extends BaseArmorItem implements ISoulRepair, ISoulDiscount {
    public static final AttributeModifier ARMOR_ATTACK_DAMAGE_MODIFIER = new AttributeModifier(UUID.fromString("8ec494ac-5668-44be-af1f-3672b1c8caf7"), "ArmorSet modifier", 2, AttributeModifier.Operation.ADDITION);
    public SpectreArmor(Type p_40387_) {
        super(ModArmorMaterials.SPECTRE, p_40387_, new Properties().rarity(RevelationRarity.SPECTRE), ImmutableMultimap.Builder::new);
    }
    public SpectreArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_, Supplier<ImmutableMultimap.Builder<Attribute, AttributeModifier>> extraAttributes) {
        super(p_40386_, p_266831_, p_40388_, extraAttributes);
    }
    @Override
    public int getSoulDiscount(EquipmentSlot equipmentSlot) {
        return 4;
    }

    @Override
    public void onArmorTick(Level level, LivingEntity living, ItemStack itemStack, Type type) {
    }
}
