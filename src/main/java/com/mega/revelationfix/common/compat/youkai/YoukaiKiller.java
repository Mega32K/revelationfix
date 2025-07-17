package com.mega.revelationfix.common.compat.youkai;

import com.mega.endinglib.mixin.accessor.AccessorEntity;
import com.mega.endinglib.mixin.accessor.AccessorServerLevel;
import dev.xkmc.youkaishomecoming.content.entity.youkai.YoukaiEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class YoukaiKiller {
    public static void killYoukai(Entity entity) {
        if (entity instanceof YoukaiEntity shouldBeRemoved) {
            Level level = shouldBeRemoved.level();
            if (!level.isClientSide) {
                if (level instanceof ServerLevel serverLevel) {
                    ((AccessorServerLevel) serverLevel).getEntityTickList().remove(shouldBeRemoved);
                }
                shouldBeRemoved.remove(Entity.RemovalReason.DISCARDED);
                shouldBeRemoved.setRemoved(Entity.RemovalReason.DISCARDED);
            }
            shouldBeRemoved.setPos(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NaN);
            shouldBeRemoved.setOldPosAndRot();
            if (shouldBeRemoved.isAddedToWorld()) {
                CompoundTag tag = shouldBeRemoved.getPersistentData();
                ListTag listTag = new ListTag();
                listTag.add(DoubleTag.valueOf(3000000));
                listTag.add(DoubleTag.valueOf(-300000));
                listTag.add(DoubleTag.valueOf(-3000000));
                tag.put("Pos", listTag);
                shouldBeRemoved.tick();
            }
            ((AccessorEntity) shouldBeRemoved).setAddedToWorldField(false);
        }
    }
}
