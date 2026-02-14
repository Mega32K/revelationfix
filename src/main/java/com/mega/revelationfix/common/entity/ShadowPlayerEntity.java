package com.mega.revelationfix.common.entity;

import com.mega.revelationfix.common.compat.SafeClass;
import com.mega.revelationfix.common.compat.Wrapped;
import com.mega.revelationfix.common.init.ModEntities;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class ShadowPlayerEntity extends Mob {
    public static final EntityDataAccessor<Integer> OWNER_PLAYER = SynchedEntityData.defineId(ShadowPlayerEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Vector3f> POS = SynchedEntityData.defineId(ShadowPlayerEntity.class, EntityDataSerializers.VECTOR3);
    public int frozenTick;
    @Nullable
    public Object renderer = null;
    public double sXOld;
    public double sYOld;
    public double sZOld;
    public double sX;
    public double sY;
    public double sZ;
    public float sYBodyRot;
    public float sYBodyRotOld;
    public float sYHeadRot;
    public float sYHeadRotOld;
    public float sXRot;
    public float sXRotOld;
    public float sWalkSpeed;
    public float sWalkPosition;
    public float sAttackAnim;
    public ItemStack sMainHandItem = ItemStack.EMPTY;
    public ItemStack sOffHandItem = ItemStack.EMPTY;
    public Pose sPose = Pose.STANDING;
    public float sSwimAmount = 0F;
    public boolean crouching = false;
    public ShadowPlayerEntity(EntityType<? extends Mob> p_21368_, Level level) {
        super(p_21368_, level);
        frozenTick = 0;
        this.noPhysics = true;
    }

    public ShadowPlayerEntity(Player player) {
        super(ModEntities.SHADOW_PLAYER.get(), player.level());
        this.noPhysics = true;
        frozenTick = player.tickCount;
        this.setBind(player);
        this.sXOld = player.xOld;
        this.sYOld = player.yOld;
        this.sZOld = player.zOld;
        this.sPose = player.getPose();
        this.sXRot = player.getXRot();
        this.sXRotOld = player.xRotO;
        this.sYBodyRotOld = player.yBodyRotO;
        this.sYBodyRot = player.yBodyRot;
        this.sYHeadRotOld = player.yHeadRotO;
        this.sYHeadRot = player.yHeadRot;
        this.sSwimAmount = player.getSwimAmount(0F);
        this.crouching = player.isCrouching();
    }

    @Override
    public boolean canBeHitByProjectile() {
        return false;
    }

    @Override
    public boolean isInvulnerable() {
        return true;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public float getHealth() {
        return this.getMaxHealth();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(OWNER_PLAYER, -1);
        this.entityData.define(POS, new Vector3f());
    }
    public @Nullable Player getBind() {
        int id = this.entityData.get(OWNER_PLAYER);
        if (id < 0) return null;
        Entity entity = this.level().getEntity(id);
        if (entity instanceof Player player)
            return player;
        else return null;
    }
    public void setBind(Player player) {
        this.entityData.set(OWNER_PLAYER, player.getId());
    }

    @Override
    public void tick() {
        this.noPhysics = true;
        Vec3 pos = new Vec3(this.getPos());
        this.setPos(pos);
        this.xOld = this.sX;
        this.yOld = this.sY;
        this.zOld = this.sZ;
        if (level().isClientSide) {
            this.sXOld = this.sX;
            this.sYOld = this.sY;
            this.sZOld = this.sZ;
        }
        super.tick();
        if (this.tickCount >= 14) {
            if (!this.level().isClientSide) this.discard();
        }
        if (this.getBind() == null) {
            if (level().isClientSide) {
                this.setInvisible(true);
            } else this.discard();
        }
    }

    @Override
    public void kill() {
        this.discard();
    }

    @Override
    public void playSound(@NotNull SoundEvent p_19938_, float p_19939_, float p_19940_) {
    }

    @Override
    public @NotNull Vec3 getDeltaMovement() {
        return Vec3.ZERO;
    }

    @Override
    public void move(@NotNull MoverType p_19973_, @NotNull Vec3 p_19974_) {
    }

    @Override
    public void travel(@NotNull Vec3 p_21280_) {
    }

    @Override
    public @NotNull Pose getPose() {
        return sPose;
    }

    @Override
    public boolean hasPose(@NotNull Pose p_217004_) {
        return p_217004_ == sPose;
    }

    @Override
    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> eda) {
        super.onSyncedDataUpdated(eda);
        if (eda.equals(OWNER_PLAYER)) {
            Player player;
            if ((player = getBind()) != null) {
                this.setInvisible(false);
                frozenTick = player.tickCount;
                this.sPose = player.getPose();
                this.sXRot = player.getXRot();
                this.sXRotOld = player.xRotO;
                this.sYBodyRotOld = player.yBodyRotO;
                this.sYBodyRot = player.yBodyRot;
                this.sYHeadRotOld = player.yHeadRotO;
                this.sYHeadRot = player.yHeadRot;
                this.sSwimAmount = player.getSwimAmount(0F);
                this.crouching = player.isCrouching();
                if (player.level().isClientSide) {
                    Wrapped.setShadowPlayerRenderer(this, player);
                }
            }
        } else if (eda.equals(POS)) {
            this.sX = this.getPos().x;
            this.sY = this.getPos().y;
            this.sZ = this.getPos().z;
            this.setPos(new Vec3(this.getPos()));
            this.xOld = this.sX;
            this.yOld = this.sY;
            this.zOld = this.sZ;
            if (level().isClientSide) {
                this.sXOld = this.sX;
                this.sYOld = this.sY;
                this.sZOld = this.sZ;
            }
        }
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean canCollideWith(@NotNull Entity p_20303_) {
        return false;
    }

    @Override
    protected void pushEntities() {
    }

    @Override
    public float getSwimAmount(float p_20999_) {
        return sSwimAmount;
    }

    @Override
    public boolean isCrouching() {
        return crouching;
    }

    public void setPosData(float x, float y, float z) {
        this.entityData.set(POS, new Vector3f(x, y, z));
        this.sX = x;
        this.sY = y;
        this.sZ = z;
    }
    public void setPosData(Vec3 posData) {
        this.setPosData((float) posData.x, (float) posData.y, (float) posData.z);
    }
    public Vector3f getPos() {
        return this.entityData.get(POS);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double d) {
        if (d < 3) return false;
        double d0 = this.getBoundingBoxForCulling().getSize();
        if (Double.isNaN(d0)) {
            d0 = 1.0D;
        }

        d0 *= 64.0D;
        return d < d0 * d0;
    }

    @Override
    public boolean isAlive() {
        return false;
    }

    @Override
    public boolean isDeadOrDying() {
        return true;
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        return new AABB(-0.3, 0.0, -0.3, 0.3, 1.875, 0.3).move(this.position());
    }
}
