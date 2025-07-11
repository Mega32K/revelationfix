package com.mega.revelationfix.mixin.gr;

import com.Polarice3.Goety.common.effects.GoetyEffects;
import com.Polarice3.Goety.common.entities.boss.Apostle;
import com.Polarice3.Goety.common.entities.hostile.cultists.SpellCastingCultist;
import com.Polarice3.Goety.common.entities.neutral.Owned;
import com.Polarice3.Goety.common.entities.projectiles.DeathArrow;
import com.Polarice3.Goety.common.entities.projectiles.Hellfire;
import com.Polarice3.Goety.common.entities.projectiles.IceBouquet;
import com.Polarice3.Goety.common.entities.projectiles.NetherMeteor;
import com.Polarice3.Goety.common.entities.util.FireBlastTrap;
import com.Polarice3.Goety.common.entities.util.FirePillar;
import com.Polarice3.Goety.config.AttributesConfig;
import com.Polarice3.Goety.config.MobsConfig;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.MobUtil;
import com.mega.revelationfix.common.apollyon.common.BypassInvulArrow;
import com.mega.revelationfix.client.enums.ModChatFormatting;
import com.mega.revelationfix.common.compat.SafeClass;
import com.mega.revelationfix.common.config.CommonConfig;
import com.mega.revelationfix.common.config.ModpackCommonConfig;
import com.mega.revelationfix.common.item.tool.combat.bow.BowOfRevelationItem;
import com.mega.revelationfix.safe.entity.DeathArrowEC;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import z1gned.goetyrevelation.config.ModConfig;
import z1gned.goetyrevelation.goal.ApollyonDeathArrowGoal;
import z1gned.goetyrevelation.goal.SummonServantGoal;
import z1gned.goetyrevelation.item.ModItems;
import z1gned.goetyrevelation.util.ApollyonAbilityHelper;
import z1gned.goetyrevelation.util.ArrowUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 末终和二阶段下界死亡箭矢
 * 去除死亡领域清除实体
 * 其他修改去处:{@link com.mega.revelationfix.mixin.goety.OwnedMixin}
 */
@Mixin(value = Apostle.class, priority = 0)
public abstract class ApostleMixin extends SpellCastingCultist implements ApollyonAbilityHelper {
    @Unique
    private static final EntityDataAccessor<Boolean> APOLLYON = SynchedEntityData.defineId(Apostle.class, EntityDataSerializers.BOOLEAN);
    @Unique
    private static final EntityDataAccessor<Integer> HIT_COOLDOWN = SynchedEntityData.defineId(Apostle.class, EntityDataSerializers.INT);
    @Unique
    private static final EntityDataAccessor<Boolean> SHOOTING = SynchedEntityData.defineId(Apostle.class, EntityDataSerializers.BOOLEAN);
    @Unique
    private static final EntityDataAccessor<Boolean> SILENT = SynchedEntityData.defineId(Apostle.class, EntityDataSerializers.BOOLEAN);
    @Unique
    private static final EntityDataAccessor<Boolean> DOOM = SynchedEntityData.defineId(Apostle.class, EntityDataSerializers.BOOLEAN);
    @Unique
    private static final EntityDataAccessor<Boolean> SETTING_DOOM = SynchedEntityData.defineId(Apostle.class, EntityDataSerializers.BOOLEAN);
    @Unique
    private static final EntityDataAccessor<Integer> TIME = SynchedEntityData.defineId(Apostle.class, EntityDataSerializers.INT);
    @Shadow(remap = false)
    public double prevX;
    @Shadow(remap = false)
    public double prevY;
    @Shadow(remap = false)
    public double prevZ;
    @Shadow(remap = false)
    private boolean regen;

    protected ApostleMixin(EntityType<? extends SpellCastingCultist> type, Level p_i48551_2_) {
        super(type, p_i48551_2_);
    }

    @Shadow(remap = false)
    public abstract boolean isSecondPhase();

    @Shadow(remap = false)
    public abstract boolean isSettingUpSecond();

    @Shadow(remap = false)
    public abstract void setFireArrow(boolean fireArrow);

    @Shadow(remap = false)
    public abstract void setRegen(boolean regen);

    @Shadow
    public abstract boolean addEffect(@NotNull MobEffectInstance p_182397_, @Nullable Entity p_182398_);

    @Shadow(remap = false)
    public abstract boolean isInNether();

    @Shadow(remap = false)
    @NotNull
    protected abstract NetherMeteor getNetherMeteor();

    @Shadow(remap = false)
    public abstract void setTornadoCoolDown(int coolDown);

