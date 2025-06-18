package com.mega.revelationfix.util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public enum MCMapping {
    WITHER_BOSS$FIELD$bossEvent("bossEvent", "f_31430_", ""),
    FontManager$FIELD$fontSets("fontSets", "f_94999_", ""),
    FontManager$FIELD$renames("renames", "f_95001_", ""),
    FontManager$FIELD$missingFontSet("missingFontSet", "f_94998_", ""),
    FontManager$METHOD$getActualId("getActualId", "m_284164_", "(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/resources/ResourceLocation;"),
    DamageTypes$METHOD$bootstrap("bootstrap", "m_269594_", "(Lnet/minecraft/data/worldgen/BootstapContext;)V"),
    GameRenderer$FIELD$zoom("zoom", "f_109077_", ""),
    GameRenderer$FIELD$zoomX("zoomX", "f_109078_", ""),
    GameRenderer$FIELD$zoomY("zoomY", "f_109079_", ""),
    GameRenderer$FIELD$panoramicMode("panoramicMode", "f_109076_", ""),
    GameRenderer$FIELD$oldFov("oldFov", "f_109067_", ""),
    GameRenderer$FIELD$fov("fov", "f_109066_", ""),
    LevelRenderer$FIELD$captureFrustum("captureFrustum", "f_109441_", ""),
    LevelRenderer$Method$captureFrustum("captureFrustum", "m_252964_", "(Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;DDDLnet/minecraft/client/renderer/culling/Frustum;)V"),
    LevelRenderer$FIELD$capturedFrustum("capturedFrustum", "f_109442_", ""),
    LevelRenderer$FIELD$frustumPos("capturedFrustum", "f_109444_", ""),
    LevelRenderer$FIELD$cullingFrustum("cullingFrustum", "f_172938_", ""),
    LivingEntity$FIELD$DATA_HEALTH_ID("DATA_HEALTH_ID", "f_20961_", ""),
    AbstractTickableSoundInstance$Field$stopped("stopped", "f_119604_", ""),
    PlayerRenderer$METHOD$getArmPose("getArmPose", "m_117794_", "(Lnet/minecraft/client/player/AbstractClientPlayer;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/client/model/HumanoidModel$ArmPose;"),
    Font$FIELD$filterFishyGlyphs("filterFishyGlyphs", "f_242994_", ""),
    Font$METHOD$getFontSet("getFontSet", "m_92863_", "(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/gui/font/FontSet;"),
    Font$METHOD$renderChar("renderChar", "m_253238_", "(Lnet/minecraft/client/gui/font/glyphs/BakedGlyph;ZZFFFLorg/joml/Matrix4f;Lcom/mojang/blaze3d/vertex/VertexConsumer;FFFFI)V"),
    LivingEntity$METHOD$getSoundVolume("getSoundVolume", "m_6121_", "()F"),
    LivingEntity$METHOD$checkTotemDeathProtection("checkTotemDeathProtection", "m_21262_", "(Lnet/minecraft/world/damagesource/DamageSource;)Z"),
    LivingEntity$METHOD$getDeathSound("getDeathSound", "m_5592_", "()Lnet/minecraft/sounds/SoundEvent;"),
    LivingEntity$METHOD$playHurtSound("playHurtSound", "m_6677_", "(Lnet/minecraft/world/damagesource/DamageSource;)V"),

    LivingEntity$METHOD$dropAllDeathLoot("dropAllDeathLoot", "m_6668_", "(Lnet/minecraft/world/damagesource/DamageSource;)V"),

    LivingEntity$METHOD$createWitherRose("createWitherRose", "m_21268_", "(Lnet/minecraft/world/entity/LivingEntity;)V"),
    Entity$METHOD$markHurt("markHurt", "m_5834_", "()V"),
    LivingEntity$METHOD$hurtHelmet("hurtHelmet", "m_142642_", "(Lnet/minecraft/world/damagesource/DamageSource;F)V"),
    Mob$FIELD$target("target", "f_21362_", ""),
    SynchedEntityData$DataItem$FIELD$value("value", "f_135391_", ""),
    LivingEntity$METHOD$getMaxHealth("getMaxHealth", "m_21233_", "()F"),
    LivingEntity$METHOD$getHealth("getHealth", "m_21223_", "()F"),
    LivingEntity$METHOD$isAlive("isAlive", "m_21223_", "()Z"),
    LivingEntity$METHOD$isDeadOrDying("isDeadOrDying", "m_21224_", "()Z"),
    ChatFormatting$FIELD$CODEC("CODEC", "f_236796_", ""),
    ChatFormatting$FIELD$FORMATTING_BY_NAME("FORMATTING_BY_NAME", "f_126619_", "()Z"),
    LivingEntity$METHOD$aiStep("aiStep", "m_8107_", "()V"),
    LivingEntity$METHOD$setHealth("setHealth", "m_21153_", "(F)V"),
    Entity$METHOD$tick("tick", "m_8119_", "()V"),
    LivingEntity$METHOD$hurt("hurt", "m_6469_", "(Lnet/minecraft/world/damagesource/DamageSource;F)Z");
    public static int isWorkingspace = 0;
    public final String workspace;
    public final String normal;
    public final String desc;

    MCMapping(String workspace, String normal, String desc) {
        this.workspace = workspace;
        this.normal = normal;
        this.desc = desc;
    }

    public static boolean isWorkingspaceMode() {
        if (isWorkingspace == 0) {
            if (isDevelopmentEnvironment()) {
                isWorkingspace = 1;
            } else isWorkingspace = 2;
        }
        return isWorkingspace == 1;
    }
    public static boolean isDevelopmentEnvironment() {
        Path projectDir = Paths.get(System.getProperty("user.dir")).getParent();
        return Files.exists(projectDir.resolve(".gradle")) &&
                Files.exists(projectDir.resolve("build"));
    }

    public String get() {
        return isWorkingspaceMode() ? workspace : normal;
    }
}
