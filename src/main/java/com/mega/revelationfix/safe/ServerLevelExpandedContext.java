package com.mega.revelationfix.safe;

import com.mega.revelationfix.util.time.TimeContext;
import com.mega.revelationfix.util.time.TimeStopEntityData;
import com.mega.revelationfix.util.time.TimeStopUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BooleanSupplier;

public class ServerLevelExpandedContext extends LevelExpandedContext {
    public ServerLevelExpandedContext(Level level) {
        super(level);
    }

    private ServerExpandedContext serverEC() {
        return ((ServerEC) ((ServerLevel) level).server).uom$serverECData();
    }

    @Override
    public void tickHead(BooleanSupplier booleanSupplier, CallbackInfo ci) {
        if (TimeStopUtils.isTimeStop) {
            ServerLevel serverLevel = (ServerLevel) level;
            boolean can = serverEC().timeStopDimensions.contains(serverLevel.dimension());
            if (can) {
                ProfilerFiller profilerfiller = serverLevel.getProfiler();
                if (TimeContext.Both.timeStopModifyMillis % 10000 == 0) {
                    AtomicBoolean has = new AtomicBoolean(false);
                    for (LivingEntity living : serverLevel.getEntitiesOfClass(LivingEntity.class, new AABB(new BlockPos(0, 0, 0)).inflate(30000000))) {
                        if (TimeStopEntityData.getTimeStopCount(living) > 0 && living.isAlive()) {
                            has.set(true);
                            break;
                        }
                    }
                    if (!has.get()) {
                        Player player = serverLevel.getRandomPlayer();
                        if (player != null) {
                            TimeStopUtils.use(false, player, true);
                        } else {
                            TimeStopUtils.isTimeStop = false;
                        }
                    }
                }
                profilerfiller.push("entities");
                serverLevel.entityTickList.forEach((p_184065_) -> {
                    if (!p_184065_.isRemoved()) {
                        if (serverLevel.shouldDiscardEntity(p_184065_)) {
                            p_184065_.discard();
                        } else {
                            profilerfiller.push("checkDespawn");
                            p_184065_.checkDespawn();
                            profilerfiller.pop();
                            if (serverLevel.chunkSource.chunkMap.getDistanceManager().inEntityTickingRange(p_184065_.chunkPosition().toLong())) {
                                Entity entity = p_184065_.getVehicle();
                                if (entity != null) {
                                    if (!entity.isRemoved() && entity.hasPassenger(p_184065_)) {
                                        return;
                                    }

                                    p_184065_.stopRiding();
                                }
                                profilerfiller.push("tick");
                                if (!p_184065_.isRemoved() && TimeStopUtils.canMove(p_184065_)) {
                                    serverLevel.guardEntityTick(serverLevel::tickNonPassenger, p_184065_);
                                }
                                profilerfiller.pop();
                            }
                        }
                    }
                });


                profilerfiller.popPush("chunkSource");
                serverLevel.getChunkSource().tick(booleanSupplier, true);
                profilerfiller.pop();
                profilerfiller.push("entityManagement");
                serverLevel.entityManager.tick();
                profilerfiller.pop();
                ci.cancel();
            }
        }
    }
}
