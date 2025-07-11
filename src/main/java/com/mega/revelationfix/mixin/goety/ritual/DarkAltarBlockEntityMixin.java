package com.mega.revelationfix.mixin.goety.ritual;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.blocks.entities.DarkAltarBlockEntity;
import com.Polarice3.Goety.common.blocks.entities.PedestalBlockEntity;
import com.Polarice3.Goety.common.crafting.RitualRecipe;
import com.Polarice3.Goety.common.ritual.Ritual;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.mega.revelationfix.api.event.block.DarkAltarEvent;
import com.mega.revelationfix.common.entity.binding.BlockShakingEntity;
import com.mega.revelationfix.common.event.GoetyEventHooks;
import com.mega.revelationfix.common.network.PacketHandler;
import com.mega.revelationfix.common.network.s2c.TheEndRitualEventPacket;
import com.mega.revelationfix.common.ritual.ModRitualTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * tick stopRitual Index :<br>
 * 0: INVALID_RITUAL_RECIPE<br>
 * 1: NOT_ENOUGH_SOULS_WITH_MSG<br>
 * 2: ENCHANTMENT_RITUAL_NOT_ENOUGH_EXP<br>
 * 3: CANNOT_CONSUME_INGREDIENTS<br>
 * 4: FINISHED_ENCHANTMENT<br>
 * 5: FINISHED_NORMAL<br>
 * 6: FINISHED_CONVERSION<br>
 * 7: CONVERSION_NOT_IN_RANGE_WHEN_WILL_FINISHED<br>
 * 8: INVALID_STRUCTURE<br>
 * 9: CONVERSION_NOT_IN_RANGE<br>
 * 10+1: NOT_ENOUGH_SOULS_WITHOUT_MSG<br>
 */
@Mixin(DarkAltarBlockEntity.class)
public abstract class DarkAltarBlockEntityMixin extends PedestalBlockEntity {
    @Shadow(remap = false)
    public Player castingPlayer;

    DarkAltarBlockEntityMixin(BlockPos blockPos, BlockState blockState) {
        super(blockPos, blockState);
    }

    @Shadow(remap = false)
    public abstract RitualRecipe getCurrentRitualRecipe();

    @Inject(remap = false, method = "startRitual", at = @At(remap = false, value = "INVOKE", target = "Lcom/Polarice3/Goety/common/blocks/entities/DarkAltarBlockEntity;markNetworkDirty()V", shift = At.Shift.AFTER))
    private void startRitual(Player player, ItemStack activationItem, RitualRecipe ritualRecipe, CallbackInfo ci) {
        if (this.level != null && !this.level.isClientSide) {
            if (ritualRecipe.getCraftType().equals(ModRitualTypes.THE_END) && ritualRecipe.getRitualType().equals(Goety.location("craft"))) {
                for (PedestalBlockEntity pbe : ritualRecipe.getRitual().getPedestals(player.level, this.getBlockPos())) {
                    BlockShakingEntity blockShakingEntity = new BlockShakingEntity(player.level, pbe.getBlockPos().getX(), pbe.getBlockPos().getY() + 2, pbe.getBlockPos().getZ(), pbe.getBlockState(), 100 * 20);
                    player.level.addFreshEntity(blockShakingEntity);
                }
                BlockShakingEntity blockShakingEntity = new BlockShakingEntity(player.level, this.getBlockPos().getX(), this.getBlockPos().getY() + 2, this.getBlockPos().getZ(), this.getBlockState(), 100 * 20);
                player.level.addFreshEntity(blockShakingEntity);
                player.addTag("odamaneFinalDeath");
                if (player instanceof ServerPlayer sp)
                    sp.setRespawnPosition(sp.level.dimension(), this.getBlockPos().above(1), 0F, true, false);
                PacketHandler.sendToAll(new TheEndRitualEventPacket(this.getBlockPos(), true));
            }
        }

    }

