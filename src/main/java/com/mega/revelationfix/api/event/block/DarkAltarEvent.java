package com.mega.revelationfix.api.event.block;

import com.Polarice3.Goety.common.blocks.entities.DarkAltarBlockEntity;
import com.Polarice3.Goety.common.crafting.RitualRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Cancelable;

import java.lang.invoke.MethodHandle;

public abstract class DarkAltarEvent extends BlockEvent {
    private final DarkAltarBlockEntity altarBlockEntity;

    public DarkAltarEvent(LevelAccessor level, BlockPos pos, BlockState state, DarkAltarBlockEntity altarBlockEntity) {
        super(level, pos, state);
        this.altarBlockEntity = altarBlockEntity;
    }

    public DarkAltarBlockEntity getAltarBlockEntity() {
        return altarBlockEntity;
    }
    public static class TickEvent extends DarkAltarEvent {
        public enum TickPhase {
            HEAD, TAIL, AFTER_HAS_SOULS
        }
        private final TickPhase tickPhase;

        public TickEvent(LevelAccessor level, BlockPos pos, BlockState state, DarkAltarBlockEntity altarBlockEntity, TickPhase tickPhase) {
            super(level, pos, state, altarBlockEntity);
            this.tickPhase = tickPhase;
        }
        public TickPhase getTickPhase() {
            return tickPhase;
        }

