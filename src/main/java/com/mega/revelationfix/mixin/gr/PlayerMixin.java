package com.mega.revelationfix.mixin.gr;

import com.Polarice3.Goety.common.entities.hostile.servants.ObsidianMonolith;
import com.Polarice3.Goety.common.entities.projectiles.NetherMeteor;
import com.Polarice3.Goety.init.ModSounds;
import com.mega.revelationfix.common.capability.entity.GRPlayerCapability;
import com.mega.revelationfix.proxy.CommonProxy;
import com.mega.revelationfix.safe.OdamanePlayerExpandedContext;
import com.mega.revelationfix.util.entity.ATAHelper2;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import z1gned.goetyrevelation.config.ModConfig;
import z1gned.goetyrevelation.util.ATAHelper;
import z1gned.goetyrevelation.util.PlayerAbilityHelper;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements PlayerAbilityHelper {
    @Unique
    private float revelationfix$haloSpin;
    @Unique
    private ObsidianMonolith obsidianMonolith;

    protected PlayerMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @Shadow
    public abstract void remove(RemovalReason p_150097_);

    @Shadow
    public abstract boolean isSpectator();

    @Shadow public abstract void tick();

    @Inject(at = @At("RETURN"), method = "isInvulnerableTo", cancellable = true)
    private void invul(DamageSource p_36249_, CallbackInfoReturnable<Boolean> cir) {
        if (!p_36249_.is(DamageTypes.FELL_OUT_OF_WORLD)) {
            if (this.getInvulTick() > 0 && ModConfig.PLAYER_INVULNERABLE.get()) {
                cir.setReturnValue(true);
            }
        }

        if (p_36249_.is(DamageTypeTags.IS_EXPLOSION) && this.getMeteoring() > 0) {
            cir.setReturnValue(true);
        }

        if (p_36249_.is(DamageTypeTags.IS_FIRE) && ATAHelper.hasHalo(this)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(at = @At("RETURN"), method = "aiStep")
    private void spinTick(CallbackInfo ci) {
        if (this.getInvulTick() > 0) {
            this.setInvulTick(this.getInvulTick() - 1);
        }

        if (this.level().isClientSide) {
            if (this.isAlive()) {
                if (this.getSpin() < 3.14F) {
                    this.setSpin(this.getSpin() + 0.01F);
                } else {
                    this.setSpin(-3.14F);
                }
            }
        }

        if (this.isAlive()) {
            if (ATAHelper.hasHalo(this)) {
                if (this.getMeteor() > 0) {
                    this.setMeteor(this.getMeteor() - 1);
                }

                if (this.getMeteoring() > 0) {
                    this.setMeteoring(this.getMeteoring() - 1);
                }

                if (this.level().dimension() == Level.NETHER) {
                    if (this.tickCount % 25 == 0) {
                        this.heal(0.8F);
                    }
                }
            }

            if (this.getMeteoring() > 0 && ATAHelper.hasHalo(this) && !isSpectator()) {
                if (this.tickCount % 20 == 0) {
                    BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos(this.getRandomX(0.2), this.getY(), this.getRandomZ(0.2));

                    while ((double) blockPos.getY() < this.getY() + 64.0 && !this.level().getBlockState(blockPos).isSolidRender(this.level(), blockPos)) {
                        blockPos.move(Direction.UP);
                    }

                    if ((double) blockPos.getY() > this.getY() + 32.0) {
                        NetherMeteor fireball = this.getNetherMeteor();
                        fireball.setPos(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                        this.level().addFreshEntity(fireball);
                    }
                }

                if (!this.level().isClientSide) {
                    ServerLevel serverLevel = (ServerLevel) this.level();
                    if (!serverLevel.isThundering()) {
                        serverLevel.setWeatherParameters(0, 6000, true, true);
                    }
                }
            }
        }
    }

    @Inject(at = @At("RETURN"), method = "getDeathSound", cancellable = true)
    private void deathSound(CallbackInfoReturnable<SoundEvent> cir) {
        if (ATAHelper2.hasOdamane(this)) {
            cir.setReturnValue(SoundEvents.WITHER_DEATH);
        } else if (ATAHelper.hasHalo(this)) {
            cir.setReturnValue(ModSounds.APOSTLE_DEATH.get());
        }
    }

    @Inject(at = @At("RETURN"), method = "getHurtSound", cancellable = true)
    private void hurtSound(DamageSource p_36310_, CallbackInfoReturnable<SoundEvent> cir) {
        if (ATAHelper2.hasOdamane(this)) {
            cir.setReturnValue(SoundEvents.WITHER_HURT);
        } else if (ATAHelper.hasHalo(this)) {
            cir.setReturnValue(ModSounds.APOSTLE_HURT.get());
        }
    }

    @ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"), index = 1, method = "hurt")
    private float modifyTokenDamage(float p_21017_) {
        if (this.getInvulTick() > 0 && !ModConfig.PLAYER_INVULNERABLE.get()) {
            p_21017_ *= 0.2F;
        }
        return ATAHelper.hasHalo(this) && this.level().dimension() == Level.NETHER ? p_21017_ / 2 : p_21017_;
    }

    @Override
    public float getSpin() {
        return revelationfix$haloSpin;
    }

    @Override
    public void setSpin(float spin) {
        this.revelationfix$haloSpin = spin;
    }

    @Override
    public int getInvulTick() {
        return CommonProxy.getPlayerCapOptional((Player) (Object) this).lazyMap(GRPlayerCapability::getInvulTick).orElse(0);
    }

    @Override
    public void setInvulTick(int tick) {
        CommonProxy.getPlayerCapOptional((Player) (Object) this).ifPresent(cap -> cap.setInvulTick(tick));
    }

    @Override
    public int getMeteor() {
        return CommonProxy.getPlayerCapOptional((Player) (Object) this).lazyMap(GRPlayerCapability::getHaloReviveCooldown).orElse(0);
    }

    @Override
    public void setMeteor(int tick) {
        CommonProxy.getPlayerCapOptional((Player) (Object) this).ifPresent(cap -> cap.setHaloReviveCooldown(tick));
    }

    @Override
    public int getMeteoring() {
        return CommonProxy.getPlayerCapOptional((Player) (Object) this).lazyMap(GRPlayerCapability::getMeteorTime).orElse(0);
    }

    @Override
    public void setMeteoring(int tick) {
        CommonProxy.getPlayerCapOptional((Player) (Object) this).ifPresent(cap -> cap.setMeteorTime(tick));
    }

    @Override
    public ObsidianMonolith getMonolith() {
        if (ATAHelper2.hasOdamane(this)) {
            OdamanePlayerExpandedContext context = ATAHelper2.getOdamaneEC(this);
            return context.getOwnedMonoliths().get(0) == null ? this.obsidianMonolith : context.getOwnedMonoliths().get(0);
        }
        return this.obsidianMonolith;
    }

    @Override
    public void setMonolith(ObsidianMonolith obsidianMonolith) {
        if (ATAHelper2.hasOdamane(this)) {
            OdamanePlayerExpandedContext context = ATAHelper2.getOdamaneEC(this);
            context.getOwnedMonoliths().add(obsidianMonolith);
        } else this.obsidianMonolith = obsidianMonolith;
    }

    @Unique
    private @NotNull NetherMeteor getNetherMeteor() {
        int range = this.getHealth() < this.getMaxHealth() / 2.0F ? 450 : 900;
        int trueRange = this.getHealth() < this.getHealth() / 4.0F ? range / 2 : range;
        RandomSource random = this.level().random;
        double d = random.nextBoolean() ? 1 : -1;
        double e = random.nextBoolean() ? 1 : -1;
        double d2 = (double) random.nextInt(trueRange) * d;
        double d3 = -900.0;
        double d4 = (double) random.nextInt(trueRange) * e;
        NetherMeteor meteor = new NetherMeteor(this.level(), this, d2, d3, d4);
        meteor.setDangerous(false);
        return meteor;
    }

}
