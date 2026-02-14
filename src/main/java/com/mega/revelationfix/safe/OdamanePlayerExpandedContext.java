package com.mega.revelationfix.safe;

import com.Polarice3.Goety.common.effects.GoetyEffects;
import com.Polarice3.Goety.common.entities.boss.Apostle;
import com.Polarice3.Goety.common.entities.hostile.servants.ObsidianMonolith;
import com.Polarice3.Goety.common.entities.neutral.Owned;
import com.Polarice3.Goety.utils.CuriosFinder;
import com.google.common.collect.ImmutableList;
import com.mega.endinglib.mixin.accessor.AccessorLivingEntity;
import com.mega.endinglib.util.mc.entity.DamageSourceGenerator;
import com.mega.endinglib.util.mc.entity.MobEffectUtils;
import com.mega.revelationfix.common.apollyon.common.CooldownsManager;
import com.mega.revelationfix.common.compat.SafeClass;
import com.mega.revelationfix.common.config.CommonConfig;
import com.mega.revelationfix.common.init.GRItems;
import com.mega.revelationfix.common.init.ModBlocks;
import com.mega.revelationfix.mixin.gr.PlayerMixin;
import com.mega.revelationfix.proxy.CommonProxy;
import com.mega.revelationfix.safe.entity.PlayerInterface;
import com.mega.revelationfix.util.entity.ATAHelper2;
import com.mega.revelationfix.util.java.SynchedFixedLengthList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.Tags;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.curios.api.CuriosApi;
import z1gned.goetyrevelation.config.ModConfig;
import z1gned.goetyrevelation.util.PlayerAbilityHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

/**
 * Getter:{@link PlayerInterface} <br>
 * 自定义1.5s无敌帧:{@link OdamanePlayerExpandedContext#invulnerableTime}<br>
 * 定时抹除Debuff:{@link OdamanePlayerExpandedContext#tryCleanDeBuffs()}<br>
 * 播放凋灵音效:{@link OdamanePlayerExpandedContext#tryPlayAmbient()}<br>
 * 终末玩家所有冷却时间缩短到0.1s:{@link OdamanePlayerExpandedContext#odamaneDecreaseCooldowns()}<br>
 */
public class OdamanePlayerExpandedContext {
    public static final float PROJECTILE_DAMAGE_REDUCE = 0.85F;
    public static final float VOID_DAMAGE_REDUCE = 0.66F;
    public static final int MAX_INVUL_TIME = 30;
    public static final int MAX_REVIVE_INVULNERABLE_TIME = 600 * 20;
    public static final int NEXT_REVIVE_COOLDOWNS = 1800 * 20;
    /**
     * 终末玩家的复活事件byte
     */
    public static final byte ODAMANE_REVIVE_EVENT = (byte) 239;
    /**
     * 无物品复活事件byte
     */
    public static final byte REVIVE_EVENT = (byte) 238;
    /**
     * 复活无敌标签
     */
    public static final int REVIVE_INVULNERABLE_FLAG = 1;
    public static final int BLASPHEMOUS_FLAG = 2;
    public static List<TagKey<DamageType>> INVULNERABLE_TO_TAGS;
    public static Predicate<DamageSource> INVULNERABLE_TO_DAMAGE = (damageSource -> {
        //                                魔法                                   仙人掌                                  挤压                                     岩浆                                 铁砧                                           动能                                           冰冻                                   窒息
        return damageSource.is(DamageTypes.MAGIC) || damageSource.is(DamageTypes.CACTUS) || damageSource.is(DamageTypes.CRAMMING) || damageSource.is(DamageTypes.LAVA) || damageSource.is(DamageTypes.FALLING_ANVIL) || damageSource.is(DamageTypes.FLY_INTO_WALL) || damageSource.is(DamageTypes.FREEZE) || damageSource.is(DamageTypes.IN_WALL);
    });
    public static Predicate<LivingEntity> NONE_C_OR_S_OR_APOSTLE = (living -> EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(living) && !(living instanceof Apostle apostle));
    public final Player player;
    public final AccessorLivingEntity accessorLivingEntity;
    /**
     * 攻击过的boss
     */
    private final SynchedFixedLengthList<LivingEntity> damagedBosses = new SynchedFixedLengthList<>(5);
    /**
     * 持有的黑曜石巨柱<br>
     * GR修改:{@link PlayerMixin#getMonolith()}
     */
    private final SynchedFixedLengthList<ObsidianMonolith> ownedMonoliths = new SynchedFixedLengthList<>(5, (o) -> {
    }, (o) -> {
        if (!o.level().isClientSide && o.getTrueOwner() instanceof Player player1 && !o.isRemoved()) {
            o.silentDie(player1.damageSources().starve());
        }
    });
    public PlayerInterface playerInterface;
    public PlayerAbilityHelper abilityHelper;
    /**
     * 巫术引动台3010号仪式玩家占位时间
     */
    public int teleportStayingTime;
    public int teleportStayingTimeOld;
    /**
     * 触发复活后玩家剩余的无敌时间
     */
    private int reviveInvulnerableTime;
    /**
     * 触发下次无敌所需时间
     */
    private int nextReviveCooldowns;
    private int ambientSoundTime;
    /**
     * 更加快速的玩家拥有终末环判断
     */
    private boolean playerHasOdamane;
    /**
     * 更强的无敌帧
     */
    private int invulnerableTime;

