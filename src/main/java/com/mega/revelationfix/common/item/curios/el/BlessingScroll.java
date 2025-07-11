package com.mega.revelationfix.common.item.curios.el;

import com.Polarice3.Goety.utils.CuriosFinder;
import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.items.CursedScroll;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mega.endinglib.api.client.cmc.CuriosMutableComponent;
import com.mega.endinglib.api.client.cmc.LoreStyle;
import com.mega.endinglib.api.item.curios.SimpleDescriptiveCurio;
import com.mega.revelationfix.common.compat.Wrapped;
import com.mega.revelationfix.common.config.ItemConfig;
import com.mega.revelationfix.util.entity.ATAHelper2;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.SlotContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BlessingScroll extends SimpleDescriptiveCurio {

    public BlessingScroll() {
        super(new Properties().stacksTo(1).rarity(Rarity.EPIC), "scroll", () -> {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> multimap = ImmutableMultimap.builder();
            return multimap.build();
        });
        this.withHead(
                CuriosMutableComponent.create().appendAttributeFormat(1, new CuriosMutableComponent.AttributeDescFunction2("attribute.name.generic.attack_damage", (s) -> ItemConfig.bsDamageBoost)),
                CuriosMutableComponent.create().appendAttributeFormat(1, new CuriosMutableComponent.AttributeDescFunction2("attribute.name.generic.attack_speed", (s) -> ItemConfig.bsAttackSpeedBoost)),
                CuriosMutableComponent.create().appendAttributeFormat(1, new CuriosMutableComponent.AttributeDescFunction2("item.goety_revelation.blessing_scroll.attribute0", (s) -> ItemConfig.bsDodgeBoost)),
                CuriosMutableComponent.EMPTY,
                CuriosMutableComponent.create(Component.translatable("item.goety_revelation.blessing_scroll.desc0"), LoreStyle.NONE),
                CuriosMutableComponent.create(Component.translatable("item.goety_revelation.blessing_scroll.desc1"), LoreStyle.NONE)
        ).withTail(
                CuriosMutableComponent.create(LoreStyle.NONE).appendFormat("%s", (o) -> SuperpositionHandler.getCurioStack(Wrapped.clientPlayer(), this) != o ? CuriosMutableComponent.NULL_ARRAY : new Object[]{I18n.get("item.goety_revelation.blessing_scroll.desc2")}),
                CuriosMutableComponent.create().appendAttributeFormat(1, (o) -> SuperpositionHandler.getCurioStack(Wrapped.clientPlayer(), this) != o ? CuriosMutableComponent.NULL_ARRAY : new CuriosMutableComponent.AttributeDescFunction2("attribute.name.generic.attack_damage", (s) -> ItemConfig.bsDamageBoost * getLuckPoints()).apply(o), true),
                CuriosMutableComponent.create().appendAttributeFormat(1, (o) -> SuperpositionHandler.getCurioStack(Wrapped.clientPlayer(), this) != o ? CuriosMutableComponent.NULL_ARRAY : new CuriosMutableComponent.AttributeDescFunction2("attribute.name.generic.attack_speed", (s) -> ItemConfig.bsAttackSpeedBoost * getLuckPoints()).apply(o), true),
                CuriosMutableComponent.create().appendAttributeFormat(1, (o) -> SuperpositionHandler.getCurioStack(Wrapped.clientPlayer(), this) != o ? CuriosMutableComponent.NULL_ARRAY : new CuriosMutableComponent.AttributeDescFunction2("item.goety_revelation.blessing_scroll.attribute0", (s) -> Math.min(ItemConfig.bsMaxDodge, ItemConfig.bsDodgeBoost * getLuckPoints())).apply(o), true)
        );
        MinecraftForge.EVENT_BUS.register(BlessingScroll.CuriosHandler.class);
    }

    public static int getLuckPoints() {
        Player player = Wrapped.clientPlayer();
        if (player != null) {
            AttributeInstance attributeInstance = player.getAttribute(Attributes.LUCK);
            return attributeInstance != null ? (int) attributeInstance.getValue() : 0;
        } else return 0;
    }

    @Override
    public List<Component> getAttributesTooltip(List<Component> tooltips, ItemStack stack) {
        List<Component> list = new ArrayList<>();
        list.add(CuriosMutableComponent.create().appendAttributeFormat(0, new CuriosMutableComponent.AttributeDescFunction2(I18n.get("attribute.name.generic.luck"), (s) -> 1)).build(stack));
        list.add(Component.empty());
        list.addAll(tooltips);
        return super.getAttributesTooltip(list, stack);
    }

    @Override
    public SimpleDescriptiveCurio.ShowMoreType shiftShowType() {
        return super.shiftShowType();
    }

    @Override
    public boolean enableShiftShowMore(ItemStack stack) {
        return true;
    }

    @Override
    public boolean showAttributeTooltips(ItemStack stack) {
        return false;
    }

    @Override
    public boolean showHead(List<Component> components, ItemStack stack) {
        showTitle = false;
        return super.showHead(components, stack) && Screen.hasShiftDown();
    }

    @Override
    public boolean enableSimpleDesc() {
        return false;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (slotIdentifier.equals(slotContext.identifier()) || slotContext.identifier().equals("curio")) {
            HashMultimap<Attribute, AttributeModifier> multimap = HashMultimap.create();
            multimap.put(Attributes.LUCK, new AttributeModifier(UUID.fromString("d9062cdd-3824-440e-b494-8e12074f02e9"), "Curios modifier", 1.0D, AttributeModifier.Operation.ADDITION));
            try {
                if (slotContext.entity() != null) {
                    int luckPoints = (int) slotContext.entity().getAttributeValue(Attributes.LUCK);
                    multimap.put(Attributes.ATTACK_SPEED, new AttributeModifier(UUID.fromString("6cc37aff-6609-46d1-a1b0-a4895b6655fd"), "Curios modifier", luckPoints * ItemConfig.bsAttackSpeedBoost / 100.0F, AttributeModifier.Operation.MULTIPLY_TOTAL));
                }
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            return multimap;
        } else return ImmutableMultimap.of();
    }

    public boolean canEquip(SlotContext context, ItemStack stack) {
        LivingEntity livingEntity = context.entity();
        if (livingEntity instanceof Player player && super.canEquip(context, stack)) {
            return !CuriosFinder.hasCurio(player, (s) -> s.getItem() instanceof CursedScroll);
        }
        return false;
    }

    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        Map<Enchantment, Integer> list = EnchantmentHelper.getEnchantments(book);
        return list.size() == 1 && list.containsKey(Enchantments.BINDING_CURSE) || super.isBookEnchantable(stack, book);
    }

    public static class CuriosHandler {
        public static UUID ATTACK_SPEED_ID = UUID.fromString("6cc37aff-6609-46d1-a1b0-a4895b6655fd");

        @SubscribeEvent
        public static void livingHurt(LivingHurtEvent event) {
            if (event.getSource().getEntity() instanceof Player player) {
                if (ATAHelper2.hasBlessingScroll(player)) {
                    float scale = 1F;
                    int luckPoints = (int) player.getAttributeValue(Attributes.LUCK);
                    scale += luckPoints * ItemConfig.bsDamageBoost / 100.0F;
                    event.setAmount(event.getAmount() * scale);
                }
            }
        }

        @SubscribeEvent
        public static void livingAttack(LivingAttackEvent event) {
            if (event.getEntity() instanceof Player player) {
                if (ATAHelper2.hasBlessingScroll(player)) {
                    if (event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY)) return;
                    int luckPoints = (int) player.getAttributeValue(Attributes.LUCK);
                    float probability = (float) Math.min(ItemConfig.bsMaxDodge / 100D, luckPoints * ItemConfig.bsDodgeBoost / 100.0F);
                    if (player.random.nextFloat() < probability)
                        event.setCanceled(true);
                }
            }
        }

        @SubscribeEvent
        public static void playerTick(TickEvent.PlayerTickEvent event) {
            if (event.phase == TickEvent.Phase.END) {
                Player player = event.player;
                if (player.tickCount % 20 == 0) {
                    AttributeInstance attributeInstance = player.getAttribute(Attributes.ATTACK_SPEED);
                    if (attributeInstance != null) {
                        int luckPoints = (int) player.getAttributeValue(Attributes.LUCK);
                        if (ATAHelper2.hasBlessingScroll(player)) {
                            float amount = (float) (luckPoints * ItemConfig.bsAttackSpeedBoost / 100.0F);
                            AttributeModifier now = attributeInstance.getModifier(ATTACK_SPEED_ID);
                            if (now == null) {
                                attributeInstance.addModifier(new AttributeModifier(ATTACK_SPEED_ID, "Curios modifier", amount, AttributeModifier.Operation.MULTIPLY_TOTAL));
                            } else if (Math.abs(now.getAmount() - amount) > 0.01F) {
                                attributeInstance.removeModifier(ATTACK_SPEED_ID);
                                attributeInstance.addModifier(new AttributeModifier(ATTACK_SPEED_ID, "Curios modifier", amount, AttributeModifier.Operation.MULTIPLY_TOTAL));
                            }
                        } else attributeInstance.removeModifier(ATTACK_SPEED_ID);
                    }

                }
            }
        }
    }
}
