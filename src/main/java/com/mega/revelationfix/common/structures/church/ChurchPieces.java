package com.mega.revelationfix.common.structures.church;

import com.Polarice3.Goety.common.blocks.ModBlocks;
import com.Polarice3.Goety.common.blocks.entities.LoftyChestBlockEntity;
import com.Polarice3.Goety.common.items.ModItems;
import com.google.common.collect.ImmutableMap;
import com.mega.revelationfix.common.data.loot.ModLootTables;
import com.mega.revelationfix.common.init.ModStructurePieceTypes;
import com.mega.revelationfix.mixin.DecoratedPotBlockEntityAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import org.jetbrains.annotations.NotNull;
import z1gned.goetyrevelation.ModMain;

import java.util.Map;

public class ChurchPieces {
    public static ResourceLocation STRUCTURE_LOCATION_CHURCH_1 = new ResourceLocation(ModMain.MODID, "church_1");
    public static ResourceLocation STRUCTURE_LOCATION_CHURCH_2 = new ResourceLocation(ModMain.MODID, "church_2");
    public static ResourceLocation STRUCTURE_LOCATION_CHURCH_3 = new ResourceLocation(ModMain.MODID, "church_3");
    public static ResourceLocation STRUCTURE_LOCATION_CHURCH_4 = new ResourceLocation(ModMain.MODID, "church_4");
    public static ResourceLocation STRUCTURE_LOCATION_CHURCH_5 = new ResourceLocation(ModMain.MODID, "church_5");
    public static ResourceLocation STRUCTURE_LOCATION_CHURCH_6 = new ResourceLocation(ModMain.MODID, "church_6");
    public static ResourceLocation STRUCTURE_LOCATION_CHURCH_7 = new ResourceLocation(ModMain.MODID, "church_7");
    public static ResourceLocation STRUCTURE_LOCATION_CHURCH_8 = new ResourceLocation(ModMain.MODID, "church_8");
    public static final Map<ResourceLocation, BlockPos> PIVOTS = ImmutableMap.of(
            STRUCTURE_LOCATION_CHURCH_1, BlockPos.ZERO,
            STRUCTURE_LOCATION_CHURCH_2, BlockPos.ZERO,
            STRUCTURE_LOCATION_CHURCH_3, BlockPos.ZERO,
            STRUCTURE_LOCATION_CHURCH_4, BlockPos.ZERO,
            STRUCTURE_LOCATION_CHURCH_5, BlockPos.ZERO,
            STRUCTURE_LOCATION_CHURCH_6, BlockPos.ZERO,
            STRUCTURE_LOCATION_CHURCH_7, BlockPos.ZERO,
            STRUCTURE_LOCATION_CHURCH_8, BlockPos.ZERO
    );
    public static Map<ResourceLocation, BlockPos> OFFSETS = ImmutableMap.of(
            STRUCTURE_LOCATION_CHURCH_1, new BlockPos(-48, 0, 0),
            STRUCTURE_LOCATION_CHURCH_2, new BlockPos(-48, 0, 48),
            STRUCTURE_LOCATION_CHURCH_3, BlockPos.ZERO,
            STRUCTURE_LOCATION_CHURCH_4, new BlockPos(0, 0, 48),
            STRUCTURE_LOCATION_CHURCH_5, new BlockPos(-48, 48 - 1, 0),
            STRUCTURE_LOCATION_CHURCH_6, new BlockPos(-48, 48 - 1, 48),
            STRUCTURE_LOCATION_CHURCH_7, new BlockPos(0, 48 - 1, 0),
            STRUCTURE_LOCATION_CHURCH_8, new BlockPos(1, 48 - 1, 48)
    );

