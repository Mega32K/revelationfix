package com.mega.revelationfix.common.item.armor;

import com.Polarice3.Goety.api.items.ISoulRepair;
import com.Polarice3.Goety.api.items.armor.ISoulDiscount;
import com.Polarice3.Goety.common.entities.ModEntityType;
import com.Polarice3.Goety.common.entities.projectiles.SpiderWeb;
import com.Polarice3.Goety.utils.MathHelper;
import com.mega.endinglib.api.client.cmc.CuriosMutableComponent;
import com.mega.endinglib.api.client.cmc.LoreStyle;
import com.mega.endinglib.api.item.IModeToggleItem;
import com.mega.endinglib.api.item.armor.OptionArmorMaterial;
import com.mega.endinglib.util.mc.entity.armor.ArmorModifiersBuilder;
import com.mega.endinglib.util.mc.entity.armor.ArmorUtils;
import com.mega.revelationfix.client.font.effect.LoreHelper;
import com.mega.revelationfix.client.model.entity.SpiderArmorModel;
import com.mega.revelationfix.common.apollyon.common.RevelationRarity;
import com.mega.revelationfix.proxy.CommonProxy;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
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

public class SpiderArmor extends BaseArmorItem implements ISoulRepair, ISoulDiscount, IModeToggleItem {
    public SpiderArmor(ArmorItem.Type p_40387_) {
        super(ModArmorMaterials.SPIDER, p_40387_, new Properties().rarity(RevelationRarity.SPIDER));
    }

    public SpiderArmor(OptionArmorMaterial optionArmorMaterial, Type armorType, Properties itemProperties) {
        super(optionArmorMaterial, armorType, itemProperties);
    }

    @Override
    public @Nullable String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return ModMain.MODID + ":textures/models/armor/spider_armor_layer.png";
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private SpiderArmorModel model;

            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                EntityModelSet modelSet = Minecraft.getInstance().getEntityModels();
                ModelPart root = modelSet.bakeLayer(SpiderArmorModel.OUTER);
                if (this.model == null)
                    this.model = (new SpiderArmorModel(root));
                boolean leggings = ArmorUtils.findLeggings(livingEntity, ModArmorMaterials.SPIDER);
                boolean boots = ArmorUtils.findBoots(livingEntity, ModArmorMaterials.SPIDER);
                boolean chestplate = ArmorUtils.findChestplate(livingEntity, ModArmorMaterials.SPIDER);

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
    public int getSoulDiscount(EquipmentSlot equipmentSlot, ItemStack itemStack) {
        return 4;
    }
    @Override
    public void onArmorTick(Level level, LivingEntity living, ItemStack itemStack, Type type) {
        if (!level.isClientSide) {
            if (type == Type.HELMET)
                living.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 1360, 0, false, false));
        }
    }

    @Override
    public boolean immuneEffects(LivingEntity living, MobEffectInstance mobEffect) {
        if (this.type == Type.HELMET) {
            MobEffect effect = mobEffect.getEffect();
            return effect == MobEffects.BLINDNESS;
        }
        return false;
    }

    @Override
    public void onLivingAttack(LivingAttackEvent event, ItemStack armorStack) {
        if (this.type == Type.BOOTS) {
            DamageSource damageSource = event.getSource();
            if (damageSource.is(DamageTypeTags.IS_FALL))
                event.setCanceled(true);
        }
    }

    @Override
    public void onArmorSetLivingHurt(LivingHurtEvent event) {
        //蜘蛛套蛛网反制
        LivingEntity beHurt = event.getEntity();
        DamageSource damageSource = event.getSource();
        if (damageSource.getEntity() instanceof LivingEntity living && living.getRandom().nextFloat() <= 0.3F && living.distanceTo(beHurt) <= 8.01D) {
            SpiderWeb spiderWeb = new SpiderWeb(ModEntityType.SPIDER_WEB.get(), beHurt.level());
            spiderWeb.setOwner(beHurt);
            spiderWeb.setLifeSpan(MathHelper.secondsToTicks(3));
            spiderWeb.setPos(living.position());
            beHurt.level().addFreshEntity(spiderWeb);
        }
    }

    @Override
    public boolean hasSetDescription() {
        return true;
    }

    @Override
    public void addSetDescription(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<CuriosMutableComponent> components, @NotNull TooltipFlag tooltipFlag) {
        components.add(CuriosMutableComponent.create(Component.translatable("item.goety_revelation.spider_set.desc0"), LoreStyle.INDENTATION_ATTRIBUTE_PREFIX));
        components.add(CuriosMutableComponent.create(Component.translatable("item.goety_revelation.spider_set.desc1"), LoreStyle.INDENTATION_ATTRIBUTE_PREFIX));
        components.add(CuriosMutableComponent.create(Component.translatable("item.goety_revelation.spider_set.desc2"), LoreStyle.INDENTATION_ATTRIBUTE_PREFIX));
        if (!(this instanceof SpiderDarkmageArmor)) {
            components.add(CuriosMutableComponent.EMPTY);
            components.add(CuriosMutableComponent.create().appendFormat("%s", (s) -> new Object[]{I18n.get("tooltip.goety_revelation.armor.ability_skill_toggle", KeyMapping.createNameSupplier("key.revelationfix.armor_skill").get().getString().toUpperCase())}));
        }
    }

    @Override
    public void addSimpleDescription(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<CuriosMutableComponent> components, @NotNull TooltipFlag tooltipFlag) {
        switch (type) {
            case BOOTS -> {
                components.add(CuriosMutableComponent.create(Component.translatable("item.goety_revelation.spider_boots.desc0"), LoreStyle.INDENTATION_ATTRIBUTE_PREFIX));
            }
            case HELMET -> {
                components.add(CuriosMutableComponent.create(Component.translatable("item.goety_revelation.spider_helmet.desc0"), LoreStyle.INDENTATION_ATTRIBUTE_PREFIX));
                components.add(CuriosMutableComponent.create(Component.translatable("item.goety_revelation.spider_helmet.desc1"), LoreStyle.INDENTATION_ATTRIBUTE_PREFIX));
            }
        }
        super.addSimpleDescription(itemStack, level, components, tooltipFlag);
    }

    @Override
    public void injectExtraArmorAttributes(ArmorModifiersBuilder builder) {
        UUID uuid = EXTRA_MODIFIER_UUID_PER_TYPE.get(type);
        if (this.type == Type.BOOTS)
            builder.addModifier(ForgeMod.STEP_HEIGHT_ADDITION.get(), new AttributeModifier(uuid, "Armor Modifier", .5D, AttributeModifier.Operation.ADDITION));
    }

    @Override
    public void toggleMode(ServerPlayer serverPlayer, Item item) {
        CommonProxy.getPlayerCapOptional(serverPlayer).ifPresent(cap -> {
            cap.setArmorClimbing(!cap.isArmorClimbingMode());
            serverPlayer.sendSystemMessage(Component.translatable("message.goety_revelation.armor.climbing_mode", cap.isArmorClimbingMode() ? LoreHelper.TRUE().getString() : LoreHelper.FALSE().getString()), true);
        });
    }
}
