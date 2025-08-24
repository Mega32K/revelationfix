package com.mega.revelationfix.util;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import sun.misc.Unsafe;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class ClassHandler {
    public static Class<?> MEMEBER_NAME;
    public static MethodHandle MEMBER_NAME_COS;
    public static Map<Class<?>, List<Class<?>>> parentsMapping = new Object2ObjectOpenHashMap<>();
    public static Map<Class<?>, Map<Predicate<Field>, List<FieldVarHandle>>> bigFilter = new Object2ObjectOpenHashMap<>();

    static {
        try {
            MEMEBER_NAME = IMPL_LOOKUP().findClass("java.lang.invoke.MemberName");
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        try {
            MEMBER_NAME_COS = IMPL_LOOKUP().findConstructor(MEMEBER_NAME, MethodType.methodType(void.class, Field.class, boolean.class));
        } catch (NoSuchMethodException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

    public static <T> T newInstance(Class<T> clazz) {
        try {
            Constructor<T> method = clazz.getConstructor();
            method.setAccessible(true);
            return method.newInstance();
        } catch (Throwable throwable) {
            return null;
        }
    }

    public static MethodHandles.Lookup IMPL_LOOKUP() throws NoSuchFieldException, IllegalAccessException {
        //return (MethodHandles.Lookup) getDsDamage(MethodHandles.Lookup.class, "IMPL_LOOKUP", MethodHandles.Lookup.class);
        Field f = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
        Field uf = Unsafe.class.getDeclaredField("theUnsafe");
        uf.setAccessible(true);
        Unsafe unsafe = (Unsafe) uf.get(null);
        return (MethodHandles.Lookup) unsafe.getObject(MethodHandles.Lookup.class, unsafe.staticFieldOffset(f));
        //long offset = UnsafeUtil.getUnsafe().staticFieldOffset(f);
        //return (MethodHandles.Lookup) UnsafeUtil.getUnsafe().getObject(UnsafeUtil.getUnsafe().staticFieldBase(f), offset);
    }

    public static List<Field> allFields(List<Class<?>> classes) {
        List<Field> fields = new ArrayList<>();
        for (Class<?> c : classes)
            fields.addAll(List.of(c.getDeclaredFields()));
        return fields;
    }

    public static List<Class<?>> superClasses(Class<?> o, Class<?> head, boolean withoutHead, boolean withoutSelf) {
        if (parentsMapping.containsKey(o)) return parentsMapping.get(o);
        List<Class<?>> superClasses = new ArrayList<>();
        if (!withoutHead)
            superClasses.add(head);
        if (!withoutSelf)
            superClasses.add(o);
        Class<?> superClass = o.getSuperclass();
        boolean isObjectClass = (superClass == head);
        if (superClass != null)
            while (!isObjectClass) {
                superClasses.add(superClass);
                superClass = superClass.getSuperclass();
                isObjectClass = (superClass == head);
            }
        parentsMapping.put(o, superClasses);
        return superClasses;
    }

    public static List<FieldVarHandle> bigFilter_allSuper(Class<?> klass, Class<?> head, Predicate<Field> fieldPredicate, boolean withoutHead, boolean withoutSelf) throws Throwable {
        if (bigFilter.containsKey(klass) && bigFilter.get(klass).containsKey(fieldPredicate))
            return bigFilter.get(klass).get(fieldPredicate);
        List<Class<?>> classes = superClasses(klass, head, withoutHead, withoutSelf);
        List<Field> fields = new ArrayList<>();
        List<FieldVarHandle> fieldVarHandles = new ArrayList<>();
        for (Class<?> c : classes) {
            Field[] fields1 = c.getDeclaredFields();
            for (int index =0;index<fields1.length;index++) {
                try {
                    Field f = fields1[index];
                    if (fieldPredicate.test(f))
                        fields.add(f);
                } catch (Throwable throwable) {}
            }
        }
        for (Field field : fields)
            fieldVarHandles.add(new FieldVarHandle(field, quickRead(field)));
        if (!bigFilter.containsKey(klass)) {
            bigFilter.put(klass, bigFilter.getOrDefault(klass, new HashMap<>()));
        }

        bigFilter.get(klass).put(fieldPredicate, fieldVarHandles);
        return fieldVarHandles;
    }

    public static VarHandle quickRead(Field f) throws Throwable {
        return IMPL_LOOKUP().unreflectVarHandle(f);
    }

    public static Class<?> getActuallyType(Class<?> clazz) {
        try {
            return Class.forName(((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0].getTypeName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public record FieldVarHandle(Field field, VarHandle varHandle) {

    }
}
