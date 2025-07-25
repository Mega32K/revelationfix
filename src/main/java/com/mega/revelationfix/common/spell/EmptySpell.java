package com.mega.revelationfix.common.spell;

import com.Polarice3.Goety.common.magic.Spell;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import org.jetbrains.annotations.Nullable;

public class EmptySpell extends Spell {
    @Override
    public int defaultSoulCost() {
        return -1;
    }

    @Override
    public int defaultCastDuration() {
        return -1;
    }

    @Nullable
    @Override
    public SoundEvent CastingSound() {
        return SoundEvents.EMPTY;
    }

    @Override
    public int defaultSpellCooldown() {
        return 0;
    }

}
