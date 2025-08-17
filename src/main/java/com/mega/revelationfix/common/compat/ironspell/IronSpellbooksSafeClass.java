package com.mega.revelationfix.common.compat.ironspell;

import com.mega.revelationfix.common.compat.SafeClass;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardAttackGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraftforge.common.MinecraftForge;

public class IronSpellbooksSafeClass {
    public static void onEventsRegister() {
        if (SafeClass.isIronSpellbookslLoaded()) {
            MinecraftForge.EVENT_BUS.register(IronSpellbooksEvents.class);
        }
    }

    public static boolean isSpellGoal(Goal goal) {
        return goal instanceof WizardAttackGoal;
    }
}
