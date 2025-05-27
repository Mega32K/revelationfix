package com.mega.revelationfix.common.init;

import com.mega.revelationfix.Revelationfix;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Revelationfix.MODID);
    public static final RegistryObject<SoundEvent> APOLLYON_NEW_NETHER_THEME = SOUNDS.register("apollyon_new_nether_theme", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Revelationfix.MODID, "apollyon_new_nether_theme")));
    public static final RegistryObject<SoundEvent> QUIETUS_BEAM = SOUNDS.register("quietus_beam", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Revelationfix.MODID, "quietus_beam")));
    public static final RegistryObject<SoundEvent> STAR_CAST = SOUNDS.register("star_arrow_cast", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Revelationfix.MODID, "star_arrow_cast")));
    public static final RegistryObject<SoundEvent> STAR_EXPLODE = SOUNDS.register("star_arrow_explode", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Revelationfix.MODID, "star_arrow_explode")));
    public static final RegistryObject<SoundEvent> TIME_STOP = SOUNDS.register("time_stop", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Revelationfix.MODID, "time_stop")));

}
