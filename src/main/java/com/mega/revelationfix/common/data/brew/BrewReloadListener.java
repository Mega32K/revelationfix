package com.mega.revelationfix.common.data.brew;

import com.google.gson.*;
import com.mega.revelationfix.api.event.register.CustomBrewRegisterEvent;
import com.mega.revelationfix.common.compat.SafeClass;
import com.mega.revelationfix.common.compat.kjs.KjsSafeClass;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class BrewReloadListener extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();

    public BrewReloadListener() {
        super(GSON, "goety_brew/brew");
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> object, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profiler) {
        BrewData.clearData();
        BrewData.Catalysts.initReaders();
        for (var entry : object.entrySet()) {
            ResourceLocation key = entry.getKey();
            JsonElement element = entry.getValue();
            if (shouldLoad(element)) {
                try {
                    BrewData data = new BrewData();
                    JsonObject jsonObject = element.getAsJsonObject();
                    data.pluginName = jsonObject.get("plugin").getAsString();
                    JsonObject expanded_jo = jsonObject.get("expanded").getAsJsonObject();
                    label_capacity:
                    {
                        if (!expanded_jo.has("capacity") || !expanded_jo.get("capacity").isJsonArray())
                            break label_capacity;
                        for (JsonElement jsonElement : expanded_jo.get("capacity").getAsJsonArray().asList()) {
                            if (jsonElement instanceof JsonObject cap) {
                                try {
                                    data.capacities.add(BrewData.Capacity.readFromJO(cap));
                                } catch (Throwable throwable) {
                                    throwable.printStackTrace();
                                }
                            }
                        }
                    }
                    label_catalysts:
                    {
                        if (!expanded_jo.has("catalysts") || !expanded_jo.get("catalysts").isJsonArray())
                            break label_catalysts;
                        for (JsonElement jsonElement : expanded_jo.get("catalysts").getAsJsonArray().asList()) {
                            if (jsonElement instanceof JsonObject currentCatalysts_jo) {
                                try {
                                    if (currentCatalysts_jo.has("type")) {
                                        String type = currentCatalysts_jo.get("type").getAsString();
                                        if (BrewData.Catalysts.readers.containsKey(type)) {
                                            data.catalysts.add(BrewData.Catalysts.readers.get(type).apply(currentCatalysts_jo));
                                        }
                                    }
                                } catch (Throwable throwable) {
                                    throwable.printStackTrace();
                                }
                            }
                        }
                    }
                    label_augmentation:
                    {
                        if (!expanded_jo.has("augmentation") || !expanded_jo.get("augmentation").isJsonArray()) {
                            break label_augmentation;
                        }
                        for (JsonElement jsonElement : expanded_jo.get("augmentation").getAsJsonArray().asList()) {
                            if (jsonElement instanceof JsonObject augmentation) {
                                try {
                                    data.augmentations.add(BrewData.Augmentation.readFromJO(augmentation));
                                } catch (Throwable throwable) {
                                    throwable.printStackTrace();
                                }
                            }
                        }
                    }
                    BrewData.register(data.pluginName, data);
                } catch (Throwable throwable) {
                    CrashReport report = CrashReport.forThrowable(throwable, "Reading Goety Brew Data");
                    CrashReportCategory crashReportCategory = report.addCategory("Data");
                    crashReportCategory.setDetail("Location", key);
                    new ReportedException(report).printStackTrace();
                }
            }
        }
        MinecraftForge.EVENT_BUS.post(new CustomBrewRegisterEvent(CustomBrewRegisterEvent.Phase.DATA_LOAD));
        if (SafeClass.isKJSLoaded()) {
            KjsSafeClass.postBrewEvent_0();
        }
    }

    protected boolean shouldLoad(JsonElement json) {
        if (json.isJsonArray()) {
            JsonArray arr = json.getAsJsonArray();
            if (arr.size() > 0) {
                json = arr.get(0);
            }
        }

        if (!json.isJsonObject()) {
            return true;
        } else {
            JsonObject jsonObject = json.getAsJsonObject();
            return !jsonObject.has("conditions") || CraftingHelper.processConditions(GsonHelper.getAsJsonArray(jsonObject, "conditions"), ICondition.IContext.EMPTY);
        }
    }
}
