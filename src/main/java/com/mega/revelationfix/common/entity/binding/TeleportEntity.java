package com.mega.revelationfix.common.entity.binding;

import com.mega.endinglib.mixin.accessor.AccessorEntity;
import com.mega.revelationfix.common.block.blockentity.RuneReactorBlockEntity;
import com.mega.revelationfix.common.init.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Function;

public class TeleportEntity extends Entity implements BlockBindingEntity {
    public static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
    public static EntityDataAccessor<BlockPos> REACTOR_POS = SynchedEntityData.defineId(TeleportEntity.class, EntityDataSerializers.BLOCK_POS);
    private final AccessorEntity accessor;
    public Vec3 customPos = Vec3.ZERO;

    public TeleportEntity(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);

        this.accessor = (AccessorEntity) this;
    }

    public TeleportEntity(Level p_19871_, BlockPos blockPos) {
        super(ModEntities.TELEPORT_ENTITY.get(), p_19871_);
        this.setReactorPos(blockPos);
        this.accessor = (AccessorEntity) this;
    }

    public static boolean hasLineOfSight(Entity entity, Entity p_147185_) {
        if (p_147185_.level() != entity.level()) {
            return false;
        } else {
            Vec3 vec3 = new Vec3(entity.getX(), entity.getEyeY(), entity.getZ());
            Vec3 vec31 = new Vec3(p_147185_.getX(), p_147185_.getEyeY(), p_147185_.getZ());
            if (vec31.distanceTo(vec3) > 128.0D) {
                return false;
            } else {
                return clip(entity.level(), new ClipContext(vec3, vec31, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity)).getType() == HitResult.Type.MISS;
            }
        }
    }

    public static BlockHitResult clip(Level level, ClipContext p_45548_) {
        return traverseBlocks(p_45548_.getFrom(), p_45548_.getTo(), p_45548_, (p_151359_, p_151360_) -> {
            BlockState blockstate = level.getBlockState(p_151360_);
            FluidState fluidstate = level.getFluidState(p_151360_);
            Vec3 vec3 = p_151359_.getFrom();
            Vec3 vec31 = p_151359_.getTo();
            BlockHitResult blockhitresult = level.clipWithInteractionOverride(vec3, vec31, p_151360_, SHAPE, blockstate);
            BlockHitResult blockhitresult1 = SHAPE.clip(vec3, vec31, p_151360_);
            double d0 = blockhitresult == null ? Double.MAX_VALUE : p_151359_.getFrom().distanceToSqr(blockhitresult.getLocation());
            double d1 = blockhitresult1 == null ? Double.MAX_VALUE : p_151359_.getFrom().distanceToSqr(blockhitresult1.getLocation());
            return d0 <= d1 ? blockhitresult : blockhitresult1;
        }, (p_275153_) -> {
            Vec3 vec3 = p_275153_.getFrom().subtract(p_275153_.getTo());
            return BlockHitResult.miss(p_275153_.getTo(), Direction.getNearest(vec3.x, vec3.y, vec3.z), BlockPos.containing(p_275153_.getTo()));
        });
    }

    static <T, C> T traverseBlocks(Vec3 p_151362_, Vec3 p_151363_, C p_151364_, BiFunction<C, BlockPos, T> p_151365_, Function<C, T> p_151366_) {
        if (p_151362_.equals(p_151363_)) {
            return p_151366_.apply(p_151364_);
        } else {
            double d0 = Mth.lerp(-1.0E-7D, p_151363_.x, p_151362_.x);
            double d1 = Mth.lerp(-1.0E-7D, p_151363_.y, p_151362_.y);
            double d2 = Mth.lerp(-1.0E-7D, p_151363_.z, p_151362_.z);
            double d3 = Mth.lerp(-1.0E-7D, p_151362_.x, p_151363_.x);
            double d4 = Mth.lerp(-1.0E-7D, p_151362_.y, p_151363_.y);
            double d5 = Mth.lerp(-1.0E-7D, p_151362_.z, p_151363_.z);
            int i = Mth.floor(d3);
            int j = Mth.floor(d4);
            int k = Mth.floor(d5);
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(i, j, k);
            T t = p_151365_.apply(p_151364_, blockpos$mutableblockpos);
            if (t != null) {
                return t;
            } else {
                double d6 = d0 - d3;
                double d7 = d1 - d4;
                double d8 = d2 - d5;
                int l = Mth.sign(d6);
                int i1 = Mth.sign(d7);
                int j1 = Mth.sign(d8);
                double d9 = l == 0 ? Double.MAX_VALUE : (double) l / d6;
                double d10 = i1 == 0 ? Double.MAX_VALUE : (double) i1 / d7;
                double d11 = j1 == 0 ? Double.MAX_VALUE : (double) j1 / d8;
                double d12 = d9 * (l > 0 ? 1.0D - Mth.frac(d3) : Mth.frac(d3));
                double d13 = d10 * (i1 > 0 ? 1.0D - Mth.frac(d4) : Mth.frac(d4));
                double d14 = d11 * (j1 > 0 ? 1.0D - Mth.frac(d5) : Mth.frac(d5));

                while (d12 <= 1.0D || d13 <= 1.0D || d14 <= 1.0D) {
                    if (d12 < d13) {
                        if (d12 < d14) {
                            i += l;
                            d12 += d9;
                        } else {
                            k += j1;
                            d14 += d11;
                        }
                    } else if (d13 < d14) {
                        j += i1;
                        d13 += d10;
                    } else {
                        k += j1;
                        d14 += d11;
                    }

                    T t1 = p_151365_.apply(p_151364_, blockpos$mutableblockpos.set(i, j, k));
                    if (t1 != null) {
                        return t1;
                    }
                }

                return p_151366_.apply(p_151364_);
            }
        }
    }

    @Override
    public void tick() {
        if (this.getTags().contains("shouldRemove")) {
            if (!this.level().isClientSide)
                this.discard();
            else {
                this.setRemoved(RemovalReason.DISCARDED);
                this.onClientRemoval();
            }
        }
        if (!level().isClientSide && tickCount % 10 == 0 && tickCount > 0) {
            if (!(level().getBlockEntity(this.getReactorPos()) instanceof RuneReactorBlockEntity))
                this.discard();
        }
        this.accessor.setBlockPositionField(this.getReactorPos());
        this.customPos = new Vec3(this.blockPosition().getX(), this.blockPosition().getY(), this.blockPosition().getZ());
        this.accessor.setPositionField(this.customPos);
        super.tick();
    }

    @Override
    public void defineSynchedData() {
        this.entityData.define(REACTOR_POS, BlockPos.ZERO);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        ListTag listTag = compound.getList("ReactorPos", 3);
        this.setReactorPos(new BlockPos(listTag.getInt(0), listTag.getInt(1), listTag.getInt(2)));
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        compound.put("ReactorPos", this.newIntList(this.getReactorPos().getX(), this.getReactorPos().getY(), this.getReactorPos().getZ()));
    }

    protected ListTag newIntList(int... p_20064_) {
        ListTag listtag = new ListTag();

        for (int d0 : p_20064_) {
            listtag.add(IntTag.valueOf(d0));
        }

        return listtag;
    }

    @Override
    public @NotNull Vec3 position() {
        return customPos;
    }

    @Override
    public double getX() {
        return customPos.x;
    }

    @Override
    public double getY() {
        return customPos.y;
    }

    @Override
    public double getZ() {
        return customPos.z;
    }

    @Override
    public double getEyeY() {
        return getY() + 3F / 16F;
    }

    @Override
    public double getX(double p_20166_) {
        return this.getX() + (double) this.getBbWidth() * p_20166_;
    }

    @Override
    public double getY(double p_20228_) {
        return this.getY() + (double) this.getBbHeight() * p_20228_;
    }

    @Override
    public double getZ(double p_20247_) {
        return this.getZ() + (double) this.getBbWidth() * p_20247_;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean isInvulnerable() {
        return true;
    }

    @Override
    public boolean isInvulnerableTo(@NotNull DamageSource p_20122_) {
        return true;
    }

    @Override
    public boolean canUpdate() {
        return true;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public void push(@NotNull Entity p_20293_) {
    }

    @Override
    public void push(double p_20286_, double p_20287_, double p_20288_) {
    }

    @Override
    public void setDeltaMovement(double p_20335_, double p_20336_, double p_20337_) {
    }

    @Override
    public boolean hurt(@NotNull DamageSource p_19946_, float p_19947_) {
        return false;
    }

    @Override
    public @NotNull Vec3 getDeltaMovement() {
        return Vec3.ZERO;
    }

    @Override
    public void setDeltaMovement(@NotNull Vec3 p_20257_) {
    }

    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        this.noPhysics = true;
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public BlockPos getReactorPos() {
        BlockPos pos = this.entityData.get(REACTOR_POS);
        this.customPos = new Vec3(pos.getX(), pos.getY(), pos.getZ());
        this.accessor.setPositionField(this.customPos);
        return pos;
    }

    public void setReactorPos(BlockPos pos) {
        this.entityData.set(REACTOR_POS, pos);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double p_19883_) {
        return p_19883_ < 64D * 64D * 8 * 8;
    }

    @Override
    public @NotNull AABB getBoundingBoxForCulling() {
        return this.getBoundingBox().inflate(96);
    }

    @Override
    public BlockPos getOwnerBlock() {
        return getReactorPos();
    }

    @Override
    public void blockOwnerCheckEvent(Level level, BlockPos pos, BlockState blockState, @Nullable BlockEntity blockEntity, int flag) {

    }

    @Override
    public void blockOwnerRemoveEvent(Level level, BlockPos pos, BlockState blockState, @Nullable BlockEntity blockEntity, int flag) {
        this.discard();
    }

    @Override
    public void blockOwnerTickEvent(Level level, BlockPos pos, BlockState blockState, @Nullable BlockEntity blockEntity, int flag) {

    }

    public boolean isLookingAtMe(Player p_32535_) {

        Vec3 vec3 = p_32535_.getViewVector(1.0F).normalize();
        Vec3 vec31 = new Vec3(this.getX() + 0.5F - p_32535_.getX(), this.getEyeY() - p_32535_.getEyeY(), this.getZ() + 0.5F - p_32535_.getZ());
        double d0 = vec31.length();
        vec31 = vec31.normalize();
        double d1 = vec3.dot(vec31);
        return d1 > 1.0D - 0.025D / d0 && hasLineOfSight(p_32535_, this);

    }
}