    public OdamanePlayerExpandedContext(Player player) {
        this.player = player;
        playerInterface = (PlayerInterface) player;
        abilityHelper = (PlayerAbilityHelper) player;
        accessorLivingEntity = (AccessorLivingEntity) player;
    }
    public static void setInvulnerableTo() {
        List<TagKey<DamageType>> tagKeys = new ArrayList<>();
        tagKeys.add(DamageTypeTags.IS_FIRE);//火焰
        tagKeys.add(DamageTypeTags.IS_FALL); //摔落
        tagKeys.add(DamageTypeTags.IS_DROWNING); //溺水
        tagKeys.add(DamageTypeTags.IS_LIGHTNING); //闪电
        tagKeys.add(DamageTypeTags.IS_EXPLOSION); //爆炸
        tagKeys.add(DamageTypeTags.IS_FREEZING); //冰冻
        OdamanePlayerExpandedContext.INVULNERABLE_TO_TAGS = ImmutableList.copyOf(tagKeys);
    }
    public static float damageScale(float amount, Player odamanePlayer) {
        return Math.min(amount, damageLimit(odamanePlayer));
    }

    public static float damageLimit(Player odamanePlayer) {
        return Math.min(20.0F, odamanePlayer.getMaxHealth() * 0.25F);
    }

    private static void selfEffectGive(LivingEntity living, MobEffectInstance effectInstance) {
        MobEffectUtils.forceAdd(living, effectInstance, living);
    }

    public static boolean isInvulnerableTo(DamageSource damageSource) {
        if (trueKill(damageSource)) return false;
        for (TagKey<DamageType> key : INVULNERABLE_TO_TAGS)
            if (damageSource.is(key))
                return true;
        return INVULNERABLE_TO_DAMAGE.test(damageSource);
    }

    private static boolean trueKill(DamageSource source) {
        return ((DamageSourceInterface) source).revelationfix$trueKill();
    }

    public boolean hasOdamane() {
        return this.playerHasOdamane;
    }

    private boolean getFlag(int mask) {
        boolean[] f = new boolean[] {false};
        CommonProxy.getPlayerCapOptional(player).ifPresent(cap -> {
            f[0] = (cap.readAllExpandedFlags() & mask) != 0;
        });
        return f[0];
    }

    private void setFlags(int mask, boolean value) {
        CommonProxy.getPlayerCapOptional(player).ifPresent(cap -> {
            int i = cap.readAllExpandedFlags();
            if (value) {
                i |= mask;
            } else {
                i &= ~mask;
            }
            cap.setAllExpandedFlags(i & 255);
        });
    }

    /**
     * xxx = nbt.get
     *
     * @param nbt NBT Data
     */
    public void read(CompoundTag nbt) {
        this.invulnerableTime = nbt.getInt("OdamaneInvulTime");
        this.reviveInvulnerableTime = nbt.getInt("OdamaneReviveInvulnerableTime");
        this.nextReviveCooldowns = nbt.getInt("OdamaneReviveCooldowns");
    }

    /**
     * nbt.put(xxx)
     *
     * @param nbt NBT Data
     */
    public void save(CompoundTag nbt) {
        nbt.putInt("OdamaneInvulTime", this.invulnerableTime);
        nbt.putInt("OdamaneReviveInvulnerableTime", this.reviveInvulnerableTime);
        nbt.putInt("OdamaneReviveCooldowns", this.nextReviveCooldowns);
    }