    public static void addPieces(StructureTemplateManager pStructureTemplateManager, BlockPos pStartPos, Rotation pRotation, StructurePieceAccessor pPieces, RandomSource pRandom) {
        OFFSETS = ImmutableMap.of(
                STRUCTURE_LOCATION_CHURCH_1, new BlockPos(-48, 0, 0),
                STRUCTURE_LOCATION_CHURCH_2, new BlockPos(-48, 0, 48),
                STRUCTURE_LOCATION_CHURCH_3, BlockPos.ZERO,
                STRUCTURE_LOCATION_CHURCH_4, new BlockPos(0, 0, 48),
                STRUCTURE_LOCATION_CHURCH_5, new BlockPos(-48, 48 - 1, 0),
                STRUCTURE_LOCATION_CHURCH_6, new BlockPos(-48, 48 - 1, 48),
                STRUCTURE_LOCATION_CHURCH_7, new BlockPos(0, 48 - 1, 0),
                STRUCTURE_LOCATION_CHURCH_8, new BlockPos(1, 48 - 1, 48)
        );
        int x = pStartPos.getX();
        int z = pStartPos.getZ();
        BlockPos rotationOffSet;
        BlockPos blockpos;
        {
            rotationOffSet = OFFSETS.get(STRUCTURE_LOCATION_CHURCH_1).rotate(pRotation);
            blockpos = rotationOffSet.offset(x, pStartPos.getY(), z);
            pPieces.addPiece(new ChurchPiece(pStructureTemplateManager, STRUCTURE_LOCATION_CHURCH_1, blockpos, pRotation, 1));
        }
        {
            rotationOffSet = OFFSETS.get(STRUCTURE_LOCATION_CHURCH_2).rotate(pRotation);
            blockpos = rotationOffSet.offset(x, pStartPos.getY(), z);
            pPieces.addPiece(new ChurchPiece(pStructureTemplateManager, STRUCTURE_LOCATION_CHURCH_2, blockpos, pRotation, 1));
        }
        {
            rotationOffSet = OFFSETS.get(STRUCTURE_LOCATION_CHURCH_3).rotate(pRotation);
            blockpos = rotationOffSet.offset(x, pStartPos.getY(), z);
            pPieces.addPiece(new ChurchPiece(pStructureTemplateManager, STRUCTURE_LOCATION_CHURCH_3, blockpos, pRotation, 1));
        }
        {
            rotationOffSet = OFFSETS.get(STRUCTURE_LOCATION_CHURCH_4).rotate(pRotation);
            blockpos = rotationOffSet.offset(x, pStartPos.getY(), z);
            pPieces.addPiece(new ChurchPiece(pStructureTemplateManager, STRUCTURE_LOCATION_CHURCH_4, blockpos, pRotation, 1));
        }
        {
            rotationOffSet = OFFSETS.get(STRUCTURE_LOCATION_CHURCH_5).rotate(pRotation);
            blockpos = rotationOffSet.offset(x, pStartPos.getY(), z);
            pPieces.addPiece(new ChurchPiece(pStructureTemplateManager, STRUCTURE_LOCATION_CHURCH_5, blockpos, pRotation, 1));
        }
        {
            rotationOffSet = OFFSETS.get(STRUCTURE_LOCATION_CHURCH_6).rotate(pRotation);
            blockpos = rotationOffSet.offset(x, pStartPos.getY(), z);
            pPieces.addPiece(new ChurchPiece(pStructureTemplateManager, STRUCTURE_LOCATION_CHURCH_6, blockpos, pRotation, 1));
        }
        {
            rotationOffSet = OFFSETS.get(STRUCTURE_LOCATION_CHURCH_7).rotate(pRotation);
            blockpos = rotationOffSet.offset(x, pStartPos.getY(), z);
            pPieces.addPiece(new ChurchPiece(pStructureTemplateManager, STRUCTURE_LOCATION_CHURCH_7, blockpos, pRotation, 1));
        }
        {
            rotationOffSet = OFFSETS.get(STRUCTURE_LOCATION_CHURCH_8).rotate(pRotation);
            blockpos = rotationOffSet.offset(x, pStartPos.getY(), z);
            pPieces.addPiece(new ChurchPiece(pStructureTemplateManager, STRUCTURE_LOCATION_CHURCH_8, blockpos, pRotation, 1));
        }

    }

    public static class ChurchPiece extends TemplateStructurePiece {
        public ResourceLocation rl;

        public ChurchPiece(StructureTemplateManager pStructureTemplateManager, ResourceLocation pLocation, BlockPos pStartPos, Rotation pRotation, int pDown) {
            super(ModStructurePieceTypes.CHURCH.get(), 0, pStructureTemplateManager, pLocation, pLocation.toString(),
                    makeSettings(pRotation, pLocation),
                    makePosition(pLocation, pStartPos, pDown)
            );
            this.rl = pLocation;
        }

        public ChurchPiece(StructureTemplateManager pStructureTemplateManager, CompoundTag pTag) {
            super(ModStructurePieceTypes.CHURCH.get(), pTag, pStructureTemplateManager, resourceLocation -> makeSettings(Rotation.valueOf(pTag.getString("Rot")), resourceLocation));
        }

        private static StructurePlaceSettings makeSettings(Rotation pRotation, ResourceLocation pLocation) {
            return new StructurePlaceSettings()
                    .setRotation(pRotation)
                    .setMirror(Mirror.NONE)
                    .addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
        }

        private static BlockPos makePosition(ResourceLocation pLocation, BlockPos pPos, int pDown) {
            return pPos.below(pDown);
        }

        private static Item[] createDecoratedPotItems(RandomSource pRandom) {
            Item[] stacks = new Item[4];
            for (int i = 0; i < 4; i++) {
                Item stack;
                int index = pRandom.nextInt(0, 6);
                if (index == 0)
                    stack = new Item[]{ModItems.ANIMATION_CORE.get(), ModItems.HUNGER_CORE.get(), ModItems.MYSTIC_CORE.get(), ModItems.WIND_CORE.get()}[pRandom.nextInt(0, 4)];
                else if (index == 1) stack = Items.NETHERITE_SCRAP;
                else if (index == 2) stack = Items.QUARTZ;
                else if (index == 3) stack = Items.GOLD_INGOT;
                else if (index == 4) stack = ModItems.SPIDER_EGG.get();
                else stack = Items.ENCHANTED_GOLDEN_APPLE;
                stacks[i] = stack;
            }
            return stacks;
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext p_192690_, CompoundTag p_192691_) {
            p_192691_.putString("Rot", this.placeSettings.getRotation().name());
            super.addAdditionalSaveData(p_192690_, p_192691_);
        }