    @Shadow(remap = false)
    public abstract void setMonolithCoolDown(int coolDown);

    @Shadow(remap = false)
    public abstract AbstractArrow getArrow(ItemStack pArrowStack, float pDistanceFactor);

    @Inject(at = @At("HEAD"), method = "addAdditionalSaveData")
    private void writeNbt(CompoundTag pCompound, CallbackInfo ci) {
        pCompound.putBoolean("isApollyon", this.allTitlesApostle_1_20_1$isApollyon());
    }

    @Inject(at = @At("HEAD"), method = "readAdditionalSaveData")
    private void readNbt(CompoundTag pCompound, CallbackInfo ci) {
        this.allTitlesApostle_1_20_1$setApollyon(pCompound.getBoolean("isApollyon"));
    }

    @Inject(at = @At("TAIL"), method = "defineSynchedData")
    private void registerEntityData(CallbackInfo ci) {
        this.entityData.define(APOLLYON, false);
        this.entityData.define(HIT_COOLDOWN, 0);
        this.entityData.define(SHOOTING, false);
        this.entityData.define(SILENT, false);
        this.entityData.define(DOOM, false);
        this.entityData.define(SETTING_DOOM, false);
        this.entityData.define(TIME, 0);
    }

    @Inject(at = @At("HEAD"), method = "setConfigurableAttributes", remap = false, cancellable = true)
    private void resetBaseHealth(CallbackInfo ci) {
        if (this.allTitlesApostle_1_20_1$isApollyon()) {
            ci.cancel();
            MobUtil.setBaseAttributes(this.getAttribute(Attributes.MAX_HEALTH), ModConfig.APOLLYON_HEALTH.get());
            MobUtil.setBaseAttributes(this.getAttribute(Attributes.ARMOR), AttributesConfig.ApostleArmor.get());
            MobUtil.setBaseAttributes(this.getAttribute(Attributes.ARMOR_TOUGHNESS), AttributesConfig.ApostleToughness.get());
        }
    }

    @Inject(at = @At("TAIL"), method = "dropCustomDeathLoot")
    private void dropHalo(DamageSource pSource, int pLooting, boolean pRecentlyHit, CallbackInfo ci) {
        if (this.allTitlesApostle_1_20_1$isApollyon()) {
            ItemEntity itementity = this.spawnAtLocation(ModItems.ASCENSION_HALO.get());
            if (itementity != null) {
                itementity.setExtendedLifetime();
                itementity.setGlowingTag(true);
            }
        } else {
            if (this.level().random.nextFloat() < 0.025F) {
                ItemEntity itementity = this.spawnAtLocation(ModItems.BROKEN_HALO.get());
                if (itementity != null) {
                    itementity.setExtendedLifetime();
                }
            }
        }
    }

    @Nullable
    @Override
    public Component getCustomName() {
        if (!this.isAddedToWorld && allTitlesApostle_1_20_1$isApollyon())
            return Component.translatable("name.goety_revelation.apollyon").withStyle(ModChatFormatting.APOLLYON);
        return super.getCustomName();
    }

    @Shadow
    public abstract void setCustomName(@Nullable Component name);

    @Inject(at = @At("TAIL"), method = "tick")
    private void tickAbility(CallbackInfo ci) {
        if (this.allTitlesApostle_1_20_1$getHitCooldown() > 0) {
            this.allTitlesApostle_1_20_1$setHitCooldown(this.allTitlesApostle_1_20_1$getHitCooldown() - 1);
        }

        if (this.allTitlesApostle_1_20_1$isApollyon()) {
            this.setCustomName(this.allTitlesApostle_1_20_1$getNameComponent());

            //使用性能更好的方法
            //MODIFIED
            //List<Owned> owneds = this.level().getEntitiesOfClass(Owned.class, new AABB(this.blockPosition()).inflate(64.0D), entity -> entity.getTrueOwner() == this);

            int i = this.allTitleApostle$titleNumber(this.getHealth());
            this.setRegen(i == 0 || i == 12);
            if (i == 0 || i == 12) {
                if (this.isInNether()) {
                    this.setMonolithCoolDown(0);
                }
            }
            //MODIFIED
            {
                if (i == 1) {
                    //主世界憎恶 主世界和下界万众都有12点帧伤魔法伤害领域
                    if (!this.isInNether()) {
                        for (LivingEntity target : this.level().getEntitiesOfClass(LivingEntity.class, new AABB(this.blockPosition()).inflate(3.0D), entity -> !(entity instanceof Owned owned && owned.getTrueOwner() == this))) {
                            if (target != this) {
                                target.hurt(this.damageSources().indirectMagic(this, this), 12.0F);
                                target.invulnerableTime = 0;
                            }
                        }
                    }
                } else if (i == 12) {
                    for (LivingEntity target : this.level().getEntitiesOfClass(LivingEntity.class, new AABB(this.blockPosition()).inflate(3.0D), entity -> !(entity instanceof Owned owned && owned.getTrueOwner() == this))) {
                        if (target != this) {
                            target.hurt(this.damageSources().indirectMagic(this, this), 12.0F);
                            if (!this.isInNether())
                                target.invulnerableTime = 0;
                        }
                    }
                }
            }
            if (i == 6 || i == 12) {
                this.addEffect(new MobEffectInstance(GoetyEffects.FIERY_AURA.get(), 20 * 5, 1, false, false), this);
            }

            if (i == 9 || i == 12) {
                this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20 * 5, 1, false, false), this);
            }

