package com.mega.revelationfix.mixin.goety;

import com.Polarice3.Goety.common.entities.projectiles.CorruptedBeam;
import com.Polarice3.Goety.common.magic.SpellStat;
import com.Polarice3.Goety.common.magic.spells.CorruptedBeamSpell;
import com.Polarice3.Goety.utils.WandUtil;
import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.mega.revelationfix.util.EventUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(CorruptedBeam.class)
public abstract class CorruptedBeamMixin {
    @Redirect(
            method = "damageEntities",
            at = @At(value = "INVOKE", target = "Lcom/Polarice3/Goety/utils/WandUtil;getLevels(Lnet/minecraft/world/item/enchantment/Enchantment;Lnet/minecraft/world/entity/LivingEntity;)I"),
            remap = false
    )
    private int potencyDamage(Enchantment enchantment, LivingEntity livingEntity) {
        if (livingEntity.level() instanceof ServerLevel serverLevel) {
            CorruptedBeamSpell spell = new CorruptedBeamSpell();
            SpellStat spellStat = spell.defaultStats();
            spellStat.setPotency(spellStat.getPotency() + WandUtil.getLevels(enchantment, livingEntity));
            EventUtil.modifySpellStatsWithoutEnchantment(spell, serverLevel, livingEntity, livingEntity.getUseItem(), spellStat);
            return spellStat.getPotency();
        }
        return WandUtil.getLevels(enchantment, livingEntity);
    }
}
