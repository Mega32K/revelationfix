package com.mega.revelationfix.util.time;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.UUID;

public class Wrapped {
    public static boolean canCancel(List<UUID> entities, Level level) {
        ClientLevel clientLevel = (ClientLevel) level;
        if (entities.size() > 1) {
            for (int i = entities.size() - 1; i >= 0; i--) {
                if (clientLevel.entityStorage.getEntityGetter().get(entities.get(i)) instanceof LivingEntity l && TimeStopEntityData.getTimeStopCount(l) > 1)
                    return true;
            }
        }
        return false;
    }
}
