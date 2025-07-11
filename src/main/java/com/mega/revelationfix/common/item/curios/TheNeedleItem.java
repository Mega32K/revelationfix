package com.mega.revelationfix.common.item.curios;

import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.CuriosFinder;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mega.endinglib.api.client.cmc.CuriosMutableComponent;
import com.mega.endinglib.api.client.cmc.LoreStyle;
import com.mega.endinglib.api.item.curios.SimpleDescriptiveCurio;
import com.mega.revelationfix.client.font.effect.LoreHelper;
import com.mega.revelationfix.common.compat.Wrapped;
import com.mega.revelationfix.common.config.ItemConfig;
import com.mega.revelationfix.common.data.TimeStopSavedData;
import com.mega.revelationfix.common.init.ModAttributes;
import com.mega.revelationfix.common.network.PacketHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotAttribute;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class TheNeedleItem extends SimpleDescriptiveCurio {
    public static final float RESISTANCE_DIV = 3F;
    public static final String IS_REAL_NBT = "isRealNeedle";
    public static AttributeModifier HEALTH = new AttributeModifier(UUID.fromString("1b242614-c859-4088-bff7-b2809f9bbe18"), "Needle modifier", 0F, AttributeModifier.Operation.MULTIPLY_BASE);
    public static AttributeModifier ATTACK_DAMAGE = new AttributeModifier(UUID.fromString("9f2e7534-e108-4c75-b71c-64c3b407f39b"), "Needle modifier", 0F, AttributeModifier.Operation.MULTIPLY_BASE);
    public static AttributeModifier ATTACK_SPEED = new AttributeModifier(UUID.fromString("44a1da2a-8219-49cf-a523-8b710f0464a1"), "Needle modifier", 0F, AttributeModifier.Operation.MULTIPLY_BASE);
    public static AttributeModifier RESISTANCE = new AttributeModifier(UUID.fromString("af236f3f-f82a-4008-97f9-5af0173b2ec9"), "Needle modifier", 0F, AttributeModifier.Operation.MULTIPLY_BASE);

    public TheNeedleItem(Properties p_41383_) {
        super(p_41383_, "hands", () -> {
            HashMultimap<Attribute, AttributeModifier> multimap = HashMultimap.create();
            multimap.clear();
            return multimap;
        });
        this.withHead(
                CuriosMutableComponent.create().appendFormat("%s", (stack) -> isFake(stack) ? CuriosMutableComponent.NULL_ARRAY : new Object[]{I18n.get("item.goety_revelation.the_needle.real_desc0")}),
                CuriosMutableComponent.create().appendFormat("%s", (stack) -> isFake(stack) ? CuriosMutableComponent.NULL_ARRAY : new Object[]{I18n.get("item.goety_revelation.the_needle.real_desc1", I18n.get("attribute.name.generic.max_health"), I18n.get("attribute.name.generic.attack_damage"), I18n.get("attribute.name.generic.attack_speed"), I18n.get("attribute.name.goety_revelation.resistance"))}),
                CuriosMutableComponent.create().appendFormat("%s", (stack) -> isFake(stack) ? CuriosMutableComponent.NULL_ARRAY : new Object[]{I18n.get("item.goety_revelation.the_needle.real_desc2", String.format("%.2f%%", ItemConfig.needleAVMin * 100F), String.format("%.2f%%", ItemConfig.needleAVMax * 100F))}),
                CuriosMutableComponent.create().appendFormat("%s", (stack) -> isFake(stack) ? CuriosMutableComponent.NULL_ARRAY : new Object[]{I18n.get("item.goety_revelation.the_needle.real_desc3", I18n.get("attribute.name.goety_revelation.resistance"), String.format("%.2f%%", ItemConfig.needleAVMin / RESISTANCE_DIV * 100F), String.format("%.2f%%", ItemConfig.needleAVMax / RESISTANCE_DIV * 100F))}),
                CuriosMutableComponent.create().appendFormat("%s", (stack) -> isFake(stack) ? CuriosMutableComponent.NULL_ARRAY : new Object[]{I18n.get("item.goety_revelation.the_needle.real_desc4", 14)}),
                CuriosMutableComponent.create().appendFormat("%s", (stack) -> isFake(stack) ? CuriosMutableComponent.NULL_ARRAY : new Object[]{I18n.get("item.goety_revelation.the_needle.real_desc5", stack.hasTag() ? 14 - stack.getOrCreateTag().getInt("usedCount") : 14)}),
                CuriosMutableComponent.create().appendFormat("%s", (stack) -> isFake(stack) ? CuriosMutableComponent.NULL_ARRAY : new Object[]{""}),
                CuriosMutableComponent.create().appendTranslation("item.goety_revelation.the_needle.desc0")
        ).withTail(
                CuriosMutableComponent.create().appendTranslation("item.goety_revelation.the_needle.tail0"),
                CuriosMutableComponent.create().appendTranslation("item.goety_revelation.the_needle.tail1")
        );
    }

    public static boolean isFake(ItemStack stack) {
        return (!stack.hasTag() || !stack.getOrCreateTag().getBoolean(IS_REAL_NBT));
    }

    private static double modifier(AttributeModifier modifier) {
        return modifier == null ? 0.0F : modifier.getAmount();
    }

    public static double increaseAmount() {
        return ItemConfig.needleAVMin + (ItemConfig.needleAVMax - ItemConfig.needleAVMin) * Math.random();
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return super.canEquip(slotContext, stack) && !CuriosFinder.hasCurio(slotContext.entity(), this);
    }

    @Override
    public boolean enableSimpleDesc() {
        return false;
    }

    @Override
    public boolean isFoil(@NotNull ItemStack p_41453_) {
        return !isFake(p_41453_);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (defaultModifiers.isEmpty()) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> multimap = ImmutableMultimap.builder();
            multimap.put(SlotAttribute.getOrCreate("hands"), new AttributeModifier(UUID.fromString("807bd36c-c1a2-4d95-8f9e-511de9e7355d"), "Curios modifier", 1.0D, AttributeModifier.Operation.ADDITION));
            multimap.put(ModAttributes.ARMOR_PENETRATION.get(), new AttributeModifier(UUID.fromString("d1041913-ddb7-4dba-b90a-19a605fd32c0"), "Curios modifier", ItemConfig.needleArmorPenetration, AttributeModifier.Operation.MULTIPLY_BASE));
            multimap.put(ModAttributes.ENCHANTMENT_PIERCING.get(), new AttributeModifier(UUID.fromString("938f8fdb-7091-4853-9999-880bb30c45ae"), "Curios modifier", ItemConfig.needleEnchantmentPiercing, AttributeModifier.Operation.MULTIPLY_BASE));
            defaultModifiers = multimap.build();
        }
        return super.getAttributeModifiers(slotContext, uuid, stack);
    }

    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack itemStack) {
        return UseAnim.SPEAR;
    }

    public int getUseDuration(@NotNull ItemStack p_43107_) {
        return 72000;
    }

    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (isFake(itemstack))
            return InteractionResultHolder.pass(itemstack);
        player.startUsingItem(hand);
        player.playSound(ModSounds.ALTAR_START.get(), 1F, (float) player.random.triangle(1F, 0.1F));
        return InteractionResultHolder.consume(itemstack);
    }

    @Override
    public void releaseUsing(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull LivingEntity living, int left) {
        if (living instanceof ServerPlayer player && !level.isClientSide) {
            String tag0 = "usedCount";
            CompoundTag tag = itemStack.getOrCreateTag();
            int usedCount = tag.getInt(tag0);
            TimeStopSavedData timeStopSavedData = TimeStopSavedData.readOrCreate(player.server);
            int playerNeedleTimes = timeStopSavedData.getPlayerNeedleTimes(player);
            if (usedCount < 14 && playerNeedleTimes < 14) {
                int duration = getUseDuration(itemStack) - left;
                if (duration < 20) return;
                tag.putInt(tag0, usedCount + 1);
                usedCount++;
                {
                    float random = player.random.nextFloat();
                    int index;
                    //                           health  damage  attackSpd resistance
                    float[] base = new float[]{0.25F, 0.25F, 0.25F, 0.25F};
                    float luck = Mth.clamp(player.getLuck(), 0F, 20.0F) / 20.0F;
                    float[] weight;
                    try {
                        weight = new float[]{ItemConfig.needleAttributeWeight.get(0), ItemConfig.needleAttributeWeight.get(1), ItemConfig.needleAttributeWeight.get(2), ItemConfig.needleAttributeWeight.get(3)};
                    } catch (Throwable throwable) {
                        weight = new float[]{0.12f, 0.12f, 0.4f, 3.5f};
                    }
                    float[] adjusted = new float[4];
                    float total = 0F;
                    for (int i = 0; i < 4; i++) {
                        adjusted[i] = Math.max(0, base[i] + luck * weight[i]);
                        total += adjusted[i];
                    }
                    if (total <= 0) {
                        total = 1F;
                        adjusted[0] = adjusted[1] = adjusted[2] = adjusted[3] = base[0];
                    }
                    for (int i = 0; i < 4; i++) {
                        adjusted[i] = adjusted[i] / total;
                    }
                    float cumulative = 0;
                    for (index = 0; index < 4; index++) {
                        cumulative += adjusted[index];
                        if (random < cumulative) {
                            break;
                        }
                    }
                    Attribute attribute;
                    AttributeInstance attributeInstance;

                    if (index == 0) {
                        attribute = Attributes.MAX_HEALTH;
                        attributeInstance = player.getAttribute(attribute);
                        if (attributeInstance != null) {
                            AttributeModifier old = attributeInstance.getModifier(HEALTH.getId());
                            if (old != null) {
                                attributeInstance.removeModifier(old);
                                HEALTH = new AttributeModifier(old.getId(), old.getName(), old.getAmount() + increaseAmount(), old.getOperation());
                                attributeInstance.addPermanentModifier(HEALTH);
                            } else {
                                HEALTH = new AttributeModifier(HEALTH.getId(), HEALTH.getName(), increaseAmount(), HEALTH.getOperation());
                                attributeInstance.addPermanentModifier(HEALTH);
                            }
                        }
                    } else if (index == 1) {
                        attribute = Attributes.ATTACK_DAMAGE;
                        attributeInstance = player.getAttribute(attribute);
                        if (attributeInstance != null) {
                            AttributeModifier old = attributeInstance.getModifier(ATTACK_DAMAGE.getId());
                            if (old != null) {
                                attributeInstance.removeModifier(old);
                                ATTACK_DAMAGE = new AttributeModifier(old.getId(), old.getName(), old.getAmount() + increaseAmount(), old.getOperation());
                                attributeInstance.addPermanentModifier(ATTACK_DAMAGE);
                            } else {
                                ATTACK_DAMAGE = new AttributeModifier(ATTACK_DAMAGE.getId(), ATTACK_DAMAGE.getName(), increaseAmount(), ATTACK_DAMAGE.getOperation());
                                attributeInstance.addPermanentModifier(ATTACK_DAMAGE);
                            }
                        }
                        player.attributes.getDirtyAttributes().add(attributeInstance);
                    } else if (index == 2) {
                        attribute = Attributes.ATTACK_SPEED;
                        attributeInstance = player.getAttribute(attribute);
                        if (attributeInstance != null) {
                            AttributeModifier old = attributeInstance.getModifier(ATTACK_SPEED.getId());
                            if (old != null) {
                                attributeInstance.removeModifier(old);
                                ATTACK_SPEED = new AttributeModifier(old.getId(), old.getName(), old.getAmount() + increaseAmount(), old.getOperation());
                                attributeInstance.addPermanentModifier(ATTACK_SPEED);
                            } else {
                                ATTACK_SPEED = new AttributeModifier(ATTACK_SPEED.getId(), ATTACK_SPEED.getName(), increaseAmount(), ATTACK_SPEED.getOperation());
                                attributeInstance.addPermanentModifier(ATTACK_SPEED);
                            }
                        }
                    } else if (index == 3) {
                        attribute = ModAttributes.DAMAGE_RESISTANCE.get();
                        attributeInstance = player.getAttribute(attribute);
                        if (attributeInstance != null) {
                            AttributeModifier old = attributeInstance.getModifier(RESISTANCE.getId());
                            if (old != null) {
                                attributeInstance.removeModifier(old);
                                RESISTANCE = new AttributeModifier(old.getId(), old.getName(), old.getAmount() + increaseAmount() / RESISTANCE_DIV, old.getOperation());
                                attributeInstance.addPermanentModifier(RESISTANCE);
                            } else {
                                RESISTANCE = new AttributeModifier(RESISTANCE.getId(), RESISTANCE.getName(), increaseAmount() / RESISTANCE_DIV, RESISTANCE.getOperation());
                                attributeInstance.addPermanentModifier(RESISTANCE);
                            }
                        }
                    }
                    timeStopSavedData.setPlayerNeedleTimes(player, playerNeedleTimes + 1);
                }
                PacketHandler.playSound(player, ModSounds.ABYSS_PREPARE_SPELL.get(), SoundSource.PLAYERS, 1F, (float) living.random.triangle(1F, 0.15F));
                PacketHandler.playSound(player, ModSounds.BIOMINE_SPAWN.get(), SoundSource.PLAYERS, 1F, (float) living.random.triangle(1F, 0.15F));
                player.hurt(player.damageSources().indirectMagic(player, player), player.getHealth() / 2.0F);
                player.getCooldowns().addCooldown(itemStack.getItem(), 20);
                if (usedCount == 14) {
                    tag.putBoolean(IS_REAL_NBT, false);
                    ItemStack stack = this.getDefaultInstance();
                    stack.readShareTag(tag);
                    player.setItemInHand(player.getUsedItemHand(), stack);
                }
            }
        }
    }

    @Override
    public boolean enableShiftShowMore(ItemStack stack) {
        return true;
    }

    @Override
    public ShowMoreType shiftShowType() {
        return super.shiftShowType();
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public void appendShiftShowMore(ItemStack stack, List<Component> components) {
        Player player = Wrapped.clientPlayer();
        if (player == null) return;
        components.clear();

        try {
            components.add(CuriosMutableComponent.create(Component.literal(
                    String.format(
                            "%s+%.2f%% %s%s",
                            LoreHelper.codeMode(ChatFormatting.GOLD),
                            (modifier(player.getAttribute(Attributes.MAX_HEALTH).getModifier(TheNeedleItem.HEALTH.getId()))) * 100.0F,
                            LoreHelper.codeMode(ChatFormatting.LIGHT_PURPLE),
                            I18n.get("attribute.name.generic.max_health")
                    )), LoreStyle.ATTRIBUTE_PREFIX).build(stack));
            components.add(CuriosMutableComponent.create(Component.literal(
                    String.format(
                            "%s+%.2f%% %s%s",
                            LoreHelper.codeMode(ChatFormatting.GOLD),
                            (modifier(player.getAttribute(Attributes.ATTACK_DAMAGE).getModifier(ATTACK_DAMAGE.getId()))) * 100.0F,
                            LoreHelper.codeMode(ChatFormatting.LIGHT_PURPLE),
                            I18n.get("attribute.name.generic.attack_damage")
                    )), LoreStyle.ATTRIBUTE_PREFIX).build(stack));
            components.add(CuriosMutableComponent.create(Component.literal(
                    String.format(
                            "%s+%.2f%% %s%s",
                            LoreHelper.codeMode(ChatFormatting.GOLD),
                            (modifier(player.getAttribute(Attributes.ATTACK_SPEED).getModifier(TheNeedleItem.ATTACK_SPEED.getId()))) * 100.0F,
                            LoreHelper.codeMode(ChatFormatting.LIGHT_PURPLE),
                            I18n.get("attribute.name.generic.attack_speed")
                    )), LoreStyle.ATTRIBUTE_PREFIX).build(stack));
            components.add(CuriosMutableComponent.create(Component.literal(
                    String.format(
                            "%s+%.2f%% %s%s",
                            LoreHelper.codeMode(ChatFormatting.GOLD),
                            (modifier(player.getAttribute(ModAttributes.DAMAGE_RESISTANCE.get()).getModifier(TheNeedleItem.RESISTANCE.getId()))) * 100.0F,
                            LoreHelper.codeMode(ChatFormatting.LIGHT_PURPLE),
                            I18n.get("attribute.name.goety_revelation.resistance")
                    )), LoreStyle.ATTRIBUTE_PREFIX).build(stack));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public boolean canRightClickEquip(ItemStack stack) {
        return false;
    }
}
