package com.mega.revelationfix.safe;

import com.Polarice3.Goety.common.entities.projectiles.NetherMeteor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
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

    void revelationfix$barrier(Entity p_213688_1_, LivingEntity livingEntity);

    NetherMeteor revelationfix$getNetherMeteor();

    void revelationfix$launch(Entity p_213688_1_, LivingEntity livingEntity);

    void revelationfix$serverRoarParticles();

    void revelationfix$teleportTowards(Entity entity);
}
