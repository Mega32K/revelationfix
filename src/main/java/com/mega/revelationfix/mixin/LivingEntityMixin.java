package com.mega.revelationfix.mixin;

import com.Polarice3.Goety.common.entities.boss.Apostle;
import com.Polarice3.Goety.config.MainConfig;
import com.Polarice3.Goety.init.ModTags;
import com.mega.revelationfix.common.config.CommonConfig;
import com.mega.revelationfix.safe.entity.EntityExpandedContext;
import com.mega.revelationfix.util.EventUtil;
import com.mega.revelationfix.util.LivingEntityEC;
import com.mega.revelationfix.util.entity.ATAHelper2;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import z1gned.goetyrevelation.util.ATAHelper;

import java.util.Map;
import java.util.Set;

@Mixin(value = LivingEntity.class, priority = 900)
public abstract class LivingEntityMixin extends Entity implements LivingEntityEC {
    @Shadow
    private boolean effectsDirty;
    @Shadow
    private Map<MobEffect, MobEffectInstance> activeEffects;
    @Unique
    private EntityExpandedContext revelationfix$entityEC;

    public LivingEntityMixin(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Shadow
    public abstract AttributeMap getAttributes();

    @Shadow
    public abstract MobType getMobType();

    @Shadow
    public abstract float getMaxHealth();

    @Shadow
    protected abstract void onEffectRemoved(MobEffectInstance p_21126_);

    @Shadow
    protected abstract void onEffectAdded(MobEffectInstance p_147190_, @Nullable Entity p_147191_);

    @Shadow
    public abstract void remove(RemovalReason p_276115_);

    @Shadow
    public abstract float getHealth();

    @Inject(method = "heal", at = @At("HEAD"), cancellable = true)
    private void heal(float p_21116_, CallbackInfo ci) {
        if ((Object) this instanceof Player player)
            ATAHelper2.getOdamaneEC(player).heal(p_21116_, ci);
    }

    @Inject(method = "handleDamageEvent", at = @At("HEAD"), cancellable = true)
    private void handleDamageEvent(DamageSource p_270229_, CallbackInfo ci) {
        if ((Object) this instanceof Player player)
            ATAHelper2.getOdamaneEC(player).handleDamageEvent(p_270229_, ci);
    }

    /*
    @Inject(method = "canBeAffected", at = @At("HEAD"), cancellable = true)
    private void canBeAffected(MobEffectInstance effectInstance, CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof Player player) {
            if (!ATAHelper2.hasOdamane(player) && ATAHelper.hasHalo(player)) {
                if (CommonConfig.inBypassEffect(effectInstance.getEffect()))
                    return;
                boolean apo = (ItemHelper.armorSet(player, ModArmorMaterials.APOCALYPTIUM));
                if (effectInstance.getEffect() == MobEffects.NIGHT_VISION || effectInstance.getEffect() == MobEffects.BAD_OMEN)
                    return;
                if (apo && effectInstance.getEffect().getCategory() != MobEffectCategory.HARMFUL) return;
                cir.setReturnValue(false);
            }
        }
    }
     */
    @Inject(
            method = {"canAttack(Lnet/minecraft/world/entity/LivingEntity;)Z"},
            at = {@At("HEAD")},
            cancellable = true
    )
    public void canAttack(LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
        //(this.getMobType() == MobType.UNDEAD || this.getType().is(ModTags.EntityTypes.LICH_NEUTRAL))
        Set<String> tags = this.getTags();
        Set<String> targetTags = target.getTags();
        if ((tags.contains(EntityExpandedContext.GR_MAY_FRIENDLY_TAG) && targetTags.contains(EntityExpandedContext.GR_MAY_FRIENDLY_TAG))) {
            if (tags.contains(EntityExpandedContext.GR_FT_CHURCH) && targetTags.contains(EntityExpandedContext.GR_FT_CHURCH)) {
                cir.setReturnValue(false);
            }
        } else if (EntityExpandedContext.isOwnerFriendlyTag(this) && EntityExpandedContext.isOwnerFriendlyTag(target)) {
            if (EntityExpandedContext.isOwnerFriendlyTag_Church(this) && EntityExpandedContext.isOwnerFriendlyTag_Church(target)) {
                cir.setReturnValue(false);
            }
        }
        if (target instanceof Player player && ATAHelper2.hasOdamane(player)) {
            if (!this.getType().is(Tags.EntityTypes.BOSSES)) cir.setReturnValue(false);
        }
        if (ATAHelper.hasHalo(target) && (this.getMobType() == MobType.UNDEAD || this.getType().is(ModTags.EntityTypes.LICH_NEUTRAL))) {
            if (CommonConfig.haloUndeadSuppression) {
                if (!((Object) this instanceof Apostle))
                    cir.setReturnValue(false);
            } else {
                if ((double) this.getMaxHealth() <= MainConfig.LichPowerfulFoesHealth.get()) {
                    cir.setReturnValue(false);
                }
            }
        }

    }

    @ModifyVariable(method = "hurt", at = @At("HEAD"), argsOnly = true)
    private float apollyonIncreaseDamage(float f, DamageSource source, float amount) {
        return EventUtil.damageIncrease((LivingEntity) (Entity) this, source, amount);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    private void readAdditionalSaveData(CompoundTag p_21096_, CallbackInfo ci) {
        this.revelationfix$livingECData().read(p_21096_);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
    private void addAdditionalSaveData(CompoundTag p_21096_, CallbackInfo ci) {
        this.revelationfix$livingECData().save(p_21096_);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        revelationfix$livingECData().tick();
    }

    @Override
    public EntityExpandedContext revelationfix$livingECData() {
        if (revelationfix$entityEC == null) {
            revelationfix$setECData(new EntityExpandedContext((LivingEntity) (Object) this));
        }
        return revelationfix$entityEC;
    }

    @Override
    public void revelationfix$setECData(EntityExpandedContext data) {
        this.revelationfix$entityEC = data;
    }
}
