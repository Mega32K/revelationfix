package com.mega.revelationfix.common.item.curios;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mega.revelationfix.common.apollyon.client.effect.CuriosMutableComponent;
import com.mega.revelationfix.common.apollyon.client.effect.LoreHelper;
import com.mega.revelationfix.common.apollyon.client.effect.LoreStyle;
import com.mega.revelationfix.common.compat.Wrapped;
import com.mega.revelationfix.api.item.ICanCurse;
import com.mega.revelationfix.util.MUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.*;
import java.util.function.Supplier;

public class SimpleDescriptiveCurio extends CuriosBaseItem implements ICurioItem, Vanishable, ICanCurse {
    protected final @NotNull String slotIdentifier;
    public List<Component> tooltips = new ArrayList<>();
    protected Multimap<Attribute, AttributeModifier> defaultModifiers;
    protected boolean showHeader;
    protected boolean showTitle = true;
    Style descriptionStyle;
    List<CuriosMutableComponent> defaultDesc = new ArrayList<>();
    List<CuriosMutableComponent> head = new ArrayList<>();
    List<CuriosMutableComponent> tail = new ArrayList<>();
    List<Component> toArg = new ArrayList<>();
    private List<CuriosMutableComponent> tempAttributeList = new ArrayList<>();

    public SimpleDescriptiveCurio(Properties properties, String slotIdentifier, Multimap<Attribute, AttributeModifier> defaultModifiers) {
        super(properties);
        this.slotIdentifier = Objects.requireNonNull(slotIdentifier);
        this.showHeader = true;
        this.descriptionStyle = Style.EMPTY.withColor(ChatFormatting.YELLOW);
        this.defaultModifiers = defaultModifiers;
    }

    public SimpleDescriptiveCurio(Properties properties, String slotIdentifier, Supplier<Multimap<Attribute, AttributeModifier>> defaultModifiers) {
        this(properties, slotIdentifier, defaultModifiers.get());
    }

    public SimpleDescriptiveCurio(Properties properties, String slotIdentifier) {
        this(properties, slotIdentifier, ImmutableMultimap::of);
    }

    static ChatFormatting textColorToCF(TextColor color) {
        return ChatFormatting.getByName(color.serialize());
    }

    public List<Component> getSlotsTooltip(List<Component> tooltips, ItemStack stack) {
        MutableComponent title = Component.translatable("curios.modifiers." + this.slotIdentifier).withStyle(ChatFormatting.GOLD);
        List<? extends Component> headList = this.getHeadDescriptionLines(stack);
        if (!headList.isEmpty() && showHead(tooltips, stack)) {
            tooltips.add(Component.empty());
            tooltips.addAll(headList);
        }
        boolean isEnableShift = enableShiftShowMore(stack);
        boolean isEnableAlt = enableAltShowMore(stack);
        if ((!isEnableShift || !Screen.hasShiftDown()) && (!isEnableAlt || !Screen.hasAltDown()))
            if (this.showHeader && showTitle) {
                tooltips.add(Component.empty());
                tooltips.add(title);
            }
        List<? extends Component> descriptionLines = this.getDescriptionLines(stack);
        if (!descriptionLines.isEmpty()) {
            tooltips.addAll(descriptionLines);
        }

        return super.getSlotsTooltip(tooltips, stack);
    }

    public boolean showHead(List<Component> components, ItemStack stack) {
        return true;
    }

