package com.mega.revelationfix.util.asm;

import com.Polarice3.Goety.api.magic.ISpell;
import com.Polarice3.Goety.common.magic.SpellStat;
import com.mega.endinglib.coremod.forge.IClassProcessor;
import com.mega.endinglib.util.asm.injection.InjectionFinder;
import com.mega.revelationfix.util.RevelationFixMixinPlugin;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GoetyClassNodeProcessor implements IClassProcessor, Opcodes {
    private static final String SPELL_STAT_CLASS = "com/Polarice3/Goety/common/magic/SpellStat";
    private static final String ISPELL_CLASS = "com/Polarice3/Goety/api/magic/ISpell";
    private static final String IOWNED_CLASS = "com/Polarice3/Goety/api/entities/IOwned";
    private static final String SPELL_STAT_FIELD_CLASS = "com/mega/revelationfix/util/asm/GoetyClassNodeProcessor$SpellStatField";
    private static final String LIVING_ENTITY_CLASS = "net/minecraft/world/entity/LivingEntity";
    private static final String SEHELPER_CLASS = "com/Polarice3/Goety/utils/SEHelper";
    private static final String IMODULAR_ITEM_CLASS = "se.mickelus.tetra.items.modular.IModularItem".replace('.', '/');
    private static final Lock LOCK = new ReentrantLock();
    public static IClassProcessor INSTANCE = new GoetyClassNodeProcessor();
    public static Logger LOGGER = RevelationFixMixinPlugin.LOGGER;

    /**
     * {@link ISpell#stopSpell(ServerLevel, LivingEntity, ItemStack, ItemStack, int, SpellStat)}特供
     */
    public static void injectSpellStatCode(MethodNode methodNode, int spellStatArgIndex) {
        for (SpellStatField enum_ : SpellStatField.values()) {
            Optional<VarInsnNode> storeOpt = getTheFirstSpellStatFieldXStoreNodeWhichCalledAfterGetEnchantmentLevelsOfVariable(methodNode.instructions, enum_);
            if (storeOpt.isPresent()) {
                VarInsnNode store = storeOpt.get();
                int indexOfStore = methodNode.instructions.indexOf(store);
                //找到的话根据field属性修改
                InsnList insnList = new InsnList();
                //加载方法参数，原值，spell stat field并写入 原此法术属性
                //共8 node
                addSpellStatInsnNodes(insnList, store.var, enum_);
                //插入到第一个使用x_load_<var>之前
                methodNode.instructions.forEach(asn -> {
                    if (asn instanceof VarInsnNode varInsnNode && varInsnNode.getOpcode() == enum_.loadOpcode && varInsnNode.var == store.var) {
                        if (methodNode.instructions.indexOf(varInsnNode) > indexOfStore) {
                            methodNode.instructions.insertBefore(varInsnNode, insnList);
                        }
                    }
                });

            }
        }
        InsnList insnList = new InsnList();
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 2));
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 3));
        insnList.add(new VarInsnNode(Opcodes.ALOAD, spellStatArgIndex));
        insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, RevelationFixMixinPlugin.EVENT_UTIL_CLASS, "modifySpellStatsWithoutEnchantment", "(Lcom/Polarice3/Goety/api/magic/ISpell;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lcom/Polarice3/Goety/common/magic/SpellStat;)V", false));
        InjectionFinder.injectHead(methodNode, insnList);
    }
    public static Optional<VarInsnNode> getTheFirstSpellStatFieldXStoreNodeWhichCalledAfterGetEnchantmentLevelsOfVariable(InsnList instructions, SpellStatField statField) {
        AbstractInsnNode getLevelsNode = null;
        VarInsnNode x_store = null;
        boolean visitedEnchantmentROField = false;
        boolean visitedEnchantedFocusMethod = false;
        int storeIndex = -1;
        for (AbstractInsnNode asn : instructions) {
            if (asn instanceof MethodInsnNode mNode) {
                if (mNode.owner.equals("com/Polarice3/Goety/utils/WandUtil") && mNode.name.equals("enchantedFocus") && mNode.desc.equals("(Lnet/minecraft/world/entity/LivingEntity;)Z")){
                    visitedEnchantedFocusMethod = true;
                    break;
                }
            }
        }
        if (visitedEnchantedFocusMethod) {
            for (AbstractInsnNode asn : instructions) {
                if (!visitedEnchantmentROField) {
                    if (statField.isEnchantmentField(asn)) {
                        visitedEnchantmentROField = true;
                        continue;
                    }
                } else if (getLevelsNode == null && isGetLevelsMethod(asn)) {
                    getLevelsNode = asn;
                }
                if (visitedEnchantmentROField && getLevelsNode != null
                        && asn instanceof VarInsnNode varInsnNode && varInsnNode.getOpcode() == statField.storeOpcode) {
                    x_store = varInsnNode;
                    break;
                }
            }
        }
        return (x_store != null) ? Optional.of(x_store) : Optional.empty();
    }

    public static boolean isGetLevelsMethod(AbstractInsnNode asn) {
        return asn instanceof MethodInsnNode methodInsnNode &&
                methodInsnNode.getOpcode() == Opcodes.INVOKESTATIC && "com/Polarice3/Goety/utils/WandUtil".equals(methodInsnNode.owner) && "getLevels".equals(methodInsnNode.name) && "(Lnet/minecraft/world/item/enchantment/Enchantment;Lnet/minecraft/world/entity/LivingEntity;)I".equals(methodInsnNode.desc);
    }

    public static void addSpellStatInsnNodes(InsnList insnList, int theXStoreNodeVar, SpellStatField statField) {
        //aload_0 加载this ISPELL
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
        //aload_1 加载ServerLevel
        insnList.add(new VarInsnNode(ALOAD, 1));
        //aload_2 加载LivingEntity
        insnList.add(new VarInsnNode(ALOAD, 2));
        //aload_3 加载ItemStack
        insnList.add(new VarInsnNode(ALOAD, 3));
        //x_load_<var> 加载对应的值
        insnList.add(new VarInsnNode(statField.loadOpcode, theXStoreNodeVar));
        //加载SpellStatField
        insnList.add(new FieldInsnNode(Opcodes.GETSTATIC, SPELL_STAT_FIELD_CLASS, statField.name(), "Lcom/mega/revelationfix/util/asm/GoetyClassNodeProcessor$SpellStatField;"));

        //调用重定向方法
        insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, RevelationFixMixinPlugin.EVENT_UTIL_CLASS, statField.getSpellModifyMethodName(), statField.getSpellModifyMethodDesc(), false));
        //x_store_<var>
        insnList.add(new VarInsnNode(statField.storeOpcode, theXStoreNodeVar));
    }

    @Override
    public void processClass(ILaunchPluginService.Phase phase, ClassNode classNode, Type type, AtomicBoolean modified) {

        String name = classNode.name;
        if (phase == ILaunchPluginService.Phase.AFTER) {
            if (!RevelationFixMixinPlugin.isUnsupportModifyingClass(name)) {
                classNode.methods.forEach(methodNode -> {
                    if (!ISPELL_CLASS.equals(name)) {
                        {

                            //查看被遍历的方法是否为ISpell类的方法的复写方法
                            if (methodNode.name.equals("SpellResult") && methodNode.desc.equals("(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lcom/Polarice3/Goety/common/magic/SpellStat;)V")) {

                                injectSpellStatCode(methodNode, 4);
                                modified.set(true);
                            } else if (methodNode.name.equals("startSpell") && methodNode.desc.equals("(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lcom/Polarice3/Goety/common/magic/SpellStat;)V")) {
                                injectSpellStatCode(methodNode, 4);
                                modified.set(true);
                            } else if (methodNode.name.equals("useSpell") && methodNode.desc.equals("(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;ILcom/Polarice3/Goety/common/magic/SpellStat;)V")) {
                                injectSpellStatCode(methodNode, 5);
                                modified.set(true);
                            } else if (methodNode.name.equals("stopSpell") && methodNode.desc.equals("(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;ILcom/Polarice3/Goety/common/magic/SpellStat;)V")) {
                                injectSpellStatCode(methodNode, 6);
                                modified.set(true);
                            }
                        }
                    }
                });
            }
        }
        if (phase == ILaunchPluginService.Phase.BEFORE) {
            if (!RevelationFixMixinPlugin.isUnsupportModifyingClass(name)) {
                classNode.methods.forEach(methodNode -> {
                    //遍历每一个方法的Code属性，检查是否调用了ISpell类相关方法
                    methodNode.instructions.forEach(abstractInsnNode -> {
                        if (abstractInsnNode instanceof MethodInsnNode mNode) {
                            //改调用，让巫术反应台的修改被应用
                            if (mNode.owner.equals(ISPELL_CLASS)) {
                                if (mNode.name.equals("SpellResult") && mNode.desc.equals("(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lcom/Polarice3/Goety/common/magic/SpellStat;)V")) {
                                    modified.set(true);
                                    methodNode.instructions.set(mNode, new MethodInsnNode(Opcodes.INVOKESTATIC, RevelationFixMixinPlugin.EVENT_UTIL_CLASS, "redirectSpellResult", "(Lcom/Polarice3/Goety/api/magic/ISpell;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lcom/Polarice3/Goety/common/magic/SpellStat;)V", false));
                                } else if (mNode.name.equals("startSpell") && mNode.desc.equals("(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lcom/Polarice3/Goety/common/magic/SpellStat;)V")) {
                                    modified.set(true);
                                    methodNode.instructions.set(mNode, new MethodInsnNode(Opcodes.INVOKESTATIC, RevelationFixMixinPlugin.EVENT_UTIL_CLASS, "redirectStartSpell", "(Lcom/Polarice3/Goety/api/magic/ISpell;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lcom/Polarice3/Goety/common/magic/SpellStat;)V", false));
                                } else if (mNode.name.equals("useSpell") && mNode.desc.equals("(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;ILcom/Polarice3/Goety/common/magic/SpellStat;)V")) {
                                    modified.set(true);
                                    methodNode.instructions.set(mNode, new MethodInsnNode(Opcodes.INVOKESTATIC, RevelationFixMixinPlugin.EVENT_UTIL_CLASS, "redirectUseSpell", "(Lcom/Polarice3/Goety/api/magic/ISpell;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;ILcom/Polarice3/Goety/common/magic/SpellStat;)V", false));
                                } else if (mNode.name.equals("stopSpell") && mNode.desc.equals("(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;ILcom/Polarice3/Goety/common/magic/SpellStat;)V")) {
                                    modified.set(true);
                                    methodNode.instructions.set(mNode, new MethodInsnNode(Opcodes.INVOKESTATIC, RevelationFixMixinPlugin.EVENT_UTIL_CLASS, "redirectStopSpell", "(Lcom/Polarice3/Goety/api/magic/ISpell;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;ILcom/Polarice3/Goety/common/magic/SpellStat;)V", false));
                                }
                            }
                        }

                    });
                });
            }
            switch (name) {
                case ISPELL_CLASS -> {
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
                                    insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, RevelationFixMixinPlugin.EVENT_UTIL_CLASS, "castDuration", "(ILcom/Polarice3/Goety/api/magic/ISpell;Lnet/minecraft/world/entity/LivingEntity;)I", false));
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
                                    insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, RevelationFixMixinPlugin.EVENT_UTIL_CLASS, "spellCooldown", "(ILcom/Polarice3/Goety/api/magic/ISpell;)I", false));
                                    methodNode.instructions.insertBefore(insnNode, insnList);
                                }
                            });
                            modified.set(true);
                        }
                    });
                }
                case IOWNED_CLASS -> classNode.methods.forEach(methodNode -> {
                    if (methodNode.name.equals("setTrueOwner") && methodNode.desc.equals("(Lnet/minecraft/world/entity/LivingEntity;)V")) {
                        AbstractInsnNode first = methodNode.instructions.getFirst();
                        InsnList insnList = new InsnList();
                        insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
                        insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, RevelationFixMixinPlugin.EVENT_UTIL_CLASS, "modifyOwner", "(Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/entity/LivingEntity;", false));
                        insnList.add(new VarInsnNode(Opcodes.ASTORE, 1));
                        methodNode.instructions.insertBefore(first, insnList);
                        modified.set(true);
                    }
                });
                case SEHELPER_CLASS -> classNode.methods.forEach(methodNode -> {
                    if (methodNode.name.equals("increaseSouls") && methodNode.desc.equals("(Lnet/minecraft/world/entity/player/Player;I)V")) {
                        InsnList insnList = new InsnList();
                        insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        insnList.add(new VarInsnNode(Opcodes.ILOAD, 1));
                        insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, RevelationFixMixinPlugin.EVENT_UTIL_CLASS, "increaseSouls", "(Lnet/minecraft/world/entity/player/Player;I)I", false));
                        insnList.add(new VarInsnNode(Opcodes.ISTORE, 1));
                        InjectionFinder.injectHead(methodNode, insnList);
                        modified.set(true);
                    } else if (methodNode.name.equals("decreaseSouls") && methodNode.desc.equals("(Lnet/minecraft/world/entity/player/Player;I)V")) {
                        InsnList insnList = new InsnList();
                        insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        insnList.add(new VarInsnNode(Opcodes.ILOAD, 1));
                        insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, RevelationFixMixinPlugin.EVENT_UTIL_CLASS, "decreaseSouls", "(Lnet/minecraft/world/entity/player/Player;I)I", false));
                        insnList.add(new VarInsnNode(Opcodes.ISTORE, 1));
                        InjectionFinder.injectHead(methodNode, insnList);
                        modified.set(true);
                    } else if (methodNode.name.equals("decreaseSESouls") && methodNode.desc.equals("(Lnet/minecraft/world/entity/player/Player;I)Z")) {
                        InsnList insnList = new InsnList();
                        insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        insnList.add(new VarInsnNode(Opcodes.ILOAD, 1));
                        insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, RevelationFixMixinPlugin.EVENT_UTIL_CLASS, "decreaseSESouls", "(Lnet/minecraft/world/entity/player/Player;I)I", false));
                        insnList.add(new VarInsnNode(Opcodes.ISTORE, 1));
                        InjectionFinder.injectHead(methodNode, insnList);
                        modified.set(true);
                    }
                });
            }
        }
    }

    /**
     * 枚举来自类{@link com.Polarice3.Goety.common.magic.SpellStat}<br>
     * 对应所有的SpellStat类的字段(属性)
     */
    public enum SpellStatField {
        //POTENCY("potency", "I", "getPotency", "()I", ISTORE, ILOAD, "POTENCY"),
        DURATION("duration", "I", "getDuration", "()I", ISTORE, ILOAD, "DURATION"),
        RANGE("range", "I", "getRange", "()I", ISTORE, ILOAD, "RANGE"),
        RADIUS("radius", "D", "getRadius", "()D", DSTORE, DLOAD, "RADIUS"),
        BURNING("burning", "I", "getBurning", "()I", ISTORE, ILOAD, "BURNING"),
        VELOCITY("velocity", "F", "getVelocity", "()F", FSTORE, FLOAD, "VELOCITY");
        final String name;
        final String desc;
        final String getterName;
        final String getterDesc;
        final int storeOpcode;
        final int loadOpcode;
        final String enchantmentROFieldName;

        SpellStatField(String name, String desc, String getterName, String getterDesc, int storeOpcode, int loadOpcode, String enchantmentFieldName) {
            this.name = name;
            this.desc = desc;
            this.getterName = getterName;
            this.getterDesc = getterDesc;
            this.storeOpcode = storeOpcode;
            this.loadOpcode = loadOpcode;
            this.enchantmentROFieldName = enchantmentFieldName;
        }

        public boolean isKindOfInvoke(AbstractInsnNode insnNode) {
            if (insnNode instanceof FieldInsnNode fieldInsnNode) {
                if (SPELL_STAT_CLASS.equals(fieldInsnNode.owner)) {
                    return fieldInsnNode.getOpcode() == Opcodes.GETFIELD && this.name.equals(fieldInsnNode.name) && this.desc.equals(fieldInsnNode.desc);
                } else return false;
            } else if (insnNode instanceof MethodInsnNode methodInsnNode) {
                if (SPELL_STAT_CLASS.equals(methodInsnNode.owner)) {
                    return methodInsnNode.getOpcode() == Opcodes.INVOKEVIRTUAL && this.getterName.equals(methodInsnNode.name) && this.getterDesc.equals(methodInsnNode.desc);
                } else return false;
            }
            return false;
        }

        public boolean isEnchantmentField(AbstractInsnNode asn) {
            return asn instanceof FieldInsnNode fieldInsnNode &&
                    fieldInsnNode.getOpcode() == Opcodes.GETSTATIC && "com/Polarice3/Goety/common/enchantments/ModEnchantments".equals(fieldInsnNode.owner) && enchantmentROFieldName.equals(fieldInsnNode.name) && "Lnet/minecraftforge/registries/RegistryObject;".equals(fieldInsnNode.desc);
        }

        public String getSpellModifyMethodName() {
            if (storeOpcode == Opcodes.ISTORE) {
                return "spellStatISTORE";
            } else if (storeOpcode == Opcodes.FSTORE) {
                return "spellStatFSTORE";
            } else if (storeOpcode == Opcodes.DSTORE) {
                return "spellStatDSTORE";
            }
            return "";
        }

        public String getSpellModifyMethodDesc() {
            if (storeOpcode == Opcodes.ISTORE) {
                return "(Lcom/Polarice3/Goety/api/magic/ISpell;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;ILcom/mega/revelationfix/util/asm/GoetyClassNodeProcessor$SpellStatField;)I";
            } else if (storeOpcode == Opcodes.FSTORE) {
                return "(Lcom/Polarice3/Goety/api/magic/ISpell;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;FLcom/mega/revelationfix/util/asm/GoetyClassNodeProcessor$SpellStatField;)F";
            } else if (storeOpcode == Opcodes.DSTORE) {
                return "(Lcom/Polarice3/Goety/api/magic/ISpell;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;DLcom/mega/revelationfix/util/asm/GoetyClassNodeProcessor$SpellStatField;)D";
            }
            return "";
        }
    }
}
