package com.mega.revelationfix.common.apollyon.common;

import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.common.entities.boss.Apostle;
import com.Polarice3.Goety.utils.ParticleUtil;
import com.mega.revelationfix.common.apollyon.client.SafePlayPostMusic;
import com.mega.revelationfix.common.config.CommonConfig;
import com.mega.revelationfix.common.config.ModpackCommonConfig;
import com.mega.revelationfix.common.entity.binding.FakeSpellerEntity;
import com.mega.revelationfix.common.network.PacketHandler;
import com.mega.revelationfix.common.network.s2c.PlayApollyonPostThemePacket;
import com.mega.revelationfix.safe.entity.Apollyon2Interface;
import com.mega.revelationfix.safe.entity.ApollyonExpandedContext;
import com.mega.revelationfix.safe.DamageSourceInterface;
import com.mega.revelationfix.safe.entity.EntityExpandedContext;
import com.mega.revelationfix.util.entity.ATAHelper2;
import com.mega.revelationfix.util.entity.EntityActuallyHurt;
import com.mega.revelationfix.util.LivingEntityEC;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.phys.AABB;
import z1gned.goetyrevelation.util.ApollyonAbilityHelper;

public class DeathPerformance {
    public static final int MAX_TIME = 560;
    //in ticks
    private static final RandomSource random = new LegacyRandomSource(666L);
    public static int CHANGE_TIME = 60;
    public static EntityDataAccessor<Integer> LEFTTIME;
    public static EntityDataAccessor<Integer> DEATH_GROWING_TIME;
    public static EntityDataAccessor<Integer> FINAL_DEATH_TIME;
    public static EntityDataAccessor<Byte> FLAGS;

    private static boolean getFlag(Apostle apostle, int mask) {
        int i = apostle.entityData.get(FLAGS);
        return (i & mask) != 0;
    }

    private static void setFlags(Apostle apostle, int mask, boolean value) {
        int i = apostle.entityData.get(FLAGS);
        if (value) {
            i |= mask;
        } else {
            i &= ~mask;
        }

        apostle.entityData.set(FLAGS, (byte) (i & 255));
    }

    public static int getLeftTime(Apostle apostle) {
        if (((ApollyonAbilityHelper) apostle).allTitlesApostle_1_20_1$isApollyon())
            return apostle.entityData.get(LEFTTIME);
        else return 0;

    }

    public static void setRevived(Apostle apostle, boolean z) {
        if (((ApollyonAbilityHelper) apostle).allTitlesApostle_1_20_1$isApollyon())
            setFlags(apostle, 1, z);

    }

    public static boolean isRevived(Apostle apostle) {
        if (((ApollyonAbilityHelper) apostle).allTitlesApostle_1_20_1$isApollyon())
            return getFlag(apostle, 1);
        else return false;

    }

    public static void setBarrierKilled(Apostle apostle, boolean z) {
        if (((ApollyonAbilityHelper) apostle).allTitlesApostle_1_20_1$isApollyon())
            setFlags(apostle, 2, z);

    }

    public static boolean isBarrierKilled(Apostle apostle) {
        if (((ApollyonAbilityHelper) apostle).allTitlesApostle_1_20_1$isApollyon())
            return getFlag(apostle, 2);
        else return false;

    }

    public static void setLeftTime(Apostle apostle, int time) {
        if (((ApollyonAbilityHelper) apostle).allTitlesApostle_1_20_1$isApollyon())
            apostle.entityData.set(LEFTTIME, Mth.clamp(time, -1, MAX_TIME));

    }

    public static int getDeathGrowingTime(Apostle apostle) {
        if (((ApollyonAbilityHelper) apostle).allTitlesApostle_1_20_1$isApollyon())
            return apostle.entityData.get(DEATH_GROWING_TIME);
        else return 0;

    }

    public static void setDeathGrowingTime(Apostle apostle, int time) {
        if (((ApollyonAbilityHelper) apostle).allTitlesApostle_1_20_1$isApollyon())
            apostle.entityData.set(DEATH_GROWING_TIME, time);

    }

