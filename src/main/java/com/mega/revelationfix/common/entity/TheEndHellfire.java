package com.mega.revelationfix.common.entity;

import com.Polarice3.Goety.common.entities.projectiles.Hellfire;
import com.mega.revelationfix.client.renderer.entity.HellfireTextures;
import com.mega.revelationfix.common.init.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class TheEndHellfire extends Hellfire {
    public TheEndHellfire(EntityType<? extends Entity> p_i50170_1_, Level p_i50170_2_) {
        super(p_i50170_1_, p_i50170_2_);
    }

    public TheEndHellfire(Level world, double pPosX, double pPosY, double pPosZ, @Nullable LivingEntity owner) {
        this(ModEntities.THE_END_HELLFIRE.get(), world);
        this.setOwner(owner);
        this.setPos(pPosX, pPosY, pPosZ);
    }

    public TheEndHellfire(Level world, BlockPos blockPos, @Nullable LivingEntity owner) {
        this(ModEntities.THE_END_HELLFIRE.get(), world);
        this.setOwner(owner);
        this.setPos(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    public TheEndHellfire(Level world, Vec3 vector3d, @Nullable LivingEntity owner) {
        this(ModEntities.THE_END_HELLFIRE.get(), world);
        this.setOwner(owner);
        this.setPos(vector3d.x(), vector3d.y(), vector3d.z());
    }

    @Override
    public void dealDamageTo(LivingEntity target) {
        if (target == getOwner()) return;
        target.invulnerableTime--;
        super.dealDamageTo(target);
        target.invulnerableTime--;
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return HellfireTextures.TEXTURES.getOrDefault(this.getAnimation(), (ResourceLocation) HellfireTextures.TEXTURES.get(0));
    }
}
