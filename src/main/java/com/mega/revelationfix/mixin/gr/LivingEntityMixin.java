package com.mega.revelationfix.mixin.gr;

import com.Polarice3.Goety.common.effects.GoetyEffects;
import com.Polarice3.Goety.common.entities.boss.Apostle;
import com.Polarice3.Goety.common.entities.neutral.Owned;
import com.Polarice3.Goety.common.entities.util.SummonCircle;
import com.Polarice3.Goety.init.ModSounds;
import com.mega.revelationfix.common.config.CommonConfig;
import com.mega.revelationfix.util.entity.ATAHelper2;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ForgeHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import z1gned.goetyrevelation.config.ModConfig;
import z1gned.goetyrevelation.entitiy.WitherServant;
import z1gned.goetyrevelation.util.ATAHelper;
import z1gned.goetyrevelation.util.ApollyonAbilityHelper;
import z1gned.goetyrevelation.util.PlayerAbilityHelper;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Unique
    private int allTitlesApostle_1_20_1$ambientSoundTime;

    public LivingEntityMixin(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Shadow
    public abstract float getSoundVolume();

    @Shadow
    public abstract boolean addEffect(MobEffectInstance p_21165_);

    @Shadow
    public abstract float getHealth();

    @Inject(at = @At("RETURN"), method = "checkTotemDeathProtection", cancellable = true)
    private void protect(DamageSource p_21263_, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity living = (LivingEntity) (Object) this;
        if (living instanceof Player player) {
            if (ATAHelper.hasHalo(player) && ((PlayerAbilityHelper) player).getMeteor() <= 0) {
                player.setHealth(player.getMaxHealth() / 2);
                ((PlayerAbilityHelper) player).setMeteor(36000);
                ((PlayerAbilityHelper) player).setMeteoring(6000);
                ((PlayerAbilityHelper) player).setInvulTick(400);
                this.allTitlesApostle_1_20_1$spawnWitherServant(player);
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(at = @At("RETURN"), method = "tick")
    private void getTarget(CallbackInfo ci) {
        if (CommonConfig.redirectTargetToServantOption) {
            if ((LivingEntity) (Object) this instanceof Mob mob) {
                LivingEntity target = mob.getTarget();
                if (target != null && mob.isAlive()) {
                    if (target instanceof Player player) {
                        Owned owned = mob.level().getNearestEntity(Owned.class, TargetingConditions.DEFAULT, mob, 0.0D, 0.0D, 0.0D, new AABB(mob.blockPosition()).inflate(mob.getAttributeValue(Attributes.FOLLOW_RANGE)));
                        if (owned != null && owned.getTrueOwner() == player && !owned.isDeadOrDying()) {
                            mob.setTarget(owned);
                        }
                    }
                }
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "canStandOnFluid", cancellable = true)
    private void walkOnLava(FluidState p_204042_, CallbackInfoReturnable<Boolean> cir) {
        if ((LivingEntity) (Object) this instanceof Player player) {
            if (ATAHelper.hasHalo(player)) {
                cir.setReturnValue(p_204042_.is(Fluids.LAVA) && !player.isCrouching());
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "baseTick")
    private void ambientSound(CallbackInfo ci) {
        if ((LivingEntity) (Object) this instanceof Player player) {
            if (ATAHelper.hasHalo(player) && !ATAHelper2.hasOdamane(player)) {
                if (player.isAlive() && player.getRandom().nextInt(1000) < this.allTitlesApostle_1_20_1$ambientSoundTime++) {
                    this.allTitlesApostle_1_20_1$resetAmbientSoundTime();
                    if (ModConfig.ENABLE_PLAYER_AMBIENT.get()) {
                        player.playSound(ModSounds.APOSTLE_AMBIENT.get(), this.getSoundVolume(), this.getSoundVolume());
                    }
                }
            }
        }
    }

    @Inject(method = "getVoicePitch", at = @At("RETURN"), cancellable = true)
    private void getVoicePitch(CallbackInfoReturnable<Float> cir) {
        if ((Object) this instanceof Player player && ATAHelper2.hasOdamane(player))
            cir.setReturnValue(cir.getReturnValueF() * 1.2F);
    }

    @Inject(at = @At("HEAD"), method = "playHurtSound")
    private void hurtSound(DamageSource p_21160_, CallbackInfo ci) {
        this.allTitlesApostle_1_20_1$resetAmbientSoundTime();
    }

    @Inject(at = @At("HEAD"), method = "getMaxHealth", cancellable = true)
    private void setMaxHealth(CallbackInfoReturnable<Float> cir) {
        if ((LivingEntity) (Object) this instanceof Apostle apostle && ((ApollyonAbilityHelper) apostle).allTitlesApostle_1_20_1$isApollyon()) {
            cir.setReturnValue((float) (1.0D * ModConfig.APOLLYON_HEALTH.get()));
        }
    }

    @Inject(at = @At("HEAD"), method = "hurt")
    private void burn(DamageSource p_21016_, float p_21017_, CallbackInfoReturnable<Boolean> cir) {
        if (p_21016_.getEntity() == null) return;
        if (p_21016_.getEntity() instanceof Player player && !this.isInvulnerableTo(p_21016_)) {
            if (ATAHelper.hasHalo(player)) {
                this.addEffect(new MobEffectInstance(GoetyEffects.BURN_HEX.get(), 100));
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "hurt", cancellable = true)
    private void canHurt(DamageSource p_21016_, float p_21017_, CallbackInfoReturnable<Boolean> cir) {
        if ((LivingEntity) (Object) this instanceof Apostle apostle) {
            if (apostle.isInNether() && ((ApollyonAbilityHelper) apostle).allTitlesApostle_1_20_1$isApollyon() && ((ApollyonAbilityHelper) apostle).allTitlesApostle_1_20_1$getHitCooldown() > 0) {
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "actuallyHurt")
    private void setCooldown(DamageSource p_21240_, float p_21241_, CallbackInfo ci) {
        if ((LivingEntity) (Object) this instanceof Apostle apostle) {
            if (apostle.isInNether() && ((ApollyonAbilityHelper) apostle).allTitlesApostle_1_20_1$isApollyon()) {
                ((ApollyonAbilityHelper) apostle).allTitlesApostle_1_20_1$setHitCooldown(30);
            }
        }
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraftforge/common/ForgeHooks;onLivingDamage(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/damagesource/DamageSource;F)F", remap = false), method = "actuallyHurt")
    private float setHealthValue(LivingEntity entity, DamageSource src, float amount) {
        if ((LivingEntity) (Object) this instanceof Apostle apostle) {
            if (((ApollyonAbilityHelper) apostle).allTitlesApostle_1_20_1$isApollyon() && !apostle.isInNether()) {
                return Math.min(ForgeHooks.onLivingDamage(entity, src, amount), (float) (1.0F * ModConfig.APOLLYON_HURT_LIMIT.get()));
            }
        }

        return ForgeHooks.onLivingDamage(entity, src, amount);
    }

    @Unique
    private void allTitlesApostle_1_20_1$spawnWitherServant(Player player) {
        Level level = player.level();

        WitherServant witherBoss = new WitherServant(z1gned.goetyrevelation.entitiy.ModEntityType.WITHER_SERVANT.get(), player.level());
        witherBoss.setTrueOwner(player);
        witherBoss.finalizeSpawn((ServerLevel) level, level.getCurrentDifficultyAt(player.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);

        SummonCircle summonCircle = new SummonCircle(level, player.blockPosition().offset(0, 2, 0), witherBoss, false, true, player);
        level.addFreshEntity(summonCircle);
    }

    @Unique
    public int allTitlesApostle_1_20_1$getAmbientSoundInterval() {
        return 440;
    }

    @Unique
    private void allTitlesApostle_1_20_1$resetAmbientSoundTime() {
        this.allTitlesApostle_1_20_1$ambientSoundTime = -this.allTitlesApostle_1_20_1$getAmbientSoundInterval();
    }

}
