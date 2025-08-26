package com.mega.revelationfix.common.capability.entity;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.mega.endinglib.api.capability.CapabilitySyncType;
import com.mega.endinglib.api.capability.EntitySyncCapabilityBase;
import com.mega.endinglib.common.capability.CapabilityEntityData;
import com.mega.endinglib.common.capability.syncher.CapabilityDataSerializer;
import com.mega.endinglib.common.capability.syncher.CapabilityDataSerializers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import z1gned.goetyrevelation.ModMain;

import java.util.Set;

public class GoetyRevelationPlayerCapability extends EntitySyncCapabilityBase {
    public static final ResourceLocation NAME = new ResourceLocation(ModMain.MODID, "gr_player_cap");
    public static final Supplier<GoetyRevelationPlayerCapability> INSTANCE_SUPPLIER = Suppliers.memoize(GoetyRevelationPlayerCapability::new);
    public CapabilityEntityData<Boolean> DEFAULT_ATTRIBUTE_MODE = this.dataManager.defineWithoutSerialization(0, false, CapabilityDataSerializers.BOOLEAN);
    public CapabilityEntityData<Integer> ODAMANE_EXPANDED_FLAGS = this.dataManager.define(1, "OdamaneExpandedFlags", 0, CapabilityDataSerializers.INT);
    public CapabilityEntityData<Integer> PURPLE_INVUL_TIME = this.dataManager.define(2, "PurpleInvulnerableTime", 0, CapabilityDataSerializers.INT);
    public CapabilityEntityData<Integer> HALO_REVIVE_COOLDOWN = this.dataManager.define(3, "HaloRvvCooldown", 0, CapabilityDataSerializers.INT);
    public CapabilityEntityData<Integer> HALO_REVIVE_METEOR_TIME = this.dataManager.define(4, "RvvMeteorTime", 0, CapabilityDataSerializers.INT);
    public CapabilityEntityData<Boolean> ARMOR_CLIMBING_MODE = this.dataManager.define(5, "ArmorClimbing", true, CapabilityDataSerializers.BOOLEAN);

    @Override
    public ResourceLocation getRegistryName() {
        return NAME;
    }
    @Override
    public Class<? extends CapabilityProvider<Entity>> getEnableClass() {
        return Player.class;
    }

    @Override
    public void syncData(CompoundTag compoundTag, Dist dist, CapabilitySyncType capabilitySyncType, Entity entity) {
        if (capabilitySyncType == CapabilitySyncType.DEATH) {
            if (!entity.isAlive())
                this.setDefaultAttributeMode(false);
        }
    }

    @Override
    public void readSyncData(CompoundTag compoundTag, Dist dist, CapabilitySyncType capabilitySyncType, Entity entity) {

    }

    @Override
    public boolean canSyncWhenTick(Entity entity, Level level) {
        return false;
    }

    @Override
    public Set<CapabilitySyncType> getEnabledSyncTypes() {
        return super.getEnabledSyncTypes();
    }

    @Override
    public void customSerializeNBT(CompoundTag compoundTag) {

    }

    @Override
    public void customDeserializeNBT(CompoundTag compoundTag) {

    }
    public boolean isDefaultAttributeMode() {
        return this.dataManager.getValue(DEFAULT_ATTRIBUTE_MODE);
    }
    public void setDefaultAttributeMode(boolean flag) {
        this.dataManager.setValue(DEFAULT_ATTRIBUTE_MODE, flag);
    }
    public int readAllExpandedFlags() {
        return this.dataManager.getValue(ODAMANE_EXPANDED_FLAGS);
    }
    public void setAllExpandedFlags(int flags) {
        this.dataManager.setValue(ODAMANE_EXPANDED_FLAGS, flags);
    }
    public int getInvulTick() {
        return this.dataManager.getValue(PURPLE_INVUL_TIME);
    }

    public void setInvulTick(int tick) {
        this.dataManager.setValue(PURPLE_INVUL_TIME, tick);
    }
    public int getHaloReviveCooldown() {
        return this.dataManager.getValue(HALO_REVIVE_COOLDOWN);
    }
    public void setHaloReviveCooldown(int tick) {
        this.dataManager.setValue(HALO_REVIVE_COOLDOWN, tick);
    }
    public int getMeteorTime() {
        return this.dataManager.getValue(HALO_REVIVE_METEOR_TIME);
    }
    public void setMeteorTime(int tick) {
        this.dataManager.setValue(HALO_REVIVE_METEOR_TIME, tick);
    }
    public void setArmorClimbing(boolean flag) {
        this.dataManager.setValue(ARMOR_CLIMBING_MODE, flag);
    }
    public boolean isArmorClimbingMode() {
        return this.dataManager.getValue(ARMOR_CLIMBING_MODE);
    }
}
