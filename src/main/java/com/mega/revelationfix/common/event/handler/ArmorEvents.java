package com.mega.revelationfix.common.event.handler;

import com.Polarice3.Goety.common.entities.ModEntityType;
import com.Polarice3.Goety.common.entities.projectiles.SpiderWeb;
import com.Polarice3.Goety.init.ModTags;
import com.Polarice3.Goety.utils.ItemHelper;
import com.Polarice3.Goety.utils.MathHelper;
import com.mega.revelationfix.Revelationfix;
import com.mega.revelationfix.api.event.entity.LivingHurtByTargetGoalEvent;
import com.mega.revelationfix.api.event.entity.StandOnFluidEvent;
import com.mega.revelationfix.common.init.GRItems;
import com.mega.revelationfix.common.item.armor.BaseArmorItem;
import com.mega.revelationfix.common.item.armor.ModArmorMaterials;
import com.mega.revelationfix.common.item.armor.SpectreArmor;
import com.mega.revelationfix.safe.OdamanePlayerExpandedContext;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = Revelationfix.MODID)
public class ArmorEvents {
    @SubscribeEvent
    public static void hurtByTargetCanUse(LivingHurtByTargetGoalEvent.CanUse event) {
        if (event.getEventPhase() == LivingHurtByTargetGoalEvent.CanUse.Phase.TAIL) {
            if (event.getGoalMob() instanceof Spider && event.getEntity() != null) {
                if (ArmorEvents.getArmorSet(event.getEntity()) == ModArmorMaterials.SPIDER_DARKMAGE)
                    event.setResult(Event.Result.DENY);
            }
        }
    }
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void goetyArmorDamageReduce(LivingDamageEvent event) {
        LivingEntity target = event.getEntity();
        float damageAmount = event.getAmount();
        float totalReduce = 0;
        for (EquipmentSlot equipmentSlot : BaseArmorItem.EQUIPMENT_SLOTS){
            if (target.getItemBySlot(equipmentSlot).getItem() instanceof ArmorItem armorItem){
                if (isDarkmageSet(armorItem.getMaterial())) {
                    float reducedDamage = getReducedDamage(event, armorItem);
                    totalReduce += reducedDamage;
                }
            }
        }
        if (totalReduce > 0) {
            damageAmount -= totalReduce;
            damageAmount = Math.max(0, damageAmount);
            event.setAmount(damageAmount);
        }
    }
    @SubscribeEvent
    public static void livingHurtArmorEffects(LivingHurtEvent event) {
        /*
         if (event.getSource().getEntity() instanceof LivingEntity living) {
            //幽魂套加5伤害
            if (armorSet(living, ModArmorMaterials.SPECTRE) || armorSet(living, ModArmorMaterials.SPECTRE_DARKMAGE))
                event.setAmount(event.getAmount() + 5.0F);
        }
         */
        //蜘蛛套蛛网反制
        LivingEntity beHurt = event.getEntity();
        DamageSource damageSource = event.getSource();
        ArmorMaterial currentSet = getArmorSet(beHurt);
        if (isSpiderSet(currentSet)) {
            if (damageSource.getEntity() instanceof LivingEntity living && living.random.nextFloat() <= 0.3F && living.distanceTo(beHurt) <= 8.01D) {
                SpiderWeb spiderWeb = new SpiderWeb(ModEntityType.SPIDER_WEB.get(), beHurt.level);
                spiderWeb.setOwner(beHurt);
                spiderWeb.setLifeSpan(MathHelper.secondsToTicks(3));
                spiderWeb.setPos(living.position());
                beHurt.level.addFreshEntity(spiderWeb);
            }
        }
        //黑魔法师套减伤
        if (isDarkmageSet(currentSet))
            if (isMagicDamage(damageSource))
                event.setAmount(event.getAmount() * 0.8F);
        //神经蚀刻套吸血
        if (currentSet == ModArmorMaterials.SPIDER_DARKMAGE)
            if (beHurt.random.nextFloat() <= 0.2F) {
                float f0 = 2F;
                float f1 = 2F;
                AttributeInstance instance = beHurt.getAttribute(Attributes.ATTACK_DAMAGE);
                if (instance != null)
                    f1 = (float) instance.getValue() * 0.1F;
                beHurt.heal(Math.max(f0, f1));
            }
    }
    @SubscribeEvent
    public static void playerArmorTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        Level world = player.level;
        if (event.phase == TickEvent.Phase.END) {
            if (!player.level.isClientSide) {
            //神金胸头衔效果
            if (player.tickCount % 5 == 0) {
                if (findChestplate(player, GRItems.A_CHESTPLATE)) {
                    int title = getApocalyptiumTitleId(player);

                    if (title == 1)
                        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 0, false, false));
                    else if (title == 2)
                        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 1, false, false));
                }
            }
            ArmorMaterial currentSet = getArmorSet(player);
            //神金套回血
            if (currentSet == ModArmorMaterials.APOCALYPTIUM) {
                if (player.tickCount % 20 == 0 && player.getHealth() > 0.0F) {
                    player.setHealth(player.getHealth() + 2.0F);
                    net.minecraftforge.event.ForgeEventFactory.onLivingHeal(player, 2.0F);
                    //player.heal();
                }
            }

                AttributeInstance attackDamage = player.getAttribute(Attributes.ATTACK_DAMAGE);
                if (attackDamage != null) {
                    boolean changed = false;
                    if (currentSet == ModArmorMaterials.SPECTRE || currentSet == ModArmorMaterials.SPECTRE_DARKMAGE) {
                        if (!attackDamage.hasModifier(SpectreArmor.ARMOR_ATTACK_DAMAGE_MODIFIER)) {
                            attackDamage.addModifier(SpectreArmor.ARMOR_ATTACK_DAMAGE_MODIFIER);
                            changed = true;
                        }
                    } else {
                        if (attackDamage.hasModifier(SpectreArmor.ARMOR_ATTACK_DAMAGE_MODIFIER)) {
                            attackDamage.removeModifier(SpectreArmor.ARMOR_ATTACK_DAMAGE_MODIFIER);
                            changed = true;
                        }
                    }
                    if (isDarkmageSet(currentSet)) {
                        if (!attackDamage.hasModifier(BaseArmorItem.ATTACK_DAMAGE_MODIFIER)) {
                            attackDamage.addModifier(BaseArmorItem.ATTACK_DAMAGE_MODIFIER);
                            changed = true;
                        }
                    } else {
                        if (attackDamage.hasModifier(BaseArmorItem.ATTACK_DAMAGE_MODIFIER)) {
                            attackDamage.removeModifier(BaseArmorItem.ATTACK_DAMAGE_MODIFIER);
                            changed = true;
                        }
                    }
                    if (changed) {
                        attackDamage.setDirty();
                        player.attributes.getDirtyAttributes().add(player.getAttribute(Attributes.ATTACK_DAMAGE));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void PotionApplicationEvents(MobEffectEvent.Applicable event) {
        MobEffectInstance instance = event.getEffectInstance();
        //免疫
        if (instance.getEffect() == MobEffects.BLINDNESS) {
            if (event.getEntity() instanceof Player player) {
                if (ItemHelper.findHelmet(player, GRItems.A_HELMET) || findHelmet(player, ModArmorMaterials.SPIDER) || findHelmet(player, ModArmorMaterials.SPIDER_DARKMAGE) || findHelmet(player, ModArmorMaterials.SPECTRE) || findHelmet(player, ModArmorMaterials.SPECTRE_DARKMAGE)) {
                    event.setResult(Event.Result.DENY);
                }
            }
        } else if (instance.getEffect() == MobEffects.DARKNESS) {
            if (event.getEntity() instanceof Player player) {
                if (ItemHelper.findHelmet(player, GRItems.A_HELMET) || findHelmet(player, ModArmorMaterials.SPECTRE) || findHelmet(player, ModArmorMaterials.SPECTRE_DARKMAGE)) {
                    event.setResult(Event.Result.DENY);
                }
            }
        } else if (instance.getEffect() == MobEffects.CONFUSION) {
            if (event.getEntity() instanceof Player player) {
                if (ItemHelper.findHelmet(player, GRItems.A_HELMET)) {
                    event.setResult(Event.Result.DENY);
                }
            }
        } else
            //神金护腿免疫缓慢
            if (instance.getEffect() == MobEffects.MOVEMENT_SLOWDOWN) {
                if (event.getEntity() instanceof Player player) {
                    if (findLeggings(player, GRItems.A_LEGGINGS)) {
                        event.setResult(Event.Result.DENY);
                    }
                }
            }
    }

    @SubscribeEvent
    public static void livingAttack(LivingAttackEvent event) {
        LivingEntity beHurt = event.getEntity();
        DamageSource damageSource = event.getSource();
        if (beHurt.level.isClientSide) return;
        if (beHurt instanceof Player player) {ArmorMaterial currentSet = getArmorSet(player);
            //神金护腿提供50％的弹射物反射
            if (player.random.nextBoolean()) {
                if (findLeggings(player, GRItems.A_LEGGINGS)) {
                    if (damageSource.is(DamageTypes.MOB_PROJECTILE) || damageSource.getDirectEntity() instanceof Projectile) {
                        event.setCanceled(true);
                        if (damageSource.getDirectEntity() instanceof Projectile projectile) {
                            projectile.hurtMarked = true;
                            projectile.setDeltaMovement(projectile.getDeltaMovement().scale(2f));
                        }
                    }
                }
            }
            //神金鞋取消摔落伤害
            if (findBoots(player, GRItems.A_BOOTS)) {
                if (damageSource.is(DamageTypeTags.IS_FALL))
                    event.setCanceled(true);
            }

            //神金全套穿戴时提供完全的火焰免疫，爆炸免疫，魔法免疫
            if (currentSet == ModArmorMaterials.APOCALYPTIUM) {
                if (damageSource.is(DamageTypeTags.IS_EXPLOSION) || isMagicDamage(damageSource) || isFire(damageSource))
                    event.setCanceled(true);
            } else if (isDarkmageSet(currentSet)) {
                if (damageSource.is(DamageTypeTags.IS_EXPLOSION) || isFire(damageSource))
                    event.setCanceled(true);
            }

            //蜘蛛鞋免疫摔落伤害
            if (findBoots(player, ModArmorMaterials.SPIDER) || findBoots(player, ModArmorMaterials.SPIDER_DARKMAGE)) {
                if (damageSource.is(DamageTypeTags.IS_FALL))
                    event.setCanceled(true);
            }


            //幽魂胸甲免疫音波伤害
            if (findChestplate(player, ModArmorMaterials.SPECTRE) || findChestplate(player, ModArmorMaterials.SPECTRE_DARKMAGE)) {
                if (damageSource.is(DamageTypes.SONIC_BOOM))
                    event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player) {
            //神金胸穿戴时提供15％免死
            if (player.random.nextFloat() < 0.15F) {
                if (findChestplate(player, GRItems.A_CHESTPLATE)) {
                    event.setCanceled(true);
                    player.setHealth(1F);
                    player.heal(7.0F);
                    player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 40, 126));
                    player.level.broadcastEntityEvent(player, OdamanePlayerExpandedContext.REVIVE_EVENT);
                }
            }
        }
    }

    @SubscribeEvent
    public static void standOnFluidEventEvent(StandOnFluidEvent event) {
        if (event.getEntity() instanceof Player player && findBoots(player, GRItems.A_BOOTS) &&
                !event.getEntity().isShiftKeyDown() && (event.getFluidState().is(Fluids.LAVA) || event.getFluidState().is(Fluids.FLOWING_LAVA))) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void livingHeal(LivingHealEvent event) {
        //神金胸头衔不灭重生
        if (event.getEntity() instanceof Player player) {
            if (getApocalyptiumTitleId(player) == 0 && findChestplate(player, GRItems.A_CHESTPLATE))
                event.setAmount(event.getAmount() * 2F);
        }
    }

    public static int getApocalyptiumTitleId(Player player) {
        return (player.tickCount / 100) % 3;
    }

    public static Component getTitle(int index) {
        if (index == 0)
            return Component.translatable("title.goety.0");
        else if (index == 1)
            return Component.translatable("title.goety.10");
        else return Component.translatable("title.goety.9");
    }

    public static boolean isFire(DamageSource source) {
        if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY))
            return false;
        if (source.is(DamageTypeTags.IS_FIRE))
            return true;
        return source.is(DamageTypes.FIREBALL) || source.is(DamageTypes.IN_FIRE) || source.is(DamageTypes.ON_FIRE) || source.is(DamageTypes.LAVA) || source.is(ModTags.DamageTypes.FIRE_ATTACKS) || source.is(ModTags.DamageTypes.MAGIC_FIRE) || source.is(ModTags.DamageTypes.HELLFIRE);
    }

    public static boolean isMagicDamage(DamageSource source) {
        if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY))
            return false;
        return source.is(DamageTypes.MAGIC) || source.is(DamageTypes.INDIRECT_MAGIC) || source.is(DamageTypes.DRAGON_BREATH) || source.is(DamageTypeTags.WITCH_RESISTANT_TO);
    }
    public static boolean isMagicArmor(ArmorMaterial material) {
        return material instanceof ModArmorMaterials;
    }
    public static boolean isDarkmageSet(ArmorMaterial material) {
        return material == ModArmorMaterials.SPECTRE_DARKMAGE || material == ModArmorMaterials.SPIDER_DARKMAGE;
    }
    public static boolean isSpiderSet(ArmorMaterial material) {
        return material == ModArmorMaterials.SPIDER || material == ModArmorMaterials.SPIDER_DARKMAGE;
    }
    public static boolean isSpectreSet(ArmorMaterial material) {
        return material == ModArmorMaterials.SPECTRE || material == ModArmorMaterials.SPECTRE_DARKMAGE;
    }
    public static boolean findHelmet(Player player, Item item) {
        return player.getItemBySlot(EquipmentSlot.HEAD).getItem() == item;
    }
    public static boolean findLeggings(Player player, Item item) {
        return player.getItemBySlot(EquipmentSlot.LEGS).getItem() == item;
    }

    public static boolean findChestplate(LivingEntity player, Item item) {
        return player.getItemBySlot(EquipmentSlot.CHEST).getItem() == item;
    }

    public static boolean findBoots(LivingEntity player, Item item) {
        return player.getItemBySlot(EquipmentSlot.FEET).getItem() == item;
    }
    public static boolean findHelmet(LivingEntity player, ArmorMaterial material) {
        return player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof ArmorItem armorItem && armorItem.getMaterial().equals(material);
    }
    public static boolean findLeggings(LivingEntity player, ArmorMaterial material) {
        return player.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof ArmorItem armorItem && armorItem.getMaterial().equals(material);
    }

    public static boolean findChestplate(LivingEntity player, ArmorMaterial material) {
        return player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof ArmorItem armorItem && armorItem.getMaterial().equals(material);
    }

    public static boolean findBoots(LivingEntity player, ArmorMaterial material) {
        return player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof ArmorItem armorItem && armorItem.getMaterial().equals(material);
    }
    public static boolean armorSet(LivingEntity living, ArmorMaterial material) {
        return ItemHelper.armorSet(living, material);
    }
    public static int armorSetCount(LivingEntity living, ArmorMaterial material) {
        int i = 0;
        Item firstCheck = living.getItemBySlot(EquipmentSlot.HEAD).getItem();
        if (firstCheck instanceof ArmorItem helmet) {
            if (helmet.getMaterial() == material) {
                EquipmentSlot[] var10 = EquipmentSlot.values();
                int var5 = var10.length;

                for (EquipmentSlot equipmentSlot : var10) {
                    if (equipmentSlot.getType() == EquipmentSlot.Type.ARMOR) {
                        Item item = living.getItemBySlot(equipmentSlot).getItem();
                        if (item instanceof ArmorItem armorItem) {
                            if (armorItem.getMaterial() == material) {
                                ++i;
                            }
                        }
                    }
                }
            }
        }

        return i;
    }
    public static @Nullable ArmorMaterial getArmorSet(LivingEntity living) {
        int i = 0;
        Item firstCheck = living.getItemBySlot(EquipmentSlot.HEAD).getItem();
        ArmorMaterial material = null;
        if (firstCheck instanceof ArmorItem helmet) {
            material = helmet.getMaterial();
            EquipmentSlot[] var10 = EquipmentSlot.values();
            int var5 = var10.length;

            for (EquipmentSlot equipmentSlot : var10) {
                if (equipmentSlot.getType() == EquipmentSlot.Type.ARMOR) {
                    Item item = living.getItemBySlot(equipmentSlot).getItem();
                    if (item instanceof ArmorItem armorItem) {
                        if (armorItem.getMaterial() == material) {
                            ++i;
                        }
                    }
                }
            }
        }

        return i >= 4 ? material : null;
    }
    /**
     * Goety同款魔法 火焰 熔岩减伤
     */
    private static float getReducedDamage(LivingDamageEvent event, ArmorItem armorItem) {
        float reduction = 0;
        if (event.getSource().is(DamageTypeTags.WITCH_RESISTANT_TO)) {
            reduction = armorItem.getDefense() / BaseArmorItem.MAGIC_DAMAGE_DIV;
        } else if (event.getSource().is(DamageTypeTags.IS_FIRE) || event.getSource().is(DamageTypeTags.IS_EXPLOSION)) {
            reduction = armorItem.getDefense() / BaseArmorItem.HOT_DIV;
        }
        return event.getAmount() * reduction;
    }

}
