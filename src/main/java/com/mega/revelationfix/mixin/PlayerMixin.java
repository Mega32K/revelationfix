package com.mega.revelationfix.mixin;

import com.Polarice3.Goety.common.entities.boss.Apostle;
import com.Polarice3.Goety.utils.NoKnockBackDamageSource;
import com.Polarice3.Goety.utils.OwnedDamageSource;
import com.mega.endinglib.util.entity.armor.ArmorUtils;
import com.mega.revelationfix.client.enums.ModChatFormatting;
import com.mega.revelationfix.common.apollyon.common.PlayerTickrateExecutor;
import com.mega.revelationfix.common.capability.entity.GoetyRevelationPlayerCapability;
import com.mega.revelationfix.common.compat.Wrapped;
import com.mega.revelationfix.common.config.ItemConfig;
import com.mega.revelationfix.common.event.handler.ArmorEvents;
import com.mega.revelationfix.common.item.armor.ModArmorMaterials;
import com.mega.revelationfix.proxy.CommonProxy;
import com.mega.revelationfix.safe.DamageSourceInterface;
import com.mega.revelationfix.safe.OdamanePlayerExpandedContext;
import com.mega.revelationfix.safe.entity.PlayerInterface;
import com.mega.revelationfix.util.entity.CapabilityGetter;
import com.mega.revelationfix.util.entity.EntityActuallyHurt;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import z1gned.goetyrevelation.util.ApollyonAbilityHelper;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements PlayerInterface {
    @Unique
    private boolean revelationfix$isBaseAttributeMode = false;
    @Unique
    private OdamanePlayerExpandedContext revelationfix$context = new OdamanePlayerExpandedContext(((Player) (Object) this));

    public PlayerMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @Shadow
    public abstract boolean isCreative();

    @Shadow
    public abstract boolean isSpectator();

    @Shadow public abstract <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing);

    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    private void readAdditionalSaveData(CompoundTag p_36215_, CallbackInfo ci) {
        revelationfix$context.read(p_36215_);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
    private void addAdditionalSaveData(CompoundTag p_36215_, CallbackInfo ci) {
        revelationfix$context.save(p_36215_);
    }

    @Inject(method = "getDisplayName", at = @At("RETURN"), cancellable = true)
    private void getDisplayName(CallbackInfoReturnable<Component> cir) {
        Player player = (Player) (Object) this;
        if (ItemConfig.apocalyptiumChestplateTitle) {
            if (ArmorUtils.findChestplate(player, ModArmorMaterials.APOCALYPTIUM) && cir.getReturnValue() instanceof MutableComponent component)
                cir.setReturnValue(component.append(ArmorEvents.getTitle(ArmorEvents.getApocalyptiumTitleId(player))));
        }
        if (revelationfix$odamaneHaloExpandedContext().isBlasphemous() && cir.getReturnValue() instanceof MutableComponent component)
            cir.setReturnValue(component.append(Component.translatable("message.goety_revelation.blasphemous_priest_fix").withStyle(ModChatFormatting.APOLLYON)));
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        PlayerTickrateExecutor.playerTick((Player) (Object) this);
        revelationfix$context.tick();
    }

    @Inject(method = "hurtHelmet", at = @At("HEAD"), cancellable = true)
    private void bypassHelmet(DamageSource p_150103_, float p_150104_, CallbackInfo ci) {
        if (((DamageSourceInterface) p_150103_).revelationfix$bypassArmor())
            ci.cancel();
    }

    @Inject(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z", shift = At.Shift.AFTER))
    private void apollyonIncreaseRealDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        Level level = this.level();
        if (level.isClientSide) return;
        Apostle apostle = null;
        if (source.getEntity() instanceof Apostle a) {
            apostle = a;
        }
        if (source.getDirectEntity() instanceof Apostle a) {
            apostle = a;
        }
        if (source instanceof NoKnockBackDamageSource nDs && nDs.getOwner() instanceof Apostle a)
            apostle = a;
        else if (source instanceof OwnedDamageSource oDs && oDs.getOwner() instanceof Apostle a)
            apostle = a;
        if (apostle != null) {
            ApollyonAbilityHelper abilityHelper = (ApollyonAbilityHelper) apostle;
            if (abilityHelper.allTitlesApostle_1_20_1$isApollyon() && apostle.isSecondPhase()) {
                if (apostle.isInNether()) {
                    GameRules gameRules = level.getGameRules();
                    if (!gameRules.getRule(GameRules.RULE_KEEPINVENTORY).get())
                        gameRules.getRule(GameRules.RULE_KEEPINVENTORY).set(true, ((ServerLevel) level).getServer());
                    if (!this.isCreative() && !this.isSpectator()) {
                        if (!source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && invulnerableTime <= 10) {
                            float f1 = amount * 0.01F;
                            float f2 = this.getHealth() * 0.01F;
                            float selectSmallerDamage = Math.min(f1, f2);
                            if (selectSmallerDamage < this.getHealth())
                                new EntityActuallyHurt(this).disableEffects(false).actuallyHurt(source, selectSmallerDamage, false);
                        }
                    }
                }
            }
        }
    }

    @Inject(method = "handleEntityEvent", at = @At("HEAD"), cancellable = true)
    private void handleEntityEvent(byte p_36120_, CallbackInfo ci) {
        if (p_36120_ == OdamanePlayerExpandedContext.ODAMANE_REVIVE_EVENT || p_36120_ == OdamanePlayerExpandedContext.REVIVE_EVENT) {
            ci.cancel();
            Wrapped.handleClientEntityEvent(p_36120_, this);
        }
    }

    @Override
    public boolean revelationfix$isBaseAttributeMode() {
        LazyOptional<GoetyRevelationPlayerCapability> optional = this.getCapability(CommonProxy.getPlayerCap());
        return optional.isPresent() && CapabilityGetter.unsafeGetLazyOptional(optional, this).isDefaultAttributeMode();
    }

    @Override
    public void revelationfix$setBaseAttributeMode(boolean z) {
        CapabilityGetter.unsafeGetCapability(CommonProxy.getPlayerCap(), this).setDefaultAttributeMode(z);
    }

    @Override
    public boolean revelationfix$temp_isBaseAttributeMode() {
        return revelationfix$isBaseAttributeMode;
    }

    @Override
    public void revelationfix$temp_setBaseAttributeMode(boolean z) {
        revelationfix$isBaseAttributeMode = z;
    }

    @Override
    public OdamanePlayerExpandedContext revelationfix$odamaneHaloExpandedContext() {
        if (revelationfix$context == null)
            revelationfix$context = new OdamanePlayerExpandedContext(((Player) (Object) this));
        return revelationfix$context;
    }
}
