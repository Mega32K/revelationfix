package com.mega.revelationfix.common.entity.binding;

import com.Polarice3.Goety.api.items.magic.IWand;
import com.Polarice3.Goety.common.entities.neutral.Owned;
import com.Polarice3.Goety.utils.ModDamageSource;
import com.mega.revelationfix.common.block.blockentity.RuneReactorBlockEntity;
import com.mega.revelationfix.common.compat.Wrapped;
import com.mega.revelationfix.common.init.ModBlocks;
import com.mega.revelationfix.common.init.ModEntities;
import com.mega.revelationfix.util.entity.RotationUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class FakeSpellerEntity extends Owned implements BlockBindingEntity {
    public static final EntityDataAccessor<Optional<UUID>> TARGET_UUID = SynchedEntityData.defineId(FakeSpellerEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    public static EntityDataAccessor<ItemStack> INSERT_WAND = SynchedEntityData.defineId(FakeSpellerEntity.class, EntityDataSerializers.ITEM_STACK);
    public static EntityDataAccessor<BlockPos> REACTOR_POS = SynchedEntityData.defineId(FakeSpellerEntity.class, EntityDataSerializers.BLOCK_POS);

    public FakeSpellerEntity(Level worldIn, ItemStack wand, BlockPos reactorPos) {
        super(ModEntities.FAKE_SPELLER.get(), worldIn);
        this.setWand(wand);
        this.setReactorPos(reactorPos);
    }
    public FakeSpellerEntity(EntityType<? extends Owned> type, Level worldIn) {
        super(type, worldIn);
    }
    public ItemStack getWand() {
        return this.entityData.get(INSERT_WAND);
    }
    public void setWand(ItemStack stack) {
        this.entityData.set(INSERT_WAND, stack);
    }

    @Override
    public void setTarget(@Nullable LivingEntity p_21544_) {
        if (!level.isClientSide) {
            if (p_21544_ != null)
                this.entityData.set(TARGET_UUID, Optional.of(p_21544_.getUUID()));
            else this.entityData.set(TARGET_UUID, Optional.empty());
        }

    }
    public void setTarget(UUID uuid) {
        if (!level.isClientSide) {
            this.entityData.set(TARGET_UUID, Optional.of(uuid));
        }
    }

    public BlockPos getReactorPos() {
        BlockPos pos = this.entityData.get(REACTOR_POS);
        this.customPos = this.position = new Vec3(pos.getX()+0.5, pos.getY()+1, pos.getZ()+0.5);
        return pos;
    }
    public void setReactorPos(BlockPos pos) {
        this.entityData.set(REACTOR_POS, pos);
    }
    public Vec3 customPos = Vec3.ZERO;
    public BlockState getReactorBlockState() {
        return this.level.getBlockState(this.getReactorPos());
    }
    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains("InsertWand", 10)) {
            this.setWand(ItemStack.of(compound.getCompound("InsertWand")));
        }
        if (compound.hasUUID("SpellerTarget"))
            this.setTarget(compound.getUUID("SpellerTarget"));
        ListTag listTag = compound.getList("ReactorPos", 3);
        this.setReactorPos(new BlockPos(listTag.getInt(0), listTag.getInt(1), listTag.getInt(2)));
        super.readAdditionalSaveData(compound);
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.ATTACK_DAMAGE, 1.0D).add(Attributes.MOVEMENT_SPEED, 0.1F).add(Attributes.ATTACK_SPEED).add(Attributes.LUCK).add(net.minecraftforge.common.ForgeMod.BLOCK_REACH.get()).add(Attributes.ATTACK_KNOCKBACK).add(net.minecraftforge.common.ForgeMod.ENTITY_REACH.get()).add(Attributes.FOLLOW_RANGE);
    }
    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.put("InsertWand", this.getWand().save(new CompoundTag()));
        if (this.entityData.get(TARGET_UUID).isPresent())
            compound.putUUID("SpellerTarget", this.entityData.get(TARGET_UUID).get());
        compound.put("ReactorPos", this.newIntList(this.getReactorPos().getX(), this.getReactorPos().getY(), this.getReactorPos().getZ()));
        super.addAdditionalSaveData(compound);
    }
    @Override
    public void defineSynchedData() {
        this.entityData.define(INSERT_WAND, ItemStack.EMPTY);
        this.entityData.define(REACTOR_POS, BlockPos.ZERO);
        this.entityData.define(TARGET_UUID, Optional.empty());
        super.defineSynchedData();
    }

    @Nullable
    @Override
    public LivingEntity getTarget() {
        if (level instanceof ServerLevel serverLevel) {
            if (entityData.get(TARGET_UUID).isPresent() && serverLevel.getEntity(entityData.get(TARGET_UUID).get()) instanceof LivingEntity living) {
                return living;
            } else return super.getTarget();
        } else {
            if (entityData.get(TARGET_UUID).isPresent() && Wrapped.getEntityByUUID(entityData.get(TARGET_UUID).get()) instanceof LivingEntity living) {
                return living;
            } else return super.getTarget();
        }
    }

    @Override
    public void tick() {
        this.customPos = getReactorPos().getCenter().add(0.5, 1, 0.5);
        this.position = customPos;
        this.setBoundingBox(makeBoundingBox());

        RuneReactorBlockEntity reactorBlockEntity = null;
        if (level.getBlockEntity(this.getReactorPos()) instanceof RuneReactorBlockEntity b)
            reactorBlockEntity = b;
        this.blockPosition = new BlockPos((int) customPos.x, (int) customPos.y, (int) customPos.z);
        if (!level.isClientSide) {
            if (!(getWand().getItem() instanceof IWand)) {
                if (this.tickCount > 5) {
                    discard();
                }
                return;
            }
            if (!getReactorBlockState().is(ModBlocks.RUNE_REACTOR.get())) {
                discard();
                return;
            }
            if (reactorBlockEntity != null) {
                this.setTarget(reactorBlockEntity.currentSpellTarget);
            }
            if (this.getTarget() != null) {
                AABB aabb = getTarget().getBoundingBox();
                Vec3 vec3 = aabb.getCenter().add(0, aabb.getYsize()/2.1F, 0);
                RotationUtils.rotationAtoB(this, vec3);

            }
        }
        float xR  = this.getXRot();
        float yR = this.getYRot();
        float yHR = this.getYHeadRot();
        super.tick();
        this.setXRot(xR);
        this.setYRot(yR);
        this.setYHeadRot(yHR);
        this.position = customPos;
    }

    @Override
    public @NotNull Component getName() {
        if ( this.getOwner() instanceof Player player)
            return player.getName();
        return super.getName();
    }

    @Override
    public @NotNull Component getDisplayName() {
        if ( this.getOwner() instanceof Player player)
            return player.getDisplayName();
        return super.getDisplayName();
    }

    @Override
    public float getHealth() {
        if ( this.getOwner() instanceof Player player)
            return player.getHealth();
        return super.getHealth();
    }

    @Override
    public float getMaxHealth() {
        if (this.getOwner() instanceof Player player)
            return player.getHealth();
        return super.getMaxHealth();
    }

    @Nullable
    @Override
    public Team getTeam() {
        if ( this.getOwner() instanceof Player player)
            return player.getTeam();
        return super.getTeam();
    }

    @Override
    protected int getFireImmuneTicks() {
        return 0;
    }
    @Override
    public double getAttributeBaseValue(@NotNull Holder<Attribute> p_248605_) {
        if ( this.getOwner() instanceof Player player && p_248605_.get() != Attributes.FOLLOW_RANGE)
            return player.getAttributeBaseValue(p_248605_);
        return super.getAttributeBaseValue(p_248605_);
    }

    @Override
    public double getAttributeBaseValue(@NotNull Attribute p_21173_) {
        if ( this.getOwner() instanceof Player player && p_21173_ != Attributes.FOLLOW_RANGE)
            return player.getAttributeBaseValue(p_21173_);
        return super.getAttributeBaseValue(p_21173_);
    }

    @Override
    public double getAttributeValue(@NotNull Attribute p_21134_) {
        if (this.getOwner() instanceof Player player && p_21134_ != Attributes.FOLLOW_RANGE)
            return player.getAttributeValue(p_21134_);
        return super.getAttributeValue(p_21134_);
    }

    @Override
    public double getAttributeValue(@NotNull Holder<Attribute> p_251296_) {
        if ( this.getOwner() instanceof Player player && p_251296_.get() != Attributes.FOLLOW_RANGE)
            return player.getAttributeValue(p_251296_);
        return super.getAttributeValue(p_251296_);
    }

    @Override
    public boolean isAlive() {
        if ( this.getOwner() instanceof Player player)
            return player.isAlive();
        return super.isAlive();
    }

    @Override
    public boolean isAlliedTo(@NotNull Team p_20032_) {
        if ( this.getOwner() instanceof Player player)
            return player.isAlliedTo(p_20032_);
        return super.isAlliedTo(p_20032_);
    }

    @Override
    public boolean isAlliedTo(Entity entityIn) {
        if ( this.getOwner() instanceof Player player)
            return player.isAlliedTo(entityIn);
        return super.isAlliedTo(entityIn);
    }

    @Override
    public boolean isDeadOrDying() {
        if ( this.getOwner() instanceof Player player)
            return player.isDeadOrDying();
        return super.isDeadOrDying();
    }

    @Override
    public boolean isInvulnerable() {
        return true;
    }
    protected ListTag newIntList(int... p_20064_) {
        ListTag listtag = new ListTag();

        for(int d0 : p_20064_) {
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
        return getReactorPos().getX()+0.5;
    }
    @Override
    public double getY() {
        return getReactorPos().getY()+1.5;
    }
    @Override
    public double getZ() {
        return getReactorPos().getZ()+0.5;
    }

    @Override
    public double getEyeY() {
        return getY() + 1F;
    }

    @Override
    public double getX(double p_20166_) {
        return this.getX() + (double)this.getBbWidth() * p_20166_;
    }

    @Override
    public double getY(double p_20228_) {
        return this.getY() + (double)this.getBbHeight() * p_20228_;
    }

    @Override
    public double getZ(double p_20247_) {
        return this.getZ() + (double)this.getBbWidth() * p_20247_;
    }

    @Override
    public boolean isInvisible() {
        return true;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public boolean isNoAi() {
        return false;
    }

    @Override
    public boolean canUpdate() {
        return true;
    }

    @Override
    public boolean canBeAffected(MobEffectInstance pPotioneffect) {
        return false;
    }

    @Override
    protected @NotNull AABB makeBoundingBox() {
        BlockPos pos = this.getReactorPos();
        return new AABB(pos.getCenter(), pos.getCenter()).inflate(0.2);
    }

    @Override
    public @NotNull ItemStack getItemBySlot(@NotNull EquipmentSlot p_21467_) {
        if (p_21467_ == EquipmentSlot.MAINHAND) {
            return getWand();
        } else return ItemStack.EMPTY;
    }

    @Override
    public void setItemInHand(@NotNull InteractionHand p_21009_, @NotNull ItemStack p_21010_) {
    }

    @Override
    public boolean hurt(DamageSource p_21016_, float p_21017_) {
        if (p_21016_.is(ModDamageSource.DEATH))
            return true;
        return false;
    }

    @Override
    public void actuallyHurt(DamageSource p_21240_, float p_21241_) {

    }

    @Override
    public void ownedTick() {
    }

    @Override
    public boolean isUsingItem() {
        if (this.level.getBlockEntity(this.getReactorPos()) instanceof RuneReactorBlockEntity blockEntity) {
            if (blockEntity.using && getTarget() != null && getTarget().isAlive()) {
                return true;
            }
        }
        return super.isUsingItem();
    }

    @Override
    public ItemStack getUseItem() {
        this.useItem = getWand();
        return isUsingItem() ? useItem : ItemStack.EMPTY;
    }

    @Override
    public int getUseItemRemainingTicks() {
        if (this.level.getBlockEntity(this.getReactorPos()) instanceof RuneReactorBlockEntity blockEntity) {

            return blockEntity.spellUseTimeRemaining;
        }
        return super.getUseItemRemainingTicks();
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
}
