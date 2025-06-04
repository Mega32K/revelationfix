package com.mega.revelationfix.common.effect;

import com.Polarice3.Goety.api.entities.IOwned;
import com.Polarice3.Goety.common.entities.ModEntityType;
import com.Polarice3.Goety.common.entities.ally.undead.skeleton.WitherSkeletonServant;
import com.Polarice3.Goety.common.entities.boss.Apostle;
import com.Polarice3.Goety.common.entities.util.SummonCircle;
import com.Polarice3.Goety.utils.CuriosFinder;
import com.mega.revelationfix.common.apollyon.common.ExtraDamageTypes;
import com.mega.revelationfix.common.init.ModAttributes;
import com.mega.revelationfix.common.init.ModEffects;
import com.mega.revelationfix.safe.entity.AttributeInstanceInterface;
import com.mega.revelationfix.safe.DamageSourceInterface;
import com.mega.revelationfix.safe.entity.EntityExpandedContext;
import com.mega.revelationfix.safe.entity.MobEffectInstanceEC;
import com.mega.revelationfix.util.ATAHelper2;
import com.mega.revelationfix.util.entity.EntityActuallyHurt;
import com.mega.revelationfix.util.LivingEntityEC;
import it.unimi.dsi.fastutil.Pair;
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
import net.minecraft.world.entity.player.Player;
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
                    attributeInstance.setDirty();
                    attributeInstance.getValue();
                }
            }
        }
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        if (event.getEntity().hasEffect(ModEffects.QUIETUS.get())) {
            LivingEntity living = event.getEntity();
            if (!living.level.isClientSide) {
                EntityExpandedContext expandedContext = ((LivingEntityEC) living).revelationfix$livingECData();
                if (!(expandedContext.getQuietusCaster() instanceof Player)) {
                    Level level = living.level;
                    WitherSkeletonServant servant = new WitherSkeletonServant(ModEntityType.WITHER_SKELETON_SERVANT.get(), level);
                    servant.setTrueOwner(expandedContext.getQuietusCaster() instanceof IOwned owned ? owned.getTrueOwner() : expandedContext.getQuietusCaster());
                    servant.setLimitedLife(5 * 60 * 20);
                    servant.finalizeSpawn((ServerLevel) level, level.getCurrentDifficultyAt(living.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                    SummonCircle summonCircle = new SummonCircle(level, living.blockPosition(), servant, false, true, expandedContext.getQuietusCaster());
                    level.addFreshEntity(summonCircle);
                    DamageSource source = living.lastDamageSource == null ? expandedContext.getQuietusCaster().damageSources().wither() : living.lastDamageSource;

                }
            }
        }
    }

    /**
     * @param living        受伤实体
     * @param srcDamage     源伤害
     * @param armoredDamage 收到被盔甲减免后的伤害
     * @param damageSource  (可能为null)从调用方法参数获取到的伤害类型
     * @return 伤害
     */
    public static float quietusArmorAbility(LivingEntity living, float srcDamage, float armoredDamage, DamageSource damageSource) {
        if (damageSource != null && damageSource.is(ExtraDamageTypes.FE_POWER) && damageSource.getEntity() != living)
            return Math.max(srcDamage, armoredDamage);
        if (living.hasEffect(ModEffects.QUIETUS.get())) {
            if (armoredDamage < srcDamage) {
                armoredDamage = armoredDamage + (srcDamage - armoredDamage) * (living.getEffect(ModEffects.QUIETUS.get()).getAmplifier() + 1) * 0.1F;
            }
        }
        if (damageSource != null && damageSource.getEntity() instanceof Player player) {
            if (armoredDamage < srcDamage) {
                armoredDamage = armoredDamage + (srcDamage - armoredDamage) * (float) (Mth.clamp(player.getAttributeValue(ModAttributes.ARMOR_PENETRATION.get()), 1F, 2F) - 1.0F);
            }
        }
        return armoredDamage;
    }

    /**
     * @param living          受伤实体
     * @param srcDamage       源伤害
     * @param enchantedDamage 收到被盔甲减免后的伤害
     * @param damageSource    (可能为null)从调用方法参数获取到的伤害类型
     * @param pair            (可能为null)从调用方法参数获取到的伤害类型和伤害(在抗性效果之前)(actuallyHurt -> getDamageAfterMagicAbsorb)
     * @return 伤害
     */
    public static float quietusEnchantmentAbility(LivingEntity living, float srcDamage, float enchantedDamage, DamageSource damageSource, Pair<DamageSource, Float> pair) {
        if (damageSource != null && damageSource.is(ExtraDamageTypes.FE_POWER) && damageSource.getEntity() != living) {
            if (pair != null && pair.first().equals(damageSource))
                return Math.max(Math.max(srcDamage, enchantedDamage), pair.right());
            return Math.max(srcDamage, enchantedDamage);
        }
        if (living.hasEffect(ModEffects.QUIETUS.get())) {
            if (pair != null && pair.first().equals(damageSource))
                if (pair.right() > srcDamage)
                    srcDamage = pair.right();
            if (enchantedDamage < srcDamage) {
                enchantedDamage = enchantedDamage + (srcDamage - enchantedDamage) * (living.getEffect(ModEffects.QUIETUS.get()).getAmplifier() + 1) * 0.1F;
            }
        }

        if (damageSource != null) {
            if (damageSource.getEntity() instanceof Player player) {
                if (enchantedDamage < srcDamage) {
                    enchantedDamage = enchantedDamage + (srcDamage - enchantedDamage) * (float) (Mth.clamp(player.getAttributeValue(ModAttributes.ENCHANTMENT_PIERCING.get()), 1F, 2F) - 1.0F);
                }
            }
            DamageSourceInterface dsi = (DamageSourceInterface) damageSource;

            if (dsi.hasTag((byte) 5)) {
                if (enchantedDamage < srcDamage) {
                    enchantedDamage = enchantedDamage + (srcDamage - enchantedDamage) * (1 - 0.6F);
                }
            } else if (dsi.hasTag((byte) 4)) {
                if (enchantedDamage < srcDamage) {
                    enchantedDamage = srcDamage;
                }
            }
        }
        return enchantedDamage;
    }

    public static float quietusHealingAbility(LivingEntity living, float srcAmount) {
        if (living.hasEffect(ModEffects.QUIETUS.get())) {
            srcAmount *= (1.0F - (living.getEffect(ModEffects.QUIETUS.get()).getAmplifier() + 1) * 0.1F);
        }
        return srcAmount;
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
                    attributeInstance.setDirty();
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
        Level level = living.level;
        int duration = instance.getDuration();
        if (amplifier == 0 && !living.level.isClientSide) {
            living.addEffect(new MobEffectInstance(MobEffects.WITHER, duration, 2));
            if (duration < 2) {
                Entity tryGetOwner = ((MobEffectInstanceEC) instance).getOwner();
                living.removeEffect(this);
                if (!living.addEffect(instance = new MobEffectInstance(this, 200, 1), tryGetOwner)) {
                    living.activeEffects.put(instance.getEffect(), instance);
                    living.onEffectAdded(instance, tryGetOwner);
                }
            }
        }
        if (amplifier == 1) {
            AttributeInstance attributeInstance = living.getAttribute(Attributes.MAX_HEALTH);
            if (attributeInstance != null) {
                AttributeInstanceInterface i = (AttributeInstanceInterface) attributeInstance;
                if (!i.revelationfix$isQuietus()) {
                    i.revelationfix$setQuietus(true);
                    attributeInstance.setDirty();
                    attributeInstance.getValue();
                    living.setHealth(living.getHealth());
                }
            }
            EntityExpandedContext expandedContext = ((LivingEntityEC) living).revelationfix$livingECData();
            if (duration < 2 && expandedContext.getQuietusCaster() != null && CuriosFinder.hasNetherRobe(living)) {
                Entity tryGetOwner = ((MobEffectInstanceEC) instance).getOwner();
                living.removeEffect(this);
                if (!living.addEffect(instance = new MobEffectInstance(this, 200, 2), tryGetOwner)) {
                    living.activeEffects.put(instance.getEffect(), instance);
                    living.onEffectAdded(instance, tryGetOwner);
                }
            }
        } else {
            AttributeInstance attributeInstance = living.getAttribute(Attributes.MAX_HEALTH);
            if (attributeInstance != null) {
                AttributeInstanceInterface i = (AttributeInstanceInterface) attributeInstance;
                if (i.revelationfix$isQuietus()) {
                    i.revelationfix$setQuietus(false);
                    attributeInstance.setDirty();
                    attributeInstance.getValue();
                }
            }
        }
        if (amplifier == 2) {
            if (duration < 2) {
                if (living.level().isClientSide) {
                    living.level().addParticle(ParticleTypes.SMOKE, living.getX(), living.getY() + 0.5D, living.getZ(), 0.0D, 0.0D, 0.0D);
                } else {
                    EntityActuallyHurt actuallyHurt = new EntityActuallyHurt(living);
                    Entity tryGetOwner = ((MobEffectInstanceEC) instance).getOwner();
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
                                            if (!livingEntity.hasEffect(effect) || livingEntity.getEffect(effect).getAmplifier() < 2) {
                                                MobEffectInstance instance2 = new MobEffectInstance(effect, 200, 2);
                                                if (!livingEntity.addEffect(instance2, tryGetOwner)) {
                                                    livingEntity.activeEffects.put(effect, instance2);
                                                    livingEntity.onEffectAdded(instance2, tryGetOwner);
                                                }
                                                ((LivingEntityEC) livingEntity).revelationfix$livingECData().setQuietusCaster(expandedContext.getQuietusCaster());
                                                new EntityActuallyHurt(livingEntity).actuallyHurt(livingEntity.damageSources().explosion(source, source), livingEntity.getMaxHealth() * 0.2F);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    living.removeEffect(this);
                    if (!living.addEffect(instance = new MobEffectInstance(this, 200, 3), tryGetOwner)) {
                        living.activeEffects.put(instance.getEffect(), instance);
                        living.onEffectAdded(instance, tryGetOwner);
                    }
                }
            }
        } else if (amplifier == 3) {
            if (living.isAlive()) {
                living.invulnerableTime = 0;
                LivingEntity source = ((MobEffectInstanceEC) instance).getOwner() instanceof LivingEntity l ? l : living;
                living.hurt(living.damageSources().source(ExtraDamageTypes.QUIETUS, source), living.getMaxHealth() * 0.04F);
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
