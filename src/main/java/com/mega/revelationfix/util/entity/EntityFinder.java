package com.mega.revelationfix.util.entity;

import com.Polarice3.Goety.api.entities.IOwned;
import com.Polarice3.Goety.utils.SEHelper;
import com.mega.revelationfix.common.entity.binding.FakeSpellerEntity;
import com.mega.revelationfix.safe.entity.EntityCondition;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

public class EntityFinder {
    public static EntityCondition ALL = ((entity, iterator) -> true);
    public static EntityCondition STRICT_ALLIED = EntityFinder::isAlliedTo;
    public static EntityCondition STRICT_NOT_ALLIED = STRICT_ALLIED.opposite();
    public static EntityCondition STRICT_ALLIED_NE = EntityFinder::isAlliedNotEquals;
    public static EntityCondition STRICT_NOT_ALLIED_NE = EntityFinder::isNotAlliedNotEquals;
    @Nullable
    public static <T extends LivingEntity> T getNearestEntity(Level level, Class<? extends T> aClass, Predicate<Entity> selector, @Nullable LivingEntity entity, double originX, double originY, double originZ, AABB aabb) {
        return EntityFinder.getNearestEntity(level, aClass, selector, ALL, entity, originX, originY, originZ, aabb);
    }
    @Nullable
    public static <T extends LivingEntity> T getNearestEntity(Level level, Class<? extends T> aClass, Predicate<Entity> selector, EntityCondition condition, @Nullable LivingEntity entity, double originX, double originY, double originZ, AABB aabb) {
        return EntityFinder.getNearestEntity(level, level.getEntitiesOfClass(aClass, aabb, selector), condition, entity, originX, originY, originZ);
    }
    @Nullable
    public static  <T extends LivingEntity> T getNearestEntity(Level level, List<? extends T> p_45983_, EntityCondition p_45984_, @Nullable LivingEntity p_45985_, double p_45986_, double p_45987_, double p_45988_) {
        double d0 = -1.0D;
        T t = null;

        for(T t1 : p_45983_) {
            if (t1.isAlive() && !t1.isRemoved() && !(t1 instanceof FakeSpellerEntity)) {
                if (p_45984_.test(p_45985_, t1)) {
                    double d1 = t1.distanceToSqr(p_45986_, p_45987_, p_45988_);
                    if (d0 == -1.0D || d1 < d0) {
                        d0 = d1;
                        t = t1;
                    }
                }
            }
        }

        return t;
    }
    public static boolean isAlliedNotEquals(LivingEntity caster, Entity entity) {
        return caster != entity && entity.isAlive() && isAlliedTo(caster, entity);
    }
    public static boolean isNotAlliedNotEquals(LivingEntity caster, Entity entity) {
        return caster != entity && entity.isAlive() && !isAlliedTo(caster, entity);
    }
    public static boolean isAlliedTo(LivingEntity caster, Entity entityIn) {
        boolean flag = false;
        if (caster instanceof IOwned owned)
            caster = owned.getTrueOwner();
         if (!caster.isAlliedTo(entityIn) && !entityIn.isAlliedTo(caster) && entityIn != caster) {
            label55:
            {
                if (entityIn instanceof IOwned owned) {

                    if (owned.getTrueOwner() == caster || owned.getMasterOwner() == caster) {
                        flag = true;
                        break label55;
                    }
                }

                if (entityIn instanceof OwnableEntity ownable) {
                    if (ownable.getOwner() == caster) {
                        flag = true;
                        break label55;
                    }
                }

                if (caster instanceof Player player) {
                    if (entityIn instanceof LivingEntity livingEntity) {
                        if (SEHelper.getAllyEntities(player).contains(livingEntity) || SEHelper.getAllyEntityTypes(player).contains(livingEntity.getType())) {
                            flag = true;
                        }
                    }

                }
            }


        } else flag = true;

        return flag;
    }
}
