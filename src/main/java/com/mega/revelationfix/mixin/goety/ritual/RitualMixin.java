package com.mega.revelationfix.mixin.goety.ritual;

import com.Polarice3.Goety.common.blocks.entities.PedestalBlockEntity;
import com.Polarice3.Goety.common.crafting.RitualRecipe;
import com.Polarice3.Goety.common.ritual.Ritual;
import com.Polarice3.Goety.utils.ItemHelper;
import com.mega.revelationfix.safe.RitualRecipeInterface;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

/**
 * <h2>需要每次同步Goety的更新</h2>
 */
@Mixin(Ritual.class)
public abstract class RitualMixin {
    @Shadow(remap = false) public RitualRecipe recipe;

    /**
     * @author MegaDarkness
     * @reason 无法使用mixin的时候不使用static修饰符，无法获取recipe实例，必须overwrite
     */
    @Overwrite(remap = false)
    public boolean consumeAdditionalIngredient(Level world, BlockPos darkAltarPos,
                                               List<PedestalBlockEntity> pedestals,
                                               Ingredient ingredient, List<ItemStack> consumedIngredients) {
        for (PedestalBlockEntity pedestal : pedestals) {
            if (pedestal.itemStackHandler.map(handler -> {
                ItemStack stack = handler.extractItem(0, 1, true);
                if (ingredient.test(stack)) {
                    ItemStack extracted = handler.extractItem(0, 1, false);

                    consumedIngredients.add(extracted);

                    if (extracted.getItem() instanceof BucketItem bucketItem && !bucketItem.getFluid().defaultFluidState().isEmpty()){
                        ItemHelper.addItemEntity(world, pedestal.getBlockPos().above(), new ItemStack(Items.BUCKET));
                        world.playSound(null, pedestal.getBlockPos(), SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS,
                                0.7F, 0.7F);
                    } else if (extracted.hasCraftingRemainingItem()){
                        ItemHelper.addItemEntity(world, pedestal.getBlockPos().above(), extracted.getCraftingRemainingItem());
                    } else {
                        int indexOfIngredient = recipe.getIngredients().indexOf(ingredient);
                        if (indexOfIngredient >= 0) {
                            RitualRecipeInterface rItf = RitualRecipeInterface.of(recipe);
                            if (rItf.inputsToRemaining().contains(indexOfIngredient)) {
                                ItemStack itemStack = extracted.copy();
                                if (rItf.remaining().containsKey(indexOfIngredient)) {
                                    itemStack = rItf.remaining().get(indexOfIngredient).copy();
                                }
                                ItemHelper.addItemEntity(world, pedestal.getBlockPos().above(), itemStack);
                            }
                        }
                    }

                    handler.setStackInSlot(0, ItemStack.EMPTY);

                    world.playSound(null, pedestal.getBlockPos(), SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS,
                            0.7F, 0.7F);
                    return true;
                }
                return false;
            }).orElse(false))
                return true;

        }
        return false;
    }
}
