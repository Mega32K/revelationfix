package com.mega.revelationfix.common.data.ritual.requirement.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.mega.revelationfix.util.RevelationFixMixinPlugin;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class NormalBlockRequirement extends BlockRequirement {
    private Pair<Block, TagKey<Block>> ingredient;
    @Nullable
    private Map<String, String> blockStateMap = null;
    @Override
    protected void compileSelfData(JsonObject jsonObject) {
        String originalString = GsonHelper.getAsString(jsonObject, "block", "minecraft:bedrock");
        if (originalString.startsWith("#")) {
            ingredient = Pair.of(null, BlockTags.create(new ResourceLocation(originalString.replace("#", ""))));
        } else ingredient = Pair.of(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(originalString)), null);
        if (jsonObject.has("state")) {
            ImmutableMap.Builder<String, String> b = deserialize(jsonObject.get("state"));
            if (b != null)
                blockStateMap = b.build();
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
        if (check) {
            if (blockStateMap != null && !blockStateMap.isEmpty()) {
                //将blockstate存在的属性转化为 属性名->Collection属性 映射
                Multimap<String, Property<? extends Comparable<?>>> propertyMultimap = propertyMultimap(state.getProperties());
                for (var pro : blockStateMap.entrySet()) {
                    Collection<Property<? extends Comparable<?>>> keyLists = propertyMultimap.get(pro.getKey());
                    //如果从json获取到的属性名，这个映射包含
                    if (!keyLists.isEmpty()) {
                        //就遍历映射的value:属性列表来判断是否属性值一致
                        for (Property<? extends Comparable<?>> property : keyLists) {
                            Optional<? extends Comparable<?>> valueFromString = property.getValue(pro.getValue());
                            if (valueFromString.isPresent()) {
                                if (!valueFromString.get().equals(state.getValue(property))) {
                                    check = false;
                                }
                            }
                        }
                    }
                }
            }
        }
        return check;
    }
    public static ImmutableMap.Builder<String, String> deserialize(JsonElement json) throws JsonParseException {
        try {
            JsonObject jo = json.getAsJsonObject();
            ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
            for (var entry : jo.asMap().entrySet()) {
                if (entry.getValue() instanceof JsonPrimitive jp)
                    builder.put(entry.getKey(), jp.getAsString());
            }
            return builder;
        } catch (JsonParseException var5) {
            RevelationFixMixinPlugin.LOGGER.debug("Failed to parse block state: {}", json, var5);
            return null;
        }
    }
    public static Multimap<String, Property<?>> propertyMultimap(Collection<Property<?>> properties) {
        ImmutableMultimap.Builder<String, Property<?>> builder = ImmutableMultimap.builder();
        for (Property<?> property : properties) {
            builder.put(property.getName(), property);
        }
        return builder.build();
    }
}
