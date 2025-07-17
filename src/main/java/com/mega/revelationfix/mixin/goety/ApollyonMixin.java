package com.mega.revelationfix.mixin.goety;

import com.Polarice3.Goety.common.entities.boss.Apostle;
import com.Polarice3.Goety.common.entities.hostile.cultists.SpellCastingCultist;
import com.Polarice3.Goety.common.entities.projectiles.DeathArrow;
import com.Polarice3.Goety.common.entities.projectiles.FireTornado;
import com.Polarice3.Goety.common.entities.projectiles.NetherMeteor;
import com.Polarice3.Goety.common.entities.projectiles.SpellEntity;
import com.Polarice3.Goety.common.entities.util.AbstractTrap;
import com.Polarice3.Goety.config.AttributesConfig;
import com.Polarice3.Goety.config.MobsConfig;
import com.Polarice3.Goety.init.ModSounds;
import com.mega.endinglib.mixin.accessor.AccessorEntity;
import com.mega.endinglib.mixin.accessor.AccessorLivingEntity;
import com.mega.revelationfix.common.apollyon.common.*;
import com.mega.revelationfix.common.compat.SafeClass;
import com.mega.revelationfix.common.config.CommonConfig;
import com.mega.revelationfix.common.config.ModpackCommonConfig;
import com.mega.revelationfix.common.entity.boss.ApostleServant;
import com.mega.revelationfix.common.entity.projectile.GungnirSpearEntity;
import com.mega.revelationfix.common.init.GRItems;
import com.mega.revelationfix.common.init.ModAttributes;
import com.mega.revelationfix.common.item.tool.combat.bow.BowOfRevelationItem;
import com.mega.revelationfix.common.item.curios.TheNeedleItem;
import com.mega.revelationfix.mixin.MobAccessor;
import com.mega.revelationfix.safe.*;
import com.mega.revelationfix.safe.entity.*;
import com.mega.revelationfix.util.entity.ATAHelper2;
import com.mega.revelationfix.util.LivingEntityEC;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import z1gned.goetyrevelation.config.ModConfig;
import z1gned.goetyrevelation.data.DefeatApollyonInNetherState;
import z1gned.goetyrevelation.item.AscensionHalo;
import z1gned.goetyrevelation.item.ModItems;
import z1gned.goetyrevelation.util.ApollyonAbilityHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings("WrongEntityDataParameterClass")
@Mixin(Apostle.class)
public abstract class ApollyonMixin extends SpellCastingCultist implements Apollyon2Interface {
    @Unique
    private static final EntityDataAccessor<Float> PIHOPJUBVLOBWXLJBSFVELGVBLEJCWB = SynchedEntityData.defineId(Apostle.class, EntityDataSerializers.FLOAT);
    @Unique
    private static final EntityDataAccessor<Integer> HIT_COOLDOWN2 = SynchedEntityData.defineId(Apostle.class, EntityDataSerializers.INT);
    @Unique
    private static final EntityDataAccessor<Integer> DEATH_TIME = SynchedEntityData.defineId(Apostle.class, EntityDataSerializers.INT);
    @Unique
    private static final EntityDataAccessor<Boolean> ILLUSION_MODE = SynchedEntityData.defineId(Apostle.class, EntityDataSerializers.BOOLEAN);
    @Shadow(remap = false)
    public int antiRegen;
    @Shadow(remap = false)
    public DamageSource deathBlow;
    @Shadow(remap = false)
    public int antiRegenTotal;
    @Shadow(remap = false)
    public int deathTime;
    @Unique
    private ApollyonExpandedContext revelationfix$apollyonEC;
    @Unique
    private int clientSideIllusionTicks;
    @Unique
    private Vec3[][] clientSideIllusionOffsets;
    @Shadow(remap = false)
    private boolean regen;

    public ApollyonMixin(EntityType<? extends SpellCastingCultist> type, Level p_i48551_2_) {
        super(type, p_i48551_2_);
    }

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void clinit(CallbackInfo ci) {
        DeathPerformance.LEFTTIME = SynchedEntityData.defineId(Apostle.class, EntityDataSerializers.INT);
        DeathPerformance.DEATH_GROWING_TIME = SynchedEntityData.defineId(Apostle.class, EntityDataSerializers.INT);
        DeathPerformance.FINAL_DEATH_TIME = SynchedEntityData.defineId(Apostle.class, EntityDataSerializers.INT);
        DeathPerformance.FLAGS = SynchedEntityData.defineId(Apostle.class, EntityDataSerializers.BYTE);
        AttackDamageChangeHandler.LIMIT_TIME = SynchedEntityData.defineId(Apostle.class, EntityDataSerializers.INT);
    }

    @Inject(remap = false, method = "teleport", at = @At("HEAD"), cancellable = true)
    private void smiteModeTeleport0(CallbackInfo ci) {
        if (isSmited() && revelationfix$asApollyonHelper().allTitlesApostle_1_20_1$isApollyon()) {
            if (random.nextBoolean()) ci.cancel();
        }
    }

    @ModifyConstant(remap = false, method = "teleport", constant = @Constant(doubleValue = 32.0))
    private double smiteModeTeleport1(double srcDistance) {
        return isSmited() && revelationfix$asApollyonHelper().allTitlesApostle_1_20_1$isApollyon() ? srcDistance / 2F : srcDistance;
    }

    @Override
    public boolean randomTeleport(double p_20985_, double p_20986_, double p_20987_, boolean p_20988_) {
        ApollyonAbilityHelper helper = revelationfix$asApollyonHelper();
        if (helper.allTitlesApostle_1_20_1$isApollyon() && this.isInNether()) {
            if (helper.getDoom())
                helper.allTitlesApostle_1_20_1$setShooting(false);
            if (this.getY() >= 99.5F)
                return super.randomTeleport(p_20985_, Math.max(this.getX(), 129.0F), p_20987_, p_20988_);
        }
        return super.randomTeleport(p_20985_, p_20986_, p_20987_, p_20988_);
    }

