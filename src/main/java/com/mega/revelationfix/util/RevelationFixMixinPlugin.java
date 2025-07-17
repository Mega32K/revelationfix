package com.mega.revelationfix.util;

import com.mega.endinglib.coremod.forge.IClassProcessor;
import com.mega.endinglib.util.asm.injection.InjectionFinder;
import com.mega.endinglib.util.mixin.ApplyCheckMixinConfigPlugin;
import com.mega.revelationfix.util.asm.CompatClassNodeProcessor;
import com.mega.revelationfix.util.asm.GoetyClassNodeProcessor;
import cpw.mods.modlauncher.LaunchPluginHandler;
import cpw.mods.modlauncher.Launcher;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.service.MixinService;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class RevelationFixMixinPlugin extends ApplyCheckMixinConfigPlugin {
    public static final Set<String> toRemovedMixins = Collections.synchronizedSet(new HashSet<>());
    public static final Set<String> handCheckMixins = Collections.synchronizedSet(new HashSet<>());
    private static final String ISPELL_CLASS = "com/Polarice3/Goety/api/magic/ISpell";
    private static final String IOWNED_CLASS = "com/Polarice3/Goety/api/entities/IOwned";
    private static final String MOB_EFFECT_EVENT$EXPIRED = "net/minecraftforge/event/entity/living/MobEffectEvent$Expired";
    private static final String ABSTRACT_SPELL_MIXIN = "com.mega.revelationfix.mixin.fantasy_ending.time.ironspellbook.AbstractSpellMixin";
    private static final String EVENT_BUS_MIXIN = "net/minecraftforge/eventbus/EventBus";
    private static final String IMODULAR_ITEM_CLASS = "se.mickelus.tetra.items.modular.IModularItem".replace('.', '/');
    public static final String EVENT_UTIL_CLASS;
    public static boolean USE_FIX_MIXIN = true;
    public static Logger LOGGER = LogManager.getLogger("RevelationFix");
    public static IClassProcessor GOETY_PROCESSOR = GoetyClassNodeProcessor.INSTANCE;
    public static IClassProcessor COMPAT_PROCESSOR = CompatClassNodeProcessor.INSTANCE;
    static {
        EVENT_UTIL_CLASS = EventUtil.class.getName().replace(".", "/");
        handCheckMixins.add(ABSTRACT_SPELL_MIXIN);
        toRemovedMixins.add("z1gned/goetyrevelation/mixin/ApostleMixin");
        toRemovedMixins.add("z1gned/goetyrevelation/mixin/BossLoopMusicMixin");
        toRemovedMixins.add("z1gned/goetyrevelation/mixin/ApostleRendererMixin");
        toRemovedMixins.add("z1gned/goetyrevelation/mixin/TargetGoalMixin");
        toRemovedMixins.add("z1gned/goetyrevelation/mixin/GhastSpellMixin");
        toRemovedMixins.add("z1gned/goetyrevelation/mixin/BossBarEventMixin");
        toRemovedMixins.add("z1gned/goetyrevelation/mixin/StringRenderOutputMixin");
        toRemovedMixins.add("z1gned/goetyrevelation/mixin/FontMixin");
        toRemovedMixins.add("z1gned/goetyrevelation/mixin/CycloneSpellMixin");
        toRemovedMixins.add("z1gned/goetyrevelation/mixin/ChatFormattingMixin");
        toRemovedMixins.add("z1gned/goetyrevelation/mixin/ClientEventsMixin");
        //toRemovedMixins.add("dev/shadowsoffire/attributeslib/mixin/LivingEntityMixin");
        toRemovedMixins.add("z1gned/goetyrevelation/mixin/LavaballSpellMixin");
        toRemovedMixins.add("z1gned/goetyrevelation/mixin/FireballSpellMixin");
        toRemovedMixins.add("z1gned/goetyrevelation/mixin/WitherSkullSpellMixin");
        toRemovedMixins.add("z1gned/goetyrevelation/mixin/ObsidianMonolithMixin");
        toRemovedMixins.add("z1gned/goetyrevelation/mixin/PlayerMixin");
        toRemovedMixins.add("z1gned/goetyrevelation/mixin/LivingEntityMixin");
        toRemovedMixins.add("z1gned/goetyrevelation/mixin/HellfireMixin");
        toRemovedMixins.add("z1gned/goetyrevelation/mixin/BarricadeSpellMixin");
        toRemovedMixins.add("z1gned/goetyrevelation/mixin/BowItemMixin");
        //BC兼容
        toRemovedMixins.add("z1gned/goetyrevelation/mixin/LivingEntityRendererMixin");
        toRemovedMixins.add("z1gned/goetyrevelation/mixin/ApostleModelMixin");
        toRemovedMixins.add("com/elfmcys/yesstevemodel/mixin/client/InventoryScreenMixin");

        //toTransformClasses.add(LIVING_ENTITY_CLASS);
        if (USE_FIX_MIXIN)
            try {
                Field f1 = Launcher.class.getDeclaredField("launchPlugins");
                f1.setAccessible(true);
                LaunchPluginHandler launchPluginHandler = (LaunchPluginHandler) f1.get(Launcher.INSTANCE);
                Field f2 = LaunchPluginHandler.class.getDeclaredField("plugins");
                f2.setAccessible(true);
                Map<String, ILaunchPluginService> plugins = (Map<String, ILaunchPluginService>) f2.get(launchPluginHandler);
                plugins.put("RevelationFixPlugin", new ILaunchPluginService() {

                    @Override
                    public String name() {
                        return "RevelationFixPlugin";
                    }

                    @Override
                    public EnumSet<Phase> handlesClass(Type classType, boolean isEmpty) {
                        return EnumSet.of(Phase.BEFORE, Phase.AFTER);
                    }

                    @Override
                    public boolean processClass(Phase phase, ClassNode classNode, Type classType) {
                        String name = classNode.name;
                        AtomicBoolean shouldWrite = new AtomicBoolean(false);
                        COMPAT_PROCESSOR.processClass(phase, classNode, classType, shouldWrite);
                        GOETY_PROCESSOR.processClass(phase, classNode, classType, shouldWrite);
                        if (phase == Phase.BEFORE) {
                            if (name.equals(MOB_EFFECT_EVENT$EXPIRED)) {
                                classNode.visitAnnotation("Lnet/minecraftforge/eventbus/api/Cancelable;", true);
                                shouldWrite.set(true);
                            } else if (name.equals(EVENT_BUS_MIXIN)) {
                                classNode.methods.forEach(methodNode -> {
                                    if (methodNode.name.equals("handleException") && methodNode.desc.equals("(Lnet/minecraftforge/eventbus/api/IEventBus;Lnet/minecraftforge/eventbus/api/Event;[Lnet/minecraftforge/eventbus/api/IEventListener;ILjava/lang/Throwable;)V")) {
                                        InsnList list = new InsnList();
                                        list.add(new VarInsnNode(Opcodes.ALOAD, 5));
                                        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, EVENT_UTIL_CLASS, "tryCaughtThrowable", "(Ljava/lang/Throwable;)V", false));
                                        methodNode.instructions.insertBefore(InjectionFinder.head(methodNode.instructions), list);
                                        shouldWrite.set(true);
                                    }
                                });
                            }
                            else if (name.endsWith("Mixin") ) {
                                synchronized (toRemovedMixins) {
                                    if (toRemovedMixins.contains(name)) {
                                        clearMixinClass(classNode);
                                        toRemovedMixins.remove(name);
                                        LOGGER.debug("Removed MixinClass :" + name);
                                        shouldWrite.set(true);
                                    }
                                }
                                if (name.startsWith("com/mega/revelationfix/mixin"))
                                    return false;
                            }
                        }
                        return shouldWrite.get();
                    }
                });
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                System.exit(-1);

            }
    }

    public static void clearMixinClass(ClassNode classNode) {
        if (classNode.methods != null)
            classNode.methods.clear();
        if (classNode.fields != null)
            classNode.fields.clear();
        if (classNode.interfaces != null)
            classNode.interfaces.clear();
        if (classNode.invisibleAnnotations != null) {
            classNode.invisibleAnnotations.removeIf(n -> !n.desc.equals("Lorg/spongepowered/asm/mixin/Mixin;"));
        }
    }

    public static boolean isUnsupportModifyingClass(String name) {
        return EventUtil.EVENT_UTIL_CLASS.equals(name) || name.startsWith("com/mega/revelationfix/util/asm");
    }

    @Override
    public void onLoad(String s) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }
    @Override
    public void acceptTargets(Set<String> set, Set<String> set1) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {

    }

    @Override
    public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {

    }
}
