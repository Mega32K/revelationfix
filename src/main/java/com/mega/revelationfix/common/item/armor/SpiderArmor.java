package com.mega.revelationfix.common.item.armor;

import com.Polarice3.Goety.api.items.ISoulRepair;
import com.Polarice3.Goety.api.items.armor.ISoulDiscount;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.mega.revelationfix.common.apollyon.common.RevelationRarity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class SpiderArmor extends BaseArmorItem implements ISoulRepair, ISoulDiscount {
    public SpiderArmor(ArmorItem.Type p_40387_) {
        super(ModArmorMaterials.SPIDER, p_40387_, new Properties().rarity(RevelationRarity.SPIDER), ImmutableListMultimap.Builder::new);
    }
    public SpiderArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_, Supplier<ImmutableMultimap.Builder<Attribute, AttributeModifier>> extraAttributes) {
        super(p_40386_, p_266831_, p_40388_, extraAttributes);
    }

    @Override
    public int getSoulDiscount(EquipmentSlot equipmentSlot) {
        return 4;
    }

    @Override
    public void onArmorTick(Level level, LivingEntity living, ItemStack itemStack, Type type) {
        if (!level.isClientSide) {
            if (type == Type.HELMET)
                living.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 1360, 0));
        }
    }
}
