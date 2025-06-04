package com.mega.revelationfix.mixin.goety.ritual;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.blocks.entities.DarkAltarBlockEntity;
import com.Polarice3.Goety.common.blocks.entities.PedestalBlockEntity;
import com.Polarice3.Goety.common.crafting.RitualRecipe;
import com.Polarice3.Goety.common.ritual.Ritual;
import com.mega.revelationfix.common.entity.BlockShakingEntity;
import com.mega.revelationfix.common.network.PacketHandler;
import com.mega.revelationfix.common.network.s2c.TheEndRitualEventPacket;
import com.mega.revelationfix.common.ritual.ModRitualTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(DarkAltarBlockEntity.class)
public abstract class DarkAltarBlockMixin extends PedestalBlockEntity {
    @Shadow(remap = false)
    public Player castingPlayer;

    DarkAltarBlockMixin(BlockPos blockPos, BlockState blockState) {
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
     * @Unique
     *     private static int getI(BlockPos pPos, Level pLevel) {
     *         int firstCount = 0;
     *         int secondCount = 0;
     *         int thirdCount = 0;
     *         int fourthCount = 0;
     *         int fifthCount = 0;
     *
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
     *
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
     *
     *                 }
     *             }
     *         }
     *         int i = 0;
     *         if (firstCount >= totalFirst) i++;
     *         if (secondCount >= totalSecond) i++;
     *         if (thirdCount >= totalThird) i++;
     *         if (fourthCount >= totalFourth) i++;
     *         if (fifthCount >= totalFifth) i++;
     *
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
