package com.mega.revelationfix.common.entity;

import com.Polarice3.Goety.common.blocks.ModBlocks;
import com.mega.revelationfix.common.init.ModEntities;
import com.mega.revelationfix.safe.ClientLevelExpandedContext;
import com.mega.revelationfix.safe.ClientLevelInterface;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

public class TheEndRitualBlockEntity extends Entity {
    protected static final EntityDataAccessor<BlockPos> DATA_START_POS;
    private static final EntityDataAccessor<BlockState> BLOCK_STATE;
    private static final EntityDataAccessor<Integer> DURATION;
    private static final EntityDataAccessor<Integer> ALIVE_TICKS;

    static {
        DATA_START_POS = SynchedEntityData.defineId(TheEndRitualBlockEntity.class, EntityDataSerializers.BLOCK_POS);
        BLOCK_STATE = SynchedEntityData.defineId(TheEndRitualBlockEntity.class, EntityDataSerializers.BLOCK_STATE);
        DURATION = SynchedEntityData.defineId(TheEndRitualBlockEntity.class, EntityDataSerializers.INT);
        ALIVE_TICKS = SynchedEntityData.defineId(TheEndRitualBlockEntity.class, EntityDataSerializers.INT);
    }

    public int durationO;
    public int aliveTicksO;
    public int duration;
    public int aliveTicks;

    public TheEndRitualBlockEntity(EntityType<TheEndRitualBlockEntity> type, Level level) {
        super(type, level);
        this.duration = 0;
        this.aliveTicks = 0;
    }

    public TheEndRitualBlockEntity(Level p_31953_, double p_31954_, double p_31955_, double p_31956_, BlockState p_31957_, int duration) {
        this(ModEntities.THE_END_RITUAL_BLOCK.get(), p_31953_);
        this.setBlock(p_31957_);
        this.setPos(p_31954_, p_31955_, p_31956_);
        this.setDeltaMovement(Vec3.ZERO);
        this.duration = duration;
        this.setDuration(duration);
        this.xo = p_31954_;
        this.yo = p_31955_;
        this.zo = p_31956_;
        this.setStartPos(this.blockPosition());
        this.aliveTicks = 0;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double p_19883_) {
        return super.shouldRenderAtSqrDistance(p_19883_ / 4F);
    }

    public BlockPos getStartPos() {
        return this.entityData.get(DATA_START_POS);
    }

    public void setStartPos(BlockPos p_31960_) {
        this.entityData.set(DATA_START_POS, p_31960_);
    }

    protected void defineSynchedData() {
        this.entityData.define(DATA_START_POS, BlockPos.ZERO);
        this.entityData.define(BLOCK_STATE, ModBlocks.PEDESTAL.get().defaultBlockState());
        this.entityData.define(DURATION, 0);
        this.entityData.define(ALIVE_TICKS, 0);
    }

    public BlockState getBlock() {
        BlockState bsOp = this.getEntityData().get(BLOCK_STATE);
        return bsOp;
    }

    public void setBlock(BlockState block) {
        this.getEntityData().set(BLOCK_STATE, block);
    }

    public void tick() {
        this.noPhysics = true;
        this.setDeltaMovement(0D, 0D, 0D);

        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(0D, 0D, 0D);
        this.durationO = this.duration;
        this.aliveTicksO = this.aliveTicks;
        if (!level.isClientSide) {
            this.setAliveTicks(this.getAliveTicks() + 1);
            if (this.getDurationData() <= 0)
                this.discard();
            else this.setDuration(this.getDurationData() - 1);
        } else {
            if (this.getBlockState().is(ModBlocks.DARK_ALTAR.get())) {
                for (Player player : this.level.getEntitiesOfClass(Player.class, new AABB(this.blockPosition()).inflate(60.0D))) {
                    ClientLevelExpandedContext context = ((ClientLevelInterface) player.level).revelationfix$ECData();
                    context.teEndRitualRunning = true;
                    context.teEndRitualBE = this.blockPosition.offset(0, -2, 0);
                }
            }
        }
        this.duration = this.getDurationData();
        this.aliveTicks = this.getAliveTicks();
    }

    @Override
    public void onClientRemoval() {
        if (this.getBlockState().is(ModBlocks.DARK_ALTAR.get())) {
            for (Player player : this.level.getEntitiesOfClass(Player.class, new AABB(this.blockPosition()).inflate(60.0D))) {
                ClientLevelExpandedContext context = ((ClientLevelInterface) player.level).revelationfix$ECData();
                context.teEndRitualRunning = false;
                context.teEndRitualBE = null;
            }
        }
        super.onClientRemoval();
    }

    public float getProgress(float partials) {
        float max = 30;
        float pro = 0F;
        if (this.getBlockState().is(ModBlocks.PEDESTAL.get())) {
            if (this.aliveTicks < max) {
                pro = Mth.lerp(partials, aliveTicksO, aliveTicks) / max;

            } else {
                pro = 1.0F;
            }
            if (this.duration <= max) {
                pro *= Mth.lerp(partials, durationO, duration) / max;
            }
        } else {
            float f = Mth.lerp(partials, aliveTicksO, aliveTicks);
            if (this.aliveTicks < max) {
                pro = f / max;
            } else {
                pro = Mth.abs(Mth.cos(f / 15.0F));
            }
            if (this.duration <= max) {
                pro *= Mth.lerp(partials, durationO, duration) / max;
            }
        }
        return pro;
    }

    public void setDuration(int time) {
        this.entityData.set(DURATION, time);
    }

    public int getDurationData() {
        return this.entityData.get(DURATION);
    }

    public int getAliveTicks() {
        return this.entityData.get(ALIVE_TICKS);
    }

    public void setAliveTicks(int time) {
        this.entityData.set(ALIVE_TICKS, time);
    }

    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        BlockState blockState = this.getBlock();
        if (blockState != null) {
            compoundTag.put("block", NbtUtils.writeBlockState(blockState));
        }

        compoundTag.putInt("Time", this.getDurationData());
        compoundTag.putInt("AliveTicks", this.getAliveTicks());
    }

    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        Tag blockStateCompound = compoundTag.get("block");
        if (blockStateCompound != null) {
            BlockPos pos = NbtUtils.readBlockPos((CompoundTag) blockStateCompound);
            this.setBlock(level().getBlockState(pos));
        }
        if (compoundTag.contains("Time"))
            this.setDuration(compoundTag.getInt("Time"));
        if (compoundTag.contains("AliveTicks"))
            this.setAliveTicks(compoundTag.getInt("AliveTicks"));
    }

    public boolean displayFireAnimation() {
        return false;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource p_20122_) {
        return !p_20122_.is(DamageTypeTags.BYPASSES_INVULNERABILITY);
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    public BlockState getBlockState() {
        return this.getEntityData().get(BLOCK_STATE);
    }

    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        this.noPhysics = true;
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
