package com.mega.revelationfix.common.init;

import com.mega.revelationfix.common.enchantment.RealityPiercerEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import z1gned.goetyrevelation.ModMain;

public class ModEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, ModMain.MODID);
    public static final RealityPiercerEnchantment REALITY_PIERCER_ENCHANTMENT = new RealityPiercerEnchantment();
    public static final RegistryObject<Enchantment> REALITY_PIERCER = ENCHANTMENTS.register("reality_piercer", () -> REALITY_PIERCER_ENCHANTMENT);
}
