package com.mega.revelationfix.common.entity.boss;

import com.Polarice3.Goety.init.ModSounds;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.PillagerRenderer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.SpellcasterIllager;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class IceHermitEntity extends SpellcasterIllager {
    public IceHermitEntity(EntityType<? extends SpellcasterIllager> p_33724_, Level p_33725_) {
        super(p_33724_, p_33725_);
    }

    @Override
    public @NotNull SoundEvent getCastingSoundEvent() {
        return SoundEvents.PLAYER_HURT_FREEZE;
    }

    @Override
    public void applyRaidBuffs(int p_37844_, boolean p_37845_) {

    }

    @Override
    public @NotNull SoundEvent getCelebrateSound() {
        return ModSounds.CRYOLOGER_CELEBRATE.get();
    }

    @Override
    public @NotNull IllagerArmPose getArmPose() {
        if (this.isCastingSpell()) {
            return AbstractIllager.IllagerArmPose.SPELLCASTING;
        } else {
            return this.isCelebrating() ? AbstractIllager.IllagerArmPose.CELEBRATING : IllagerArmPose.NEUTRAL;
        }
    }
}
