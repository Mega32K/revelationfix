package com.mega.revelationfix.mixin.goety;

import com.Polarice3.Goety.common.entities.boss.Apostle;
import com.Polarice3.Goety.common.entities.projectiles.DeathArrow;
import com.mega.endinglib.mixin.accessor.AccessorArrow;
import com.mega.endinglib.util.entity.MobEffectUtils;
import com.mega.revelationfix.client.renderer.trail.TrailRenderTask;
import com.mega.revelationfix.client.renderer.entity.DeathArrowTrailTask;
import com.mega.revelationfix.common.apollyon.client.WrappedTrailData;
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
public abstract  class DeathArrowMixin extends Arrow implements DeathArrowEC {

    @Unique
    public int revelationfix$inGroundTime = 0;
    @Unique
    private WrappedTrailData revelationfix$wrappedTrailData;

    public DeathArrowMixin(EntityType<? extends Arrow> p_36858_, Level p_36859_) {
        super(p_36858_, p_36859_);
    }

    @Inject(method = "<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;)V", at = @At("RETURN"))
    private void init1(EntityType p_36721_, Level p_36722_, CallbackInfo ci) {
        revelationfix$wrappedTrailData = new WrappedTrailData(this);
    }

    @Inject(method = "<init>(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;)V", at = @At("RETURN"))
    private void init2(Level p_36866_, LivingEntity p_36867_, CallbackInfo ci) {
        revelationfix$wrappedTrailData = new WrappedTrailData(this);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        if (this.inGround)
            revelationfix$inGroundTime++;
        if (!level().isClientSide) {

            if (revelationfix$inGroundTime > 20 && getWrappedTrailData().shouldRenderTrail()) {
                revelationfix$wrappedTrailData.setShouldRenderTrail(false);
                this.discard();

            }

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
        revelationfix$wrappedTrailData.remove();
        super.onClientRemoval();
    }

    @Override
    public WrappedTrailData getWrappedTrailData() {
        return revelationfix$wrappedTrailData;
    }

    @Override
    public boolean shouldRenderWrappedTrail() {
        return !this.onGround() || ((DeathArrowEC) this).revelationfix$inGroundTime() < 20;
    }

    @Override
    public TrailRenderTask createTrailRenderTask(WrappedTrailData data) {
        return new DeathArrowTrailTask(data.trailPoints);
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        if (this.getTags().contains(BypassInvulArrow.TAG_BYPASS_NAME))
            return super.getBoundingBoxForCulling().inflate(2d);
        return super.getBoundingBoxForCulling();
    }

    @Override
    public boolean displayFireAnimation() {
        if (getWrappedTrailData().shouldRenderTrail())
            return false;
        return super.displayFireAnimation();
    }

    @Inject(method = "doPostHurtEffects", at = @At("TAIL"))
    private void doPostHurtEffects(LivingEntity livingEntity, CallbackInfo ci) {
        if (this.getTags().contains(BowOfRevelationItem.FORCE_ADD_EFFECT)) {
            Entity entity = this.getEffectSource();
            AccessorArrow accessorArrow = (AccessorArrow) this;
            for (MobEffectInstance mobeffectinstance : accessorArrow.getPotion().getEffects()) {
                MobEffectUtils.forceAdd(livingEntity, new MobEffectInstance(mobeffectinstance.getEffect(), Math.max(mobeffectinstance.mapDuration((p_268168_) -> p_268168_ / 8), 1), mobeffectinstance.getAmplifier(), mobeffectinstance.isAmbient(), mobeffectinstance.isVisible()), entity);
            }

            if (!accessorArrow.getEffects().isEmpty()) {
                for (MobEffectInstance mobeffectinstance1 : accessorArrow.getEffects()) {
                    MobEffectUtils.forceAdd(livingEntity, mobeffectinstance1, entity);
                }
            }
        }
    }

    @Override
    public float getTrailRenderPosDeltaMultiplier() {
        return -0.4F;
    }
}
