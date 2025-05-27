package com.mega.revelationfix.common.init;

import com.mega.revelationfix.common.structures.church.ChurchPieces;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import z1gned.goetyrevelation.ModMain;

import java.util.Locale;

public class ModStructurePieceTypes {
    // 注册器
    public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECE_TYPES = DeferredRegister.create(Registries.STRUCTURE_PIECE, ModMain.MODID);
    // 注册的对象
    public static final RegistryObject<StructurePieceType> CHURCH = registerPieceType("church", ChurchPieces.ChurchPiece::new);
    // 辅助注册的方法
    private static RegistryObject<StructurePieceType> registerPieceType(String name, StructurePieceType.StructureTemplateType structurePieceType) {
        return STRUCTURE_PIECE_TYPES.register(name.toLowerCase(Locale.ROOT), () -> structurePieceType);
    }
}
