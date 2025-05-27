package com.mega.revelationfix.common.compat.tetra;

import io.netty.util.internal.shaded.org.jctools.util.UnsafeAccess;
import se.mickelus.tetra.gui.stats.getter.IStatGetter;
import se.mickelus.tetra.gui.stats.getter.StatFormat;
import se.mickelus.tetra.gui.stats.getter.TooltipGetterMultiValue;
import sun.misc.Unsafe;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class TetraVersionCompat {
    public static Version version;
    public static Constructor<?> GUI_STAT_BAR_C;
    public static Constructor<?> TOOLTIP_GETTER_MULTI_VALUE_C;

    public static void checkMethods() {
        try {
            //GUI_STAT_BAR_C = GuiStatBar.class.getConstructor(int.class, int.class, int.class, String.class, double.class, double.class, boolean.class, IStatGetter.class, ILabelGetter.class, ITooltipGetter.class);
            TOOLTIP_GETTER_MULTI_VALUE_C = TooltipGetterMultiValue.class.getConstructor(String.class, IStatGetter[].class, StatFormat[].class);
            version = Version.OLD;
        } catch (Throwable throwable) {
            try {
                //GUI_STAT_BAR_C = GuiStatBar.class.getConstructor(int.class, int.class, int.class, String.class, double.class, double.class, boolean.class, IStatGetter.class, ILabelGetter.class, ITooltipGetter.class);

                version = Version.NEW;
            } catch (Throwable throwable1) {
                throwable1.printStackTrace();
                System.exit(-1);
            }
        }
    }

    public static TooltipGetterMultiValue createTGM(String s, IStatGetter[] statGetters, Object... statFormats) throws InstantiationException, IllegalAccessException, NoSuchFieldException {
        checkMethods();
        Unsafe unsafe = UnsafeAccess.UNSAFE;
        Object tgm = unsafe.allocateInstance(TooltipGetterMultiValue.class);
        {
            Field field = TooltipGetterMultiValue.class.getDeclaredField("statGetter");
            field.setAccessible(true);
            field.set(tgm, statGetters);
        }
        {
            Field field = TooltipGetterMultiValue.class.getDeclaredField("formatters");
            field.setAccessible(true);
            unsafe.putObjectVolatile(tgm, unsafe.objectFieldOffset(field), statFormats);
        }
        {
            Field field = TooltipGetterMultiValue.class.getDeclaredField("localizationKey");
            field.setAccessible(true);
            field.set(tgm, s);
        }
        return (TooltipGetterMultiValue) tgm;
    }

    public enum Version {
        OLD, NEW
    }
}
