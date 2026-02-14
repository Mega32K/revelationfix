package com.mega.revelationfix.common.item.tool.combat.whip;

import com.Polarice3.Goety.api.items.ISoulRepair;
import com.Polarice3.Goety.utils.SEHelper;
import com.mega.endinglib.util.mc.entity.DamageSourceGenerator;
import com.mega.endinglib.util.mc.entity.armor.ArmorUtils;
import com.mega.revelationfix.common.apollyon.common.RevelationRarity;
import com.mega.revelationfix.common.event.handler.ArmorEvents;
import com.mega.revelationfix.common.init.ModEffects;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import org.jetbrains.annotations.NotNull;

public class SpectreWhipItem extends BaseWhipItem implements ISoulRepair {
    public SpectreWhipItem(float attackDamageAddition, float attackSpeedAddition) {
        super(Tiers.DIAMOND, attackDamageAddition, attackSpeedAddition, 1.0F, new Properties().rarity(RevelationRarity.SPECTRE));
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack itemStack, @NotNull LivingEntity entity, @NotNull LivingEntity player) {
        if (player instanceof Player pE) {
            SEHelper.increaseSouls(pE, 1);
            if ((entity.getRandom().nextFloat() < 0.25F || ArmorEvents.isSpectreSet(ArmorUtils.getArmorSet(player))) && !pE.getCooldowns().isOnCooldown(this)) {
                int invul = entity.invulnerableTime;
                entity.invulnerableTime = 0;
                if (entity.hurt(new DamageSourceGenerator(entity).source(DamageTypes.SONIC_BOOM, player), 5.0F))
                    pE.level().levelEvent(232424314, entity.blockPosition(), 7);
                entity.invulnerableTime = invul;
                pE.getCooldowns().addCooldown(this, 10);

            }
        }
        if (entity.getRandom().nextFloat() < 0.25F)
            entity.addEffect(new MobEffectInstance(ModEffects.COUNTERSPELL.get(), 60, 3));

        return super.hurtEnemy(itemStack, entity, player);
    }
}
