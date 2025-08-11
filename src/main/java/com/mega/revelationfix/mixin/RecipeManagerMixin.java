package com.mega.revelationfix.mixin;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.mega.endinglib.util.annotation.DeprecatedMixin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;

@Mixin(RecipeManager.class)
@DeprecatedMixin
public class RecipeManagerMixin {
    @Inject(method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V", at = @At(value = "INVOKE", target = "Ljava/util/Map;entrySet()Ljava/util/Set;", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD)
    private void dynamicBrewingRecipe(Map<ResourceLocation, JsonElement> p_44037_, ResourceManager p_44038_, ProfilerFiller p_44039_, CallbackInfo ci, Map<RecipeType<?>, ImmutableMap.Builder<ResourceLocation, Recipe<?>>> map, ImmutableMap.Builder<ResourceLocation, Recipe<?>> builder) {

    }
}
