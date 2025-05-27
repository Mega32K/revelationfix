package com.mega.revelationfix.common.entity;

import com.Polarice3.Goety.common.entities.boss.Apostle;
import com.mega.revelationfix.common.apollyon.client.WrappedTrailUpdate;
import com.mega.revelationfix.common.compat.SafeClass;
import com.mega.revelationfix.common.init.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.UUID;

public class FakeItemEntity extends Entity implements TraceableEntity {
    private static final EntityDataAccessor<Boolean> SHOULD_RENDER_TRAIL = SynchedEntityData.defineId(FakeItemEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<ItemStack> DATA_ITEM = SynchedEntityData.defineId(FakeItemEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final int LIFETIME = 6000;
    private static final int INFINITE_PICKUP_DELAY = 32767;
    private static final int INFINITE_LIFETIME = -32768;
    public final float bobOffs;
    public WrappedTrailUpdate wrappedTrailUpdate = new WrappedTrailUpdate(this);
    /**
     * The maximum age of this EntityItem.  The item is expired once this is reached.
     */
    public int lifespan = FakeItemEntity.LIFETIME;
    private UUID apollyon;
    private int age;
    @Nullable
    private UUID thrower;
    @Nullable
    private UUID target;

    public FakeItemEntity(EntityType<? extends Entity> p_31991_, Level p_31992_) {
        super(p_31991_, p_31992_);
        this.bobOffs = this.random.nextFloat() * (float) Math.PI * 2.0F;
        this.setYRot(this.random.nextFloat() * 360.0F);
    }

    public FakeItemEntity(Level p_32001_, double p_32002_, double p_32003_, double p_32004_, ItemStack p_32005_) {
        this(p_32001_, p_32002_, p_32003_, p_32004_, p_32005_, p_32001_.random.nextDouble() * 0.2D - 0.1D, 0.2D, p_32001_.random.nextDouble() * 0.2D - 0.1D);
    }

    public FakeItemEntity(Level p_149663_, double p_149664_, double p_149665_, double p_149666_, ItemStack p_149667_, double p_149668_, double p_149669_, double p_149670_) {
        this(ModEntities.FAKE_ITEM_ENTITY.get(), p_149663_);
        this.setPos(p_149664_, p_149665_, p_149666_);
        this.setDeltaMovement(p_149668_, p_149669_, p_149670_);
        this.setItem(p_149667_);
        this.lifespan = (p_149667_.getItem() == null ? FakeItemEntity.LIFETIME : p_149667_.getEntityLifespan(p_149663_));
    }

    private FakeItemEntity(FakeItemEntity p_31994_) {
        super(p_31994_.getType(), p_31994_.level());
        this.setItem(p_31994_.getItem().copy());
        this.copyPosition(p_31994_);
        this.age = p_31994_.age;
        this.bobOffs = p_31994_.bobOffs;
        this.lifespan = p_31994_.lifespan;
    }

    public boolean dampensVibrations() {
        return this.getItem().is(ItemTags.DAMPENS_VIBRATIONS);
    }

    @Nullable
    public Entity getOwner() {
        if (this.thrower != null) {
            Level level = this.level();
            if (level instanceof ServerLevel serverlevel) {
                return serverlevel.getEntity(this.thrower);
            }
        }

        return null;
    }

    protected Entity.@NotNull MovementEmission getMovementEmission() {
        return Entity.MovementEmission.NONE;
    }

    protected void defineSynchedData() {
        this.getEntityData().define(SHOULD_RENDER_TRAIL, false);
        this.getEntityData().define(DATA_ITEM, ItemStack.EMPTY);
    }

    public Entity getApollyon() {
        if (this.apollyon != null) {
            Level level = this.level();
            if (level instanceof ServerLevel serverlevel) {
                return serverlevel.getEntity(this.apollyon);
            }
        }

        return null;
    }

    public UUID getApollyonUUID() {
        return apollyon;
    }

    public void setApollyonUUID(UUID uuid) {
        this.apollyon = uuid;
    }

    public void tick() {

        if (this.getItem().isEmpty()) {

            this.discard();
        } else {
            if (!this.level.isClientSide) {

                Entity entity = getApollyon();
                if (entity instanceof Apostle apostle && !apostle.isRemoved()) {
                    if (!SafeClass.isNetherDoomApollyon(apostle)) {
                        this.discard();
                    } else {
                        this.setNoGravity(true);
                        this.hurtMarked = true;
                        {
                            Vec3 center = entity.getBoundingBox().getCenter();
                            float distance = (float) center.distanceTo(this.position());
                            float f = 0.7F;
                            float scale = f * f * f * f * 0.45F;
                            Vec3 diff = center.subtract(this.position()).normalize().scale(scale);
                            if (this.getDeltaMovement().length() < 0.6F)
                                this.push(diff.x, diff.y, diff.z);
                        }
                        if (this.distanceToSqr(apostle) < 2.5D) {
                            this.discard();

                        }
                    }
                } else {
                    ServerLevel serverLevel = (ServerLevel) level;
                    Apostle apostle = serverLevel.getNearestEntity(serverLevel.getEntitiesOfClass(Apostle.class, this.getBoundingBox().inflate(128.0))
                            , TargetingConditions.forNonCombat(), null, this.getX(), this.getY(), this.getZ());
                    if (apostle != null && SafeClass.isNetherDoomApollyon(apostle)) {
                        this.apollyon = apostle.uuid;
                    }
                    if (apostle == null || apostle.isRemoved())
                        this.discard();
                }
            }
            super.tick();

            this.xo = this.getX();
            this.yo = this.getY();
            this.zo = this.getZ();
            Vec3 vec3 = this.getDeltaMovement();
            if (!this.isNoGravity()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.04D, 0.0D));
            }

            if (this.level().isClientSide) {
                this.noPhysics = false;
            } else {
                this.noPhysics = !this.level().noCollision(this, this.getBoundingBox().deflate(1.0E-7D));
                if (this.noPhysics) {
                    this.moveTowardsClosestSpace(this.getX(), (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2.0D, this.getZ());
                }
            }

            if (!this.onGround() || this.getDeltaMovement().horizontalDistanceSqr() > (double) 1.0E-5F || (this.tickCount + this.getId()) % 4 == 0) {
                this.move(MoverType.SELF, this.getDeltaMovement());
                float f1 = 0.98F;
                if (this.onGround()) {
                    BlockPos groundPos = getBlockPosBelowThatAffectsMyMovement();
                    f1 = this.level().getBlockState(groundPos).getFriction(level(), groundPos, this) * 0.98F;
                }

                this.setDeltaMovement(this.getDeltaMovement().multiply(f1, 0.98D, f1));

            }

            if (!this.level().isClientSide) {
                double d0 = this.getDeltaMovement().subtract(vec3).lengthSqr();
                if (d0 > 0.01D) {
                    this.hasImpulse = true;
                }
            }

            ItemStack item = this.getItem();
            if (item.isEmpty() && !this.isRemoved()) {
                this.discard();

            }

        }
    }

    protected @NotNull BlockPos getBlockPosBelowThatAffectsMyMovement() {
        return this.getOnPos(0.999999F);
    }


    public boolean fireImmune() {
        return true;
    }

    public boolean hurt(@NotNull DamageSource p_32013_, float p_32014_) {
        return false;
    }

    public void addAdditionalSaveData(@NotNull CompoundTag p_32050_) {
        if (this.apollyon != null) {
            p_32050_.putUUID("ApollyonUUID", this.apollyon);
        }
        p_32050_.putShort("Age", (short) this.age);
        p_32050_.putInt("Lifespan", this.lifespan);
        if (this.thrower != null) {
            p_32050_.putUUID("Thrower", this.thrower);
        }

        if (this.target != null) {
            p_32050_.putUUID("Owner", this.target);
        }

        if (!this.getItem().isEmpty()) {
            p_32050_.put("Item", this.getItem().save(new CompoundTag()));
        }

    }

    public void readAdditionalSaveData(CompoundTag p_32034_) {
        if (p_32034_.hasUUID("ApollyonUUID")) {
            this.apollyon = p_32034_.getUUID("ApollyonUUID");
        }
        this.age = p_32034_.getShort("Age");
        if (p_32034_.contains("Lifespan")) {
            this.lifespan = p_32034_.getInt("Lifespan");
        }

        if (p_32034_.hasUUID("Owner")) {
            this.target = p_32034_.getUUID("Owner");
        }

        if (p_32034_.hasUUID("Thrower")) {
            this.thrower = p_32034_.getUUID("Thrower");
        }

        CompoundTag compoundtag = p_32034_.getCompound("Item");
        this.setItem(ItemStack.of(compoundtag));
        if (this.getItem().isEmpty()) {
            this.discard();

        }

    }

    @Override
    public void onClientRemoval() {
        wrappedTrailUpdate.remove();
        super.onClientRemoval();
    }

    public @NotNull Component getName() {
        Component component = this.getCustomName();
        return component != null ? component : Component.translatable(this.getItem().getDescriptionId());
    }

    public boolean isAttackable() {
        return false;
    }

    @Nullable
    public Entity changeDimension(@NotNull ServerLevel p_32042_, net.minecraftforge.common.util.@NotNull ITeleporter teleporter) {
        Entity entity = super.changeDimension(p_32042_, teleporter);
        return entity;
    }

    public ItemStack getItem() {
        return this.getEntityData().get(DATA_ITEM);
    }

    public void setItem(ItemStack p_32046_) {
        this.getEntityData().set(DATA_ITEM, p_32046_);
    }

    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> p_32036_) {
        super.onSyncedDataUpdated(p_32036_);
        if (DATA_ITEM.equals(p_32036_)) {
            this.getItem().setEntityRepresentation(this);
        }

    }

    public void setTarget(@Nullable UUID p_266724_) {
        this.target = p_266724_;
    }

    public void setThrower(@Nullable UUID p_32053_) {
        this.thrower = p_32053_;
    }

    public int getAge() {
        return this.age;
    }

    public void setUnlimitedLifetime() {
        this.age = -32768;
    }

    public void setExtendedLifetime() {
        this.age = -6000;
    }

    public float getSpin(float p_32009_) {
        return ((float) this.getAge() + p_32009_) / 20.0F + this.bobOffs;
    }

    public FakeItemEntity copy() {
        return new FakeItemEntity(this);
    }

    public @NotNull SoundSource getSoundSource() {
        return SoundSource.AMBIENT;
    }

    public float getVisualRotationYInDegrees() {
        return 180.0F - this.getSpin(0.5F) / ((float) Math.PI * 2F) * 360.0F;
    }
}
