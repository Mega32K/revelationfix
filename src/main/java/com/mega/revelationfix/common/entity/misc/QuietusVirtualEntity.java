package com.mega.revelationfix.common.entity.misc;

import com.mega.revelationfix.common.init.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

public class QuietusVirtualEntity extends Entity implements IEntityAdditionalSpawnData {
    public static final int lifetime = 8;
    public float distance;

    public QuietusVirtualEntity(EntityType<? extends Entity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public QuietusVirtualEntity(Level level, Vec3 start, Vec3 end, LivingEntity owner) {
        super(ModEntities.QUIETUS_VIRTUAL_ENTITY.get(), level);
        this.setPos(start);
        this.distance = (float) start.distanceTo(end);
        this.setRot(owner.getYRot(), owner.getXRot());
    }

    public void tick() {
        if (this.tickCount == 1) {
            if (this.level().isClientSide) {
                Vec3 forward = this.getForward();
                for (float i = 1.0F; i < this.distance; i += 0.5F) {
                    Vec3 pos = this.position().add(forward.scale(i));
                    this.level().addParticle(ParticleTypes.SMOKE, false, pos.x, pos.y + 0.5, pos.z, 0.0, 0.0, 0.0);
                }
            }
        } else if (this.tickCount > 8) {
            this.discard();
        }

    }

    public boolean shouldRender(double pX, double pY, double pZ) {
        return true;
    }

    public boolean shouldBeSaved() {
        return false;
    }

    protected void defineSynchedData() {
    }

    protected void readAdditionalSaveData(CompoundTag pCompound) {
    }

    protected void addAdditionalSaveData(CompoundTag pCompound) {
    }

    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeInt((int) (this.distance * 10.0F));
    }

    public void readSpawnData(FriendlyByteBuf additionalData) {
        this.distance = (float) additionalData.readInt() / 10.0F;
    }
}
