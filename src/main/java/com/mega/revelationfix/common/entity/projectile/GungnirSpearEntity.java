package com.mega.revelationfix.common.entity.projectile;

import com.Polarice3.Goety.api.entities.IOwned;
import com.Polarice3.Goety.common.effects.GoetyEffects;
import com.Polarice3.Goety.common.entities.projectiles.Hellfire;
import com.mega.endinglib.mixin.accessor.AccessorAbstractArrow;
import com.mega.endinglib.util.entity.DamageSourceGenerator;
import com.mega.endinglib.util.entity.MobEffectUtils;
import com.mega.revelationfix.api.entity.ITrailRendererEntity;
import com.mega.revelationfix.client.renderer.entity.SpearTrailTask;
import com.mega.revelationfix.client.renderer.trail.TrailRenderTask;
import com.mega.revelationfix.client.renderer.VFRBuilders;
import com.mega.revelationfix.common.apollyon.client.WrappedTrailData;
import com.mega.revelationfix.common.config.ClientConfig;
import com.mega.revelationfix.common.init.GRItems;
import com.mega.revelationfix.common.init.ModEntities;
import com.mega.revelationfix.safe.DamageSourceInterface;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector4f;

import javax.annotation.Nullable;
import java.util.List;

public class GungnirSpearEntity extends AbstractArrow implements ITrailRendererEntity {
    public static final int maxTrails = 16;
    public static final EntityDataAccessor<Boolean> INVISIBLE = SynchedEntityData.defineId(GungnirSpearEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Byte> ID_LOYALTY = SynchedEntityData.defineId(GungnirSpearEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> ID_FOIL = SynchedEntityData.defineId(GungnirSpearEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> TARGET = SynchedEntityData.defineId(GungnirSpearEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> TRAIL_LIFE_TIME = SynchedEntityData.defineId(GungnirSpearEntity.class, EntityDataSerializers.INT);
    private static final int FLAG_SHOULD_BACK = 2;
    private static final int FLAG_NO_PHYSICS = 8;
    private static final double seekDistance = 8.0;
    private static final double seekFactor = 0.5;
    private static final double seekAngle = Math.PI / 32.0;
    private static final double seekThreshold = 0.5;
    public static int maxTrialLife = 53;
    public final float initialZRot = (float) (Math.random() * 360F);
    public final double rotationFactor = random.triangle(0F, 2F);
    public final WrappedTrailData trailData;
    private final AccessorAbstractArrow accessorAbstractArrow = (AccessorAbstractArrow) this;
    public int clientSideReturnTridentTickCount;
    public boolean shouldBack = false;
    public Vector4f color = new Vector4f(202 / 255F, 122 / 255F, 84 / 255F, 1F);
    public float size = 1F;
    public double[] oldX = new double[maxTrails];
    public double[] oldY = new double[maxTrails];
    public double[] oldZ = new double[maxTrails];
    protected @Nullable Entity targetEntity;
    private ItemStack spearItem = new ItemStack(GRItems.GUNGNIR.get());
    private boolean dealtDamage;

    public GungnirSpearEntity(EntityType<? extends GungnirSpearEntity> p_37561_, Level p_37562_) {
        super(p_37561_, p_37562_);
        this.trailData = new WrappedTrailData(this);
    }

    public GungnirSpearEntity(Level p_37569_, LivingEntity p_37570_, ItemStack p_37571_) {
        super(ModEntities.GUNGNIR.get(), p_37570_, p_37569_);
        this.spearItem = p_37571_.copy();
        this.entityData.set(ID_LOYALTY, (byte) 5);
        this.entityData.set(ID_FOIL, p_37571_.hasFoil());
        this.setBaseDamage(0F);
        this.trailData = new WrappedTrailData(this);
    }

    public static boolean shouldHurt(Player owner, Entity iterator) {
        if (iterator instanceof IOwned owned && owned.getMasterOwner() == owner)
            return false;
        if (iterator instanceof OwnableEntity ownable && ownable.getOwner() == owner)
            return false;
        return EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(iterator);
    }

    public void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ID_LOYALTY, (byte) 0);
        this.entityData.define(ID_FOIL, false);
        this.entityData.define(INVISIBLE, false);
        this.entityData.define(TRAIL_LIFE_TIME, maxTrialLife);
        this.entityData.define(TARGET, -1);
    }

    public boolean isInvisibleSpear() {
        return this.entityData.get(INVISIBLE);
    }

    public void setInvisibleSpear(boolean z) {
        this.entityData.set(INVISIBLE, z);
    }

    public int getTrailLifeTime() {
        return this.entityData.get(TRAIL_LIFE_TIME);
    }

    public void setTrailLifeTime(int time) {
        this.entityData.set(TRAIL_LIFE_TIME, time);
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket p_150170_) {
        for (int i = 0; i < maxTrails; i++) {
            oldX[i] = p_150170_.getX();
            oldY[i] = p_150170_.getY();
            oldZ[i] = p_150170_.getZ();
        }
        super.recreateFromPacket(p_150170_);
    }

    public void tick() {
        if (!level().isClientSide) {
            if (!this.getWrappedTrailData().shouldRenderTrail())
                this.getWrappedTrailData().setShouldRenderTrail(true);
        }
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }
        Entity entity = this.getOwner();
        int i = this.entityData.get(ID_LOYALTY);
        if (i > 0 && (this.dealtDamage || this.isNoPhysics()) && entity != null) {
            if (!this.isAcceptibleReturnOwner()) {
                if (!this.level().isClientSide && this.pickup == AbstractArrow.Pickup.ALLOWED) {
                    this.spawnAtLocation(this.getPickupItem(), 0.1F);
                }

                this.discard();
            } else {
                this.setNoPhysics(true);
                Vec3 vec3 = entity.getEyePosition().subtract(this.position());
                this.setPosRaw(this.getX(), this.getY() + vec3.y * 0.015D * (double) i, this.getZ());
                if (this.level().isClientSide) {
                    this.yOld = this.getY();
                }

                double d0 = 0.05D * (double) i;

                this.setDeltaMovement(this.getDeltaMovement().scale(isNoGravity() ? 1F : 0.95D).add(vec3.normalize().scale(d0)));
                if (this.clientSideReturnTridentTickCount == 0) {
                    this.playSound(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
                }

                ++this.clientSideReturnTridentTickCount;
            }
        }
        super.tick();
        label:
        {
            if (this.isSeek() && !dealtDamage) {
                if (!isThisArrowFlying())
                    break label;
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

        if ((tickCount > 400 || this.getY() < this.level().getMinBuildHeight())) {
            this.dealtDamage = true;
            setNoPhysics(true);
            if (!this.level().isClientSide) {
                if (this.getOwner() != null && isAcceptibleReturnOwner()) {
                    this.setPos(this.getOwner().position().add(new Vec3(0, 1F, 0F)));
                } else if (isNoPhysics()) {
                    if (!this.level().isClientSide && this.pickup == AbstractArrow.Pickup.ALLOWED) {
                        this.spawnAtLocation(this.getPickupItem(), 0.1F);
                    }
                } else if (isNoGravity()) {
                    this.discard();
                }
            }
        }

    }

    protected Entity updateTarget(double distance, double angel) {
        Entity target = level().getEntity(getTargetId());

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

    private boolean isAcceptibleReturnOwner() {
        Entity entity = this.getOwner();
        if (entity != null && entity.isAlive()) {
            return !(entity instanceof ServerPlayer) || !entity.isSpectator();
        } else {
            return false;
        }
    }

    public ItemStack getPickupItem() {
        return this.spearItem.copy();
    }

    public void setSpearItem(ItemStack spearItem) {
        this.spearItem = spearItem;
    }

    public boolean isFoil() {
        return this.entityData.get(ID_FOIL);
    }

    @Nullable
    public EntityHitResult findHitEntity(Vec3 p_37575_, Vec3 p_37576_) {
        return this.dealtDamage ? null : super.findHitEntity(p_37575_, p_37576_);
    }

    public void onHitEntity(EntityHitResult p_37573_) {
        Entity entity = p_37573_.getEntity();
        float f = 20.0F;
        if (entity instanceof LivingEntity livingentity) {
            f += EnchantmentHelper.getDamageBonus(this.spearItem, livingentity.getMobType());
        }
        f += this.getBaseDamage();
        Entity entity1 = this.getOwner();
        DamageSource damagesource = this.damageSources().trident(this, entity1 == null ? this : entity1);
        this.dealtDamage = true;
        SoundEvent soundevent = SoundEvents.TRIDENT_HIT;
        if (entity.hurt(damagesource, f)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (entity instanceof LivingEntity beHit) {
                if (entity1 instanceof LivingEntity owner) {
                    EnchantmentHelper.doPostHurtEffects(beHit, owner);
                    EnchantmentHelper.doPostDamageEffects(owner, beHit);
                    if (this.isNoGravity()) {
                        MobEffectUtils.forceAdd(beHit, new MobEffectInstance(GoetyEffects.STUNNED.get(), 200, 4), owner);
                        MobEffectUtils.forceAdd(beHit, new MobEffectInstance(GoetyEffects.BURN_HEX.get(), 200, 4), owner);
                        Hellfire hellfire = new Hellfire(level(), beHit.getX(), beHit.getY(), beHit.getZ(), owner);
                        level().addFreshEntity(hellfire);
                    } else {
                        for (LivingEntity living : level().getEntitiesOfClass(LivingEntity.class, beHit.getBoundingBox().inflate(6D), (e) -> EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(e) && e != owner)) {
                            MobEffectUtils.forceAdd(living, new MobEffectInstance(GoetyEffects.STUNNED.get(), 300, 4), owner);
                            MobEffectUtils.forceAdd(living, new MobEffectInstance(GoetyEffects.BURN_HEX.get(), 300, 4), owner);
                            Hellfire hellfire = new Hellfire(level(), living.getX(), living.getY(), living.getZ(), owner);
                            level().addFreshEntity(hellfire);
                        }
                    }
                    DamageSourceGenerator generator = new DamageSourceGenerator(beHit);
                    DamageSource source = generator.source(DamageTypes.MAGIC, owner);
                    ((DamageSourceInterface) source).giveSpecialTag((byte) 1);
                    for (LivingEntity beOwned : level().getEntitiesOfClass(LivingEntity.class, beHit.getBoundingBox().inflate(128D), (e) -> ((e instanceof IOwned owned && owned.getTrueOwner() == beHit) || (e instanceof OwnableEntity ownable && ownable.getOwner() == beHit)) && e != beHit && e != owner)) {
                        beOwned.hurt(source, f);
                        EnchantmentHelper.doPostHurtEffects(beOwned, owner);
                        EnchantmentHelper.doPostDamageEffects(owner, beOwned);
                        this.doPostHurtEffects(beOwned);
                    }
                }

                this.doPostHurtEffects(beHit);
            }
        }

        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01D, -0.1D, -0.01D));
        float f1 = 1.0F;
        if (this.level() instanceof ServerLevel && this.level().isThundering() && this.isChanneling()) {
            BlockPos blockpos = entity.blockPosition();
            if (this.level().canSeeSky(blockpos)) {
                LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(this.level());
                if (lightningbolt != null) {
                    lightningbolt.moveTo(Vec3.atBottomCenterOf(blockpos));
                    lightningbolt.setCause(entity1 instanceof ServerPlayer ? (ServerPlayer) entity1 : null);
                    this.level().addFreshEntity(lightningbolt);
                    soundevent = SoundEvents.TRIDENT_THUNDER;
                    f1 = 5.0F;
                }
            }
        }

        this.playSound(soundevent, f1, 1.0F);
    }

    public boolean isChanneling() {
        return EnchantmentHelper.hasChanneling(this.spearItem);
    }

    public boolean tryPickup(Player p_150196_) {
        return super.tryPickup(p_150196_) || (this.isNoPhysics() && this.ownedBy(p_150196_));
    }

    public SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }

    public void playerTouch(Player p_37580_) {
        if (this.ownedBy(p_37580_) || this.getOwner() == null) {
            super.playerTouch(p_37580_);
        }

    }

    @Override
    public AABB getBoundingBoxForCulling() {
        return super.getBoundingBoxForCulling().inflate(16D);
    }

    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        if (compoundTag.contains("Trident", 10)) {
            this.spearItem = ItemStack.of(compoundTag.getCompound("Trident"));
        }

        this.dealtDamage = compoundTag.getBoolean("DealtDamage");
        this.entityData.set(ID_LOYALTY, (byte) EnchantmentHelper.getLoyalty(this.spearItem));
        this.setTarget(compoundTag.getInt("StarTarget"));
        this.setInvisibleSpear(compoundTag.getBoolean("SpearInvisible"));
        if (compoundTag.contains("TrialLifeTime"))
            this.entityData.set(TRAIL_LIFE_TIME, compoundTag.getInt("TrialLifeTime"));
    }

    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.put("Trident", this.spearItem.save(new CompoundTag()));
        compoundTag.putBoolean("DealtDamage", this.dealtDamage);
        compoundTag.putInt("SpearTarget", this.getTargetId());
        compoundTag.putBoolean("SpearInvisible", isInvisibleSpear());
        compoundTag.putInt("TrialLifeTime", getTrailLifeTime());
    }