    public boolean showTail(List<Component> components, ItemStack stack) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, level, components, tooltipFlag);
    }

    @Override
    public List<Component> getAttributesTooltip(List<Component> tooltips, ItemStack stack) {
        try {
            if (tempAttributeList == null || (Wrapped.clientPlayer() != null)) {
                if (showAttributeTooltips(stack))
                    tempAttributeList = MUtils.safelyForEach(tooltips, (component) -> {
                        if (!component.equals(Component.empty())
                                && component instanceof MutableComponent mutableComponent
                                && mutableComponent.getContents() instanceof TranslatableContents contents) {
                            if (!I18n.exists(contents.getKey())) return null;
                            if (contents.getArgs().length != 2)
                                return null;
                            String key = contents.getKey();
                            if (contents.getArgs()[1] instanceof Component cArg) {
                                CuriosMutableComponent.FormatDescFunction descFunction = stack1 -> new Object[]{
                                        LoreHelper.codeMode(ChatFormatting.GOLD),
                                        Float.valueOf(contents.getArgs()[0].toString()),
                                        (cArg instanceof MutableComponent mcArg && mcArg.getStyle().getColor() != null && 16777215 != (mcArg.getStyle().getColor().getValue())) ? LoreHelper.codeMode(textColorToCF(mcArg.getStyle().getColor())) : LoreHelper.codeMode(ChatFormatting.LIGHT_PURPLE),
                                        I18n.get(cArg.getContents() instanceof TranslatableContents tC ? tC.getKey() : "")
                                };
                                CuriosMutableComponent.FormatDescFunction descFunction2 = stack1 -> new Object[]{
                                        LoreHelper.codeMode(showHeader ? ChatFormatting.LIGHT_PURPLE : ChatFormatting.GOLD),
                                        Integer.valueOf(contents.getArgs()[0].toString()),
                                        (cArg instanceof MutableComponent mcArg && mcArg.getStyle().getColor() != null && 16777215 != (mcArg.getStyle().getColor().getValue())) ? LoreHelper.codeMode(textColorToCF(mcArg.getStyle().getColor())) : LoreHelper.codeMode(showHeader ? ChatFormatting.GOLD : ChatFormatting.LIGHT_PURPLE),
                                        I18n.get(cArg.getContents() instanceof TranslatableContents tC ? tC.getKey() : ""),
                                        I18n.get("curios.goety_revelation.modifiers.slots")
                                };
                                if (key.endsWith(".plus.1") || key.endsWith(".plus.2")) {
                                    return CuriosMutableComponent.create(LoreStyle.ATTRIBUTE_PREFIX).appendAttributeFormat(
                                            1,
                                            descFunction
                                    );
                                }
                                if (key.endsWith("modifier.take.0")) {
                                    return CuriosMutableComponent.create(LoreStyle.ATTRIBUTE_PREFIX).appendFormat(
                                            "%s-%s %s%s",
                                            descFunction
                                    );
                                } else if (key.endsWith("modifier.plus.0")) {
                                    return CuriosMutableComponent.create(LoreStyle.ATTRIBUTE_PREFIX).appendFormat(
                                            "%s+%s %s%s",
                                            descFunction
                                    );
                                }
                                if (key.equals("curios.modifiers.slots.plus.0")) {
                                    return CuriosMutableComponent.create(LoreStyle.NONE).appendFormat(
                                            "%s+%s %s%s%s",
                                            descFunction2
                                    );
                                }
                                if (key.endsWith(".take.1") || key.endsWith(".take.2")) {
                                    return CuriosMutableComponent.create(LoreStyle.ATTRIBUTE_PREFIX).appendNegAttributeFormat(
                                            1,
                                            descFunction
                                    );
                                }
                                if (key.endsWith("curios.modifiers.slots.take.0")) {
                                    return CuriosMutableComponent.create(LoreStyle.NONE).appendFormat(
                                            "%s-%s %s%s%s",
                                            descFunction2
                                    );
                                }
                            }
                        }
                        return null;
                    });
                else tempAttributeList = new ArrayList<>();
                if (toArg == null) toArg = new ArrayList<>();
                else toArg.clear();
                boolean isEnableShift = enableShiftShowMore(stack);
                boolean isEnableAlt = enableAltShowMore(stack);
                if ((!isEnableShift || !Screen.hasShiftDown()) && (!isEnableAlt || !Screen.hasAltDown()))
                    toArg.addAll(tempAttributeList.stream().map(cmc -> cmc.build(stack)).filter(Objects::nonNull).toList());
                if (isEnableShift && !Screen.hasShiftDown() && (!isEnableAlt || !Screen.hasAltDown()))
                    toArg.add(shiftShowType() == ShowMoreType.SPECIAL_EFFECTS ? Component.translatable("tooltip.revelationfix.holdShiftEffect") : Component.translatable("tooltip.revelationfix.holdShift"));
                if (isEnableAlt && !Screen.hasAltDown() && (!isEnableShift || !Screen.hasShiftDown()))
                    toArg.add(altShowType() == ShowMoreType.SPECIAL_EFFECTS ? Component.translatable("tooltip.revelationfix.holdAltEffect") : Component.translatable("tooltip.revelationfix.holdAlt"));
                if (isEnableShift && Screen.hasShiftDown()) {
                    this.appendShiftShowMore(stack, toArg);
                } else if (isEnableAlt && Screen.hasAltDown()) {
                    this.appendAltShowMore(stack, toArg);
                }
                List<MutableComponent> tailList = this.getTailDescriptionLines(stack);
                if (!tailList.isEmpty() && showTail(tooltips, stack)) {
                    toArg.add(Component.empty());
                    toArg.addAll(tailList);
                }
            }
            return super.getAttributesTooltip(toArg, stack);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return tooltips;
    }

    public void appendShiftShowMore(ItemStack stack, List<Component> components) {

    }

    public void appendAltShowMore(ItemStack stack, List<Component> components) {

    }

    public boolean enableShiftShowMore(ItemStack stack) {
        return false;
    }

    public boolean enableAltShowMore(ItemStack stack) {
        return false;
    }

    public ShowMoreType shiftShowType() {
        return ShowMoreType.CONTEXT;
    }

    public final ShowMoreType altShowType() {
        return this.shiftShowType() == ShowMoreType.CONTEXT ? ShowMoreType.SPECIAL_EFFECTS : ShowMoreType.CONTEXT;
    }

    public boolean showAttributeTooltips(ItemStack stack) {
        return true;
    }

    @Override
    public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
        super.onInventoryTick(stack, level, player, slotIndex, selectedIndex);
    }

    public List<? extends Component> getDescriptionLines(ItemStack stack) {
        if (enableSimpleDesc())
            return List.of(this.getDescription(stack));
        else return defaultDesc.stream().map(cmc -> cmc.build(stack)).filter(Objects::nonNull).toList();
    }

    public List<MutableComponent> getHeadDescriptionLines(ItemStack stack) {
        if (head.isEmpty()) return List.of();
        return head.stream().map(cmc -> cmc.build(stack)).filter(Objects::nonNull).toList();
    }

    public List<MutableComponent> getTailDescriptionLines(ItemStack stack) {
        if (tail.isEmpty())
            return List.of();
        else
            return tail.stream().map(cmc -> cmc.build(stack)).filter(Objects::nonNull).toList();

    }

    public Component getDescription(ItemStack stack) {
        return Component.literal(" ").append(Component.translatable(this.getDescriptionId() + ".desc")).withStyle(this.descriptionStyle);
    }

    public boolean enableSimpleDesc() {
        return true;
    }

    protected Item withHead(List<CuriosMutableComponent> head) {
        this.head = head;
        return this;
    }

    protected Item withTail(List<CuriosMutableComponent> tail) {
        this.tail = tail;
        return this;
    }

    protected Item defaultDesc(List<CuriosMutableComponent> defaultDesc) {
        this.defaultDesc = defaultDesc;
        return this;
    }

    public SimpleDescriptiveCurio withHead(CuriosMutableComponent... head) {
        this.head = List.of(head);
        return this;
    }

    public SimpleDescriptiveCurio withTail(CuriosMutableComponent... tail) {
        this.tail = List.of(tail);
        return this;
    }

    public SimpleDescriptiveCurio defaultDesc(CuriosMutableComponent... defaultDesc) {
        this.defaultDesc = List.of(defaultDesc);
        return this;
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (slotIdentifier.equals("curio")) {
            if (slotContext.identifier().equals("hands")) {
                return defaultModifiers;
            }
        }
        return (slotIdentifier.equals(slotContext.identifier()) || slotContext.identifier().equals("curio")) ? defaultModifiers : ImmutableMultimap.of();
    }

    @Override
    public boolean isEnchantable(ItemStack p_41456_) {
        return true;
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return !CuriosApi.getCuriosHelper().findEquippedCurio(this, slotContext.entity()).isPresent();
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        Map<Enchantment, Integer> list = EnchantmentHelper.getEnchantments(book);
        return list.size() == 1 && list.containsKey(Enchantments.BINDING_CURSE) || super.isBookEnchantable(stack, book);
    }

    public enum ShowMoreType {
        SPECIAL_EFFECTS,
        CONTEXT
    }
}
