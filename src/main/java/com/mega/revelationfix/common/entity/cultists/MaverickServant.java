package com.mega.revelationfix.common.entity.cultists;

import com.Polarice3.Goety.api.entities.IAutoRideable;
import com.Polarice3.Goety.api.entities.IOwned;
import com.Polarice3.Goety.api.entities.ally.IServant;
import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.common.advancements.ModCriteriaTriggers;
import com.Polarice3.Goety.common.effects.GoetyEffects;
import com.Polarice3.Goety.common.entities.ai.SummonTargetGoal;
import com.Polarice3.Goety.common.entities.ally.Summoned;
import com.Polarice3.Goety.common.entities.hostile.cultists.Cultist;
import com.Polarice3.Goety.common.entities.hostile.cultists.Maverick;
import com.Polarice3.Goety.common.entities.neutral.Owned;
import com.Polarice3.Goety.common.items.ModItems;
import com.Polarice3.Goety.config.MobsConfig;
import com.Polarice3.Goety.init.ModMobType;
import com.Polarice3.Goety.utils.*;
import com.mega.revelationfix.mixin.PatrollingMonsterAccessor;
import com.mega.revelationfix.mixin.goety.MaverickAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.*;
import net.minecraft.world.scores.Team;
import org.jetbrains.annotations.Nullable;
import z1gned.goetyrevelation.util.ATAHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

/**
 * 独行者
 */
public class MaverickServant extends Maverick implements IServant {
    //Summoned
    protected static final EntityDataAccessor<Byte> SUMMONED_FLAGS;
    protected static final EntityDataAccessor<Byte> UPGRADE_FLAGS;
    //Owned
    protected static final EntityDataAccessor<Optional<UUID>> OWNER_UNIQUE_ID;
    protected static final EntityDataAccessor<Integer> OWNER_CLIENT_ID;
    protected static final EntityDataAccessor<Boolean> HOSTILE;
    protected static final EntityDataAccessor<Boolean> NATURAL;

    static {
        //Summoned
        SUMMONED_FLAGS = SynchedEntityData.defineId(MaverickServant.class, EntityDataSerializers.BYTE);
        UPGRADE_FLAGS = SynchedEntityData.defineId(MaverickServant.class, EntityDataSerializers.BYTE);
        //Owned
        OWNER_UNIQUE_ID = SynchedEntityData.defineId(MaverickServant.class, EntityDataSerializers.OPTIONAL_UUID);
        OWNER_CLIENT_ID = SynchedEntityData.defineId(MaverickServant.class, EntityDataSerializers.INT);
        HOSTILE = SynchedEntityData.defineId(MaverickServant.class, EntityDataSerializers.BOOLEAN);
        NATURAL = SynchedEntityData.defineId(MaverickServant.class, EntityDataSerializers.BOOLEAN);
    }

    //Owned
    private final NearestAttackableTargetGoal<Player> targetGoal = new NearestAttackableTargetGoal(this, Player.class, true);
    //Summoned
    public LivingEntity commandPosEntity;
    public BlockPos commandPos;
    public BlockPos boundPos;
    public int commandTick;
    public int killChance;
    public int noHealTime;
    //Owned
    public boolean limitedLifespan;
    public int limitedLifeTicks;

    public MaverickServant(EntityType<? extends Cultist> type, Level worldIn) {
        super(type, worldIn);
    }

    //Owned
    public static boolean checkHostileSpawnRules(EntityType<? extends Owned> pType, ServerLevelAccessor pLevel, MobSpawnType pReason, BlockPos pPos, RandomSource pRandom) {
        return pLevel.getDifficulty() != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(pLevel, pPos, pRandom) && checkMobSpawnRules(pType, pLevel, pReason, pPos, pRandom);
    }

    //Owned
    public static boolean checkAnyLightMonsterSpawnRules_(EntityType<? extends Owned> pType, LevelAccessor pLevel, MobSpawnType pReason, BlockPos pPos, RandomSource pRandom) {
        return pLevel.getDifficulty() != Difficulty.PEACEFUL && checkMobSpawnRules(pType, pLevel, pReason, pPos, pRandom);
    }

