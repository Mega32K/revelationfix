package com.mega.revelationfix.common.init;

import com.mega.revelationfix.common.structures.church.ChurchStructure;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import z1gned.goetyrevelation.ModMain;

public class ModStructureTypes {
    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES = DeferredRegister.create(Registries.STRUCTURE_TYPE, ModMain.MODID);
    public static final RegistryObject<StructureType<ChurchStructure>> CHURCH = STRUCTURE_TYPES.register("church", ()-> ()-> ChurchStructure.CODEC);
}
