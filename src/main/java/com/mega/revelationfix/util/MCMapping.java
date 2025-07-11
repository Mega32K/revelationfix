package com.mega.revelationfix.util;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public enum MCMapping {
    CombatRules$METHOD$getDamageAfterMagicAbsorb("getDamageAfterMagicAbsorb", "m_19269_", "(FF)F"),
    CombatRules$METHOD$getDamageAfterAbsorb("getDamageAfterAbsorb", "m_19272_", "(FFF)F"),
    LivingEntity$METHOD$getDamageAfterArmorAbsorb("getDamageAfterArmorAbsorb", "m_21161_", "(Lnet/minecraft/world/damagesource/DamageSource;F)F"),
    LivingEntity$METHOD$getDamageAfterMagicAbsorb("getDamageAfterMagicAbsorb", "m_6515_", "(Lnet/minecraft/world/damagesource/DamageSource;F)F"),
    LivingEntity$METHOD$heal("heal", "m_5634_", "(F)V"),
    Entity$METHOD$markHurt("markHurt", "m_5834_", "()V"),
    SynchedEntityData$DataItem$FIELD$value("value", "f_135391_", ""),
    LivingEntity$METHOD$getMaxHealth("getMaxHealth", "m_21233_", "()F"),
    LivingEntity$METHOD$getHealth("getHealth", "m_21223_", "()F");
    public final String workspace;
    public final String normal;
    public final String desc;

    MCMapping(String workspace, String normal, String desc) {
        this.workspace = workspace;
        this.normal = normal;
        this.desc = desc; 
    }
    public boolean equalsFieldNode(FieldNode node) {
        return this.get().equals(node.name) && this.desc.equals(node.desc);
    }

    public boolean equalsFieldNode(FieldInsnNode node) {
        return this.get().equals(node.name) && this.desc.equals(node.desc);
    }

    public boolean equalsMethodNode(MethodNode node) {
        return this.get().equals(node.name) && this.desc.equals(node.desc);
    }

    public boolean equalsMethodNode(MethodInsnNode node) {
        return this.get().equals(node.name) && this.desc.equals(node.desc);
    }
    public static boolean isWorkingspaceMode() {
        return com.mega.endinglib.util.MCMapping.isWorkingspaceMode();
    }

    public String get() {
        return isWorkingspaceMode() ? workspace : normal;
    }
}
