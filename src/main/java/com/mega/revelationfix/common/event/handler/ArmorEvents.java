package com.mega.revelationfix.common.event.handler;

import com.Polarice3.Goety.init.ModTags;
import com.Polarice3.Goety.utils.ItemHelper;
import com.mega.revelationfix.Revelationfix;
import com.mega.revelationfix.common.event.StandOnFluidEvent;
import com.mega.revelationfix.common.init.GRItems;
import com.mega.revelationfix.common.item.armor.ModArmorMaterials;
import com.mega.revelationfix.safe.OdamanePlayerExpandedContext;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = Revelationfix.MODID)
public class ArmorEvents {
    @SubscribeEvent
    public static void PlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        Level world = player.level;
        if (event.phase == TickEvent.Phase.END) {
            //神金胸头衔效果
            if (player.tickCount % 5 == 0) {
                if (findChestplate(player, GRItems.A_CHESTPLATE)) {
                    int title = getApocalyptiumTitleId(player);

                    if (title == 1)
                        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 0));
                    else if (title == 2)
                        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 1));
                }
            }
            //神金套回血
            if (ItemHelper.armorSet(player, ModArmorMaterials.APOCALYPTIUM)) {
                if (player.tickCount % 20 == 0 && player.getHealth() > 0.0F) {
                    player.setHealth(player.getHealth() + 2.0F);
                    net.minecraftforge.event.ForgeEventFactory.onLivingHeal(player, 2.0F);
                    //player.heal();
                }
            }
        }
    }

    @SubscribeEvent
    public static void PotionApplicationEvents(MobEffectEvent.Applicable event) {
        MobEffectInstance instance = event.getEffectInstance();
        //神金头免疫
        if (instance.getEffect() == MobEffects.BLINDNESS) {
            if (event.getEntity() instanceof Player player) {
                if (ItemHelper.findHelmet(player, GRItems.A_HELMET)) {
                    event.setResult(Event.Result.DENY);
                }
            }
        } else if (instance.getEffect() == MobEffects.DARKNESS) {
            if (event.getEntity() instanceof Player player) {
                if (ItemHelper.findHelmet(player, GRItems.A_HELMET)) {
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
        if (beHurt instanceof Player player) {
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
            if (ItemHelper.armorSet(player, ModArmorMaterials.APOCALYPTIUM)) {
                if (damageSource.is(DamageTypeTags.IS_EXPLOSION) || isMagicDamage(damageSource) || isFire(damageSource))
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

    public static boolean findLeggings(Player player, Item item) {
        return player.getItemBySlot(EquipmentSlot.LEGS).getItem() == item;
    }

    public static boolean findChestplate(Player player, Item item) {
        return player.getItemBySlot(EquipmentSlot.CHEST).getItem() == item;
    }

    public static boolean findBoots(Player player, Item item) {
        return player.getItemBySlot(EquipmentSlot.FEET).getItem() == item;
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
}
