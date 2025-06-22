package com.mega.revelationfix.common.item.armor;

import com.google.common.collect.ImmutableMultimap;
import com.mega.endinglib.api.item.IDragonLightRendererItem;
import com.mega.revelationfix.api.item.IDamageLimitItem;
import com.mega.revelationfix.client.model.entity.SpiderDarkmageArmorModel;
import com.mega.revelationfix.common.apollyon.common.RevelationRarity;
import com.mega.revelationfix.common.event.handler.ArmorEvents;
import com.mega.revelationfix.common.init.ModAttributes;
import com.mega.revelationfix.common.item.FontItemExtensions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import z1gned.goetyrevelation.ModMain;

import java.util.UUID;
import java.util.function.Consumer;

public class SpiderDarkmageArmor extends SpiderArmor implements IDragonLightRendererItem, IDamageLimitItem {
    public SpiderDarkmageArmor(Type p_40387_) {
        super(ModArmorMaterials.SPIDER_DARKMAGE, p_40387_, new Properties().rarity(RevelationRarity.SPIDER), () -> {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
            UUID uuid = EXTRA_MODIFIER_UUID_PER_TYPE.get(p_40387_);
            builder.put(ModAttributes.SPELL_POWER.get(), new AttributeModifier(uuid, "Armor modifier", 0.35, AttributeModifier.Operation.ADDITION));
            builder.put(ModAttributes.SPELL_POWER_MULTIPLIER.get(), new AttributeModifier(uuid, "Armor modifier", .1, AttributeModifier.Operation.ADDITION));
            builder.put(ModAttributes.CAST_DURATION.get(), new AttributeModifier(uuid, "Armor modifier", .1, AttributeModifier.Operation.ADDITION));
            builder.put(ModAttributes.SPELL_COOLDOWN.get(), new AttributeModifier(uuid, "Armor modifier", .1, AttributeModifier.Operation.ADDITION));
            return builder;
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
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new FontItemExtensions() {
            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                EntityModelSet modelSet = Minecraft.getInstance().getEntityModels();
                ModelPart root = modelSet.bakeLayer(SpiderDarkmageArmorModel.OUTER);
                boolean leggings = ArmorEvents.findLeggings(livingEntity, ModArmorMaterials.SPIDER_DARKMAGE);
                boolean boots = ArmorEvents.findBoots(livingEntity, ModArmorMaterials.SPIDER_DARKMAGE);
                boolean chestplate = ArmorEvents.findChestplate(livingEntity, ModArmorMaterials.SPIDER_DARKMAGE);
                SpiderDarkmageArmorModel model = (new SpiderDarkmageArmorModel(root));
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
    public boolean enableDragonLightRenderer(ItemStack stack) {
        return true;
    }

    @Override
    public int getUseDamageLimit(ItemStack stack) {
        return 20;
    }
}
