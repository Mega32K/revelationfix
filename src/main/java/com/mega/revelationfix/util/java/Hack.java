package com.mega.revelationfix.util.java;

import io.netty.util.internal.shaded.org.jctools.util.UnsafeAccess;
import sun.misc.Unsafe;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;

public class Hack {
    private static final long KLASS_OFFSET = 8;
    public static boolean isCompressedOops = true;
    public static Unsafe UNSAFE;
    private static MethodHandles.Lookup IMPL_LOOKUP;

    static {
        try {
            IMPL_LOOKUP = IMPL_LOOKUP();
            int baseOffset = dynamicUnsafe().arrayBaseOffset(Object[].class);
            isCompressedOops = (baseOffset == 12);
            UNSAFE = UnsafeAccess.UNSAFE == null ? dynamicUnsafe() : UnsafeAccess.UNSAFE;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            System.exit(-1);
            Runtime.getRuntime().halt(-1);
        }
    }

    public static MethodHandles.Lookup IMPL_LOOKUP() throws NoSuchFieldException, IllegalAccessException {
        Field f = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
        Field uf = Unsafe.class.getDeclaredField("theUnsafe");
        uf.setAccessible(true);
        Unsafe unsafe = (Unsafe) uf.get(null);
        return (MethodHandles.Lookup) unsafe.getObject(MethodHandles.Lookup.class, unsafe.staticFieldOffset(f));
    }

    public static MethodHandles.Lookup getImplLookup() {
        return IMPL_LOOKUP;
    }

    public static Unsafe dynamicUnsafe() {
        try {
            VarHandle varHandle = getImplLookup().findStaticVarHandle(Unsafe.class, "theUnsafe", Unsafe.class);
            return (Unsafe) varHandle.get();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            System.exit(-1);
            Runtime.getRuntime().halt(-1);
            return null;
        }
    }

    public static long getKlassPointer(Object obj) {
        long objectAddress = UNSAFE.getLong(obj, 0L); // 获取对象地址
        return UNSAFE.getLong(objectAddress + KLASS_OFFSET); // 读取 KlassPtr
    }

    // 替换对象的 KlassPtr
    public static void setKlassPointer(Object obj, long newKlassPtr) {
        long objectAddress = UNSAFE.getLong(obj, 0L);
        UNSAFE.putLong(objectAddress + KLASS_OFFSET, newKlassPtr); // 写入新值
    }

    public static int getKlassPointerCompressed(Object obj) {
        long objectAddress = UNSAFE.getLong(obj, 0L); // 获取对象地址
        return UNSAFE.getInt(objectAddress + KLASS_OFFSET); // 读取 KlassPtr
    }

    // 替换对象的 KlassPtr
    public static void setKlassPointerCompressed(Object obj, int newKlassPtr) {
        long objectAddress = UNSAFE.getLong(obj, 0L);
        UNSAFE.putInt(objectAddress + KLASS_OFFSET, newKlassPtr); // 写入新值
    }
}
