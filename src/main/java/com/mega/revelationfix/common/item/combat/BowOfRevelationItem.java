package com.mega.revelationfix.common.item.combat;

import com.Polarice3.Goety.common.effects.GoetyEffects;
import com.Polarice3.Goety.common.entities.projectiles.DeathArrow;
import com.Polarice3.Goety.init.ModSounds;
import com.mega.revelationfix.common.apollyon.common.BypassInvulArrow;
import com.mega.revelationfix.common.item.FontItemExtensions;
import com.mega.revelationfix.common.item.ICenterDescItem;
import com.mega.revelationfix.util.ATAHelper2;
import com.mega.revelationfix.util.MUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import z1gned.goetyrevelation.util.ATAHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("deprecation")
public class BowOfRevelationItem extends BowItem implements ICenterDescItem {
    public static final String FORCE_ADD_EFFECT = "forceAddEffect";
    public static final List<MobEffect> BAD_EFFECTS = new ArrayList<>();

    public BowOfRevelationItem(Properties p_40660_) {
        super(p_40660_);
    }

    public static float getPowerForTime(int p_40662_) {
        float f = (float) p_40662_ / 10.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    public static void forceAddEffect(LivingEntity living, MobEffectInstance effectInstance, Entity source) {
        if (!living.addEffect(effectInstance)) {
            MobEffectInstance mobeffectinstance = living.activeEffects.get(effectInstance.getEffect());
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.living.MobEffectEvent.Added(living, mobeffectinstance, effectInstance, source));
            if (mobeffectinstance == null) {
                living.activeEffects.put(effectInstance.getEffect(), effectInstance);
                living.onEffectAdded(effectInstance, living);
            } else if (mobeffectinstance.update(effectInstance)) {
                living.onEffectUpdated(mobeffectinstance, true, source);
            }
        }
    }

    private static MobEffect selectBadEffect() {
        if (BAD_EFFECTS.isEmpty()) {
            for (MobEffect effect : ForgeRegistries.MOB_EFFECTS.getValues()) {
                try {
                    String s = BuiltInRegistries.MOB_EFFECT.getKey(effect).getNamespace();
                    if (effect.getCategory() == MobEffectCategory.HARMFUL && (s.equals("minecraft") || s.equals("goety")))
                        BAD_EFFECTS.add(effect);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }
        MobEffect effect = MUtils.randomSelect(BAD_EFFECTS);
        if (effect == MobEffects.HARM || effect == MobEffects.HEAL)
            effect = MobEffects.HUNGER;
        return effect;
    }

    @Override
    public void releaseUsing(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull LivingEntity livingEntity, int timeLeft) {

        if (livingEntity instanceof Player player) {
            boolean flag = player.getAbilities().instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, itemStack) > 0;
            ItemStack itemstack = player.getProjectile(itemStack);

            int i = this.getUseDuration(itemStack) - timeLeft;
            i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(itemStack, level, player, i, !itemstack.isEmpty() || flag);
            if (i < 0) return;

            if (!itemstack.isEmpty() || flag) {
                if (itemstack.isEmpty()) {
                    itemstack = new ItemStack(Items.ARROW);
                }

                float f = getPowerForTime(i);
                if (!((double) f < 0.1D)) {
                    boolean flag1 = player.getAbilities().instabuild || (itemstack.getItem() instanceof ArrowItem && ((ArrowItem) itemstack.getItem()).isInfinite(itemstack, itemStack, player));
                    if (!level.isClientSide) {
                        int repeatTimes = 1;
                        boolean isOdamane = false;
                        boolean isAscension = ATAHelper.hasHalo(player);
                        if (ATAHelper2.hasOdamane(player)) {
                            repeatTimes = 3;
                            isOdamane = true;
                        }
                        for (int i2 = 0; i2 < repeatTimes; i2++) {
                            ArrowItem arrowitem = (ArrowItem) (itemstack.getItem() instanceof ArrowItem ? itemstack.getItem() : Items.ARROW);
                            AbstractArrow deathArrow = new DeathArrow(level, player);
                            if (player.isShiftKeyDown())
                                deathArrow.addTag(BypassInvulArrow.TAG_BYPASS_NAME);
                            deathArrow.addTag(FORCE_ADD_EFFECT);
                            ((DeathArrow) (deathArrow)).setEffectsFromItem(itemStack);
                            deathArrow = customArrow(deathArrow);
                            deathArrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, f * 3.2F * ((isOdamane || isAscension) ? 2.0F : 1.0F), i2 == 0 ? 1.0F : 5.0F);

                            if (isOdamane) {
                                ((DeathArrow) (deathArrow)).addEffect(new MobEffectInstance(GoetyEffects.DOOM.get(), 400, 19));
                            }
                            if (isAscension || isOdamane) {
                                deathArrow.setPierceLevel((byte) 7);
                            }
                            if (isAscension) {
                                if (i / 20F >= 3.5) {
                                    DeathArrow deathArrow1 = (DeathArrow) deathArrow;
                                    int duration = player.random.nextInt(200, 600);
                                    for (RegistryObject<MobEffect> ro : GoetyEffects.EFFECTS.getEntries()) {
                                        MobEffect effect = ro.get();
                                        if (effect.getCategory() == MobEffectCategory.HARMFUL && ro != GoetyEffects.DOOM && ro != GoetyEffects.EVIL_EYE && ro != GoetyEffects.WILD_RAGE && ro != GoetyEffects.STORMS_WRATH) {
                                            deathArrow1.addEffect(new MobEffectInstance(effect, duration, 4));
                                        }
                                    }
                                    deathArrow1.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, duration, 4));
                                }

                            }

                            ((DeathArrow) deathArrow).addEffect(new MobEffectInstance(selectBadEffect(), player.random.nextInt(60, 80 + 1), player.random.nextInt(4)));

                            if (f >= 1.0F) {
                                deathArrow.setCritArrow(true);
                            }

                            int j = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, itemStack);
                            if (j > 0) {
                                deathArrow.setBaseDamage(deathArrow.getBaseDamage() + (double) j * 0.5D + 0.5D);
                            }

                            int k = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, itemStack);
                            if (k > 0) {
                                deathArrow.setKnockback(k);
                            }

                            if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, itemStack) > 0) {
                                deathArrow.setSecondsOnFire(100);
                            }