    public int getTargetId() {
        return this.entityData.get(TARGET);
    }

    private boolean isFriendly(Entity entity) {
        if (this.getOwner() instanceof Player player) {
            if (entity instanceof Player p && p.isAlliedTo(player) && !p.getTeam().isAllowFriendlyFire())
                return true;
            if (entity instanceof IOwned owned && owned.getTrueOwner() == player)
                return true;
            return entity instanceof OwnableEntity ownable && ownable.getOwner() == player;
        }
        return false;
    }

    public void setTarget(Entity entity) {
        if (entity == null || isFriendly(entity)) setTarget(-1);
        else if (getOwner() instanceof Player player && shouldHurt(player, entity))
            this.setTarget(entity.getId());

    }

    public void setTarget(int id) {
        this.entityData.set(TARGET, id);
    }

    private boolean isThisArrowFlying() {
        return !this.onGround() && getDeltaMovement().lengthSqr() > 1.0;
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
                targetEntity = level().getEntity(getTargetId());
        }
        return targetEntity;
    }

    public Vec3 getTrail(int index) {
        if (index > 0) {
            return new Vec3((oldX[index] + oldX[index - 1]) / 3f, (oldY[index] + oldY[index - 1]) / 3f, (oldZ[index] + oldZ[index - 1]) / 3f);
        }
        return new Vec3(oldX[0], oldY[0], oldZ[0]);
    }

    public void tickDespawn() {
        int i = this.entityData.get(ID_LOYALTY);
        if (this.pickup != AbstractArrow.Pickup.ALLOWED || i <= 0) {
            super.tickDespawn();
        }

    }

    public float getWaterInertia() {
        return 0.99F;
    }

    public boolean shouldRender(double p_37588_, double p_37589_, double p_37590_) {
        return true;
    }

    public boolean isNoPhysics() {
        if (!this.level().isClientSide) {
            return this.noPhysics;
        } else {
            return (this.entityData.get(AccessorAbstractArrow.getID_FLAGS()) & FLAG_NO_PHYSICS) != 0;
        }
    }

    public void setNoPhysics(boolean p_36791_) {
        this.noPhysics = p_36791_;
        accessorAbstractArrow.callSetFlag(FLAG_NO_PHYSICS, p_36791_);
    }

    public boolean isShouldBack() {
        if (!this.level().isClientSide) {
            return this.shouldBack;
        } else {
            return (this.entityData.get(AccessorAbstractArrow.getID_FLAGS()) & FLAG_SHOULD_BACK) != 0;
        }
    }

    public void setShouldBack(boolean p_36791_) {
        this.shouldBack = p_36791_;
        accessorAbstractArrow.callSetFlag(FLAG_SHOULD_BACK, p_36791_);
    }

    @Override
    public WrappedTrailData getWrappedTrailData() {
        return this.trailData;
    }

    @Override
    public boolean shouldRenderWrappedTrail() {
        return ClientConfig.enableTrailRenderer && this.getTrailLifeTime() > 35;
    }

    @Override
    public TrailRenderTask createTrailRenderTask(WrappedTrailData data) {
        return new SpearTrailTask(this);
    }
}