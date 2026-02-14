package com.mega.revelationfix.common.apollyon.common;

import com.Polarice3.Goety.common.entities.projectiles.DeathArrow;
import com.mega.endinglib.util.mc.entity.DamageSourceGenerator;
import com.mega.revelationfix.safe.DamageSourceInterface;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;

public class BypassInvulArrow {
    public static final String TAG_NAME = "apollyon_doom_arrow";
    public static final String TAG_BYPASS_NAME = "apollyon_2phase_arrow";

    public static boolean doomDeathArrow(AbstractArrow arrow, Entity instance, DamageSource damageSource, float damage) {
        if (arrow instanceof DeathArrow deathArrow) {
            if (damageSource.typeHolder().is(DamageTypes.ARROW)) {
                Entity owner = arrow.getOwner();
                if (owner instanceof LivingEntity livingOwner) {
                    ((DamageSourceInterface) damageSource).giveSpecialTag((byte) 4);
                    damageSource = new DamageSourceGenerator(livingOwner).source(ExtraDamageTypes.ARROW, damageSource.getEntity() == null ? owner : damageSource.getEntity());
                    damage = Math.max(damage, 999.0F);
                }
            }
        }
        return instance.hurt(damageSource, damage);
    }

    public static boolean phase2Arrow(AbstractArrow arrow, Entity instance, DamageSource damageSource, float damage) {
        if (arrow instanceof DeathArrow deathArrow) {
            if (damageSource.typeHolder().is(DamageTypes.ARROW)) {
                Entity owner = arrow.getOwner();
                if (owner instanceof LivingEntity livingOwner) {
                    damageSource = new DamageSourceGenerator(livingOwner).source(ExtraDamageTypes.ARROW, arrow.getOwner());
                    ((DamageSourceInterface) damageSource).giveSpecialTag((byte) 5);
                }
            }
        }
        return instance.hurt(damageSource, damage);
    }
}
