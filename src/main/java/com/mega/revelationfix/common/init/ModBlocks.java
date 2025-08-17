package com.mega.revelationfix.common.init;

import com.mega.revelationfix.common.block.RuneReactorBlock;
import com.mega.revelationfix.common.block.RunestoneEngravedTableBlock;
import com.mega.revelationfix.common.block.blockentity.RuneReactorBlockEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import z1gned.goetyrevelation.ModMain;

/**
 * invoked in {@link GRItems}
 */
public class ModBlocks {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ModMain.MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ModMain.MODID);
    public static final RegistryObject<Block> RUNESTONE_ENGRAVED_TABLE = BLOCKS.register("runestone_engraved_table", RunestoneEngravedTableBlock::new);
    public static final RegistryObject<Block> RUNE_REACTOR = BLOCKS.register("rune_reactor", RuneReactorBlock::new);

    public static RegistryObject<BlockItem> asBLockItem(DeferredRegister<Item> deferredRegister, RegistryObject<Block> ro) {
        return deferredRegister.register(ro.getId().getPath(), () -> new BlockItem(ro.get(), new Item.Properties()));
    }    public static final RegistryObject<BlockEntityType<RuneReactorBlockEntity>> RUNE_REACTOR_ENTITY = BLOCK_ENTITIES.register("rune_reactor", () ->
            BlockEntityType.Builder.of(
                    RuneReactorBlockEntity::new,
                    RUNE_REACTOR.get()
            ).build(null)
    );

    public static RegistryObject<BlockItem> asBLockItem(DeferredRegister<Item> deferredRegister, RegistryObject<Block> ro, Item.Properties properties) {
        return deferredRegister.register(ro.getId().getPath(), () -> new BlockItem(ro.get(), properties));
    }


}