    //Owned
    public static boolean checkDayMonsterSpawnRules(EntityType<? extends Owned> pType, LevelAccessor pLevel, MobSpawnType pReason, BlockPos pPos, RandomSource pRandom) {
        return pLevel.getBrightness(LightLayer.BLOCK, pPos) <= 8 && checkAnyLightMonsterSpawnRules_(pType, pLevel, pReason, pPos, pRandom);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        //Owned
        this.targetSelector.addGoal(1, new Owned.OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new Owned.OwnerHurtTargetGoal(this));
        //Summoned
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.followGoal();
        this.targetSelectGoal();
    }

    public void followGoal() {
        this.goalSelector.addGoal(5, new Summoned.FollowOwnerGoal(this, 1.0, 10.0F, 2.0F));
    }

    public void targetSelectGoal() {
        this.targetSelector.addGoal(1, new SummonTargetGoal(this));
    }

    @Override
    protected void updateControlFlags() {
        boolean flag = !(this.getControllingPassenger() instanceof Mob);
        boolean flag1 = !(this.getVehicle() instanceof Boat);
        this.goalSelector.setControlFlag(Goal.Flag.MOVE, flag);
        this.goalSelector.setControlFlag(Goal.Flag.JUMP, flag && flag1);
        this.goalSelector.setControlFlag(Goal.Flag.LOOK, flag);
        this.goalSelector.setControlFlag(Goal.Flag.TARGET, flag);
    }

    @Override
    public void heal(float p_21116_) {
        if (this.getLifespan() > 0)
            super.heal(p_21116_);
    }

    public void checkDespawn() {
        if (this.isHostile()) {
            super.checkDespawn();
        }

    }

    public ItemStack getProjectile(ItemStack pShootable) {
        if (pShootable.getItem() instanceof ProjectileWeaponItem) {
            Predicate<ItemStack> predicate = ((ProjectileWeaponItem) pShootable.getItem()).getSupportedHeldProjectiles();
            ItemStack itemstack = ProjectileWeaponItem.getHeldProjectile(this, predicate);
            return itemstack.isEmpty() ? new ItemStack(Items.ARROW) : itemstack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.ownedTick();
        servantTick();
    }

    @Override
    public void setTarget(@javax.annotation.Nullable LivingEntity p_21544_) {
        if (this.isPatrolling()) {
            if (p_21544_ != null) {
                if (p_21544_.distanceToSqr(this.vec3BoundPos()) <= (double) Mth.square(GUARDING_RANGE)) {
                    this.normalSetTarget(p_21544_);
                }
            } else {
                this.normalSetTarget(null);
            }
        } else {
            this.normalSetTarget(p_21544_);
        }

    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @javax.annotation.Nullable SpawnGroupData pSpawnData, @javax.annotation.Nullable CompoundTag pDataTag) {
        //Summoned Owned
        pSpawnData = super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
        //Owned
        this.checkHostility();
        if (pReason != MobSpawnType.MOB_SUMMONED && this.getTrueOwner() == null) {
            this.setNatural(true);
        }
        //Summoned
        if (pReason == MobSpawnType.MOB_SUMMONED && this.getTrueOwner() != null && this.getMobType() == MobType.UNDEAD) {
            for (int i = 0; i < pLevel.getLevel().random.nextInt(10) + 10; ++i) {
                pLevel.getLevel().sendParticles(ModParticleTypes.SUMMON.get(), this.getRandomX(1.5), this.getRandomY(), this.getRandomZ(1.5), 0, 0.0, 0.0, 0.0, 1.0);
            }

            pLevel.getLevel().sendParticles(ModParticleTypes.SOUL_EXPLODE.get(), this.getX(), this.getY(), this.getZ(), 0, 0.0, 2.0, 0.0, 1.0);
        }

        if (this.getTrueOwner() != null) {
            this.spawnUpgraded();
        }

        this.setWandering(this.getTrueOwner() == null);
        this.setStaying(false);
        this.setBoundPos(null);
        return pSpawnData;
    }

    public boolean canSpawnArmor() {
        return this.getTrueOwner() != null && CuriosFinder.hasCurio(this.getTrueOwner(), ModItems.RING_OF_THE_FORGE.get());
    }

    protected void populateDefaultEquipmentSlots(RandomSource p_217055_, DifficultyInstance p_217056_) {
        if (this.canSpawnArmor()) {
            this.populateDefaultArmor(p_217055_, p_217056_);
        } else {
            super.populateDefaultEquipmentSlots(p_217055_, p_217056_);
        }

        this.populateDefaultWeapons(p_217055_, p_217056_);
    }

    public void populateDefaultArmor(RandomSource randomSource, DifficultyInstance difficulty) {
        if (this.canSpawnArmor()) {
            EquipmentSlot[] var3 = EquipmentSlot.values();
            int var4 = var3.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                EquipmentSlot equipmentslot = var3[var5];
                if (equipmentslot.getType() == EquipmentSlot.Type.ARMOR) {
                    int i = randomSource.nextInt(2);
                    if (randomSource.nextFloat() < 0.095F) {
                        ++i;
                    }

                    if (randomSource.nextFloat() < 0.095F) {
                        ++i;
                    }

                    if (randomSource.nextFloat() < 0.095F) {
                        ++i;
                    }

                    ItemStack itemstack = this.getItemBySlot(equipmentslot);
                    if (itemstack.isEmpty()) {
                        Item item = getEquipmentForSlot(equipmentslot, i);
                        if (item != null) {
                            this.setItemSlot(equipmentslot, new ItemStack(item));
                            this.setDropChance(equipmentslot, 0.0F);
                        }
                    }
                }
            }
        }

    }