    @Shadow(remap = false)
    public abstract boolean isInNether();

    @Shadow
    protected abstract void populateDefaultEquipmentSlots(@NotNull RandomSource randomSource, @NotNull DifficultyInstance difficulty);

    @Shadow(remap = false)
    public abstract boolean isSecondPhase();

    @Shadow(remap = false)
    protected abstract SoundEvent getTrueDeathSound();

    @Shadow(remap = false)
    public abstract AbstractArrow getArrow(ItemStack pArrowStack, float pDistanceFactor);

    @Shadow
    public abstract void tick();

    @Shadow
    protected abstract boolean shouldDespawnInPeaceful();

    @Shadow(remap = false)
    public abstract boolean isSmited();

    @Shadow(remap = false) private boolean killedPlayer;

    @Shadow(remap = false) private int lastKilledPlayer;

    @Override
    public int revelaionfix$getHitCooldown() {
        return entityData.get(HIT_COOLDOWN2);
    }

    @Override
    public void revelaionfix$setHitCooldown(int time) {
        if (!revelationfix$asApollyonHelper().allTitlesApostle_1_20_1$isApollyon()) time = 0;
        this.entityData.set(HIT_COOLDOWN2, time);
    }

    @Inject(method = "defineSynchedData", at = @At("HEAD"))
    private void defineSynchedData(CallbackInfo ci) {
        this.entityData.define(DEATH_TIME, 0);
        this.entityData.define(HIT_COOLDOWN2, 0);
        this.entityData.define(PIHOPJUBVLOBWXLJBSFVELGVBLEJCWB, ModConfig.APOLLYON_HEALTH.get().floatValue());
        this.entityData.define(ILLUSION_MODE, false);
        this.entityData.define(DeathPerformance.LEFTTIME, -1);
        this.entityData.define(DeathPerformance.DEATH_GROWING_TIME, 0);
        this.entityData.define(DeathPerformance.FINAL_DEATH_TIME, -1);
        this.entityData.define(DeathPerformance.FLAGS, (byte) 0);
        this.entityData.define(AttackDamageChangeHandler.LIMIT_TIME, AttackDamageChangeHandler.vanillaLimitTime);
        //ApollyonSynchedEntityData.hackData(revelationfix$asApostle(), this.entityData);
    } 
    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    private void readAdditionalSaveData(CompoundTag pCompound, CallbackInfo ci) {
        Apostle apostle = revelationfix$asApostle();
        ApollyonExpandedContext apollyonEC = revelaionfix$apollyonEC();
        AttackDamageChangeHandler handler0 = apollyonEC.attackDamageReducer;
        this.revelaionfix$setIllusionMode(pCompound.getBoolean("IllusionMode"));
        if (pCompound.contains("ApollyonHP", 99)) {
            this.revelaionfix$setApollyonHealth(pCompound.getFloat("ApollyonHP"));
        }
        if (pCompound.contains("deathPerformance")) {
            DeathPerformance.setLeftTime(apostle, pCompound.getInt("deathPerformance"));
        }
        if (pCompound.contains("ApollyonFlags")) {
            this.entityData.set(DeathPerformance.FLAGS, pCompound.getByte("ApollyonFlags"));
        }
        if (pCompound.contains("finalDeathTime")) {
            DeathPerformance.setFinalDeathTime(apostle, pCompound.getInt("finalDeathTime"));
        }
        if (pCompound.contains("LimitTime_ddr")) {
            handler0.setLimitTime(pCompound.getInt("LimitTime_ddr"));
        }
        DeathPerformance.setDeathGrowingTime(apostle, pCompound.getInt("deathGrowTime"));
        this.setDeathTime(pCompound.getInt("ApollyonDeathTime"));
        apollyonEC.readFromNBT(pCompound);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
    private void addAdditionalSaveData(CompoundTag pCompound, CallbackInfo ci) {
        Apostle apostle = revelationfix$asApostle();
        ApollyonExpandedContext apollyonEC = revelaionfix$apollyonEC();
        AttackDamageChangeHandler handler0 = apollyonEC.attackDamageReducer;
        pCompound.putBoolean("IllusionMode", this.revelaionfix$illusionMode());
        pCompound.putFloat("ApollyonHP", this.revelaionfix$getApollyonHealth());
        pCompound.putInt("deathPerformance", DeathPerformance.getLeftTime(apostle));
        pCompound.putInt("deathGrowTime", DeathPerformance.getDeathGrowingTime(apostle));
        pCompound.putByte("ApollyonFlags", this.entityData.get(DeathPerformance.FLAGS));
        pCompound.putInt("finalDeathTime", DeathPerformance.getFinalDeathTime(apostle));
        pCompound.putInt("ApollyonDeathTime", this.getDeathTime());
        pCompound.putInt("LimitTime_ddr", handler0.getLimitTime());
        apollyonEC.writeToNBT(pCompound);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(EntityType<? extends SpellCastingCultist> type, Level worldIn, CallbackInfo ci) {
        this.clientSideIllusionOffsets = new Vec3[2][4];

        for (int i = 0; i < 4; ++i) {
            this.clientSideIllusionOffsets[0][i] = Vec3.ZERO;
            this.clientSideIllusionOffsets[1][i] = Vec3.ZERO;
        }
        revelationfix$asAccessorLE().setActiveEffects(new ApostleNonDebuffMap(revelationfix$asApostle()));
    }

    @Override
    public void dropAllDeathLoot(DamageSource p_21192_) {
        if (DeathPerformance.isDropped(revelationfix$asApostle()))
            return;
        DeathPerformance.setDropped(revelationfix$asApostle(), true);
        super.dropAllDeathLoot(p_21192_);
    }

    @Override
    public void dropFromLootTable(@NotNull DamageSource p_21021_, boolean p_21022_) {
        if (revelationfix$asApollyonHelper().allTitlesApostle_1_20_1$isApollyon()) {
            {
                ItemStack medalS = new ItemStack(ModItems.DOOM_MEDAL.get());
                if (this.isInNether()) {
                    this.spawnAtLocation(new ItemStack(GRItems.DISC_2.get()));
                } else {
                    this.spawnAtLocation(new ItemStack(GRItems.DISC_1.get()));
                }
                if (this.isInNether()) medalS.setCount(10);
                spawnAtLocation(medalS);
                if (this.isInNether()) {
                    DefeatApollyonInNetherState state = GRSavedDataExpandedContext.state(((ServerLevel) this.level()).getServer());
                    if (!state.isDropped()) {
                        state.setDropped(true);
                        ItemEntity ie;
                        if ((ie = spawnAtLocation(new ItemStack(ModItems.WITHER_QUIETUS.get()))) != null && ie.isAlive())
                            state.setDirty();
                    }
                }
            }
            if (this.isInNether()) {
                {
                    ItemStack stack0 = new ItemStack(Items.NETHERITE_INGOT);
                    stack0.setCount(this.random.nextInt(16, 48 + 1));
                    this.spawnAtLocation(stack0);
                }
                {
                    ItemStack stack0 = new ItemStack(Items.NETHERITE_BLOCK);
                    stack0.setCount(this.random.nextInt(12, 32 + 1));
                    this.spawnAtLocation(stack0);
                }
                {
                    ItemStack stack0 = new ItemStack(Items.ENCHANTED_GOLDEN_APPLE);
                    stack0.setCount(24);
                    this.spawnAtLocation(stack0);
                }
                {
                    ItemStack stack0 = new ItemStack(Items.BLAZE_ROD);
                    stack0.setCount(this.random.nextInt(2, 4 + 1));
                    this.spawnAtLocation(stack0);
                }
                {
                    ItemStack stack0 = new ItemStack(GRItems.THE_NEEDLE.get());
                    stack0.setCount(1);
                    stack0.getOrCreateTag().putBoolean(TheNeedleItem.IS_REAL_NBT, true);
                    this.spawnAtLocation(stack0);
                }

                MobAccessor mobAccessor = (MobAccessor) this;
                mobAccessor.setLootTable(null);
                int reward = net.minecraftforge.event.ForgeEventFactory.getExperienceDrop(this, this.lastHurtByPlayer, this.getExperienceReward());
                ExperienceOrb.award((ServerLevel) this.level(), this.position(), reward);
            }
            for (Entity entity : level().getEntitiesOfClass(NetherMeteor.class, new AABB(this.blockPosition()).inflate(64D)))
                entity.discard();
        } else super.dropFromLootTable(p_21021_, p_21022_);
    }

    @Inject(method = "monolithWeakened", at = @At("HEAD"), cancellable = true, remap = false)
    private void doomWeaknessMode(CallbackInfoReturnable<Boolean> cir) {
        if (this.isDoomNow() && revelationfix$asApollyonHelper().allTitlesApostle_1_20_1$isApollyon() && this.isInNether())
            cir.setReturnValue(true);
    }

    @Override
    public void die(@NotNull DamageSource cause) {
        int time;
        if (revelationfix$asApollyonHelper().allTitlesApostle_1_20_1$isApollyon() && this.isInNether())
            time = this.getDeathTime();
        else time = deathTime;
        if (time > 0) {
            super.die(cause);
        }
    }

    @Inject(method = "tickDeath", at = @At("HEAD"), cancellable = true)
    private void tickDeath(CallbackInfo ci) {
        if (!(revelationfix$asApollyonHelper().allTitlesApostle_1_20_1$isApollyon() && this.isInNether()))
            return;
        ci.cancel();
        Level level = level();
        int currentDeathTime = this.getDeathTime();
        if (currentDeathTime == 0)
            DeathPerformance.setLeftTime(revelationfix$asApostle(), DeathPerformance.MAX_TIME);
        Apostle apostle = revelationfix$asApostle();
        ApollyonAbilityHelper helper = revelationfix$asApollyonHelper();
        if (this.isInNether()) {
            if (currentDeathTime >= 3) {
                int lefttime = DeathPerformance.getLeftTime(apostle);
                if (DeathPerformance.perform((Apostle) (Object) this, helper, lefttime))
                    return;
            }
            if (currentDeathTime >= 200)
                for (Entity entity : this.level().getEntities(this, new AABB(blockPosition()).inflate(64D), (e) -> e instanceof Player)) {
                    if (!level.isClientSide)
                        ((PlayerInterface) entity).revelationfix$setBaseAttributeMode(false);
                    ((PlayerInterface) entity).revelationfix$temp_setBaseAttributeMode(false);
                    PlayerTickrateExecutor.disableBaseValueMode((Player) entity);
                }
        }
        //更新死亡时间
        this.setDeathTime(this.getDeathTime() + 1);
        currentDeathTime = this.getDeathTime();

        if (currentDeathTime == 1) {
            this.antiRegen = 0;
            this.antiRegenTotal = 0;
            Iterator var1 = level.getEntitiesOfClass(AbstractTrap.class, this.getBoundingBox().inflate(64.0)).iterator();

            while (var1.hasNext()) {
                AbstractTrap trapEntity = (AbstractTrap) var1.next();
                if (trapEntity.getOwner() == this) {
                    trapEntity.discard();
                }
            }

            var1 = level.getEntitiesOfClass(SpellEntity.class, this.getBoundingBox().inflate(64.0)).iterator();

            while (var1.hasNext()) {
                SpellEntity trapEntity = (SpellEntity) var1.next();
                if (trapEntity.getOwner() == this) {
                    trapEntity.discard();
                }
            }

            var1 = level.getEntitiesOfClass(FireTornado.class, this.getBoundingBox().inflate(64.0)).iterator();

            while (var1.hasNext()) {
                FireTornado fireTornadoEntity = (FireTornado) var1.next();
                fireTornadoEntity.discard();
            }

            this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        }

        float f2;
        float f1;
        double d1;
        double d2;
        double d3;
        ServerLevel ServerLevel;
        int k;
        if (!(Boolean) MobsConfig.FancierApostleDeath.get() && !this.isInNether()) {
            this.move(MoverType.SELF, new Vec3(0.0, 0.0, 0.0));
            if (currentDeathTime == 1) {
                if (!level.isClientSide) {
                    ServerLevel = (ServerLevel) level;
                    if (ServerLevel.getLevelData().isThundering()) {
                        ServerLevel.setWeatherParameters(6000, 0, false, false);
                    }

                    for (k = 0; k < 200; ++k) {
                        f2 = this.random.nextFloat() * 4.0F;
                        f1 = this.random.nextFloat() * 6.2831855F;
                        d1 = Mth.cos(f1) * f2;
                        d2 = 0.01 + this.random.nextDouble() * 0.5;
                        d3 = Mth.sin(f1) * f2;
                        ServerLevel.sendParticles(ParticleTypes.LARGE_SMOKE, this.getX() + d1 * 0.1, this.getY() + 0.3, this.getZ() + d3 * 0.1, 0, d1, d2, d3, 0.5);
                        ServerLevel.sendParticles(ParticleTypes.FLAME, this.getX() + d1 * 0.1, this.getY() + 0.3, this.getZ() + d3 * 0.1, 0, d1, d2, d3, 0.5);
                    }

                    for (k = 0; k < 16; ++k) {
                        ServerLevel.sendParticles(ParticleTypes.FLAME, this.getRandomX(1.0), this.getRandomY() - 0.25, this.getRandomZ(1.0), 1, 0.0, 0.0, 0.0, 0.0);
                    }
                }

                this.die(this.deathBlow);
                this.playSound(this.getTrueDeathSound(), 5.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            }

            if (currentDeathTime >= 30) {
                this.remove(RemovalReason.KILLED);
                for (Entity entity : level.getEntities(this, new AABB(blockPosition()).inflate(64D), (e) -> e instanceof Player)) {
                    if (!level.isClientSide)
                        ((PlayerInterface) entity).revelationfix$setBaseAttributeMode(false);
                    ((PlayerInterface) entity).revelationfix$temp_setBaseAttributeMode(false);
                    PlayerTickrateExecutor.disableBaseValueMode((Player) entity);
                }
            }
        } else {
            this.setNoGravity(true);
            if (this.getKillCredit() instanceof Player) {
                this.lastHurtByPlayerTime = 100;
            }

            if (currentDeathTime < 180) {
                if (currentDeathTime > 20) {
                    this.move(MoverType.SELF, new Vec3(0.0, 0.1, 0.0));
                }

                level.explode(this, this.getRandomX(1.0), this.getRandomY(), this.getRandomZ(1.0), 0.0F, Level.ExplosionInteraction.NONE);
            } else if (currentDeathTime != 200) {
                this.move(MoverType.SELF, new Vec3(0.0, 0.0, 0.0));
            }

            if (currentDeathTime >= 200) {
                this.move(MoverType.SELF, new Vec3(0.0, -4.0, 0.0));
                if (this.onGround() || this.getY() <= (double) level.getMinBuildHeight()) {
                    if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
                        if (serverLevel.getLevelData().isThundering()) {
                            serverLevel.setWeatherParameters(6000, 0, false, false);
                        }

                        for (k = 0; k < 200; ++k) {
                            f2 = this.random.nextFloat() * 4.0F;
                            f1 = this.random.nextFloat() * 6.2831855F;
                            d1 = Mth.cos(f1) * f2;
                            d2 = 0.01 + this.random.nextDouble() * 0.5;
                            d3 = Mth.sin(f1) * f2;
                            serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE, this.getX() + d1 * 0.1, this.getY() + 0.3, this.getZ() + d3 * 0.1, 0, d1, d2, d3, 0.5);
                            serverLevel.sendParticles(ParticleTypes.FLAME, this.getX() + d1 * 0.1, this.getY() + 0.3, this.getZ() + d3 * 0.1, 0, d1, d2, d3, 0.5);
                        }

                        serverLevel.sendParticles(ParticleTypes.EXPLOSION_EMITTER, this.getX(), this.getY(), this.getZ(), 0, 1.0, 0.0, 0.0, 0.5);
                    }

                    this.playSound(SoundEvents.GENERIC_EXPLODE, 4.0F, (1.0F + (level.random.nextFloat() - level.random.nextFloat()) * 0.2F) * 0.7F);
                    this.playSound(this.getTrueDeathSound(), 5.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                    this.die(this.deathBlow);
                    this.remove(RemovalReason.KILLED);
                    for (Entity entity : level.getEntities(this, new AABB(blockPosition()).inflate(64D), (e) -> e instanceof Player)) {
                        if (!level.isClientSide) {
                            //触发成就
                            ServerPlayer serverPlayer = (ServerPlayer) entity;
                            CriteriaTriggers.PLAYER_KILLED_ENTITY.trigger(serverPlayer, this, serverPlayer.damageSources().playerAttack(serverPlayer));
                            ((PlayerInterface) entity).revelationfix$setBaseAttributeMode(false);
                        }
                        ((PlayerInterface) entity).revelationfix$temp_setBaseAttributeMode(false);
                        PlayerTickrateExecutor.disableBaseValueMode((Player) entity);
                    }
                }
            }
        }
    }

    @Override
    public void remove(RemovalReason p_276115_) {
        super.remove(p_276115_);
        Level level = this.level();
        if (!level.isClientSide && isInNether() && revelationfix$asApollyonHelper().allTitlesApostle_1_20_1$isApollyon()) {
            ChestBlock chest = (ChestBlock) Blocks.CHEST;
            BlockState blockState = chest.defaultBlockState();

            level.setBlock(this.blockPosition(), blockState, 3);

            DefeatApollyonInNetherState state = GRSavedDataExpandedContext.state((ServerLevel) level);
            List<ItemStack> stacks = ((GRSavedDataEC) state).revelationfix$dataEC().getBannedCurios();

            AtomicBoolean changed = new AtomicBoolean(false);
            Container container = ChestBlock.getContainer(chest, blockState, level, this.blockPosition(), true);
            List<ItemStack> toAddToContainer = new ArrayList<>(stacks);
            List<ItemStack> tempToRemove = new ArrayList<>();
            for (int i = 0; i < container.getContainerSize(); i++) {
                if (toAddToContainer.size() < i + 1) break;
                ItemStack itemStack = toAddToContainer.get(i);
                container.setItem(i, itemStack);
                tempToRemove.add(itemStack);
            }
            toAddToContainer.removeAll(tempToRemove);
            if (!toAddToContainer.isEmpty())
                for (ItemStack stack : toAddToContainer) {
                    ItemEntity itemEntity = new ItemEntity(level, getRandomX(1F), getY() + 2F, getRandomZ(1F), stack);
                    level.addFreshEntity(itemEntity);
                }
            if (stacks.isEmpty())
                level.setBlock(this.blockPosition(), Blocks.AIR.defaultBlockState(), 3);
            stacks.clear();
            state.setDirty();
        }
    }

    @Override
    public void move(@NotNull MoverType p_19973_, @NotNull Vec3 p_19974_) {
        if (revelationfix$asApollyonHelper().allTitlesApostle_1_20_1$isApollyon() && p_19974_.y > 0 && this.isInNether()) {
            if (DeathPerformance.getLeftTime(revelationfix$asApostle()) > 0 || ((LivingEntityEC) this).revelationfix$livingECData().apollyonLastGrowingTime > 0) {
                p_19974_ = new Vec3(0F, 0F, 0F);
            }
        }
        super.move(p_19973_, p_19974_);
    }

    @Override
    public @NotNull Vec3 getDeltaMovement() {
        if (revelationfix$asApollyonHelper().allTitlesApostle_1_20_1$isApollyon() && this.isInNether()) {
            if (DeathPerformance.getLeftTime(revelationfix$asApostle()) > 0 || ((LivingEntityEC) this).revelationfix$livingECData().apollyonLastGrowingTime > 0) {
                return new Vec3(0F, 0F, 0F);
            }
        }
        return super.getDeltaMovement();
    }

    @Override
    public void setDeltaMovement(@NotNull Vec3 p_20257_) {
        if (revelationfix$asApollyonHelper().allTitlesApostle_1_20_1$isApollyon() && this.isInNether()) {
            if (DeathPerformance.getLeftTime(revelationfix$asApostle()) > 0 || ((LivingEntityEC) this).revelationfix$livingECData().apollyonLastGrowingTime > 0) {
                return;
            }
        }
        super.setDeltaMovement(p_20257_);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tickTail(CallbackInfo ci) {
        //更新数据
        this.revelaionfix$apollyonEC().tickTail();
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lcom/Polarice3/Goety/common/entities/boss/Apostle;floatApostle()V", remap = false))
    private void tick(CallbackInfo ci) {
        ApollyonAbilityHelper helper = revelationfix$asApollyonHelper();
        if (this.isAlive() && helper.allTitlesApostle_1_20_1$isApollyon())
            if (!this.getMainHandItem().isEnchanted() || !(this.getMainHandItem().getItem() instanceof BowOfRevelationItem)) {
                ItemStack stack = new ItemStack(GRItems.BOW_OF_REVELATION.get());
                stack.enchant(Enchantments.POWER_ARROWS, 5);
                this.setItemSlot(EquipmentSlot.MAINHAND, stack);
            }
        if (!this.isDeadOrDying() && tickCount > 0) {
            Apostle apostle = revelationfix$asApostle();
            if (DeathPerformance.getLeftTime(apostle) > 0) {
                DeathPerformance.setLeftTime(apostle, -1);
                DeathPerformance.setDeathGrowingTime(apostle, 0);
                DeathPerformance.setFinalDeathTime(apostle, -1);
            }
        }

        if (helper.allTitlesApostle_1_20_1$isApollyon()) {
            if ((isAlive() || this.getDeathTime() < 200) && this.isInNether()) {
                if (tickCount <= 1 || tickCount % 20 == 0) {
                    for (Entity entity : this.level().getEntities(this, new AABB(this.blockPosition()).inflate(128), (e) -> e instanceof Player && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(e))) {
                        if (entity instanceof Player player)
                            ((PlayerInterface) player).revelationfix$setBaseAttributeMode(this.isDoomNow());
                    }
                }
            }
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tickHead(CallbackInfo ci) {
        //更新数据
        this.revelaionfix$apollyonEC().tickHead();
    }

    @Inject(method = "heal", at = @At("HEAD"), cancellable = true)
    private void doomNoHealing(float p_21116_, CallbackInfo ci) {
        if (this.revelationfix$asApollyonHelper().allTitlesApostle_1_20_1$isApollyon() && this.isSecondPhase())
            if (this.isDoomNow())
                ci.cancel();
    }

    @Redirect(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/DamageSource;is(Lnet/minecraft/resources/ResourceKey;)Z", ordinal = 0))
    private boolean hurtRedirect1(DamageSource instance, ResourceKey<DamageType> p_276108_) {
        return instance.is(p_276108_) && !(instance.getEntity() instanceof Player);
    }

    @Inject(method = "hurt", at = @At("RETURN"), cancellable = true)
    private void cantCancel(DamageSource pSource, float pAmount, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue())
            if (((DamageSourceInterface) pSource).hasTag((byte) 1) || pSource.getDirectEntity() instanceof GungnirSpearEntity)
                cir.setReturnValue(false);
    }

    @Redirect(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/DamageSource;getEntity()Lnet/minecraft/world/entity/Entity;", ordinal = 1))
    private Entity hurtRedirect2(DamageSource instance) {
        return null;
    }

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void hurt(DamageSource pSource, float pAmount, CallbackInfoReturnable<Boolean> cir) {
        revelationfix$syncHitCount();
        if (!this.level().isClientSide && pSource.getEntity() instanceof Player player) {
            if (player.isCreative() && player.getMainHandItem().getItem() instanceof AscensionHalo) {
                if (player.isShiftKeyDown())
                    this.revelaionfix$setApollyonHealth(0.0F);
                else this.revelaionfix$setApollyonHealth(revelaionfix$getApollyonHealth() * 0.5F);
            }
        }
        ApollyonAbilityHelper abilityHelper = revelationfix$asApollyonHelper();
        if (abilityHelper.allTitlesApostle_1_20_1$isApollyon() && this.isInNether()) {
            if (!this.isSpellcasting())
                if (abilityHelper.allTitleApostle$getTitleNumber() == 12 || this.isDoomNow()) {
                    //下界亚不施法时候无敌只有 万众or末终
                    cir.setReturnValue(false);
                }
        }
    }

    @ModifyArg(method = "actuallyHurt", at = @At(value = "INVOKE", target = "Lcom/Polarice3/Goety/common/entities/hostile/cultists/SpellCastingCultist;actuallyHurt(Lnet/minecraft/world/damagesource/DamageSource;F)V"), index = 1)
    private float actuallyHurt(float amount) {
        if (revelationfix$asApollyonHelper().allTitlesApostle_1_20_1$isApollyon()) {
            amount = revelaionfix$apollyonEC().attackDamageReducer.redirectActuallyHurtAmount(amount);
        }
        return amount;
    }

    @Inject(method = "populateDefaultEquipmentSlots", at = @At("HEAD"), cancellable = true)
    private void populateDefaultEquipmentSlots(RandomSource randomSource, DifficultyInstance difficulty, CallbackInfo ci) {
        if (revelationfix$asApollyonHelper().allTitlesApostle_1_20_1$isApollyon()) {
            ci.cancel();
            ItemStack stack = new ItemStack(Items.BOW);
            stack.enchant(Enchantments.POWER_ARROWS, 5);
            this.setItemSlot(EquipmentSlot.MAINHAND, stack);
        }
    }

    @Redirect(method = "aiStep", at = @At(value = "INVOKE", target = "Lcom/Polarice3/Goety/common/entities/boss/Apostle;remove(Lnet/minecraft/world/entity/Entity$RemovalReason;)V"))
    private void rederectDifficuly(Apostle instance, RemovalReason trapEntity) {
        if (revelationfix$asApostle() instanceof ApostleServant servant) {
            if (servant.shouldDespawnInPeaceful())
                instance.remove(trapEntity);
        } else instance.remove(trapEntity);
    }

    @Redirect(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isCreative()Z"))
    private boolean redirectCreative(Player instance) {
        if (revelationfix$asApostle() instanceof ApostleServant servant) {
            return instance.getAbilities().flying;
        }
        return instance.isCreative() || ATAHelper2.hasOdamane(instance);
    }

    @Inject(method = "aiStep", at = @At("HEAD"))
    private void aiStep(CallbackInfo ci) {
        if (revelationfix$asApollyonHelper().allTitlesApostle_1_20_1$isApollyon()) {
            Level level = this.level();
            //禁止亚波伦杀完玩家后消失
            this.killedPlayer = false;
            this.lastKilledPlayer = 200;
            if (level.isClientSide) {
                if (this.tickCount == 1 || this.tickCount % 60 == 1) {
                    AttributeInstance armorP = this.getAttribute(ModAttributes.ARMOR_PENETRATION.get());
                    if (armorP != null)
                        armorP.setBaseValue(CommonConfig.apollyon_armorPiercing);

                    AttributeInstance enchantP = this.getAttribute(ModAttributes.ENCHANTMENT_PIERCING.get());
                    if (enchantP != null)
                        enchantP.setBaseValue(CommonConfig.apollyon_enchantmentPiercing);
                }
            }
            if (this.isInNether()) {
                if (this.level().isClientSide && this.isDoomNow()) {
                    --this.clientSideIllusionTicks;
                    if (this.clientSideIllusionTicks < 0) {
                        this.clientSideIllusionTicks = 0;
                    }

                    if (this.hurtTime != 1 && this.tickCount % 1200 != 0) {
                        if (this.hurtTime == this.hurtDuration - 1) {
                            this.clientSideIllusionTicks = 3;

                            for (int k = 0; k < 4; ++k) {
                                this.clientSideIllusionOffsets[0][k] = this.clientSideIllusionOffsets[1][k];
                                this.clientSideIllusionOffsets[1][k] = new Vec3(0.0D, 0.0D, 0.0D);
                            }
                        }
                    } else {
                        this.clientSideIllusionTicks = 3;

                        for (int j = 0; j < 4; ++j) {
                            this.clientSideIllusionOffsets[0][j] = this.clientSideIllusionOffsets[1][j];
                            this.clientSideIllusionOffsets[1][j] = new Vec3((double) (-6.0F + (float) this.random.nextInt(13)) * 0.5D, Math.max(0, this.random.nextInt(6) - 4), (double) (-6.0F + (float) this.random.nextInt(13)) * 0.5D);
                        }
                    }
                }
            }
        }
    }

    @ModifyArg(method = "aiStep", at = @At(value = "INVOKE", target = "Lcom/Polarice3/Goety/common/entities/boss/Apostle;setItemInHand(Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;)V"), index = 1)
    private ItemStack setItem(ItemStack value) {
        if (revelationfix$asApollyonHelper().allTitlesApostle_1_20_1$isApollyon())
            value.enchant(Enchantments.POWER_ARROWS, 5);
        return value;
    }

    public @NotNull AABB getBoundingBoxForCulling() {
        ApollyonAbilityHelper helper = revelationfix$asApollyonHelper();
        if (helper.allTitlesApostle_1_20_1$isApollyon() && this.isDeadOrDying() && this.isInNether())
            return super.getBoundingBoxForCulling().inflate(17.0F);
        if (helper.allTitlesApostle_1_20_1$isApollyon() &&
                this.isInNether() &&
                this.isSecondPhase() &&
                isDoomNow())
            return super.getBoundingBoxForCulling().inflate(3.0D, 0.0D, 3.0D);
        else return super.getBoundingBoxForCulling();
    }

    @Inject(method = "performRangedAttack", at = @At("HEAD"), cancellable = true)
    private void doomShootArrow(LivingEntity pTarget, float pDistanceFactor, CallbackInfo ci) {
        ApollyonAbilityHelper abilityHelper = revelationfix$asApollyonHelper();
        if (abilityHelper.allTitlesApostle_1_20_1$isApollyon() && this.isInNether() && this.isDoomNow()) {
            ci.cancel();
            //Copied from Goety
            ItemStack itemstack = this.getProjectile(this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, (item) -> item instanceof BowOfRevelationItem)));
            /*
            {
                AbstractArrow abstractarrowentity = this.getArrow(itemstack, pDistanceFactor * (float) (Integer) AttributesConfig.ApostleBowDamage.get());
                if (this.getMainHandItem().getItem() instanceof BowItem) {
                    abstractarrowentity = ((BowItem) this.getMainHandItem().getItem()).customArrow(abstractarrowentity);
                }

                double d0 = pTarget.getX() - this.getX();
                double d1 = pTarget.getY(0.5) - this.getY(0.5);
                double d2 = pTarget.getZ() - this.getZ();
                //下界末终第一发 加速箭矢
                float speed = 3.2F * 2.8F;
                float accuracy = 1.0F;
                abstractarrowentity.shoot(d0, d1, d2, speed, accuracy);
                this.playSound((SoundEvent) ModSounds.APOSTLE_SHOOT.get(), 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
                this.level.addFreshEntity(abstractarrowentity);
            }
            */
            //下界末终三发 散射箭矢
            for (int j = 0; j < 3; j++) {
                AbstractArrow abstractarrowentity = this.getArrow(itemstack, pDistanceFactor * (float) AttributesConfig.ApostleBowDamage.get());
                if (this.getMainHandItem().getItem() instanceof BowOfRevelationItem) {
                    abstractarrowentity = ((BowOfRevelationItem) this.getMainHandItem().getItem()).customArrow(abstractarrowentity);
                }
                float ticksMayToBe = (float) this.random.triangle(38, 3) / 20F;
                double d0 = pTarget.getX() - this.getX();
                double d1 = pTarget.getY(0.5) - this.getY(0.5);
                double d2 = pTarget.getZ() - this.getZ();
                float speed = 2.4F;
                float accuracy = 5.0F;
                if (j == 2) {
                    speed = 3.2F;
                    accuracy = 1.0F;
                }
                abstractarrowentity.shoot(d0, d1, d2, speed, accuracy);
                this.playSound(ModSounds.APOSTLE_SHOOT.get(), 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
                this.level().addFreshEntity(abstractarrowentity);
            }
        }
    }

    @Inject(method = "getArrow", at = @At("RETURN"), cancellable = true, remap = false)
    private void bypassArrow(ItemStack pArrowStack, float pDistanceFactor, CallbackInfoReturnable<AbstractArrow> cir) {
        if (revelationfix$asApollyonHelper().allTitlesApostle_1_20_1$isApollyon())
            if (this.isInNether()) {
                AbstractArrow abstractArrow = cir.getReturnValue();
                if (abstractArrow instanceof DeathArrow deathArrow && this.isSecondPhase())
                    ((DeathArrowEC) deathArrow).revelationfix$getTrailData().setShouldRenderTrail(true);
                if (this.isDoomNow())
                    abstractArrow.addTag(BypassInvulArrow.TAG_NAME);
                if (this.isSecondPhase())
                    abstractArrow.addTag(BypassInvulArrow.TAG_BYPASS_NAME);
                cir.setReturnValue(abstractArrow);
            }
    }

    @Redirect(method = "aiStep", at = @At(value = "INVOKE", target = "Lcom/Polarice3/Goety/common/entities/boss/Apostle;heal(F)V"),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lcom/Polarice3/Goety/common/entities/boss/Apostle;heal(F)V", ordinal = 1, shift = At.Shift.AFTER),
                    to = @At(value = "INVOKE", target = "Lcom/Polarice3/Goety/common/entities/boss/Apostle;heal(F)V", ordinal = 4, shift = At.Shift.AFTER)
            )
    )
    private void modifyHealingAmount(Apostle instance, float p_21116_) {
        if (ModpackCommonConfig.apollyonModpackMode)
            if (revelationfix$asApollyonHelper().allTitlesApostle_1_20_1$isApollyon()) {
                instance.heal((float) (ModpackCommonConfig.apollyonSelfHealing * this.getMaxHealth()));
                return;
            }
        instance.heal(p_21116_);
    }

    @Override
    public void setTarget(@Nullable LivingEntity newTarget) {
        if (revelationfix$asApollyonHelper().allTitlesApostle_1_20_1$isApollyon()) {
            revelaionfix$apollyonEC().lastTarget = this.getTarget();
            if (this.isInNether()) {
                if (newTarget != null && revelaionfix$apollyonEC().lastTarget instanceof Player) {
                    if (newTarget.getY() < 127F && !(newTarget instanceof Player))
                        return;
                }
            }
        }
        super.setTarget(newTarget);
    }

    @Override
    public void onEffectRemoved(MobEffectInstance effectInstance) {
        MobEffect key = effectInstance.getEffect();
        if (key != null && key.getCategory() == MobEffectCategory.HARMFUL)
            return;
        super.onEffectRemoved(effectInstance);
    }

    @Override
    public void onEffectAdded(MobEffectInstance effectInstance, @Nullable Entity p_147191_) {
        MobEffect key = effectInstance.getEffect();
        if (key != null && key.getCategory() == MobEffectCategory.HARMFUL)
            return;
        super.onEffectAdded(effectInstance, p_147191_);
    }

    @Override
    public void onEffectUpdated(MobEffectInstance effectInstance, boolean p_147193_, @Nullable Entity p_147194_) {
        MobEffect key = effectInstance.getEffect();
        if (key != null && key.getCategory() == MobEffectCategory.HARMFUL)
            return;
        super.onEffectUpdated(effectInstance, p_147193_, p_147194_);
    }

    @Override
    public boolean isAlive() {
        return super.isAlive();
    }

    @Override
    public float revelaionfix$getApollyonHealth() {
        return this.entityData.get(PIHOPJUBVLOBWXLJBSFVELGVBLEJCWB);
    }

    @Override
    public void revelaionfix$setApollyonHealth(float health) {
        if (Float.isNaN(health)) return;
        ApollyonExpandedContext aec = this.revelaionfix$apollyonEC();
        aec.lastPercent = aec.percent;
        health = Mth.clamp(health, 0.0F, this.getMaxHealth());
        this.entityData.set(PIHOPJUBVLOBWXLJBSFVELGVBLEJCWB, health);
        aec.percent = ApollyonExpandedContext.getPercent(health);
    }

    @Override
    public float getHealth() {
        if (((ApollyonAbilityHelper) this).allTitlesApostle_1_20_1$isApollyon() && isInNether()) {
            return revelaionfix$getApollyonHealth();
        } else return super.getHealth();
    }

    @Override
    public void setHealth(float health) {
        ApollyonAbilityHelper helper = revelationfix$asApollyonHelper();
        if (helper.allTitlesApostle_1_20_1$isApollyon()) {
            if (Float.isNaN(health)) return;
            if (isInNether()) {
                int hitCount = revelaionfix$getHitCooldown();
                if (hitCount <= 0) {
                    health = revelaionfix$apollyonEC().attackDamageReducer.redirectSetHealth(health);
                }
                float currentHealth = this.getHealth();
                float delta = health - currentHealth;
                if (delta < 0.0F) {
                    if (this.isInNether() && hitCount <= 0) {
                        float limit = ModConfig.APOLLYON_HURT_LIMIT.get().floatValue();
                        delta = -Math.min(-delta, limit);
                    }
                    if (hitCount > 0) delta = 0;
                    health = delta + currentHealth;
                }
                revelaionfix$setApollyonHealth(health);
                //无敌帧
                if (delta < 0.0F) {
                    this.revelaionfix$setHitCooldown(AttackDamageChangeHandler.vanillaLimitTime);
                    helper.allTitlesApostle_1_20_1$setHitCooldown(AttackDamageChangeHandler.vanillaLimitTime);
                }
                return;
            } else {
                float currentHealth = this.getHealth();
                float delta = health - currentHealth;
                int hitCount = helper.allTitlesApostle_1_20_1$getHitCooldown();
                if (delta < 0.0F) {
                    if (!this.isInNether() && hitCount <= 0) {
                        float limit = (float) Math.min(Math.max(ModConfig.APOLLYON_HURT_LIMIT.get().floatValue(), Math.sqrt(-delta)), this.getMaxHealth() / 10.0F);
                        delta = -Math.min(-delta, limit);
                    }
                    if (hitCount > 0) delta = 0;
                    health = delta + currentHealth;
                }
            }
        }
        super.setHealth(health);

    }

    /**
     * Fix target bug
     */
    @Nullable
    @Override
    public LivingEntity getTarget() {
        LivingEntity living = super.getTarget();
        if (living == null)
            return null;
        else if (!living.isDeadOrDying() && living.isAlive())
            return living;
        else return null;
    }

    @Unique
    private ApollyonAbilityHelper revelationfix$asApollyonHelper() {
        return (ApollyonAbilityHelper) this;
    }
    @Unique
    private AccessorLivingEntity revelationfix$asAccessorLE() {
        return (AccessorLivingEntity) this;
    }
    @Unique
    private AccessorEntity revelationfix$asAccessorE() {
        return (AccessorEntity) this;
    }
    @Unique
    private Apostle revelationfix$asApostle() {
        return (Apostle) (Object) this;
    }

    @Unique
    private void revelationfix$syncHitCount() {
        if (this.isInNether())
            revelationfix$asApollyonHelper().allTitlesApostle_1_20_1$setHitCooldown(this.revelaionfix$getHitCooldown());
    }

    @Override
    public ApollyonExpandedContext revelaionfix$apollyonEC() {
        if (this.revelationfix$apollyonEC == null)
            this.revelationfix$apollyonEC = new ApollyonExpandedContext(revelationfix$asApostle());
        return this.revelationfix$apollyonEC;
    }

    @Override
    public Vec3[] revelaionfix$getIllusionOffsets(float partialTicks) {
        if (this.clientSideIllusionTicks <= 0) {
            return this.clientSideIllusionOffsets[1];
        } else {
            double d0 = ((float) this.clientSideIllusionTicks - partialTicks) / 3.0F;
            d0 = Math.pow(d0, 0.25D);
            Vec3[] avec3 = new Vec3[4];

            for (int i = 0; i < 4; ++i) {
                avec3[i] = this.clientSideIllusionOffsets[1][i].scale(1.0D - d0).add(this.clientSideIllusionOffsets[0][i].scale(d0));
            }

            return avec3;
        }
    }

    @Override
    public boolean revelaionfix$illusionMode() {
        return this.entityData.get(ILLUSION_MODE);
    }

    @Override
    public void revelaionfix$setIllusionMode(boolean z) {
        this.entityData.set(ILLUSION_MODE, z);
    }

    @Override
    public void revelaionfix$setApollyonEC(ApollyonExpandedContext ec) {
        this.revelationfix$apollyonEC = ec;
    }

    @Unique
    private boolean isDoomNow() {
        return SafeClass.isDoom(revelationfix$asApostle());
    }

    @Unique
    public int getDeathTime() {
        return this.entityData.get(DEATH_TIME);
    }

    @Unique
    public void setDeathTime(int time) {
        this.entityData.set(DEATH_TIME, time);
    }
}
