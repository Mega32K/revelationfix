package com.mega.revelationfix.common.data.ritual.requirement.block;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mega.revelationfix.util.RevelationFixMixinPlugin;
import com.mojang.serialization.JsonOps;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraftforge.client.model.lighting.ForgeModelBlockRenderer;
import net.minecraftforge.registries.ForgeRegistries;

public class NormalBlockRequirement extends BlockRequirement {
    private Pair<Block, TagKey<Block>> ingredient;
    private BlockState blockState;
    @Override
    protected void compileSelfData(JsonObject jsonObject) {
        String originalString = GsonHelper.getAsString(jsonObject, "block", "minecraft:bedrock");
        if (originalString.startsWith("#")) {
            ingredient = Pair.of(null, BlockTags.create(new ResourceLocation(originalString.replace("#", ""))));
        } else ingredient = Pair.of(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(originalString)), null);
        if (jsonObject.has("state")) {
            blockState = deserialize(jsonObject.get("state"));
        }
    }

    @Override
    public boolean canUse(Level level, BlockPos blockPos, BlockState state) {
        boolean check = false;
        if (ingredient != null) {
            if (ingredient.first() != null)
                check = state.is(ingredient.first());
            else if (ingredient.right() != null)
                check = state.is(ingredient.right());
        }

        if (blockState != null) {
            for (var pro : blockState.getProperties()) {
                if (pro instanceof DirectionProperty)
                    continue;
                if (!state.hasProperty(pro) || !state.getValue(pro).equals(blockState.getValue(pro))) {
                    check = false;
                }
            }
        }
        return check;
    }
    public static BlockState deserialize(JsonElement json) throws JsonParseException {
        try {
            return BlockState.CODEC.decode(JsonOps.INSTANCE, json).result().map(com.mojang.datafixers.util.Pair::getFirst).orElseThrow(() -> new JsonParseException("Missing block state data in json element"));
        } catch (JsonParseException var5) {
            RevelationFixMixinPlugin.LOGGER.debug("Failed to parse block state: {}", json, var5);
            return null;
        }
    }
}
