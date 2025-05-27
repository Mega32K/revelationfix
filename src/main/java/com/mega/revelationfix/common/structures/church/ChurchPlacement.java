package com.mega.revelationfix.common.structures.church;

import com.google.common.collect.ImmutableMap;
import com.mega.revelationfix.common.init.ModStructurePlacementTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.core.Vec3i;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.minecraftforge.common.BiomeManager;
import org.joml.Vector2i;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class ChurchPlacement extends StructurePlacement {
    public static final Map<Integer, Vector2i> map = Util.make(() -> {
        ImmutableMap.Builder<Integer, Vector2i> temp = new ImmutableMap.Builder<>();
        temp.put(41, new Vector2i(41));
        temp.put(416, new Vector2i(416, -417));
        temp.put(-417, new Vector2i(416, -417));
        temp.put(4162, new Vector2i(4162, -4163));
        temp.put(-4163, new Vector2i(4162, -4163));
        temp.put(41625, new Vector2i(41625, -41625));
        temp.put(-41625, new Vector2i(41625, -41625));
        return temp.build();
    });
    public static final Codec<ChurchPlacement> CODEC = ExtraCodecs.validate(RecordCodecBuilder.mapCodec((p_204996_) -> {
        return placementCodec(p_204996_).apply(p_204996_, ChurchPlacement::new);
    }), ChurchPlacement::validate).codec();

    private static DataResult<ChurchPlacement> validate(ChurchPlacement p_286361_) {
        return DataResult.success(p_286361_);
    }

    public ChurchPlacement(Vec3i p_227000_, StructurePlacement.FrequencyReductionMethod p_227001_, float p_227002_, int p_227003_, Optional<StructurePlacement.ExclusionZone> p_227004_) {
        super(p_227000_, p_227001_, p_227002_, p_227003_, p_227004_);
    }

    public ChurchPlacement(int p_204983_) {
        this(Vec3i.ZERO, StructurePlacement.FrequencyReductionMethod.DEFAULT, 1.0F, p_204983_, Optional.empty());
    }

    private boolean in(int toCheck, Integer i) {
        return i.equals(toCheck);
    }
    protected boolean isPlacementChunk(ChunkGeneratorStructureState p_256267_, int x, int z) {
        AtomicBoolean should = new AtomicBoolean(false);
        map.forEach((i1, i2) -> {
            if (in(x, i1) && (in(z, i2.x) || in(z, i2.y)))
                should.set(true);
        });
        return should.get();
    }

    public StructurePlacementType<?> type() {
        return ModStructurePlacementTypes.CHURCH.get();
    }
}
