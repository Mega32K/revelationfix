package com.mega.revelationfix.safe.entity;

import net.minecraft.world.phys.Vec3;

public interface Apollyon2Interface {
    ApollyonExpandedContext revelaionfix$apollyonEC();

    void revelaionfix$setApollyonEC(ApollyonExpandedContext ec);

    void revelaionfix$setApollyonHealth(float health);

    float revelaionfix$getApollyonHealth();

    void revelaionfix$setHitCooldown(int var1);

    int revelaionfix$getHitCooldown();

    Vec3[] revelaionfix$getIllusionOffsets(float partialTicks);

    boolean revelaionfix$illusionMode();

    void revelaionfix$setIllusionMode(boolean z);

    int getDeathTime();

    void setDeathTime(int time);
}
