package com.mega.revelationfix.util;

import com.mega.revelationfix.util.asm.GoetyClassNodeProcessor;
import com.mega.revelationfix.util.asm.IClassNodeProcessor;
import com.mega.revelationfix.util.asm.MillisTimeRedirector;
import cpw.mods.modlauncher.LaunchPluginHandler;
import cpw.mods.modlauncher.Launcher;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.service.MixinService;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class RevelationFixMixinPlugin implements IMixinConfigPlugin {
    public static final Set<String> toRemovedMixins = Collections.synchronizedSet(new HashSet<>());
    public static final Set<String> handCheckMixins = Collections.synchronizedSet(new HashSet<>());
    private static final String ISPELL_CLASS = "com/Polarice3/Goety/api/magic/ISpell";
    private static final String IOWNED_CLASS = "com/Polarice3/Goety/api/entities/IOwned";
    private static final String MOB_EFFECT_EVENT$EXPIRED = "net/minecraftforge/event/entity/living/MobEffectEvent$Expired";
    private static final String ABSTRACT_SPELL_MIXIN = "com.mega.revelationfix.mixin.fantasy_ending.time.ironspellbook.AbstractSpellMixin";
    private static final String EVENT_BUS_MIXIN = "net/minecraftforge/eventbus/EventBus";
    private static final String IMODULAR_ITEM_CLASS = "se.mickelus.tetra.items.modular.IModularItem".replace('.', '/');
    private static final String EVENT_UTIL_CLASS = "com/mega/revelationfix/util/EventUtil";
    public static boolean USE_FIX_MIXIN = true;
    public static Logger LOGGER = LogManager.getLogger("RevelationFix");
    public static IClassNodeProcessor GOETY_PROCESSOR = GoetyClassNodeProcessor.INSTANCE;
    public static IClassNodeProcessor TIME_PROCESSOR = MillisTimeRedirector.INSTANCE;
    static {
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
        toRemovedMixins.add("dev/shadowsoffire/attributeslib/mixin/LivingEntityMixin");
        toRemovedMixins.add("z1gned/goetyrevelation/mixin/LavaballSpellMixin");
        toRemovedMixins.add("z1gned/goetyrevelation/mixin/FireballSpellMixin");
        toRemovedMixins.add("z1gned/goetyrevelation/mixin/WitherSkullSpellMixin");
        toRemovedMixins.add("z1gned/goetyrevelation/mixin/ObsidianMonolithMixin");
        toRemovedMixins.add("z1gned/goetyrevelation/mixin/PlayerMixin");
        toRemovedMixins.add("z1gned/goetyrevelation/mixin/LivingEntityMixin");
        toRemovedMixins.add("z1gned/goetyrevelation/mixin/HellfireMixin");
        toRemovedMixins.add("z1gned/goetyrevelation/mixin/BarricadeSpellMixin");
        toRemovedMixins.add("z1gned/goetyrevelation/mixin/BowItemMixin");
        toRemovedMixins.add("net/bettercombat.mixin/client/MinecraftClientInject");
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
                        if (phase == Phase.BEFORE) {
                            GOETY_PROCESSOR.transform(name, classNode, classType, shouldWrite);
                            TIME_PROCESSOR.transform(name, classNode, classType, shouldWrite);
                            if (name.equals(MOB_EFFECT_EVENT$EXPIRED)) {
                                classNode.visitAnnotation("Lnet/minecraftforge/eventbus/api/Cancelable;", true);
                                shouldWrite.set(true);
                            }
                            else if (name.endsWith("Mixin") ) {
                                synchronized (toRemovedMixins) {
                                    if (toRemovedMixins.contains(name)) {
                                        clearMixinClass(classNode);
                                        toRemovedMixins.remove(name);
                                        LOGGER.debug("Removed MixinClass :" + name);
                                    /*
                                    if (toRemovedMixins.isEmpty()) {
                                        synchronized (plugins) {
                                            plugins.remove("RevelationFixPlugin");
                                        }
                                    }
                                     */
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

    @Override
    public void onLoad(String s) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        LOGGER.debug("Added RF MixinClass :" + mixinClassName);
        ClassNode node;
        try {
            node = MixinService.getService().getBytecodeProvider().getClassNode(mixinClassName);
        } catch (Exception var9) {
            return false;
        }
        if (!handCheckMixins.isEmpty()) {
            if (mixinClassName.equals(ABSTRACT_SPELL_MIXIN)) {
                if (EarlyConfig.modIds.contains("fantasy_ending"))
                    return false;
                else return EarlyConfig.modIds.contains("irons_spellbooks");
            }
        }
        List<AnnotationNode> annotationNodes = new ArrayList<>(node.invisibleAnnotations);
        for (AnnotationNode annotationNode : annotationNodes) {
            if (annotationNode.desc.equals("com/mega/revelationfix/safe/mixinpart/DeprecatedMixin;"))
                return false;
            if (annotationNode.desc.equals("Lcom/mega/revelationfix/safe/mixinpart/NonDevEnvMixin;") && MCMapping.isWorkingspaceMode())
                return false;
            if (annotationNode.desc.equals("Lcom/mega/revelationfix/safe/mixinpart/DevEnvMixin;") && !MCMapping.isWorkingspaceMode())
                return false;
            if (annotationNode.desc.equals("Lcom/mega/revelationfix/safe/mixinpart/ModDependsMixin;")) {
                //0-> value 1-> modid
                return EarlyConfig.modIds.contains((String) annotationNode.values.get(1));
            }
            if (annotationNode.desc.equals("Lcom/mega/revelationfix/safe/mixinpart/NoModDependsMixin;")) {
                //0-> value 1-> modid
                return !EarlyConfig.modIds.contains((String) annotationNode.values.get(1));
            }
        }
        return true;
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
