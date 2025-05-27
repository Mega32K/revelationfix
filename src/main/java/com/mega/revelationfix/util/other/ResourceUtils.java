package com.mega.revelationfix.util.other;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.server.packs.resources.ResourceProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ResourceUtils {

    public ResourceUtils() {
    }

    public static InputStream getResourceAsStream(ResourceLocation resource) throws IOException {
        return getResource(resource).open();
    }

    public static ReloadableResourceManager getResourceManager() {
        return (ReloadableResourceManager) Minecraft.getInstance().getResourceManager();
    }

    public static Resource getResource(String location) throws IOException {
        return getResource(new ResourceLocation(location));
    }

    public static Resource getResource(ResourceLocation location) throws IOException {
        return getResourceManager().getResourceOrThrow(location);
    }

    public static void registerReloadListener(ResourceManagerReloadListener reloadListener) {
        getResourceManager().registerReloadListener(reloadListener);
    }

    public static List<String> loadResource(ResourceProvider resourceProvider, ResourceLocation loc) {
        try {
            Resource resource = resourceProvider.getResourceOrThrow(loc);
            BufferedReader reader = resource.openAsReader();

            List var4;
            try {
                var4 = reader.lines().toList();
            } catch (Throwable var7) {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Throwable var6) {
                        var7.addSuppressed(var6);
                    }
                }

                throw var7;
            }

            if (reader != null) {
                reader.close();
            }

            return var4;
        } catch (IOException var8) {
            throw new RuntimeException("Failed to load MTL file: " + loc, var8);
        }
    }
}
