package com.mega.revelationfix.common.entity;

import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.common.effects.GoetyEffects;
import com.Polarice3.Goety.common.entities.projectiles.AbstractBeam;
import com.Polarice3.Goety.common.network.ModNetwork;
import com.Polarice3.Goety.common.network.client.CBeamPacket;
import com.Polarice3.Goety.config.SpellConfig;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.Goety.utils.ParticleUtil;
import com.mega.revelationfix.common.apollyon.common.DeathPerformance;
import com.mega.revelationfix.safe.DamageSourceInterface;
import com.mega.revelationfix.util.ATAHelper2;
import com.mega.revelationfix.util.EntityActuallyHurt;
import com.mega.revelationfix.util.LivingEntityEC;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.phys.AABB;

public class RevelationCageEntity extends AbstractBeam {
    public static EntityDataAccessor<Integer> ALIVE_TICKS = SynchedEntityData.defineId(RevelationCageEntity.class, EntityDataSerializers.INT);
    public static EntityDataAccessor<Integer> MAX_AGE = SynchedEntityData.defineId(RevelationCageEntity.class, EntityDataSerializers.INT);
    private final RandomSource randomSource = new LegacyRandomSource(666L);

    public RevelationCageEntity(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    public RevelationCageEntity(EntityType<?> p_i48580_1_, Level p_i48580_2_, LivingEntity owner) {
        super(p_i48580_1_, p_i48580_2_);
        this.setOwner(owner);
    }

    public void tick() {
        LivingEntity owner = this.getOwner();
        if (this.getMaxAge() < 0) {
            if (this.level.isClientSide) {
                if (this.owner instanceof Player) {
                    ModNetwork.INSTANCE.sendToServer(new CBeamPacket(this));
                }

                this.updatePositionAndRotation();
            }
            {
                if (owner != null) {
                    //伤害领域
                    synchronized (randomSource) {
                        for (Entity entity : this.level().getEntities(this, new AABB(this.blockPosition()).inflate(8.5D), EntitySelector.NO_CREATIVE_OR_SPECTATOR)) {
                            if (entity instanceof LivingEntity living) {
                                if (owner == living || ownerUUID.equals(living.getUUID())) continue;
                                double fireScale = living.getBoundingBox().getYsize() / 2D;
                                float percentAmount = living.getMaxHealth() / 17F / 20 / 20 + 0.01F;
                                AttributeInstance knockback = living.getAttribute(Attributes.KNOCKBACK_RESISTANCE);
                                AttributeInstance movement = living.getAttribute(Attributes.MOVEMENT_SPEED);

                                if (living.isAlive()) {
                                    double movementSpeed = .1F;
                                    double knockbackValue = 0F;
                                    if (movement != null) {
                                        movementSpeed = movement.getBaseValue();
                                        movement.setBaseValue(0D);
                                    }
                                    if (knockback != null) {
                                        knockbackValue = knockback.getBaseValue();
                                        knockback.setBaseValue(1D);
                                    }
                                    if (!living.hasEffect(GoetyEffects.CURSED.get()))
                                        living.addEffect(new MobEffectInstance(GoetyEffects.CURSED.get(), 20, 0));
                                    ((LivingEntityEC) living).revelationfix$livingECData().banHealingTime = 20;
                                    for (DamageSource source : DeathPerformance.damageSourcesFromForge(owner)) {
                                        //越过盔甲
                                        ((DamageSourceInterface) source).revelationfix$setBypassArmor(true);
                                        //尾砂，伤害成功的话进行播放音效并重置无敌帧
                                        if (randomSource.nextBoolean()) {
                                            //server

                                            if (living.hurt(source, percentAmount)) {
                                                living.playSound(SoundEvents.FIRE_AMBIENT, 2.0F + randomSource.nextFloat(), randomSource.nextFloat() * 0.7F + 0.3F);
                                                living.invulnerableTime = 0;
                                            }
                                        } else {
                                            label0:
                                            {
                                                if (ATAHelper2.hasOdamane(living)) break label0;
                                                if (living.isAlive() && living.level.isClientSide) {
                                                    ParticleUtil.addParticleInternal(ModParticleTypes.BIG_FIRE.get(), false, living.getRandomX(0.3D * fireScale), living.getY(0.2) + randomSource.nextFloat(), living.getRandomZ(0.3D * fireScale), randomSource.triangle(0D, 0.25D * fireScale), randomSource.nextFloat() * 0.1F + 0.25F * fireScale, randomSource.triangle(0D, 0.25D * fireScale));
                                                }
                                                new EntityActuallyHurt(living).actuallyHurt(source, percentAmount, true);
                                                living.playSound(SoundEvents.FIRE_AMBIENT, 2.0F + randomSource.nextFloat(), randomSource.nextFloat() * 0.7F + 0.3F);
                                                living.invulnerableTime = 0;
                                            }
                                        }
                                        if (living instanceof Player player && !living.isAlive()) {
                                            if (!player.isSpectator()) {
                                                player.dropAllDeathLoot(source);
                                            }
                                        }
                                    }
                                    if (knockback != null)
                                        knockback.setBaseValue(knockbackValue);
                                    if (movement != null)
                                        movement.setBaseValue(movementSpeed);
                                    if (living.tickCount > 0 && !ATAHelper2.hasOdamane(living)) {
                                        living.setPos(living.xOld, living.yOld, living.zOld);
                                        living.setOldPosAndRot();
                                        living.setDeltaMovement(0F, 0F, 0F);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (owner != null && owner.isAlive() && SpellConfig.CorruptionImmobile.get()) {
                owner.setDeltaMovement(0.0, owner.getDeltaMovement().y, 0.0);
                owner.xxa = 0.0F;
                owner.zza = 0.0F;
            }
        }
        if (!this.level.isClientSide) {
            this.setAliveTicks(this.getAliveTicks() + 1);
            if (owner == null || !owner.isAlive() || this.itemBase && !MobUtil.isSpellCasting(owner)) {
                if (this.getMaxAge() < 0) {
                    this.setMaxAge(this.getAliveTicks());
                }
            }
            int maxAge = this.getMaxAge();
            if (maxAge > 0) {
                if (this.getAliveTicks() - maxAge > 30) {
                    this.discard();
                    return;
                }
            }
            if (maxAge < 0)
                this.updatePositionAndRotation();
        }
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        return super.getBoundingBoxForCulling().inflate(9.0D);
    }

    public float alpha(float partialTicks) {
        int maxAge = this.getMaxAge();
        if (maxAge > 0)
            return Math.max(0.0F, 1F - Math.min((this.getAliveTicks() + partialTicks - maxAge) / 30.0F, 1.0F));
        else return Math.min((this.getAliveTicks() + partialTicks) / 30.0F, 1.0F);
    }

    public int getAliveTicks() {
        return this.entityData.get(ALIVE_TICKS);
    }

    public void setAliveTicks(int ticks) {
        this.entityData.set(ALIVE_TICKS, ticks);
    }

    public int getMaxAge() {
        return this.entityData.get(MAX_AGE);
    }

    public void setMaxAge(int ticks) {
        this.entityData.set(MAX_AGE, ticks);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ALIVE_TICKS, 0);
        this.entityData.define(MAX_AGE, -1);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setAliveTicks(pCompound.getInt("AliveTicks"));
        if (pCompound.contains("MaxAge"))
            this.setMaxAge(pCompound.getInt("MaxAge"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("AliveTicks", this.getAliveTicks());
        pCompound.putInt("MaxAge", this.getMaxAge());
    }
}
