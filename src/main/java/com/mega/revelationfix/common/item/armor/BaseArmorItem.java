package com.mega.revelationfix.common.item.armor;

import com.Polarice3.Goety.api.items.armor.ISoulDiscount;
import com.mega.endinglib.api.client.cmc.CuriosMutableComponent;
import com.mega.endinglib.api.item.IDamageLimitItem;
import com.mega.endinglib.api.item.armor.ModifiableArmorItem;
import com.mega.endinglib.api.item.armor.OptionArmorMaterial;
import com.mega.revelationfix.api.item.armor.IGoetyDamageResistanceArmor;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.List;
import java.util.UUID;

public abstract class BaseArmorItem extends ModifiableArmorItem implements IGoetyDamageResistanceArmor {
    public static final float MAGIC_DAMAGE_DIV = 25.0F;
    public static final float HOT_DIV = 10.0F;
    public static final EquipmentSlot[] EQUIPMENT_SLOTS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
    public static final AttributeModifier ATTACK_DAMAGE_MODIFIER = new AttributeModifier(UUID.fromString("5f90fe78-8c3a-4c1c-a3f7-ea61fa77425b"), "ArmorSet Modifier", 0.1F, AttributeModifier.Operation.MULTIPLY_TOTAL);
    public static final AttributeModifier SPELL_POWER_MODIFIER = new AttributeModifier(UUID.fromString("390f1cd0-7f0d-452d-842f-fae0c3867161"), "Armor modifier", 0.1, AttributeModifier.Operation.MULTIPLY_TOTAL);
    protected static final EnumMap<Type, UUID> EXTRA_MODIFIER_UUID_PER_TYPE = Util.make(new EnumMap<>(ArmorItem.Type.class), (p_266744_) -> {
        p_266744_.put(ArmorItem.Type.BOOTS, UUID.fromString("7c3fcaa9-ca78-402c-b204-10e2dc351421"));
        p_266744_.put(ArmorItem.Type.LEGGINGS, UUID.fromString("9cc067f2-daf0-4a1a-b550-4c28f8140dcd"));
        p_266744_.put(ArmorItem.Type.CHESTPLATE, UUID.fromString("c7a19bf9-d069-450d-a199-0f2540026696"));
        p_266744_.put(ArmorItem.Type.HELMET, UUID.fromString("3b222938-b3e8-4faf-abae-10f731d4c34c"));
    });
    protected static final EnumMap<Type, UUID> EXTRA_MODIFIER_UUID_PER_TYPE_2 = Util.make(new EnumMap<>(ArmorItem.Type.class), (p_266744_) -> {
        p_266744_.put(ArmorItem.Type.BOOTS, UUID.fromString("390f1cd0-7f0d-452d-842f-fae0c3867161"));
        p_266744_.put(ArmorItem.Type.LEGGINGS, UUID.fromString("a3e8cfad-445d-4e46-9c04-8427f8dd2878"));
        p_266744_.put(ArmorItem.Type.CHESTPLATE, UUID.fromString("5d7f0ab9-3465-4a9a-8c54-0eb19bc28d9d"));
        p_266744_.put(ArmorItem.Type.HELMET, UUID.fromString("ed7a2ece-574a-4971-a495-0ef680e6cd37"));
    });

    public BaseArmorItem(OptionArmorMaterial optionArmorMaterial, Type armorType, Properties itemProperties) {
        super(optionArmorMaterial, armorType, itemProperties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @javax.annotation.Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (this instanceof ISoulDiscount soulDiscount) {
            tooltip.add(soulDiscount.soulDiscountTooltip(stack));
        }
        if (this instanceof IDamageLimitItem damageLimitItem) {
            tooltip.add(damageLimitItem.damageLimitTooltip(stack));
        }
    }

    @Override
    public boolean hasSimpleDescription() {
        return true;
    }

    @Override
    public void addSimpleDescription(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<CuriosMutableComponent> components, @NotNull TooltipFlag tooltipFlag) {
        components.addAll(this.damageResistanceTooltipCMC(this, itemStack));
    }
}
