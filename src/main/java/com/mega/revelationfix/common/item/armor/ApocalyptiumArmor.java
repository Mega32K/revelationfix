package com.mega.revelationfix.common.item.armor;

import com.Polarice3.Goety.api.items.ISoulRepair;
import com.Polarice3.Goety.api.items.armor.ISoulDiscount;
import com.mega.endinglib.api.client.cmc.CuriosMutableComponent;
import com.mega.endinglib.api.client.cmc.LoreHelper;
import com.mega.endinglib.api.client.cmc.LoreStyle;
import com.mega.endinglib.api.item.IDamageLimitItem;
import com.mega.endinglib.api.item.IInvulnerableItem;
import com.mega.endinglib.api.item.armor.ModifiableArmorItem;
import com.mega.endinglib.util.entity.armor.ArmorUtils;
import com.mega.revelationfix.api.item.armor.IGoetyDamageResistanceArmor;
import com.mega.revelationfix.common.init.GRItems;
import com.mega.revelationfix.safe.OdamanePlayerExpandedContext;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class ApocalyptiumArmor extends ModifiableArmorItem implements IGoetyDamageResistanceArmor, ISoulRepair, ISoulDiscount, IDamageLimitItem, IInvulnerableItem {

    public ApocalyptiumArmor(Type armorType) {
        super(ModArmorMaterials.APOCALYPTIUM, armorType, new Properties().fireResistant().rarity(Rarity.UNCOMMON));
    }

    public int getSoulDiscount(EquipmentSlot equipmentSlot) {
        return 16;
    }

    @Override
    public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        /*
        consumer.accept(new IClientItemExtensions() {
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                EntityModelSet modelSet = Minecraft.getInstance().getEntityModels();
                ModelPart root = modelSet.bakeLayer(equipmentSlot == EquipmentSlot.LEGS ? ModModelLayer.DARK_ARMOR_INNER : ModModelLayer.DARK_ARMOR_OUTER);
                DarkArmorModel model = new DarkArmorModel(root).animate(livingEntity);
                model.hat.visible = equipmentSlot == EquipmentSlot.HEAD;
                model.body.visible = equipmentSlot == EquipmentSlot.CHEST;
                model.rightArm.visible = equipmentSlot == EquipmentSlot.CHEST;
                model.leftArm.visible = equipmentSlot == EquipmentSlot.CHEST;
                model.bottom.visible = equipmentSlot == EquipmentSlot.LEGS;
                model.rightLeg.visible = equipmentSlot == EquipmentSlot.FEET;
                model.leftLeg.visible = equipmentSlot == EquipmentSlot.FEET;

                if (livingEntity instanceof AbstractClientPlayer player){
                    if (player.isCapeLoaded() && player.isModelPartShown(PlayerModelPart.CAPE) && player.getCloakTextureLocation() != null){
                        model.cape.visible = false;
                    }
                }

                model.young = original.young;
                model.crouching = original.crouching;
                model.riding = original.riding;
                model.rightArmPose = original.rightArmPose;
                model.leftArmPose = original.leftArmPose;

                return model;
            }
        });
         */
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(this.soulDiscountTooltip(stack));
        tooltip.add(this.damageLimitTooltip(stack));
    }

    @Override
    public void onArmorTick(Level level, LivingEntity living, ItemStack itemStack, Type type) {
        if (type == Type.CHESTPLATE) {
            if (!level.isClientSide) {
                //神金胸头衔效果
                if (living.tickCount % 5 == 0) {
                    int title = getApocalyptiumTitleId(living);
                    if (title == 1)
                        living.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 0, false, false));
                    else if (title == 2)
                        living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 1, false, false));
                }
            }
        }
    }

    @Override
    public void when4SetTick(LivingEntity living, Level level) {
        //神金套回血
        if (living.tickCount % 20 == 0 && living.getHealth() > 0.0F) {
            living.setHealth(living.getHealth() + 2.0F);
            net.minecraftforge.event.ForgeEventFactory.onLivingHeal(living, 2.0F);
            //player.heal();
        }
    }

    @Override
    public boolean immuneEffects(LivingEntity living, MobEffectInstance mobEffect) {
        if (this.type == Type.HELMET) {
            MobEffect effect = mobEffect.getEffect();
            return effect == MobEffects.BLINDNESS || effect == MobEffects.DARKNESS || effect == MobEffects.CONFUSION;
        } else if (this.type == Type.LEGGINGS) {
            MobEffect effect = mobEffect.getEffect();
            return effect == MobEffects.MOVEMENT_SLOWDOWN;
        }
        return false;
    }

    @Override
    public void onLivingAttack(LivingAttackEvent event, ItemStack armorStack) {
        LivingEntity entity = event.getEntity();
        DamageSource damageSource = event.getSource();
        //神金护腿提供50％的弹射物反射
        if (this.type == Type.LEGGINGS) {
            if (entity.getRandom().nextBoolean()) {
                if (damageSource.is(DamageTypes.MOB_PROJECTILE) || damageSource.getDirectEntity() instanceof Projectile) {
                    event.setCanceled(true);
                    if (damageSource.getDirectEntity() instanceof Projectile projectile) {
                        projectile.hurtMarked = true;
                        projectile.setDeltaMovement(projectile.getDeltaMovement().scale(2f));
                    }
                }
            }
            //神金鞋取消摔落伤害
        } else if (type == Type.BOOTS) {
            if (damageSource.is(DamageTypeTags.IS_FALL))
                event.setCanceled(true);
        }
    }

    @Override
    public void onArmorSetLivingAttack(LivingAttackEvent event) {
        //神金全套穿戴时提供完全的火焰免疫，爆炸免疫，魔法免疫
        DamageSource damageSource = event.getSource();
        if (damageSource.is(DamageTypeTags.IS_EXPLOSION) || ArmorUtils.isMagicDamage(damageSource) || ArmorUtils.isFire(damageSource))
            event.setCanceled(true);
    }

    @Override
    public void onLivingDeath(LivingDeathEvent event, ItemStack armorStack) {
        if (type == Type.CHESTPLATE) {
            LivingEntity entity = event.getEntity();
            if (entity.getRandom().nextFloat() < 0.15F) {
                event.setCanceled(true);
                entity.setHealth(1F);
                entity.heal(7.0F);
                entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 40, 126));
                entity.level().broadcastEntityEvent(entity, OdamanePlayerExpandedContext.REVIVE_EVENT);
            }
        }
    }

    public static int getApocalyptiumTitleId(LivingEntity living) {
        return (living.tickCount / 100) % 3;
    }
    @Override
    public int getUseDamageLimit(ItemStack stack) {
        return 20;
    }

    @Override
    public boolean hasSetDescription() {
        return true;
    }

    @Override
    public boolean hasSimpleDescription() {
        return true;
    }

    @Override
    public void addSimpleDescription(@NotNull ItemStack itemStack, @org.jetbrains.annotations.Nullable Level level, @NotNull List<CuriosMutableComponent> components, @NotNull TooltipFlag tooltipFlag) {
        switch (type) {
            case BOOTS -> {
                components.add(CuriosMutableComponent.create(Component.translatable("item.goety_revelation.apocalyptium_boots.desc0"), LoreStyle.INDENTATION_ATTRIBUTE_PREFIX));
                components.add(CuriosMutableComponent.create(Component.translatable("item.goety_revelation.apocalyptium_boots.desc1") , LoreStyle.INDENTATION_ATTRIBUTE_PREFIX));
            }
            case HELMET -> {
                components.add(CuriosMutableComponent.create(Component.translatable("item.goety_revelation.apocalyptium_helmet.desc"), LoreStyle.INDENTATION_ATTRIBUTE_PREFIX));
            }
            case LEGGINGS -> {
                components.add(CuriosMutableComponent.create(Component.translatable("item.goety_revelation.apocalyptium_leggings.desc0"), LoreStyle.INDENTATION_ATTRIBUTE_PREFIX));
                components.add(CuriosMutableComponent.create(Component.translatable("item.goety_revelation.apocalyptium_leggings.desc1"), LoreStyle.INDENTATION_ATTRIBUTE_PREFIX));

            }
            case CHESTPLATE -> {
                components.add(CuriosMutableComponent.create(Component.translatable("item.goety_revelation.apocalyptium_chestplate.desc0"), LoreStyle.INDENTATION_ATTRIBUTE_PREFIX));
                components.add(CuriosMutableComponent.create(Component.translatable("item.goety_revelation.apocalyptium_chestplate.desc1"), LoreStyle.INDENTATION_ATTRIBUTE_PREFIX));
            }
        }
        components.addAll(this.damageResistanceTooltipCMC(this, itemStack));
    }

    @Override
    public void addSetDescription(@NotNull ItemStack itemStack, @org.jetbrains.annotations.Nullable Level level, @NotNull List<CuriosMutableComponent> components, @NotNull TooltipFlag tooltipFlag) {
        components.add(CuriosMutableComponent.create(Component.translatable("item.goety_revelation.apocalyptium_set.desc0"), LoreStyle.INDENTATION_ATTRIBUTE_PREFIX));
        components.add(CuriosMutableComponent.create(Component.translatable("item.goety_revelation.apocalyptium_set.desc1"), LoreStyle.INDENTATION_ATTRIBUTE_PREFIX));
        components.add(CuriosMutableComponent.create(Component.translatable("item.goety_revelation.apocalyptium_set.desc2"), LoreStyle.INDENTATION_ATTRIBUTE_PREFIX));
        super.addSetDescription(itemStack, level, components, tooltipFlag);
    }
}