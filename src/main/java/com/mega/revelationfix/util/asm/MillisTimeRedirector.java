package com.mega.revelationfix.util.asm;

import com.mega.revelationfix.Revelationfix;
import com.mega.revelationfix.util.EventUtil;
import com.mega.revelationfix.util.RevelationFixMixinPlugin;
import net.minecraft.Util;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class MillisTimeRedirector implements IClassNodeProcessor {
    public static IClassNodeProcessor INSTANCE = new MillisTimeRedirector();
    static final String MC_PACKAGE_0 = "net/minecraft/client/gui/components/";
    static final String MC_CLASS_0 = "net/minecraft/client/renderer/RenderStateShard";
    static final String UTIL_CLASS = "net/minecraft/Util";
    static final String EVENT_UTIL_CLASS = "com/mega/revelationfix/util/EventUtil";
    @Override
    public void transform(String name, ClassNode classNode, Type classType, AtomicBoolean modified) {
        if (!IClassNodeProcessor.isUnsupportModifyingClass(name)) {
            if (name.startsWith(MC_PACKAGE_0) || name.equals(MC_CLASS_0)) {
                 classNode.methods.forEach(method -> {
                    method.instructions.forEach(abstractInsnNode -> {
                        if (abstractInsnNode instanceof MethodInsnNode mNode) {
                            if (mNode.owner.equals(UTIL_CLASS)) {
                                if (mNode.name.equals("getMillis") && mNode.desc.equals("()J")) {
                                    method.instructions.set(mNode, new MethodInsnNode(Opcodes.INVOKESTATIC, EVENT_UTIL_CLASS, "getMillis", "()J", false));
                                    modified.set(true);
                                }
                            }
                        }
                    });
                });
                if (modified.get())
                    RevelationFixMixinPlugin.LOGGER.debug("Modified Util#getMillis()J in class {}", name);
            }
        }
    }
    boolean pass(String name) {
        return name.equals("net/minecraft/client/gui/screens/LoadingOverlay");
    }
}
