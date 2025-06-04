package com.mega.revelationfix.common.data;

import com.mega.revelationfix.Revelationfix;
import com.mega.revelationfix.common.structures.ModStructureSets;
import com.mega.revelationfix.common.structures.ModStructures;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import z1gned.goetyrevelation.ModMain;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModWorldGen extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.STRUCTURE_SET, ModStructureSets::bootstrap)
            .add(Registries.STRUCTURE, ModStructures::bootstrap);

    public ModWorldGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(ModMain.MODID));
    }
}
