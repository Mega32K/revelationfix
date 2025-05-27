package com.mega.revelationfix.common.apollyon.common;

import com.mega.revelationfix.Revelationfix;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public class ExtraDamageTypes {
    public static ResourceKey<DamageType> ARROW = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(Revelationfix.MODID, "doom_arrow"));
    public static ResourceKey<DamageType> QUIETUS = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(Revelationfix.MODID, "quietus"));
    public static ResourceKey<DamageType> FE_POWER = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(Revelationfix.MODID, "fe_power"));
}