                            itemStack.hurtAndBreak(1, player, (p_289501_) -> {
                                p_289501_.broadcastBreakEvent(player.getUsedItemHand());
                            });
                            deathArrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                            level.addFreshEntity(deathArrow);
                        }
                    }
                    if (player.isShiftKeyDown()) {
                        int id = player.random.nextInt(1, 4);
                        player.playSound(com.mega.revelationfix.common.init.ModSounds.STAR_CAST.get(), 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                        if (id == 1)
                            player.playSound(SoundEvents.TRIDENT_RIPTIDE_1, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                        if (id == 2)
                            player.playSound(SoundEvents.TRIDENT_RIPTIDE_2, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                        if (id == 3)
                            player.playSound(SoundEvents.TRIDENT_RIPTIDE_3, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);

                    } else
                        level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.APOSTLE_SHOOT.get(), SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                    if (!flag1 && !player.getAbilities().instabuild) {
                        itemstack.shrink(1);
                        if (itemstack.isEmpty()) {
                            player.getInventory().removeItem(itemstack);
                        }
                    }

                    player.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }

    @Override
    public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
        if (!stack.hasTag() || !stack.getOrCreateTag().getBoolean("Unbreakable"))
            stack.getOrCreateTag().putBoolean("Unbreakable", true);
        super.onInventoryTick(stack, level, player, slotIndex, selectedIndex);
    }

    @Override
    public int getDamage(ItemStack stack) {
        return 0;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new FontItemExtensions());
        super.initializeClient(consumer);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack p_41421_, @Nullable Level p_41422_, @NotNull List<Component> p_41423_, @NotNull TooltipFlag p_41424_) {
        p_41423_.add(Component.translatable("item.goety_revelation.bow_of_revelation.desc0").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
        p_41423_.add(Component.translatable("item.goety_revelation.bow_of_revelation.desc1").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
        p_41423_.add(Component.translatable("item.goety_revelation.bow_of_revelation.desc2").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
    }
}
