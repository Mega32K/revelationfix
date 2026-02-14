package com.mega.revelationfix.mixin.goety.ritual;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.blocks.entities.DarkAltarBlockEntity;
import com.Polarice3.Goety.common.blocks.entities.PedestalBlockEntity;
import com.Polarice3.Goety.common.crafting.RitualRecipe;
import com.Polarice3.Goety.common.items.ModItems;
import com.Polarice3.Goety.common.ritual.CraftItemRitual;
import com.Polarice3.Goety.common.ritual.Ritual;
import com.Polarice3.Goety.utils.ItemHelper;
import com.mega.revelationfix.common.init.GRItems;
import com.mega.revelationfix.common.ritual.ModRitualTypes;
import com.mega.revelationfix.safe.RitualRecipeInterface;
import com.mega.revelationfix.safe.TheEndRitualItemContext;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(CraftItemRitual.class)
public abstract class CraftRitualMixin extends Ritual  {

    @Unique
    private ItemStack tempActivation;

    public CraftRitualMixin(RitualRecipe recipe) {
        super(recipe);
    }

    @Unique
    private static boolean isValidEnderChest(PlayerEnderChestContainer chestContainer) {
        return chestContainer.hasAnyMatching(stack -> stack.is(GRItems.THE_END_PUZZLES) && stack.is(GRItems.PUZZLE_ITEM.get()))
                && chestContainer.hasAnyMatching(stack -> stack.is(GRItems.THE_END_PUZZLES2) && stack.is(GRItems.PUZZLE_ITEM.get()))
                && chestContainer.hasAnyMatching(stack -> stack.is(GRItems.THE_END_PUZZLES3) && stack.is(GRItems.PUZZLE_ITEM.get()))
                && chestContainer.hasAnyMatching(stack -> stack.is(GRItems.THE_END_PUZZLES4) && stack.is(GRItems.PUZZLE_ITEM.get()));
    }

    @Override
    public boolean identify(Level world, BlockPos darkAltarPos, Player player, ItemStack activationItem) {
        if (recipe.getCraftType().equals(ModRitualTypes.THE_END) && recipe.getRitualType().equals(Goety.location("craft"))) {
            return super.identify(world, darkAltarPos, player, activationItem);// && isValidEnderChest(player.getEnderChestInventory());
        }
        return super.identify(world, darkAltarPos, player, activationItem);
    }

    @Override
    public boolean isValid(Level world, BlockPos darkAltarPos, DarkAltarBlockEntity tileEntity, Player castingPlayer, ItemStack activationItem, List<Ingredient> remainingAdditionalIngredients) {
        if (recipe.getCraftType().equals(ModRitualTypes.THE_END) && recipe.getRitualType().equals(Goety.location("craft"))) {
            return super.isValid(world, darkAltarPos, tileEntity, castingPlayer, activationItem, remainingAdditionalIngredients);// && castingPlayer != null && isValidEnderChest(castingPlayer.getEnderChestInventory());
        }
        return super.isValid(world, darkAltarPos, tileEntity, castingPlayer, activationItem, remainingAdditionalIngredients);
    }

    @Redirect(method = "finish", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V"))
    private void shrink(ItemStack instance, int p_41775_) {
        instance.shrink(1);
        this.tempActivation = instance;
    }

    @ModifyArg(remap = false, method = "finish",
            at = @At(remap = false, value = "INVOKE", target = "Lnet/minecraftforge/items/IItemHandler;insertItem(ILnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/world/item/ItemStack;"),
            index = 1
    )
    private ItemStack finish(ItemStack stack) {
        if (recipe.getCraftType().equals(ModRitualTypes.THE_END_MAGIC) && recipe.getId().equals(new ResourceLocation("goety_revelation", "anchor_ritual")) && recipe.getRitualType().equals(Goety.location("craft"))) {
            ItemStack itemStack = new ItemStack(TheEndRitualItemContext.THE_END_CRAFT);
            itemStack.getOrCreateTag().putBoolean(GRItems.NBT_CRAFTING, true);
            return itemStack;
        } else {
            if (((RitualRecipeInterface) this.recipe).revelationfix$isKeepingNbt()) {
                stack.getOrCreateTag().merge(tempActivation.getOrCreateTag());
            }
            if (recipe.getRitualType().equals(Goety.location("craft")) && stack.is(GRItems.PUZZLE_ITEM.get())) {
                ItemStack itemStack = new ItemStack(GRItems.PUZZLE_ITEM.get());
                if (recipe.getCraftType().contains("sky")) {
                    itemStack.getOrCreateTag().putBoolean(GRItems.NBT_PUZZLES, true);
                } else if (recipe.getCraftType().contains("expert_nether")) {
                    itemStack.getOrCreateTag().putBoolean(GRItems.NBT_PUZZLES2, true);
                } else if (recipe.getCraftType().contains("necroturgy")) {
                    itemStack.getOrCreateTag().putBoolean(GRItems.NBT_PUZZLES3, true);
                } else {
                    itemStack.getOrCreateTag().putBoolean(GRItems.NBT_PUZZLES4, true);
                }

                return itemStack;
            }
        }
        return stack;
    }

    /**
     * 修复仅参考物品输出
     */
    @ModifyArg(method = "finish", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/items/IItemHandler;insertItem(ILnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/world/item/ItemStack;", remap = false), remap = false)
    private ItemStack result(ItemStack stack) {
        if (stack.is(ModItems.JEI_DUMMY_NONE.get()))
            return ItemStack.EMPTY;
        return stack;
    }

}
