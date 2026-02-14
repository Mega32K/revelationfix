package com.mega.revelationfix.common.entity.boss;

import com.Polarice3.Goety.common.entities.boss.Apostle;
import com.Polarice3.Goety.init.ModSounds;
import com.mega.revelationfix.common.init.GRItems;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import org.jetbrains.annotations.NotNull;

public class IceHermitEntity extends SpellcasterIllager {
    public IceHermitEntity(EntityType<? extends SpellcasterIllager> p_33724_, Level p_33725_) {
        super(p_33724_, p_33725_);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.LAVA, 8.0F);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, 0.0F);
        this.xpReward = 1000;
        this.setItemInHand(InteractionHand.MAIN_HAND, GRItems.STAFF_FROSTBLOOM.get().getDefaultInstance());
        this.handDropChances[0] = 0F;
        this.handDropChances[1] = 0F;
    }

    @Override
    public void registerGoals() {
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 15.0F));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Blaze.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, MagmaCube.class, true));
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35F)
                .add(Attributes.MAX_HEALTH, 168.0D)
                .add(Attributes.ATTACK_DAMAGE, 9.0D)
                .add(Attributes.ARMOR, 8.0D);
    }
    @Override
    public @NotNull SoundEvent getCastingSoundEvent() {
        return SoundEvents.PLAYER_HURT_FREEZE;
    }
    public SoundEvent getAmbientSound() {
        return ModSounds.CRYOLOGER_AMBIENT.get();
    }

    public SoundEvent getDeathSound() {
        return ModSounds.CRYOLOGER_DEATH.get();
    }

    public SoundEvent getHurtSound(@NotNull DamageSource pDamageSource) {
        return ModSounds.CRYOLOGER_HURT.get();
    }
    @Override
    public void applyRaidBuffs(int p_37844_, boolean p_37845_) {
    }

    @Override
    public @NotNull SoundEvent getCelebrateSound() {
        return ModSounds.CRYOLOGER_CELEBRATE.get();
    }

    @Override
    public @NotNull IllagerArmPose getArmPose() {
        if (this.isCastingSpell()) {
            return AbstractIllager.IllagerArmPose.SPELLCASTING;
        } else {
            return this.isCelebrating() ? AbstractIllager.IllagerArmPose.CELEBRATING : IllagerArmPose.NEUTRAL;
        }
    }

    @Override
    public boolean isFullyFrozen() {
        return false;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        if (damageSource.is(DamageTypes.FREEZE)) return true;
        return super.isInvulnerableTo(damageSource);
    }
    @Override
    public boolean hurt(DamageSource damageSource, float amount) {
        if (damageSource.is(DamageTypeTags.IS_FREEZING)) {
            if (amount < 6F) return false;
            amount *= 0.5F;
        }
        return super.hurt(damageSource, amount);
    }

    @Override
    protected void dropEquipment() {
        super.dropEquipment();
    }
}
