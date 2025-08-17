package com.mega.revelationfix.common.block;

import com.Polarice3.Goety.common.items.ModItems;
import com.mega.revelationfix.common.init.ModBlocks;
import com.mega.revelationfix.common.init.ModParticleTypes;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RunestoneEngravedTableBlock extends Block implements ICoreInlaidBlock {
    public static final BooleanProperty IS_STRUCTURE_PLACED = BooleanProperty.create("structure_placed");
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final IntegerProperty CORE = IntegerProperty.create("core", 0, 4);
    protected static final VoxelShape BASE_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 13.0D, 16.0D);
    protected static final VoxelShape CORE_SHAPE = Block.box(4.0D, 13.0D, 4.0D, 12.0D, 16.0D, 12.0D);
    protected static final VoxelShape FULL_SHAPE = Shapes.or(BASE_SHAPE, CORE_SHAPE);
    public Object2IntArrayMap<Item> coreMap0 = new Object2IntArrayMap<>();
    public Int2ObjectOpenHashMap<Item> coreMap1 = new Int2ObjectOpenHashMap<>();

    public RunestoneEngravedTableBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_PURPLE)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .sound(SoundType.STONE)
                .lightLevel(state -> state.getValue(CORE) > 0 ? 9 : 0)
                .strength(1.5F, 3600000.0F)
        );
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(CORE, 0).setValue(IS_STRUCTURE_PLACED, false));
    }

    public boolean useShapeForLightOcclusion(@NotNull BlockState p_53079_) {
        return true;
    }

    public @NotNull VoxelShape getShape(BlockState p_53073_, @NotNull BlockGetter p_53074_, @NotNull BlockPos p_53075_, @NotNull CollisionContext p_53076_) {
        return p_53073_.getValue(CORE) > 0 ? FULL_SHAPE : BASE_SHAPE;
    }

    public BlockState getStateForPlacement(BlockPlaceContext p_53052_) {
        return this.defaultBlockState().setValue(FACING, p_53052_.getHorizontalDirection().getOpposite()).setValue(CORE, 0);
    }

    public @NotNull BlockState rotate(BlockState p_53068_, Rotation p_53069_) {
        return p_53068_.setValue(FACING, p_53069_.rotate(p_53068_.getValue(FACING)));
    }

    public @NotNull BlockState mirror(BlockState p_53065_, Mirror p_53066_) {
        return p_53065_.rotate(p_53066_.getRotation(p_53065_.getValue(FACING)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_53071_) {
        p_53071_.add(FACING, CORE, IS_STRUCTURE_PLACED);
    }

    @Override
    public void animateTick(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource randomSource) {
        if (blockState.getValue(CORE) <= 0)
            return;
        VoxelShape voxelshape = this.getShape(blockState, level, pos, CollisionContext.empty());
        Vec3 vec3 = voxelshape.bounds().getCenter();

        for (int i = 0; i < 2; ++i) {
            double d0 = (double) pos.getX() + (randomSource.nextBoolean() ? voxelshape.bounds().minX : voxelshape.bounds().maxX);
            double d1 = (double) pos.getZ() + (randomSource.nextBoolean() ? voxelshape.bounds().minZ : voxelshape.bounds().maxZ);

            level.addParticle(ModParticleTypes.FROST_FLOWER.get(), d0 + randomSource.nextDouble() / 5.0D, (double) pos.getY() + (0.5D - randomSource.nextDouble()), d1 + randomSource.nextDouble() / 5.0D, 0.0D, 0.1D, 0.0D);

        }

    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult blockHitResult) {
        if (blockState.is(ModBlocks.RUNESTONE_ENGRAVED_TABLE.get())) {
            ItemStack handItem = player.getItemInHand(hand);
            if (level.isClientSide) {
                if (blockState.getValue(CORE) == 0 && isCore(handItem.getItem())) {
                    return InteractionResult.SUCCESS;
                } else if (blockState.getValue(CORE) > 0) {
                    return InteractionResult.SUCCESS;
                } else return InteractionResult.FAIL;
            } else {
                if (blockState.getValue(CORE) == 0 && isCore(handItem.getItem())) {
                    BlockState blockstate1 = blockState.setValue(CORE, getCore(handItem.getItem()));
                    Block.pushEntitiesUp(blockState, blockstate1, level, blockPos);
                    level.setBlock(blockPos, blockstate1, 2);
                    handItem.shrink(1);
                    level.levelEvent(1503, blockPos, 0);
                    return InteractionResult.SUCCESS;
                } else if (blockState.getValue(CORE) > 0) {
                    player.addItem(getCore(blockState.getValue(CORE)).getDefaultInstance());
                    BlockState blockstate1 = blockState.setValue(CORE, 0);
                    level.setBlock(blockPos, blockstate1, 2);
                    level.levelEvent(232424314, blockPos, 0);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return super.use(blockState, level, blockPos, player, hand, blockHitResult);
    }

    @Override
    public @NotNull List<ItemStack> getDrops(@NotNull BlockState blockState, LootParams.@NotNull Builder builder) {
        List<ItemStack> stacks = super.getDrops(blockState, builder);
        if (blockState.getValue(CORE) > 0) {
            if (stacks.stream().noneMatch(stack -> stack.getItem() == getCore(blockState.getValue(CORE))))
                stacks.add(getCore(blockState.getValue(CORE)).getDefaultInstance());
        }
        return stacks;
    }

    public int getCore(Item item) {
        checkMap();
        return coreMap0.getOrDefault(item, -1);
    }

    public Item getCore(int core) {
        checkMap();
        return coreMap1.getOrDefault(core, Items.AIR);
    }

    @Override
    public boolean isCore(Item item) {
        checkMap();
        return coreMap0.containsKey(item);
    }

    public void checkMap() {
        if (coreMap1 == null || coreMap1.isEmpty()) {
            coreMap1 = new Int2ObjectOpenHashMap<>();
            coreMap1.put(1, ModItems.ANIMATION_CORE.get());
            coreMap1.put(2, ModItems.HUNGER_CORE.get());
            coreMap1.put(3, ModItems.MYSTIC_CORE.get());
            coreMap1.put(4, ModItems.WIND_CORE.get());
        }
        if (coreMap0 == null || coreMap0.isEmpty()) {
            coreMap0 = new Object2IntArrayMap<>();
            coreMap0.put(ModItems.ANIMATION_CORE.get(), 1);
            coreMap0.put(ModItems.HUNGER_CORE.get(), 2);
            coreMap0.put(ModItems.MYSTIC_CORE.get(), 3);
            coreMap0.put(ModItems.WIND_CORE.get(), 4);
        }
    }

    public void insertCore(BlockState state, int core) {
        if (core > 0) {
            state.setValue(CORE, core);
        }
    }

    @Override
    public boolean canEntityDestroy(BlockState state, BlockGetter level, BlockPos pos, Entity entity) {
        if (!(entity instanceof Player player) || !player.isCreative()) {
            if (state.getValue(IS_STRUCTURE_PLACED)) {
                return false;
            }
        }
        return super.canEntityDestroy(state, level, pos, entity);
    }

    @Override
    public float getDestroyProgress(BlockState state, Player player, BlockGetter blockGetter, BlockPos pos) {
        if (!player.isCreative()) {
            if (state.getValue(IS_STRUCTURE_PLACED)) {
                return 0F;
            }
        }
        return super.getDestroyProgress(state, player, blockGetter, pos);
    }
}