    public void populateDefaultWeapons(RandomSource randomSource, DifficultyInstance difficulty) {
    }

    public void die(DamageSource pCause) {
        if (!this.level.isClientSide && this.hasCustomName() && this.level.getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES) && this.getTrueOwner() instanceof ServerPlayer) {
            this.getTrueOwner().sendSystemMessage(this.getCombatTracker().getDeathMessage());
        }

        super.die(pCause);
    }

    public boolean hurt(DamageSource source, float amount) {
        if (MobsConfig.ServantsMasterImmune.get()) {
            Entity var4 = source.getEntity();
            if (var4 instanceof Summoned summoned) {
                if (!summoned.isHostile() && !this.isHostile() && this.getTrueOwner() != null && summoned.getTrueOwner() == this.getTrueOwner()) {
                    return false;
                }
            }
        }

        boolean flag = super.hurt(source, amount);
        if (flag) {
            this.noHealTime = MathHelper.secondsToTicks(MobsConfig.ServantHealHalt.get());
        }

        return flag;
    }

    public void normalSetTarget(@javax.annotation.Nullable LivingEntity p_21544_) {
        super.setTarget(p_21544_);
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        boolean flag = doHurtTarget_owned(entityIn);
        if (flag) {
            if (this.getMobType() == MobType.UNDEAD) {
                float f = this.level.getCurrentDifficultyAt(this.blockPosition()).getEffectiveDifficulty();
                if (this.getMainHandItem().isEmpty() && this.isOnFire() && this.random.nextFloat() < f * 0.3F) {
                    entityIn.setSecondsOnFire(2 * (int) f);
                }
            }

            if (!this.getMainHandItem().isEmpty() && this.getMainHandItem().isDamageableItem()) {
                ItemHelper.hurtAndBreak(this.getMainHandItem(), 1, this);
            }
        }

        return flag;
    }

    //Owned
    public boolean doHurtTarget_owned(Entity entity) {
        if (this.getTrueOwner() != null) {
            float f = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
            float f1 = (float) this.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
            if (entity instanceof LivingEntity) {
                f += EnchantmentHelper.getDamageBonus(this.getMainHandItem(), ((LivingEntity) entity).getMobType());
                f1 += (float) EnchantmentHelper.getKnockbackBonus(this);
            }

            int i = EnchantmentHelper.getFireAspect(this);
            if (i > 0) {
                entity.setSecondsOnFire(i * 4);
            }
            boolean flag = this.doHurtTarget(f, entity);
            if (flag) {
                if (f1 > 0.0F && entity instanceof LivingEntity) {
                    ((LivingEntity) entity).knockback(f1 * 0.5F, Mth.sin(this.getYRot() * 0.017453292F), -Mth.cos(this.getYRot() * 0.017453292F));
                    this.setDeltaMovement(this.getDeltaMovement().multiply(0.6, 1.0, 0.6));
                }

                if (entity instanceof Player player) {
                    this.maybeDisableShield(player, this.getMainHandItem(), player.isUsingItem() ? player.getUseItem() : ItemStack.EMPTY);
                }

                this.doEnchantDamageEffects(this, entity);
                this.setLastHurtMob(entity);
            }

            return flag;
        } else {
            boolean flag = super.doHurtTarget(entity);
            specialHurtTargetEffect(flag, entity);
            return flag;
        }
    }

    //Owned
    public void maybeDisableShield(Player player, ItemStack axe, ItemStack shield) {
        if (!axe.isEmpty() && !shield.isEmpty() && axe.getItem() instanceof AxeItem && shield.is(Items.SHIELD)) {
            float f = 0.25F + (float) EnchantmentHelper.getBlockEfficiency(this) * 0.05F;
            if (this.random.nextFloat() < f) {
                player.getCooldowns().addCooldown(Items.SHIELD, 100);
                this.level.broadcastEntityEvent(player, (byte) 30);
            }
        }

    }

    //Owned
    public boolean doHurtTarget(float amount, Entity target) {
        boolean flag = target.hurt(ModDamageSource.summonAttack(this, this.getTrueOwner()), amount);
        specialHurtTargetEffect(flag, target);
        return flag;
    }

    public void specialHurtTargetEffect(boolean flag, Entity target) {
        //Maverick效果
        List<MobEffectInstance> effectInstanceList = new ArrayList<>();
        Potion potion = Potions.HARMING;
        if (flag && target instanceof LivingEntity livingEntity) {
            if (livingEntity instanceof Raider raider && this.hasActiveRaid() && raider.getTarget() != this) {
                double attack = this.getAttributeValue(Attributes.ATTACK_DAMAGE);
                if (livingEntity.getHealth() <= attack + 1.0D) {
                    potion = Potions.HEALING;
                } else {
                    potion = Potions.REGENERATION;
                }
                this.setTarget(null);
            } else {
                if (!livingEntity.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
                    potion = Potions.SLOWNESS;
                } else if (livingEntity.isInvertedHealAndHarm()) {
                    potion = Potions.HEALING;
                }
                boolean isHaloOwner = this.getTrueOwner() != null && ATAHelper.hasHalo(this.getTrueOwner());
                //Custom

                effectInstanceList.addAll(potion.getEffects());
                effectInstanceList.add(new MobEffectInstance(MobEffects.WEAKNESS, 1800, isNetherStaffSummoned() ? 1 : (isHaloOwner ? 3 : 0)));
                effectInstanceList.add(new MobEffectInstance(MobEffects.POISON, 900, isNetherStaffSummoned() ? 1 : (isHaloOwner ? 3 : 0)));
                if (isHaloOwner)
                    effectInstanceList.add(new MobEffectInstance(GoetyEffects.CURSED.get(), 600));
            }
            for (MobEffectInstance instance : effectInstanceList) {
                livingEntity.addEffect(new MobEffectInstance(instance));
            }
            if (!livingEntity.isSprinting()) {
                MaverickAccessor accessor = (MaverickAccessor) this;
                if (accessor.fleeTime() <= 0) {
                    accessor.setFleeTime(MathHelper.secondsToTicks(1));
                }
            }
        }
    }

    //Owned
    @javax.annotation.Nullable
    public Team getTeam() {
        if (this.getTrueOwner() != null) {
            LivingEntity livingentity = this.getTrueOwner();
            if (livingentity != null && livingentity != this && !this.areOwnedByEachOther(livingentity) && livingentity.getTeam() != null) {
                return livingentity.getTeam();
            }
        }

        return super.getTeam();
    }

    //owned
    public boolean areOwnedByEachOther(LivingEntity livingEntity) {
        if (!(livingEntity instanceof IOwned owned)) {
            return false;
        } else {
            return owned.getTrueOwner() == this && this.getTrueOwner() == livingEntity;
        }
    }

    //Owned
    public boolean isAlliedTo(Entity entityIn) {
        if (this.getTrueOwner() == null) {
            return super.isAlliedTo(entityIn);
        } else {
            LivingEntity trueOwner = this.getTrueOwner();
            boolean var10000;
            if (!trueOwner.isAlliedTo(entityIn) && !entityIn.isAlliedTo(trueOwner) && entityIn != trueOwner) {
                label55:
                {
                    if (entityIn instanceof IOwned owned) {
                        if (MobUtil.ownerStack(this, owned)) {
                            break label55;
                        }
                    }

                    if (entityIn instanceof OwnableEntity ownable) {
                        if (ownable.getOwner() == trueOwner) {
                            break label55;
                        }
                    }

                    if (trueOwner instanceof Player player) {
                        if (entityIn instanceof LivingEntity livingEntity) {
                            if (SEHelper.getAllyEntities(player).contains(livingEntity) || SEHelper.getAllyEntityTypes(player).contains(livingEntity.getType())) {
                                break label55;
                            }
                        }
                    }

                    var10000 = false;
                    return var10000;
                }
            }

            var10000 = true;
            return var10000;
        }
    }

    @Override
    public void hurtArmor(DamageSource pDamageSource, float pDamage) {
        if (!(pDamage <= 0.0F)) {
            pDamage /= 4.0F;
            if (pDamage < 1.0F) {
                pDamage = 1.0F;
            }

            EquipmentSlot[] var3 = EquipmentSlot.values();
            int var4 = var3.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                EquipmentSlot equipmentSlotType = var3[var5];
                if (equipmentSlotType.getType() == EquipmentSlot.Type.ARMOR) {
                    ItemStack itemstack = this.getItemBySlot(equipmentSlotType);
                    if ((!pDamageSource.is(DamageTypeTags.IS_FIRE) || !itemstack.getItem().isFireResistant()) && itemstack.getItem() instanceof ArmorItem) {
                        itemstack.hurtAndBreak((int) pDamage, this, (p_214023_1_) -> {
                            p_214023_1_.broadcastBreakEvent(equipmentSlotType);
                        });
                    }
                }
            }
        }

    }

    @Override
    public void defineSynchedData() {
        super.defineSynchedData();
        //Owned
        this.entityData.define(OWNER_UNIQUE_ID, Optional.empty());
        this.entityData.define(OWNER_CLIENT_ID, -1);
        this.entityData.define(HOSTILE, false);
        this.entityData.define(NATURAL, false);
        //Summoned
        this.entityData.define(SUMMONED_FLAGS, (byte) 0);
        this.entityData.define(UPGRADE_FLAGS, (byte) 0);
    }

    private boolean getFlag(int mask) {
        int i = this.entityData.get(SUMMONED_FLAGS);
        return (i & mask) != 0;
    }

    private void setFlags(int mask, boolean value) {
        int i = this.entityData.get(SUMMONED_FLAGS);
        if (value) {
            i |= mask;
        } else {
            i &= ~mask;
        }

        this.entityData.set(SUMMONED_FLAGS, (byte) (i & 255));
    }

    public boolean isWandering() {
        return this.getFlag(1);
    }

    public void setWandering(boolean wandering) {
        this.setFlags(1, wandering);
    }

    public boolean isStaying() {
        return this.getFlag(2) && !this.isCommanded() && !this.isVehicle();
    }

    public void setStaying(boolean staying) {
        this.setFlags(2, staying);
    }

    public boolean isNetherStaffSummoned() {
        return this.getFlag(4);
    }

    public void setNetherStaffSummoned(boolean z) {
        this.setFlags(4, z);
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        //Owned
        this.readOwnedData(compound);
        this.setConfigurableAttributes();
        //Summoned
        this.readServantData(compound);
        //下界魔杖召唤
        if (compound.contains("netherStaff")) {
            this.setNetherStaffSummoned(compound.getBoolean("netherStaff"));
        }
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        //Owned
        this.saveOwnedData(compound);
        //Summoned
        this.saveServantData(compound);
        //下界魔杖
        compound.putBoolean("netherStaff", this.isNetherStaffSummoned());
    }

    public boolean canUpdateMove() {
        return this.getMobType() == MobType.UNDEAD || this.getMobType() == ModMobType.NATURAL;
    }

    public boolean isUpgraded() {
        return (this.entityData.get(UPGRADE_FLAGS) & 4) != 0;
    }

    public void setUpgraded(boolean upgraded) {
        byte b0 = this.entityData.get(UPGRADE_FLAGS);
        if (upgraded) {
            this.entityData.set(UPGRADE_FLAGS, (byte) (b0 | 4));
        } else {
            this.entityData.set(UPGRADE_FLAGS, (byte) (b0 & -5));
        }

    }

    public void upgrade() {
        this.setUpgraded(true);
    }

    public void downgrade() {
        this.setUpgraded(false);
    }

    public void setCommandPos(BlockPos blockPos, boolean removeEntity) {
        if (removeEntity) {
            this.commandPosEntity = null;
        }

        this.commandPos = blockPos;
        this.setCommandTick(MathHelper.secondsToTicks(10));
    }

    public BlockPos getCommandPos() {
        return this.commandPos;
    }

    @javax.annotation.Nullable
    public LivingEntity getCommandPosEntity() {
        return this.commandPosEntity;
    }

    public void setCommandPosEntity(@javax.annotation.Nullable LivingEntity living) {
        this.commandPosEntity = living;
        if (living != null) {
            this.setCommandPos(living.blockPosition(), false);
        }

    }

    public int getCommandTick() {
        return this.commandTick;
    }

    public void setCommandTick(int commandTick) {
        this.commandTick = commandTick;
    }

    @Override
    public boolean isCommanded() {
        return this.commandPos != null;
    }

    public void dropEquipment(EquipmentSlot equipmentSlot, ItemStack stack) {
        if (this.getEquipmentDropChance(equipmentSlot) > 0.0F) {
            this.spawnAtLocation(stack);
        }

    }

    public BlockPos getBoundPos() {
        return this.boundPos;
    }

    public void setBoundPos(BlockPos blockPos) {
        this.boundPos = blockPos;
    }

    public boolean isMoving() {
        return !(this.walkAnimation.speed() < 0.01F);
    }

    public int getNoHealTime() {
        return this.noHealTime;
    }

    public void setNoHealTime(int time) {
        this.noHealTime = time;
    }

    public int getKillChance() {
        return this.killChance;
    }

    public void setKillChance(int killChance) {
        this.killChance = killChance;
    }
    //Owned

    public void warnKill(Player player) {
        this.killChance = 60;
        player.displayClientMessage(Component.translatable("info.goety.servant.tryKill", this.getDisplayName()), true);
    }

    public void tryKill(Player player) {
        this.kill();
    }

    //Owned
    public boolean isInvisibleTo(Player p_20178_) {
        return p_20178_ != this.getMasterOwner() && super.isInvisibleTo(p_20178_);
    }

    //Owned
    public void aiStep() {
        this.updateSwingTime();
        if (this.isHostile()) {
            this.updateNoActionTime();
        }

        super.aiStep();
    }

    protected void updateNoActionTime() {
        float f = this.getLightLevelDependentMagicValue();
        if (f > 0.5F) {
            this.noActionTime += 2;
        }

    }

    //Owned
    public void setHasLifespan(boolean lifespan) {
        this.limitedLifespan = lifespan;
    }

    //Owned
    public boolean hasLifespan() {
        return this.limitedLifespan;
    }

    //Owned
    public int getLifespan() {
        return this.limitedLifeTicks;
    }

    //Owned
    public void setLifespan(int lifespan) {
        this.limitedLifeTicks = lifespan;
    }

    //Owned
    public void convertNewEquipment(Entity entity) {
        this.populateDefaultEquipmentSlots(this.random, this.level.getCurrentDifficultyAt(this.blockPosition()));
    }

    //Owned
    @javax.annotation.Nullable
    public EntityType<?> getVariant(Level level, BlockPos blockPos) {
        return this.getType();
    }

    //Owned
    @javax.annotation.Nullable
    public LivingEntity getTrueOwner() {
        if (!this.level.isClientSide) {
            UUID uuid = this.getOwnerId();
            return uuid == null ? null : EntityFinder.getLivingEntityByUuiD(this.level, uuid);
        } else {
            int id = this.getOwnerClientId();
            LivingEntity var10000;
            if (id <= -1) {
                var10000 = null;
            } else {
                Entity var3 = this.level.getEntity(this.getOwnerClientId());
                if (var3 instanceof LivingEntity living) {
                    if (living != this) {
                        var10000 = living;
                        return var10000;
                    }
                }

                var10000 = null;
            }

            return var10000;
        }
    }

    //Owned
    public void setTrueOwner(@javax.annotation.Nullable LivingEntity livingEntity) {
        if (livingEntity != null) {
            this.setOwnerId(livingEntity.getUUID());
            this.setOwnerClientId(livingEntity.getId());
        }

    }

    //Owned
    @javax.annotation.Nullable
    public LivingEntity getMasterOwner() {
        LivingEntity var2 = this.getTrueOwner();
        if (var2 instanceof IOwned owned) {
            return owned.getTrueOwner();
        } else {
            return this.getTrueOwner();
        }
    }

    //Owned
    @javax.annotation.Nullable
    public UUID getOwnerUUID() {
        return this.getOwnerId();
    }

    //Owned
    @javax.annotation.Nullable
    public LivingEntity getOwner() {
        return this.getTrueOwner();
    }

    //Owned
    @javax.annotation.Nullable
    public UUID getOwnerId() {
        return (UUID) ((Optional) this.entityData.get(OWNER_UNIQUE_ID)).orElse(null);
    }

    //Owned
    public void setOwnerId(@javax.annotation.Nullable UUID p_184754_1_) {
        this.entityData.set(OWNER_UNIQUE_ID, Optional.ofNullable(p_184754_1_));
    }

    //Owned
    public int getOwnerClientId() {
        return this.entityData.get(OWNER_CLIENT_ID);
    }

    //Owned
    public void setOwnerClientId(int id) {
        this.entityData.set(OWNER_CLIENT_ID, id);
    }

    //Owned
    public void addTargetGoal() {
        this.targetSelector.addGoal(2, this.targetGoal);
    }

    //Owned
    public boolean isHostile() {
        return this.entityData.get(HOSTILE);
    }

    //Owned
    public void setHostile(boolean hostile) {
        this.entityData.set(HOSTILE, hostile);
        this.addTargetGoal();
    }

    //Owned
    public boolean isNatural() {
        return this.entityData.get(NATURAL);
    }

    //Owned
    public void setNatural(boolean natural) {
        this.entityData.set(NATURAL, natural);
    }

    //Owned
    public boolean canBeAffected(MobEffectInstance pPotioneffect) {
        return pPotioneffect.getEffect() != GoetyEffects.GOLD_TOUCHED.get() && super.canBeAffected(pPotioneffect);
    }

    //Owned
    public int getExperienceReward() {
        if (this.isHostile()) {
            this.xpReward = this.xpReward();
        }

        return super.getExperienceReward();
    }

    //Owned
    public int xpReward() {
        return 5;
    }

    //Owned
    public void push(Entity p_21294_) {
        if (!this.level.isClientSide && p_21294_ != this.getTrueOwner()) {
            super.push(p_21294_);
        }

    }

    //Owned
    public void doPush(Entity p_20971_) {
        if (!this.level.isClientSide && p_20971_ != this.getTrueOwner()) {
            super.doPush(p_20971_);
        }

    }

    //Owned
    public boolean canCollideWith(Entity p_20303_) {
        return p_20303_ != this.getTrueOwner() && super.canCollideWith(p_20303_);
    }

    //Owned
    protected boolean shouldDespawnInPeaceful() {
        return this.isHostile();
    }

    //Owned
    public boolean removeWhenFarAway(double p_27519_) {
        return this.isHostile();
    }

    //Owned
    public void awardKillScore(Entity entity, int p_19954_, DamageSource damageSource) {
        super.awardKillScore(entity, p_19954_, damageSource);
        LivingEntity var5 = this.getMasterOwner();
        if (var5 instanceof ServerPlayer serverPlayer) {
            ModCriteriaTriggers.SERVANT_KILLED_ENTITY.trigger(serverPlayer, entity, damageSource);
        }

    }

    //Owned
    public boolean isVehicle() {
        if (this instanceof IAutoRideable rideable) {
            if (rideable.isAutonomous()) {
                return false;
            }
        }

        return super.isVehicle();
    }

    @Override
    public boolean isPatrolling() {
        return ((PatrollingMonsterAccessor) this).patrolling();
    }
}
