package com.mega.revelationfix.common.compat.ironspell;

import com.Polarice3.Goety.common.entities.boss.Vizier;
import com.Polarice3.Goety.common.entities.hostile.Wraith;
import com.Polarice3.Goety.common.entities.hostile.cultists.Warlock;
import com.Polarice3.Goety.common.entities.hostile.illagers.Sorcerer;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardAttackGoal;
import io.redspace.ironsspellbooks.entity.mobs.necromancer.NecromancerEntity;
import net.minecraft.world.entity.ai.goal.Goal;

public class IronSpellbooksSafeClass {
    public static boolean isSpellGoal(Goal goal) {
        if (goal instanceof WizardAttackGoal)
            return true;
        return false;
    }
}
