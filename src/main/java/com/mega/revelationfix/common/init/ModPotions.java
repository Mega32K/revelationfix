package com.mega.revelationfix.common.init;

import com.Polarice3.Goety.common.items.ModItems;
import com.mega.revelationfix.Revelationfix;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModPotions {
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, Revelationfix.MODID);
    public static final RegistryObject<Potion> COUNTERSPELL = POTIONS.register("counterspell", () -> new Potion(new MobEffectInstance(ModEffects.COUNTERSPELL.get(), 900)));
    public static final RegistryObject<Potion> LONG_COUNTERSPELL = POTIONS.register("long_counterspell", () -> new Potion(new MobEffectInstance(ModEffects.COUNTERSPELL.get(), 2400)));
    public static final RegistryObject<Potion> STRONG_COUNTERSPELL = POTIONS.register("strong_counterspell", () -> new Potion(new MobEffectInstance(ModEffects.COUNTERSPELL.get(), 450, 2)));

    public static void register(IEventBus bus) {
        POTIONS.register(bus);
    }

    public static void registerMix() {
        PotionBrewing.addMix(Potions.AWKWARD, ModItems.EMPTY_FOCUS.get(), ModPotions.COUNTERSPELL.get());
        PotionBrewing.addMix(ModPotions.COUNTERSPELL.get(), Items.REDSTONE, ModPotions.LONG_COUNTERSPELL.get());
        PotionBrewing.addMix(ModPotions.COUNTERSPELL.get(), Items.GLOWSTONE_DUST, ModPotions.STRONG_COUNTERSPELL.get());
    }
}