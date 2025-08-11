package com.mega.revelationfix.util.asm;

import com.Polarice3.Goety.common.magic.spells.VexSpell;
import com.mega.endinglib.coremod.forge.IClassProcessor;
import com.mega.endinglib.util.asm.injection.InjectionFinder;
import com.mega.revelationfix.util.EventUtil;
import com.mega.revelationfix.util.MCMapping;
import com.mega.revelationfix.util.RevelationFixMixinPlugin;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.concurrent.atomic.AtomicBoolean;

public class CompatClassNodeProcessor implements IClassProcessor {
    public static IClassProcessor INSTANCE = new CompatClassNodeProcessor();
    private static final String LIVING_ENTITY_CLASS = "net/minecraft/world/entity/LivingEntity";
    public static Logger LOGGER = RevelationFixMixinPlugin.LOGGER;
    @Override
    public void processClass(ILaunchPluginService.Phase phase, ClassNode classNode, Type type, AtomicBoolean modified) {
        if (phase == ILaunchPluginService.Phase.BEFORE) {
            String name = classNode.name;
            if (LIVING_ENTITY_CLASS.equals(name)) {
                classNode.methods.forEach(eachMethod -> {
                    //回血
                    if (MCMapping.LivingEntity$METHOD$heal.equalsMethodNode(eachMethod)) {
                        InsnList list = new InsnList();
                        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        list.add(new VarInsnNode(Opcodes.FLOAD, 1));
                        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/mega/revelationfix/util/entity/EntityRedirectUtils", "quietusHealingAbility", "(Lnet/minecraft/world/entity/LivingEntity;F)F", false));
                        list.add(new VarInsnNode(Opcodes.FSTORE, 1));
                        InjectionFinder.injectHead(eachMethod, list);
                        //盔甲减伤
                    } else if (MCMapping.LivingEntity$METHOD$getDamageAfterArmorAbsorb.equalsMethodNode(eachMethod)) {
                        eachMethod.instructions.forEach(abstractInsnNode -> {
                            //盔甲减伤方法
                            if (abstractInsnNode instanceof MethodInsnNode mNode && MCMapping.CombatRules$METHOD$getDamageAfterAbsorb.equalsMethodNode(mNode)) {
                                InsnList list = new InsnList();
                                list.add(new VarInsnNode(Opcodes.ALOAD, 0));
                                list.add(new VarInsnNode(Opcodes.FLOAD, 2));
                                //获取mixin注入的字段值
                                list.add(new VarInsnNode(Opcodes.ALOAD, 0));
                                list.add(new FieldInsnNode(Opcodes.GETFIELD, LIVING_ENTITY_CLASS, "revelationfix$runtimeDamageSource", "Lnet/minecraft/world/damagesource/DamageSource;"));

                                list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/mega/revelationfix/util/entity/EntityRedirectUtils", "quietusArmorAbility", "(FLnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/damagesource/DamageSource;)F", false));
                                //插入之后，软重定向
                                eachMethod.instructions.insert(mNode, list);
                            }
                        });
                        //附魔减伤
                    } else if (MCMapping.LivingEntity$METHOD$getDamageAfterMagicAbsorb.equalsMethodNode(eachMethod)) {
                        eachMethod.instructions.forEach(abstractInsnNode -> {
                            //附魔减伤方法
                            if (abstractInsnNode instanceof MethodInsnNode mNode && MCMapping.CombatRules$METHOD$getDamageAfterMagicAbsorb.equalsMethodNode(mNode)) {
                                InsnList list = new InsnList();
                                list.add(new VarInsnNode(Opcodes.ALOAD, 0));
                                list.add(new VarInsnNode(Opcodes.FLOAD, 2));
                                //获取mixin注入的字段值
                                list.add(new VarInsnNode(Opcodes.ALOAD, 0));
                                list.add(new FieldInsnNode(Opcodes.GETFIELD, LIVING_ENTITY_CLASS, "revelationfix$runtimeDamageSource", "Lnet/minecraft/world/damagesource/DamageSource;"));
                                //获取mixin注入的字段值
                                list.add(new VarInsnNode(Opcodes.ALOAD, 0));
                                list.add(new FieldInsnNode(Opcodes.GETFIELD, LIVING_ENTITY_CLASS, "revelationfix$runtimeCorrectDamageBeforeResistance", "Lit/unimi/dsi/fastutil/Pair;"));

                                list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/mega/revelationfix/util/entity/EntityRedirectUtils", "quietusEnchantmentAbility", "(FLnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/damagesource/DamageSource;Lit/unimi/dsi/fastutil/Pair;)F", false));
                                //插入之后，软重定向
                                eachMethod.instructions.insert(mNode, list);
                            }
                        });
                    }
                });
            }
        }
    }
}