    public void tick() {
        Level level = player.level();
        if (level.isClientSide) {
            this.teleportStayingTimeOld = this.teleportStayingTime;
            if (player.isShiftKeyDown()) {
                BlockPos pos = player.blockPosition().above(-1);
                if (level.getBlockState(pos).is(ModBlocks.RUNE_REACTOR.get())) {
                    if (teleportStayingTime < 20)
                        teleportStayingTime++;
                } else if (teleportStayingTime > 0)
                    teleportStayingTime--;
            } else if (teleportStayingTime > 0)
                teleportStayingTime--;
        }
        this.playerHasOdamane = CuriosFinder.hasCurio(player, GRItems.ODAMANE_HALO.get());
        if (ownedMonoliths.size() > 0)
            ownedMonoliths.removeIf(obsidianMonolith -> !this.playerHasOdamane || obsidianMonolith.isRemoved());
        if (!ATAHelper2.hasOdamane(player)) return;
        reviveCheck();
        playerInterface.revelationfix$setBaseAttributeMode(false);
        tryPlayAmbient();
        tryCleanDeBuffs();
        odamaneDecreaseCooldowns();
        if (!player.isSpectator())
            baseTick(level);
        tryCleanDeBuffs();
    }

    /**
     * 若事件复活没能成功,在此处再次尝试复生
     */
    public void reviveCheck() {
        Level level = player.level();
        if (!level.isClientSide) {
            this.setReviveInvulnerableFlag(this.reviveInvulnerableTime > MAX_REVIVE_INVULNERABLE_TIME - 60 * 20);
            if (player.isDeadOrDying() && this.nextReviveCooldowns <= 0) {
                this.player.setHealth(this.player.getMaxHealth() * 0.75F);
                if (this.player.getHealth() > 0) {
                    level.broadcastEntityEvent(player, ODAMANE_REVIVE_EVENT);
                    this.onRevive();
                    if (!player.getCooldowns().isOnCooldown(GRItems.HALO_OF_THE_END)) {
                        SafeClass.enableTimeStop(player, true, 300);
                    }
                }
            }
            if (this.reviveInvulnerableTime > 0)
                this.reviveInvulnerableTime--;
            if (this.nextReviveCooldowns > 0)
                this.nextReviveCooldowns--;
        }
    }

    public void onRevive() {
        this.nextReviveCooldowns = NEXT_REVIVE_COOLDOWNS;
        this.reviveInvulnerableTime = MAX_REVIVE_INVULNERABLE_TIME;
    }

    /**
     * 终末玩家播放凋灵音效
     */
    private void tryPlayAmbient() {
        if (ModConfig.ENABLE_PLAYER_AMBIENT.get()) {
            if (player.isAlive() && player.getRandom().nextInt(1000) < ambientSoundTime++) {
                resetAmbientSoundTime();
                if (ModConfig.ENABLE_PLAYER_AMBIENT.get()) {
                    player.playSound(SoundEvents.WITHER_AMBIENT);
                }
            }
        }
    }