        @Override
        public boolean isCancelable() {
            return this.getTickPhase() != TickPhase.TAIL;
        }
    }
    public static class StopRitualEvent extends DarkAltarEvent {
        /**
         * 代码分布<br>
         * <blockquote><pre>{@code public void tick() {
         *         boolean flag = this.checkCage();
         *         if (this.level == null){
         *             return;
         *         }
         *         if (flag) {
         *             if (this.cursedCageTile.getSouls() > 0) {
         *                 RitualRecipe recipe = this.getCurrentRitualRecipe();
         *                 double d0 = (double)this.worldPosition.getX() + this.level.random.nextDouble();
         *                 double d1 = (double)this.worldPosition.getY() + this.level.random.nextDouble();
         *                 double d2 = (double)this.worldPosition.getZ() + this.level.random.nextDouble();
         *                 if (!this.level.isClientSide) {
         *                     ServerLevel serverWorld = (ServerLevel) this.level;
         *                     if (recipe != null) {
         *
         *                         this.restoreCastingPlayer();
         *
         *                         if (this.castingPlayer == null || !this.sacrificeFulfilled()) {
         *                             for (int p = 0; p < 4; ++p) {
         *                                 serverWorld.sendParticles(ParticleTypes.FLAME, d0, d1, d2, 1, 0.0F, 0.0F, 0.0F, 0.0F);
         *                             }
         *                         }
         *                         for (int p = 0; p < 4; ++p) {
         *                             serverWorld.sendParticles(ModParticleTypes.TOTEM_EFFECT.get(), d0, d1, d2, 0, 0.45, 0.45, 0.45, 1);
         *                         }
         *
         *                         if (this.remainingAdditionalIngredients == null) {
         *                             this.restoreRemainingAdditionalIngredients();
         *                             if (this.remainingAdditionalIngredients == null) {
         *                                 Goety.LOGGER
         *                                         .warn("Could not restore remainingAdditionalIngredients during tick - world seems to be null. Will attempt again next tick.");
         *                                 return;
         *                             }
         *                         }
         *
         *                         IItemHandler handler = this.itemStackHandler.orElseThrow(RuntimeException::new);
         *                         if (!recipe.getRitual().isValid(this.level, this.worldPosition, this, this.castingPlayer,
         *                                 handler.getStackInSlot(0), this.remainingAdditionalIngredients)) {
         *
         *                             thisIsWhereReasonIsUsed(()-> Reason.INVALID_RITUAL_RECIPE);
         *
         *                             this.stopRitual(false);
         *                             return;
         *                         }
         *
         *                         if (this.castingPlayer == null || !this.sacrificeFulfilled()) {
         *                             return;
         *                         }
         *
         *                         if (this.level.getGameTime() % 20 == 0) {
         *                             if (this.cursedCageTile.getSouls() >= recipe.getSoulCost()){
         *                                 this.cursedCageTile.decreaseSouls(recipe.getSoulCost());
         *                                 serverWorld.playSound(null, this.worldPosition, SoundEvents.SCULK_CATALYST_BLOOM, SoundSource.BLOCKS, 1.0F, this.level.random.nextFloat() * 0.1F + 0.9F);
         *                                 serverWorld.sendParticles(ParticleTypes.SCULK_SOUL, (double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 1.15D, (double)this.worldPosition.getZ() + 0.5D, 2, 0.2D, 0.0D, 0.2D, 0.0D);
         *                                 this.currentTime++;
         *                             } else {
         *                                 this.castingPlayer.displayClientMessage(Component.translatable("info.goety.ritual.noSouls.fail"), true);
         *
         *                                 thisIsWhereReasonIsUsed(()-> Reason.NOT_ENOUGH_SOULS_WITH_MSG);
         *
         *                                 this.stopRitual(false);
         *                                 return;
         *                             }
         *                         }
         *
         *                         if (recipe.getRitual() instanceof EnchantItemRitual enchantItemRitual) {
         *                             if (this.experienceTaken < enchantItemRitual.getLevelCost(handler.getStackInSlot(0))) {
         *                                 if (this.castingPlayer.experienceLevel > 1) {
         *                                     if (this.level.getGameTime() % 10 == 0) {
         *                                         serverWorld.playSound(null, this.worldPosition, SoundEvents.SCULK_CATALYST_BLOOM, SoundSource.BLOCKS, 0.5F, 0.5F);
         *                                         this.castingPlayer.giveExperienceLevels(-1);
         *                                         this.experienceTaken += 1;
         *                                     }
         *                                     this.addXPParticles(serverWorld);
         *                                 } else {
         *                                     this.castingPlayer.displayClientMessage(Component.translatable("info.goety.ritual.noXP.fail"), true);
         *
         *                                     thisIsWhereReasonIsUsed(()-> Reason.ENCHANTMENT_RITUAL_NOT_ENOUGH_EXP);
         *
         *                                     this.stopRitual(false);
         *                                     return;
         *                                 }
         *                             }
         *                         }
         *
         *                         recipe.getRitual().update(this.level, this.worldPosition, this, this.castingPlayer, handler.getStackInSlot(0),
         *                                 this.currentTime, recipe.getDuration());
         *
         *                         if (!recipe.getRitual()
         *                                 .consumeAdditionalIngredients(this.level, this.worldPosition, this.castingPlayer, this.remainingAdditionalIngredients,
         *                                         this.currentTime, this.consumedIngredients)) {
         *
         *                             thisIsWhereReasonIsUsed(()-> Reason.CANNOT_CONSUME_INGREDIENTS);
         *
         *                             this.stopRitual(false);
         *                             return;
         *                         }
         *
         *                         if (recipe.getDuration() >= 0 && this.currentTime >= recipe.getDuration()) {
         *                             if (recipe.getRitual() instanceof EnchantItemRitual enchantItemRitual){
         *                                 if (this.experienceTaken >= enchantItemRitual.getLevelCost(handler.getStackInSlot(0))){
         *
         *                                     thisIsWhereReasonIsUsed(()-> Reason.FINISHED_ENCHANTMENT);
         *
         *                                     this.stopRitual(true);
         *                                 }
         *                             } else if (!recipe.isConversion()) {
         *
         *                                 thisIsWhereReasonIsUsed(()-> Reason.FINISHED_NORMAL);
         *
         *                                 this.stopRitual(true);
         *                             } else {
         *                                 if (this.getConvertEntity != null){
         *
         *                                     thisIsWhereReasonIsUsed(()-> Reason.FINISHED_CONVERSION);
         *
         *                                     this.stopRitual(true);
         *                                 } else {
         *                                     this.castingPlayer.displayClientMessage(Component.translatable("info.goety.ritual.convert.fail"), true);
         *
         *                                     thisIsWhereReasonIsUsed(()-> Reason.CONVERSION_NOT_IN_RANGE_WHEN_WILL_FINISHED);
         *
         *                                     this.stopRitual(false);
         *                                 }
         *                             }
         *                         }
         *
         *                         int totalTime = 60;
         *
         *                         if (!RitualRequirements.getProperStructure(recipe.getCraftType(), this, this.worldPosition, this.level)){
         *                             ++this.structureTime;
         *                             if (this.structureTime >= totalTime) {
         *                                 this.castingPlayer.displayClientMessage(Component.translatable("info.goety.ritual.structure.fail"), true);
         *
         *                                 thisIsWhereReasonIsUsed(()-> Reason.INVALID_STRUCTURE);
         *
         *                                 this.stopRitual(false);
         *                             }
         *                         } else {
         *                             this.structureTime = 0;
         *                         }
         *                         if (recipe.isConversion()){
         *                             if (RitualRequirements.noConvertEntity(recipe.getEntityToConvert(), this.worldPosition, this.level)){
         *                                 ++this.convertTime;
         *                                 if (this.getConvertEntity != null){
         *                                     this.getConvertEntity = null;
         *                                 }
         *                                 if (this.convertTime >= totalTime) {
         *                                     this.castingPlayer.displayClientMessage(Component.translatable("info.goety.ritual.convert.fail"), true);
         *
         *                                     thisIsWhereReasonIsUsed(()-> Reason.CONVERSION_NOT_IN_RANGE);
         *
         *                                     this.stopRitual(false);
         *                                 }
         *                             } else {
         *                                 this.getConvertEntity = RitualRequirements.getConvertEntity(recipe.getEntityToConvert(), this.worldPosition, this.level);
         *                                 this.convertTime = 0;
         *                             }
         *                         }
         *                     } else {
         *                         if (this.level.getGameTime() % 20 == 0) {
         *                             for (int p = 0; p < 4; ++p) {
         *                                 serverWorld.sendParticles(ModParticleTypes.TOTEM_EFFECT.get(), d0, d1, d2, 0, 0.45, 0.45, 0.45, 1);
         *                             }
         *                         }
         *                     }
         *                 }
         *             } else {
         *                 this.stopRitual(false);
         *             }
         *         } else {
         *             if (currentTime > 0) {
         *
         *                 thisIsWhereReasonIsUsed(()-> Reason.NOT_ENOUGH_SOULS_WITHOUT_MSG);
         *
         *                 this.stopRitual(false);
         *             }
         *         }
         *         this.level.setBlock(this.getBlockPos(), this.getBlockState().setValue(DarkAltarBlock.LIT, flag), 3);
         *     }
         *     }</pre></blockquote>
         */
        public enum Reason {
            INVALID_RITUAL_RECIPE,
            NOT_ENOUGH_SOULS_WITH_MSG,
            NOT_ENOUGH_SOULS_WITHOUT_MSG,
            ENCHANTMENT_RITUAL_NOT_ENOUGH_EXP,
            CANNOT_CONSUME_INGREDIENTS,
            CONVERSION_NOT_IN_RANGE_WHEN_WILL_FINISHED,
            CONVERSION_NOT_IN_RANGE,
            INVALID_STRUCTURE,
            FINISHED_ENCHANTMENT,
            FINISHED_NORMAL,
            FINISHED_CONVERSION
        }
        public StopRitualEvent(LevelAccessor level, BlockPos pos, BlockState state, DarkAltarBlockEntity altarBlockEntity, boolean isBefore, boolean isFinished, Reason reason) {
            super(level, pos, state, altarBlockEntity);
            this.isBefore = isBefore;
            this.isFinished = isFinished;
            this.reason = reason;
        }

        private final boolean isBefore;
        private final boolean isFinished;
        private final Reason reason;
        public boolean isFinished() {
            return isFinished;
        }
        public boolean isTypeBefore() {
            return isBefore;
        }
        public boolean isTypeAfter() {
            return !isTypeBefore();
        }

        public Reason getReason() {
            return reason;
        }

        @Override
        public boolean isCancelable() {
            return isTypeBefore();
        }
    }
    @Cancelable
    public static class StartRitualEvent extends DarkAltarEvent {
        private final Player castingPlayer;
        private final ItemStack activationItem;
        private final RitualRecipe ritualRecipe;

        public StartRitualEvent(LevelAccessor level, BlockPos pos, BlockState state, DarkAltarBlockEntity altarBlockEntity, Player castingPlayer, ItemStack activationItem, RitualRecipe ritualRecipe) {
            super(level, pos, state, altarBlockEntity);
            this.castingPlayer = castingPlayer;
            this.activationItem = activationItem;
            this.ritualRecipe = ritualRecipe;
        }

        public Player getCastingPlayer() {
            return castingPlayer;
        }

        public ItemStack getActivationItem() {
            return activationItem;
        }

        public RitualRecipe getRitualRecipe() {
            return ritualRecipe;
        }
    }
}