        @Override
        protected void handleDataMarker(@NotNull String p_226906_, @NotNull BlockPos p_226907_, @NotNull ServerLevelAccessor p_226908_, @NotNull RandomSource p_226909_, @NotNull BoundingBox p_226910_) {

        }

        @Override
        public void postProcess(@NotNull WorldGenLevel pLevel, @NotNull StructureManager p_226900_, @NotNull ChunkGenerator p_226901_, @NotNull RandomSource pRandom, @NotNull BoundingBox boundingBox, @NotNull ChunkPos p_226904_, @NotNull BlockPos pPos) {
            super.postProcess(pLevel, p_226900_, p_226901_, pRandom, boundingBox, p_226904_, pPos);
            try {
                for (StructureTemplate.StructureBlockInfo structuretemplate$structureblockinfo : this.template.filterBlocks(this.templatePosition, this.placeSettings, Blocks.BARREL)) {
                    if (pLevel.getBlockEntity(structuretemplate$structureblockinfo.pos()) instanceof BarrelBlockEntity barrelBlockEntity && barrelBlockEntity.isEmpty()) {
                        if (rl.equals(ChurchPieces.STRUCTURE_LOCATION_CHURCH_1)) {
                            barrelBlockEntity.setLootTable(pRandom.nextBoolean() ? ModLootTables.Chests.CHURCH_FOOD : ModLootTables.Chests.CHURCH_FOOD_2, pRandom.nextLong());
                        } else {
                            int i = pRandom.nextInt(0, 4);
                            if (i < 2) {
                                barrelBlockEntity.setLootTable(BuiltInLootTables.BASTION_BRIDGE, pRandom.nextLong());
                            } else if (i == 2) {
                                barrelBlockEntity.setLootTable(BuiltInLootTables.BASTION_OTHER, pRandom.nextLong());
                            } else {
                                barrelBlockEntity.setLootTable(BuiltInLootTables.BASTION_TREASURE, pRandom.nextLong());
                            }
                        }
                    }
                }
                for (StructureTemplate.StructureBlockInfo structuretemplate$structureblockinfo : this.template.filterBlocks(this.templatePosition, this.placeSettings, Blocks.CHEST)) {
                    BlockPos pos = structuretemplate$structureblockinfo.pos();
                    if (pLevel.getBlockEntity(pos) instanceof ChestBlockEntity chestBlockEntity && chestBlockEntity.isEmpty()) {
                        if (pos.getY() < 128) {
                            if (pRandom.nextBoolean()) {
                                chestBlockEntity.setLootTable(BuiltInLootTables.BASTION_TREASURE, pRandom.nextLong());
                            } else {
                                chestBlockEntity.setLootTable(BuiltInLootTables.ANCIENT_CITY, pRandom.nextLong());
                            }
                        } else {
                            chestBlockEntity.setLootTable(BuiltInLootTables.NETHER_BRIDGE, pRandom.nextLong());
                        }
                    }
                }
                for (StructureTemplate.StructureBlockInfo structuretemplate$structureblockinfo : this.template.filterBlocks(this.templatePosition, this.placeSettings, ModBlocks.LOFTY_CHEST.get())) {
                    BlockPos pos = structuretemplate$structureblockinfo.pos();
                    if (pLevel.getBlockEntity(pos) instanceof LoftyChestBlockEntity chestBlockEntity && chestBlockEntity.isEmpty()) {
                        if (pos.getY() > 128) {
                            if (pRandom.nextBoolean()) {
                                chestBlockEntity.setLootTable(BuiltInLootTables.BASTION_TREASURE, pRandom.nextLong());
                            } else {
                                chestBlockEntity.setLootTable(BuiltInLootTables.ANCIENT_CITY, pRandom.nextLong());
                            }
                        }
                    }
                }
                for (StructureTemplate.StructureBlockInfo structuretemplate$structureblockinfo : this.template.filterBlocks(this.templatePosition, this.placeSettings, Blocks.DECORATED_POT)) {
                    BlockPos pos = structuretemplate$structureblockinfo.pos();
                    if (pLevel.getBlockEntity(pos) instanceof DecoratedPotBlockEntity chestBlockEntity) {
                        DecoratedPotBlockEntityAccessor accessor = (DecoratedPotBlockEntityAccessor) chestBlockEntity;
                        Item[] stacks = createDecoratedPotItems(pRandom);
                        accessor.setDecorations(new DecoratedPotBlockEntity.Decorations(stacks[0], stacks[1], stacks[2], stacks[3]));
                    }
                }

            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }
}