    @Inject(remap = false, method = "stopRitual", at = @At(remap = false, value = "INVOKE", target = "Lcom/Polarice3/Goety/common/blocks/entities/DarkAltarBlockEntity;markNetworkDirty()V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void stopRitual(boolean finished, CallbackInfo ci) {
        if (this.level != null && !this.level.isClientSide) {
            for (BlockShakingEntity ter : this.level.getEntitiesOfClass(BlockShakingEntity.class, new AABB(this.getBlockPos()).inflate(Ritual.RANGE + 1))) {
                ter.setDuration(30);
                PacketHandler.sendToAll(new TheEndRitualEventPacket(this.getBlockPos(), false));
            }
            RitualRecipe ritualRecipe = this.getCurrentRitualRecipe();
        }
    }

    @Inject(remap = false, method = "removeItem", at = @At(remap = false, value = "INVOKE", target = "Lcom/Polarice3/Goety/common/blocks/entities/DarkAltarBlockEntity;markNetworkDirty()V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void removeItem(CallbackInfo ci, IItemHandler handler, ItemStack itemStack) {
        if (this.level != null && !this.level.isClientSide) {
            for (BlockShakingEntity ter : this.level.getEntitiesOfClass(BlockShakingEntity.class, new AABB(this.getBlockPos()).inflate(Ritual.RANGE + 1))) {
                ter.setDuration(30);
                PacketHandler.sendToAll(new TheEndRitualEventPacket(this.getBlockPos(), false));
            }
        }
    }

    /**
     * 修正原能量不足结束仪式方法
     */
    @Redirect(remap = false, method = "tick", at = @At(remap = false, value = "INVOKE", target = "Lcom/Polarice3/Goety/common/blocks/entities/DarkAltarBlockEntity;stopRitual(Z)V", ordinal = 11))
    private void stopRitual_NOT_ENOUGH_SOULS_WITHOUT_MSG(DarkAltarBlockEntity instance, boolean z) {
        if (instance.currentTime > 0) {
            if (!GoetyEventHooks.postStopRitualEvent(this.level, this.getBlockPos(), this.getBlockState(), instance, true, z, DarkAltarEvent.StopRitualEvent.Reason.NOT_ENOUGH_SOULS_WITHOUT_MSG)) {
                instance.stopRitual(z);
            }
            GoetyEventHooks.postStopRitualEvent(this.level, this.getBlockPos(), this.getBlockState(), instance, false, z, DarkAltarEvent.StopRitualEvent.Reason.NOT_ENOUGH_SOULS_WITHOUT_MSG);
        }
    }
    @Redirect(remap = false, method = "tick", at = @At(remap = false, value = "INVOKE", target = "Lcom/Polarice3/Goety/common/blocks/entities/DarkAltarBlockEntity;stopRitual(Z)V", ordinal = 0))
    private void stopRitual_INVALID_RITUAL_RECIPE(DarkAltarBlockEntity instance, boolean z) {
        if (!GoetyEventHooks.postStopRitualEvent(this.level, this.getBlockPos(), this.getBlockState(), instance, true, z, DarkAltarEvent.StopRitualEvent.Reason.INVALID_RITUAL_RECIPE)) {
            instance.stopRitual(z);
        }
        GoetyEventHooks.postStopRitualEvent(this.level, this.getBlockPos(), this.getBlockState(), instance, false, z, DarkAltarEvent.StopRitualEvent.Reason.INVALID_RITUAL_RECIPE);
    }
    @Redirect(remap = false, method = "tick", at = @At(remap = false, value = "INVOKE", target = "Lcom/Polarice3/Goety/common/blocks/entities/DarkAltarBlockEntity;stopRitual(Z)V", ordinal = 1))
    private void stopRitual_NOT_ENOUGH_SOULS_WITH_MSG(DarkAltarBlockEntity instance, boolean z) {
        if (!GoetyEventHooks.postStopRitualEvent(this.level, this.getBlockPos(), this.getBlockState(), instance, true, z, DarkAltarEvent.StopRitualEvent.Reason.NOT_ENOUGH_SOULS_WITH_MSG)) {
            instance.stopRitual(z);
        }
        GoetyEventHooks.postStopRitualEvent(this.level, this.getBlockPos(), this.getBlockState(), instance, false, z, DarkAltarEvent.StopRitualEvent.Reason.NOT_ENOUGH_SOULS_WITH_MSG);
    }
    @Redirect(remap = false, method = "tick", at = @At(remap = false, value = "INVOKE", target = "Lcom/Polarice3/Goety/common/blocks/entities/DarkAltarBlockEntity;stopRitual(Z)V", ordinal = 2))
    private void stopRitual_ENCHANTMENT_RITUAL_NOT_ENOUGH_EXP(DarkAltarBlockEntity instance, boolean z) {
        if (!GoetyEventHooks.postStopRitualEvent(this.level, this.getBlockPos(), this.getBlockState(), instance, true, z, DarkAltarEvent.StopRitualEvent.Reason.ENCHANTMENT_RITUAL_NOT_ENOUGH_EXP)) {
            instance.stopRitual(z);
        }
        GoetyEventHooks.postStopRitualEvent(this.level, this.getBlockPos(), this.getBlockState(), instance, false, z, DarkAltarEvent.StopRitualEvent.Reason.ENCHANTMENT_RITUAL_NOT_ENOUGH_EXP);
    }
    @Redirect(remap = false, method = "tick", at = @At(remap = false, value = "INVOKE", target = "Lcom/Polarice3/Goety/common/blocks/entities/DarkAltarBlockEntity;stopRitual(Z)V", ordinal = 3))
    private void stopRitual_CANNOT_CONSUME_INGREDIENTS(DarkAltarBlockEntity instance, boolean z) {
        if (!GoetyEventHooks.postStopRitualEvent(this.level, this.getBlockPos(), this.getBlockState(), instance, true, z, DarkAltarEvent.StopRitualEvent.Reason.CANNOT_CONSUME_INGREDIENTS)) {
            instance.stopRitual(z);
        }
        GoetyEventHooks.postStopRitualEvent(this.level, this.getBlockPos(), this.getBlockState(), instance, false, z, DarkAltarEvent.StopRitualEvent.Reason.CANNOT_CONSUME_INGREDIENTS);
    }
    @Redirect(remap = false, method = "tick", at = @At(remap = false, value = "INVOKE", target = "Lcom/Polarice3/Goety/common/blocks/entities/DarkAltarBlockEntity;stopRitual(Z)V", ordinal = 4))
    private void stopRitual_FINISHED_ENCHANTMENT(DarkAltarBlockEntity instance, boolean z) {
        if (!GoetyEventHooks.postStopRitualEvent(this.level, this.getBlockPos(), this.getBlockState(), instance, true, z, DarkAltarEvent.StopRitualEvent.Reason.FINISHED_ENCHANTMENT)) {
            instance.stopRitual(z);
        }
        GoetyEventHooks.postStopRitualEvent(this.level, this.getBlockPos(), this.getBlockState(), instance, false, z, DarkAltarEvent.StopRitualEvent.Reason.FINISHED_ENCHANTMENT);
    }
    @Redirect(remap = false, method = "tick", at = @At(remap = false, value = "INVOKE", target = "Lcom/Polarice3/Goety/common/blocks/entities/DarkAltarBlockEntity;stopRitual(Z)V", ordinal = 5))
    private void stopRitual_FINISHED_NORMAL(DarkAltarBlockEntity instance, boolean z) {
        if (!GoetyEventHooks.postStopRitualEvent(this.level, this.getBlockPos(), this.getBlockState(), instance, true, z, DarkAltarEvent.StopRitualEvent.Reason.FINISHED_NORMAL)) {
            instance.stopRitual(z);
        }
        GoetyEventHooks.postStopRitualEvent(this.level, this.getBlockPos(), this.getBlockState(), instance, false, z, DarkAltarEvent.StopRitualEvent.Reason.FINISHED_NORMAL);
    }
    @Redirect(remap = false, method = "tick", at = @At(remap = false, value = "INVOKE", target = "Lcom/Polarice3/Goety/common/blocks/entities/DarkAltarBlockEntity;stopRitual(Z)V", ordinal = 6))
    private void stopRitual_FINISHED_CONVERSION(DarkAltarBlockEntity instance, boolean z) {
        if (!GoetyEventHooks.postStopRitualEvent(this.level, this.getBlockPos(), this.getBlockState(), instance, true, z, DarkAltarEvent.StopRitualEvent.Reason.FINISHED_CONVERSION)) {
            instance.stopRitual(z);
        }
        GoetyEventHooks.postStopRitualEvent(this.level, this.getBlockPos(), this.getBlockState(), instance, false, z, DarkAltarEvent.StopRitualEvent.Reason.FINISHED_CONVERSION);
    }
    @Redirect(remap = false, method = "tick", at = @At(remap = false, value = "INVOKE", target = "Lcom/Polarice3/Goety/common/blocks/entities/DarkAltarBlockEntity;stopRitual(Z)V", ordinal = 7))
    private void stopRitual_CONVERSION_NOT_IN_RANGE_WHEN_WILL_FINISHED(DarkAltarBlockEntity instance, boolean z) {
        if (!GoetyEventHooks.postStopRitualEvent(this.level, this.getBlockPos(), this.getBlockState(), instance, true, z, DarkAltarEvent.StopRitualEvent.Reason.CONVERSION_NOT_IN_RANGE_WHEN_WILL_FINISHED)) {
            instance.stopRitual(z);
        }
        GoetyEventHooks.postStopRitualEvent(this.level, this.getBlockPos(), this.getBlockState(), instance, false, z, DarkAltarEvent.StopRitualEvent.Reason.CONVERSION_NOT_IN_RANGE_WHEN_WILL_FINISHED);
    }
    @Redirect(remap = false, method = "tick", at = @At(remap = false, value = "INVOKE", target = "Lcom/Polarice3/Goety/common/blocks/entities/DarkAltarBlockEntity;stopRitual(Z)V", ordinal = 8))
    private void stopRitual_INVALID_STRUCTURE(DarkAltarBlockEntity instance, boolean z) {
        if (!GoetyEventHooks.postStopRitualEvent(this.level, this.getBlockPos(), this.getBlockState(), instance, true, z, DarkAltarEvent.StopRitualEvent.Reason.INVALID_STRUCTURE)) {
            instance.stopRitual(z);
        }
        GoetyEventHooks.postStopRitualEvent(this.level, this.getBlockPos(), this.getBlockState(), instance, false, z, DarkAltarEvent.StopRitualEvent.Reason.INVALID_STRUCTURE);
    }
    @Redirect(remap = false, method = "tick", at = @At(remap = false, value = "INVOKE", target = "Lcom/Polarice3/Goety/common/blocks/entities/DarkAltarBlockEntity;stopRitual(Z)V", ordinal = 9))
    private void stopRitual_CONVERSION_NOT_IN_RANGE(DarkAltarBlockEntity instance, boolean z) {
        if (GoetyEventHooks.postStopRitualEvent(this.level, this.getBlockPos(), this.getBlockState(), instance, true, z, DarkAltarEvent.StopRitualEvent.Reason.CONVERSION_NOT_IN_RANGE)) {
            instance.stopRitual(z);
        }
        GoetyEventHooks.postStopRitualEvent(this.level, this.getBlockPos(), this.getBlockState(), instance, false, z, DarkAltarEvent.StopRitualEvent.Reason.CONVERSION_NOT_IN_RANGE);
    }

    @Inject(remap = false, method = "tick", at = @At("HEAD"), cancellable = true)
    private void tickHead(CallbackInfo ci) {
        if (GoetyEventHooks.postDarkAltarBETickEvent(this.level, this.getBlockPos(), this.getBlockState(), ((DarkAltarBlockEntity) (Object)this), DarkAltarEvent.TickEvent.TickPhase.HEAD))
            ci.cancel();
    }
    @Inject(remap = false, method = "tick", at = @At(remap = false, value = "INVOKE", target = "Lcom/Polarice3/Goety/common/blocks/entities/DarkAltarBlockEntity;getCurrentRitualRecipe()Lcom/Polarice3/Goety/common/crafting/RitualRecipe;", ordinal = 0), cancellable = true)
    private void tickAfterHasSouls(CallbackInfo ci) {
        if (GoetyEventHooks.postDarkAltarBETickEvent(this.level, this.getBlockPos(), this.getBlockState(), ((DarkAltarBlockEntity) (Object)this), DarkAltarEvent.TickEvent.TickPhase.AFTER_HAS_SOULS))
            ci.cancel();
    }
    @Inject(remap = false, method = "tick", at = @At(value = "TAIL"))
    private void tickTail(CallbackInfo ci) {
        GoetyEventHooks.postDarkAltarBETickEvent(this.level, this.getBlockPos(), this.getBlockState(), ((DarkAltarBlockEntity) (Object)this), DarkAltarEvent.TickEvent.TickPhase.TAIL);
    }

    @Redirect(remap = false, method = "activate", at = @At(remap = false, value = "INVOKE", target = "Lcom/Polarice3/Goety/common/blocks/entities/DarkAltarBlockEntity;startRitual(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;Lcom/Polarice3/Goety/common/crafting/RitualRecipe;)V"))
    private void startRitual(DarkAltarBlockEntity instance, Player player, ItemStack activationItem, RitualRecipe ritualRecipe, @Share("isStartRitualCanceled") LocalBooleanRef isStartRitualCanceled) {
        if (!GoetyEventHooks.postStartRitualEvent(this.level, this.getBlockPos(), this.getBlockState(), instance, player, activationItem, ritualRecipe)) {
            isStartRitualCanceled.set(false);
            instance.startRitual(player, activationItem, ritualRecipe);
        } else isStartRitualCanceled.set(true);
    }
    @Inject(remap = false, method = "activate", at = @At("RETURN"), cancellable = true)
    private void active(Level world, BlockPos pos, Player player, InteractionHand hand, Direction face, CallbackInfoReturnable<Boolean> cir, @Share("isStartRitualCanceled") LocalBooleanRef isStartRitualCanceled) {
        if (isStartRitualCanceled.get())
            cir.setReturnValue(false);
    }
    /**
     * @Unique
     *     private static int getI(BlockPos pPos, Level pLevel) {
     *         int firstCount = 0;
     *         int secondCount = 0;
     *         int thirdCount = 0;
     *         int fourthCount = 0;
     *         int fifthCount = 0;
     * <p>
     *         int totalFirst = 0;
     *         int totalSecond = 0;
     *         int totalThird = 0;
     *         int totalFourth = 0;
     *         int totalFifth = 0;
     *         for (int i = -8; i <= 8; ++i) {
     *             for (int j = -8; j <= 8; ++j) {
     *                 for (int k = -8; k <= 8; ++k) {
     *                     BlockPos blockpos1 = pPos.offset(i, j, k);
     *                     BlockState blockstate = pLevel.getBlockState(blockpos1);
     * <p>
     *                     totalFirst = 4;
     *                     totalSecond = 8;
     *                     totalThird = 16;
     *                     totalFourth = 4800;
     *                     totalFifth = 1;
     *                     if (blockstate.is(Blocks.END_ROD)) {
     *                         ++firstCount;
     *                     }
     *                     if (blockstate.is(Blocks.PURPUR_BLOCK)) {
     *                         ++secondCount;
     *                     }
     *                     if (blockstate.is(Blocks.END_STONE_BRICKS)) {
     *                         ++thirdCount;
     *                     }
     *                     if (blockstate.is(Blocks.END_STONE)) {
     *                         ++fourthCount;
     *                     }
     *                     if (blockstate.is(Blocks.CHEST) && blockstate.getBlock() instanceof ChestBlock chestBlock) {
     *                         if (checkChest(ChestBlock.getContainer(chestBlock, blockstate, pLevel, blockpos1, true)))
     *                             ++fifthCount;
     *                     }
     * <p>
     *                 }
     *             }
     *         }
     *         int i = 0;
     *         if (firstCount >= totalFirst) i++;
     *         if (secondCount >= totalSecond) i++;
     *         if (thirdCount >= totalThird) i++;
     *         if (fourthCount >= totalFourth) i++;
     *         if (fifthCount >= totalFifth) i++;
     * <p>
     *         return i;
     *     }
     *     @Unique
     *     private static boolean checkChest(Container container) {
     *         List<Predicate<ItemStack>> shouldMatched = new ArrayList<>();
     *         shouldMatched.add(stack -> stack.is(Items.GOLD_INGOT));
     *         shouldMatched.add(stack -> stack.is(Items.IRON_INGOT));
     *         shouldMatched.add(stack -> stack.is(Items.DIAMOND));
     *         shouldMatched.add(stack -> stack.is(Items.BEETROOT_SEEDS));
     *         shouldMatched.add(stack -> stack.is(Items.SADDLE));
     *         shouldMatched.add(stack -> stack.is(Items.EMERALD));
     *         shouldMatched.add(stack -> stack.is(Items.IRON_HORSE_ARMOR) || stack.is(Items.GOLDEN_HORSE_ARMOR) || stack.is(Items.DIAMOND_HORSE_ARMOR));
     *         for (int i=shouldMatched.size()-1;i>=0;i--) {
     *             Predicate<ItemStack> predicate = shouldMatched.get(i);
     *             for(int j = 0; j < container.getContainerSize(); ++j) {
     *                 ItemStack itemstack = container.getItem(j);
     *                 if ( predicate.test(itemstack)) {
     *                     shouldMatched.remove(predicate);
     *                 }
     *             }
     *         }
     *         return shouldMatched.isEmpty();
     *     }
     */
}
