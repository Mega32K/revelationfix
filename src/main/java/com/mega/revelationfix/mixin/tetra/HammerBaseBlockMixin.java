package com.mega.revelationfix.mixin.tetra;

import com.mega.revelationfix.common.advancement.ModCriteriaTriggers;
import com.mega.revelationfix.common.compat.tetra.hammer.DarkIngotHammerContext;
import com.mega.revelationfix.safe.mixinpart.ModDependsMixin;
import com.mega.revelationfix.safe.mixinpart.tetra.HammerBaseEntityEC;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ToolAction;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import se.mickelus.mutil.util.TileEntityOptional;
import se.mickelus.tetra.TetraToolActions;
import se.mickelus.tetra.blocks.TetraBlock;
import se.mickelus.tetra.blocks.forged.hammer.HammerBaseBlock;
import se.mickelus.tetra.blocks.forged.hammer.HammerBaseBlockEntity;
import se.mickelus.tetra.blocks.forged.hammer.HammerEffect;
import se.mickelus.tetra.blocks.salvage.InteractiveBlockOverlay;
import se.mickelus.tetra.items.modular.impl.ModularDoubleHeadedItem;
import se.mickelus.tetra.module.ItemModuleMajor;
import se.mickelus.tetra.properties.PropertyHelper;

@Mixin(HammerBaseBlock.class)
@ModDependsMixin("tetra")
public abstract class HammerBaseBlockMixin extends TetraBlock {
    @Shadow(remap = false)
    public static HammerBaseBlock instance;

    public HammerBaseBlockMixin(Properties properties) {
        super(properties);
    }

    @Shadow(remap = false)
    public abstract void consumeFuel(Level world, BlockPos pos);

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void tryImprove(BlockState blockState, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult, CallbackInfoReturnable<InteractionResult> cir) {
        HammerBaseBlockEntity te = TileEntityOptional.from(world, pos, HammerBaseBlockEntity.class).orElse(null);

        if (te != null) {

            HammerBaseEntityEC ec = (HammerBaseEntityEC) te;
            if (!ec.revelationfix$isDarkIngot()) {

                ItemStack heldStack = player.getItemInHand(InteractionHand.MAIN_HAND);
                if (heldStack.getItem() instanceof ModularDoubleHeadedItem) {

                    ItemStack otherStack = player.getItemInHand(InteractionHand.OFF_HAND);

                    if (PropertyHelper.getItemTools(heldStack).contains(TetraToolActions.hammer) && DarkIngotHammerContext.improveNeeds.contains(otherStack.getItem()) && DarkIngotHammerContext.improveNeeds.indexOf(otherStack.getItem()) == ec.revelationfix$getImproved()) {

                        ec.revelationfix$setImproved(ec.revelationfix$getImproved() + 1);
                        world.playSound(player, pos, SoundEvents.ZOMBIE_ATTACK_IRON_DOOR, SoundSource.PLAYERS, 1.0F, 0.5F);
                        if (world.isClientSide) {
                            player.swing(InteractionHand.MAIN_HAND);
                            InteractiveBlockOverlay.markDirty();
                        }
                        if (!world.isClientSide) {
                            if (heldStack.isDamageableItem())
                                heldStack.setDamageValue(heldStack.getDamageValue() + 1);
                            otherStack.shrink(1);
                            world.sendBlockUpdated(pos, te.getBlockState(), te.getBlockState(), 3);
                            te.setChanged();
                            if (ec.revelationfix$isDarkIngot() && player instanceof ServerPlayer serverPlayer) {
                                ModCriteriaTriggers.IMPROVE_HAMMER_TRIGGER.trigger(serverPlayer);
                            }
                        }
                        cir.setReturnValue(InteractionResult.sidedSuccess(player.level().isClientSide));
                    }
                }
            }
        }
    }

    public void animateTick(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, @NotNull RandomSource rand) {
        HammerBaseBlockEntity te = TileEntityOptional.from(world, pos, HammerBaseBlockEntity.class).orElse(null);
        if (te != null && ((HammerBaseEntityEC) te).revelationfix$isDarkIngot()) {
            if (rand.nextBoolean()) {
                boolean flipped = rand.nextBoolean();
                float x = (float) pos.getX() + (flipped ? (rand.nextBoolean() ? -0.05F : 1.05F) : rand.nextFloat());
                float z = (float) pos.getZ() + (!flipped ? (rand.nextBoolean() ? -0.05F : 1.05F) : rand.nextFloat());
                world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, x, pos.getY() + 0.02, z, 0.0, 0.02, 0.0);
            }
            if (rand.nextFloat() > 0.66F) {
                boolean flipped = rand.nextBoolean();
                float x = (float) pos.getX() + (flipped ? (rand.nextBoolean() ? -0.05F : 1.05F) : rand.nextFloat());
                float z = (float) pos.getZ() + (!flipped ? (rand.nextBoolean() ? -0.05F : 1.05F) : rand.nextFloat());
                world.addParticle(ParticleTypes.SOUL, x, pos.getY() + 0.02, z, 0.0, 0.02, 0.0);
            }
        }
    }

    @Inject(method = "applyCraftEffects", remap = false, at = @At("HEAD"), cancellable = true)
    private void applyCraftEffects(Level world, BlockPos pos, BlockState blockState, ItemStack targetStack, String slot, boolean isReplacing, Player player, ToolAction requiredTool, int requiredLevel, boolean consumeResources, CallbackInfoReturnable<ItemStack> cir) {
        boolean isDarkIngotImproved = false;
        HammerBaseBlockEntity te0 = TileEntityOptional.from(world, pos, HammerBaseBlockEntity.class).orElse(null);
        if (te0 != null) {
            HammerBaseEntityEC ec = (HammerBaseEntityEC) te0;
            if (ec.revelationfix$isDarkIngot()) {
                isDarkIngotImproved = true;
            }
        }
        if (consumeResources && !isDarkIngotImproved) {
            this.consumeFuel(world, pos);
        }
        if (isReplacing) {
            if (isDarkIngotImproved) {
                ItemStack upgradedStack = targetStack.copy();
                ItemModuleMajor.addImprovement(upgradedStack, slot, "quality", 2);
                ItemModuleMajor.addImprovement(upgradedStack, slot, "settled", 2);
                cir.setReturnValue(upgradedStack);
                return;
            }
        }
        if (isReplacing) {
            int preciseLevel = TileEntityOptional.from(world, pos, HammerBaseBlockEntity.class).map((te) -> te.getEffectLevel(HammerEffect.precise)).orElse(0);
            if (preciseLevel > 0) {
                ItemStack upgradedStack = targetStack.copy();
                ItemModuleMajor.addImprovement(upgradedStack, slot, "quality", preciseLevel);
                cir.setReturnValue(upgradedStack);
                return;
            }
        }
        cir.setReturnValue(targetStack);
    }

    @Inject(method = "getHammerLevel", remap = false, at = @At("RETURN"), cancellable = true)
    private void getHammerLevel(Level world, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        HammerBaseBlockEntity te = TileEntityOptional.from(world, pos, HammerBaseBlockEntity.class).orElse(null);
        if (te != null) {
            HammerBaseEntityEC ec = (HammerBaseEntityEC) te;
            if (ec.revelationfix$isDarkIngot()) {
                cir.setReturnValue(Math.max(9, cir.getReturnValue()));
            }
        }
    }
}
