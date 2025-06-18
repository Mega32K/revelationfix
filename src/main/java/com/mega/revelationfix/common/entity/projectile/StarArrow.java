package com.mega.revelationfix.common.entity.projectile;

import com.Polarice3.Goety.api.entities.IOwned;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mega.revelationfix.client.renderer.trail.TrailPoint;
import com.mega.revelationfix.common.init.ModEntities;
import com.mega.revelationfix.common.init.ModSounds;
import com.mega.revelationfix.common.odamane.common.FeDamage;
import com.mega.revelationfix.util.entity.EntityActuallyHurt;
import com.mega.revelationfix.util.LivingEntityEC;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.EntityBasedExplosionDamageCalculator;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.joml.Vector4f;

import javax.annotation.Nullable;
import java.util.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class StarArrow extends WitherSkull {
    public static final int maxTrails = 16;
    public static final EntityDataAccessor<Float> POWER_STAR = SynchedEntityData.defineId(StarArrow.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> DAMAGE_MULTIPLIER = SynchedEntityData.defineId(StarArrow.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Vector3f> RGB = SynchedEntityData.defineId(StarArrow.class, EntityDataSerializers.VECTOR3);
    private static final EntityDataAccessor<Float> ALPHA = SynchedEntityData.defineId(StarArrow.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> TARGET = SynchedEntityData.defineId(StarArrow.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> TRAIL_LIFE_TIME = SynchedEntityData.defineId(StarArrow.class, EntityDataSerializers.INT);
    private static final double seekDistance = 5.0;
    private static final double seekFactor = 0.2;
    private static final double seekAngle = Math.PI / 6.0;
    private static final double seekThreshold = 0.5;
    public static int maxTrialLife = 53;
    public final float initialZRot = (float) (Math.random() * 360F);
    public final double rotationFactor = random.triangle(0F, 2F);
    public final List<TrailPoint> trailPoints = new ArrayList<>();
    public Vector4f color = new Vector4f(1F, 1F, 1F, 1F);
    public float size = 1F;
    public double[] oldX = new double[maxTrails];
    public double[] oldY = new double[maxTrails];
    public double[] oldZ = new double[maxTrails];
    protected @Nullable Entity targetEntity;

    public StarArrow(EntityType<? extends StarArrow> p_37598_, Level p_37599_) {
        super(p_37598_, p_37599_);
        this.noCulling = true;
    }

    public StarArrow(Level p_37609_, LivingEntity p_37610_, double p_37611_, double p_37612_, double p_37613_) {
        this(ModEntities.STAR_ENTITY.get(), p_37610_, p_37611_, p_37612_, p_37613_, p_37609_);
    }

    public StarArrow(EntityType<? extends StarArrow> p_36826_, LivingEntity p_36827_, double p_36828_, double p_36829_, double p_36830_, Level p_36831_) {
        this(p_36826_, p_36827_.getX(), p_36827_.getY(), p_36827_.getZ(), p_36828_, p_36829_, p_36830_, p_36831_);
        this.setColor(new Vector4f(random.nextFloat(), random.nextFloat(), random.nextFloat(), Math.max(0.11F, random.nextFloat())));
        this.setOwner(p_36827_);
        this.setRot(p_36827_.getYRot(), p_36827_.getXRot());
    }

    public StarArrow(EntityType<? extends StarArrow> p_36817_, double p_36818_, double p_36819_, double p_36820_, double p_36821_, double p_36822_, double p_36823_, Level p_36824_) {
        this(p_36817_, p_36824_);
        this.moveTo(p_36818_, p_36819_, p_36820_, this.getYRot(), this.getXRot());
        this.reapplyPosition();
        double d0 = Math.sqrt(p_36821_ * p_36821_ + p_36822_ * p_36822_ + p_36823_ * p_36823_);
        if (d0 != 0.0D) {
            this.xPower = p_36821_ / d0 * 0.2D;
            this.yPower = p_36822_ / d0 * 0.2D;
            this.zPower = p_36823_ / d0 * 0.2D;
        }

    }

    public static boolean shouldHurt(Player owner, Entity iterator) {
        if (iterator instanceof IOwned owned && owned.getMasterOwner() == owner)
            return false;
        if (iterator instanceof OwnableEntity ownable && ownable.getOwner() == owner)
            return false;
        return EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(iterator);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double p_36837_) {
        return p_36837_ < 512D * 512D;
    }

    @Override
    public boolean isAlwaysTicking() {
        return true;
    }

    @Override
    public boolean canUpdate() {
        return true;
    }

    public void onHitEntity(@NotNull EntityHitResult p_37626_) {
        boolean flag1 = true;
        if (!this.level().isClientSide) {
            Entity hit = p_37626_.getEntity();
            Entity owner = this.getOwner();
            if (!(owner instanceof Player player))
                return;
            boolean flag;
            if (hit instanceof LivingEntity hitLiving) {
                if (shouldHurt(player, hitLiving)) {
                    int i = 0;
                    if (this.level().getDifficulty() == Difficulty.NORMAL) {
                        i = 10;
                    } else if (this.level().getDifficulty() == Difficulty.HARD) {
                        i = 40;
                    }
                    if (i > 0) {
                        ((LivingEntity) hit).addEffect(new MobEffectInstance(MobEffects.WITHER, 20 * i, 1), this.getEffectSource());
                        ((LivingEntity) hit).addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 15 * i, 1), this.getEffectSource());
                        ((LivingEntity) hit).addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 10 * i, 1), this.getEffectSource());
                        ((LivingEntity) hit).addEffect(new MobEffectInstance(MobEffects.HARM, 1, 1), this.getEffectSource());
                    }
                }
            } else if (!(hit instanceof ItemEntity))
                hit.hurt(hit.damageSources().generic(), 20F);
        }

    }

    public void onHit(@NotNull HitResult p_37628_) {
        Player owner;
        if (this.getOwner() instanceof Player p) owner = p;
        else return;
        float power = (float) Math.max(15.0F, owner.getAttributeValue(Attributes.ATTACK_DAMAGE));
        HitResult.Type hitresult$type = p_37628_.getType();
        if (hitresult$type == HitResult.Type.ENTITY) {
            if (((EntityHitResult) p_37628_).getEntity() == this.getOwner()) return;
            this.onHitEntity((EntityHitResult) p_37628_);
            this.level().gameEvent(GameEvent.PROJECTILE_LAND, p_37628_.getLocation(), GameEvent.Context.of(this, null));
        }
        if (!tags.contains("exploded")) {
            if (!this.level().isClientSide) {
                for (LivingEntity entity : level().getEntitiesOfClass(LivingEntity.class, new AABB(blockPosition).inflate(Mth.clamp(5F + getPower() / 10F, 5, 15)), (e) -> shouldHurt(owner, e))) {
                    if (p.is(entity))
                        continue;
                    if (Math.random() < 0.5D)
                        ((LivingEntityEC) entity).revelationfix$livingECData().banHealingTime = 60;
                    entity.invulnerableTime = 0;
                    boolean flag = entity.hurt(FeDamage.get(entity, owner), power * getDamageMultiplier());
                    new EntityActuallyHurt(entity).actuallyHurt(FeDamage.get(entity, owner), power * 0.1F, true);
                    if (flag) {
                        if (entity.isAlive()) {
                            this.doEnchantDamageEffects(owner, entity);
                        }
                    }

                }
                Explosion.BlockInteraction explosion$blockinteraction = Explosion.BlockInteraction.KEEP;
                NoDamageExplosion explosion = new NoDamageExplosion(level(), this, level().damageSources().explosion(this, this), new ExplosionDamageCalculator(), getX(), getY(), getZ(), Math.min(power / 10, 3), false, explosion$blockinteraction);
                if (!net.minecraftforge.event.ForgeEventFactory.onExplosionStart(level(), explosion)) {
                    explosion.explode();
                    explosion.finalizeExplosion(true);
                }
            }
        }
        tags.add("exploded");
        this.playSound(ModSounds.STAR_EXPLODE.get(), 3.0f, 1f + random.nextFloat() - random.nextFloat());
        if (!level.isClientSide)
            this.setInvisibleStar(true);
    }

    public boolean isInvisibleStar() {
        return this.getPower() < 0F;
    }

    public void setInvisibleStar(boolean z) {
        this.setPower(-1F);
    }

    @Override
    public void recreateFromPacket(@NotNull ClientboundAddEntityPacket p_150128_) {
        for (int i = 0; i < maxTrails; i++) {
            oldX[i] = p_150128_.getX();
            oldY[i] = p_150128_.getY();
            oldZ[i] = p_150128_.getZ();
        }
        super.recreateFromPacket(p_150128_);
    }

    @Override
    public void tick() {
        Entity entity = this.getOwner();
        if (tickCount > 5 * 20) {
            if (!level().isClientSide) {
                setInvisibleStar(true);
            }
        }
        if (this.isInvisibleStar()) {
            if (!level().isClientSide) {
                this.setTrailLifeTime(this.getTrailLifeTime() - 1);

                if (this.getTrailLifeTime() < 0)
                    discard();
            }
            return;
        }

        super.tick();
        if (this.level().isClientSide || (entity == null || !entity.isRemoved()) && this.level().hasChunkAt(this.blockPosition())) {
            if (getDeltaMovement().length() > 3)
                this.setDeltaMovement(this.getDeltaMovement().add(-xPower, -yPower, -zPower));
        }
        if (this.isSeek()) {
            if (!isThisArrowFlying())
                return;
            if (!this.level().isClientSide()) {
                this.updateTarget(seekDistance, seekAngle * 2.0F);
            }
            Entity target = checkSaveTarget();
            if (target != null) {

                Vec3 targetVec = getVectorToTarget(target).scale(seekFactor);
                Vec3 courseVec = getMotionVec();

                // vector lengths
                double courseLen = courseVec.length();
                double targetLen = targetVec.length();
                double totalLen = Math.sqrt(courseLen * courseLen + targetLen * targetLen);

                double dotProduct = courseVec.dot(targetVec) / (courseLen * targetLen); // cosine similarity

                if (dotProduct > seekThreshold) {

                    // add vector to target, scale to match current velocity
                    Vec3 newMotion = courseVec.scale(courseLen / totalLen).add(targetVec.scale(courseLen / totalLen));

                    this.setDeltaMovement(newMotion.add(0, 0.045F, 0));

                } else if (!this.level().isClientSide()) {
                    // too inaccurate for our intended target, give up on it
                    this.setTarget(null);
                }
            }

        }
    }

    protected Entity updateTarget(double distance, double angel) {
        Entity target = level.getEntity(getTargetId());

        if (target != null && !target.isAlive()) {
            target = null;
            this.setTarget(null);
        }

        if (target == null) {
            AABB positionBB = new AABB(getX(), getY(), getZ(), getX(), getY(), getZ());
            AABB targetBB = positionBB;

            // add two possible courses to our selection box
            Vec3 courseVec = getMotionVec().scale(distance).yRot((float) angel);
            targetBB = targetBB.minmax(positionBB.move(courseVec));

            courseVec = getMotionVec().scale(distance).yRot((float) -angel);
            targetBB = targetBB.minmax(positionBB.move(courseVec));

            targetBB = targetBB.inflate(0, distance * 0.5, 0);

            double closestDot = -1.0;
            Entity closestTarget = null;

            List<LivingEntity> entityList = this.level().getEntitiesOfClass(LivingEntity.class, targetBB);
            List<LivingEntity> monsters = entityList.stream().filter(l -> l instanceof Monster).toList();

            if (!monsters.isEmpty()) {
                for (LivingEntity monster : monsters) {
                    if (((Monster) monster).getTarget() == this.getOwner()) {
                        setTarget(monster);
                        return monster;
                    }
                }
                for (LivingEntity monster : monsters) {
                    if (monster instanceof NeutralMob) continue;

                    if (monster.hasLineOfSight(this)) {
                        setTarget(monster);
                        return monster;
                    }
                }
            }

            for (LivingEntity living : entityList) {

                if (!living.hasLineOfSight(this)) continue;

                if (living == this.getOwner()) continue;

                if (getOwner() != null && living instanceof TamableAnimal animal && animal.getOwner() == this.getOwner())
                    continue;

                Vec3 motionVec = getMotionVec().normalize();
                Vec3 targetVec = getVectorToTarget(living).normalize();

                double dot = motionVec.dot(targetVec);

                if (dot > Math.max(closestDot, seekThreshold)) {
                    closestDot = dot;
                    closestTarget = living;
                }
            }

            if (closestTarget != null) {
                setTarget(closestTarget);
                return closestTarget;
            }
        }
        return target;
    }

    protected Entity updateTarget(double seekDistance) {
        return updateTarget(seekDistance, seekAngle);
    }

    private Vec3 getMotionVec() {
        return new Vec3(this.getDeltaMovement().x(), this.getDeltaMovement().y(), this.getDeltaMovement().z());
    }

    private Vec3 getVectorToTarget(Entity target) {
        return new Vec3(target.getX() - this.getX(), (target.getY() + target.getEyeHeight()) - this.getY(), target.getZ() - this.getZ());
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
    }

    @Override
    public float getInertia() {
        return 1F;
    }

    public void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(RGB, new Vector3f(1f));
        this.entityData.define(ALPHA, 1f);
        this.entityData.define(DAMAGE_MULTIPLIER, 1.0F);
        this.entityData.define(POWER_STAR, 1.0F);
        this.entityData.define(TARGET, -1);
        this.entityData.define(TRAIL_LIFE_TIME, maxTrialLife);
    }

    public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putFloat("Red", this.getRed());
        compoundTag.putFloat("Green", this.getGreen());
        compoundTag.putFloat("Blue", this.getBlue());
        compoundTag.putFloat("Alpha", this.getAlpha());
        compoundTag.putInt("StarTarget", this.getTargetId());
        compoundTag.putFloat("PowerStar", getPower());
        compoundTag.putFloat("DamageMultiplier", getDamageMultiplier());
        compoundTag.putInt("TrialLifeTime", getTrailLifeTime());
    }

    public void readAdditionalSaveData(@NotNull CompoundTag p_31474_) {
        super.readAdditionalSaveData(p_31474_);
        this.setRed(p_31474_.getFloat("Red"));
        this.setGreen(p_31474_.getFloat("Green"));
        this.setBlue(p_31474_.getFloat("Blue"));
        this.setAlpha(p_31474_.getFloat("Alpha"));
        this.setTarget(p_31474_.getInt("StarTarget"));
        this.setPower(p_31474_.getFloat("PowerStar"));
        this.setDamageMultiplier(p_31474_.getInt("DamageMultiplier"));
        if (p_31474_.contains("TrialLifeTime"))
            this.entityData.set(TRAIL_LIFE_TIME, p_31474_.getInt("TrialLifeTime"));
    }

    public int getTrailLifeTime() {
        return this.entityData.get(TRAIL_LIFE_TIME);
    }

    public void setTrailLifeTime(int time) {
        this.entityData.set(TRAIL_LIFE_TIME, time);
    }

    public float getPower() {
        return this.entityData.get(POWER_STAR);
    }

    public void setPower(float power) {
        this.entityData.set(POWER_STAR, power);
    }

    public int getTargetId() {
        return this.entityData.get(TARGET);
    }

    public void setTarget(Entity entity) {
        if (entity == null) setTarget(-1);
        else if (getOwner() instanceof Player player && shouldHurt(player, entity))
            this.setTarget(entity.getId());

    }

    public void setTarget(int id) {
        this.entityData.set(TARGET, id);
    }

    public Vector4f getColor() {
        return new Vector4f(getRGB(), getAlpha());
    }

    public void setColor(Vector4f color) {
        setRGB(color.x, color.y, color.z);
        setAlpha(color.w);
        this.color = color;
    }

    private boolean isThisArrowFlying() {
        return !this.onGround && getDeltaMovement().lengthSqr() > 1.0;
    }

    public Vector3f getRGB() {
        return entityData.get(RGB);
    }

    public void setRGB(float r, float g, float b) {
        this.entityData.set(RGB, new Vector3f(r, g, b));
    }

    public float getRed() {
        return getRGB().x;
    }

    public void setRed(float f) {
        Vector3f rgb = getRGB();
        entityData.set(RGB, new Vector3f(f, rgb.y, rgb.z));
    }

    public float getGreen() {
        return getRGB().y;
    }

    public void setGreen(float f) {
        Vector3f rgb = getRGB();
        entityData.set(RGB, new Vector3f(rgb.x, f, rgb.z));
    }

    public float getBlue() {
        return getRGB().z;
    }

    public void setBlue(float f) {
        Vector3f rgb = getRGB();
        entityData.set(RGB, new Vector3f(rgb.x, rgb.y, f));
    }

    public float getAlpha() {
        return entityData.get(ALPHA);
    }

    public void setAlpha(float alpha) {
        entityData.set(ALPHA, alpha);
    }

    public float getDamageMultiplier() {
        return entityData.get(DAMAGE_MULTIPLIER);
    }

    public void setDamageMultiplier(float f) {
        entityData.set(DAMAGE_MULTIPLIER, f);
    }

    public boolean isSeek() {
        return true;
    }

    public boolean hasTarget() {
        return getTargetId() > -1;
    }

    public Entity checkSaveTarget() {
        if (getTargetId() > -1) {
            if (targetEntity == null)
                targetEntity = level.getEntity(getTargetId());
        }
        return targetEntity;
    }

    public Vec3 getTrail(int index) {
        if (index > 0) {
            return new Vec3((oldX[index] + oldX[index - 1]) / 3f, (oldY[index] + oldY[index - 1]) / 3f, (oldZ[index] + oldZ[index - 1]) / 3f);
        }
        return new Vec3(oldX[0], oldY[0], oldZ[0]);
    }

    public static class NoDamageExplosion extends Explosion {
        private static final ExplosionDamageCalculator EXPLOSION_DAMAGE_CALCULATOR = new ExplosionDamageCalculator();
        private final boolean fire;
        private final Explosion.BlockInteraction blockInteraction;
        private final RandomSource random = RandomSource.create();
        private final Level level;
        private final double x;
        private final double y;
        private final double z;
        @javax.annotation.Nullable
        private final Entity source;
        private final float radius;
        private final DamageSource damageSource;
        private final ExplosionDamageCalculator damageCalculator;
        private final ObjectArrayList<BlockPos> toBlow = new ObjectArrayList<>();
        private final Map<Player, Vec3> hitPlayers = Maps.newHashMap();
        private final Vec3 position;

        public NoDamageExplosion(Level p_46051_, @javax.annotation.Nullable Entity p_46052_, @javax.annotation.Nullable DamageSource p_46053_, @javax.annotation.Nullable ExplosionDamageCalculator p_46054_, double p_46055_, double p_46056_, double p_46057_, float p_46058_, boolean p_46059_, Explosion.BlockInteraction p_46060_) {
            super(p_46051_, p_46052_, p_46055_, p_46056_, p_46057_, p_46058_, List.of());
            this.level = p_46051_;
            this.source = p_46052_;
            this.radius = p_46058_;
            this.x = p_46055_;
            this.y = p_46056_;
            this.z = p_46057_;
            this.fire = p_46059_;
            this.blockInteraction = p_46060_;
            this.damageSource = p_46053_ == null ? p_46051_.damageSources().explosion(this) : p_46053_;
            this.damageCalculator = p_46054_ == null ? this.makeDamageCalculator(p_46052_) : p_46054_;
            this.position = new Vec3(this.x, this.y, this.z);
        }

        public static void addBlockDrops(ObjectArrayList<Pair<ItemStack, BlockPos>> p_46068_, @NotNull ItemStack p_46069_, @NotNull BlockPos p_46070_) {
            int i = p_46068_.size();

            for (int j = 0; j < i; ++j) {
                Pair<ItemStack, BlockPos> pair = p_46068_.get(j);
                ItemStack itemstack = pair.getFirst();
                if (ItemEntity.areMergable(itemstack, p_46069_)) {
                    ItemStack itemstack1 = ItemEntity.merge(itemstack, p_46069_, 16);
                    p_46068_.set(j, Pair.of(itemstack1, pair.getSecond()));
                    if (p_46069_.isEmpty()) {
                        return;
                    }
                }
            }

            p_46068_.add(Pair.of(p_46069_, p_46070_));
        }

        public @NotNull ExplosionDamageCalculator makeDamageCalculator(@javax.annotation.Nullable Entity p_46063_) {
            return p_46063_ == null ? EXPLOSION_DAMAGE_CALCULATOR : new EntityBasedExplosionDamageCalculator(p_46063_);
        }

        public void explode() {
            this.level.gameEvent(this.source, GameEvent.EXPLODE, new Vec3(this.x, this.y, this.z));
            Set<BlockPos> set = Sets.newHashSet();

            for (int j = 0; j < 16; ++j) {
                for (int k = 0; k < 16; ++k) {
                    for (int l = 0; l < 16; ++l) {
                        if (j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15) {
                            double d0 = (float) j / 15.0F * 2.0F - 1.0F;
                            double d1 = (float) k / 15.0F * 2.0F - 1.0F;
                            double d2 = (float) l / 15.0F * 2.0F - 1.0F;
                            double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                            d0 /= d3;
                            d1 /= d3;
                            d2 /= d3;
                            float f = this.radius * (0.7F + this.level.random.nextFloat() * 0.6F);
                            double d4 = this.x;
                            double d6 = this.y;
                            double d8 = this.z;

                            for (; f > 0.0F; f -= 0.22500001F) {
                                BlockPos blockpos = BlockPos.containing(d4, d6, d8);
                                BlockState blockstate = this.level.getBlockState(blockpos);
                                FluidState fluidstate = this.level.getFluidState(blockpos);
                                if (!this.level.isInWorldBounds(blockpos)) {
                                    break;
                                }

                                Optional<Float> optional = this.damageCalculator.getBlockExplosionResistance(this, this.level, blockpos, blockstate, fluidstate);
                                if (optional.isPresent()) {
                                    f -= (optional.get() + 0.3F) * 0.3F;
                                }

                                if (f > 0.0F && this.damageCalculator.shouldBlockExplode(this, this.level, blockpos, blockstate, f)) {
                                    set.add(blockpos);
                                }

                                d4 += d0 * (double) 0.3F;
                                d6 += d1 * (double) 0.3F;
                                d8 += d2 * (double) 0.3F;
                            }
                        }
                    }
                }
            }

            this.toBlow.addAll(set);
        }

        public void finalizeExplosion(boolean p_46076_) {
            if (this.level.isClientSide) {
                this.level.playLocalSound(this.x, this.y, this.z, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 4.0F, (1.0F + (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.2F) * 0.7F, false);
            }

            boolean flag = this.interactsWithBlocks();
            if (p_46076_) {
                if (!(this.radius < 2.0F) && flag) {
                    this.level.addParticle(ParticleTypes.EXPLOSION_EMITTER, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
                } else {
                    this.level.addParticle(ParticleTypes.EXPLOSION, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
                }
            }

            if (flag) {
                ObjectArrayList<Pair<ItemStack, BlockPos>> objectarraylist = new ObjectArrayList<>();
                boolean flag1 = this.getIndirectSourceEntity() instanceof Player;
                Util.shuffle(this.toBlow, this.level.random);

                for (BlockPos blockpos : this.toBlow) {
                    BlockState blockstate = this.level.getBlockState(blockpos);
                    if (!blockstate.isAir()) {
                        BlockPos blockpos1 = blockpos.immutable();
                        this.level.getProfiler().push("explosion_blocks");
                        if (blockstate.canDropFromExplosion(this.level, blockpos, this)) {
                            if (this.level instanceof ServerLevel serverlevel) {
                                BlockEntity blockentity = blockstate.hasBlockEntity() ? this.level.getBlockEntity(blockpos) : null;
                                LootParams.Builder lootparams$builder = (new LootParams.Builder(serverlevel)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(blockpos)).withParameter(LootContextParams.TOOL, ItemStack.EMPTY).withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockentity).withOptionalParameter(LootContextParams.THIS_ENTITY, this.source);
                                if (this.blockInteraction == Explosion.BlockInteraction.DESTROY_WITH_DECAY) {
                                    lootparams$builder.withParameter(LootContextParams.EXPLOSION_RADIUS, this.radius);
                                }

                                blockstate.spawnAfterBreak(serverlevel, blockpos, ItemStack.EMPTY, flag1);
                                blockstate.getDrops(lootparams$builder).forEach((p_46074_) -> addBlockDrops(objectarraylist, p_46074_, blockpos1));
                            }
                        }

                        blockstate.onBlockExploded(this.level, blockpos, this);
                        this.level.getProfiler().pop();
                    }
                }

                for (Pair<ItemStack, BlockPos> pair : objectarraylist) {
                    Block.popResource(this.level, pair.getSecond(), pair.getFirst());
                }
            }

            if (this.fire) {
                for (BlockPos blockpos2 : this.toBlow) {
                    if (this.random.nextInt(3) == 0 && this.level.getBlockState(blockpos2).isAir() && this.level.getBlockState(blockpos2.below()).isSolidRender(this.level, blockpos2.below())) {
                        this.level.setBlockAndUpdate(blockpos2, BaseFireBlock.getState(this.level, blockpos2));
                    }
                }
            }

        }

        public boolean interactsWithBlocks() {
            return this.blockInteraction != Explosion.BlockInteraction.KEEP;
        }

        public @NotNull DamageSource getDamageSource() {
            return this.damageSource;
        }

        public @NotNull Map<Player, Vec3> getHitPlayers() {
            return this.hitPlayers;
        }

        @javax.annotation.Nullable
        public LivingEntity getIndirectSourceEntity() {
            if (this.source == null) {
                return null;
            } else {
                Entity entity = this.source;
                if (entity instanceof PrimedTnt primedtnt) {
                    return primedtnt.getOwner();
                } else {
                    if (entity instanceof LivingEntity livingentity) {
                        return livingentity;
                    } else {
                        if (entity instanceof Projectile projectile) {
                            entity = projectile.getOwner();
                            if (entity instanceof LivingEntity) {
                                return (LivingEntity) entity;
                            }
                        }

                        return null;
                    }
                }
            }
        }

        @javax.annotation.Nullable
        public Entity getDirectSourceEntity() {
            return this.source;
        }

        public void clearToBlow() {
            this.toBlow.clear();
        }

        public @NotNull List<BlockPos> getToBlow() {
            return this.toBlow;
        }

        public @NotNull Vec3 getPosition() {
            return this.position;
        }

        @Nullable
        public Entity getExploder() {
            return this.source;
        }
    }
}
