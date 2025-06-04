package com.mega.revelationfix.common.item.combat;

import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.utils.ParticleUtil;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mega.revelationfix.client.enums.ModUseAnim;
import com.mega.revelationfix.common.entity.projectile.GungnirSpearEntity;
import com.mega.revelationfix.common.item.FontItemExtensions;
import com.mega.revelationfix.common.item.IInvulnerableItem;
import com.mega.revelationfix.safe.DamageSourceInterface;
import com.mega.revelationfix.safe.entity.LivingEventEC;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class GungnirItem extends Item implements Vanishable, IInvulnerableItem, ICustomHurtWeapon {
    public static final int THROW_THRESHOLD_TIME = 10;
    public static final float BASE_DAMAGE = 8.0F;
    public static final float SHOOT_POWER = 2.5F;
    private static final RandomSource randomSource = new LegacyRandomSource(666L);
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public GungnirItem() {
        super(new Properties().durability(6666).fireResistant().rarity(Rarity.UNCOMMON));
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 21.0, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -2.9000000953674316, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    @Override
    public boolean canBeHurtBy(DamageSource p_41387_) {
        if (!p_41387_.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) return false;
        return super.canBeHurtBy(p_41387_);
    }

    public boolean canAttackBlock(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, Player player) {
        return !player.isCreative();
    }

    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack itemStack) {
        return ModUseAnim.GUNGNIR_SPEAR;
    }

    public int getUseDuration(@NotNull ItemStack itemStack) {
        return 72000;
    }
    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        return super.onEntitySwing(stack, entity);
    }
    public void releaseUsing(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull LivingEntity livingEntity, int timeLeft) {
        if (livingEntity instanceof Player player) {
            int i = this.getUseDuration(itemStack) - timeLeft;
            if (i >= 7) {
                {
                    if (!level.isClientSide) {
                        itemStack.hurtAndBreak(player.isShiftKeyDown() ? 6 : 1, player, (p_43388_) -> p_43388_.broadcastBreakEvent(livingEntity.getUsedItemHand()));
                        {
                            GungnirSpearEntity gungnirSpear = new GungnirSpearEntity(level, player, itemStack);
                            gungnirSpear.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F, 1.0F);

                            if (!player.isShiftKeyDown()) {
                                level.playSound(null, player, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
                                gungnirSpear.setSpearItem(ItemStack.EMPTY);
                                gungnirSpear.setNoGravity(true);
                            } else {
                                gungnirSpear.setBaseDamage(5F);
                                if (!player.getAbilities().instabuild) {
                                    player.getInventory().removeItem(itemStack);
                                }
                                //itemStack.hurtAndBreak(5, player, (p_43388_) -> p_43388_.broadcastBreakEvent(livingEntity.getUsedItemHand()));
                                int rand = livingEntity.random.nextInt(0, 3);
                                SoundEvent soundEvent;
                                if (rand == 0) {
                                    soundEvent = SoundEvents.TRIDENT_RIPTIDE_3;
                                } else if (rand == 1) {
                                    soundEvent = SoundEvents.TRIDENT_RIPTIDE_2;
                                } else {
                                    soundEvent = SoundEvents.TRIDENT_RIPTIDE_1;
                                }
                                level.playSound(null, player, soundEvent, SoundSource.PLAYERS, 1.0F, 1.0F);

                            }
                            CompoundTag itemTag = itemStack.getOrCreateTag();
                            if (itemTag.hasUUID("TargetID")) {
                                if (level instanceof ServerLevel serverLevel) {
                                    Entity target = serverLevel.getEntity(itemTag.getUUID("TargetID"));
                                    if (target != null && !target.isRemoved()) {
                                        gungnirSpear.setTarget(target);
                                    }
                                }
                            }
                            level.addFreshEntity(gungnirSpear);

                        }
                    }

                    player.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }

    @Override
    public void inventoryTick(@NotNull ItemStack p_41404_, @NotNull Level p_41405_, @NotNull Entity p_41406_, int p_41407_, boolean p_41408_) {
        if (p_41408_ && p_41406_ instanceof Player player) {
            GungnirClient.handTick(player, p_41404_);
            Entity target = GungnirClient.selectEntity;
            if (p_41405_.isClientSide) {
                if (target != null && target.isAlive()) {
                    double fireScale = target.getBoundingBox().getYsize() / 2D;
                    ParticleUtil.addParticleInternal(ModParticleTypes.BIG_FIRE.get(), false, target.getRandomX(0.3D * fireScale), target.getY() + randomSource.nextFloat(), target.getRandomZ(0.3D * fireScale), randomSource.triangle(0D, 0.25D * fireScale), randomSource.nextFloat() * 0.1F + 0.25F * fireScale, randomSource.triangle(0D, 0.25D * fireScale));
                }
            }
            if (target != null && target.isRemoved())
                GungnirClient.selectEntity = null;

        }
    }

    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack itemInHand = player.getItemInHand(hand);
        if (itemInHand.getDamageValue() >= itemInHand.getMaxDamage() - 1) {
            return InteractionResultHolder.fail(itemInHand);
        } else {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemInHand);
        }
    }

    public boolean hurtEnemy(ItemStack p_43390_, @NotNull LivingEntity p_43391_, @NotNull LivingEntity p_43392_) {
        p_43390_.hurtAndBreak(1, p_43392_, (p_43414_) -> p_43414_.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        return true;
    }

    public boolean mineBlock(@NotNull ItemStack itemStack, @NotNull Level level, BlockState blockState, @NotNull BlockPos blockPos, @NotNull LivingEntity living) {
        if ((double) blockState.getDestroySpeed(level, blockPos) != 0.0) {
            itemStack.hurtAndBreak(2, living, (p_43385_) -> p_43385_.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        }

        return true;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        return slot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getAttributeModifiers(slot, stack);
    }

    @Override
    public int getEnchantmentValue(ItemStack stack) {
        return 30;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return super.canApplyAtEnchantingTable(stack, enchantment) || enchantment.category == EnchantmentCategory.TRIDENT || enchantment.category == EnchantmentCategory.WEAPON;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new FontItemExtensions() {
            public static final HumanoidModel.ArmPose GUNGNIR = HumanoidModel.ArmPose.create("GUNGNIR", false, ((model, entity, arm) -> {
                if (arm == HumanoidArm.LEFT) {
                    model.leftArm.xRot = model.leftArm.xRot * 0.5F - (float) Math.PI;
                    model.leftArm.yRot = 0.0F;
                } else {
                    model.rightArm.xRot = model.rightArm.xRot * 0.5F - (float) Math.PI;
                    model.rightArm.yRot = 0.0F;
                }
            }));

            @Override
            public HumanoidModel.@Nullable ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
                if (!itemStack.isEmpty() && entityLiving.getUsedItemHand() == hand && entityLiving.getUseItemRemainingTicks() > 0) {
                    return GUNGNIR;
                }
                return super.getArmPose(entityLiving, hand, itemStack);
            }
        });
    }

    @Override
    public void onAttack(ItemStack itemStack, LivingAttackEvent event) {
        ((LivingEventEC) event).revelationfix$hackedUnCancelable(true);
        ((DamageSourceInterface) event.getSource()).giveSpecialTag((byte) 1);
    }

    @Override
    public void onHurt(ItemStack itemStack, LivingHurtEvent event) {

    }

    @Override
    public void onDamage(ItemStack itemStack, LivingDamageEvent event) {

    }

    @Override
    public void onAttackEntity(ItemStack stack, AttackEntityEvent event) {
        ((LivingEventEC) event).revelationfix$hackedUnCancelable(true);
    }
}
