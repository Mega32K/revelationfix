package com.mega.revelationfix.common.compat.ironspell;

import com.Polarice3.Goety.common.entities.boss.Vizier;
import com.Polarice3.Goety.common.entities.hostile.Wraith;
import com.Polarice3.Goety.common.entities.hostile.cultists.Warlock;
import com.Polarice3.Goety.common.entities.hostile.illagers.Sorcerer;
import com.mega.revelationfix.common.compat.SafeClass;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardAttackGoal;
import io.redspace.ironsspellbooks.entity.mobs.necromancer.NecromancerEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraftforge.common.MinecraftForge;

public class IronSpellbooksSafeClass {
    public static void onEventsRegister() {
        if (SafeClass.isIronSpellbookslLoaded()) {
            MinecraftForge.EVENT_BUS.register(IronSpellbooksEvents.class);
        }
    }
    public static boolean isSpellGoal(Goal goal) {
        if (goal instanceof WizardAttackGoal)
            return true;
        return false;
    }
}
