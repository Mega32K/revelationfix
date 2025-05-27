package com.mega.revelationfix.common.apollyon.client.effect;

import com.mega.revelationfix.util.MUtils;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class CuriosMutableComponent {
    public static final NullComponent NULL = new NullComponent();
    public static final Object[] NULL_ARRAY = new Object[]{NULL};
    public static final CuriosMutableComponent EMPTY = CuriosMutableComponent.create();
    final MutableComponent component;
    public LoreStyle style = LoreStyle.NONE;
    List<Object> content = new ArrayList<>();

    CuriosMutableComponent(MutableComponent component) {
        this.component = component;
    }

    public static CuriosMutableComponent create(MutableComponent component, LoreStyle style) {
        return new CuriosMutableComponent(component).loreStyle(style);
    }

    public static CuriosMutableComponent create() {
        return new CuriosMutableComponent(Component.literal("")).loreStyle(LoreStyle.NONE);
    }

    public static CuriosMutableComponent create(LoreStyle style) {
        return new CuriosMutableComponent(Component.literal("")).loreStyle(style);
    }

    public static List<Component> listBake(List<CuriosMutableComponent> curiosMutableComponents, ItemStack stack) {
        List<Component> list = new ArrayList<>();
        for (CuriosMutableComponent cmc : curiosMutableComponents)
            list.add(cmc.build(stack));
        return list;
    }

    public List<CuriosMutableComponent> bake(List<MutableComponent> components) {
        return MUtils.safelyForEach(components, (Function<MutableComponent, CuriosMutableComponent>) (mutable) -> CuriosMutableComponent.create(mutable, style));
    }

    private CuriosMutableComponent loreStyle(LoreStyle style) {
        this.style = style;
        return this;
    }

    public CuriosMutableComponent appendChild(CuriosMutableComponent child) {
        content.add(child);
        return this;
    }

    public CuriosMutableComponent appendFormat(String format, FormatDescFunction args, boolean onlyClient) {
        content.add(FormatWrapper.wrap(FormatPair.of(format, args)).onlyClient(onlyClient));
        return this;
    }

    public CuriosMutableComponent appendAttributeFormat(int p, FormatDescFunction args, boolean onlyClient) {
        //index 0 -> 属性值CF
        //index 1 -> 属性
        //index 2 -> 属性CF
        //index 3 -> 属性名
        if (p > 0)
            content.add(FormatWrapper.wrap(FormatPair.of("%s+%." + p + "f%% %s%s", args)).onlyClient(onlyClient));
        else content.add(FormatWrapper.wrap(FormatPair.of("%s+%s%% %s%s", args)).onlyClient(onlyClient));
        return this;
    }

    public CuriosMutableComponent appendNegAttributeFormat(int p, FormatDescFunction args, boolean onlyClient) {
        //index 0 -> 属性值CF
        //index 1 -> 属性
        //index 2 -> 属性CF
        //index 3 -> 属性名
        if (p > 0)
            content.add(FormatWrapper.wrap(FormatPair.of("%s-%." + p + "f%% %s%s", args)).onlyClient(onlyClient));
        else content.add(FormatWrapper.wrap(FormatPair.of("%s-%s%% %s%s", args)).onlyClient(onlyClient));
        return this;
    }

    public CuriosMutableComponent appendTranslation(String format, FormatDescFunction args, boolean onlyClient) {
        content.add(FormatWrapper.wrap(TranslationFormatPair.of(format, args)).onlyClient(onlyClient));
        return this;
    }

    public CuriosMutableComponent appendFormat(String format, FormatDescFunction args) {
        return this.appendFormat(format, args, false);
    }

    public CuriosMutableComponent appendAttributeFormat(int p, FormatDescFunction args) {
        return this.appendAttributeFormat(p, args, false);
    }

    public CuriosMutableComponent appendNegAttributeFormat(int p, FormatDescFunction args) {
        return this.appendNegAttributeFormat(p, args, false);
    }

    public CuriosMutableComponent appendTranslation(String format, FormatDescFunction args) {
        return this.appendTranslation(format, args, false);
    }

    public CuriosMutableComponent appendTranslation(String format) {
        return this.appendTranslation(format, null);
    }

    public CuriosMutableComponent appendComponent(Component component) {
        content.add(component);
        return this;
    }

    public MutableComponent build(ItemStack stack) {
        MutableComponent component = this.component.copy();
        component = this.style.getDelegate().call(component);
        Dist dist = FMLEnvironment.dist;
        for (Object o : content) {
            if (o instanceof FormatWrapper formatWrapper) {
                if (formatWrapper.pair instanceof FormatPair pair) {
                    Object[] objects = pair.right() == null ? null : pair.right().apply(stack);
                    if (formatWrapper.canUse(dist)) {
                        if (!(objects != null && objects[0] instanceof NullComponent))
                            component.append(Component.literal(String.format(pair.first(), objects)));
                        else return null;
                    }
                } else if (formatWrapper.pair instanceof TranslationFormatPair pair) {
                    if (formatWrapper.canUse(dist)) {
                        if (pair.right() != null)
                            component.append(Component.translatable(pair.first(), pair.right().apply(stack)));
                        else component.append(Component.translatable(pair.first()));
                    }
                }
            } else if (o instanceof Component c)
                component.append(c);
            else if (o instanceof CuriosMutableComponent child)
                component.append(child.build(stack));
        }
        return component;
    }

    public interface AttributeValueGetter {
        Object getValue(ItemStack stack);
    }

    /**
     * ItemStack 提供实时的物品栈
     * Object[] 提供给格式化的参数
     */
    public interface FormatDescFunction extends Function<ItemStack, Object[]> {
    }

    public interface FormatPair extends Pair<String, FormatDescFunction> {
        static FormatPair of(String s, FormatDescFunction function) {
            return new FormatPair() {
                @Override
                public String left() {
                    return s;
                }

                @Override
                public FormatDescFunction right() {
                    return function;
                }
            };
        }
    }

    public interface TranslationFormatPair extends Pair<String, FormatDescFunction> {
        static TranslationFormatPair of(String s, FormatDescFunction function) {
            return new TranslationFormatPair() {
                @Override
                public String left() {
                    return s;
                }

                @Override
                public FormatDescFunction right() {
                    return function;
                }
            };
        }
    }

    public static class AttributeDescFunction2 implements FormatDescFunction {
        final String attributeName;
        final AttributeValueGetter func2;

        public AttributeDescFunction2(String attributeName, AttributeValueGetter func2) {
            this.func2 = func2;
            this.attributeName = attributeName;
        }

        @Override
        public Object[] apply(ItemStack stack) {
            return new Object[]{LoreHelper.codeMode(ChatFormatting.GOLD), func2.getValue(stack), LoreHelper.codeMode(ChatFormatting.LIGHT_PURPLE), I18n.get(attributeName)};
        }
    }

    public static class FormatWrapper {
        private final Pair<String, FormatDescFunction> pair;
        public boolean onlyClient = false;

        private FormatWrapper(Pair<String, FormatDescFunction> pair) {
            this.pair = pair;
        }

        public static FormatWrapper wrap(Pair<String, FormatDescFunction> pair) {
            return new FormatWrapper(pair);
        }

        public FormatWrapper onlyClient(boolean z) {
            this.onlyClient = z;
            return this;
        }

        public boolean canUse(Dist dist) {
            if (onlyClient)
                return dist != Dist.DEDICATED_SERVER;
            return true;
        }
    }

    static class NullComponent {
    }
}
