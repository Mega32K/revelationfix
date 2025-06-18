package com.mega.revelationfix.common.item.food;

import com.mega.revelationfix.client.font.effect.CuriosMutableComponent;
import com.mega.revelationfix.client.font.effect.LoreHelper;
import com.mega.revelationfix.client.font.effect.LoreStyle;
import com.mega.revelationfix.safe.GRSavedDataEC;
import com.mega.revelationfix.safe.GRSavedDataExpandedContext;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import z1gned.goetyrevelation.data.DefeatApollyonInNetherState;

import java.util.List;

public class AscensionHardCandy extends Item {
    public AscensionHardCandy() {
        super(new Properties().rarity(Rarity.EPIC).food(
                new FoodProperties.Builder()
                        .fast()
                        .alwaysEat()
                        .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 300, 3), 1.0F)
                        .build()
        ));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if (!level.isClientSide) {
            DefeatApollyonInNetherState state = GRSavedDataExpandedContext.state(((ServerLevel) level));
            GRSavedDataExpandedContext context = ((GRSavedDataEC) state).revelationfix$dataEC();
            if (context.getAteAscensions().contains(player.getUUID())) {
                return InteractionResultHolder.pass(player.getItemInHand(hand));
            }
        }
        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<Component> components, @NotNull TooltipFlag tooltipFlag) {
        MutableComponent srcAttribute = Component.translatable("curios.modifiers.slots.plus.0", 1, Component.translatable("curios.identifier.curio"));
        CuriosMutableComponent.FormatDescFunction descFunction = stack1 -> new Object[]{
                LoreHelper.codeMode(ChatFormatting.GOLD),
                1,
                LoreHelper.codeMode(ChatFormatting.LIGHT_PURPLE),
                Component.translatable("curios.identifier.curio").getString(),
                I18n.get("curios.goety_revelation.modifiers.slots")
        };
        super.appendHoverText(itemStack, level, components, tooltipFlag);
        //components.add(CuriosMutableComponent.create().appendComponent(Component.translatable("tooltip.goety_revelation.abyssalStew1")).build(itemStack));
        Component component = CuriosMutableComponent.create(LoreStyle.NONE)
                .appendComponent(Component.translatable("tooltip.goety_revelation.abyssalStew1"))
                .appendChild(CuriosMutableComponent.create().appendFormat(
                        "%s+%s %s%s%s",
                        descFunction
                ))
                .build(itemStack);
        if (component != null)
            components.add(component);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity consumer) {
        if (!level.isClientSide) {
            affectConsumer(stack, level, consumer);
        }
        ItemStack containerStack = stack.getCraftingRemainingItem();
        Player player;
        if (stack.isEdible()) {
            super.finishUsingItem(stack, level, consumer);
        } else {
            player = consumer instanceof Player ? (Player) consumer : null;
            if (player instanceof ServerPlayer) {
                CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer) player, stack);
            }

            if (player != null) {
                player.awardStat(Stats.ITEM_USED.get(this));
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
            }
        }

        if (stack.isEmpty()) {
            return containerStack;
        } else {
            if (consumer instanceof Player) {
                player = (Player) consumer;
                if (!((Player) consumer).getAbilities().instabuild && !player.getInventory().add(containerStack)) {
                    player.drop(containerStack, false);
                }
            }

            return stack;
        }
    }

    public void affectConsumer(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity consumer) {
        if (consumer instanceof Player player) {
            DefeatApollyonInNetherState state = GRSavedDataExpandedContext.state(((ServerLevel) level));
            GRSavedDataExpandedContext context = ((GRSavedDataEC) state).revelationfix$dataEC();
            if (!context.getAteAscensions().contains(player.getUUID())) {
                context.getAteAscensions().add(player.getUUID());
                state.setDirty();
                CuriosApi.getSlotHelper().growSlotType("curio", 1, player);
            }
        }
    }

    @Override
    public boolean isFoil(@NotNull ItemStack p_41453_) {
        return true;
    }
}
