package com.mega.revelationfix.common.structures;

import com.mega.revelationfix.common.structures.church.ChurchStructure;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.StructureBlock;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import z1gned.goetyrevelation.ModMain;

import java.util.Map;

public class ModStructures {
    public static Structure.StructureSettings structure(
            HolderSet<Biome> pBiomes, Map<MobCategory, StructureSpawnOverride> pSpawnOverrides, GenerationStep.Decoration pStep, TerrainAdjustment pTerrainAdaptation) {
        return new Structure.StructureSettings(pBiomes, pSpawnOverrides, pStep, pTerrainAdaptation);
    }
    private static Structure.StructureSettings structure(HolderSet<Biome> pBiomes, GenerationStep.Decoration pStep, TerrainAdjustment pTerrainAdaptation) {
        return structure(pBiomes, Map.of(), pStep, pTerrainAdaptation);
    }

    private static Structure.StructureSettings structure(HolderSet<Biome> pBiomes, TerrainAdjustment pTerrainAdaptation) {
        return structure(pBiomes, Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, pTerrainAdaptation);
    }
    public static final ResourceKey<Structure> CHURCH = registerKey("church");
    public static ResourceKey<Structure> registerKey(String name) {
        return ResourceKey.create(Registries.STRUCTURE, new ResourceLocation(ModMain.MODID, name));
    }
    public static void bootstrap(BootstapContext<Structure> context) {
        // 从上下文中获得所有的生物群系
        HolderGetter<Biome> biomeHolderGetter = context.lookup(Registries.BIOME);
        // 第一个参数是key
        // 第二个参数就是我们的strucutre的构造
        // 构造的要传入一个StructureSettings
        // setting的一个参数是生物群系，我们指定了生物群系是主世界，对于第二个参数设置的是TerrainAdjustment.NONE，当然你也可以选择其他的。

        context.register(
                ModStructures.CHURCH,
                new ChurchStructure(structure(biomeHolderGetter.getOrThrow(BiomeTags.IS_NETHER), GenerationStep.Decoration.UNDERGROUND_STRUCTURES, TerrainAdjustment.BURY))
        );

    }
}