    /**
     * 在末地时，终末之环会给予周围6格实体诅咒，痉挛，缓慢，虚弱效果。（全效果IV级）<br>
     * 在下界时，终末之环会给予玩家食尸者，力量，排斥效果。（全效果IV级）<br>
     * 在主世界的晚上时，终末之环会给予玩家夜视，迅捷，急迫，发光效果。（全效果III级）<br>
     * 在主世界的晚上时，终末之环会给予玩家夜视，迅捷，急迫，发光效果。（全效果III级）<br>
     */
    private void baseTick(Level level) {
        ResourceKey<Level> dimension = level.dimension();
        if (this.invulnerableTime > 0) {
            this.invulnerableTime--;
        }
        if (this.reviveInvulnerableTime > 0) {
            //复活后的无敌时间持续对周围实体造成伤害（排除使徒和自己的仆从,宠物）
            for (LivingEntity living : level.getEntitiesOfClass(LivingEntity.class, new AABB(player.blockPosition()).inflate(3.0F), NONE_C_OR_S_OR_APOSTLE)) {
                if (living == player) continue;
                if (living instanceof Owned owned && owned.getMasterOwner() == player)
                    continue;
                UUID uuid;
                if (living instanceof OwnableEntity ownable && (uuid = ownable.getOwnerUUID()) != null && uuid.equals(player.getUUID()))
                    continue;
                if (living.invulnerableTime > 0)
                    living.invulnerableTime--;
                living.setLastHurtByPlayer(player);
                DamageSource damageSource = new DamageSourceGenerator(living).source(DamageTypes.FELL_OUT_OF_WORLD, player);
                living.hurt(damageSource, 10.0F);
            }
        }
        //服务端
        if (!level.isClientSide) {
            if (this.reviveInvulnerableTime > MAX_REVIVE_INVULNERABLE_TIME - 60 * 20) {
                if (accessorLivingEntity.getLastDamageSource() == null || !((DamageSourceInterface) accessorLivingEntity.getLastDamageSource()).revelationfix$trueKill())
                    player.setHealth(Math.max(player.getHealth(), player.getMaxHealth() * 0.45F));
                abilityHelper.setInvulTick(10);
            }
            if (dimension == Level.END) {
                //在末地时，终末之环会给予周围6格实体诅咒，痉挛，缓慢，虚弱效果。（全效果IV级）
                if (player.tickCount % 5 == 0) {
                    for (LivingEntity living : level.getEntitiesOfClass(LivingEntity.class, new AABB(player.blockPosition()).inflate(6), EntitySelector.NO_CREATIVE_OR_SPECTATOR)) {
                        selfEffectGive(living, new MobEffectInstance(GoetyEffects.CURSED.get(), 200, 3));
                        selfEffectGive(living, new MobEffectInstance(GoetyEffects.SPASMS.get(), 200, 3));
                        selfEffectGive(living, new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 3));
                        selfEffectGive(living, new MobEffectInstance(MobEffects.WEAKNESS, 200, 3));
                    }
                }
            } else if (dimension == Level.NETHER) {
                //在下界时，终末之环会给予玩家食尸者，力量，排斥效果。（全效果IV级）
                selfEffectGive(player, new MobEffectInstance(GoetyEffects.CORPSE_EATER.get(), 200, 3, false, false));
                selfEffectGive(player, new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 3, false, false));
                selfEffectGive(player, new MobEffectInstance(GoetyEffects.REPULSIVE.get(), 200, 3, false, false));
            } else if (dimension == Level.OVERWORLD) {
                if (level.getDayTime() % 24000L < 12000L) {
                    //在主世界的白天时，终末之环会给予玩家饱和，抗性提升，生命恢复效果（全效果II级）
                    selfEffectGive(player, new MobEffectInstance(MobEffects.SATURATION, 200, 1, false, false));
                    selfEffectGive(player, new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 1, false, false));
                    selfEffectGive(player, new MobEffectInstance(MobEffects.REGENERATION, 200, 1, false, false));
                } else {
                    //在主世界的晚上时，终末之环会给予玩家夜视，迅捷，急迫，发光效果。（全效果III级）
                    selfEffectGive(player, new MobEffectInstance(MobEffects.NIGHT_VISION, 1360, 2, false, false));
                    selfEffectGive(player, new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, 1, false, false));
                    selfEffectGive(player, new MobEffectInstance(MobEffects.DIG_SPEED, 200, 2, false, false));
                    selfEffectGive(player, new MobEffectInstance(MobEffects.GLOWING, 200, 2, false, false));
                }
            }
            if (!player.getAbilities().mayfly) {
                player.getAbilities().mayfly = true;
                //if (player instanceof ServerPlayer sp) {
                //    sp.connection.handlePlayerAbilities(new ServerboundPlayerAbilitiesPacket(sp.getAbilities()));
                //}
            }
        }//客户端
        else {
            if (!player.getAbilities().mayfly)
                player.getAbilities().mayfly = true;
            /*

            if (player.getRandom().nextInt(8) == 0) {
                //float time = (float) (Blaze3D.getTime() * 90F);
                //this.player.level.addParticle(ModParticleTypes.SUMMON.get(), player.getX() + Mth.cos(time) / 1.5F, player.random.triangle(player.getY(0.4F), 0.3F), player.getZ() + Mth.sin(time) / 1.5F, 0D, player.random.nextFloat() * 0.1F, 0D);
            }
             */
        }
    }

    /**
     * 定时抹除Debuff
     */
    @SuppressWarnings("DataFlowIssue")
    public void tryCleanDeBuffs() {
        if (!this.player.level().isClientSide) {
            List<MobEffect> toRemovedDeBuffs = new ArrayList<>();
            for (MobEffect effect : player.getActiveEffectsMap().keySet()) {
                if (CommonConfig.inBypassEffect(effect))
                    continue;
                if (effect.getCategory() == MobEffectCategory.HARMFUL)
                    toRemovedDeBuffs.add(effect);
                else if (effect.getCategory() == MobEffectCategory.NEUTRAL && !BuiltInRegistries.MOB_EFFECT.getKey(effect).getNamespace().equals("minecraft"))
                    toRemovedDeBuffs.add(effect);
            }
            toRemovedDeBuffs.forEach(effect -> {
                MobEffectInstance mobeffectinstance = player.removeEffectNoUpdate(effect);
                if (mobeffectinstance != null) {
                    accessorLivingEntity.callOnEffectRemoved(mobeffectinstance);
                }
            });
        }
    }

    /**
     * 终末玩家所有冷却时间缩短到0.1s
     */
    public void odamaneDecreaseCooldowns() {
        if (!this.player.level().isClientSide) {
            Inventory inventory = player.getInventory();
            inventory.items.forEach(stack -> CooldownsManager.odamaneDecreaseCooldowns(player, stack.getItem()));
            inventory.armor.forEach(stack -> CooldownsManager.odamaneDecreaseCooldowns(player, stack.getItem()));
            inventory.offhand.forEach(stack -> CooldownsManager.odamaneDecreaseCooldowns(player, stack.getItem()));
            if (player.tickCount % 5 == 0)
                CuriosApi.getCuriosInventory(player).ifPresent(iCuriosItemHandler -> iCuriosItemHandler.getCurios().values().forEach(iCurioStacksHandler -> {
                    for (int i = iCurioStacksHandler.getSlots() - 1; i >= 0; i--) {
                        ItemStack stack = iCurioStacksHandler.getStacks().getStackInSlot(i);
                        if (!stack.isEmpty())
                            CooldownsManager.odamaneDecreaseCooldowns(player, stack.getItem());
                    }
                }));
        }
    }

    private void resetAmbientSoundTime() {
        ambientSoundTime = -220;
    }

    public boolean recentlyAttackedBoss(LivingEntity toCheck) {
        return this.damagedBosses.contains(toCheck);
    }

    public void tryAttackBoss(Entity target) {
        if (target instanceof LivingEntity living && living.getType().is(Tags.EntityTypes.BOSSES)) {
            this.damagedBosses.add(living);
        }
    }

    /**
     * 调用:{@link com.mega.revelationfix.mixin.LivingEntityMixin#handleDamageEvent(DamageSource, CallbackInfo)}<br>
     * 佩戴终末之环的玩家免疫魔法，燃烧，坠落，仙人掌，溺水，闪电，爆炸，岩浆，挤压，铁砧坠落，动能（鞘翅撞击），冰冻细雪，窒息伤害。
     */
    @SuppressWarnings("JavadocReference")
    public void handleDamageEvent(DamageSource damageSource, CallbackInfo ci) {
        if (this.hasOdamane() && !trueKill(damageSource)) {
            if (this.invulnerableTime > 0 || isInvulnerableTo(damageSource)) ci.cancel();
            else this.invulnerableTime = MAX_INVUL_TIME;
        }
    }

    /**
     * 越过事件拦截恢复生命，汲取有益部分
     */
    public void heal(float amount, CallbackInfo ci) {
        if (hasOdamane()) {
            float afterEvent = net.minecraftforge.event.ForgeEventFactory.onLivingHeal(player, amount);
            if (afterEvent > amount)
                amount = afterEvent;
            float f = player.getHealth();
            if (f > 0.0F)
                player.setHealth(f + amount);
            ci.cancel();
        }
    }

    public void setInvulnerableTicks() {
        this.invulnerableTime = MAX_INVUL_TIME;
    }

    public int getInvulnerableTime() {
        return invulnerableTime;
    }

    public boolean isInvulnerable() {
        return this.playerHasOdamane && (this.getInvulnerableTime() > 0 || this.getReviveInvulnerableTime() > MAX_REVIVE_INVULNERABLE_TIME - 60 * 20);
    }

    public boolean isReviveInvulnerableFlag() {
        return this.playerHasOdamane && this.getFlag(REVIVE_INVULNERABLE_FLAG);
    }

    public void setReviveInvulnerableFlag(boolean flag) {
        this.setFlags(REVIVE_INVULNERABLE_FLAG, flag);
    }

    public boolean isBlasphemous() {
        return this.getFlag(BLASPHEMOUS_FLAG);
    }

    public void setBlasphemous(boolean flag) {
        this.setFlags(BLASPHEMOUS_FLAG, flag);
    }

    public int getReviveInvulnerableTime() {
        return this.reviveInvulnerableTime;
    }

    public int getNextReviveCooldowns() {
        return this.nextReviveCooldowns;
    }

    public SynchedFixedLengthList<ObsidianMonolith> getOwnedMonoliths() {
        return ownedMonoliths;
    }
}
