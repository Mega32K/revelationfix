package com.mega.revelationfix.mixin.goety;

import com.Polarice3.Goety.common.entities.boss.Apostle;
import com.Polarice3.Goety.common.entities.projectiles.DeathArrow;
import com.mega.revelationfix.common.apollyon.client.WrappedTrailUpdate;
import com.mega.revelationfix.common.apollyon.common.BypassInvulArrow;
import com.mega.revelationfix.common.config.ModpackCommonConfig;
import com.mega.revelationfix.common.item.tool.combat.bow.BowOfRevelationItem;
import com.mega.revelationfix.safe.entity.DeathArrowEC;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import z1gned.goetyrevelation.util.ApollyonAbilityHelper;

@Mixin(DeathArrow.class)
public abstract class DeathArrowMixin extends Arrow implements DeathArrowEC {
    static {
        WrappedTrailUpdate.SHOULD_RENDER_TRAIL = SynchedEntityData.defineId(DeathArrow.class, EntityDataSerializers.BOOLEAN);
    }

    @Unique
    public int revelationfix$inGroundTime = 0;
    @Unique
    private WrappedTrailUpdate revelationfix$wrappedTrailUpdate;

    public DeathArrowMixin(EntityType<? extends Arrow> p_36858_, Level p_36859_) {
        super(p_36858_, p_36859_);
    }

    @Override
    public void defineSynchedData() {
        this.entityData.define(WrappedTrailUpdate.SHOULD_RENDER_TRAIL, false);
        super.defineSynchedData();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag p_36875_) {
        super.readAdditionalSaveData(p_36875_);
        revelationfix$getTrailData().setShouldRenderTrail(p_36875_.getBoolean("ShouldRenderDoomTrail"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag p_36881_) {
        super.addAdditionalSaveData(p_36881_);
        p_36881_.putBoolean("ShouldRenderDoomTrail", revelationfix$getTrailData().shouldRenderTrail());
    }

    @Inject(method = "<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;)V", at = @At("RETURN"))
    private void init1(EntityType p_36721_, Level p_36722_, CallbackInfo ci) {
        revelationfix$wrappedTrailUpdate = new WrappedTrailUpdate(this);
    }

    @Inject(method = "<init>(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;)V", at = @At("RETURN"))
    private void init2(Level p_36866_, LivingEntity p_36867_, CallbackInfo ci) {
        revelationfix$wrappedTrailUpdate = new WrappedTrailUpdate(this);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        if (this.onGround)
            revelationfix$inGroundTime++;
        if (!level.isClientSide) {
            if (revelationfix$inGroundTime > 20 && revelationfix$getTrailData().shouldRenderTrail())
                revelationfix$wrappedTrailUpdate.setShouldRenderTrail(false);
        }
    }

    @Redirect(method = "doPostHurtEffects", at = @At(value = "INVOKE", target = "Lcom/Polarice3/Goety/common/entities/boss/Apostle;heal(F)V"))
    private void modifyArrowHeal(Apostle instance, float p_21116_) {
        if (ModpackCommonConfig.apollyonModpackMode && ((ApollyonAbilityHelper) instance).allTitlesApostle_1_20_1$isApollyon()) {
            instance.heal((float) ((instance.isSecondPhase() ? ModpackCommonConfig.apollyonSecondPhaseArrowHealing : ModpackCommonConfig.apollyonSelfHealing) * instance.getMaxHealth()));
        } else instance.heal(p_21116_);
    }

    @Override
    public int revelationfix$inGroundTime() {
        return revelationfix$inGroundTime;
    }

    @Override
    public void onClientRemoval() {
        revelationfix$wrappedTrailUpdate.remove();
        super.onClientRemoval();
    }

    @Override
    public WrappedTrailUpdate revelationfix$getTrailData() {
        return revelationfix$wrappedTrailUpdate;
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        if (this.tags.contains(BypassInvulArrow.TAG_BYPASS_NAME))
            return super.getBoundingBoxForCulling().inflate(2d);
        return super.getBoundingBoxForCulling();
    }

    @Override
    public boolean displayFireAnimation() {
        if (revelationfix$getTrailData().shouldRenderTrail())
            return false;
        return super.displayFireAnimation();
    }

    @Override
    public void makeParticle(int p_36877_) {
        if (revelationfix$getTrailData().shouldRenderTrail())
            return;
        super.makeParticle(p_36877_);
    }

    @Inject(method = "doPostHurtEffects", at = @At("TAIL"), cancellable = true)
    private void doPostHurtEffects(LivingEntity livingEntity, CallbackInfo ci) {
        if (this.tags.contains(BowOfRevelationItem.FORCE_ADD_EFFECT)) {
            Entity entity = this.getEffectSource();

            for (MobEffectInstance mobeffectinstance : this.potion.getEffects()) {
                BowOfRevelationItem.forceAddEffect(livingEntity, new MobEffectInstance(mobeffectinstance.getEffect(), Math.max(mobeffectinstance.mapDuration((p_268168_) -> {
                    return p_268168_ / 8;
                }), 1), mobeffectinstance.getAmplifier(), mobeffectinstance.isAmbient(), mobeffectinstance.isVisible()), entity);
            }

            if (!this.effects.isEmpty()) {
                for (MobEffectInstance mobeffectinstance1 : this.effects) {
                    BowOfRevelationItem.forceAddEffect(livingEntity, mobeffectinstance1, entity);
                }
            }
        }
    }
}
