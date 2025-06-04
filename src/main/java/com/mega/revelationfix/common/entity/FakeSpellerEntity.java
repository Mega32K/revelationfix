package com.mega.revelationfix.common.entity;

import com.Polarice3.Goety.api.items.magic.IWand;
import com.Polarice3.Goety.common.entities.neutral.Owned;
import com.Polarice3.Goety.common.magic.spells.SoulBoltSpell;
import com.mega.revelationfix.common.block.RuneReactorBlock;
import com.mega.revelationfix.common.init.ModAttributes;
import com.mega.revelationfix.common.init.ModBlocks;
import com.mega.revelationfix.common.init.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FakeSpellerEntity extends Owned {
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
    public BlockPos getReactorPos() {
        BlockPos pos = this.entityData.get(REACTOR_POS);
        this.customPos = this.position = new Vec3(pos.getX()+0.5, pos.getY(), pos.getZ()+0.5);
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
        ListTag listTag = compound.getList("ReactorPos", 3);
        this.setReactorPos(new BlockPos(listTag.getInt(0), listTag.getInt(1), listTag.getInt(2)));
        super.readAdditionalSaveData(compound);
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.ATTACK_DAMAGE, 1.0D).add(Attributes.MOVEMENT_SPEED, (double) 0.1F).add(Attributes.ATTACK_SPEED).add(Attributes.LUCK).add(net.minecraftforge.common.ForgeMod.BLOCK_REACH.get()).add(Attributes.ATTACK_KNOCKBACK).add(net.minecraftforge.common.ForgeMod.ENTITY_REACH.get()).add(Attributes.FOLLOW_RANGE);
    }
    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.put("InsertWand", this.getWand().save(new CompoundTag()));
        compound.put("ReactorPos", this.newIntList(this.getReactorPos().getX(), this.getReactorPos().getY(), this.getReactorPos().getZ()));
        super.addAdditionalSaveData(compound);
    }
    @Override
    public void defineSynchedData() {
        this.entityData.define(INSERT_WAND, ItemStack.EMPTY);
        this.entityData.define(REACTOR_POS, BlockPos.ZERO);
        super.defineSynchedData();
    }
    @Override
    public void tick() {
        this.customPos = getReactorPos().getCenter().add(0.5, 0, 0.5);
        this.position = customPos;
        this.blockPosition = new BlockPos((int) customPos.x, (int) customPos.y, (int) customPos.z);
        if (!level.isClientSide) {
            if (!(getWand().getItem() instanceof IWand)) {
                if (this.tickCount > 5)
                    discard();
                return;
            }
            if (getReactorBlockState().is(ModBlocks.RUNE_REACTOR.get())) {
                discard();
                return;
            }
        }

        super.tick();
        this.position = customPos;
    }

    @Override
    public Component getName() {
        if ( this.getOwner() instanceof Player)
            return this.getOwner().getName();
        return super.getName();
    }

    @Override
    public Component getDisplayName() {
        if ( this.getOwner() instanceof Player)
            return this.getOwner().getDisplayName();
        return super.getDisplayName();
    }

    @Override
    public float getHealth() {
        if ( this.getOwner() instanceof Player)
            return this.getOwner().getHealth();
        return super.getHealth();
    }

    @Nullable
    @Override
    public Team getTeam() {
        if ( this.getOwner() instanceof Player)
            return this.getOwner().getTeam();
        return super.getTeam();
    }

    @Override
    protected int getFireImmuneTicks() {
        return 0;
    }
    @Override
    public double getAttributeBaseValue(Holder<Attribute> p_248605_) {
        if ( this.getOwner() instanceof Player && p_248605_.get() != Attributes.FOLLOW_RANGE)
            return this.getOwner().getAttributeBaseValue(p_248605_);
        return super.getAttributeBaseValue(p_248605_);
    }

    @Override
    public double getAttributeBaseValue(Attribute p_21173_) {
        if ( this.getOwner() instanceof Player && p_21173_ != Attributes.FOLLOW_RANGE)
            return this.getOwner().getAttributeBaseValue(p_21173_);
        return super.getAttributeBaseValue(p_21173_);
    }

    @Override
    public double getAttributeValue(Attribute p_21134_) {
        if (this.getOwner() instanceof Player && p_21134_ != Attributes.FOLLOW_RANGE)
            return this.getOwner().getAttributeValue(p_21134_);
        return super.getAttributeValue(p_21134_);
    }

    @Override
    public double getAttributeValue(Holder<Attribute> p_251296_) {
        if ( this.getOwner() instanceof Player && p_251296_.get() != Attributes.FOLLOW_RANGE)
            return this.getOwner().getAttributeValue(p_251296_);
        return super.getAttributeValue(p_251296_);
    }

    @Override
    public boolean isAlive() {
        if ( this.getOwner() instanceof Player)
            return this.getOwner().isAlive();
        return super.isAlive();
    }

    @Override
    public boolean isAlliedTo(Team p_20032_) {
        if ( this.getOwner() instanceof Player)
            return this.getOwner().isAlliedTo(p_20032_);
        return super.isAlliedTo(p_20032_);
    }

    @Override
    public boolean isAlliedTo(Entity entityIn) {
        if ( this.getOwner() instanceof Player)
            return this.getOwner().isAlliedTo(entityIn);
        return super.isAlliedTo(entityIn);
    }

    @Override
    public boolean isAlwaysTicking() {
        return true;
    }

    @Override
    public boolean isDeadOrDying() {
        if ( this.getOwner() instanceof Player)
            return this.getOwner().isDeadOrDying();
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
    public Vec3 position() {
        return customPos;
    }

    @Override
    public double getX() {
        return getReactorPos().getX()+0.5;
    }
    @Override
    public double getY() {
        return getReactorPos().getY()+0.5;
    }
    @Override
    public double getZ() {
        return getReactorPos().getZ()+0.5;
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
        return new AABB(this.getReactorPos());
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
    public void ownedTick() {
    }
}