    public static int getFinalDeathTime(Apostle apostle) {
        if (((ApollyonAbilityHelper) apostle).allTitlesApostle_1_20_1$isApollyon())
            return apostle.entityData.get(FINAL_DEATH_TIME);
        else return 0;

    }

    public static void setFinalDeathTime(Apostle apostle, int time) {

        if (((ApollyonAbilityHelper) apostle).allTitlesApostle_1_20_1$isApollyon())
            apostle.entityData.set(FINAL_DEATH_TIME, time);

    }

    /**
     * 此方法在{@link LivingEntity#tickDeath()} owner Apostle调用
     * 前提为isDeadOrDying true
     *
     * @param apostle  Apostle
     * @param helper   {@link ApollyonAbilityHelper}
     * @param leftTime from 560 to 0
     */
    public static boolean perform(Apostle apostle, ApollyonAbilityHelper helper, int leftTime) {
        CHANGE_TIME = CommonConfig.apollyonBarrierPreparation * 20;
        synchronized (random) {
            //存储旧growingTime
            EntityExpandedContext entityEC = ((LivingEntityEC) apostle).revelationfix$livingECData();
            Apollyon2Interface apollyon2Interface = (Apollyon2Interface) apostle;
            ApollyonExpandedContext apollyonEC = apollyon2Interface.revelaionfix$apollyonEC();
            entityEC.apollyonLastGrowingTime = getDeathGrowingTime(apostle);
            //刚开始死亡，初始化
            if (!apostle.level.isClientSide) {
                if (leftTime == -1) {
                    setLeftTime(apostle, MAX_TIME);
                    leftTime = MAX_TIME;
                }

                //尾砂期间，deathTime不增加,锁定使徒坐标
                if (leftTime > 0 || entityEC.apollyonLastGrowingTime > 0) {
                    //apostle.deathTime--;
                }
                //尾砂处于开始阶段的时候
                if (leftTime >= (MAX_TIME - CHANGE_TIME)) {
                    setDeathGrowingTime(apostle, getDeathGrowingTime(apostle) + 1);
                    //尾砂剩余时间处于结束阶段并且finalDeathTime还未赋值
                } else if (leftTime <= CHANGE_TIME && getFinalDeathTime(apostle) == -1) {
                    //赋值finalDeathTime
                    setFinalDeathTime(apostle, getDeathGrowingTime(apostle));
                    //重置growingTime,仅作为获取delta数据用
                    if (getDeathGrowingTime(apostle) > 0)
                        setDeathGrowingTime(apostle, 0);

                }
                //结束阶段（结束时刻的growingTime已经赋值给finalDeathTime）
                boolean isEnding = getFinalDeathTime(apostle) != -1;
                //处于结束阶段时,growingTime-- (已经被设置为0,仅用来计算关于finalDeathTime的delta)
                if (isEnding)
                    setDeathGrowingTime(apostle, getDeathGrowingTime(apostle) - 1);
            }
            boolean flag = true;
            //尾砂剩余时间>0

            if (MAX_TIME - leftTime > CHANGE_TIME && leftTime > CHANGE_TIME / 2F) {
                Level level = apostle.level;
                if (!level.isClientSide) {
                    GameRules gameRules = level.getGameRules();
                    if (!gameRules.getRule(GameRules.RULE_KEEPINVENTORY).get())
                        gameRules.getRule(GameRules.RULE_KEEPINVENTORY).set(true, ((ServerLevel) level).getServer());
                }

                for (Entity entity : apostle.level().getEntities(apostle, new AABB(apostle.blockPosition()).inflate(17.0D), EntitySelector.NO_CREATIVE_OR_SPECTATOR)) {
                    if (entity instanceof LivingEntity living && !(living instanceof FakeSpellerEntity)) {
                        double fireScale = living.getBoundingBox().getYsize() / 2D;
                        float percentAmount = living.getMaxHealth() / 17F / 20 / 20 + 0.01F;
                        AttributeInstance knockback = living.getAttribute(Attributes.KNOCKBACK_RESISTANCE);
                        AttributeInstance movement = living.getAttribute(Attributes.MOVEMENT_SPEED);

                        if (living.isAlive()) {

                            double movementSpeed = .1F;
                            double knockbackValue = 0F;
                            if (movement != null) {
                                movementSpeed = movement.getBaseValue();
                                movement.setBaseValue(0D);
                            }
                            if (knockback != null) {
                                knockbackValue = knockback.getBaseValue();
                                knockback.setBaseValue(1D);
                            }
                            RandomSource randomSource = random;
                            ((LivingEntityEC) living).revelationfix$livingECData().banHealingTime = 20;
                            for (DamageSource source : damageSourcesFromForge(apostle)) {
                                //越过盔甲
                                ((DamageSourceInterface) source).revelationfix$setBypassArmor(true);
                                //尾砂，伤害成功的话进行播放音效并重置无敌帧
                                if (randomSource.nextBoolean()) {
                                    if (living.hurt(source, percentAmount)) {
                                        living.playSound(SoundEvents.FIRE_AMBIENT, 2.0F + randomSource.nextFloat(), randomSource.nextFloat() * 0.7F + 0.3F);
                                        living.invulnerableTime = 0;
                                    }
                                } else {
                                    label0:
                                    {
                                        if (ATAHelper2.hasOdamane(living))
                                            break label0;
                                        if (living.isAlive() && living.level.isClientSide) {
                                            ParticleUtil.addParticleInternal(ModParticleTypes.BIG_FIRE.get(), false, living.getRandomX(0.3D * fireScale), living.getY() + randomSource.nextFloat(), living.getRandomZ(0.3D * fireScale), randomSource.triangle(0D, 0.25D * fireScale), randomSource.nextFloat() * 0.1F + 0.25F * fireScale, randomSource.triangle(0D, 0.25D * fireScale));
                                        }
                                        new EntityActuallyHurt(living).actuallyHurt(source, percentAmount, true);
                                        living.playSound(SoundEvents.FIRE_AMBIENT, 2.0F + randomSource.nextFloat(), randomSource.nextFloat() * 0.7F + 0.3F);
                                        living.invulnerableTime = 0;
                                    }
                                }
                            }
                            if (knockback != null)
                                knockback.setBaseValue(knockbackValue);
                            if (movement != null)
                                movement.setBaseValue(movementSpeed);
                            if (living.tickCount > 0 && !ATAHelper2.hasOdamane(living)) {
                                living.setPos(living.xOld, living.yOld, living.zOld);
                                living.setOldPosAndRot();
                                living.setDeltaMovement(0F, 0F, 0F);
                            }
                            flag = false;
                            //结界杀敌回复模式
                            if (!apostle.level.isClientSide) {
                                if (CommonConfig.barrierKillingMobsHealingMode)
                                    if (living.isDeadOrDying()) {
                                        setBarrierKilled(apostle, true);
                                    }
                            }

                        }
                    }
                }
            }
            if (flag && leftTime > 0) {
                if (!apostle.level.isClientSide)
                    setLeftTime(apostle, leftTime - 1);
                if (getLeftTime(apostle) == 0) {
                    AABB aabb = new AABB(apostle.blockPosition).inflate(128D);
                    for (Player player : apostle.level.players()) {
                        if (aabb.contains(player.getX(), player.getY(), player.getZ())) {
                            if (player instanceof ServerPlayer serverPlayer)
                                PacketHandler.sendToPlayer(serverPlayer, new PlayApollyonPostThemePacket(apostle.getUUID()));
                        }
                    }                    //判断周围有生存玩家
                    if ((long) apostle.level.getEntitiesOfClass(Player.class, apostle.getBoundingBox().inflate(512.0D), EntitySelector.NO_CREATIVE_OR_SPECTATOR).size() > 0 && isBarrierKilled(apostle)) {

                        //数据设为初始
                        apostle.deathTime = 0;
                        apollyon2Interface.setDeathTime(0);
                        apostle.hurtTime = 0;
                        apostle.dead = false;
                        if (!apostle.level.isClientSide)
                            setBarrierKilled(apostle, false);
                        boolean revived = isRevived(apostle);
                        //数据设为初始
                        if (!apostle.level.isClientSide) {
                            setLeftTime(apostle, -1);
                            setDeathGrowingTime(apostle, 0);
                            setFinalDeathTime(apostle, -1);
                            setRevived(apostle, true);
                        }
                        entityEC.apollyonLastGrowingTime = 0;

                        float additionPercent = 0.07F;
                        if (ModpackCommonConfig.apollyonModpackMode) {
                            additionPercent = Math.min(additionPercent, (float) ModpackCommonConfig.netherTheDoomPercent);
                        }
                        apostle.setNoGravity(false);
                        //图腾事件
                        if (!apostle.level.isClientSide)
                            apostle.level().broadcastEntityEvent(apostle, (byte) 35);
                        //回血到末日终焉
                        apollyon2Interface.revelaionfix$setApollyonHealth(apollyon2Interface.revelaionfix$getApollyonHealth() + additionPercent * apostle.getMaxHealth());
                        SafePlayPostMusic.playTotem(apostle, revived);
                    }

                }
            }
            return getLeftTime(apostle) != 0 || getFinalDeathTime(apostle) == -1;
        }
    }

