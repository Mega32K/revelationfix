package com.mega.revelationfix.common.init;

import com.mega.revelationfix.common.structures.church.ChurchPlacement;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import z1gned.goetyrevelation.ModMain;

public class ModStructurePlacementTypes {
    public static final DeferredRegister<StructurePlacementType<?>> STRUCTURE_PLACEMENT_TYPES = DeferredRegister.create(Registries.STRUCTURE_PLACEMENT, ModMain.MODID);
    public static final RegistryObject<StructurePlacementType<ChurchPlacement>> CHURCH = STRUCTURE_PLACEMENT_TYPES.register("church_placement", () -> () -> ChurchPlacement.CODEC);
}
