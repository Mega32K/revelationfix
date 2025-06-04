package com.mega.revelationfix.util;

import com.Polarice3.Goety.api.magic.ISpell;
import com.Polarice3.Goety.common.magic.Spell;
import cpw.mods.modlauncher.LaunchPluginHandler;
import cpw.mods.modlauncher.Launcher;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
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
    public static final String ISPELL_CLASS_SPELLSTATS_FIELD = "gr_spellStatus";
    private static final String ISPELL_CLASS = "com/Polarice3/Goety/api/magic/ISpell";
    private static final String LIVING_ENTITY_CLASS = "net/minecraft/world/entity/LivingEntity";
    private static final String MOD_FOCUS_ITEM = "z1gned/goetyrevelation/item/ModFocusItem";
    private static final String MOB_EFFECT_EVENT$EXPIRED = "net/minecraftforge/event/entity/living/MobEffectEvent$Expired";
    private static final String ABSTRACT_SPELL_MIXIN = "com.mega.revelationfix.mixin.fantasy_ending.time.ironspellbook.AbstractSpellMixin";
    private static final String EVENT_BUS_MIXIN = "net/minecraftforge/eventbus/EventBus";
    private static final String IMODULAR_ITEM_CLASS = "se.mickelus.tetra.items.modular.IModularItem".replace('.', '/');
    private static final String EVENT_UTIL_CLASS = "com/mega/revelationfix/util/EventUtil";
    public static boolean USE_FIX_MIXIN = true;
    public static Logger LOGGER = LogManager.getLogger("RevelationFix");
    public static boolean isUnsupportModifyingClass(String name) {
        return name.replace('/', '.').startsWith("com.mega.revelationfix.util.EventUtil");
    }

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
                        if (phase == Phase.BEFORE) {
                            {
                                AtomicBoolean atomicBoolean = new AtomicBoolean(false);
                                classNode.methods.forEach(methodNode -> {
                                    methodNode.instructions.forEach(abstractInsnNode -> {
                                        if (abstractInsnNode instanceof MethodInsnNode mNode) {
                                            if (mNode.owner.equals(ISPELL_CLASS)) {
                                                if (mNode.name.equals("SpellResult") && mNode.desc.equals("(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lcom/Polarice3/Goety/common/magic/SpellStat;)V")) {
                                                    if (!isUnsupportModifyingClass(name)) {
                                                        atomicBoolean.set(true);
                                                        methodNode.instructions.set(mNode, new MethodInsnNode(Opcodes.INVOKESTATIC, EVENT_UTIL_CLASS, "redirectSpellResult", "(Lcom/Polarice3/Goety/api/magic/ISpell;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lcom/Polarice3/Goety/common/magic/SpellStat;)V", false));
                                                    }
                                                }
                                            }
                                        }
                                    });
                                });
                                if (atomicBoolean.get())
                                    return true;
                            }
                                if (name.equals(ISPELL_CLASS)) {
                                    classNode.methods.forEach(methodNode -> {
                                        if (methodNode.name.equals("castDuration")) {
                                            methodNode.instructions.forEach(insnNode -> {
                                                int opcode = insnNode.getOpcode();
                                                if (insnNode instanceof InsnNode node)
                                                    LOGGER.debug("InsnNode : " + node.getOpcode());
                                                if (opcode == Opcodes.IRETURN) {
                                                    InsnList insnList = new InsnList();
                                                    insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                                                    insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
                                                    insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, EVENT_UTIL_CLASS, "castDuration", "(ILcom/Polarice3/Goety/api/magic/ISpell;Lnet/minecraft/world/entity/LivingEntity;)I", false));
                                                    methodNode.instructions.insertBefore(insnNode, insnList);
                                                }
                                            });
                                        } else if (methodNode.name.equals("spellCooldown")) {
                                            LOGGER.debug("MethodName : " + methodNode.name);
                                            methodNode.instructions.forEach(insnNode -> {
                                                int opcode = insnNode.getOpcode();
                                                if (insnNode instanceof InsnNode node)
                                                    LOGGER.debug("InsnNode : " + node.getOpcode());
                                                if (opcode == Opcodes.IRETURN) {
                                                    InsnList insnList = new InsnList();
                                                    insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                                                    insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, EVENT_UTIL_CLASS, "spellCooldown", "(ILcom/Polarice3/Goety/api/magic/ISpell;)I", false));
                                                    methodNode.instructions.insertBefore(insnNode, insnList);
                                                }
                                            });
                                        }
                                    });
                                    return true;
                                } else if (name.equals(MOB_EFFECT_EVENT$EXPIRED)) {
                                    classNode.visitAnnotation("Lnet/minecraftforge/eventbus/api/Cancelable;", true);
                                    return true;
                                }

                        }
                        if (name.endsWith("Mixin") && phase == Phase.BEFORE) {
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
                                    return true;
                                }
                            }
                            if (name.startsWith("com/mega/revelationfix/mixin"))
                                return false;
                        }
                        return false;
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
