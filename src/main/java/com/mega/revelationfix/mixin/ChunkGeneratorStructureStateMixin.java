package com.mega.revelationfix.mixin;

import com.mega.revelationfix.common.structures.ModStructureSets;
import com.mega.revelationfix.common.structures.church.ChurchPlacement;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkGeneratorStructureState.class)
public class ChunkGeneratorStructureStateMixin {
    @Inject(method = "hasBiomesForStructureSet", at = @At("HEAD"))
    private static void hasBiomesForStructureSet(StructureSet p_255766_, BiomeSource p_256424_, CallbackInfoReturnable<Boolean> cir) {

    }
}
