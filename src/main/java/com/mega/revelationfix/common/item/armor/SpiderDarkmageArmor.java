package com.mega.revelationfix.common.item.armor;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mega.endinglib.api.client.cmc.CuriosMutableComponent;
import com.mega.endinglib.api.client.cmc.LoreStyle;
import com.mega.endinglib.api.item.IDamageLimitItem;
import com.mega.endinglib.api.item.IDragonLightRendererItem;
import com.mega.endinglib.util.entity.armor.ArmorModifiersBuilder;
import com.mega.endinglib.util.entity.armor.ArmorUtils;
import com.mega.revelationfix.client.model.entity.SpiderDarkmageArmorModel;
import com.mega.revelationfix.common.apollyon.common.RevelationRarity;
import com.mega.revelationfix.common.init.ModAttributes;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import z1gned.goetyrevelation.ModMain;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class SpiderDarkmageArmor extends SpiderArmor implements IDragonLightRendererItem, IDamageLimitItem {
    public SpiderDarkmageArmor(Type p_40387_) {
        super(ModArmorMaterials.SPIDER_DARKMAGE, p_40387_, new Properties().rarity(RevelationRarity.SPIDER));
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private SpiderDarkmageArmorModel model;

            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                EntityModelSet modelSet = Minecraft.getInstance().getEntityModels();
                ModelPart root = modelSet.bakeLayer(SpiderDarkmageArmorModel.OUTER);
                if (this.model == null)
                    this.model = new SpiderDarkmageArmorModel(root);
                boolean leggings = ArmorUtils.findLeggings(livingEntity, ModArmorMaterials.SPIDER_DARKMAGE);
                boolean boots = ArmorUtils.findBoots(livingEntity, ModArmorMaterials.SPIDER_DARKMAGE);
                boolean chestplate = ArmorUtils.findChestplate(livingEntity, ModArmorMaterials.SPIDER_DARKMAGE);
                model.setupAnim(livingEntity, 0, 0, livingEntity.tickCount, 0, 0);
                model.body_1.copyFrom(original.body);
                model.body_1.visible = chestplate;
                model.body_2.copyFrom(original.body);
                model.body_2.visible = leggings;
                model.left_leg_1.copyFrom(original.leftLeg);
                model.left_leg_1.visible = boots;
                model.right_leg_1.copyFrom(original.rightLeg);
                model.right_leg_1.visible = boots;
                model.left_leg_2.copyFrom(original.leftLeg);
                model.left_leg_2.visible = leggings;
                model.right_leg_2.copyFrom(original.rightLeg);
                model.right_leg_2.visible = leggings;
                return model;
            }
        });
    }

    @Override
    public @Nullable String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return ModMain.MODID + ":textures/models/armor/spider_darkmage_armor_layer.png";
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
        UUID uuid2 = EXTRA_MODIFIER_UUID_PER_TYPE_2.get(type);
        if (this.type == Type.BOOTS)
            builder.addModifier(ForgeMod.STEP_HEIGHT_ADDITION.get(), new AttributeModifier(uuid, "Armor Modifier", 1D, AttributeModifier.Operation.ADDITION));
        builder.addModifier(ModAttributes.SPELL_POWER.get(), new AttributeModifier(uuid, "Armor modifier", 0.35, AttributeModifier.Operation.ADDITION));
        builder.addModifier(ModAttributes.SPELL_POWER_MULTIPLIER.get(), new AttributeModifier(uuid, "Armor modifier", .15, AttributeModifier.Operation.ADDITION));
        builder.addModifier(ModAttributes.CAST_DURATION.get(), new AttributeModifier(uuid, "Armor modifier", .1, AttributeModifier.Operation.MULTIPLY_TOTAL));
        builder.addModifier(ModAttributes.SPELL_COOLDOWN.get(), new AttributeModifier(uuid, "Armor modifier", .1, AttributeModifier.Operation.MULTIPLY_TOTAL));
    }

    @Override
    public void onArmorSetLivingAttack(LivingAttackEvent event) {
        //黑魔法师套全套免疫爆炸火焰
        DamageSource damageSource = event.getSource();
        if (damageSource.is(DamageTypeTags.IS_EXPLOSION) || ArmorUtils.isFire(damageSource))
            event.setCanceled(true);
    }

    @Override
    public void onSetHurtOthers(LivingHurtEvent event, LivingEntity attacker, LivingEntity beHurt) {
        //神经蚀刻套吸血
        if (attacker.getRandom().nextFloat() <= 0.2F) {
            float f0 = 2F;
            float f1 = 2F;
            AttributeInstance instance = attacker.getAttribute(Attributes.ATTACK_DAMAGE);
            if (instance != null)
                f1 = (float) instance.getValue() * 0.1F;
            attacker.heal(Math.max(f0, f1));
            attacker.invulnerableTime++;
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getSetAttributesModifiers(LivingEntity living) {
        return ImmutableMultimap.of(Attributes.ATTACK_DAMAGE, BaseArmorItem.ATTACK_DAMAGE_MODIFIER, ModAttributes.SPELL_POWER.get(), BaseArmorItem.SPELL_POWER_MODIFIER);
    }

    @Override
    public boolean hasSetDescription() {
        return true;
    }

    @Override
    public void addSetDescription(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<CuriosMutableComponent> components, @NotNull TooltipFlag tooltipFlag) {
        components.add(CuriosMutableComponent.create(Component.translatable("item.goety_revelation.spider_darkmage_set.desc0"), LoreStyle.INDENTATION_ATTRIBUTE_PREFIX));
        super.addSetDescription(itemStack, level, components, tooltipFlag);
        components.add(CuriosMutableComponent.create(Component.translatable("item.goety_revelation.darkmage_set.desc0"), LoreStyle.INDENTATION_ATTRIBUTE_PREFIX));
        components.add(CuriosMutableComponent.create(LoreStyle.INDENTATION_ATTRIBUTE_PREFIX).appendAttributeFormat(1, new CuriosMutableComponent.AttributeDescFunction2("attribute.name.generic.attack_damage", (s) -> BaseArmorItem.ATTACK_DAMAGE_MODIFIER.getAmount() * 100.0F)));
        components.add(CuriosMutableComponent.create(LoreStyle.INDENTATION_ATTRIBUTE_PREFIX).appendAttributeFormat(1, new CuriosMutableComponent.AttributeDescFunction2("attribute.name." + ModMain.MODID + ".spell_power", (s) -> BaseArmorItem.SPELL_POWER_MODIFIER.getAmount() * 100.0F)));
        components.add(CuriosMutableComponent.EMPTY);
        components.add(CuriosMutableComponent.create().appendFormat("%s", (s) -> new Object[]{I18n.get("tooltip.goety_revelation.armor.ability_skill_toggle", KeyMapping.createNameSupplier("key.revelationfix.armor_skill").get().getString().toUpperCase())}));
    }
}