    public static DamageSource[] damageSourcesFromForge(LivingEntity source) {
        return new DamageSource[]{
                (source.damageSources().anvil(source)),
                (source.damageSources().source(DamageTypes.CACTUS, source)),
                (source.damageSources().source(DamageTypes.CRAMMING, source)),
                (source.damageSources().source(DamageTypes.DRAGON_BREATH, source)),
                (source.damageSources().source(DamageTypes.DROWN, source)),
                (source.damageSources().source(DamageTypes.DRY_OUT, source)),
                (source.damageSources().explosion(source, source)),
                (source.damageSources().source(DamageTypes.FREEZE, source)),
                (source.damageSources().source(DamageTypes.FALL, source)),
                (source.damageSources().fallingStalactite(source)),
                (source.damageSources().source(DamageTypes.FELL_OUT_OF_WORLD, source)),
                (source.damageSources().source(DamageTypes.FLY_INTO_WALL, source)),
                (source.damageSources().source(DamageTypes.GENERIC, source)),
                (source.damageSources().source(DamageTypes.GENERIC_KILL, source)),
                (source.damageSources().source(DamageTypes.HOT_FLOOR, source)),
                (source.damageSources().source(DamageTypes.IN_FIRE, source)),
                (source.damageSources().source(DamageTypes.IN_WALL, source)),
                (source.damageSources().indirectMagic(source, source)),
                (source.damageSources().source(DamageTypes.LAVA, source)),
                (source.damageSources().source(DamageTypes.LIGHTNING_BOLT, source)),
                (source.damageSources().mobProjectile(source, source)),
                (source.damageSources().source(DamageTypes.MAGIC, source)),
                (source.damageSources().mobAttack(source)),
                (source.damageSources().noAggroMobAttack(source)),
                (source.damageSources().source(DamageTypes.ON_FIRE, source)),
                (source.damageSources().source(DamageTypes.OUTSIDE_BORDER, source)),
                (source.damageSources().source(DamageTypes.STALAGMITE, source)),
                (source.damageSources().source(DamageTypes.STARVE, source)),
                (source.damageSources().sonicBoom(source)),
                (source.damageSources().sting(source)),
                (source.damageSources().source(DamageTypes.SWEET_BERRY_BUSH, source)),
                (source.damageSources().thorns(source)),
                (source.damageSources().trident(source, source)),
                (source.damageSources().source(DamageTypes.WITHER, source))
        };
    }
}
