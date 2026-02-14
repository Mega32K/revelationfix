package com.mega.revelationfix.common.effect;

import com.Polarice3.Goety.api.entities.IOwned;
import com.Polarice3.Goety.common.entities.ModEntityType;
import com.Polarice3.Goety.common.entities.ally.undead.skeleton.WitherSkeletonServant;
import com.Polarice3.Goety.common.entities.boss.Apostle;
import com.Polarice3.Goety.common.entities.util.SummonCircle;
import com.Polarice3.Goety.utils.CuriosFinder;
import com.mega.endinglib.api.entity.MobEffectInstanceItf;
import com.mega.endinglib.mixin.accessor.AccessorAttributeInstance;
import com.mega.endinglib.util.mc.entity.DamageSourceGenerator;
import com.mega.endinglib.util.mc.entity.MobEffectUtils;
import com.mega.revelationfix.common.apollyon.common.ExtraDamageTypes;
import com.mega.revelationfix.common.init.ModEffects;
import com.mega.revelationfix.safe.entity.AttributeInstanceInterface;
import com.mega.revelationfix.safe.entity.EntityExpandedContext;
import com.mega.revelationfix.util.LivingEntityEC;
import com.mega.revelationfix.util.entity.ATAHelper2;
import com.mega.revelationfix.util.entity.EntityActuallyHurt;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import z1gned.goetyrevelation.util.ATAHelper;
import z1gned.goetyrevelation.util.ApollyonAbilityHelper;

