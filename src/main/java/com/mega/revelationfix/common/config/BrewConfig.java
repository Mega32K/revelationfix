package com.mega.revelationfix.common.config;

import com.Polarice3.Goety.common.effects.GoetyEffects;
import com.mega.revelationfix.Revelationfix;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class BrewConfig {
    public static int defaultModified(MobEffect effect) {
        if (effect == MobEffects.DAMAGE_RESISTANCE)
            return 3;
        else if (effect == GoetyEffects.EXPLOSIVE.get())
            return 3;
        else return 127;
    }
    public static int maxAmplier(MobEffect effect) {
        return defaultModified(effect);
    }
}
