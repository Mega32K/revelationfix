package com.mega.revelationfix.common.init;

import com.mega.revelationfix.Revelationfix;
import com.mega.revelationfix.common.effect.CounterspellMobEffect;
import com.mega.revelationfix.common.effect.QuietusEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Revelationfix.MODID);
    public static RegistryObject<MobEffect> QUIETUS = EFFECTS.register("quietus", QuietusEffect::new);
    public static RegistryObject<MobEffect> COUNTERSPELL = EFFECTS.register("counterspell", CounterspellMobEffect::new);
}
