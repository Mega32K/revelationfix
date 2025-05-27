package com.mega.revelationfix.common.structures.church;

import com.mega.revelationfix.common.init.ModStructureTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ChurchStructure extends Structure {
    public static Codec<ChurchStructure> CODEC = RecordCodecBuilder.create((p_226611_) -> {
        return p_226611_.group(settingsCodec(p_226611_)).apply(p_226611_, ChurchStructure::new);
    });;
    public ChurchStructure(StructureSettings structureSettings) {
        super(structureSettings);
    }

    @Override
    protected @NotNull Optional<GenerationStub> findGenerationPoint(GenerationContext generationContext) {
        return onTopOfChunkCenter(generationContext, Heightmap.Types.WORLD_SURFACE_WG, builder -> this.generatePieces(builder, generationContext));

    }
    private void generatePieces(StructurePiecesBuilder builder, GenerationContext generationContext) {
        ChunkPos chunkpos = generationContext.chunkPos();
        WorldgenRandom worldgenrandom = generationContext.random();
        BlockPos blockpos = new BlockPos(chunkpos.getMinBlockX(), 128 - 10, chunkpos.getMinBlockZ());
        Rotation rotation = Rotation.getRandom(worldgenrandom);
        ChurchPieces.addPieces(generationContext.structureTemplateManager(), blockpos, rotation, builder, worldgenrandom);
    }
    @Override
    public @NotNull StructureType<?> type() {
        return ModStructureTypes.CHURCH.get();
    }
}
