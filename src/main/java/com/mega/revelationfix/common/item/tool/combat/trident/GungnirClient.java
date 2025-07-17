package com.mega.revelationfix.common.item.tool.combat.trident;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.*;

public class GungnirClient {
    public static Entity selectEntity = null;

    public static void handTick(Player src, ItemStack stack) {
        if (src != null) {
            if (src.isShiftKeyDown()) {
                src.level().getProfiler().push("pick");
                float partial = 1F;
                double d0 = 128;
                double d1;
                double entityReach = 128;
                HitResult hitResult = src.pick(entityReach, partial, false);
                Vec3 vec3 = src.getEyePosition(partial);
                boolean flag = false;
                int i = 3;
                {

                }
                d1 = d0;// Pick entities with the max of both for the same reason.

                d1 *= d1;
                // If we picked a block, we need to ignore entities past that block. Added != MISS check to not truncate on failed picks.
                // Also fixes MC-250858
                if (hitResult != null && hitResult.getType() != HitResult.Type.MISS) {
                    d1 = hitResult.getLocation().distanceToSqr(vec3);
                    double blockReach = src.getBlockReach();
                    // Discard the hitResult as a miss if it is outside the block reach.
                    if (d1 > blockReach * blockReach) {
                        Vec3 pos = hitResult.getLocation();
                        hitResult = BlockHitResult.miss(pos, Direction.getNearest(vec3.x, vec3.y, vec3.z), BlockPos.containing(pos));
                    }
                }

                Vec3 vec31 = src.getViewVector(1.0F);
                Vec3 vec32 = vec3.add(vec31.x * d0, vec31.y * d0, vec31.z * d0);
                float f = 1.0F;
                AABB aabb = src.getBoundingBox().expandTowards(vec31.scale(d0)).inflate(1.0D, 1.0D, 1.0D);
                EntityHitResult entityhitresult = ProjectileUtil.getEntityHitResult(src, vec3, vec32, aabb, (p_234237_) -> !p_234237_.isSpectator() && p_234237_.isPickable(), d1);
                if (entityhitresult != null) {
                    Entity entity1 = entityhitresult.getEntity();
                    Vec3 vec33 = entityhitresult.getLocation();
                    double d2 = vec3.distanceToSqr(vec33);
                    if (d2 > d1 || d2 > entityReach * entityReach) {
                    } else if (d2 < d1 || hitResult == null) {
                        if (entity1 instanceof LivingEntity || entity1 instanceof ItemFrame) {
                            selectEntity = entity1;
                            if (!src.level().isClientSide)
                                stack.getOrCreateTag().putInt("TargetID", entity1.getId());
                        }
                    }
                }
                src.level().getProfiler().pop();
            }
        }
    }
}
