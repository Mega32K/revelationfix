package com.mega.revelationfix.common.data.ritual;

import com.google.gson.*;
import com.mega.revelationfix.api.event.register.CustomBrewRegisterEvent;
import com.mega.revelationfix.api.event.register.CustomRitualRegisterEvent;
import com.mega.revelationfix.common.compat.SafeClass;
import com.mega.revelationfix.common.compat.kjs.KjsSafeClass;
import com.mega.revelationfix.common.data.brew.BrewData;
import com.mega.revelationfix.common.data.ritual.requirement.DimensionTypeRequirement;
import com.mega.revelationfix.common.data.ritual.requirement.PositionRequirement;
import com.mega.revelationfix.common.data.ritual.requirement.Requirement;
import com.mega.revelationfix.common.data.ritual.requirement.TimeRequirement;
import com.mega.revelationfix.common.data.ritual.requirement.block.BlockRequirement;
import com.mega.revelationfix.common.data.ritual.requirement.entity.EntityRequirement;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class RitualReloadListener extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();
    public RitualReloadListener() {
        super(GSON, "goety_ritual/ritual");
    }
    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> object, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profiler) {
        RitualDataManager.clearData();
        for (var entry : object.entrySet()) {
            ResourceLocation key = entry.getKey();
            JsonElement element = entry.getValue();
            if (shouldLoad(element) && element instanceof JsonObject main) {
                try {

                    String pluginName = main.get("ritual").getAsString();
                    String iconItem = GsonHelper.getAsString(main, "iconItem", "minecraft:air");
                    int[] range = new int[] {8, 8, 8};
                    if (main.get("range") instanceof JsonArray array) {
                        if (array.size() >= 3) {
                            range[0] = array.get(0).getAsInt();
                            range[1] = array.get(1).getAsInt();
                            range[2] = array.get(2).getAsInt();
                        }
                    }
                    RitualData ritualData = new RitualData(pluginName);
                    ritualData.setIconItemKey(iconItem);
                    ritualData.setRange(range);

                    if (main.get("requirements") instanceof JsonObject allRequirements) {
                        if (allRequirements.get(RitualData.BLOCKS) instanceof JsonArray blockRequirements) {
                            Collection<Requirement> requirements = new ArrayList<>();
                            for (var blockElement : blockRequirements) {
                                BlockRequirement requirement = BlockRequirement.createFromJson(blockElement);
                                if (requirement != null)
                                    requirements.add(requirement);
                            }
                            ritualData.setRequirements(RitualData.BLOCKS, requirements);
                        }
                        if (allRequirements.get(RitualData.ENTITIES) instanceof JsonArray entityRequirements) {
                            Collection<Requirement> requirements = new ArrayList<>();
                            for (var entityElement : entityRequirements) {
                                EntityRequirement requirement = EntityRequirement.createFromJson(entityElement);
                                if (requirement != null)
                                    requirements.add(requirement);
                            }
                            ritualData.setRequirements(RitualData.ENTITIES, requirements);
                        }
                        if (allRequirements.get(RitualData.DIMENSION) instanceof JsonObject dimensionJO) {
                            ritualData.setRequirements(RitualData.DIMENSION, DimensionTypeRequirement.createFromJson(dimensionJO));
                        }
                        if (allRequirements.get(RitualData.POSITION) instanceof JsonObject dimensionJO) {
                            ritualData.setRequirements(RitualData.POSITION, PositionRequirement.createFromJson(dimensionJO));
                        }
                        if (allRequirements.get(RitualData.TIME) instanceof JsonObject dimensionJO) {
                            ritualData.setRequirements(RitualData.TIME, TimeRequirement.createFromJson(dimensionJO));
                        }
                    }
                    RitualDataManager.register(pluginName, ritualData);
                } catch (Throwable throwable) {
                    CrashReport report = CrashReport.forThrowable(throwable, "Reading Goety Ritual Data");
                    CrashReportCategory crashReportCategory = report.addCategory("Data");
                    crashReportCategory.setDetail("Location", key);
                    new ReportedException(report).printStackTrace();
                }
            }
        }

        MinecraftForge.EVENT_BUS.post(new CustomRitualRegisterEvent());
        if (SafeClass.isKJSLoaded()) {
            KjsSafeClass.postRitualEvent_0();
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
