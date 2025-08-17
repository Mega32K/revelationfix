package com.mega.revelationfix.mixin.goety.overwrite;

import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.common.effects.GoetyEffects;
import com.Polarice3.Goety.common.entities.util.FireBlastTrap;
import com.Polarice3.Goety.config.AttributesConfig;
import com.Polarice3.Goety.utils.ColorUtil;
import com.Polarice3.Goety.utils.CuriosFinder;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.Goety.utils.ModDamageSource;
import com.mega.revelationfix.util.client.ClientParticleUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.*;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mixin(FireBlastTrap.class)
public abstract class FireBlastTrapMixin extends Entity {
    @Unique
    private static final EntityDataAccessor<Float> AREA_EFFECT_DATA = SynchedEntityData.defineId(FireBlastTrap.class, EntityDataSerializers.FLOAT);
    @Shadow(remap = false)
    @Final
    private static EntityDataAccessor<Boolean> IMMEDIATE;
    @Shadow(remap = false)
    public LivingEntity owner;
    @Shadow(remap = false)
    private int burning;
    @Shadow(remap = false)
    private UUID ownerUniqueId;
    @Shadow(remap = false)
    private float extraDamage;

    public FireBlastTrapMixin(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    /**
     * @author Mega
     * @reason TPS UP FIX
     */
    @Overwrite
    protected void defineSynchedData() {
        this.entityData.define(AREA_EFFECT_DATA, 0.0F);
        this.entityData.define(IMMEDIATE, false);
    }

    /**
     * @author Mega
     * @reason TPS UP FIX
     */
    @Overwrite
    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.hasUUID("Owner")) {
            this.ownerUniqueId = compound.getUUID("Owner");
        }

