package com.mega.revelationfix.util.asm;

import com.mega.revelationfix.util.RevelationFixMixinPlugin;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.concurrent.atomic.AtomicBoolean;

public class GoetyClassNodeProcessor implements IClassNodeProcessor {

    public static IClassNodeProcessor INSTANCE = new GoetyClassNodeProcessor();
    private static final String ISPELL_CLASS = "com/Polarice3/Goety/api/magic/ISpell";
    private static final String IOWNED_CLASS = "com/Polarice3/Goety/api/entities/IOwned";
    private static final String LIVING_ENTITY_CLASS = "net/minecraft/world/entity/LivingEntity";
    private static final String MOD_FOCUS_ITEM = "z1gned/goetyrevelation/item/ModFocusItem";
    private static final String MOB_EFFECT_EVENT$EXPIRED = "net/minecraftforge/event/entity/living/MobEffectEvent$Expired";
    private static final String ABSTRACT_SPELL_MIXIN = "com.mega.revelationfix.mixin.fantasy_ending.time.ironspellbook.AbstractSpellMixin";
    private static final String EVENT_BUS_MIXIN = "net/minecraftforge/eventbus/EventBus";
    private static final String IMODULAR_ITEM_CLASS = "se.mickelus.tetra.items.modular.IModularItem".replace('.', '/');
    private static final String EVENT_UTIL_CLASS = "com/mega/revelationfix/util/EventUtil";
    public static Logger LOGGER = RevelationFixMixinPlugin.LOGGER;
    @Override
    public void transform(String name, ClassNode classNode, Type classType, AtomicBoolean modified) {
        classNode.methods.forEach(methodNode -> {
            methodNode.instructions.forEach(abstractInsnNode -> {
                if (abstractInsnNode instanceof MethodInsnNode mNode) {
                    if (mNode.owner.equals(ISPELL_CLASS)) {
                        if (!IClassNodeProcessor.isUnsupportModifyingClass(name)) {
                            if (mNode.name.equals("SpellResult") && mNode.desc.equals("(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lcom/Polarice3/Goety/common/magic/SpellStat;)V")) {
                                modified.set(true);
                                methodNode.instructions.set(mNode, new MethodInsnNode(Opcodes.INVOKESTATIC, EVENT_UTIL_CLASS, "redirectSpellResult", "(Lcom/Polarice3/Goety/api/magic/ISpell;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lcom/Polarice3/Goety/common/magic/SpellStat;)V", false));
                            } else if (mNode.name.equals("startSpell") && mNode.desc.equals("(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lcom/Polarice3/Goety/common/magic/SpellStat;)V")) {
                                modified.set(true);
                                methodNode.instructions.set(mNode, new MethodInsnNode(Opcodes.INVOKESTATIC, EVENT_UTIL_CLASS, "redirectStartSpell", "(Lcom/Polarice3/Goety/api/magic/ISpell;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lcom/Polarice3/Goety/common/magic/SpellStat;)V", false));
                            } else if (mNode.name.equals("useSpell") && mNode.desc.equals("(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;ILcom/Polarice3/Goety/common/magic/SpellStat;)V")) {
                                modified.set(true);
                                methodNode.instructions.set(mNode, new MethodInsnNode(Opcodes.INVOKESTATIC, EVENT_UTIL_CLASS, "redirectUseSpell", "(Lcom/Polarice3/Goety/api/magic/ISpell;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;ILcom/Polarice3/Goety/common/magic/SpellStat;)V", false));
                            }
                        }
                    }
                }
            });
        });
        if (name.equals(ISPELL_CLASS)) {
            LOGGER.debug(ISPELL_CLASS);
            classNode.methods.forEach(methodNode -> {
                if (methodNode.name.equals("castDuration")) {
                    LOGGER.debug(methodNode.name);
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
                    modified.set(true);
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
                    modified.set(true);
                }
            });

        }
        else if (name.endsWith(IOWNED_CLASS)) {
            classNode.methods.forEach(methodNode -> {
                if (methodNode.name.equals("setTrueOwner") && methodNode.desc.equals("(Lnet/minecraft/world/entity/LivingEntity;)V")) {
                    AbstractInsnNode first = methodNode.instructions.getFirst();
                    InsnList insnList = new InsnList();
                    insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, EVENT_UTIL_CLASS, "modifyOwner", "(Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/entity/LivingEntity;", false));
                    insnList.add(new VarInsnNode(Opcodes.ASTORE, 1));
                    methodNode.instructions.insertBefore(first, insnList);
                    modified.set(true);
                }
            });
        }
    }
}
