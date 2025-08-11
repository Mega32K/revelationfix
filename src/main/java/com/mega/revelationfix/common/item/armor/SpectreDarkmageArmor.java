package com.mega.revelationfix.common.item.armor;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mega.endinglib.api.client.cmc.CuriosMutableComponent;
import com.mega.endinglib.api.client.cmc.LoreStyle;
import com.mega.endinglib.api.item.IDamageLimitItem;
import com.mega.endinglib.api.item.IDragonLightRendererItem;
import com.mega.endinglib.util.entity.armor.ArmorModifiersBuilder;
import com.mega.endinglib.util.entity.armor.ArmorUtils;
import com.mega.revelationfix.client.model.entity.SpectreDarkmageHatModel;
import com.mega.revelationfix.client.model.entity.SpiderArmorModel;
import com.mega.revelationfix.common.apollyon.common.RevelationRarity;
import com.mega.revelationfix.common.init.ModAttributes;
import com.mega.revelationfix.common.item.FontItemExtensions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import z1gned.goetyrevelation.ModMain;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class SpectreDarkmageArmor extends SpectreArmor implements IDragonLightRendererItem, IDamageLimitItem {
    public SpectreDarkmageArmor(Type p_40387_) {
        super(ModArmorMaterials.SPECTRE_DARKMAGE, p_40387_, new Properties().rarity(RevelationRarity.SPECTRE));
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new FontItemExtensions() {
            private SpectreDarkmageHatModel model;
            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                if (equipmentSlot == EquipmentSlot.HEAD) {
                    if (this.model == null) {
                        EntityModelSet modelSet = Minecraft.getInstance().getEntityModels();
                        ModelPart root = modelSet.bakeLayer(SpectreDarkmageHatModel.LAYER_LOCATION);
                        this.model = new SpectreDarkmageHatModel(root);
                    }
                    this.model.hat.copyFrom(original.head);
                    return this.model;
                }
                return super.getHumanoidArmorModel(livingEntity, itemStack, equipmentSlot, original);
            }
        });
    }

    @Override
    public int getSoulDiscount(EquipmentSlot equipmentSlot) {
        return super.getSoulDiscount(equipmentSlot) + 2;
    }

    @Override
    public boolean enableDragonLightRenderer(ItemStack stack) {
        return true;
    }

    @Override
    public int getUseDamageLimit(ItemStack stack) {
        return 20;
    }

    @Override
    public void injectExtraArmorAttributes(ArmorModifiersBuilder builder) {
        UUID uuid = EXTRA_MODIFIER_UUID_PER_TYPE.get(type);
        builder.addModifier(ModAttributes.SPELL_POWER.get(), new AttributeModifier(uuid, "Armor modifier", 0.35, AttributeModifier.Operation.ADDITION));
        builder.addModifier(ModAttributes.SPELL_POWER.get(), new AttributeModifier(UUID.fromString("02a1113b-07b4-4e15-a23f-7485c054a3c3"), "Armor modifier", 0.1, AttributeModifier.Operation.MULTIPLY_TOTAL));
        builder.addModifier(ModAttributes.SPELL_POWER_MULTIPLIER.get(), new AttributeModifier(uuid, "Armor modifier", .15, AttributeModifier.Operation.ADDITION));
        builder.addModifier(ModAttributes.CAST_DURATION.get(), new AttributeModifier(uuid, "Armor modifier", .1, AttributeModifier.Operation.ADDITION));
        builder.addModifier(ModAttributes.SPELL_COOLDOWN.get(), new AttributeModifier(uuid, "Armor modifier", .1, AttributeModifier.Operation.ADDITION));
    }

    @Override
    public void onArmorSetLivingAttack(LivingAttackEvent event) {
        //黑魔法师套全套免疫爆炸火焰
        DamageSource damageSource = event.getSource();
        if (damageSource.is(DamageTypeTags.IS_EXPLOSION) || ArmorUtils.isFire(damageSource))
            event.setCanceled(true);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getSetAttributesModifiers(LivingEntity living) {
        return ImmutableMultimap.of(Attributes.ATTACK_DAMAGE, SpectreArmor.ARMOR_ATTACK_DAMAGE_MODIFIER, Attributes.ATTACK_DAMAGE, BaseArmorItem.ATTACK_DAMAGE_MODIFIER);
    }

    @Override
    public void addSetDescription(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<CuriosMutableComponent> components, @NotNull TooltipFlag tooltipFlag) {
        components.add(CuriosMutableComponent.create(Component.translatable("item.goety_revelation.darkmage_set.desc0"), LoreStyle.INDENTATION_ATTRIBUTE_PREFIX));
        super.addSetDescription(itemStack, level, components, tooltipFlag);
        components.add(CuriosMutableComponent.create(LoreStyle.INDENTATION_ATTRIBUTE_PREFIX).appendAttributeFormat(1, new CuriosMutableComponent.AttributeDescFunction2("attribute.name.generic.attack_damage", (s)-> BaseArmorItem.ATTACK_DAMAGE_MODIFIER.getAmount() * 100.0F)));
    }

    @Override
    public void addSimpleDescription(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<CuriosMutableComponent> components, @NotNull TooltipFlag tooltipFlag) {
        super.addSimpleDescription(itemStack, level, components, tooltipFlag);
    }

    @Override
    public @Nullable String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        if (slot == EquipmentSlot.HEAD) {
            return ModMain.MODID + ":textures/models/armor/spectre_darkmage_hat.png";
        }
        return super.getArmorTexture(stack, entity, slot, type);
    }
}
