package com.mega.revelationfix.common.data.ritual.requirement.block;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.crafting.CraftingHelper;

import java.util.HashSet;
import java.util.Set;

public class CheckChestBlockRequirement extends BlockRequirement {
    public Set<ContainerIngredient> containers = new HashSet<>();

    @Override
    protected void compileSelfData(JsonObject jsonObject) {
        JsonArray jsonArray = jsonObject.getAsJsonArray("container");
        if (!jsonArray.isEmpty()) {
            for (JsonElement jElement : jsonArray) {
                if (jElement instanceof JsonObject elementLoop) {
                    int minCount = GsonHelper.getAsInt(elementLoop, "minCount", 1);
                    this.containers.add(new ContainerIngredient(minCount, CraftingHelper.getIngredient(elementLoop.get("slot"), true)));
                }
            }
        }
    }

    @Override
    public boolean canUse(Level level, BlockPos blockPos, BlockState state) {
        int match = 0;
        if (level.getBlockEntity(blockPos) instanceof BaseContainerBlockEntity blockEntity) {
            for (ContainerIngredient containerIngredient : containers) {
                for (int i = 0; i < blockEntity.getContainerSize(); i++) {
                    if (containerIngredient.is(blockEntity.getItem(i))) {
                        match++;
                    }
                }
            }
        }
        return match >= containers.size();
    }

    record ContainerIngredient(int minCount, Ingredient ingredient) {
        boolean is(ItemStack stack) {
            if (minCount <= 0)
                return true;
            else if (ingredient != null) {
                return ingredient.test(stack);
            }
            return false;
        }
    }
}
