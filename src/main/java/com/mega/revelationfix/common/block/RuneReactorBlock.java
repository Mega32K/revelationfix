package com.mega.revelationfix.common.block;

import com.Polarice3.Goety.api.items.magic.IFocus;
import com.Polarice3.Goety.api.items.magic.IWand;
import com.Polarice3.Goety.api.magic.ISpell;
import com.Polarice3.Goety.client.particles.CircleExplodeParticleOption;
import com.Polarice3.Goety.common.blocks.entities.CursedCageBlockEntity;
import com.Polarice3.Goety.common.items.ModItems;
import com.mega.revelationfix.common.block.blockentity.RuneReactorBlockEntity;
import com.mega.revelationfix.common.config.BlockConfig;
import com.mega.revelationfix.common.entity.binding.BlockBindingEntity;
import com.mega.revelationfix.common.entity.binding.BlockShakingEntity;
import com.mega.revelationfix.common.entity.binding.FakeSpellerEntity;
import com.mega.revelationfix.common.init.ModBlocks;
import com.mega.revelationfix.safe.entity.EntityExpandedContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class RuneReactorBlock extends BaseEntityBlock {
    public static Set<Ingredient> canUseStructureCoreItems = new HashSet<>();
    public static final int RANGE = 9;
    public static final BooleanProperty IS_STRUCTURE_PLACED = BooleanProperty.create("structure_placed");
    public static final IntegerProperty RITUAL_CORE = IntegerProperty.create("ritual_core", 0, 3);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final VoxelShape BASE_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 13.0D, 16.0D);
    protected static final VoxelShape CORE_SHAPE = Block.box(4.0D, 13.0D, 4.0D, 12.0D, 16.0D, 12.0D);
    protected static final VoxelShape FULL_SHAPE = Shapes.or(BASE_SHAPE, CORE_SHAPE);
    public RuneReactorBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_PURPLE)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .sound(SoundType.STONE)
                .strength(1.5F, 3600000.0F)
        );
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(IS_STRUCTURE_PLACED, false).setValue(RITUAL_CORE, 0));
    }

    public boolean useShapeForLightOcclusion(@NotNull BlockState p_53079_) {
        return true;
    }
    public @NotNull VoxelShape getShape(@NotNull BlockState p_53073_, @NotNull BlockGetter p_53074_, @NotNull BlockPos p_53075_, @NotNull CollisionContext p_53076_) {
        return p_53073_.getValue(RITUAL_CORE) > 0 ? FULL_SHAPE : BASE_SHAPE;
    }
    public BlockState getStateForPlacement(BlockPlaceContext p_53052_) {
        return this.defaultBlockState().setValue(FACING, p_53052_.getHorizontalDirection().getOpposite());
    }
    public @NotNull BlockState rotate(BlockState p_53068_, Rotation p_53069_) {
        return p_53068_.setValue(FACING, p_53069_.rotate(p_53068_.getValue(FACING)));
    }

    public @NotNull BlockState mirror(BlockState p_53065_, Mirror p_53066_) {
        return p_53065_.rotate(p_53066_.getRotation(p_53065_.getValue(FACING)));
    }
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_53071_) {
        p_53071_.add(FACING, IS_STRUCTURE_PLACED, RITUAL_CORE);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult blockHitResult) {
        if (blockState.is(ModBlocks.RUNE_REACTOR.get())) {
            if (level.isClientSide) {
                return InteractionResult.SUCCESS;
            } else {
                ItemStack handItem = player.getItemInHand(hand);
                if (level.getBlockEntity(blockPos) instanceof RuneReactorBlockEntity blockEntity) {
                    boolean isStructurePlaced = blockState.getValue(IS_STRUCTURE_PLACED);
                    if ((!isStructurePlaced || !EntityExpandedContext.NO_GODS.test(player)) && !player.isShiftKeyDown()) {
                        if (blockEntity.getInsertItem().isEmpty()) {
                            if (!handItem.isEmpty()) {
                                if (isRitualStructureCore(handItem)) {
                                    BlockState blockState1 = blockState.setValue(RITUAL_CORE, getRitualStructureCoreIndex(handItem));
                                    Block.pushEntitiesUp(blockState, blockState1, level, blockPos);
                                    level.setBlock(blockPos, blockState1, 2);
                                }
                                blockEntity.setOwner(player);
                                {
                                    if (!findFocus(handItem).isEmpty()) {
                                        FakeSpellerEntity spellerEntity = new FakeSpellerEntity(level, handItem, blockPos);
                                        spellerEntity.setWand(handItem.copy());
                                        spellerEntity.setTrueOwner(player);
                                        level.addFreshEntity(spellerEntity);
                                        blockEntity.setSpeller(spellerEntity);
                                    }
                                }
                                ItemStack stack = handItem.copy();
                                stack.setCount(1);
                                blockEntity.setInsertItem(stack);
                                handItem.shrink(1);
                                level.sendBlockUpdated(blockPos, level.getBlockState(blockPos), level.getBlockState(blockPos), 2);
                                level.levelEvent(1503, blockPos, 0);
                                return InteractionResult.SUCCESS;
                            }
                        } else {
                            if (isRitualStructureCore(blockEntity.getInsertItem())) {
                                BlockState blockState1 = blockState.setValue(RITUAL_CORE, 0);
                                level.setBlock(blockPos, blockState1, 2);
                            }
                            for (BlockShakingEntity blockShakingEntity : level.getEntitiesOfClass(BlockShakingEntity.class, new AABB(blockPos.above(2)))) {
                                if (blockShakingEntity.getBlockState().is(ModBlocks.RUNE_REACTOR.get())) {
                                    blockShakingEntity.discard();
                                }
                            }
                            if (blockEntity.getSpeller() != null)
                                blockEntity.getSpeller().discard();
                            blockEntity.setOwner((UUID) null);
                            blockEntity.setOwner((Player) null);
                            blockEntity.setSpeller((UUID) null);
                            blockEntity.setSpeller((FakeSpellerEntity) null);
                            player.addItem(blockEntity.getInsertItem());
                            blockEntity.setInsertItem(ItemStack.EMPTY);
                            level.levelEvent(232424314, blockPos, 0);
                            level.sendBlockUpdated(blockPos, level.getBlockState(blockPos), level.getBlockState(blockPos), 2);
                            return InteractionResult.SUCCESS;
                        }
                    }
                }
            }
        }
        return super.use(blockState, level, blockPos, player, hand, blockHitResult);
    }
    @Override
    public boolean canEntityDestroy(BlockState state, BlockGetter level, BlockPos pos, Entity entity) {
        if (!(entity instanceof Player player) || !player.isCreative()) {
            if (state.getValue(IS_STRUCTURE_PLACED))  {
                return false;
            }
        }
        return super.canEntityDestroy(state, level, pos, entity);
    }
    @Override
    public float getDestroyProgress(@NotNull BlockState state, Player player, @NotNull BlockGetter blockGetter, @NotNull BlockPos pos) {
        if (!player.isCreative()) {
            if (state.getValue(IS_STRUCTURE_PLACED))  {
                return 0F;
            }
        }
        return super.getDestroyProgress(state, player, blockGetter, pos);
    }

    @Override
    public void onRemove(BlockState blockState, @NotNull Level level, @NotNull BlockPos pos, BlockState blockState1, boolean p_60519_) {
        if (!blockState.is(blockState1.getBlock())) {
            BlockEntity blockentity = level.getBlockEntity(pos);
            if (blockentity instanceof RuneReactorBlockEntity blockEntity) {
                try {
                    ItemStack stack = blockEntity.getInsertItem();
                    if (!stack.isEmpty() && blockEntity.getLevel() != null) {
                        ItemEntity itementity = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), stack);
                        itementity.setDefaultPickUpDelay();
                        itementity.setGlowingTag(true);
                        level.addFreshEntity(itementity);
                    }
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                level.updateNeighbourForOutputSignal(pos, this);
                for (BlockShakingEntity blockShakingEntity : level.getEntitiesOfClass(BlockShakingEntity.class, new AABB(pos).inflate(4D))) {
                    if (blockShakingEntity.getBlockState().is(ModBlocks.RUNE_REACTOR.get())) {
                        blockShakingEntity.discard();
                    }
                }
                if (level instanceof ServerLevel serverLevel) {
                    blockEntity.getListenerEntities().forEach(entity->entity.blockOwnerRemoveEvent(level, pos, blockState, blockEntity, 0));
                    if (blockEntity.getSpeller() != null)
                        blockEntity.getSpeller().blockOwnerRemoveEvent(level, pos, blockState, blockEntity, 0);
                }
            }

        }
        super.onRemove(blockState, level, pos, blockState1, p_60519_);
    }

    @Override
    public void destroy(@NotNull LevelAccessor levelAccessor, @NotNull BlockPos pos, @NotNull BlockState blockState) {
        if (!levelAccessor.isClientSide()) {
            levelAccessor.levelEvent(232424314, pos, 1);
        } else {
            levelAccessor.addParticle(new CircleExplodeParticleOption(.70F, .45F, .95F, 3F, 1), pos.getX() + BASE_SHAPE.bounds().getXsize()/2F, pos.getY() + BASE_SHAPE.bounds().getYsize()/2F, pos.getZ() + BASE_SHAPE.bounds().getZsize()/2F, 0 ,0 ,0);
        }
        super.destroy(levelAccessor, pos, blockState);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos p_153215_, @NotNull BlockState p_153216_) {
        return new RuneReactorBlockEntity(p_153215_, p_153216_);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, @NotNull BlockState p_153213_, @NotNull BlockEntityType<T> p_153214_) {
        return p_153212_.isClientSide ? null : createTickerHelper(p_153214_, ModBlocks.RUNE_REACTOR_ENTITY.get(), RuneReactorBlockEntity::serverTick);
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState p_49232_) {
        return RenderShape.MODEL;
    }
    public static boolean canUseStructure(Level level, BlockPos pos, BlockState state) {
        checkCores();
        boolean canUse = false;
        if (level.getBlockEntity(pos) instanceof RuneReactorBlockEntity blockEntity) {
            if (state.getValue(IS_STRUCTURE_PLACED))
                return true;
            else if (level.getSignal(pos.above(-1), Direction.UP) > 0)
                return false;
            else if (level.getBlockEntity(pos.above(-1)) instanceof CursedCageBlockEntity cageBlockEntity && cageBlockEntity.getSouls() > 0) {
                return true;
            } else return level.getBlockEntity(pos.above(-2)) instanceof CursedCageBlockEntity cageBlockEntity && cageBlockEntity.getSouls() > 0;
        }
        return false;
    }
    public static int structuralIntegrity(Level level, BlockPos pos, BlockState state) {
        int integrity = 0;
        if (canUseStructure(level, pos, state)) {

            RuneReactorBlockEntity blockEntity = (RuneReactorBlockEntity) level.getBlockEntity(pos);
            assert blockEntity != null;
            for (BlockPos pos1 : blockEntity.getRunestonePoses()) {

                BlockState tempState = level.getBlockState(pos1);
                if (tempState.getBlock() instanceof RunestoneEngravedTableBlock engravedTableBlock) {
                    if (tempState.getValue(RunestoneEngravedTableBlock.CORE) > 0)
                        integrity++;
                }
            }
        }
        return integrity;
    }
    public static boolean isRitualStructureCore(ItemStack stack) {
        return stack.is(ModItems.TOTEM_OF_ROOTS.get()) || stack.is(ModItems.TOTEM_OF_SOULS.get()) || stack.is(ModItems.SOUL_TRANSFER.get());
    }
    public static int getRitualStructureCoreIndex(ItemStack stack) {
        if (stack.is(ModItems.TOTEM_OF_ROOTS.get()))
            return 1;
        else if (stack.is(ModItems.TOTEM_OF_SOULS.get()))
            return 2;
        else if (stack.is(ModItems.SOUL_TRANSFER.get()))
            return 3;
        else return 0;
    }
    public static boolean isKindOfCore(ItemStack stack) {
        checkCores();
        ISpell spell;
        return isRitualStructureCore(stack) || ((spell = getSpell(stack)) != null && !BlockConfig.blacklistSpellNames.contains(spell.getClass().getName()));
    }
    public static void checkCores() {
    }
    public static ItemStack findWand(ItemStack stack) {
        if (stack.getItem() instanceof IWand) {
            return stack;
        } else return ItemStack.EMPTY;
    }
    public static ItemStack findFocus(ItemStack stack) {
        ItemStack foundStack = ItemStack.EMPTY;
        ItemStack findWand = findWand(stack);
        if (!findWand.isEmpty() && !IWand.getFocus(findWand).isEmpty()) {
            foundStack = IWand.getFocus(findWand);
        }

        return foundStack;
    }
    public static ISpell getSpell(ItemStack stack) {
        Item var2 = findFocus(stack).getItem();
        if (var2 instanceof IFocus magicFocus) {
            if (magicFocus.getSpell() != null) {
                return magicFocus.getSpell();
            }
        }

        return null;
    }
}
