package com.mega.revelationfix.common.spell.nether;

import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.magic.EverChargeSpell;
import com.Polarice3.Goety.init.ModSounds;
import com.mega.revelationfix.common.config.GRSpellConfig;
import com.mega.revelationfix.common.entity.RevelationCageEntity;
import com.mega.revelationfix.common.init.ModEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RevelationSpell extends EverChargeSpell {
    @Override
    public int defaultSoulCost() {
        return GRSpellConfig.RevelationCost.get();
    }

    @Nullable
    @Override
    public SoundEvent CastingSound() {
        return ModSounds.APOSTLE_PREPARE_SPELL.get();
    }

    @Override
    public int defaultSpellCooldown() {
        return GRSpellConfig.RevelationCoolDown.get();
    }

    @Override
    public void stopSpell(ServerLevel worldIn, LivingEntity caster, ItemStack staff, int useTimeRemaining) {
        playSound(worldIn, caster, SoundEvents.BEACON_DEACTIVATE);
    }

    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff) {
        List<RevelationCageEntity> entities = worldIn.getEntitiesOfClass(RevelationCageEntity.class, caster.getBoundingBox().inflate(2.0), (cageEntity) -> cageEntity.getOwner() == caster);

        if (entities.isEmpty()) {
            RevelationCageEntity cageEntity = new RevelationCageEntity(ModEntities.REVELATION_CAGE_ENTITY.get(), worldIn, caster);
            cageEntity.moveTo(caster.getX(), caster.getY(0.5), caster.getZ());
            cageEntity.setOwner(caster);
            cageEntity.setItemBase(true);
            worldIn.addFreshEntity(cageEntity);
        }

    }

    @Override
    public SpellType getSpellType() {
        return SpellType.NETHER;
    }
}
