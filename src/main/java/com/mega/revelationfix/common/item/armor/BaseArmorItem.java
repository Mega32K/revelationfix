package com.mega.revelationfix.common.item.armor;

import com.Polarice3.Goety.api.items.armor.ISoulDiscount;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mega.revelationfix.api.item.IExtraAttributesInjector;
import com.mega.revelationfix.client.font.effect.LoreHelper;
import com.mega.revelationfix.common.item.curios.CuriosBaseItem;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public abstract class BaseArmorItem extends ArmorItem implements IExtraAttributesInjector {
    public static final AttributeModifier ATTACK_DAMAGE_MODIFIER = new AttributeModifier(UUID.fromString("5f90fe78-8c3a-4c1c-a3f7-ea61fa77425b"), "ArmorSet Modifier", 0.1F, AttributeModifier.Operation.MULTIPLY_TOTAL);
    protected static final EnumMap<Type, UUID> EXTRA_MODIFIER_UUID_PER_TYPE = Util.make(new EnumMap<>(ArmorItem.Type.class), (p_266744_) -> {
        p_266744_.put(ArmorItem.Type.BOOTS, UUID.fromString("7c3fcaa9-ca78-402c-b204-10e2dc351421"));
        p_266744_.put(ArmorItem.Type.LEGGINGS, UUID.fromString("9cc067f2-daf0-4a1a-b550-4c28f8140dcd"));
        p_266744_.put(ArmorItem.Type.CHESTPLATE, UUID.fromString("c7a19bf9-d069-450d-a199-0f2540026696"));
        p_266744_.put(ArmorItem.Type.HELMET, UUID.fromString("3b222938-b3e8-4faf-abae-10f731d4c34c"));
    });
    private final Multimap<Attribute, AttributeModifier> allModifiers;
    public BaseArmorItem(ArmorMaterial armorMaterial, Type type, Properties p_40388_, Supplier<ImmutableMultimap.Builder<Attribute, AttributeModifier>> extraAttributes) {
        super(armorMaterial, type, p_40388_);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(this.defaultModifiers);
        builder.putAll(extraAttributes.get().build());
        injectExtraAttributes(armorMaterial, type, builder);
        allModifiers = builder.build();
    }
    public abstract void onArmorTick(Level level, LivingEntity living, ItemStack itemStack, Type type);

    @Override
    public @Nullable String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {

        return super.getArmorTexture(stack, entity, slot, type);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack itemStack, @NotNull Level p_41405_, @NotNull Entity p_41406_, int p_41407_, boolean p_41408_) {
        if (p_41406_ instanceof LivingEntity living) {
            if (living instanceof Player player) {
                Inventory inventory = player.getInventory();
                switch (type) {
                    case HELMET -> {
                        if (inventory.armor.get(EquipmentSlot.HEAD.getIndex()).equals(itemStack, true))
                            onArmorTick(p_41405_, player, itemStack, Type.HELMET);
                    }
                    case CHESTPLATE -> {
                        if (inventory.armor.get(EquipmentSlot.CHEST.getIndex()).equals(itemStack, true))
                            onArmorTick(p_41405_, player, itemStack, Type.CHESTPLATE);
                    }
                    case LEGGINGS -> {
                        if (inventory.armor.get(EquipmentSlot.LEGS.getIndex()).equals(itemStack, true))
                            onArmorTick(p_41405_, player, itemStack, Type.LEGGINGS);
                    }
                    case BOOTS -> {
                        if (inventory.armor.get(EquipmentSlot.FEET.getIndex()).equals(itemStack, true))
                            onArmorTick(p_41405_, player, itemStack, Type.BOOTS);
                    }
                }
            } else {
                switch (type) {
                    case HELMET -> {
                        if (living.getItemBySlot(EquipmentSlot.HEAD).equals(itemStack, true))
                            onArmorTick(p_41405_, living, itemStack, Type.HELMET);
                    }
                    case CHESTPLATE -> {
                        if (living.getItemBySlot(EquipmentSlot.CHEST).equals(itemStack, true))
                            onArmorTick(p_41405_, living, itemStack, Type.CHESTPLATE);
                    }
                    case LEGGINGS -> {
                        if (living.getItemBySlot(EquipmentSlot.LEGS).equals(itemStack, true))
                            onArmorTick(p_41405_, living, itemStack, Type.LEGGINGS);
                    }
                    case BOOTS -> {
                        if (living.getItemBySlot(EquipmentSlot.FEET).equals(itemStack, true))
                            onArmorTick(p_41405_, living, itemStack, Type.BOOTS);
                    }
                }
            }
        }
    }
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @javax.annotation.Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (hasShiftDescription()) {
            if (LoreHelper.hasShiftDown())
                tooltip.add(Component.translatable("tooltip.revelationfix.holdShiftEffect"));
            else appendHoverEffectText(stack, worldIn, tooltip, flagIn);
        }
        if (this instanceof ISoulDiscount soulDiscount) {
            tooltip.add(soulDiscount.soulDiscountTooltip(stack));
        }
    }
    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot equipmentSlot) {
        return equipmentSlot == this.type.getSlot() ? this.allModifiers : super.getDefaultAttributeModifiers(equipmentSlot);
    }
    @Override
    public void injectExtraAttributes(ArmorMaterial armorMaterial, Type type, ImmutableMultimap.Builder<Attribute, AttributeModifier> builder) {

    }
    protected boolean hasShiftDescription() {
        return false;
    }
    protected void appendHoverEffectText(@NotNull ItemStack stack, @javax.annotation.Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {

    }
}
