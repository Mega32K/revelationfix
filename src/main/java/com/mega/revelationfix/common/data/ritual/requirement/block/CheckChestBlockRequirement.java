package com.mega.revelationfix.common.data.ritual.requirement.block;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

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
                    Pair<Item, TagKey<Item>> ingredient;
                    String originalString = GsonHelper.getAsString(elementLoop, "item", "minecraft:air");
                    if (originalString.startsWith("#")) {
                        ingredient = Pair.of(null, ItemTags.create(new ResourceLocation(originalString.replace("#", ""))));
                    } else ingredient = Pair.of(ForgeRegistries.ITEMS.getValue(new ResourceLocation(originalString)), null);
                    this.containers.add(new ContainerIngredient(minCount, ingredient));
                }
            }
        }
    }

    @Override
    public boolean canUse(Level level, BlockPos blockPos, BlockState state) {
        int match = 0;
        if (level.getBlockEntity(blockPos) instanceof BaseContainerBlockEntity blockEntity) {
            for (ContainerIngredient containerIngredient : containers) {
                for (int i=0;i<blockEntity.getContainerSize();i++) {
                    if (containerIngredient.is(blockEntity.getItem(i))) {
                        match++;
                    }
                }
            }
        }
        return match >= containers.size();
    }
    record ContainerIngredient(int minCount, Pair<Item, TagKey<Item>> ingredient) {
        boolean is(ItemStack stack) {
            if (minCount <= 0)
                return true;
            if (stack.isEmpty())
                return false;
            else {
                if (ingredient.first() != null) {
                    if (ingredient.first() == Items.AIR)
                        return true;
                    else return stack.is(ingredient.first()) && stack.getCount() >= minCount;
                } else if (ingredient.right() != null) {
                    return stack.is(ingredient.right()) && stack.getCount() >= minCount;
                }
            }
            return false;
        }
    }
}
