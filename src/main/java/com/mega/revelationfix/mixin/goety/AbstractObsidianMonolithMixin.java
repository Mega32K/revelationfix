package com.mega.revelationfix.mixin.goety;

import com.Polarice3.Goety.common.entities.ModEntityType;
import com.Polarice3.Goety.common.entities.ally.Summoned;
import com.Polarice3.Goety.common.entities.ally.spider.SpiderServant;
import com.Polarice3.Goety.common.entities.ally.undead.skeleton.SkeletonServant;
import com.Polarice3.Goety.common.entities.ally.undead.zombie.HuskServant;
import com.Polarice3.Goety.common.entities.boss.Apostle;
import com.Polarice3.Goety.common.entities.hostile.servants.ObsidianMonolith;
import com.Polarice3.Goety.common.entities.neutral.AbstractMonolith;
import com.Polarice3.Goety.common.entities.neutral.AbstractObsidianMonolith;
import com.Polarice3.Goety.common.entities.neutral.Owned;
import com.Polarice3.Goety.common.entities.neutral.ZPiglinServant;
import com.Polarice3.Goety.common.entities.util.SummonCircle;
import com.Polarice3.Goety.common.items.ModItems;
import com.Polarice3.Goety.utils.BlockFinder;
import com.Polarice3.Goety.utils.MobUtil;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mega.revelationfix.common.entity.binding.FakeSpellerEntity;
import com.mega.revelationfix.common.entity.cultists.HereticServant;
import com.mega.revelationfix.common.init.ModEntities;
import com.mega.revelationfix.safe.OdamanePlayerExpandedContext;
import com.mega.revelationfix.util.entity.ATAHelper2;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import z1gned.goetyrevelation.entitiy.PhantomServant;
import z1gned.goetyrevelation.entitiy.WitherServant;
import z1gned.goetyrevelation.util.ATAHelper;
import z1gned.goetyrevelation.util.ApollyonAbilityHelper;
import z1gned.goetyrevelation.util.PlayerAbilityHelper;

/**
 * 2.5.46.1版本的巨石兼容
 */
@Mixin(AbstractObsidianMonolith.class)
public abstract class AbstractObsidianMonolithMixin extends AbstractMonolith {
    @Shadow public abstract void silentDie(DamageSource cause);

    @Unique
    private int allTitleApostle$deadTick = 20 * 45;
    @Unique
    private int playerOwnTick = 20 * 45;

    AbstractObsidianMonolithMixin(EntityType<? extends Owned> type, Level worldIn) {
        super(type, worldIn);
    }