            if (i == 10 || i == 12) {
                //MODIFIED
                /*
                int level = 0;
                if (this.isInNether()) {
                    level = 2;
                }
                 */
                this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20 * 5, 0, false, false), this);
                //MODIFIED
                //for (Owned owned : owneds) {
                //    owned.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20 * 5, level));
                //}
            }

            if (i == 11 || i == 12) {
                //MODIFIED
                /*
                for (Owned owned : owneds) {
                    owned.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20 * 5, 1));
                    owned.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20 * 5, 1));
                }
                 */

                if (this.isInNether()) {
                    this.setTornadoCoolDown(0);
                }
            }

            if (i == 1 || i == 13) {
                for (LivingEntity closeEntity : this.level().getEntitiesOfClass(LivingEntity.class, new AABB(this.blockPosition()).inflate(5.0D), entity -> !(entity instanceof Owned owned && owned.getTrueOwner() == this))) {
                    if (closeEntity instanceof Player player && !player.isCreative() && !player.isSpectator()) {
                        closeEntity.addEffect(new MobEffectInstance(GoetyEffects.SAPPED.get(), 20 * 2, 4));
                    }

                    if (!(closeEntity instanceof Player)) {
                        closeEntity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20 * 2, 3));
                    }
                }

            }

            if (i == 8 || i == 12) {
                for (LivingEntity closeEntity : this.level().getEntitiesOfClass(LivingEntity.class, new AABB(this.blockPosition()).inflate(5.0D), entity -> !(entity instanceof Owned owned && owned.getTrueOwner() == this))) {
                    if (closeEntity instanceof Player player && !player.isCreative() && !player.isSpectator()) {
                        closeEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20 * 2, 1));
                    }

                    if (!(closeEntity instanceof Player)) {
                        closeEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20 * 2, 1));
                    }
                }

                for (LivingEntity closeEntity : this.level().getEntitiesOfClass(LivingEntity.class, new AABB(this.blockPosition()).inflate(3.0D), entity -> !(entity instanceof Owned owned && owned.getTrueOwner() == this))) {
                    if (closeEntity instanceof Player player && !player.isCreative() && !player.isSpectator()) {
                        closeEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20 * 2, 6));
                    }

                    if (!(closeEntity instanceof Player)) {
                        closeEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20 * 2, 6));
                    }
                }
            }

            if (i == 13) {
                this.setRegen(false);
                if (this.getTarget() != null) {
                    //Objects.requireNonNull(this.getTarget().getAttribute(Attributes.MAX_HEALTH)).setBaseValue(1.0F);
                    this.getTarget().addEffect(new MobEffectInstance(GoetyEffects.CURSED.get(), 20 * 3, 0, false, false), this);
                    this.getTarget().addEffect(new MobEffectInstance(GoetyEffects.SAPPED.get(), 20 * 4, 0, false, false), this);
                }
                //MODIFIED
                /*
                for (Owned owned : owneds) {
                    owned.discard();
                }
                */
                //MODIFIED
                //主世界的末日是1点帧伤虚空
                if (!this.isInNether()) {
                    for (LivingEntity target : this.level().getEntitiesOfClass(LivingEntity.class, new AABB(this.blockPosition()).inflate(3.0D), entity -> EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity) && !(entity instanceof Owned owned && owned.getTrueOwner() == this))) {
                        if (target != this) {
                            target.hurt(this.damageSources().fellOutOfWorld(), 1.0F);
                            target.invulnerableTime = 0;
                        }
                    }
                }
            }
        }
    }

    @Inject(at = @At("TAIL"), method = "aiStep")
    private void netherUpgrade(CallbackInfo ci) {
        if (!this.level().isClientSide) {
            if (this.allTitlesApostle_1_20_1$isApollyon() && this.isInNether()) {
                if (this.isSecondPhase()) {
                    int tickLift = 20;
                    if (this.allTitleApostle$getTitleNumber() == 6) {
                        tickLift /= 2;
                    } else if (this.allTitleApostle$getTitleNumber() == 12) {
                        //tickLift /= 3;
                        tickLift /= 2;
                    }
                    //MODIFIED
                    if (this.isAlive()) {
                        if (this.tickCount % tickLift == 0) {
                            BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos(this.getRandomX(0.2), this.getY(), this.getRandomZ(0.2));

                            while ((double) blockPos.getY() < this.getY() + 64.0 && !this.level().getBlockState(blockPos).isSolidRender(this.level(), blockPos)) {
                                blockPos.move(Direction.UP);
                            }

                            if ((double) blockPos.getY() > this.getY() + 32.0) {
                                NetherMeteor fireball = this.getNetherMeteor();
                                fireball.setDangerous(ForgeEventFactory.getMobGriefingEvent(this.level(), this) && MobsConfig.ApocalypseMode.get());
                                fireball.setPos(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                                this.level().addFreshEntity(fireball);
                            }
                        }
                    }

                    if (this.tickCount % tickLift == 0) {
                        double offsetX = (this.random.nextDouble() * 2 - 1) * 24;
                        double offsetZ = (this.random.nextDouble() * 2 - 1) * 24;
                        double posX = this.getX() + offsetX;
                        double posZ = this.getZ() + offsetZ;

                        FireBlastTrap fireBlastTrap = new FireBlastTrap(this.level(), posX, this.getY(), posZ);
                        fireBlastTrap.setOwner(this);
                        fireBlastTrap.setAreaOfEffect(3.0F);
                        this.level().addFreshEntity(fireBlastTrap);
                    }
                }

                int i = this.allTitleApostle$titleNumber(this.getHealth());
                if (i == 4 || i == 12) {
                    //MODIFIED
                    for (Player player : this.level().getEntitiesOfClass(Player.class, new AABB(this.blockPosition()).inflate(64.0D), EntitySelector.NO_CREATIVE_OR_SPECTATOR)) {
                        player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 20 * 5, 0));
                    }
                    //MODIFIED
                    /*
                    for (Owned owned : this.level().getEntitiesOfClass(Owned.class, new AABB(this.blockPosition()).inflate(64.0D), owned -> owned.getTrueOwner() == this)) {
                        owned.addEffect(new MobEffectInstance(MobEffects.GLOWING, 20 * 5, 0));
                    }
                     */

                    this.addEffect(new MobEffectInstance(MobEffects.GLOWING, 20 * 5, 1, false, false), this);
                }

                if (i == 7 || i == 12) {
                    if (this.tickCount % 100 == 0) {
                        List<LivingEntity> livingEntities = this.level().getEntitiesOfClass(LivingEntity.class, new AABB(this.blockPosition()).inflate(64.0D), entity -> entity != this && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity) && !(entity instanceof Owned owned && owned.getTrueOwner() == this));
                        for (LivingEntity living : livingEntities) {
                            List<MobEffectInstance> mobEffectInstances = new ArrayList<>();
                            for (MobEffectInstance instance : living.getActiveEffects()) {
                                if (instance.getEffect().isBeneficial() && instance.getEffect() != MobEffects.NIGHT_VISION) {
                                    mobEffectInstances.add(instance);
                                }
                            }

                            for (MobEffectInstance instance : mobEffectInstances) {
                                living.removeEffect(instance.getEffect());
                            }
                        }
                    }
                }

                if (i == 9 || i == 12) {
                    if (this.random.nextInt(20) == 0) {
                        Hellfire hellfire = new Hellfire(this.level(), Vec3.atCenterOf(this.blockPosition()), this);
                        this.level().addFreshEntity(hellfire);
                    }
                }

                if (i == 5 || i == 12) {
                    for (LivingEntity living : this.level().getEntitiesOfClass(LivingEntity.class, new AABB(this.blockPosition()).inflate(3.0D), entity -> EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity) && entity != this && !(entity instanceof Owned owned && owned.getTrueOwner() == this))) {
                        living.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 20 * 5, 1));
                    }
                }

                if (i == 1 || i == 12) {
                    for (LivingEntity living : this.level().getEntitiesOfClass(LivingEntity.class, new AABB(this.blockPosition()).inflate(128.0D))) {
                        living.addEffect(new MobEffectInstance(GoetyEffects.WILD_RAGE.get(), 20 * 5, 0));
                    }
                }

                if (i == 8 || i == 12) {
                    if (this.random.nextInt(10) == 0) {
                        if (this.getTarget() != null) {
                            IceBouquet iceBouquet = new IceBouquet(this.level(), this.getTarget().getX(), this.getTarget().getY() + 2, this.getTarget().getZ(), this);
                            this.level().addFreshEntity(iceBouquet);
                        }
                    }
                }

                if (i == 13) {
                    LivingEntity target = this.getTarget();
                    if (target != null) {
                        //target.setHealth(1.0F);
                        target.addEffect(new MobEffectInstance(GoetyEffects.CURSED.get(), 20 * 5, 1));
                        target.addEffect(new MobEffectInstance(GoetyEffects.BURN_HEX.get(), 20 * 5, 1));
                    }
                    List<LivingEntity> livingEntities = this.level().getEntitiesOfClass(LivingEntity.class, new AABB(this.blockPosition()).inflate(64.0D), entity -> EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity) && entity != this && !(entity instanceof Owned owned && owned.getTrueOwner() == this));
                    for (LivingEntity living : livingEntities) {
                        List<MobEffectInstance> mobEffectInstances = new ArrayList<>();
                        for (MobEffectInstance instance : living.getActiveEffects()) {
                            if (instance.getEffect().isBeneficial() && instance.getEffect() != MobEffects.NIGHT_VISION) {
                                mobEffectInstances.add(instance);
                            }
                        }

                        for (MobEffectInstance instance : mobEffectInstances) {
                            living.removeEffect(instance.getEffect());
                        }
                    }
                    //MODIFIED
                    for (Entity entity : this.level().getEntities(this, new AABB(this.blockPosition()).inflate(32.0D),
                            (e -> !(e instanceof LivingEntity) && !(CommonConfig.inWhitelist(e))))) {
                        if (entity instanceof Projectile projectile) {
                            if (projectile.getOwner() != this) {
                                projectile.discard();
                            }
                        } else if (entity instanceof FireBlastTrap trap) {
                            if (trap.getOwner() != this) {
                                trap.discard();
                            }
                        } else {
                            entity.discard();
                        }
                    }
                }
            }

            LivingEntity target = this.getTarget();
            if (this.allTitlesApostle_1_20_1$isShooting() && target != null && this.isAlive() && !this.isDeadOrDying()) {
                ItemStack itemstack = this.getProjectile(this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, (item) -> item instanceof BowOfRevelationItem)));
                AbstractArrow abstractarrowentity = this.getArrow(itemstack, (float) AttributesConfig.ApostleBowDamage.get());
                //MODIFIED
                if (this.isSecondPhase() && this.isInNether())
                    if (abstractarrowentity instanceof DeathArrow deathArrow)
                        ((DeathArrowEC) deathArrow).revelationfix$getTrailData().setShouldRenderTrail(true);
                if (this.isSecondPhase())
                    abstractarrowentity.addTag(BypassInvulArrow.TAG_BYPASS_NAME);
                if (SafeClass.isDoom((Apostle) (Object) this))
                    abstractarrowentity.addTag(BypassInvulArrow.TAG_NAME);
                double d0 = target.getX() - this.getX();
                double d1 = target.getY(0.5) - this.getY(0.5);
                double d2 = target.getZ() - this.getZ();
                float speed = 3.2F;
                float accuracy = 1.0F;
                double offset = (-0.5D + this.random.nextDouble()) / 8.0D;
                abstractarrowentity.shoot(d0 + offset, d1, d2 + offset, speed, accuracy);
                this.playSound(ModSounds.APOSTLE_SHOOT.get(), 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
                this.level().addFreshEntity(abstractarrowentity);
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "registerGoals")
    private void registerGoal(CallbackInfo ci) {
        this.goalSelector.addGoal(3, new ApollyonDeathArrowGoal((Apostle) (Object) this));
    }

    @Inject(at = @At("RETURN"), method = "setCustomName", cancellable = true)
    private void componentName(Component name, CallbackInfo ci) {
        if (name != this.allTitlesApostle_1_20_1$getNameComponent() && this.allTitlesApostle_1_20_1$isApollyon()) {
            ci.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "getExperienceReward", cancellable = true)
    private void setReward(CallbackInfoReturnable<Integer> cir) {
        int expReward = 1000;
        if (this.isInNether()) {
            expReward = 1000 * 12;

            if (this.allTitlesApostle_1_20_1$isApollyon()) {
                expReward = 1000 * 1200;
            }
        }

        if (this.allTitlesApostle_1_20_1$isApollyon() && !this.isInNether()) {
            expReward = 10000;
        }
        cir.setReturnValue(expReward);
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lcom/Polarice3/Goety/common/entities/projectiles/DeathArrow;addEffect(Lnet/minecraft/world/effect/MobEffectInstance;)V"), method = "getArrow")
    private void setArrowEffect(DeathArrow instance, MobEffectInstance mobEffectInstance) {
        if (this.allTitlesApostle_1_20_1$isApollyon()) {
            MobEffect[] arrowEffects = {MobEffects.HUNGER, MobEffects.BLINDNESS, MobEffects.POISON, MobEffects.WITHER, GoetyEffects.BURN_HEX.get(), MobEffects.WEAKNESS, MobEffects.HARM, MobEffects.MOVEMENT_SLOWDOWN, GoetyEffects.SAPPED.get()};
            MobEffect arrowEffect = MobEffects.HUNGER;
            int i = this.allTitleApostle$getTitleNumber();
            if (i == 4) {
                arrowEffect = MobEffects.BLINDNESS;
            } else if (i == 2) {
                arrowEffect = MobEffects.POISON;
            } else if (i == 3) {
                arrowEffect = MobEffects.WITHER;
            } else if (i == 6) {
                this.setFireArrow(true);
                arrowEffect = GoetyEffects.BURN_HEX.get();
            } else if (i == 5) {
                arrowEffect = MobEffects.WEAKNESS;
            } else if (i == 1) {
                arrowEffect = MobEffects.HARM;
            } else if (i == 8) {
                arrowEffect = MobEffects.MOVEMENT_SLOWDOWN;
            } else if (i == 11) {
                arrowEffect = GoetyEffects.SAPPED.get();
            } else if (i == 13) {
                arrowEffect = GoetyEffects.DOOM.get();
            }

            MobEffect mobEffect = arrowEffect;
            byte amp;
            if (i == 13) {
                amp = 20;
            } else if (this.isSecondPhase() && arrowEffect != MobEffects.HARM) {
                amp = 1;
            } else {
                amp = 0;
            }

            int duration = i == 13 ? 1 : 200;

            if (this.getTarget() != null && arrowEffect == MobEffects.HARM && this.getTarget().isInvertedHealAndHarm()) {
                mobEffect = MobEffects.HEAL;
            }

            if (this.isSecondPhase() && arrowEffect == MobEffects.DARKNESS) {
                mobEffect = MobEffects.BLINDNESS;
            }

            if (i == 12) {
                for (MobEffect effect : arrowEffects) {
                    instance.addEffect(new MobEffectInstance(effect, effect.isInstantenous() ? 1 : duration, amp));
                }
            } else {
                instance.addEffect(new MobEffectInstance(mobEffect, mobEffect.isInstantenous() ? 1 : duration, amp));
            }

            ((ArrowUtil) instance).allTitleApostle$setExplosive(i == 0 || i == 12);
        } else {
            instance.addEffect(mobEffectInstance);
        }
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lcom/Polarice3/Goety/common/entities/boss/Apostle;setCustomName(Lnet/minecraft/network/chat/Component;)V"), method = "finalizeSpawn")
    private void setInitName(Apostle instance, Component name) {
        if (this.allTitlesApostle_1_20_1$isApollyon()) {
            Component apollyon = Component.translatable("name.goety_revelation.apollyon");
            instance.setCustomName(Component.translatable(apollyon.getString()));
        } else {
            instance.setCustomName(name);
        }
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lcom/Polarice3/Goety/common/entities/boss/Apostle;TitleEffect(Ljava/lang/Integer;)V", remap = false), method = "finalizeSpawn")
    private void redirectNumber(Apostle instance, Integer integer) {
        if (this.allTitlesApostle_1_20_1$isApollyon()) {
            instance.setTitleNumber(1);
            instance.TitleEffect(1);
        } else {
            instance.setTitleNumber(integer);
            instance.TitleEffect(integer);
        }
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lcom/Polarice3/Goety/common/entities/boss/Apostle;heal(F)V", ordinal = 0), method = "aiStep")
    private void settingUpHeal(Apostle instance, float p_21116_) {
        instance.heal(this.getMaxHealth() * 0.025F);
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z", ordinal = 0), method = "aiStep")
    private boolean canSpawnMeteor(Level instance, Entity entity) {
        if (this.allTitlesApostle_1_20_1$isApollyon() && this.isInNether()) {
            return false;
        }

        return instance.addFreshEntity(entity);
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z", ordinal = 0), method = "teleportHits")
    private boolean teleportProjectile(Level instance, Entity entity) {
        int i = this.allTitleApostle$getTitleNumber();
        if (this.allTitlesApostle_1_20_1$isApollyon() && (i == 3 || i == 12)) {
            FirePillar firePillar = new FirePillar(this.level(), this.prevX, this.prevY + 0.25, this.prevZ);
            firePillar.setDuration(360);
            firePillar.setOwner(this);
            firePillar.setWarmUp(20);
            instance.addFreshEntity(firePillar);

            if (this.getTarget() != null) {
                double yaw = Math.toRadians(this.getYRot());
                for (int l = -1; l < 1; l++) {
                    double offsetX = Math.cos(yaw + Math.PI / 2) * i * 1.5F;
                    double offsetZ = Math.sin(yaw + Math.PI / 2) * i * 1.5F;
                    Vec3 vec3 = this.getViewVector(1.0F);
                    double d2 = this.getTarget().getX() - (this.getX() + vec3.x * 4.0D) + offsetX;
                    double d3 = this.getTarget().getY(0.5D) - (0.5D + this.getY(0.5D));
                    double d4 = this.getTarget().getZ() - (this.getZ() + vec3.z * 4.0D) + offsetZ;
                    WitherSkull witherSkull = new WitherSkull(this.level(), this, d2, d3, d4);
                    witherSkull.setPos(this.getX() + offsetX, this.getEyeY(), this.getZ() + offsetZ);
                    this.level().addFreshEntity(witherSkull);
                }
            }
        }

        return instance.addFreshEntity(entity);
    }

    @ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/GoalSelector;addGoal(ILnet/minecraft/world/entity/ai/goal/Goal;)V"), method = "registerGoals", index = 1)
    private Goal setSummonGoal(Goal p_25354_) {
        if (p_25354_.getClass().getSimpleName().equals("RangedSummonSpellGoal") && this.allTitlesApostle_1_20_1$isApollyon()) {
            return new SummonServantGoal((Apostle) (Object) this);
        }

        return p_25354_;
    }

    @Override
    public int allTitleApostle$getTitleNumber() {
        return this.allTitleApostle$titleNumber(this.getHealth());
    }

    @Override
    public boolean allTitlesApostle_1_20_1$isApollyon() {
        return this.entityData.get(APOLLYON);
    }

    @Override
    public void allTitlesApostle_1_20_1$setApollyon(boolean apollyon) {
        this.entityData.set(APOLLYON, apollyon);
    }

    @Unique
    private int allTitleApostle$titleNumber(float health) {
        //MODIFIED
        if (!ModpackCommonConfig.apollyonModpackMode || !isInNether()) {
            int i = 7;
            float MAX = this.getMaxHealth();
            float per = MAX / 14;
            if ((this.isSettingUpSecond() || this.isSecondPhase()) && health > MAX - 240) {
                i = 6;
            }

            if (health <= MAX - per && health > MAX - per * 2 && !this.isSecondPhase() && !this.isSettingUpSecond()) {
                i = 4;
            }

            if (health <= MAX - (per * 2) && health > MAX - (per * 3) && !this.isSecondPhase() && !this.isSettingUpSecond()) {
                i = 2;
            }

            if (health <= MAX - (per * 3) && health > MAX - (per * 4) && !this.isSecondPhase() && !this.isSettingUpSecond()) {
                i = 9;
            }

            if (health <= MAX - (per * 4) && health > MAX - (per * 5) && !this.isSecondPhase() && !this.isSettingUpSecond()) {
                i = 3;
            }

            if (health <= MAX - (per * 5) && health > MAX - (per * 6)) {
                i = 6;
            }

            if (health <= MAX - (per * 6) && health > MAX - (per * 7)) {
                i = 10;
            }

            if (health <= MAX - (per * 7) && health > MAX - (per * 8)) {
                i = 5;
            }

            if (health <= MAX - (per * 8) && health > MAX - (per * 9)) {
                i = 1;
            }

            if (health <= MAX - (per * 9) && health > MAX - (per * 10)) {
                i = 8;
            }

            if (health <= MAX - (per * 10) && health > MAX - (per * 11)) {
                i = 11;
            }

            if (health <= MAX - (per * 11) && health > MAX - (per * 12)) {
                i = 0;
            }

            if (health <= MAX - (per * 12) && health > MAX - (per * 13)) {
                i = 12;
            }

            if (health <= MAX - (per * 13)) {
                i = 13;
                this.setDoom(true);
            }

            return i;
        } else {
            int i = 7;
            float MAX = this.getMaxHealth();
            double doomPercent = ModpackCommonConfig.netherTheDoomPercent;
            float per = (float) (MAX * (1F - doomPercent) / 13F);
            if ((this.isSettingUpSecond() || this.isSecondPhase()) && health > MAX - 240) {
                i = 6;
            }

            if (health <= MAX - per && health > MAX - per * 2 && !this.isSecondPhase() && !this.isSettingUpSecond()) {
                i = 4;
            }

            if (health <= MAX - (per * 2) && health > MAX - (per * 3) && !this.isSecondPhase() && !this.isSettingUpSecond()) {
                i = 2;
            }

            if (health <= MAX - (per * 3) && health > MAX - (per * 4) && !this.isSecondPhase() && !this.isSettingUpSecond()) {
                i = 9;
            }

            if (health <= MAX - (per * 4) && health > MAX - (per * 5) && !this.isSecondPhase() && !this.isSettingUpSecond()) {
                i = 3;
            }

            if (health <= MAX - (per * 5) && health > MAX - (per * 6)) {
                i = 6;
            }

            if (health <= MAX - (per * 6) && health > MAX - (per * 7)) {
                i = 10;
            }

            if (health <= MAX - (per * 7) && health > MAX - (per * 8)) {
                i = 5;
            }

            if (health <= MAX - (per * 8) && health > MAX - (per * 9)) {
                i = 1;
            }

            if (health <= MAX - (per * 9) && health > MAX - (per * 10)) {
                i = 8;
            }

            if (health <= MAX - (per * 10) && health > MAX - (per * 11)) {
                i = 11;
            }

            if (health <= MAX - (per * 11) && health > MAX - (per * 12)) {
                i = 0;
            }

            if (health <= MAX - (per * 12) && health > MAX - (per * 13)) {
                i = 12;
            }

            if (health <= doomPercent * MAX) {
                i = 13;
                this.setDoom(true);
                if (this.isInNether()) {
                    if (!level.isClientSide) {
                        GameRules gameRules = level.getGameRules();
                        if (!gameRules.getRule(GameRules.RULE_KEEPINVENTORY).get())
                            gameRules.getRule(GameRules.RULE_KEEPINVENTORY).set(true, ((ServerLevel) level).getServer());
                    }
                }
            }

            return i;
        }
    }

    @Unique
    private Component allTitlesApostle_1_20_1$getNameComponent() {
        Component title = Component.translatable("title.goety." + this.allTitleApostle$titleNumber(this.getHealth()));
        Component name = Component.translatable("name.goety_revelation.apollyon");
        String str = name.getString();
        Component name1 = Component.translatable(str + title.getString());
        if (this.isInNether()) {
            name1 = Component.translatable(str + title.getString()).withStyle(ChatFormatting.BOLD).withStyle(ModChatFormatting.APOLLYON);
        }
        return name1;
    }

    @Override
    public void allTitlesApostle_1_20_1$setHitCooldown(int time) {
        if (!allTitlesApostle_1_20_1$isApollyon()) time = 0;
        this.entityData.set(HIT_COOLDOWN, time);
    }

    @Override
    public int allTitlesApostle_1_20_1$getHitCooldown() {
        return this.entityData.get(HIT_COOLDOWN);
    }

    @Override
    public void allTitlesApostle_1_20_1$setShooting(boolean shooting) {
        this.entityData.set(SHOOTING, shooting);
    }

    @Override
    public boolean allTitlesApostle_1_20_1$isShooting() {
        return this.entityData.get(SHOOTING);
    }

    @Override
    public void allTitlesApostle_1_20_1$setSilent(boolean silent) {
        this.entityData.set(SILENT, silent);
    }

    @Override
    public boolean allTitlesApostle_1_20_1$isSilent() {
        return this.entityData.get(SILENT);
    }

    @Override
    public boolean getDoom() {
        return this.entityData.get(DOOM);
    }

    @Override
    public void setDoom(boolean isDoom) {
        this.entityData.set(DOOM, isDoom);
    }

    @Override
    public boolean isSettingUpDoom() {
        return this.entityData.get(SETTING_DOOM);
    }

    @Override
    public void setSettingUpDoom(boolean settingUp) {
        this.entityData.set(SETTING_DOOM, settingUp);
    }

    @Override
    public int getApollyonTime() {
        return this.entityData.get(TIME);
    }

    @Override
    public void setApollyonTime(int time) {
        this.entityData.set(TIME, time);
    }
}
