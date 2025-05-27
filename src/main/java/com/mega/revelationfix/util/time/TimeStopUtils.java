package com.mega.revelationfix.util.time;

import com.mega.revelationfix.common.data.TimeStopSavedData;
import com.mega.revelationfix.common.init.ModSounds;
import com.mega.revelationfix.common.network.PacketHandler;
import com.mega.revelationfix.common.network.s2c.timestop.TSDimensionSynchedPacket;
import com.mega.revelationfix.common.network.s2c.timestop.TimeStopSkillPacket;
import com.mega.revelationfix.safe.ClientLevelExpandedContext;
import com.mega.revelationfix.safe.LevelEC;
import com.mega.revelationfix.safe.ServerEC;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

/**
 * 节省性能，在客户端判断{@link TimeStopUtils#andSameDimension(Level)}请直接调用预备好的boolean字段{@link com.mega.revelationfix.common.client.RendererUtils#isTimeStop_andSameDimension}
 */
public class TimeStopUtils {
    public static volatile boolean isTimeStop;

    public static boolean andSameDimension(Level level) {
        if (level == null) return false;
        if (level.isClientSide) {
            return ((ClientLevelExpandedContext) ((LevelEC) level).uom$levelECData()).isCurrentTS();
        } else
            return ((ServerEC) ((ServerLevel) level).getServer()).uom$serverECData().timeStopDimensions.contains(level.dimension());
    }

    public static boolean andSameDimension(ResourceKey<Level> level, MinecraftServer server) {
        if (level == null) return false;
        return ((ServerEC) server).uom$serverECData().timeStopDimensions.contains(level);
    }

    public static boolean canMove(Entity entity) {
        if (entity instanceof Player player) {
            if (player.isCreative() || player.isSpectator())
                return true;
            else return TimeStopEntityData.getTimeStopCount(player) > 0;
        } else if (entity instanceof LivingEntity living)
            return TimeStopEntityData.getTimeStopCount(living) > 0;
        return false;
    }

    public static synchronized void use(boolean z, LivingEntity source) {
        use(z, source, true);
    }

    /**
     * @param z      是否时停
     * @param source 实体
     * @param force  为true时无条件设置当前实体剩余时停时间0
     */
    public static synchronized void use(boolean z, LivingEntity source, boolean force) {
        use(z, source, force, 180);
    }

    /**
     * @param z      是否时停
     * @param source 实体
     * @param time   设置时停的时候同时设置剩余时间
     * @param force  为true时无条件设置当前实体剩余时停时间0
     */
    public static synchronized void use(boolean z, LivingEntity source, boolean force, int time) {
        if (source.level().isClientSide) throw new RuntimeException(("time stop should be called on server side."));
        if (!source.level().isClientSide) {
            boolean lastState = isTimeStop;

            if (!z) {
                for (LivingEntity living : source.level.getEntitiesOfClass(LivingEntity.class, new AABB(new BlockPos(0, 0, 0)).inflate(30000000))) {
                    if (TimeStopEntityData.getTimeStopCount(living) > 0 && living.isAlive()) {
                        if (force)
                            TimeStopEntityData.setTimeStopCount(source, 0);
                        return;
                    }
                }
            }
            isTimeStop = z;
            if (!isTimeStop) {
                TimeStopSavedData.readOrCreate(((ServerLevel) source.level).server).removeTsDimension(source.level.dimension());
            }
            PacketHandler.sendToAll(new TimeStopSkillPacket(isTimeStop, source.getUUID()));
            if (isTimeStop)
                PacketHandler.sendToAll(new TSDimensionSynchedPacket(new ResourceLocation(""), source.level.dimension().location()));
            else
                PacketHandler.sendToAll(new TSDimensionSynchedPacket(source.level.dimension().location(), new ResourceLocation("")));
            if (z) {
                TimeStopSavedData.readOrCreate(((ServerLevel) source.level).server).addTsDimension(source.level.dimension());
                TimeStopEntityData.setTimeStopCount(source, TimeStopEntityData.getTimeStopCount(source) <= 1 ? time : TimeStopEntityData.getTimeStopCount(source));
            } else {
                if (force)
                    TimeStopEntityData.setTimeStopCount(source, 0);
            }

            if (!lastState && isTimeStop) {
                if (source instanceof ServerPlayer serverPlayer) {
                    PacketHandler.playSound(serverPlayer, ModSounds.TIME_STOP.get(), SoundSource.PLAYERS, 1f, 1f);
                } else source.playSound(ModSounds.TIME_STOP.get());
            }
        }

    }
}
