package com.mega.revelationfix.safe.entity;

import com.Polarice3.Goety.api.entities.IOwned;
import com.mega.revelationfix.common.compat.SafeClass;
import com.mega.revelationfix.util.entity.ATAHelper2;
import com.mega.revelationfix.util.entity.EntityActuallyHurt;
import com.mega.revelationfix.util.LivingEntityEC;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

public class EntityExpandedContext {
    public static final Predicate<Entity> NO_GODS = (e) -> EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(e) && !ATAHelper2.hasOdamane(e);
    public static final String GR_MAY_FRIENDLY_TAG = "grFriendlyToTeam";
    public static final String GR_FT_CHURCH = "grFT_church";
    public final LivingEntity entity;
    public String className;
    public EntityActuallyHurt.IndexAndType indexAndType;
    public int apollyonLastGrowingTime;
    public int banHealingTime;
    @Nullable
    public UUID quietusCasterID;
    public int tetraFadingTime;
    @Nullable
    private LivingEntity quietusCaster;
    public int banAnySpelling;
    public EntityExpandedContext(LivingEntity entity) {
        this.entity = entity;
        this.className = entity.getClass().getName();
        if (!SafeClass.isFantasyEndingLoaded()) {
            EntityActuallyHurt.checkAndSave(entity);
            this.indexAndType = EntityActuallyHurt.entityHealthDatas.get(className);
        }
    }
    public static boolean isOwnerFriendlyTag(Entity owned) {
        return owned instanceof IOwned iOwned && iOwned.getTrueOwner() != null && iOwned.getTrueOwner().tags.contains(GR_MAY_FRIENDLY_TAG);
    }
    public static boolean isOwnerFriendlyTag_Church(Entity owned) {
        return owned instanceof IOwned iOwned && iOwned.getTrueOwner() != null && iOwned.getTrueOwner().tags.contains(GR_FT_CHURCH);
    }
    public static EntityActuallyHurt.IndexAndType getIndexAndType(LivingEntity living) {
        if (living == null) return null;
        else return ((LivingEntityEC) living).revelationfix$livingECData().indexAndType;
    }

    /**
     * xxx = nbt.get
     *
     * @param nbt NBT Data
     */
    public void read(CompoundTag nbt) {
        if (nbt.hasUUID("quietusCasterID"))
            quietusCasterID = nbt.getUUID("quietusCasterID");
    }

    /**
     * nbt.put(xxx)
     *
     * @param nbt NBT Data
     */
    public void save(CompoundTag nbt) {
        if (quietusCasterID != null) {
            nbt.putUUID("quietusCasterID", quietusCasterID);
        }
    }

    @Nullable
    public LivingEntity getQuietusCaster() {
        if (quietusCasterID == null) return null;
        if (entity.level.isClientSide) return null;
        else if (entity.level instanceof ServerLevel serverLevel) {
            if (serverLevel.getEntities().get(quietusCasterID) instanceof LivingEntity living && quietusCaster == null)
                quietusCaster = living;
            return quietusCaster;
        } else return null;
    }

    public void setQuietusCaster(LivingEntity living) {
        this.quietusCaster = living;
        this.quietusCasterID = living.getUUID();
    }

    public void tick() {
        if (SafeClass.isTetraLoaded()) {
            if (tetraFadingTime > 0) {
                tetraFadingTime--;
            }

            if (tetraFadingTime == 0) {
                synchronized (this) {
                    Map<Attribute, UUID> attributeUUIDMap = Collections.synchronizedMap(SafeClass.getAttributes());
                    for (Map.Entry<Attribute, UUID> s : attributeUUIDMap.entrySet()) {
                        AttributeInstance instance = entity.getAttribute(s.getKey());
                        if (instance != null) {
                            instance.removeModifier(s.getValue());
                        }
                    }
                }
            }
        }
    }
}
