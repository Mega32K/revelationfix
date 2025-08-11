package com.mega.revelationfix.util.asm;

import com.mega.endinglib.coremod.forge.IClassProcessor;
import com.mega.endinglib.util.asm.injection.InjectionFinder;
import com.mega.revelationfix.util.RevelationFixMixinPlugin;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.concurrent.atomic.AtomicBoolean;

public class NormalClassNodeProcessor implements IClassProcessor {
    public static NormalClassNodeProcessor INSTANCE = new NormalClassNodeProcessor();
    public static Logger LOGGER = RevelationFixMixinPlugin.LOGGER;
    public static final String SELF_AT_ITF = "Lcom/mega/revelationfix/util/java/Self;";
    public static final String DYNAMIC_UTIL_CLASS = "com/mega/revelationfix/util/DynamicUtil";
    public static final String GR_ITEMS_CLASS = "com/mega/revelationfix/common/init/GRItems";
    @Override
    public void processClass(ILaunchPluginService.Phase phase, ClassNode classNode, Type type, AtomicBoolean atomicBoolean) {
        if (phase == ILaunchPluginService.Phase.BEFORE) {
            String name = classNode.name;
            if (name.equals(GR_ITEMS_CLASS)) {
                classNode.methods.forEach(methodNode -> {
                    if ("init".equals(methodNode.name) && "(Lnet/minecraftforge/registries/DeferredRegister;)V".equals(methodNode.desc)) {
                        InsnList insnNodes = new InsnList();
                        classNode.fields.forEach(fieldNode -> {
                            if (fieldNode.desc.equals("Lnet/minecraftforge/registries/RegistryObject;")
                                    && (fieldNode.visibleAnnotations == null || fieldNode.visibleAnnotations.isEmpty() || !hasRuntimeVisibleAnnotation(fieldNode, SELF_AT_ITF))) {
                                insnNodes.add(new FieldInsnNode(Opcodes.GETSTATIC, GR_ITEMS_CLASS, "CREATIVE_TAB_ITEMS", "Ljava/util/List;"));
                                insnNodes.add(new FieldInsnNode(Opcodes.GETSTATIC, GR_ITEMS_CLASS, fieldNode.name, fieldNode.desc));
                                insnNodes.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z", true));
                                insnNodes.add(new InsnNode(Opcodes.POP));
                            }
                        });
                        InjectionFinder.injectTail(methodNode, insnNodes);
                    }
                });
            }
        }
    }
    public static boolean hasRuntimeVisibleAnnotation(FieldNode field, String annotationDescriptor) {
        if (field.visibleAnnotations == null) {
            return false;
        }

        for (AnnotationNode ann : field.visibleAnnotations) {
            if (annotationDescriptor.equals(ann.desc)) {
                return true;
            }
        }
        return false;
    }
}
