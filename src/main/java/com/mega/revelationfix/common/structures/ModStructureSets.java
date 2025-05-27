package com.mega.revelationfix.common.structures;

import com.mega.revelationfix.common.structures.church.ChurchPlacement;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import z1gned.goetyrevelation.ModMain;

public class ModStructureSets {
    public static final ResourceKey<StructureSet> CHURCH_SET = register("church");
    public static void bootstrap(BootstapContext<StructureSet> pContext) {
        HolderGetter<Structure> holdergetter = pContext.lookup(Registries.STRUCTURE);
        pContext.register(
                ModStructureSets.CHURCH_SET,
                new StructureSet(holdergetter.getOrThrow(ModStructures.CHURCH), new ChurchPlacement(232424314))
        );
    }
    private static ResourceKey<StructureSet> register(String pName) {
        return ResourceKey.create(Registries.STRUCTURE_SET, new ResourceLocation(ModMain.MODID, pName));
    }
}
