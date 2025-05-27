package com.mega.revelationfix.common.data.brew;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mega.revelationfix.mixin.goety.BrewCauldronBlockEntityMixin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class BrewReloadListener extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();
    public BrewReloadListener() {
        super(GSON, "goety_brew/brew");
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> object, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profiler) {

    }
}