        this.extraDamage = compound.getFloat("ExtraDamage");
        this.setAreaOfEffect(compound.getFloat("AreaOfEffect"));
        this.burning = compound.getInt("Burning");
    }

    /**
     * @author Mega
     * @reason TPS UP FIX
     */
    @Overwrite
    protected void addAdditionalSaveData(CompoundTag compound) {
        if (this.ownerUniqueId != null) {
            compound.putUUID("Owner", this.ownerUniqueId);
        }

        compound.putFloat("ExtraDamage", this.extraDamage);
        compound.putFloat("AreaOfEffect", this.getAreaOfEffect());
        compound.putInt("Burning", this.burning);
    }

    /**
     * @author Mega
     * @reason TPS UP FIX
     */
    @Overwrite(remap = false)
    @Nullable
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUniqueId != null && this.level() instanceof ServerLevel) {
            Entity entity = ((ServerLevel) this.level()).getEntity(this.ownerUniqueId);
            if (entity instanceof LivingEntity) {
                this.owner = (LivingEntity) entity;
            }
        }

        return this.owner;
    }

    /**
     * @author Mega
     * @reason TPS UP FIX
     */
    @Overwrite(remap = false)
    public void setOwner(@Nullable LivingEntity ownerIn) {
        this.owner = ownerIn;
        this.ownerUniqueId = ownerIn == null ? null : ownerIn.getUUID();
    }

    /**
     * @author Mega
     * @reason TPS UP FIX
     */
    @Overwrite(remap = false)
    public int getBurning() {
        return this.burning;
    }

    /**
     * @author Mega
     * @reason TPS UP FIX
     */
    @Overwrite(remap = false)
    public void setBurning(int burning) {
        this.burning = burning;
    }

    /**
     * @author Mega
     * @reason TPS UP FIX
     */
    @Overwrite(remap = false)
    public float getExtraDamage() {
        return this.extraDamage;
    }

    /**
     * @author Mega
     * @reason TPS UP FIX
     */
    @Overwrite(remap = false)
    public void setExtraDamage(float damage) {
        this.extraDamage = damage;
    }

    /**
     * @author Mega
     * @reason TPS UP FIX
     */
    @Overwrite(remap = false)
    public float getAreaOfEffect() {
        return this.entityData.get(AREA_EFFECT_DATA);
    }

    /**
     * @author Mega
     * @reason TPS UP FIX
     */
    @Overwrite(remap = false)
    public void setAreaOfEffect(float damage) {
        this.entityData.set(AREA_EFFECT_DATA, damage);
    }

    /**
     * @author Mega
     * @reason TPS UP FIX
     */
    @Overwrite(remap = false)
    public boolean getImmediate() {
        return this.entityData.get(IMMEDIATE);
    }

    /**
     * @author Mega
     * @reason TPS UP FIX
     */
    @Overwrite(remap = false)
    public void setImmediate(boolean immediate) {
        this.entityData.set(IMMEDIATE, immediate);
    }

    /**
     * @author Mega
     * @reason TPS UP FIX
     */
    @Overwrite
    public @NotNull PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }

    /**
     * @author Mega
     * @reason TPS UP FIX
     */
    @Overwrite
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    /**
     * @author Mega
     * @reason TPS UP FIX
     */
    @Overwrite
    public void tick() {
        super.tick();
        float area = this.getAreaOfEffect() / 2;
        float f = 1.5F + area;
        float f5 = (float) Math.PI * f * f;
        Level level = level();
        if (level.isClientSide) {
            for (int k1 = 0; (float) k1 < f5; ++k1) {
                float f6 = this.random.nextFloat() * ((float) Math.PI * 2F);
                float f7 = Mth.sqrt(this.random.nextFloat()) * f;
                float f8 = Mth.cos(f6) * f7;
                float f9 = Mth.sin(f6) * f7;
                level.addParticle(ModParticleTypes.BURNING.get(), this.getX() + (double) f8, this.getY(), this.getZ() + (double) f9, 0, 0, 0);
            }
            ColorUtil color = new ColorUtil(ChatFormatting.GOLD);
            ClientParticleUtil.windParticle(level, color, (f - 1.0F) + level.random.nextFloat() * 0.5F, 0.0F, this.getId(), this.position());
            ClientParticleUtil.windParticle(level, color, f + level.random.nextFloat() * 0.5F, 0.0F, this.getId(), this.position());
            if (this.tickCount == 20 || this.getImmediate()) {
                for (int j1 = 0; j1 < 16; ++j1) {
                    for (int k1 = 0; (float) k1 < f5; ++k1) {
                        float f6 = this.random.nextFloat() * ((float) Math.PI * 2F);
                        float f7 = Mth.sqrt(this.random.nextFloat()) * f;
                        float f8 = Mth.cos(f6) * f7;
                        float f9 = Mth.sin(f6) * f7;
                        ClientParticleUtil.handleSendParticlesOnClient(level, ParticleTypes.FLAME, this.getX() + (double) f8, this.getY(), this.getZ() + (double) f9, 0, 0, 0.5D, 0, 0.5F);
                    }
                }
            }
        }
        if (level instanceof ServerLevel serverLevel) {
            if (this.tickCount == 20 || this.getImmediate()) {
                List<Entity> targets = new ArrayList<>();
                float area0 = 1.0F + area;
                AABB aabb = this.getBoundingBox();
                AABB aabb1 = new AABB(aabb.minX - area0, aabb.minY - 1.0F, aabb.minZ - area0, aabb.maxX + area0, aabb.maxY + 1.0F, aabb.maxZ + area0);
                for (Entity entity : this.level().getEntitiesOfClass(Entity.class, aabb1)) {
                    if (this.owner != null) {
                        if (entity != this.owner && !MobUtil.areAllies(entity, this.owner)) {
                            if (this.owner instanceof Mob mob && this.owner instanceof Enemy && entity instanceof Enemy) {
                                if (mob.getTarget() == entity) {
                                    targets.add(entity);
                                }
                            } else {
                                targets.add(entity);
                            }
                        }
                    } else {
                        targets.add(entity);
                    }
                }
                if (!targets.isEmpty()) {
                    for (Entity entity : targets) {
                        if ((this.owner != null && CuriosFinder.hasUnholySet(this.owner))) {
                            if (entity instanceof LivingEntity livingEntity) {
                                livingEntity.addEffect(new MobEffectInstance(GoetyEffects.BURN_HEX.get(), 1200));
                            }
                            entity.hurt(ModDamageSource.hellfire(this, this.owner), AttributesConfig.ApostleMagicDamage.get().floatValue() + this.getExtraDamage());
                        } else {
                            if (this.owner != null) {
                                float damage = 5.0F;
                                if (this.owner instanceof Mob mob && mob.getAttribute(Attributes.ATTACK_DAMAGE) != null) {
                                    damage = (float) mob.getAttributeValue(Attributes.ATTACK_DAMAGE);
                                }
                                entity.hurt(ModDamageSource.magicFireBreath(this, this.owner), damage + this.getExtraDamage());
                            } else {
                                entity.hurt(damageSources().magic(), 5.0F + this.getExtraDamage());
                            }
                        }
                        if (entity instanceof LivingEntity livingEntity) {
                            MobUtil.push(livingEntity, 0.0D, 1.0D, 0.0D, 0.5D);
                            if (this.burning > 0) {
                                livingEntity.setSecondsOnFire(this.burning * 4);
                            }
                        }
                    }
                }
            }
        }
        if (this.tickCount > 20 || (this.getImmediate() && this.tickCount > 5)) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, 0.25D, 0.0D));
            this.move(MoverType.SELF, this.getDeltaMovement());
        }
        if (this.tickCount == 20 || (this.getImmediate() && this.tickCount == 5)) {
            this.playSound(SoundEvents.GENERIC_EXPLODE, 1.0F, 0.5F);
        }
        if (this.owner != null) {
            if (this.owner.isDeadOrDying() || this.owner.isRemoved()) {
                this.discard();
            }
        }
        if (this.tickCount % 30 == 0) {
            this.discard();
        }
    }
}
