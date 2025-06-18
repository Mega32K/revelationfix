package com.mega.revelationfix.util.asm;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;

import java.util.concurrent.atomic.AtomicBoolean;

public interface IClassNodeProcessor {
    void transform(String name,ClassNode classNode, Type classType, AtomicBoolean modified);
    static boolean isUnsupportModifyingClass(String name) {
        return "com/mega/revelationfix/util/EventUtil".equals(name) || name.startsWith("com/mega/revelationfix/util/asm");
    }
}