    @Inject(method = "aiStep", at = @At("HEAD"))
    private void aiStep(CallbackInfo ci) {
        if ((Object)this instanceof ObsidianMonolith obsidianMonolith) {
            if (!level().isClientSide) {
                if (this.getTrueOwner() instanceof Player player) {
                    OdamanePlayerExpandedContext expandedContext = ATAHelper2.getOdamaneEC(player);
                    if (!expandedContext.getOwnedMonoliths().contains((ObsidianMonolith) (Object) this)) {
                        expandedContext.getOwnedMonoliths().add((ObsidianMonolith) (Object) this);
                    }
                }
            }
        }
    }
    @Inject(at = @At("RETURN"), method = "aiStep")
    private void spawnWither(CallbackInfo ci) {
        if ((Object)this instanceof ObsidianMonolith) {
            if (!this.isEmerging()) {
                if (!this.level().isClientSide) {
                    if (this.getTrueOwner() instanceof Apostle apostle && ((ApollyonAbilityHelper) apostle).allTitlesApostle_1_20_1$isApollyon()) {
                        if (apostle.isAlive() && apostle.isSecondPhase()) {
                            --this.allTitleApostle$deadTick;
                            if (this.allTitleApostle$deadTick == 0) {
                                Level level = this.level();
                                LivingEntity target = null;
                                if (apostle.getTarget() != null) {
                                    target = apostle.getTarget();
                                }

                                WitherServant witherBoss = new WitherServant(z1gned.goetyrevelation.entitiy.ModEntityType.WITHER_SERVANT.get(), this.level());
                                witherBoss.setTrueOwner(apostle);
                                witherBoss.setLimitedLife(60 * (90 + level.random.nextInt(180)));
                                witherBoss.finalizeSpawn((ServerLevel) level, level.getCurrentDifficultyAt(this.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                                witherBoss.setTarget(target);

                                SummonCircle summonCircle = new SummonCircle(level, this.blockPosition(), witherBoss, false, true, apostle);
                                level.addFreshEntity(summonCircle);
                                this.allTitleApostle$deadTick = 20 * 45;
                                this.silentDie(this.damageSources().starve());
                            }
                        }
                    }

                    if (this.getTrueOwner() instanceof ServerPlayer) {
                        if (--this.playerOwnTick == 0) {
                            this.silentDie(this.damageSources().starve());
                        }
                    }
                }
            }

            if (this.getTrueOwner() instanceof Apostle apostle) {
                if (((ApollyonAbilityHelper) apostle).allTitlesApostle_1_20_1$isApollyon() && apostle.isInNether()) {
                    this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20 * 9999, 1));
                }
            }
        }
    }
    @Inject(at = @At("RETURN"), method = "isCurrentlyGlowing", cancellable = true)
    private void setGlowing(CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue())
            cir.setReturnValue(!(this.getTrueOwner() instanceof Player) && cir.getReturnValueZ());
    }

    @ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z", ordinal = 0), method = "aiStep")
    private Entity spawnServant(Entity par1) {
        if (this.getTrueOwner() instanceof Apostle apostle && ((ApollyonAbilityHelper) apostle).allTitlesApostle_1_20_1$isApollyon()) {
            return this.allTitleApostle$spawn(this.level());
        }

        return par1;
    }

    @ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lcom/Polarice3/Goety/common/entities/neutral/AbstractObsidianMonolith;getTrueOwner()Lnet/minecraft/world/entity/LivingEntity;", remap = false), method = "aiStep")
    private LivingEntity getPlayerOwner(LivingEntity original) {
        LivingEntity living = this.getTrueOwner();
        if (living instanceof FakeSpellerEntity spellerEntity)
            living = spellerEntity.getOwner();
        if (living instanceof Player player) {
            //MODIFIED
            if (ATAHelper.hasHalo(player) || ATAHelper2.hasOdamane(player)) {
                ((PlayerAbilityHelper) player).setInvulTick(10);
            }

            if (player.tickCount % 25 == 0) {
                player.heal(0.8F);
            }
        }

        return living;
    }

    @ModifyVariable(at = @At("STORE"), method = "aiStep", index = 1)
    private int setTime(int value) {
        if (this.getTrueOwner() instanceof Apostle apostle) {
            if (((ApollyonAbilityHelper) apostle).allTitlesApostle_1_20_1$isApollyon() && apostle.isInNether()) {
                value /= 2;
            }
        }

        return value;
    }
    @Unique
    private SummonCircle allTitleApostle$spawn(Level level) {
        int i = -1;
        int random = this.level().random.nextInt(12);
        Apostle apostle = (Apostle) this.getTrueOwner();
        Summoned summoned = new ZPiglinServant(ModEntityType.ZPIGLIN_SERVANT.get(), level);
        LivingEntity target = null;

        if (apostle != null) {
            if (apostle.getTarget() != null) {
                target = apostle.getTarget();
            }

            i = ((ApollyonAbilityHelper) apostle).allTitleApostle$getTitleNumber();
            int s = i == 12 ? random : i;
            if (apostle.isSecondPhase()) {
                boolean bl = s == 10 || s == 5 || s == 8;
                if (s == 8) {
                    ZPiglinServant zPiglinServant = new ZPiglinServant(ModEntityType.ZPIGLIN_SERVANT.get(), level);
                    zPiglinServant.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.FROZEN_BLADE.get()));
                    summoned = zPiglinServant;
                }
                if (bl) {
                    if (this.level().random.nextFloat() > 0.25F) {
                        summoned = new ZPiglinServant(ModEntityType.ZPIGLIN_BRUTE_SERVANT.get(), level);
                    }
                } else if (s == 1) {
                    summoned = new ZPiglinServant(ModEntityType.ZPIGLIN_BRUTE_SERVANT.get(), level);
                } else if (s == 6 && this.random.nextBoolean()) {
                    //MODIFIED
                    HereticServant servant = new HereticServant(ModEntities.HERETIC_SERVANT.get(), level);
                    BlockPos blockPos = BlockFinder.SummonRadius(this.blockPosition(), servant, level);
                    servant.moveTo(blockPos, 0.0F, 0.0F);
                    servant.setTrueOwner(apostle);
                    servant.setLimitedLife(MobUtil.getSummonLifespan(level));
                    servant.finalizeSpawn((ServerLevel) level, level.getCurrentDifficultyAt(blockPos), MobSpawnType.MOB_SUMMONED, null, null);
                    servant.setTarget(apostle.getTarget());
                    servant.setApollyonSummoned(true);
                    SummonCircle summonCircle = new SummonCircle(level, blockPos, servant, false, true, apostle);
                    return summonCircle;
                }
            } else {
                if (s == 7) {
                    summoned = new HuskServant(ModEntityType.HUSK_SERVANT.get(), level);
                } else if (s == 2) {
                    //MODIFIED
                    SpiderServant spiderServant = new SpiderServant(ModEntityType.SPIDER_SERVANT.get(), level);
                    BlockPos blockPos = BlockFinder.SummonRadius(this.blockPosition(), spiderServant, level);
                    spiderServant.moveTo(blockPos, 0.0F, 0.0F);
                    spiderServant.setTrueOwner(apostle);
                    spiderServant.setLimitedLife(MobUtil.getSummonLifespan(level));
                    spiderServant.finalizeSpawn((ServerLevel) level, level.getCurrentDifficultyAt(blockPos), MobSpawnType.MOB_SUMMONED, null, null);
                    spiderServant.setTarget(target);
                    SummonCircle summonCircle = new SummonCircle(level, blockPos, spiderServant, false, true, apostle);
                    return summonCircle;
                } else if (s == 3) {
                    summoned = new SkeletonServant(ModEntityType.WITHER_SKELETON_SERVANT.get(), level);
                    summoned.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
                }
            }
        }

        BlockPos blockPos = BlockFinder.SummonRadius(this.blockPosition(), summoned, level);
        summoned.moveTo(blockPos, 0.0F, 0.0F);
        summoned.setTrueOwner(apostle);
        summoned.setLimitedLife(MobUtil.getSummonLifespan(level));
        ((Owned) summoned).finalizeSpawn((ServerLevel) level, level.getCurrentDifficultyAt(blockPos), MobSpawnType.MOB_SUMMONED, null, null);
        summoned.setTarget(target);

        PhantomServant phantomServant = new PhantomServant(z1gned.goetyrevelation.entitiy.ModEntityType.PHANTOM_SERVANT.get(), level);
        phantomServant.setTrueOwner(apostle);
        phantomServant.setLimitedLife(60 * (90 + level.random.nextInt(180)));
        phantomServant.finalizeSpawn((ServerLevel) level, level.getCurrentDifficultyAt(blockPos), MobSpawnType.MOB_SUMMONED, null, null);
        phantomServant.setTarget(target);

        int s = i == 12 ? random : i;
        SummonCircle summonCircle = new SummonCircle(level, blockPos, summoned, false, true, apostle);
        if (s == 4) {
            summonCircle = new SummonCircle(level, blockPos, phantomServant, false, true, apostle);
        }

        return summonCircle;
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        if (this.getTrueOwner() instanceof Player) return false;
        return super.shouldDespawnInPeaceful();
    }
}
