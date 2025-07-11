package com.mega.revelationfix.common.event.handler;

import com.mega.endinglib.util.entity.armor.ArmorUtils;
import com.mega.revelationfix.Revelationfix;
import com.mega.revelationfix.api.event.entity.LivingHurtByTargetGoalEvent;
import com.mega.revelationfix.api.event.entity.StandOnFluidEvent;
import com.mega.revelationfix.common.item.armor.BaseArmorItem;
import com.mega.revelationfix.common.item.armor.ModArmorMaterials;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = Revelationfix.MODID)
public class ArmorEvents {
    @SubscribeEvent
    public static void hurtByTargetCanUse(LivingHurtByTargetGoalEvent.CanUse event) {
        if (event.getEventPhase() == LivingHurtByTargetGoalEvent.CanUse.Phase.TAIL) {
            if (event.getGoalMob() instanceof Spider && event.getEntity() != null) {
                if (ArmorUtils.getArmorSet(event.getEntity()) == ModArmorMaterials.SPIDER_DARKMAGE)
                    event.setResult(Event.Result.DENY);
            }
        }
    }

    /**
     * 单独事件性能更佳
     */
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void goetyArmorDamageReduce(LivingDamageEvent event) {
        LivingEntity target = event.getEntity();
        float damageAmount = event.getAmount();
        float totalReduce = 0;
        for (EquipmentSlot equipmentSlot : BaseArmorItem.EQUIPMENT_SLOTS){
            if (target.getItemBySlot(equipmentSlot).getItem() instanceof ArmorItem armorItem){
                if (isMagicArmor(armorItem.getMaterial())) {
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
    public static void standOnFluidEventEvent(StandOnFluidEvent event) {
        if (event.getEntity() instanceof Player player && ArmorUtils.findBoots(player, ModArmorMaterials.APOCALYPTIUM) &&
                !event.getEntity().isShiftKeyDown()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void livingHeal(LivingHealEvent event) {
        //神金胸头衔不灭重生
        if (event.getEntity() instanceof Player player) {
            if (getApocalyptiumTitleId(player) == 0 && ArmorUtils.findChestplate(player, ModArmorMaterials.APOCALYPTIUM))
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
    public static float getDamageReduction(TagKey<DamageType> tagKey, ArmorItem armorItem) {
        float reduction = 0;
        if (tagKey.equals(DamageTypeTags.WITCH_RESISTANT_TO)) {
            reduction = armorItem.getDefense() / BaseArmorItem.MAGIC_DAMAGE_DIV;
        } else if (tagKey.equals(DamageTypeTags.IS_FIRE) || tagKey.equals(DamageTypeTags.IS_EXPLOSION)) {
            reduction = armorItem.getDefense() / BaseArmorItem.HOT_DIV;
        }
        return reduction;
    }
}