import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class QuietusEffect extends MobEffect {
    public QuietusEffect() {
        super(MobEffectCategory.NEUTRAL, 9154528);
    }


    @SubscribeEvent
    public static void onClear(MobEffectEvent.Remove event) {
        if (event.getEffect() instanceof QuietusEffect quietusEffect) {
            AttributeInstance attributeInstance = event.getEntity().getAttribute(Attributes.MAX_HEALTH);
            if (attributeInstance != null) {
                AttributeInstanceInterface i = (AttributeInstanceInterface) attributeInstance;
                if (i.revelationfix$isQuietus()) {
                    i.revelationfix$setQuietus(false);
                    ((AccessorAttributeInstance) attributeInstance).invokeSetDirty();
                    attributeInstance.getValue();
                }
            }
        }
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        if (event.getEntity().hasEffect(ModEffects.QUIETUS.get())) {
            LivingEntity living = event.getEntity();
            if (!living.level().isClientSide && living.isDeadOrDying()) {
                EntityExpandedContext expandedContext = ((LivingEntityEC) living).revelationfix$livingECData();
                Level level = living.level();
                WitherSkeletonServant servant = new WitherSkeletonServant(ModEntityType.WITHER_SKELETON_SERVANT.get(), level);
                servant.setTrueOwner(expandedContext.getQuietusCaster() instanceof IOwned owned ? owned.getTrueOwner() : expandedContext.getQuietusCaster());
                servant.setLimitedLife(5 * 60 * 20);
                servant.finalizeSpawn((ServerLevel) level, level.getCurrentDifficultyAt(living.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                SummonCircle summonCircle = new SummonCircle(level, living.blockPosition(), servant, false, true, expandedContext.getQuietusCaster());
                level.addFreshEntity(summonCircle);
                DamageSource source = living.getLastDamageSource() == null ? expandedContext.getQuietusCaster().damageSources().wither() : living.getLastDamageSource();

            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int tick, int amplifier) {

        if (amplifier <= 1) {
            return true;
        } else if (amplifier == 2) {
            return true;
        } else if (tick % 20 == 0) {
            return true;
        }
        return super.isDurationEffectTick(tick, amplifier);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity living, AttributeMap p_19470_, int p_19471_) {
        if (!living.hasEffect(this)) {
            AttributeInstance attributeInstance = living.getAttribute(Attributes.MAX_HEALTH);
            if (attributeInstance != null) {
                AttributeInstanceInterface i = (AttributeInstanceInterface) attributeInstance;
                if (i.revelationfix$isQuietus()) {
                    i.revelationfix$setQuietus(false);
                    ((AccessorAttributeInstance) attributeInstance).invokeSetDirty();
                    attributeInstance.getValue();
                }
            }
        }
        super.removeAttributeModifiers(living, p_19470_, p_19471_);
    }

    @Override
    public void applyEffectTick(LivingEntity living, int amplifier) {
        if (living instanceof Apostle && ((ApollyonAbilityHelper) living).allTitlesApostle_1_20_1$isApollyon())
            return;
        MobEffectInstance instance = living.getEffect(this);
        Level level = living.level();
        int duration = instance.getDuration();
        if (amplifier == 0 && !level.isClientSide) {
            living.addEffect(new MobEffectInstance(MobEffects.WITHER, duration, 2));
            if (duration == 1) {
                MobEffectUtils.forceAdd(living, new MobEffectInstance(this, 200, 1), ((MobEffectInstanceItf) instance).getOwner());
            }
        }
        if (amplifier == 1) {
            AttributeInstance attributeInstance = living.getAttribute(Attributes.MAX_HEALTH);
            if (attributeInstance != null) {
                AttributeInstanceInterface i = (AttributeInstanceInterface) attributeInstance;
                if (!i.revelationfix$isQuietus()) {
                    i.revelationfix$setQuietus(true);
                    ((AccessorAttributeInstance) attributeInstance).invokeSetDirty();
                    attributeInstance.getValue();
                    living.setHealth(living.getHealth());
                }
            }
            EntityExpandedContext expandedContext = ((LivingEntityEC) living).revelationfix$livingECData();
            if (duration == 1 && expandedContext.getQuietusCaster() != null && CuriosFinder.hasNetherRobe(living)) {
                MobEffectUtils.forceAdd(living, new MobEffectInstance(this, 200, 2), ((MobEffectInstanceItf) instance).getOwner());
            }
        } else {
            AttributeInstance attributeInstance = living.getAttribute(Attributes.MAX_HEALTH);
            if (attributeInstance != null) {
                AttributeInstanceInterface i = (AttributeInstanceInterface) attributeInstance;
                if (i.revelationfix$isQuietus()) {
                    i.revelationfix$setQuietus(false);
                    ((AccessorAttributeInstance) attributeInstance).invokeSetDirty();
                    attributeInstance.getValue();
                }
            }
        }
        if (amplifier == 2) {
            if (duration == 1) {
                if (living.level().isClientSide) {
                    living.level().addParticle(ParticleTypes.SMOKE, living.getX(), living.getY() + 0.5D, living.getZ(), 0.0D, 0.0D, 0.0D);
                } else {
                    EntityActuallyHurt actuallyHurt = new EntityActuallyHurt(living);
                    Entity tryGetOwner = ((MobEffectInstanceItf) instance).getOwner();
                    LivingEntity source = tryGetOwner instanceof LivingEntity l ? l : living;
                    actuallyHurt.actuallyHurt(living.damageSources().explosion(source, source), living.getMaxHealth() * 0.2F);
                    living.level().explode(living, living.getX(), living.getY(0.0625D), living.getZ(), 4.0F, Level.ExplosionInteraction.NONE);

                    EntityExpandedContext expandedContext = ((LivingEntityEC) living).revelationfix$livingECData();
                    boolean isHalo = expandedContext.getQuietusCaster() != null && (ATAHelper.hasHalo(expandedContext.getQuietusCaster()) || ATAHelper2.hasOdamane(expandedContext.getQuietusCaster()));
                    float f2 = 4.0F * 2.0F;
                    int k1 = Mth.floor(living.getX() - (double) f2 - 1.0D);
                    int l1 = Mth.floor(living.getX() + (double) f2 + 1.0D);
                    int i2 = Mth.floor(living.getY(0.0625D) - (double) f2 - 1.0D);
                    int i1 = Mth.floor(living.getY(0.0625D) + (double) f2 + 1.0D);
                    int j2 = Mth.floor(living.getZ() - (double) f2 - 1.0D);
                    int j1 = Mth.floor(living.getZ() + (double) f2 + 1.0D);
                    List<Entity> list = level.getEntities(living, new AABB(k1, i2, j2, l1, i1, j1));
                    MobEffect effect = ModEffects.QUIETUS.get();
                    Vec3 vec3 = new Vec3(living.getX(), living.getY(0.0625D), living.getZ());
                    for (Entity entity : list) {
                        if (entity == living || entity == expandedContext.getQuietusCaster()) continue;
                        if (!entity.ignoreExplosion()) {
                            double d12 = Math.sqrt(entity.distanceToSqr(vec3)) / (double) f2;
                            if (d12 <= 1.0D) {
                                double d5 = entity.getX() - vec3.x;
                                double d7 = (entity instanceof PrimedTnt ? entity.getY() : entity.getEyeY()) - vec3.y;
                                double d9 = entity.getZ() - vec3.z;
                                double d13 = Math.sqrt(d5 * d5 + d7 * d7 + d9 * d9);
                                if (d13 != 0.0D) {
                                    if (isHalo) {
                                        if (entity instanceof LivingEntity livingEntity) {
                                            MobEffectUtils.forceAdd(livingEntity, new MobEffectInstance(this, 200, 2), ((MobEffectInstanceItf) instance).getOwner());
                                            ((LivingEntityEC) livingEntity).revelationfix$livingECData().setQuietusCaster(expandedContext.getQuietusCaster());
                                            new EntityActuallyHurt(livingEntity).actuallyHurt(livingEntity.damageSources().explosion(source, source), livingEntity.getMaxHealth() * 0.2F);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    MobEffectUtils.forceAdd(living, new MobEffectInstance(this, 200, 3), tryGetOwner);
                }
            }
        } else if (amplifier == 3) {
            if (living.isAlive()) {
                living.invulnerableTime = 0;
                LivingEntity source = ((MobEffectInstanceItf) instance).getOwner() instanceof LivingEntity l ? l : living;
                living.hurt(new DamageSourceGenerator(living).source(ExtraDamageTypes.QUIETUS, source), living.getMaxHealth() * 0.04F);
                living.invulnerableTime = 0;
            }
        }
        super.applyEffectTick(living, amplifier);
    }

    public static class QuietusExplosion extends Explosion {
        private final boolean isHalo;

        public QuietusExplosion(Level level, LivingEntity living, double x, double y, double z, float radius, boolean isHalo) {
            super(level, living, null, null, x, y, z, radius, false, BlockInteraction.KEEP);
            this.isHalo = isHalo;
        }

        public void explode() {

            super.explode();
        }
    }
}
