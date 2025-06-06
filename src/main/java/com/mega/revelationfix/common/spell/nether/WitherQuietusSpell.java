package com.mega.revelationfix.common.spell.nether;

import com.Polarice3.Goety.api.entities.IOwned;
import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.entities.ModEntityType;
import com.Polarice3.Goety.common.entities.ally.undead.skeleton.WitherSkeletonServant;
import com.Polarice3.Goety.common.entities.util.SummonCircle;
import com.Polarice3.Goety.common.magic.Spell;
import com.Polarice3.Goety.common.magic.SpellStat;
import com.Polarice3.Goety.utils.WandUtil;
import com.mega.revelationfix.common.config.GRSpellConfig;
import com.mega.revelationfix.common.entity.FakeSpellerEntity;
import com.mega.revelationfix.common.entity.misc.QuietusVirtualEntity;
import com.mega.revelationfix.common.init.ModEffects;
import com.mega.revelationfix.common.init.ModSounds;
import com.mega.revelationfix.safe.entity.EntityExpandedContext;
import com.mega.revelationfix.safe.entity.MobEffectInstanceEC;
import com.mega.revelationfix.util.LivingEntityEC;
import com.mega.revelationfix.util.entity.EntityFinder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class WitherQuietusSpell extends Spell {
    public int defaultSoulCost() {
        return GRSpellConfig.WitherQuietusCost.get();
    }

    public int defaultCastDuration() {
        return GRSpellConfig.WitherQuietusDuration.get();
    }

    public SoundEvent CastingSound() {
        return ModSounds.QUIETUS_BEAM.get();
    }

    public int defaultSpellCooldown() {
        return GRSpellConfig.WitherQuietusCoolDown.get();
    }

    public SpellType getSpellType() {
        return SpellType.NETHER;
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = super.acceptedEnchantments();
        list.add(ModEnchantments.RANGE.get());
        return list;
    }

    @Override
    public boolean conditionsMet(ServerLevel worldIn, LivingEntity caster, SpellStat spellStat) {
        int range = 8;
        if (WandUtil.enchantedFocus(caster)) {
            range += WandUtil.getLevels(ModEnchantments.RANGE.get(), caster) * 4;
        }
        LivingEntity target = this.getTarget(caster, range);
        return target != null && target.isAlive() && (!(target instanceof IOwned owned) || owned.getTrueOwner() != caster);
    }

    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat) {
        int range = 14;
        if (WandUtil.enchantedFocus(caster)) {
            range += WandUtil.getLevels(ModEnchantments.RANGE.get(), caster) * 4;
        }
        LivingEntity target = this.getTarget(caster, range);
        if (target != null && target.isAlive() && EntityFinder.STRICT_NOT_ALLIED.test(caster, target)) {
            this.playSound(worldIn, caster, this.CastingSound(), 1.0F, 1.0F);
            QuietusVirtualEntity quietusVirtual = new QuietusVirtualEntity(caster.level, new Vec3(caster.getX(), caster.getY(0.5F), caster.getZ()), new Vec3(target.getX(), target.getY(0.5D), target.getZ()), caster);
            caster.level.addFreshEntity(quietusVirtual);

            target.invulnerableTime = 0;
            target.hurt(caster.damageSources().source(DamageTypes.WITHER, caster), 12.0F);
            target.invulnerableTime = 0;
            target.hurt(caster.damageSources().source(DamageTypes.MAGIC, caster), 8.0F);
            if (!target.level.isClientSide && target.isDeadOrDying() ) {
                Level level = target.level;
                WitherSkeletonServant servant = new WitherSkeletonServant(ModEntityType.WITHER_SKELETON_SERVANT.get(), level);
                servant.setTrueOwner(caster);
                servant.setLimitedLife(5 * 60 * 20);
                servant.finalizeSpawn((ServerLevel) level, level.getCurrentDifficultyAt(target.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                SummonCircle summonCircle = new SummonCircle(level, target.blockPosition(), servant, false, true, caster);
                level.addFreshEntity(summonCircle);
            }
            if (!target.level.isClientSide) {
                int count = target.random.nextInt(18, 25) + (int) Math.max(10, target.getBoundingBox().getSize() * 2F);
                if (target.level instanceof ServerLevel serverLevel)
                    for (ServerPlayer serverplayer : serverLevel.players()) {
                        if (serverplayer.distanceTo(caster) < 64)
                            serverLevel.sendParticles(serverplayer, ParticleTypes.SOUL, false, target.getX(), target.getY(0.5F), target.getZ(), count, 0, 0.02, 0, 0.07F + Math.max(0.05F, target.getBoundingBox().getSize() / 32F));

                    }
            }
            /*
            target.invulnerableTime = 0;
            target.hurt(caster.damageSources().wither(), 20.0F);
            target.invulnerableTime = 30;
            target.hurt(caster.damageSources().magic(), 20.0F);
            */

            EntityExpandedContext entityEC = ((LivingEntityEC) target).revelationfix$livingECData();
            if (target.hasEffect(ModEffects.QUIETUS.get()) || target.activeEffects.containsKey(ModEffects.QUIETUS.get())) {
                MobEffectInstance instance = target.getEffect(ModEffects.QUIETUS.get());
                entityEC.setQuietusCaster(caster);
                if (instance != null) {
                    if (instance.getAmplifier() < 3) {
                        target.removeEffect(ModEffects.QUIETUS.get());
                        int a = instance.getAmplifier();
                        if (!target.addEffect(new MobEffectInstance(instance.getEffect(), 200, a + 1), caster)) {
                            instance = new MobEffectInstance(instance.getEffect(), 200, a + 1);

                            ((MobEffectInstanceEC) instance).setOwnerEntity(caster);

                            target.activeEffects.put(instance.getEffect(), instance);
                            target.onEffectAdded(instance, caster);
                        }
                    } else if (instance.getAmplifier() == 3) {
                        target.removeEffect(ModEffects.QUIETUS.get());
                        int a = instance.getAmplifier();
                        if (!target.addEffect(new MobEffectInstance(instance.getEffect(), 200, 3), caster)) {
                            instance = new MobEffectInstance(instance.getEffect(), 200, 3);

                            ((MobEffectInstanceEC) instance).setOwnerEntity(caster);

                            target.activeEffects.put(instance.getEffect(), instance);
                            target.onEffectAdded(instance, caster);
                        }
                    }
                } else {
                    target.getActiveEffects().clear();
                    target.activeEffects.values().clear();
                    instance = new MobEffectInstance(ModEffects.QUIETUS.get(), 200, 0);

                    ((MobEffectInstanceEC) instance).setOwnerEntity(caster);

                    target.activeEffects.put(instance.getEffect(), instance);
                    target.onEffectAdded(instance, caster);
                }
            } else {
                if (!target.addEffect(new MobEffectInstance(ModEffects.QUIETUS.get(), 200, 0), caster)) {
                    MobEffectInstance instance = new MobEffectInstance(ModEffects.QUIETUS.get(), 200, 0);

                    ((MobEffectInstanceEC) instance).setOwnerEntity(caster);

                    target.activeEffects.put(ModEffects.QUIETUS.get(), instance);
                    target.onEffectAdded(instance, caster);
                }
                entityEC.setQuietusCaster(caster);
            }
        }
    }
}
