package com.mega.revelationfix.common.compat.patchouli;

import com.Polarice3.Goety.common.effects.brew.BrewEffects;
import com.google.gson.JsonElement;
import com.mega.revelationfix.common.data.brew.BrewData;
import com.mega.revelationfix.util.RevelationFixMixinPlugin;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PatchouliBrewEntriesInjector {
    private static final Lock LOCK = new ReentrantLock();
    public static final JsonModify[] modifyInvokers = new JsonModify[]{PatchouliBrewEntriesInjector::modifyCapacityJson, PatchouliBrewEntriesInjector::modifyCatalystsJson, PatchouliBrewEntriesInjector::modifyAugmentationJson};

    public static int getCategoryIndex(ResourceLocation file) {
        if (!file.getNamespace().equals("goety"))
            return -1;
        String pate = file.getPath();
        if (pate.endsWith("brewing/capacity.json")) {
            return 0;
        } else if (pate.endsWith("brewing/catalysts.json"))
            return 1;
        else if (pate.endsWith("brewing/augmentation.json"))
            return 2;
        return -1;
    }

    public static JsonElement modifyCapacityJson(JsonElement src) {
        LOCK.lock();
        try {
            BrewData.Capacity.injectPatchouliJson(src);
        } catch (Throwable throwable) {
            RevelationFixMixinPlugin.LOGGER.throwing(throwable);
        }
        BrewEffects.INSTANCE = new BrewEffects();
        BrewData.reRegister();
        LOCK.unlock();
        return src;
    }

    public static JsonElement modifyAugmentationJson(JsonElement src) {
        LOCK.lock();
        try {
            BrewData.Augmentation.injectPatchouliJson(src);
        } catch (Throwable throwable) {
            RevelationFixMixinPlugin.LOGGER.throwing(throwable);
        }
        LOCK.unlock();
        BrewEffects.INSTANCE = new BrewEffects();
        BrewData.reRegister();
        return src;
    }

    public static JsonElement modifyCatalystsJson(JsonElement src) {
        LOCK.lock();
        try {
            BrewData.Catalysts.injectPatchouliJson(src);
        } catch (Throwable throwable) {
            RevelationFixMixinPlugin.LOGGER.throwing(throwable);
        }
        LOCK.unlock();
        BrewEffects.INSTANCE = new BrewEffects();
        BrewData.reRegister();
        return src;
    }

    public interface JsonModify {
        JsonElement get(JsonElement src);
    }
}
