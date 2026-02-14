package com.mega.revelationfix.common.item.tool.combat.whip;

import com.Polarice3.Goety.api.items.ISoulRepair;
import com.Polarice3.Goety.common.effects.GoetyEffects;
import com.Polarice3.Goety.common.entities.ModEntityType;
import com.Polarice3.Goety.common.entities.neutral.Wartling;
import com.Polarice3.Goety.utils.MathHelper;
import com.mega.endinglib.util.mc.entity.armor.ArmorUtils;
import com.mega.revelationfix.api.item.combat.ICustomHurtWeapon;
import com.mega.revelationfix.common.apollyon.common.RevelationRarity;
import com.mega.revelationfix.common.event.handler.ArmorEvents;
import com.mega.revelationfix.common.item.armor.ModArmorMaterials;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import org.jetbrains.annotations.NotNull;

public class VenomousSpiderWhipItem extends BaseWhipItem implements ISoulRepair, ICustomHurtWeapon {
    public VenomousSpiderWhipItem(float attackDamageAddition, float attackSpeedAddition) {
        super(Tiers.DIAMOND, attackDamageAddition, attackSpeedAddition, 1.0F, new Properties().rarity(RevelationRarity.SPIDER));
    }

    public static void warlockUse(Level level, Player player) {
        if (level instanceof ServerLevel serverLevel) {
            Wartling wartling = new Wartling(ModEntityType.WARTLING.get(), level);
            wartling.setTrueOwner(player);
            wartling.setLimitedLife(MathHelper.secondsToTicks(9));
            wartling.moveTo(player.blockPosition(), player.getYRot(), player.getXRot());
            player.getActiveEffects().stream().filter((mobEffect) -> mobEffect.getEffect().getCategory() == MobEffectCategory.HARMFUL && !mobEffect.getEffect().getCurativeItems().isEmpty()).findFirst().ifPresent((effect) -> {
                wartling.setStoredEffect(effect);
                player.removeEffect(effect.getEffect());
            });
            wartling.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(player.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
            serverLevel.addFreshEntity(wartling);
        }

        if (!player.isSilent()) {
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SCULK_BLOCK_SPREAD, player.getSoundSource(), 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
        }
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack itemStack, @NotNull LivingEntity entity, @NotNull LivingEntity player) {
        boolean set = ArmorEvents.isSpiderSet(ArmorUtils.getArmorSet(player));
        if (set) {
            if (entity.getRandom().nextFloat() < 0.35F)
                entity.addEffect(new MobEffectInstance(GoetyEffects.ACID_VENOM.get(), 80, 4));
        } else {
            if (entity.getRandom().nextFloat() < 0.25F)
                entity.addEffect(new MobEffectInstance(GoetyEffects.ACID_VENOM.get(), 60, 3));
        }
        return super.hurtEnemy(itemStack, entity, player);
    }

    @Override
    public void onAttack(ItemStack itemStack, LivingAttackEvent event) {
        if (event.getEntity() instanceof Player player && !player.level().isClientSide) {
            if (!player.getCooldowns().isOnCooldown(this)) {
                warlockUse(player.level(), player);
                player.getCooldowns().addCooldown(this, ArmorUtils.armorSet(player, ModArmorMaterials.SPIDER_DARKMAGE) ? 20 : 40);
            }
        }
    }
}
