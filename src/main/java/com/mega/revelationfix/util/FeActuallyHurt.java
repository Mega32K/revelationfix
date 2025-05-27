package com.mega.revelationfix.util;

import com.mega.uom.util.entity.EntityActuallyHurt;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public class FeActuallyHurt {
    public static void actuallyHurt(LivingEntity living, DamageSource source, float amount, boolean special) {
        EntityActuallyHurt entityActuallyHurt = new EntityActuallyHurt(living);
        entityActuallyHurt.actuallyHurt(source, amount, special);
        entityActuallyHurt = null;
    }
}
