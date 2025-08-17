package com.mega.revelationfix.util.entity;

import com.mega.endinglib.mixin.accessor.AccessorEntity;
import com.mega.endinglib.mixin.accessor.AccessorLivingEntity;
import com.mega.revelationfix.common.compat.SafeClass;
import com.mega.revelationfix.mixin.SyncEntityDataAccessor;
import com.mega.revelationfix.safe.entity.EntityExpandedContext;
import com.mega.revelationfix.util.ClassHandler;
import com.mega.revelationfix.util.MCMapping;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import org.apache.commons.lang3.ObjectUtils;

import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * @author MegaDarkness
 */
public class EntityActuallyHurt {
    /**
     * String className
     * Integer indexOfDataItem
     */
    public static final Class<?> HEAD = LivingEntity.class;
    public static final HashMap<String, IndexAndType> entityHealthDatas = new HashMap<>();
    public static final IndexAndType NULL_DATA = new IndexAndType(-1, false);
    //Vanilla
    public static final Method getMaxHealth;
    public static Predicate<Field> isHealthField;

    static {
        try {
            getMaxHealth = LivingEntity.class.getDeclaredMethod(MCMapping.LivingEntity$METHOD$getMaxHealth.get());
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        isHealthField = (field -> containsIgnoreCase(field.getName(), "HEALTH")
                && (!containsIgnoreCase(field.getName(), "MAX_HEALTH"))
                && (field.getType().isAssignableFrom(EntityDataAccessor.class))
                && (Modifier.isStatic(field.getModifiers())));
    }

    public final LivingEntity entity;
    public final AccessorLivingEntity accessorLivingEntity;
    public final AccessorEntity accessorEntity;
    private boolean disableEffects;

    public EntityActuallyHurt(LivingEntity entity) {
        this.entity = entity;
        accessorLivingEntity = (AccessorLivingEntity) entity;
        accessorEntity = (AccessorEntity) entity;
    }

    public static boolean containsIgnoreCase(String str, String searchStr) {
        return str.toUpperCase().contains(searchStr.toUpperCase());
    }

    public static void checkAndSave(LivingEntity living) {
        try {
            checkAndSave0(living);
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private static void checkAndSave0(LivingEntity living) {
        SynchedEntityData eD = living.getEntityData();
        SyncEntityDataAccessor accessor = (SyncEntityDataAccessor) eD;
        Class<? extends LivingEntity> klass = living.getClass();
        synchronized (entityHealthDatas) {
            if (!entityHealthDatas.containsKey(klass.getName()))
                if (living instanceof Player || klass.getName().startsWith("net.minecraft.")) {
                    entityHealthDatas.put(klass.getName(), new IndexAndType(LivingEntity.DATA_HEALTH_ID.getId(), true));
                }
            if (!entityHealthDatas.containsKey(klass.getName()))
                accessor.itemsById().forEach((integer, dataItem) -> {
                    synchronized (living) {
                        List<ClassHandler.FieldVarHandle> fields;
                        try {
                            fields = ClassHandler.bigFilter_allSuper(living.getClass(), HEAD, isHealthField, false, false);
                        } catch (Throwable e) {
                            throw new RuntimeException(e);
                        }
                        VarHandle handle = fields.size() > 1 ? fields.get(fields.size() - 1).varHandle() : fields.size() == 1 ? fields.get(0).varHandle() : null;
                        if (handle != null) {
                            //if (!hasVanillaGetHealth(klass.getDeclaredMethods()) && hasSelfGetHealth(klass.getDeclaredMethods()).size() > 0) {
                            //    EntityASMUtil.cantUseASMGetHealthAndHasSelfMethodClasses.add(klass);
                            //}
                            EntityDataAccessor<?> data = (EntityDataAccessor<?>) handle.get();
                            if (getItem((SyncEntityDataAccessor) living.getEntityData(), data).getValue() instanceof Float) {
                                accessor.itemsById().forEach((index, dataItem0) -> {
                                    if (dataItem0.getAccessor() == data) {
                                        entityHealthDatas.put(klass.getName(), new IndexAndType(index, true));
                                    }
                                });
                                return;
                            } else if (getItem((SyncEntityDataAccessor) living.getEntityData(), data).getValue() instanceof Double) {
                                accessor.itemsById().forEach((index, dataItem0) -> {
                                    if (dataItem0.getAccessor() == data) {
                                        entityHealthDatas.put(klass.getName(), new IndexAndType(index, false));
                                    }
                                });
                                return;
                            }

                        } else {
                            try {
                                if (dataItem.getValue() instanceof Float) {
                                    float dataHealth = getValue(living, LivingEntity.DATA_HEALTH_ID);
                                    if (Objects.equals(dataHealth, living.getHealth())) {
                                        float health = dataHealth;
                                        living.getEntityData().set(LivingEntity.DATA_HEALTH_ID, health - 0.1F);
                                        dataHealth = getValue(living, LivingEntity.DATA_HEALTH_ID);
                                        if (Objects.equals(dataHealth, living.getHealth())) {
                                            entityHealthDatas.put(klass.getName(), new IndexAndType(LivingEntity.DATA_HEALTH_ID.getId(), true));
                                        }
                                    }
                                    if (!entityHealthDatas.containsKey(klass.getName())) {
                                        boolean flag1 = Objects.equals(living.getHealth(), (getItem(accessor, dataItem.getAccessor()).getValue()));
                                        living.setHealth(living.getHealth() + .001F);
                                        boolean flag2 = Objects.equals(living.getHealth(), dataItem.getValue());
                                        living.setHealth(living.getHealth() - .001F);
                                        if (flag1 && flag2) {
                                            entityHealthDatas.put(klass.getName(), new IndexAndType(integer, dataItem.getValue() instanceof Float));
                                        }
                                    }
                                } else if (dataItem.getValue() instanceof Double) {
                                    boolean flag1 = Objects.equals((double) living.getHealth(), dataItem.getValue());
                                    living.setHealth(living.getHealth() + .001F);
                                    boolean flag2 = Objects.equals((double) living.getHealth(), dataItem.getValue());
                                    living.setHealth(living.getHealth() - .001F);
                                    if (flag1 && flag2) {
                                        entityHealthDatas.put(klass.getName(), new IndexAndType(integer, dataItem.getValue() instanceof Float));
                                    }
                                }
                            } catch (Throwable throwable) {
                                if (!entityHealthDatas.containsKey(klass.getName())) {
                                    entityHealthDatas.put(klass.getName(), new IndexAndType(LivingEntity.DATA_HEALTH_ID.getId(), true));
                                    return;
                                }
                            }
                        }
                    }
                    if (!entityHealthDatas.containsKey(klass.getName())) {
                        accessor.itemsById().forEach((index, dataItem0) -> {
                            if (dataItem0.getAccessor() == LivingEntity.DATA_HEALTH_ID)
                                entityHealthDatas.put(klass.getName(), new IndexAndType(index, true));
                        });
                    }
                });
        }
    }

    /**
     * 存储类里最大血量数据的字段和自定义血量字段
     */
    private static void checkAndSave0_class(Class<? extends LivingEntity> clazz) throws Throwable {
        if (!entityHealthDatas.containsKey(clazz.getName()))
            if (clazz.isAssignableFrom(Player.class) || clazz.getName().startsWith("net.minecraft.")) {
                entityHealthDatas.put(clazz.getName(), new IndexAndType(LivingEntity.DATA_HEALTH_ID.getId(), true));
            }
        if (!entityHealthDatas.containsKey(clazz.getName())) {

            List<ClassHandler.FieldVarHandle> fields;
            try {
                fields = ClassHandler.bigFilter_allSuper(clazz, HEAD, isHealthField, false, false);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
            VarHandle handle = fields.size() > 1 ? fields.get(fields.size() - 1).varHandle() : fields.size() == 1 ? fields.get(0).varHandle() : null;
            if (handle != null) {
                EntityDataAccessor<?> data = (EntityDataAccessor<?>) handle.get();
                boolean typeFloat = ClassHandler.getActuallyType(data.getClass()).isAssignableFrom(Float.class);
                entityHealthDatas.put(clazz.getName(), new IndexAndType(data.getId(), typeFloat));
            }
        }


    }

    public static Float getValue(LivingEntity living, EntityDataAccessor<Float> data) {
        return getItem(((SyncEntityDataAccessor) living.getEntityData()), data).getValue();
    }

    public static <T> SynchedEntityData.DataItem<T> getItem(SyncEntityDataAccessor eD, EntityDataAccessor<T> p_135380_) {
        eD.lock().readLock().lock();

        SynchedEntityData.DataItem<T> dataitem;
        try {
            //noinspection unchecked
            dataitem = (SynchedEntityData.DataItem<T>) eD.itemsById().get(p_135380_.getId());
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.forThrowable(throwable, "Getting synched entity data");
            CrashReportCategory crashreportcategory = crashreport.addCategory("Synched entity data");
            crashreportcategory.setDetail("Data ID", p_135380_);
            throw new ReportedException(crashreport);
        } finally {
            eD.lock().readLock().unlock();
        }

        return dataitem;
    }

    public static <T> void set(SyncEntityDataAccessor eD, EntityDataAccessor<T> p_276368_, T p_276363_) {
        SynchedEntityData.DataItem<T> dataitem = getItem(eD, p_276368_);
        if (ObjectUtils.notEqual(p_276363_, dataitem.getValue())) {
            setDataItemValue(dataitem, p_276363_);
            eD.caller().onSyncedDataUpdated(p_276368_);
            dataitem.setDirty(true);
            eD.setIsDirty(true);
        }

    }

    @SuppressWarnings("unchecked")
    public static void catchSetTrueHealth(LivingEntity living, float value) {
        IndexAndType indexAndType = EntityExpandedContext.getIndexAndType(living);
        SynchedEntityData eD = living.getEntityData();
        SyncEntityDataAccessor accessor = (SyncEntityDataAccessor) eD;
        if (indexAndType != null)
            if (indexAndType.isFloat()) {

                set(accessor, (EntityDataAccessor<Float>) accessor.itemsById().get(indexAndType.index()).getAccessor(), value);
            } else {
                set(accessor, (EntityDataAccessor<Double>) accessor.itemsById().get(indexAndType.index()).getAccessor(), (double) value);

            }
    }

    private static void dropAllDeathLoot(LivingEntity entity, DamageSource source) {
        ((AccessorLivingEntity) entity).callDropAllDeathLoot(source);
    }

    private static void createWitherRose(LivingEntity entity, LivingEntity living) {
        ((AccessorLivingEntity) entity).callCreateWitherRose(living);
    }

    private static float getSoundVolume(LivingEntity living) {
        return ((AccessorLivingEntity) living).invokeGetSoundVolume();
    }

    private static boolean checkTotemDeathProtection(LivingEntity living, DamageSource source) {
        return ((AccessorLivingEntity) living).callCheckTotemDeathProtection(source);
    }

    private static SoundEvent getDeathSound(LivingEntity living) {
        return ((AccessorLivingEntity) living).invokeGetDeathSound();
    }

    private static void playHurtSound(LivingEntity living, DamageSource source) {
        ((AccessorLivingEntity) living).invokePlayHurtSound(source);
    }

    private static void markHurt(Entity entity) {
        try {
            ClassHandler.IMPL_LOOKUP().findVirtual(Entity.class, MCMapping.Entity$METHOD$markHurt.get(), MethodType.methodType(void.class))
                    .bindTo(entity).invoke();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static void hurtHelmet(LivingEntity entity, DamageSource source, float amount) {
        ((AccessorLivingEntity) entity).callHurtHelmet(source, amount);
    }

    public static <T> void setDataItemValue(SynchedEntityData.DataItem<T> dataItem, T value) {
        try {
            VarHandle varHandle = ClassHandler.IMPL_LOOKUP().findVarHandle(SynchedEntityData.DataItem.class, MCMapping.SynchedEntityData$DataItem$FIELD$value.get(), Object.class);
            varHandle.set(dataItem, value);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean hasVanillaGetHealth(Method[] methods) {
        return Arrays.stream(methods).anyMatch(method -> !Modifier.isStatic(method.getModifiers())
                && method.getParameterCount() == 0
                && method.getName().equals(MCMapping.LivingEntity$METHOD$getHealth.get()));
    }

    public static List<Method> hasSelfGetMaxHealth(Method[] methods) {
        return Arrays.stream(methods).filter(method -> !Modifier.isStatic(method.getModifiers())
                && method.getParameterCount() == 0
                && method.getReturnType().isAssignableFrom(float.class)
                && containsIgnoreCase(method.getName(), "HEALTH")
                && containsIgnoreCase(method.getName(), "GET")
                && containsIgnoreCase(method.getName(), "MAX")).toList();
    }

    public static List<Method> hasSelfGetHealth(Method[] methods) {
        return Arrays.stream(methods).filter(method -> !Modifier.isStatic(method.getModifiers())
                && method.getParameterCount() == 0
                && method.getReturnType().isAssignableFrom(float.class)
                && containsIgnoreCase(method.getName(), "HEALTH")
                && containsIgnoreCase(method.getName(), "GET")
                && !containsIgnoreCase(method.getName(), "MAX")).toList();
    }

    public static boolean died(LivingEntity living) {
        return living.getTags().contains("deAddedKillsCount");
    }

    public EntityActuallyHurt disableEffects(boolean z) {
        this.disableEffects = z;
        return this;
    }

    public void actuallyHurt(DamageSource source, float amount) {
        actuallyHurt(source, amount, false);
    }

    public void actuallyHurt(DamageSource source, float amount, boolean special) {
        if (SafeClass.isFantasyEndingLoaded()) {
            FeActuallyHurt.actuallyHurt(entity, source, amount, special);
            return;
        }
        try {
            if (entity.level().isClientSide) {
                return;
            } else if (entity.isDeadOrDying()) {
                return;
            }
            if (!disableEffects)
                net.minecraftforge.common.ForgeHooks.onLivingAttack(entity, source, amount);
            if (entity.isSleeping() && !entity.level().isClientSide) {
                entity.stopSleeping();
            }
            accessorLivingEntity.setNoActionTime(0);
            float f = amount;
            boolean flag = false;
            if (amount > 0.0F && entity.isDamageSourceBlocked(source)) {
                net.minecraftforge.event.entity.living.ShieldBlockEvent ev = net.minecraftforge.common.ForgeHooks.onShieldBlock(entity, source, amount);
                if (!ev.isCanceled()) {
                    if (ev.shieldTakesDamage()) accessorLivingEntity.callHurtCurrentlyUsedShield(amount);
                }
            }
            if (source.is(DamageTypeTags.IS_FREEZING) && entity.getType().is(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES)) {
                amount *= 5.0F;
            }

            entity.walkAnimation.setSpeed(1.5F);
            accessorLivingEntity.setLastHurt(amount);
            entity.invulnerableTime = 20;
            actuallyHurt0(source, amount, special);
            entity.hurtTime = entity.hurtDuration;
            if (source.is(DamageTypeTags.DAMAGES_HELMET) && !entity.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
                accessorLivingEntity.callHurtHelmet(source, amount);
                amount *= 0.75F;
            }

            Entity entity1 = source.getEntity();
            if (entity1 != null) {
                if (entity1 instanceof LivingEntity livingentity1) {
                    if (!source.is(DamageTypeTags.NO_ANGER)) {
                        entity.setLastHurtByMob(livingentity1);
                    }
                }

                if (entity1 instanceof Player player1) {
                    accessorLivingEntity.setLastHurtByPlayerTime(100);
                    entity.setLastHurtByPlayer(player1);
                } else if (entity1 instanceof net.minecraft.world.entity.TamableAnimal tamableEntity) {
                    if (tamableEntity.isTame()) {
                        accessorLivingEntity.setLastHurtByPlayerTime(100);
                        LivingEntity livingentity2 = tamableEntity.getOwner();
                        if (livingentity2 instanceof Player player) {
                            entity.setLastHurtByPlayer(player);
                        } else {
                            entity.setLastHurtByPlayer(null);
                        }
                    }
                }
            }
            if (!source.is(DamageTypeTags.NO_IMPACT)) {
                markHurt(entity);
            }

            if (entity1 != null && !source.is(DamageTypeTags.IS_EXPLOSION)) {
                double d0 = entity1.getX() - entity.getX();

                double d1;
                for (d1 = entity1.getZ() - entity.getZ(); d0 * d0 + d1 * d1 < 1.0E-4D; d1 = (Math.random() - Math.random()) * 0.01D) {
                    d0 = (Math.random() - Math.random()) * 0.01D;
                }

                entity.knockback(0.4F, d0, d1);
                if (!flag) {
                    entity.indicateDamage(d0, d1);
                }
            }
            entity.level().broadcastDamageEvent(entity, source);
            if (entity.isDeadOrDying()) {
                if (!checkTotemDeathProtection(entity, source)) {
                    SoundEvent soundevent = getDeathSound(entity);
                    if (soundevent != null) {
                        if (!accessorLivingEntity.isDead() && !disableEffects) {
                            entity.playSound(soundevent, getSoundVolume(entity), entity.getVoicePitch());
                        }
                    }
                    die(entity, source, special);
                }
            } else {
                //if (special)
                //    playHurtSound(entity, source);
            }

            accessorLivingEntity.setLastDamageSource(source);
            accessorLivingEntity.setLastDamageStamp(entity.level().getGameTime());
            if (entity instanceof ServerPlayer) {
                CriteriaTriggers.ENTITY_HURT_PLAYER.trigger((ServerPlayer) entity, source, f, amount, flag);
            }
            if (entity1 instanceof ServerPlayer) {
                CriteriaTriggers.PLAYER_HURT_ENTITY.trigger((ServerPlayer) entity1, entity, source, f, amount, flag);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    //only server
    public void die(LivingEntity livingEntity, DamageSource p_21014_, boolean special) {
        if (livingEntity.level().isClientSide) return;

        try {
            livingEntity.die(p_21014_);
        } catch (ClassCastException exception) {
            exception.printStackTrace();
        }

        LivingEntity lastHurtBy = livingEntity.getKillCredit();
        if (accessorLivingEntity.getDeathScore() >= 0 && lastHurtBy != null) {
            lastHurtBy.awardKillScore(livingEntity, accessorLivingEntity.getDeathScore(), p_21014_);
        }
        if (!livingEntity.isRemoved() && !accessorLivingEntity.isDead()) {
            synchronized (ForgeHooks.class) {
                MinecraftForge.EVENT_BUS.post(new LivingDeathEvent(entity, p_21014_));
            }
            if (special) catchSetTrueHealth(livingEntity, 0);
            Entity entity = p_21014_.getEntity();
            LivingEntity livingentity = livingEntity.getKillCredit();
            if (accessorLivingEntity.getDeathScore() >= 0 && livingentity != null) {
                livingentity.awardKillScore(livingEntity, accessorLivingEntity.getDeathScore(), p_21014_);
            }

            if (livingEntity.isSleeping()) {
                livingEntity.stopSleeping();
            }

            accessorLivingEntity.setDead(true);
            livingEntity.getCombatTracker().recheckStatus();
            Level level = livingEntity.level();
            if (level instanceof ServerLevel serverlevel) {
                if (entity == null || entity.killedEntity(serverlevel, livingEntity)) {
                    livingEntity.gameEvent(GameEvent.ENTITY_DIE);
                    dropAllDeathLoot(livingEntity, p_21014_);
                    createWitherRose(livingEntity, livingentity);
                }

                livingEntity.level().broadcastEntityEvent(livingEntity, (byte) 3);
            }

            livingEntity.setPose(Pose.DYING);
        }
    }

    public void actuallyHurt0(DamageSource source, float amount, boolean special) {
        if (amount <= 0) return;
        entity.getCombatTracker().recordDamage(source, amount);
        if (amount == Float.POSITIVE_INFINITY) {
            entity.setHealth(Float.NEGATIVE_INFINITY);
            if (special) catchSetTrueHealth(entity, Float.NEGATIVE_INFINITY);
        } else {
            entity.setHealth(Math.min(entity.getHealth() - amount, entity.getMaxHealth()));
            if (special) catchSetTrueHealth(entity, Math.min(entity.getHealth() - amount, entity.getMaxHealth()));
        }
        if (!disableEffects)
            entity.gameEvent(GameEvent.ENTITY_DAMAGE);
        if (!entity.isDeadOrDying()) entity.deathTime = 0;
    }

    public void playDeathSound() {
        SoundEvent soundEvent = getDeathSound(entity);
        if (soundEvent != null)
            entity.playSound(soundEvent, getSoundVolume(entity), entity.getVoicePitch());
    }

    public record IndexAndType(int index, boolean isFloat) {
    }
}
