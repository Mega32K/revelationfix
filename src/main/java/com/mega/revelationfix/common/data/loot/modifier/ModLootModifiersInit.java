package com.mega.revelationfix.common.data.loot.modifier;
import com.mojang.serialization.Codec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import z1gned.goetyrevelation.ModMain;

public class ModLootModifiersInit {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> GLOBAL_LOOT_MODIFIER = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, ModMain.MODID);

    public static final RegistryObject<Codec<SOOSpecialDeathLootModifier>> INSTANT_PICK_MODIFIER = GLOBAL_LOOT_MODIFIER.register("special_loot_entity", SOOSpecialDeathLootModifier.CODEC);

}
